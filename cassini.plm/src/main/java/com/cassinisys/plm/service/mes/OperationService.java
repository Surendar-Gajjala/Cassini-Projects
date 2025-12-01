package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.OperationEvents;
import com.cassinisys.plm.filtering.OperationCriteria;
import com.cassinisys.plm.filtering.OperationPredicateBuilder;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mes.dto.OperationResourceTypeDto;
import com.cassinisys.plm.model.plm.PLMItemAttribute;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.service.cm.DCOService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 29-10-2020.
 */
@Service
public class OperationService implements CrudService<MESOperation, Integer> {

    @Autowired
    private MESOperationRepository mesOperationRepository;
    @Autowired(required = true)
    private OperationPredicateBuilder operationPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private OperationTypeRepository operationTypeRepository;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MESWorkCenterRepository workCenterRepository;
    @Autowired
    private OperationResourcesRepository operationResourcesRepository;
    @Autowired
    private MESObjectTypeRepository mesObjectTypeRepository;
    @Autowired
    private MESOperationRepository operationRepository;
    @Autowired
    private BOPOperationResourceRepository bopOperationResourceRepository;
    @Autowired
    private ObjectFileService objectFileService;

    @Override
    @PreAuthorize("hasPermission(#mesOperation,'create')")
    public MESOperation create(MESOperation mesOperation) {
        MESOperation existOperationNumber = mesOperationRepository.findByNumber(mesOperation.getNumber());
        if (existOperationNumber != null) {
            String message = messageSource.getMessage("operation_number_already_exists", null, "{0} Operation number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existOperationNumber.getNumber());
            throw new CassiniException(result);
        }
        MESOperation existingOperation = mesOperationRepository.findByName(mesOperation.getName());
        if (existingOperation != null) {
            throw new CassiniException("Operation name already exist");
        } else {
            autoNumberService.saveNextNumber(mesOperation.getType().getAutoNumberSource().getId(), mesOperation.getNumber());
            mesOperation = mesOperationRepository.save(mesOperation);
        }
        applicationEventPublisher.publishEvent(new OperationEvents.OperationCreatedEvent(mesOperation));
        return mesOperation;
    }

    @Override
    @PreAuthorize("hasPermission(#mesOperation.id ,'edit')")
    public MESOperation update(MESOperation mesOperation) {
        MESOperation oldOperation = mesOperationRepository.findOne(mesOperation.getId());
        MESOperation existingOperation = mesOperationRepository.findByName(mesOperation.getName());
        if (existingOperation != null && !existingOperation.getId().equals(mesOperation.getId())) {
            throw new CassiniException("OperationId name already exist");
        } else {
            mesOperation = mesOperationRepository.save(mesOperation);
        }
        applicationEventPublisher.publishEvent(new OperationEvents.OperationBasicInfoUpdatedEvent(oldOperation, mesOperation));
        return mesOperation;
    }

    @Override
    @PreAuthorize("hasPermission(#operationId,'delete')")
    public void delete(Integer operationId) {
        mesOperationRepository.delete(operationId);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESOperation get(Integer operationId) {
        MESOperation mesOperation = mesOperationRepository.findOne(operationId);
        if (mesOperation != null) {
        }
        return mesOperation;

    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESOperation> getAll() {
        return mesOperationRepository.findAll();
    }


    public void saveOperationAttributes(List<MESObjectAttribute> attributes) {
        for (MESObjectAttribute attribute : attributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {
                mesObjectAttributeRepository.save(attribute);
            }
        }
    }

    @Transactional
    public MESOperation saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        MESOperation mesOperation = mesOperationRepository.findOne(objectId);
        if (mesOperation != null) {
            PLMItemAttribute itemAttribute = new PLMItemAttribute();
            itemAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, itemAttribute);

        }

        return mesOperation;
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public MESObjectAttribute updateOperationAttribute(MESObjectAttribute attribute) {
        MESObjectAttribute oldValue = mesObjectAttributeRepository.findByObjectAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, MESObjectAttribute.class);
        MESObjectTypeAttribute mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());

        if (mesObjectTypeAttribute.getMeasurement() != null) {
            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(mesObjectTypeAttribute.getMeasurement().getId());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(attribute.getMeasurementUnit().getId());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(mesObjectTypeAttribute.getMeasurement().getId());

            Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
            Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);

            if (!attributeUnitIndex.equals(baseUnitIndex)) {
                attribute.setDoubleValue(attribute.getDoubleValue() / measurementUnit.getConversionFactor());
            } else {
                attribute.setDoubleValue(attribute.getDoubleValue());
            }
        } else {
            attribute.setDoubleValue(attribute.getDoubleValue());
        }
        attribute = mesObjectAttributeRepository.save(attribute);

        MESOperation mesOperation = mesOperationRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new OperationEvents.OperationAttributesUpdatedEvent(mesOperation, oldValue, attribute));
        return attribute;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESOperation> getAllOperationsByPageable(Pageable pageable, OperationCriteria criteria) {
        Predicate predicate = operationPredicateBuilder.build(criteria, QMESOperation.mESOperation);
        Page<MESOperation> operations = mesOperationRepository.findAll(predicate, pageable);
        if (Criteria.isEmpty(criteria.getBop())) {
            operations.getContent().forEach(operation -> {
                ObjectFileDto objectFileDto = objectFileService.getObjectFiles(operation.getId(), PLMObjectType.MESOBJECT,false);
                operation.setOperationFiles(objectFileDto.getObjectFiles());
            });
        }

        return operations;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESOperation> getOperationsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MESOperationType type = operationTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return mesOperationRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MESOperationType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESOperationType> children = operationTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESOperationType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional
    public MESOperationResources createOperationResources(MESOperationResources resource) {
        MESOperationResources existResource = operationResourcesRepository.findByOperationAndResourceAndResourceType(resource.getOperation(), resource.getResource(), resource.getResourceType());
        if (existResource != null) {
            String message = messageSource.getMessage("resource_type_already_exist", null, "{0} resource already exist", LocaleContextHolder.getLocale());
            MESObjectType objectType = mesObjectTypeRepository.findOne(resource.getResourceType());
            String result = MessageFormat.format(message + ".", objectType.getName());
            throw new CassiniException(result);
        } else {
            MESOperation operation = mesOperationRepository.findOne(resource.getOperation());
            resource = operationResourcesRepository.save(resource);
            applicationEventPublisher.publishEvent(new OperationEvents.OperationResourceCreatedEvent(operation, resource));
            return resource;
        }
    }

    @Transactional
    public List<MESOperationResources> getOperationResources(Integer id) {
        List<MESOperationResources> resources = operationResourcesRepository.findByOperation(id);

        for (MESOperationResources resource : resources) {
            resource.setResourceTypeName(mesObjectTypeRepository.findOne(resource.getResourceType()).getName());

        }

        return resources;
    }

    @Transactional
    public List<OperationResourceTypeDto> getOperationResourceList(Integer id, Integer planId) {
        HashMap<String, OperationResourceTypeDto> resourceTypeMap = new HashMap<>();
        List<OperationResourceTypeDto> dtoList = new ArrayList<>();
        List<MESOperationResources> resources = operationResourcesRepository.findByOperation(id);

        for (MESOperationResources resource : resources) {
            OperationResourceTypeDto typeDto = resourceTypeMap.containsKey(resource.getResource()) ? resourceTypeMap.get(resource.getResource()) : new OperationResourceTypeDto();
            typeDto.setResource(resource.getResource());
            resource.setResourceTypeName(mesObjectTypeRepository.findOne(resource.getResourceType()).getName());
            resource.setConsumedQty(bopOperationResourceRepository.getBopOperationResourceTypeObjectCount(planId, id, resource.getResourceType()));
            typeDto.getResourceTypes().add(resource);
            resourceTypeMap.put(resource.getResource(), typeDto);
        }
        dtoList = new ArrayList<>(resourceTypeMap.values());
        return dtoList;
    }

    @Transactional
    public void deleteOperationResource(Integer resourceId) {
        MESOperationResources operationResource = operationResourcesRepository.findOne(resourceId);
        MESOperation operation = operationRepository.findOne(operationResource.getOperation());
        applicationEventPublisher.publishEvent(new OperationEvents.OperationResourceDeletedEvent(operation, operationResource));
        operationResourcesRepository.delete(resourceId);
    }

    @Transactional
    public MESOperationResources updateOperationResources(Integer id, MESOperationResources resources) {
        MESOperationResources oldResources = operationResourcesRepository.findOne(resources.getId());
        applicationEventPublisher.publishEvent(new OperationEvents.OperationResourceUpdatedEvent(oldResources, resources));
        return operationResourcesRepository.save(resources);
    }

}
