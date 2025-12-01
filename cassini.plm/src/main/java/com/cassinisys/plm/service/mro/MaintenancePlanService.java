package com.cassinisys.plm.service.mro;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.MaintenancePlanEvents;
import com.cassinisys.plm.filtering.MaintenancePlanCriteria;
import com.cassinisys.plm.filtering.MaintenancePlanPredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mro.MROMaintenanceOperation;
import com.cassinisys.plm.model.mro.MROMaintenancePlan;
import com.cassinisys.plm.model.mro.MROWorkOrder;
import com.cassinisys.plm.model.mro.QMROMaintenancePlan;
import com.cassinisys.plm.model.mro.dto.MaintenancePlanDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.mro.*;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
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
import java.util.List;

@Service
public class MaintenancePlanService implements CrudService<MROMaintenancePlan, Integer> {
    @Autowired
    private MROMaintenancePlanRepository mroMaintenancePlanRepository;
    @Autowired
    private MaintenancePlanPredicateBuilder maintenancePlanPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MROWorkOrderTypeRepository workOrderTypeRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MROAssetRepository mroAssetRepository;
    @Autowired
    private MROMaintenanceOperationRepository mroMaintenanceOperationRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MROWorkOrderRepository mroWorkOrderRepository;
    @Autowired
    private AutoNumberService autoNumberService;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#maintenancePlan,'create')")
    public MROMaintenancePlan create(MROMaintenancePlan maintenancePlan) {

        MROMaintenancePlan existMaintencePlanNumber = mroMaintenancePlanRepository.findByNumber(maintenancePlan.getNumber());
        if (existMaintencePlanNumber != null) {
            String message = messageSource.getMessage("maintenancepaln_number_already_exists", null, "{0} Maintenance plan Number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existMaintencePlanNumber.getNumber());
            throw new CassiniException(result);
        }
        MROMaintenancePlan existingPart = mroMaintenancePlanRepository.findByNameContainingIgnoreCase(maintenancePlan.getName());
        if (existingPart != null) {
            throw new CassiniException(messageSource.getMessage("maintenance_plan_already_exist", null, "Maintenance plan name already exist", LocaleContextHolder.getLocale()));

        } else {

            AutoNumber autoNumber = autoNumberService.getByName("Default Maintenance Plan Number Source");
            autoNumberService.saveNextNumber(autoNumber.getId(), maintenancePlan.getNumber());
            maintenancePlan = mroMaintenancePlanRepository.save(maintenancePlan);
        }
        applicationEventPublisher.publishEvent(new MaintenancePlanEvents.MaintenancePlanCreatedEvent(maintenancePlan));
        return maintenancePlan;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#maintenancePlan.id ,'edit')")
    public MROMaintenancePlan update(MROMaintenancePlan maintenancePlan) {
        MROMaintenancePlan oldPart = JsonUtils.cloneEntity(mroMaintenancePlanRepository.findOne(maintenancePlan.getId()), MROMaintenancePlan.class);
        MROMaintenancePlan existingPart = mroMaintenancePlanRepository.findByNameContainingIgnoreCase(maintenancePlan.getName());
        if (existingPart != null && !existingPart.getId().equals(maintenancePlan.getId())) {
            throw new CassiniException(messageSource.getMessage("maintenance_plan_already_exist", null, "Maintenance plan name already exist", LocaleContextHolder.getLocale()));

        } else {
            maintenancePlan = mroMaintenancePlanRepository.save(maintenancePlan);
        }
        applicationEventPublisher.publishEvent(new MaintenancePlanEvents.MaintenancePlanBasicInfoUpdatedEvent(oldPart, maintenancePlan));
        return maintenancePlan;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#planId,'delete')")
    public void delete(Integer planId) {
        List<MROWorkOrder> workOrders = mroWorkOrderRepository.findByPlan(planId);
        if (workOrders.size() > 0) {
            throw new CassiniException(messageSource.getMessage("maintenance_plan_already_in_use", null, "Maintenance plan already in use", LocaleContextHolder.getLocale()));
        } else {
            mroMaintenancePlanRepository.delete(planId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MROMaintenancePlan get(Integer planId) {
        MROMaintenancePlan maintenancePlan = mroMaintenancePlanRepository.findOne(planId);
        return maintenancePlan;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROMaintenancePlan> getMaintenancePlansByAsset(Integer assetId) {
        List<MROMaintenancePlan> maintenancePlansWithOperations = new ArrayList<>();
        List<MROMaintenancePlan> maintenancePlans = mroMaintenancePlanRepository.findByAsset(assetId);
        maintenancePlans.forEach(maintenancePlan -> {
            List<MROMaintenanceOperation> maintenanceOperations = mroMaintenanceOperationRepository.findByMaintenancePlanOrderByModifiedDateDesc(maintenancePlan.getId());
            if (maintenanceOperations.size() > 0) {
                maintenancePlansWithOperations.add(maintenancePlan);
            }
        });
        return maintenancePlansWithOperations;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROMaintenancePlan> getAll() {
        return mroMaintenancePlanRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<MaintenancePlanDto> getAllMaintenancePlans(Pageable pageable, MaintenancePlanCriteria criteria) {
        Predicate predicate = maintenancePlanPredicateBuilder.build(criteria, QMROMaintenancePlan.mROMaintenancePlan);
        Page<MROMaintenancePlan> maintenancePlanPage = mroMaintenancePlanRepository.findAll(predicate, pageable);
        List<MaintenancePlanDto> maintenancePlanDtos = new ArrayList<>();
        maintenancePlanPage.getContent().forEach(mroMaintenancePlan -> {
            MaintenancePlanDto maintenancePlanDto = new MaintenancePlanDto();
            maintenancePlanDto.setId(mroMaintenancePlan.getId());
            maintenancePlanDto.setName(mroMaintenancePlan.getName());
            maintenancePlanDto.setNumber(mroMaintenancePlan.getNumber());
            maintenancePlanDto.setDescription(mroMaintenancePlan.getDescription());
            maintenancePlanDto.setObjectType(mroMaintenancePlan.getObjectType().toString());
            if (mroMaintenancePlan.getAsset() != null) {
                maintenancePlanDto.setAsset(mroMaintenancePlan.getAsset());
                maintenancePlanDto.setAssetName(mroAssetRepository.findOne(mroMaintenancePlan.getAsset()).getName());
            }
            maintenancePlanDto.setModifiedDate(mroMaintenancePlan.getModifiedDate());
            maintenancePlanDto.setModifiedByName(personRepository.findOne(mroMaintenancePlan.getModifiedBy()).getFullName());
            maintenancePlanDtos.add(maintenancePlanDto);
        });

        return new PageImpl<MaintenancePlanDto>(maintenancePlanDtos, pageable, maintenancePlanPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getTabCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setOperations(mroMaintenanceOperationRepository.findByMaintenancePlanOrderByModifiedDateDesc(id).size());
        return detailsDto;
    }

    /*
    *
    * MaintenancePlan Workflow Events
    * */
    @EventListener(condition = "#event.attachedToType.name() == 'MROMAINTENANCEPLAN'")
    public void maintenancePlanWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        MROMaintenancePlan maintenancePlan = (MROMaintenancePlan) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new MaintenancePlanEvents.MaintenancePlanWorkflowStartedEvent(maintenancePlan, event.getPlmWorkflow()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MROMAINTENANCEPLAN'")
    public void maintenancePlanWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        MROMaintenancePlan maintenancePlan = (MROMaintenancePlan) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        applicationEventPublisher.publishEvent(new MaintenancePlanEvents.MaintenancePlanWorkflowPromotedEvent(maintenancePlan, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MROMAINTENANCEPLAN'")
    public void maintenancePlanWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        MROMaintenancePlan maintenancePlan = (MROMaintenancePlan) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new MaintenancePlanEvents.MaintenancePlanWorkflowDemotedEvent(maintenancePlan, event.getPlmWorkflow(), event.getFromStatus(), event.getToStatus()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MROMAINTENANCEPLAN'")
    public void maintenancePlanWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        MROMaintenancePlan maintenancePlan = (MROMaintenancePlan) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        applicationEventPublisher.publishEvent(new MaintenancePlanEvents.MaintenancePlanWorkflowFinishedEvent(maintenancePlan, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MROMAINTENANCEPLAN'")
    public void maintenancePlanWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        MROMaintenancePlan maintenancePlan = (MROMaintenancePlan) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new MaintenancePlanEvents.MaintenancePlanWorkflowHoldEvent(maintenancePlan, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MROMAINTENANCEPLAN'")
    public void maintenancePlanWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        MROMaintenancePlan maintenancePlan = (MROMaintenancePlan) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new MaintenancePlanEvents.MaintenancePlanWorkflowUnholdEvent(maintenancePlan, plmWorkflow, fromStatus));
    }

    @Transactional(readOnly = true)
    public List<MROMaintenanceOperation> getMaintenancePlanOperations(Integer workOrderId) {
        return mroMaintenanceOperationRepository.findByMaintenancePlanOrderByModifiedDateDesc(workOrderId);
    }

    @Transactional
    public MROMaintenanceOperation createMaintenancePlanOperation(MROMaintenanceOperation maintenanceOperation) {
        List<MROMaintenanceOperation> maintenanceOperations = new ArrayList<>();
        MROMaintenanceOperation existPart = mroMaintenanceOperationRepository.findByMaintenancePlanAndName(maintenanceOperation.getMaintenancePlan(), maintenanceOperation.getName());
        if (existPart != null && !existPart.getId().equals(maintenanceOperation.getId())) {
            throw new CassiniException(messageSource.getMessage(maintenanceOperation.getName() + " : " + "operation_name_already_exist", null, maintenanceOperation.getName() + " : Operation name already exist", LocaleContextHolder.getLocale()));
        } else {
            maintenanceOperation = mroMaintenanceOperationRepository.save(maintenanceOperation);
            maintenanceOperations.add(maintenanceOperation);
            applicationEventPublisher.publishEvent(new MaintenancePlanEvents.MaintenancePlanOperationCreatedEvent(maintenanceOperation.getMaintenancePlan(), maintenanceOperations));
        }
        return maintenanceOperation;
    }

    @Transactional
    public List<MROMaintenanceOperation> createMultipleMaintenancePlanOperations(Integer planId, List<MROMaintenanceOperation> maintenanceOperations) {
        for (MROMaintenanceOperation maintenanceOperation : maintenanceOperations) {
            MROMaintenanceOperation existPart = mroMaintenanceOperationRepository.findByMaintenancePlanAndName(maintenanceOperation.getMaintenancePlan(), maintenanceOperation.getName());
            if (existPart != null && !existPart.getId().equals(maintenanceOperation.getId())) {
                throw new CassiniException(messageSource.getMessage(maintenanceOperation.getName() + " : " + "operation_name_already_exist", null, maintenanceOperation.getName() + " : Operation name already exist", LocaleContextHolder.getLocale()));
            } else {
                maintenanceOperation = mroMaintenanceOperationRepository.save(maintenanceOperation);
            }
        }
        applicationEventPublisher.publishEvent(new MaintenancePlanEvents.MaintenancePlanOperationCreatedEvent(planId, maintenanceOperations));
        return maintenanceOperations;
    }

    @Transactional
    public MROMaintenanceOperation updateMaintenancePlanOperation(MROMaintenanceOperation maintenanceOperation) {
        MROMaintenanceOperation existOperation = JsonUtils.cloneEntity(mroMaintenanceOperationRepository.findByMaintenancePlanAndName(maintenanceOperation.getMaintenancePlan(), maintenanceOperation.getName()), MROMaintenanceOperation.class);
        MROMaintenanceOperation oldOperation = JsonUtils.cloneEntity(mroMaintenanceOperationRepository.findOne(maintenanceOperation.getId()), MROMaintenanceOperation.class);
        if (existOperation != null && !maintenanceOperation.getId().equals(existOperation.getId())) {
            throw new CassiniException(messageSource.getMessage(maintenanceOperation.getName() + " : " + "operation_name_already_exist", null, maintenanceOperation.getName() + " : Operation name already exist", LocaleContextHolder.getLocale()));
        } else {
            maintenanceOperation = mroMaintenanceOperationRepository.save(maintenanceOperation);
            applicationEventPublisher.publishEvent(new MaintenancePlanEvents.MaintenancePlanOperationUpdatedEvent(maintenanceOperation.getMaintenancePlan(), oldOperation, maintenanceOperation));
        }
        return maintenanceOperation;
    }

    @Transactional
    public void deleteMaintenancePlanOperation(Integer operationId) {
        MROMaintenanceOperation maintenanceOperation = mroMaintenanceOperationRepository.findOne(operationId);
        applicationEventPublisher.publishEvent(new MaintenancePlanEvents.MaintenancePlanOperationDeletedEvent(maintenanceOperation.getMaintenancePlan(), maintenanceOperation));
        mroMaintenanceOperationRepository.delete(operationId);
    }
}

