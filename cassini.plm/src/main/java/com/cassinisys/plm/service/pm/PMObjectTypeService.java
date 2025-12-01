package com.cassinisys.plm.service.pm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.cassinisys.platform.service.core.TypeSystem;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.event.ProgramEvents;
import com.cassinisys.plm.event.ProjectEvents;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PMObjectType;
import com.cassinisys.plm.model.pm.PMObjectTypeAttribute;
import com.cassinisys.plm.model.pm.PMType;
import com.cassinisys.plm.model.req.PMObjectTypesDto;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.service.classification.ClassificationTypeService;
import com.cassinisys.plm.service.plm.ItemTypeService;
import com.cassinisys.plm.service.req.RequirementTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by GSR on 03-06-2020.
 */
@Service
public class PMObjectTypeService implements CrudService<PMObjectType, Integer>,
        TypeSystem, ClassificationTypeService<PMObjectType, PMObjectTypeAttribute> {

    @Autowired
    private PMObjectTypeRepository pmObjectTypeRepository;
    @Autowired
    private PMObjectTypeAttributeRepository pmObjectTypeAttributeRepository;
    @Autowired
    private RequirementTypeService requirementTypeService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private PMObjectTypeService pmObjectTypeService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    @Autowired
    private ObjectRepository objectRepository;

    @PostConstruct
    public void InitPmTypeService() {
        objectTypeService.registerTypeSystem("pmType", new PMObjectTypeService());
    }

    @Override
    @PreAuthorize("hasPermission(#pmObjectType,'create')")
    public PMObjectType create(PMObjectType pmObjectType) {
        PMObjectType pmObjectType1 = pmObjectTypeRepository.save(pmObjectType);
        return pmObjectType1;
    }

    @Override
    @PreAuthorize("hasPermission(#objectType.id ,'edit')")
    public PMObjectType update(PMObjectType objectType) {
        return pmObjectTypeRepository.save(objectType);
    }

    @Override
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        pmObjectTypeRepository.delete(id);
    }


    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PMObjectType get(Integer id) {
        return pmObjectTypeRepository.findOne(id);
    }

    @Override
    public List getTypeAttributes(Integer typeId, Boolean hier) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        pmObjectTypeAttributeRepository = webApplicationContext.getBean(PMObjectTypeAttributeRepository.class);
        List<PMObjectTypeAttribute> pmObjectTypeAttributes = new ArrayList<>();
        if (!hier) {
            pmObjectTypeAttributes = pmObjectTypeAttributeRepository.findByTypeOrderBySequence(typeId);
        } else {
            pmObjectTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return pmObjectTypeAttributes;
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public List<PMObjectType> getAll() {
        return pmObjectTypeRepository.findAll();
    }

    @PreAuthorize("hasPermission(#objectType,'create')")
    public PMObjectType createProjectType(PMObjectType objectType) {
        PMObjectType objectType1 = pmObjectTypeRepository.save(objectType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.PMOBJECTTYPE, objectType1));
        return objectType1;
    }

    @Transactional
    @PreAuthorize("hasPermission(#pmObjectType.id ,'edit')")
    public PMObjectType updateProjectType(Integer id, PMObjectType pmObjectType) {
        PMObjectType oldPmType = pmObjectTypeRepository.findOne(pmObjectType.getId());
        PMObjectType existingPmType;
        if (pmObjectType.getParent() == null) {
            existingPmType = pmObjectTypeRepository.findByParentIsNullAndNameEqualsIgnoreCase(pmObjectType.getName());
        } else {
            existingPmType = pmObjectTypeRepository.findByNameEqualsIgnoreCaseAndParent(pmObjectType.getName(), pmObjectType.getParent());
        }
        if ( existingPmType != null && !pmObjectType.getId().equals(existingPmType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingPmType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.PMOBJECTTYPE, oldPmType, pmObjectType));
        return pmObjectTypeRepository.save(pmObjectType);
    }

    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteProjectType(Integer id) {
        Integer count = getObjectCountByType(id);
        PMObjectType pmObjectType = pmObjectTypeRepository.findOne(id);
        if (count > 0) {
            String message = messageSource.getMessage("type_already_in_use", null, "{0} type already in use", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", pmObjectType.getName());
            throw new CassiniException(result);
        }
        pmObjectTypeRepository.delete(id);
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PMObjectType getProjectType(Integer id) {
        return pmObjectTypeRepository.findOne(id);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PMObjectType> getAllProjectTypes() {
        return pmObjectTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Integer getObjectCountByType(Integer type) {
        Integer count = 0;
        PMObjectType objectType = pmObjectTypeRepository.findOne(type);
        if (objectType.getType().equals(PMType.PROGRAM)) {
            count = programRepository.getProgramCountByType(type);
        } else if (objectType.getType().equals(PMType.PROJECT)) {
            count = projectRepository.getProjectCountByType(type);
        } else if (objectType.getType().equals(PMType.PROJECTACTIVITY)) {
            count = activityRepository.getActivityCountByType(type);
        } else if (objectType.getType().equals(PMType.PROJECTTASK)) {
            count = taskRepository.getTaskCountByType(type);
        }
        return count;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PMObjectType> getMultipleProjectTypes(List<Integer> ids) {
        return pmObjectTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PMObjectType> getTypeTree() {
        List<PMObjectType> types = pmObjectTypeRepository.getObjectTypeSpecifiedOrder();
        for (PMObjectType type : types) {
            visitProjectTypeChildren(type);
        }
        return types;
    }

    @Override
    public List<PMObjectType> getRootTypes() {
        return pmObjectTypeRepository.getObjectTypeSpecifiedOrder();
    }

    @Override
    public List<PMObjectType> getChildren(Integer parent) {
        return pmObjectTypeRepository.findByParentOrderByIdAsc(parent);
    }

    @Override
    public List<PMObjectType> getClassificationTree() {
        List<PMObjectType> types = getRootTypes();
        for (PMObjectType type : types) {
            visitProjectTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PMObjectType> getTypeChildren(Integer id) {
        return pmObjectTypeRepository.findByParentOrderByIdAsc(id);
    }

    private void visitProjectTypeChildren(PMObjectType parent) {
        List<PMObjectType> childrens = getTypeChildren(parent.getId());
        for (PMObjectType child : childrens) {
            visitProjectTypeChildren(child);
        }
        parent.setChildren(childrens);
    }

    @Override
    public PMObjectTypeAttribute createAttribute(PMObjectTypeAttribute attribute) {
        List<PMObjectTypeAttribute> qualityTypeAttributes = pmObjectTypeAttributeRepository.findByTypeOrderBySequence(attribute.getType());
        if (qualityTypeAttributes.size() >= 0) {
            attribute.setSequence(qualityTypeAttributes.size() + 1);
        }
        attribute = pmObjectTypeAttributeRepository.save(attribute);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(PLMObjectType.PMOBJECTTYPE, attribute));
        return attribute;

    }

    @Override
    public PMObjectTypeAttribute updateAttribute(PMObjectTypeAttribute attribute) {
        PMObjectTypeAttribute plantTypeAttribute = pmObjectTypeAttributeRepository.findOne(attribute.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(PLMObjectType.PMOBJECTTYPE, plantTypeAttribute, attribute));
        return pmObjectTypeAttributeRepository.save(attribute);
    }

    @Override
    public void deleteAttribute(Integer id) {
        PMObjectTypeAttribute attribute = pmObjectTypeAttributeRepository.findOne(id);
        List<PMObjectTypeAttribute> requirementObjectTypeAttributes = pmObjectTypeAttributeRepository.findByTypeOrderBySequence(attribute.getType());
        if (requirementObjectTypeAttributes.size() > 0) {
            for (PMObjectTypeAttribute requirementObjectTypeAttribute : requirementObjectTypeAttributes) {
                if (requirementObjectTypeAttribute.getSequence() > attribute.getSequence()) {
                    requirementObjectTypeAttribute.setSequence(requirementObjectTypeAttribute.getSequence() - 1);
                    requirementObjectTypeAttribute = pmObjectTypeAttributeRepository.save(requirementObjectTypeAttribute);
                }
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(PLMObjectType.PMOBJECTTYPE, attribute));
        pmObjectTypeAttributeRepository.delete(id);
    }

    @Override
    public PMObjectTypeAttribute getAttribute(Integer id) {
        return pmObjectTypeAttributeRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PMObjectTypeAttribute> getAttributes(Integer typeId, Boolean hier) {
        List<PMObjectTypeAttribute> pmObjectTypeAttributes = new ArrayList<>();
        if (!hier) {
            pmObjectTypeAttributes = pmObjectTypeAttributeRepository.findByTypeOrderBySequence(typeId);
            pmObjectTypeAttributes.forEach(pmObjectTypeAttribute -> {
                if (pmObjectTypeAttribute.getRefSubType() != null) {
                    pmObjectTypeAttribute.setRefSubTypeName(itemTypeService.getRefSubType(pmObjectTypeAttribute.getRefType().name(), pmObjectTypeAttribute.getRefSubType()));
                }
            });
        } else {
            pmObjectTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return pmObjectTypeAttributes;
    }

    @Transactional(readOnly = true)
    public List<PMObjectTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<PMObjectTypeAttribute> collector = new ArrayList<>();
        List<PMObjectTypeAttribute> atts = pmObjectTypeAttributeRepository.findByTypeOrderBySequence(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<PMObjectTypeAttribute> collector, Integer typeId) {
        PMObjectType objectType = pmObjectTypeRepository.findOne(typeId);
        if (objectType != null) {
            Integer parentType = objectType.getParent();
            if (parentType != null) {
                List<PMObjectTypeAttribute> atts = pmObjectTypeAttributeRepository.findByTypeOrderBySequence(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    public PMObjectTypesDto getAllPMObjectTypesTree() {
        PMObjectTypesDto pmObjectTypesDto = new PMObjectTypesDto();
        pmObjectTypesDto.getPmObjectTypes().addAll(pmObjectTypeService.getTypeTree());
        pmObjectTypesDto.getRequirementDocumentTypes().addAll(requirementTypeService.getReqDocumentTypeTree());
        pmObjectTypesDto.getRequirementTypes().addAll(requirementTypeService.getRequirementTypeTree());
        return pmObjectTypesDto;
    }

    @Override
    public Boolean isSubtypeOf(String s1, Object s2, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        pmObjectTypeRepository = webApplicationContext.getBean(PMObjectTypeRepository.class);
        PMObjectType pmObjectType = (PMObjectType) s2;
        if (subTypeId != null && checkWithId(pmObjectType, subTypeId)) {
            return true;
        }
        if (pmObjectType.getName().equalsIgnoreCase(s1)) {
            return true;
        } else {
            if (pmObjectType.getParent() != null)
                return compareWithParent(pmObjectTypeRepository.findOne(pmObjectType.getParent()), s1);
        }
        return false;
    }

    public Boolean checkWithId(PMObjectType plmItemType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        pmObjectTypeRepository = webApplicationContext.getBean(PMObjectTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmItemType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmItemType.getParent() != null)
                flag = checkWithId(pmObjectTypeRepository.findOne(plmItemType.getParent()), typeId);
        }
        return flag;
    }

    private Boolean compareWithParent(PMObjectType pmObjectType, String s1) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        pmObjectTypeRepository = webApplicationContext.getBean(PMObjectTypeRepository.class);
        Boolean flag = false;
        if (pmObjectType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (pmObjectType.getParent() != null)
                flag = compareWithParent(pmObjectTypeRepository.findOne(pmObjectType.getParent()), s1);
        }
        return flag;
    }


    @Transactional
    public ObjectAttribute updateAttribute(ObjectAttribute objectAttribute) {
        ObjectAttribute existingAttribute = JsonUtils.cloneEntity(objectAttributeRepository.findByObjectIdAndAttributeDefId(objectAttribute.getId().getObjectId(), objectAttribute.getId().getAttributeDef()), ObjectAttribute.class);
        checkNotNull(objectAttribute);
        ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(objectAttribute.getId().getAttributeDef());
        if (objectTypeAttribute.getMeasurement() != null) {
            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(objectTypeAttribute.getMeasurement().getId());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(objectAttribute.getMeasurementUnit().getId());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(objectTypeAttribute.getMeasurement().getId());

            Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
            Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);

            if (!attributeUnitIndex.equals(baseUnitIndex)) {
                objectAttribute.setDoubleValue(objectAttribute.getDoubleValue() / measurementUnit.getConversionFactor());
            } else {
                objectAttribute.setDoubleValue(objectAttribute.getDoubleValue());
            }
        } else {
            objectAttribute.setDoubleValue(objectAttribute.getDoubleValue());
        }

        objectAttribute = objectAttributeRepository.save(objectAttribute);

        CassiniObject cassiniObject = objectRepository.findById(objectAttribute.getId().getObjectId());
        if (cassiniObject.getObjectType().name().equals(PLMObjectType.PROGRAM.name())) {
            applicationEventPublisher
                    .publishEvent(new ProgramEvents.ProgramAttributesUpdatedEvent(cassiniObject.getId(), existingAttribute, objectAttribute));
        }
        if (cassiniObject.getObjectType().name().equals(PLMObjectType.PROJECT.name())) {
            applicationEventPublisher
                    .publishEvent(new ProjectEvents.ProjectAttributesUpdatedEvent(cassiniObject.getId(), existingAttribute, objectAttribute));
        }


        return objectAttribute;
    }


}

