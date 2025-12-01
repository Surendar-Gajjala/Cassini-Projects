package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.BOPEvents;
import com.cassinisys.plm.event.ProductionOrderEvents;
import com.cassinisys.plm.filtering.MESObjectCriteria;
import com.cassinisys.plm.filtering.ProductionOrderPredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mes.dto.*;
import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.cassinisys.plm.service.wf.WorkflowEventService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ProductionOrderService implements CrudService<MESProductionOrder, Integer> {
    @Autowired
    private ProductionOrderRepository productionOrderRepository;
    @Autowired
    private ProductionOrderTypeRepository productionOrderTypeRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private ProductionOrderPredicateBuilder productionOrderPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private WorkflowEventService workflowEventService;
    @Autowired
    private BOPFileRepository bopFileRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private ObjectFileService objectFileService;
    @Autowired
    private MESPlantRepository mesPlantRepository;
    @Autowired
    private MESShiftRepository shiftRepository;
    @Autowired
    private ProductionOrderItemRepository productionOrderItemRepository;
    @Autowired
    private MBOMInstanceRepository mbomInstanceRepository;
    @Autowired
    private MESBOMItemRepository mesbomItemRepository;
    @Autowired
    private MBOMInstanceItemRepository mbomInstanceItemRepository;
    @Autowired
    private BOPInstanceRepository bopInstanceRepository;
    @Autowired
    private BOPRouteOperationRepository bopRouteOperationRepository;
    @Autowired
    private BOPOperationResourceRepository bopOperationResourceRepository;
    @Autowired
    private BOPOperationInstructionsRepository bopOperationInstructionsRepository;
    @Autowired
    private BOPOperationPartRepository bopOperationPartRepository;
    @Autowired
    private BOPOperationFileRepository bopOperationFileRepository;
    @Autowired
    private BOPInstanceRouteOperationRepository bopInstanceRouteOperationRepository;
    @Autowired
    private BOPInstanceOperationResourceRepository bopInstanceOperationResourceRepository;
    @Autowired
    private BOPInstanceOperationInstructionsRepository bopInstanceOperationInstructionsRepository;
    @Autowired
    private BOPInstanceOperationPartRepository bopInstanceOperationPartRepository;
    @Autowired
    private BOPInstanceOperationFileRepository bopInstanceOperationFileRepository;
    @Autowired
    private MESMBOMRevisionRepository mesmbomRevisionRepository;
    @Autowired
    private MESMBOMRepository mesmbomRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#productionOrder,'create')")
    public MESProductionOrder create(MESProductionOrder productionOrder) {
        Integer workflowDef = null;
        if (productionOrder.getWorkflowDefinition() != null) {
            workflowDef = productionOrder.getWorkflowDefinition();
        }

        MESProductionOrder existingBop = productionOrderRepository.findByName(productionOrder.getName());
        MESProductionOrder existBopNumber = productionOrderRepository.findByNumber(productionOrder.getNumber());
        if (existBopNumber != null) {
            String message = messageSource.getMessage("number_already_exists", null, "{0} number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existBopNumber.getNumber());
            throw new CassiniException(result);
        }
        if (existingBop != null) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingBop.getName());
            throw new CassiniException(result);

        }
        MESProductionOrderType productionOrderType = productionOrderTypeRepository.findOne(productionOrder.getType().getId());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(productionOrderType.getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        productionOrder.setLifeCyclePhase(lifeCyclePhase);
        autoNumberService.saveNextNumber(productionOrder.getType().getAutoNumberSource().getId(), productionOrder.getNumber());
        productionOrder = productionOrderRepository.save(productionOrder);

        if (workflowDef != null) {
            attachProductionOrderWorkflow(productionOrder.getId(), workflowDef);
        }
        applicationEventPublisher.publishEvent(new ProductionOrderEvents.ProductionOrderCreatedEvent(productionOrder));
//        applicationEventPublisher.publishEvent(new BOPEvents.BOPRevisionCreatedEvent(productionOrder, productionOrder.getId()));
        return productionOrder;
    }


    @Transactional
    public PLMWorkflow attachProductionOrderWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        MESProductionOrder productionOrder = productionOrderRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        PLMWorkflow workflow1 = plmWorkflowRepository.findByAttachedTo(id);
        if (workflow1 != null) {
            workflowService.deleteWorkflow(id);
        }
        if (productionOrder != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(MESEnumObject.PRODUCTIONORDER, productionOrder.getId(), wfDef);
            productionOrder.setWorkflow(workflow.getId());
            productionOrderRepository.save(productionOrder);
        }
        return workflow;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#productionOrder.id ,'edit')")
    public MESProductionOrder update(MESProductionOrder productionOrder) {
        MESProductionOrder oldbop = JsonUtils.cloneEntity(productionOrderRepository.findOne(productionOrder.getId()), MESProductionOrder.class);
        MESProductionOrder existingBop = productionOrderRepository.findByName(productionOrder.getName());
        if (existingBop != null && !productionOrder.getId().equals(existingBop.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingBop.getName());
            throw new CassiniException(result);
        }
        productionOrder = productionOrderRepository.save(productionOrder);
        applicationEventPublisher.publishEvent(new ProductionOrderEvents.ProductionOrderBasicInfoUpdatedEvent(oldbop, productionOrder));
        return productionOrder;
    }

    @Override
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        MESProductionOrder productionOrder = productionOrderRepository.findOne(id);
        if (productionOrder.getApproved()) {
            String message = messageSource.getMessage("cannot_delete_approved_po", null, "You cannot delete approved production order", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", productionOrder.getName());
            throw new CassiniException(result);
        } else {
            productionOrderRepository.delete(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESProductionOrder get(Integer id) {
        MESProductionOrder productionOrder = productionOrderRepository.findOne(id);
        if (productionOrder.getPlant() != null) {
            productionOrder.setPlantName(mesPlantRepository.findOne(productionOrder.getPlant()).getName());
        }
        if (productionOrder.getShift() != null) {
            productionOrder.setShiftName(shiftRepository.findOne(productionOrder.getShift()).getName());
        }
        return productionOrder;
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getProductionOrderCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setItemFiles(bopFileRepository.findByBopAndFileTypeAndLatestTrueOrderByModifiedDateDesc(id, "FILE").size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        Integer count = productionOrderItemRepository.getItemsSumByProductionOrder(id);
        if (count == null) {
            count = 0;
        }
        detailsDto.setItems(count);
        return detailsDto;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESProductionOrder> getAll() {
        return productionOrderRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<ProductionOrderDto> getAllProductionOrdersByPageable(Pageable pageable, MESObjectCriteria criteria) {
        Predicate predicate = productionOrderPredicateBuilder.build(criteria, QMESProductionOrder.mESProductionOrder);
        List<ProductionOrderDto> productionOrderDtos = new LinkedList<>();
        Page<MESProductionOrder> productionOrders = productionOrderRepository.findAll(predicate, pageable);
        productionOrders.getContent().forEach(productionOrder -> {
            ProductionOrderDto productionOrderDto = new ProductionOrderDto();
            productionOrderDto.setId(productionOrder.getId());
            productionOrderDto.setName(productionOrder.getName());
            productionOrderDto.setNumber(productionOrder.getNumber());
            productionOrderDto.setDescription(productionOrder.getDescription());
            productionOrderDto.setType(productionOrder.getType().getId());
            productionOrderDto.setTypeName(productionOrder.getType().getName());
            productionOrderDto.setCreatedDate(productionOrder.getCreatedDate());
            productionOrderDto.setModifiedDate(productionOrder.getModifiedDate());
            productionOrderDto.setPlannedStartDate(productionOrder.getPlannedStartDate());
            productionOrderDto.setPlannedFinishDate(productionOrder.getPlannedFinishDate());
            productionOrderDto.setApprovedDate(productionOrder.getApprovedDate());
            productionOrderDto.setApproved(productionOrder.getApproved());
            productionOrderDto.setRejected(productionOrder.getRejected());
            productionOrderDto.setLifeCyclePhase(productionOrder.getLifeCyclePhase());
            if (productionOrder.getAssignedTo() != null) {
                productionOrderDto.setAssignedTo(productionOrder.getAssignedTo());
                productionOrderDto.setAssignedToName(personRepository.findOne(productionOrder.getAssignedTo()).getFullName());
            }
            if (productionOrder.getShift() != null) {
                productionOrderDto.setShift(productionOrder.getShift());
                productionOrderDto.setShiftName(shiftRepository.findOne(productionOrder.getShift()).getName());
            }
            productionOrderDto.setCreatedByName(personRepository.findOne(productionOrder.getCreatedBy()).getFullName());
            productionOrderDto.setModifiedByName(personRepository.findOne(productionOrder.getModifiedBy()).getFullName());
            ObjectFileDto objectFileDto = objectFileService.getObjectFiles(productionOrder.getId(), PLMObjectType.MESOBJECT, false);
            productionOrderDto.setItemFiles(objectFileDto.getObjectFiles());
            if (productionOrder.getPlant() != null) {
                MESPlant mesPlant = mesPlantRepository.findOne(productionOrder.getPlant());

                if (mesPlant != null) {
                    productionOrderDto.setPlantName(mesPlant.getName());
                    productionOrderDto.setPlantNumber(mesPlant.getNumber());
                }
            }
            productionOrderDtos.add(productionOrderDto);
        });
        return new PageImpl<ProductionOrderDto>(productionOrderDtos, pageable, productionOrders.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<ProductionOrderCalenderDto> getAllCalenderProductionOrders() {
        List<ProductionOrderCalenderDto> productionOrderCalenders = new ArrayList<>();
        List<MESProductionOrder> productionOrders = productionOrderRepository.getPlannedDatesNotNullProductionOrders();
        for (MESProductionOrder productionOrder : productionOrders) {
            ProductionOrderCalenderDto orderCalenderDto = new ProductionOrderCalenderDto();
            orderCalenderDto.setId(productionOrder.getId());
            orderCalenderDto.setName(productionOrder.getName());
            orderCalenderDto.setNumber(productionOrder.getNumber());
            orderCalenderDto.setDescription(productionOrder.getDescription());
            orderCalenderDto.setPlannedStartDate(productionOrder.getPlannedStartDate());
            orderCalenderDto.setPlannedFinishDate(productionOrder.getPlannedFinishDate());
            orderCalenderDto.setLifeCyclePhase(productionOrder.getLifeCyclePhase());
            orderCalenderDto.setApproved(productionOrder.getApproved());
            orderCalenderDto.setRejected(productionOrder.getRejected());
            productionOrderCalenders.add(orderCalenderDto);
        }
        return productionOrderCalenders;
    }

    public MESProductionOrder promoteProductionOrder(Integer id, MESProductionOrder order) {
        MESProductionOrder productionOrder = productionOrderRepository.findOne(id);

        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(productionOrder.getType().getLifecycle().getId());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = productionOrder.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                findFirst().get();
        Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
        if (index != -1) {
            index++;
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
            if (lifeCyclePhase != null) {
                productionOrder.setLifeCyclePhase(lifeCyclePhase);
                if (lifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                    Integer count = productionOrderItemRepository.getItemCountByProductionOrder(productionOrder.getId());
                    if (count == null || count == 0) {
                        String message = messageSource.getMessage("production_order_has_no_items", null, "Add at least one item to the production order", LocaleContextHolder.getLocale());
                        throw new CassiniException(message);
                    }
                    productionOrder.setApproved(true);
                    productionOrder.setApprovedDate(new Date());
                }
                productionOrder = productionOrderRepository.save(productionOrder);
            }
        }
        return productionOrder;
    }

    @Transactional
    public MESProductionOrder demoteProductionOrder(Integer id, MESProductionOrder order) {
        MESProductionOrder productionOrder = productionOrderRepository.findOne(id);
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(productionOrder.getType().getLifecycle().getId());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = productionOrder.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                findFirst().get();
        Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
        if (index != -1) {
            index--;
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
            if (lifeCyclePhase != null) {
                productionOrder.setLifeCyclePhase(lifeCyclePhase);
                if (!lifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                    productionOrder.setApproved(false);
                    productionOrder.setApprovedDate(null);
                }
                productionOrder = productionOrderRepository.save(productionOrder);
            }
        }
        return productionOrder;
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PRODUCTIONORDER'")
    public void productionOrderWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        MESProductionOrder productionOrder = (MESProductionOrder) event.getAttachedToObject();
        PLMWorkflowStatus toStatus = event.getToStatus();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        productionOrder.setStatus(toStatus.getName());
        productionOrder.setStatusType(toStatus.getType());
        productionOrderRepository.save(productionOrder);
        applicationEventPublisher.publishEvent(new BOPEvents.BOPWorkflowDemotedEvent(productionOrder.getId(), plmWorkflow, fromStatus, toStatus));
        workflowEventService.workflowActivityFinishDemote("PRODUCTIONORDER", plmWorkflow, toStatus);
        workflowEventService.workflowActivityStartDemote("PRODUCTIONORDER", plmWorkflow, fromStatus);
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PRODUCTIONORDER'")
    public void productionOrderWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        MESProductionOrder productionOrder = (MESProductionOrder) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        productionOrder.setStatus(fromStatus.getName());
        productionOrder.setStatusType(fromStatus.getType());
        workflowEventService.workflowActivityFinish("PRODUCTIONORDER", plmWorkflow, fromStatus);
        workflowEventService.workflowFinish("PRODUCTIONORDER", plmWorkflow);
        if (productionOrder.getStatusType() == WorkflowStatusType.RELEASED) {
//            productionOrder.setReleased(true);
//            productionOrder.setReleasedDate(new Date());
        } else if (productionOrder.getStatusType().equals(WorkflowStatusType.REJECTED)) {
//            productionOrder.setRejected(true);
//            productionOrder.setReleasedDate(new Date());
        }
        productionOrderRepository.save(productionOrder);
//        applicationEventPublisher.publishEvent(new BOPEvents.BOPWorkflowFinishedEvent(productionOrder.getId(), plmWorkflow));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PRODUCTIONORDER'")
    public void productionOrderWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        MESProductionOrder productionOrder = (MESProductionOrder) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new BOPEvents.BOPWorkflowHoldEvent(productionOrder.getId(), plmWorkflow, fromStatus));
        workflowEventService.workflowHold("PRODUCTIONORDER", plmWorkflow);
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PRODUCTIONORDER'")
    public void productionOrderWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        MESProductionOrder productionOrder = (MESProductionOrder) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new BOPEvents.BOPWorkflowUnholdEvent(productionOrder.getId(), plmWorkflow, fromStatus));
        workflowEventService.workflowUnhold("PRODUCTIONORDER", plmWorkflow);
    }

    @Transactional(readOnly = true)
    public ProductionOrderDateDto getProductionOrderMinAndMaxDate() {
        ProductionOrderDateDto productionOrderDateDto = new ProductionOrderDateDto();
        productionOrderDateDto.setMinDate(productionOrderRepository.getProductionOrderMinDate());
        productionOrderDateDto.setMaxDate(productionOrderRepository.getProductionOrderMaxDate());
        return productionOrderDateDto;
    }

    @Transactional
    public List<ProductionOrderDto> saveMultipleProductionOrders(List<ProductionOrderDto> productionOrderDtos) {
        List<MESProductionOrder> productionOrders = new ArrayList<>();
        for (ProductionOrderDto productionOrderDto : productionOrderDtos) {
            MESProductionOrder productionOrder = productionOrderRepository.findOne(productionOrderDto.getId());
            productionOrder.setPlannedStartDate(productionOrderDto.getPlannedStartDate());
            productionOrder.setPlannedFinishDate(productionOrderDto.getPlannedFinishDate());
            productionOrders.add(productionOrder);
        }

        if (productionOrders.size() > 0) {
            productionOrders = productionOrderRepository.save(productionOrders);
        }
        return productionOrderDtos;
    }

    @Transactional
    public List<MESProductionOrderItem> createProductionOrderItems(List<MESProductionOrderItem> productionOrderItems) {
        return productionOrderItemRepository.save(productionOrderItems);
    }

    @Transactional
    public MESProductionOrderItem createProductionOrderItem(MESProductionOrderItem productionOrderItem) {
        MESProductionOrderItem existProductionOrder = productionOrderItemRepository.findByProductionOrderAndMbomRevision(productionOrderItem.getProductionOrder(), productionOrderItem.getMbomRevision());
        if (existProductionOrder != null) {
            MESMBOMRevision mesmbomRevision = mesmbomRevisionRepository.findOne(productionOrderItem.getMbomRevision());
            MESMBOM mesmbom = mesmbomRepository.findOne(mesmbomRevision.getMaster());
            String message = messageSource.getMessage("production_item_already_exist", null, "{0} : MBOM item already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", mesmbom.getNumber() + " - " + mesmbomRevision.getRevision());
            throw new CassiniException(result);
        }
        productionOrderItem = productionOrderItemRepository.save(productionOrderItem);
        for (int i = 0; i < productionOrderItem.getQuantityProduced(); i++) {
            MESMBOMInstance mbomInstance = new MESMBOMInstance();
            mbomInstance.setProductionOrderItem(productionOrderItem.getId());
            AutoNumber autoNumber = autoNumberService.getByName("Default Production Order Item Number Source");
            if (autoNumber == null) {
                autoNumber = createAutoNumberSource();
            }
            mbomInstance.setNumber(autoNumberService.getNextNumberWithoutUpdate(autoNumber.getName()));
            mbomInstance.setMbomRevision(productionOrderItem.getMbomRevision());
            mbomInstance = mbomInstanceRepository.save(mbomInstance);
            autoNumberService.saveNextNumber(autoNumber.getId(), mbomInstance.getNumber());

            copyMBOMItems(productionOrderItem.getMbomRevision(), mbomInstance.getId());
            copyBOP(productionOrderItem.getBopRevision(), mbomInstance.getId());
        }
        return productionOrderItem;
    }

    private AutoNumber createAutoNumberSource() {
        AutoNumber autoNumber = new AutoNumber();
        autoNumber.setName("Default Production Order Item Number Source");
        autoNumber.setDescription("Auto Production Order Item Number");
        autoNumber.setNumbers(5);
        autoNumber.setStart(1);
        autoNumber.setIncrement(1);
        autoNumber.setNextNumber(1);
        autoNumber.setPadwith("0");
        autoNumber.setPrefix("POI-");
        autoNumber = autoNumberService.createAutoNumber(autoNumber);
        return autoNumber;
    }

    @Transactional
    private void copyMBOMItems(Integer mbomRevision, Integer mbomInstance) {
        List<MESBOMItem> mesbomItems = mesbomItemRepository.findByMbomRevisionAndParentIsNullOrderByCreatedDateAsc(mbomRevision);
        mesbomItems.forEach(mesbomItem -> {
            MESMBOMInstanceItem instanceItem = new MESMBOMInstanceItem();
            instanceItem.setMbomInstance(mbomInstance);
            instanceItem.setMbomItem(mesbomItem.getId());
            instanceItem = mbomInstanceItemRepository.save(instanceItem);
            copyMBOMItemChildren(mesbomItem.getId(), instanceItem);
        });
    }

    private void copyMBOMItemChildren(Integer mbomItem, MESMBOMInstanceItem mesmbomInstanceItem) {
        List<MESBOMItem> mesbomItems = mesbomItemRepository.findByParentOrderByCreatedDateAsc(mbomItem);
        mesbomItems.forEach(mesbomItem -> {
            MESMBOMInstanceItem instanceItem = new MESMBOMInstanceItem();
            instanceItem.setMbomInstance(mesmbomInstanceItem.getMbomInstance());
            instanceItem.setMbomItem(mesbomItem.getId());
            instanceItem.setParent(mesmbomInstanceItem.getId());
            instanceItem = mbomInstanceItemRepository.save(instanceItem);
            copyMBOMItemChildren(mesbomItem.getId(), instanceItem);
        });
    }

    private void copyBOP(Integer bopRevision, Integer mbomInstance) {
        MESBOPInstance bopInstance = new MESBOPInstance();
        bopInstance.setMbomInstance(mbomInstance);
        bopInstance.setBopRevision(bopRevision);
        bopInstance = bopInstanceRepository.save(bopInstance);

        copyBopRoutes(bopRevision, bopInstance);
    }

    private void copyBopRoutes(Integer bopRevision, MESBOPInstance bopInstance) {
        List<MESBOPRouteOperation> routeOperations = bopRouteOperationRepository.findByBopAndParentIsNullOrderByIdAsc(bopRevision);
        for (MESBOPRouteOperation routeOperation : routeOperations) {
            MESBOPInstanceRouteOperation operation = new MESBOPInstanceRouteOperation();
            operation.setBopInstance(bopInstance.getId());
            operation.setSequenceNumber(routeOperation.getSequenceNumber());
            operation.setOperation(routeOperation.getOperation());
            operation.setPhantom(routeOperation.getPhantom());
            operation.setType(routeOperation.getType());
            operation.setSetupTime(routeOperation.getSetupTime());
            operation.setCycleTime(routeOperation.getCycleTime());
            operation = bopInstanceRouteOperationRepository.save(operation);
            if (routeOperation.getType().equals(BOPPlanTypeEnum.OPERATION)) {
                copyResources(routeOperation.getId(), operation.getId());
                copyParts(routeOperation.getId(), operation.getId(), bopInstance.getMbomInstance());
                copyInstructions(routeOperation.getId(), operation.getId());
//                copyRouteFiles(routeOperation, operation);
            }
            List<MESBOPRouteOperation> operationList = bopRouteOperationRepository.findByParentOrderByIdAsc(routeOperation.getId());
            for (MESBOPRouteOperation mesbopRouteOperation : operationList) {
                MESBOPInstanceRouteOperation instanceRouteOperation = new MESBOPInstanceRouteOperation();
                instanceRouteOperation.setBopInstance(bopInstance.getId());
                instanceRouteOperation.setSequenceNumber(mesbopRouteOperation.getSequenceNumber());
                instanceRouteOperation.setOperation(mesbopRouteOperation.getOperation());
                instanceRouteOperation.setPhantom(mesbopRouteOperation.getPhantom());
                instanceRouteOperation.setParent(operation.getId());
                instanceRouteOperation.setType(mesbopRouteOperation.getType());
                instanceRouteOperation.setSetupTime(mesbopRouteOperation.getSetupTime());
                instanceRouteOperation.setCycleTime(mesbopRouteOperation.getCycleTime());
                instanceRouteOperation = bopInstanceRouteOperationRepository.save(instanceRouteOperation);
                if (mesbopRouteOperation.getType().equals(BOPPlanTypeEnum.OPERATION)) {
                    copyResources(mesbopRouteOperation.getId(), instanceRouteOperation.getId());
                    copyParts(mesbopRouteOperation.getId(), instanceRouteOperation.getId(), bopInstance.getMbomInstance());
                    copyInstructions(mesbopRouteOperation.getId(), instanceRouteOperation.getId());
//                    copyRouteFiles(mesbopRouteOperation, instanceRouteOperation);
                }
            }
        }
    }

    private void copyResources(Integer oldRoute, Integer newRoute) {
        List<MESBOPInstanceOperationResource> resourceList = new ArrayList<>();
        List<MESBOPOperationResource> operationResources = bopOperationResourceRepository.findByBopOperationOrderByIdAsc(oldRoute);
        for (MESBOPOperationResource operationResource : operationResources) {
            MESBOPInstanceOperationResource resource = new MESBOPInstanceOperationResource();
            resource.setOperation(operationResource.getOperation());
            resource.setBopOperation(newRoute);
            resource.setNotes(operationResource.getNotes());
            resource.setResource(operationResource.getResource());
            resource.setResourceType(operationResource.getResourceType());
            resource.setType(operationResource.getType());
            resourceList.add(resource);
        }
        if (resourceList.size() > 0) {
            resourceList = bopInstanceOperationResourceRepository.save(resourceList);
        }
    }

    private void copyParts(Integer oldRoute, Integer newRoute, Integer mbomInstance) {
        List<MESBOPInstanceOperationPart> operationParts = new ArrayList<>();
        List<MESBOPOperationPart> parts = bopOperationPartRepository.findByBopOperation(oldRoute);
        for (MESBOPOperationPart part : parts) {
            MESMBOMInstanceItem mesmbomInstanceItem = mbomInstanceItemRepository.findByMbomItemAndMbomInstance(part.getMbomItem(), mbomInstance);
            if (mesmbomInstanceItem != null) {
                MESBOPInstanceOperationPart operationPart = new MESBOPInstanceOperationPart();
                operationPart.setBopOperation(newRoute);
                operationPart.setMbomInstanceItem(mesmbomInstanceItem.getId());
                operationPart.setQuantity(part.getQuantity());
                operationPart.setType(part.getType());
                operationPart.setNotes(part.getNotes());
                operationParts.add(operationPart);
            }
        }
        if (operationParts.size() > 0) {
            bopInstanceOperationPartRepository.save(operationParts);
        }
    }

    private void copyInstructions(Integer oldRoute, Integer newRoute) {
        MESBOPOperationInstructions operationInstructions = bopOperationInstructionsRepository.findByBopOperation(oldRoute);
        if (operationInstructions != null) {
            MESBOPInstanceOperationInstructions instructions = new MESBOPInstanceOperationInstructions();
            instructions.setBopOperation(newRoute);
            instructions.setInstructions(operationInstructions.getInstructions());
            instructions = bopInstanceOperationInstructionsRepository.save(instructions);
        }
    }

    @Transactional
    public ProductionOrderItemDto updateProductionOrderItem(ProductionOrderItemDto productionOrderItem) {
        MESProductionOrderItem orderItem = productionOrderItemRepository.findOne(productionOrderItem.getId());
        if (!productionOrderItem.getQuantityProduced().equals(orderItem.getQuantityProduced())) {
            if (orderItem.getQuantityProduced() > productionOrderItem.getQuantityProduced()) {
                List<MESMBOMInstance> instances = mbomInstanceRepository.findByProductionOrderItemOrderByIdAsc(orderItem.getId());
                for (int i = 0; i < orderItem.getQuantityProduced() - productionOrderItem.getQuantityProduced(); i++) {
                    mbomInstanceRepository.delete(instances.get(i).getId());
                }
            } else {
                for (int i = 0; i < productionOrderItem.getQuantityProduced() - orderItem.getQuantityProduced(); i++) {
                    MESMBOMInstance mbomInstance = new MESMBOMInstance();
                    mbomInstance.setProductionOrderItem(productionOrderItem.getId());
                    AutoNumber autoNumber = autoNumberService.getByName("Default Production Order Item Number Source");
                    if (autoNumber == null) {
                        autoNumber = createAutoNumberSource();
                    }
                    mbomInstance.setNumber(autoNumberService.getNextNumberWithoutUpdate(autoNumber.getName()));
                    mbomInstance.setMbomRevision(productionOrderItem.getMbomRevision());
                    mbomInstance = mbomInstanceRepository.save(mbomInstance);
                    autoNumberService.saveNextNumber(autoNumber.getId(), mbomInstance.getNumber());

                    copyMBOMItems(productionOrderItem.getMbomRevision(), mbomInstance.getId());
                    copyBOP(productionOrderItem.getBopRevision(), mbomInstance.getId());
                }
            }
        }
        orderItem.setQuantityProduced(productionOrderItem.getQuantityProduced());
        orderItem = productionOrderItemRepository.save(orderItem);
        return convertProductionOrderItemToDto(orderItem);
    }

    @Transactional
    public MESMBOMInstance updateProductionOrderItemInstance(MESMBOMInstance mesmbomInstance) {
        MESMBOMInstance instance = mbomInstanceRepository.findOne(mesmbomInstance.getId());
        instance.setSerialNumber(mesmbomInstance.getSerialNumber());
        instance.setBatchNumber(mesmbomInstance.getBatchNumber());
        instance.setDescription(mesmbomInstance.getDescription());
        instance.setName(mesmbomInstance.getName());
        return mbomInstanceRepository.save(instance);
    }

    @Transactional
    public void deleteProductionOrderItem(Integer productionOrderItem) {
        productionOrderItemRepository.delete(productionOrderItem);
    }

    @Transactional
    public void deleteProductionOrderItemInstance(Integer productionOrderItem) {
        MESMBOMInstance mesmbomInstance = mbomInstanceRepository.findOne(productionOrderItem);
        if (mesmbomInstance != null) {
            mbomInstanceRepository.delete(productionOrderItem);
            MESProductionOrderItem orderItem = productionOrderItemRepository.findOne(mesmbomInstance.getProductionOrderItem());
            orderItem.setQuantityProduced(orderItem.getQuantityProduced() - 1);
            orderItem = productionOrderItemRepository.save(orderItem);
        }
    }

    @Transactional
    public MESProductionOrderItem getProductionOrderItem(Integer productionOrderItem) {
        return productionOrderItemRepository.findOne(productionOrderItem);
    }

    @Transactional(readOnly = true)
    public List<ProductionOrderItemDto> getProductionOrderItems(Integer id) {
        List<ProductionOrderItemDto> orderItems = new ArrayList<>();
        List<MESProductionOrderItem> productionOrderItems = productionOrderItemRepository.findByProductionOrderOrderByIdAsc(id);
        productionOrderItems.forEach(productionOrderItem -> {
            orderItems.add(convertProductionOrderItemToDto(productionOrderItem));
        });
        return orderItems;
    }

    private ProductionOrderItemDto convertProductionOrderItemToDto(MESProductionOrderItem productionOrderItem) {
        ProductionOrderItemDto orderItemDto = new ProductionOrderItemDto();
        orderItemDto.setId(productionOrderItem.getId());
        orderItemDto.setBopRevision(productionOrderItem.getBopRevision());
        orderItemDto.setMbomRevision(productionOrderItem.getMbomRevision());
        orderItemDto.setQuantityProduced(productionOrderItem.getQuantityProduced());
        orderItemDto.setProductionOrder(productionOrderItem.getProductionOrder());
        orderItemDto.setObjectType(productionOrderItem.getObjectType().name());
        MESMBOMRevision mesmbomRevision = mesmbomRevisionRepository.findOne(productionOrderItem.getMbomRevision());
        MESMBOM mesmbom = mesmbomRepository.findOne(mesmbomRevision.getMaster());
        orderItemDto.setName(mesmbom.getName());
        orderItemDto.setNumber(mesmbom.getNumber());
        orderItemDto.setRevision(mesmbomRevision.getRevision());
        orderItemDto.setDescription(mesmbom.getDescription());
        List<MESMBOMInstance> mesmbomInstances = mbomInstanceRepository.findByProductionOrderItemOrderByIdAsc(productionOrderItem.getId());
        mesmbomInstances.forEach(mesmbomInstance -> {
            MBOMInstanceDto mbomInstanceDto = new MBOMInstanceDto();
            mbomInstanceDto.setId(mesmbomInstance.getId());
            mbomInstanceDto.setMbomRevision(mesmbomInstance.getMbomRevision());
            mbomInstanceDto.setBatchNumber(mesmbomInstance.getBatchNumber());
            mbomInstanceDto.setDescription(mesmbomInstance.getDescription());
            mbomInstanceDto.setFinishDate(mesmbomInstance.getFinishDate());
            mbomInstanceDto.setMfgDate(mesmbomInstance.getMfgDate());
            mbomInstanceDto.setName(mesmbomInstance.getName());
            mbomInstanceDto.setNumber(mesmbomInstance.getNumber());
            mbomInstanceDto.setProductionOrderItem(mesmbomInstance.getProductionOrderItem());
            mbomInstanceDto.setRejected(mesmbomInstance.getRejected());
            mbomInstanceDto.setRejectedDate(mesmbomInstance.getRejectedDate());
            mbomInstanceDto.setRejectedReason(mesmbomInstance.getRejectedReason());
            mbomInstanceDto.setReleased(mesmbomInstance.getReleased());
            mbomInstanceDto.setSerialNumber(mesmbomInstance.getSerialNumber());
            mbomInstanceDto.setStartDate(mesmbomInstance.getStartDate());
            mbomInstanceDto.setStatus(mesmbomInstance.getStatus());
            mbomInstanceDto.setObjectType(mesmbomInstance.getObjectType().name());
            orderItemDto.getChildren().add(mbomInstanceDto);
        });
        return orderItemDto;
    }
}
