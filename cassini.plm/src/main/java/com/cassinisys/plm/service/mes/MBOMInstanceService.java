package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mes.dto.BOPInstanceOperationPartDto;
import com.cassinisys.plm.model.mes.dto.BOPInstanceRouteDto;
import com.cassinisys.plm.model.mes.dto.BOPOperationResourceDto;
import com.cassinisys.plm.model.mes.dto.BOPRouteDto;
import com.cassinisys.plm.model.mes.dto.InstanceOperationPartDto;
import com.cassinisys.plm.model.mes.dto.MBOMInstanceItemDto;
import com.cassinisys.plm.model.mes.dto.OperationResourceDto;
import com.cassinisys.plm.model.mes.dto.ResourceDto;
import com.cassinisys.plm.model.plm.PLMBom;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.plm.BomRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Service
public class MBOMInstanceService implements CrudService<MESMBOMInstance, Integer> {
    @Autowired
    private MBOMInstanceRepository mbomInstanceRepository;
    @Autowired
    private MBOMInstanceItemRepository mbomInstanceItemRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MESBOMItemRepository mesbomItemRepository;
    @Autowired
    private BomRepository bomRepository;
    @Autowired
    private PhantomRepository phantomRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private BOPInstanceRouteOperationRepository bopInstanceRouteOperationRepository;
    @Autowired
    private MESOperationRepository mesOperationRepository;
    @Autowired
    private BOPInstanceOperationResourceRepository bopInstanceOperationResourceRepository;
    @Autowired
    private BOPInstanceOperationPartRepository bopInstanceOperationPartRepository;
    @Autowired
    private BOPInstanceRepository bopInstanceRepository;
    @Autowired
    private MESMBOMRevisionRepository mesmbomRevisionRepository;
    @Autowired
    private MESMBOMRepository mesmbomRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MESObjectTypeRepository mesObjectTypeRepository;
    @Autowired
    private OperationResourcesRepository operationResourcesRepository;
    @Autowired
    private MESObjectRepository mesObjectRepository;
    @Autowired
    private BOPInstanceOperationInstructionsRepository bopInstanceOperationInstructionsRepository;
    @Autowired
    private BOPInstanceOperationFileRepository bopInstanceOperationFileRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;



    @Override
    @Transactional
    @PreAuthorize("hasPermission(#bominstance,'create')")
    public MESMBOMInstance create(MESMBOMInstance mesmbomInstance) {
        mesmbomInstance = mbomInstanceRepository.save(mesmbomInstance);
        return mesmbomInstance;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mesmbomInstance.id ,'edit')")
    public MESMBOMInstance update(MESMBOMInstance mesmbomInstance) {
        MESMBOMInstance oldMbom = JsonUtils.cloneEntity(mbomInstanceRepository.findOne(mesmbomInstance.getId()), MESMBOMInstance.class);
        MESMBOMInstance existingMbom = mbomInstanceRepository.findBySerialNumber(mesmbomInstance.getSerialNumber());
        if (existingMbom != null && !mesmbomInstance.getId().equals(existingMbom.getId())) {
            String message = messageSource.getMessage("serial_number_already_exists", null, "{0} serial number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMbom.getName());
            throw new CassiniException(result);

        }
        mesmbomInstance = mbomInstanceRepository.save(mesmbomInstance);
//        applicationEventPublisher.publishEvent(new MBOMEvents.MbomBasicInfoUpdatedEvent(oldMbom, mesmbomInstance));
        return mesmbomInstance;
    }

    @Override
    @PreAuthorize("hasPermission(#mbomId,'delete')")
    public void delete(Integer mbomInstanceId) {
        mbomInstanceRepository.delete(mbomInstanceId);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESMBOMInstance get(Integer mbomInstanceId) {
        MESMBOMInstance mbomInstance = mbomInstanceRepository.findOne(mbomInstanceId);
        MESMBOMRevision mesmbomRevision = mesmbomRevisionRepository.findOne(mbomInstance.getMbomRevision());
        MESMBOM mesmbom = mesmbomRepository.findOne(mesmbomRevision.getMaster());
        mbomInstance.setMbomName(mesmbom.getName());
        mbomInstance.setMbomNumber(mesmbom.getNumber());
        mbomInstance.setMbomRevisionName(mesmbomRevision.getRevision());
        return mbomInstance;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMBOMInstance> getAll() {
        return mbomInstanceRepository.findAll();
    }

    @Transactional
    public ItemDetailsDto getMBOMInstanceCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        return detailsDto;
    }

    @Transactional
    public List<MBOMInstanceItemDto> getMBOMInstanceItems(Integer id, Boolean hierarchy) {
        List<MBOMInstanceItemDto> list = new ArrayList<>();
        List<MESMBOMInstanceItem> instanceItems = mbomInstanceItemRepository.findByMbomInstanceAndParentIsNullOrderByIdAsc(id);
        instanceItems.forEach(instanceItem -> {
            MBOMInstanceItemDto mbomItemDto = convertMBomItemToDto(instanceItem);
            list.add(mbomItemDto);
            if (hierarchy) {
                mbomItemDto = visitBomItemChildren(mbomItemDto, instanceItem);
            }
        });
        return list;
    }

    private MBOMInstanceItemDto visitBomItemChildren(MBOMInstanceItemDto dto, MESMBOMInstanceItem item) {
        List<MESMBOMInstanceItem> mesbomItems = mbomInstanceItemRepository.findByParentOrderByIdAsc(item.getId());
        mesbomItems.forEach(mesbomItem -> {
            MBOMInstanceItemDto mbomItemDto = convertMBomItemToDto(mesbomItem);
            dto.getChildren().add(mbomItemDto);
            mbomItemDto = visitBomItemChildren(mbomItemDto, mesbomItem);
        });
        return dto;
    }

    private MBOMInstanceItemDto convertMBomItemToDto(MESMBOMInstanceItem instanceItem) {
        MBOMInstanceItemDto mbomInstanceItemDto = new MBOMInstanceItemDto();
        mbomInstanceItemDto.setId(instanceItem.getId());
        mbomInstanceItemDto.setMbomInstance(instanceItem.getMbomInstance());
        mbomInstanceItemDto.setMbomItem(instanceItem.getMbomItem());
        mbomInstanceItemDto.setParent(instanceItem.getParent());
        MESBOMItem mesbomItem = mesbomItemRepository.findOne(instanceItem.getMbomItem());
        mbomInstanceItemDto.setType(mesbomItem.getType());
        mbomInstanceItemDto.setQuantity(mesbomItem.getQuantity());
        if (mbomInstanceItemDto.getType().equals(MESBomItemType.NORMAL)) {
            PLMBom bom = bomRepository.findOne(mesbomItem.getBomItem());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(bom.getAsReleasedRevision());
            mbomInstanceItemDto.setItemName(bom.getItem().getItemName());
            mbomInstanceItemDto.setItemNumber(bom.getItem().getItemNumber());
            mbomInstanceItemDto.setItemTypeName(bom.getItem().getItemType().getName());
            mbomInstanceItemDto.setRevision(itemRevision.getRevision());
            mbomInstanceItemDto.setAsReleasedRevision(itemRevision.getId());
            mbomInstanceItemDto.setItemRevisionHasBom(itemRevision.getHasBom());
        } else if (mbomInstanceItemDto.getType().equals(MESBomItemType.PHANTOM)) {
            MESPhantom phantom = phantomRepository.findOne(mesbomItem.getPhantom());
            if (phantom != null) {
                mbomInstanceItemDto.setPhantomName(phantom.getName());
                mbomInstanceItemDto.setPhantomNumber(phantom.getNumber());
            }
        }
        Integer count = mbomInstanceItemRepository.getChildCountByParent(instanceItem.getId());
        if (count > 0) {
            mbomInstanceItemDto.setHasBom(true);
        }
        return mbomInstanceItemDto;
    }

    @Transactional(readOnly = true)
    public List<MBOMInstanceItemDto> getMBOMInstanceItemChildren(Integer id, Integer itemId) {
        List<MBOMInstanceItemDto> mbomItemDtos = new LinkedList<>();
        List<MESMBOMInstanceItem> mesbomItems = mbomInstanceItemRepository.findByParentOrderByIdAsc(itemId);
        mesbomItems.forEach(mesbomItem -> {
            MBOMInstanceItemDto mbomItemDto = convertMBomItemToDto(mesbomItem);
            mbomItemDtos.add(mbomItemDto);
        });

        return mbomItemDtos;
    }


    @Transactional(readOnly = true)
    public List<BOPInstanceRouteDto> getMBOMInstanceOperations(Integer id) {
        MESBOPInstance mesbopInstance = bopInstanceRepository.findByMbomInstance(id);
        List<BOPInstanceRouteDto> list = new ArrayList<>();
        List<MESBOPInstanceRouteOperation> plans = bopInstanceRouteOperationRepository.findByBopInstanceAndParentIsNullOrderByIdAsc(mesbopInstance.getId());
        plans.forEach(plan -> {
            BOPInstanceRouteDto planDto = convertBOPPlanToDto(plan);
            planDto = visitBOPPlanChildren(planDto, plan);
            list.add(planDto);
        });
        return list;
    }

    private BOPInstanceRouteDto visitBOPPlanChildren(BOPInstanceRouteDto planDto, MESBOPInstanceRouteOperation plan) {
        List<MESBOPInstanceRouteOperation> childList = bopInstanceRouteOperationRepository.findByParentOrderByIdAsc(plan.getId());
        childList.forEach(child -> {
            BOPInstanceRouteDto bopRouteDto = convertBOPPlanToDto(child);
            bopRouteDto = visitBOPPlanChildren(bopRouteDto, child);
            planDto.getChildren().add(bopRouteDto);
        });
        return planDto;
    }

    private BOPInstanceRouteDto convertBOPPlanToDto(MESBOPInstanceRouteOperation plan) {
        BOPInstanceRouteDto planDto = new BOPInstanceRouteDto();
        planDto.setId(plan.getId());
        planDto.setBopInstance(plan.getBopInstance());
        planDto.setOperation(plan.getOperation());
        planDto.setPhantom(plan.getPhantom());
        planDto.setParent(plan.getParent());
        planDto.setSetupTime(plan.getSetupTime());
        planDto.setCycleTime(plan.getCycleTime());
        planDto.setSequenceNumber(plan.getSequenceNumber());
        planDto.setType(plan.getType());
        if (planDto.getType().equals(BOPPlanTypeEnum.OPERATION)) {
            MESOperation operation = mesOperationRepository.findOne(plan.getOperation());
            planDto.setName(operation.getName());
            planDto.setNumber(operation.getNumber());
            planDto.setTypeName(operation.getType().getName());
            planDto.setDescription(operation.getDescription());
        } else {
            MESPhantom phantom = phantomRepository.findOne(plan.getPhantom());
            planDto.setName(phantom.getName());
            planDto.setNumber(phantom.getNumber());
            planDto.setTypeName("Phantom");
            planDto.setDescription(phantom.getDescription());
        }
        planDto.setModifiedDate(plan.getModifiedDate());
        planDto.setCreatedDate(plan.getCreatedDate());
        planDto.setCount(bopInstanceRouteOperationRepository.getPlanCountByParent(plan.getId()));
        planDto.setResourceCount(bopInstanceOperationResourceRepository.getBopOperationResourcesCountByPlan(plan.getId()));
        planDto.setPartCount(bopInstanceOperationPartRepository.getItemsCountByPlan(plan.getId()));
        return planDto;
    }

    @Transactional(readOnly = true)
    public BOPInstanceRouteDto getMbomInstanceOperationItem(Integer operationId) {
        MESBOPInstanceRouteOperation operation = bopInstanceRouteOperationRepository.findOne(operationId);
        return convertBOPOperationToDto(operation);
    }

    private BOPInstanceRouteDto convertBOPOperationToDto(MESBOPInstanceRouteOperation routeOperation) {
        BOPInstanceRouteDto planDto = new BOPInstanceRouteDto();
        planDto.setId(routeOperation.getId());
        planDto.setBopInstance(routeOperation.getBopInstance());
        planDto.setOperation(routeOperation.getOperation());
        planDto.setPhantom(routeOperation.getPhantom());
        planDto.setParent(routeOperation.getParent());
        planDto.setSetupTime(routeOperation.getSetupTime());
        planDto.setCycleTime(routeOperation.getCycleTime());
        planDto.setSequenceNumber(routeOperation.getSequenceNumber());
        planDto.setType(routeOperation.getType());
        if (planDto.getType().equals(BOPPlanTypeEnum.OPERATION)) {
            MESOperation operation = mesOperationRepository.findOne(routeOperation.getOperation());
            planDto.setName(operation.getName());
            planDto.setNumber(operation.getNumber());
            planDto.setTypeName(operation.getType().getName());
            planDto.setDescription(operation.getDescription());
        } else {
            MESPhantom phantom = phantomRepository.findOne(routeOperation.getPhantom());
            planDto.setName(phantom.getName());
            planDto.setNumber(phantom.getNumber());
            planDto.setTypeName("Phantom");
            planDto.setDescription(phantom.getDescription());
        }
        planDto.setModifiedDate(routeOperation.getModifiedDate());
        planDto.setCreatedDate(routeOperation.getCreatedDate());
        planDto.setCreatedByName(personRepository.findOne(routeOperation.getCreatedBy()).getFullName());
        planDto.setModifiedByName(personRepository.findOne(routeOperation.getModifiedBy()).getFullName());
        planDto.setCount(bopInstanceRouteOperationRepository.getPlanCountByParent(routeOperation.getId()));
        planDto.setResourceCount(bopInstanceRouteOperationRepository.getBopInstanceOperationResourcesCountByOperation(routeOperation.getId()));
        planDto.setPartCount(bopInstanceRouteOperationRepository.getItemsCountByOperation(routeOperation.getId()));
        return planDto;
    }

    @Transactional(readOnly = true)
    public InstanceOperationPartDto getBopInstanceOperationItems(Integer operationId) {
        InstanceOperationPartDto operationPartDto = new InstanceOperationPartDto();
        List<MESBOPInstanceOperationPart> parts = bopInstanceOperationPartRepository.findByBopOperationAndType(operationId, OperationPartType.CONSUMED);
        parts.forEach(mesbopPlanItem -> {
            operationPartDto.getConsumedParts().add(convertBOPInstanceOperationPartToDto(mesbopPlanItem));
        });
        parts = bopInstanceOperationPartRepository.findByBopOperationAndType(operationId, OperationPartType.PRODUCED);
        parts.forEach(mesbopPlanItem -> {
            operationPartDto.getProducedParts().add(convertBOPInstanceOperationPartToDto(mesbopPlanItem));
        });
        return operationPartDto;
    }

    private BOPInstanceOperationPartDto convertBOPInstanceOperationPartToDto(MESBOPInstanceOperationPart mbomInstanceItem) {
        BOPInstanceOperationPartDto operationItemDto = new BOPInstanceOperationPartDto();
        MESMBOMInstanceItem mesbomInstanceItem = mbomInstanceItemRepository.findOne(mbomInstanceItem.getMbomInstanceItem());
        MESBOMItem bomItem = mesbomItemRepository.findOne(mesbomInstanceItem.getMbomItem());
        PLMBom bom = bomRepository.findOne(bomItem.getBomItem());
        operationItemDto.setId(mbomInstanceItem.getId());
        operationItemDto.setBopOperation(mbomInstanceItem.getBopOperation());
        operationItemDto.setMbomInstanceItem(mbomInstanceItem.getMbomInstanceItem());
        operationItemDto.setQuantity(mbomInstanceItem.getQuantity());
        operationItemDto.setNotes(mbomInstanceItem.getNotes());
        operationItemDto.setType(mbomInstanceItem.getType());
        operationItemDto.setItemName(bom.getItem().getItemName());
        operationItemDto.setItemNumber(bom.getItem().getItemNumber());
        operationItemDto.setItemTypeName(bom.getItem().getItemType().getName());
        operationItemDto.setRevision(bom.getItem().getItemType().getName());
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(bom.getAsReleasedRevision());
        operationItemDto.setRevision(itemRevision.getRevision());
        operationItemDto.setAsReleasedRevision(itemRevision.getId());
        operationItemDto.setHasBom(itemRevision.getHasBom());
        return operationItemDto;
    }

    @Transactional(readOnly = true)
    public List<ResourceDto> getBopInstanceOperationResources(Integer operationId) {
        List<ResourceDto> list = new ArrayList<>();

        Map<String, Map<Integer, List<BOPOperationResourceDto>>> resourceMap = new HashMap<>();
        Map<Integer, Integer> resourceQuantityMap = new HashMap<>();

        List<String> planResourceTypes = bopInstanceOperationResourceRepository.getUniqueTypesByBopOperation(operationId);
        planResourceTypes.forEach(type -> {
            resourceMap.put(type, new HashMap<>());
        });
        List<MESBOPInstanceOperationResource> planResources = bopInstanceOperationResourceRepository.findByBopOperationOrderByIdAsc(operationId);
        planResources.forEach(planResource -> {
            Map<Integer, List<BOPOperationResourceDto>> resourceTypeMap = resourceMap.get(planResource.getType());
            List<BOPOperationResourceDto> resourceDtos = resourceTypeMap.containsKey(planResource.getResourceType()) ? resourceTypeMap.get(planResource.getResourceType()) : new ArrayList<BOPOperationResourceDto>();
            resourceDtos.add(convertBopOperationResourceToDto(planResource));
            MESOperationResources operationResource = operationResourcesRepository.findByOperationAndResourceType(planResource.getOperation(), planResource.getResourceType());
            resourceQuantityMap.put(planResource.getResourceType(), operationResource.getQuantity());
            resourceTypeMap.put(planResource.getResourceType(), resourceDtos);
            resourceMap.put(planResource.getType(), resourceTypeMap);
        });

        for (String key : resourceMap.keySet()) {
            Map<Integer, List<BOPOperationResourceDto>> resourceTypeMap = resourceMap.get(key);
            ResourceDto resourceDto = new ResourceDto();
            resourceDto.setResource(key);

            for (Integer resourceType : resourceTypeMap.keySet()) {
                OperationResourceDto operationResourceDto = new OperationResourceDto();
                operationResourceDto.setResourceType(mesObjectTypeRepository.findOne(resourceType).getName());
                operationResourceDto.setQuantity(resourceQuantityMap.get(resourceType));
                operationResourceDto.getResources().addAll(resourceTypeMap.get(resourceType));
                resourceDto.setCount(resourceDto.getCount() + operationResourceDto.getResources().size());
                resourceDto.getResourceTypes().add(operationResourceDto);
            }
            list.add(resourceDto);
        }
        return list;
    }

    private BOPOperationResourceDto convertBopOperationResourceToDto(MESBOPInstanceOperationResource operationResource) {
        BOPOperationResourceDto resourceDto = new BOPOperationResourceDto();
        resourceDto.setId(operationResource.getId());
        resourceDto.setBopOperation(operationResource.getBopOperation());
        resourceDto.setOperation(operationResource.getOperation());
        resourceDto.setType(operationResource.getType());
        resourceDto.setResourceType(operationResource.getResourceType());
        resourceDto.setResource(operationResource.getResource());
        resourceDto.setNotes(operationResource.getNotes());
        resourceDto.setTypeName(mesObjectTypeRepository.findOne(operationResource.getResourceType()).getName());
        MESObject mesObject = mesObjectRepository.findOne(operationResource.getResource());
        resourceDto.setName(mesObject.getName());
        resourceDto.setNumber(mesObject.getNumber());
        resourceDto.setDescription(mesObject.getDescription());
        return resourceDto;
    }

    @Transactional
    public MESBOPInstanceOperationInstructions getBopInstanceOperationInstructions(Integer operationId) {
        return bopInstanceOperationInstructionsRepository.findByBopOperation(operationId);
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getBOPInstanceOperationCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setItemFiles(bopInstanceOperationFileRepository.findByBopOperationAndFileTypeAndLatestTrueOrderByModifiedDateDesc(id, "FILE").size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        detailsDto.setItems(bopInstanceOperationPartRepository.getItemsCountByPlan(id));
        detailsDto.setResourcesCount(bopInstanceOperationResourceRepository.getBopOperationResourcesCountByPlan(id));
        return detailsDto;
    }

}
