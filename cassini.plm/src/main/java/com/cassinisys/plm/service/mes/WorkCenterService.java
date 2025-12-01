package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.AssemblyLineEvents;
import com.cassinisys.plm.event.WorkCenterEvents;
import com.cassinisys.plm.filtering.WorkCenterCriteria;
import com.cassinisys.plm.filtering.WorkCenterPredicateBuilder;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mro.MROAssetRepository;
import com.cassinisys.plm.service.mro.AssetService;
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
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.model.plm.PLMObjectType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 27-10-2020.
 */
@Service
public class WorkCenterService implements CrudService<MESWorkCenter, Integer> {

    @Autowired
    private MESWorkCenterRepository workCenterRepository;
    @Autowired
    private WorkCenterPredicateBuilder workCenterPredicateBuilder;
    @Autowired
    private MESPlantRepository mesPlantRepository;
    @Autowired
    private WorkCenterTypeRepository workCenterTypeRepository;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private AssetService assetService;
    @Autowired
    private MROAssetRepository mroAssetRepository;
    @Autowired
    private MESAssemblyLineRepository assemblyLineRepository;
    @Autowired
    private MESMachineRepository mesMachineRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ObjectFileService objectFileService;
    @Autowired
    private MESWorkCenterOperationRepo workCenterOperationRepo;
    @Autowired
    private MESOperationRepository operationRepository;
    @Autowired
    private PersonRepository personRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#workCenter,'create')")
    public MESWorkCenter create(MESWorkCenter workCenter) {
        MROAsset mroAsset = workCenter.getAsset();
        List<MESObjectAttribute> mesObjectAttributes = workCenter.getMesObjectAttributes();
        MESWorkCenter existWorkCenterNumber = workCenterRepository.findByNumber(workCenter.getNumber());
        if (existWorkCenterNumber != null) {
            String message = messageSource.getMessage("workcenter_number_already_exists", null, "{0} Workcenter number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existWorkCenterNumber.getNumber());
            throw new CassiniException(result);
        }
        autoNumberService.saveNextNumber(workCenter.getType().getAutoNumberSource().getId(), workCenter.getNumber());
        workCenter = workCenterRepository.save(workCenter);
        for (MESObjectAttribute attribute : mesObjectAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(workCenter.getId(), attribute.getId().getAttributeDef()));
                mesObjectAttributeRepository.save(attribute);
            }
        }
        if (workCenter.getRequiresMaintenance() && mroAsset != null) {
            mroAsset.setResource(workCenter.getId());
            assetService.create(mroAsset);
        }
        applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterCreatedEvent(workCenter));
        return workCenter;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#workCenter.id ,'edit')")
    public MESWorkCenter update(MESWorkCenter workCenter) {
        MESWorkCenter oldInspection = JsonUtils.cloneEntity(workCenterRepository.findOne(workCenter.getId()), MESWorkCenter.class);
        applicationEventPublisher.publishEvent(new WorkCenterEvents.WorkCenterBasicInfoUpdatedEvent(oldInspection, workCenter));
        if (oldInspection.getAssemblyLine() != null && workCenter.getAssemblyLine() == null) {
            MESAssemblyLine assemblyLine = assemblyLineRepository.findOne(oldInspection.getAssemblyLine());
            applicationEventPublisher.publishEvent(new AssemblyLineEvents.AssemblyLineWorkCenterDeletedEvent(assemblyLine, workCenter));
        }
        return workCenterRepository.save(workCenter);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        List<MESMachine> machines = mesMachineRepository.findByWorkCenter(id);
        if (machines.size() > 0) {
            String message = messageSource.getMessage("workcenter_already_used_in_machine", null, "Work center already in use", LocaleContextHolder.getLocale());
            throw new CassiniException(message);
        }
        List<MROAsset> assets = mroAssetRepository.findByResource(id);
        if (assets.size() > 0) {
            String message = messageSource.getMessage("workcenter_already_used_in_asset", null, "Work center already in use", LocaleContextHolder.getLocale());
            throw new CassiniException(message);
        }
        workCenterRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESWorkCenter get(Integer id) {
        MESWorkCenter workCenter = workCenterRepository.findOne(id);
        if (workCenter.getAssemblyLine() != null) {
            workCenter.setAssemblyLineName(assemblyLineRepository.findOne(workCenter.getAssemblyLine()).getName());
        }
        return workCenter;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESWorkCenter> findMultiple(List<Integer> ids) {
        return workCenterRepository.findByIdIn(ids);
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESWorkCenter> getAll() {
        return workCenterRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESWorkCenter> getPageableWorkCenters(Pageable pageable, WorkCenterCriteria criteria) {
        Predicate predicate = workCenterPredicateBuilder.build(criteria, QMESWorkCenter.mESWorkCenter);
        Page<MESWorkCenter> workCenters = workCenterRepository.findAll(predicate, pageable);
        workCenters.getContent().forEach(workCenter -> {
            if (workCenter.getPlant() != null) {
                workCenter.setPlantName(mesPlantRepository.findOne(workCenter.getPlant()).getName());
            }
            if (workCenter.getAssemblyLine() != null) {
                workCenter.setAssemblyLineName(assemblyLineRepository.findOne(workCenter.getAssemblyLine()).getName());
            }

            ObjectFileDto objectFileDto = objectFileService.getObjectFiles(workCenter.getId(), PLMObjectType.MESOBJECT,false);
            workCenter.setWorkcenterFiles(objectFileDto.getObjectFiles());
        });
        return workCenters;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESWorkCenter> getWorkCentersByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MESWorkCenterType type = workCenterTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return workCenterRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MESWorkCenterType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESWorkCenterType> children = workCenterTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESWorkCenterType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional
    public List<MESWorkCenter> updateMultipleAssemblyLineWorkCenters(Integer assemblyLineId, List<MESWorkCenter> workCenters) {
        MESAssemblyLine assemblyLine = assemblyLineRepository.findOne(assemblyLineId);
        workCenters.forEach(mesWorkCenter -> {
            mesWorkCenter.setAssemblyLine(assemblyLineId);
        });
        applicationEventPublisher.publishEvent(new AssemblyLineEvents.AssemblyLineWorkCentersAddedEvent(assemblyLine, workCenters));
        return workCenterRepository.save(workCenters);
    }

    @Transactional
    public List<MESWorkCenterOperation> createWorkCenterOperations(Integer workCenterId, List<MESOperation> operations) {
        List<MESWorkCenterOperation> workCenterOperations = new ArrayList<>();
        operations.forEach(operation -> {
            MESWorkCenterOperation workCenterOperation = new MESWorkCenterOperation();
            workCenterOperation.setOperation(operation.getId());
            workCenterOperation.setWorkCenter(workCenterId);
            workCenterOperations.add(workCenterOperation);
        });
        return workCenterOperationRepo.save(workCenterOperations);
    }

    @Transactional
    public List<MESWorkCenterOperation> getAllWorkCenterOperations(Integer workCenterId) {
        List<MESWorkCenterOperation> workCenterOperations = workCenterOperationRepo.findByWorkCenter(workCenterId);
        workCenterOperations.forEach(workCenterOperation -> {
            MESOperation operation = operationRepository.findOne(workCenterOperation.getOperation());
            workCenterOperation.setOperationObject(operation);
            workCenterOperation.setModifiedName(personRepository.findOne(operation.getModifiedBy()).getFullName());
        });
        return workCenterOperations;
    }

    @Transactional
    public void deleteWorkCenterOperations(Integer id) {
        workCenterOperationRepo.delete(id);
    }
    
}
