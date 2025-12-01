package com.cassinisys.plm.service.mro;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.plm.event.WorkOrderEvents;
import com.cassinisys.plm.filtering.WorkOrderCriteria;
import com.cassinisys.plm.filtering.WorkOrderPredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.mro.dto.WorkOrderDto;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mro.*;
import com.cassinisys.plm.repo.wf.*;
import com.cassinisys.plm.service.cm.DCOService;
import com.cassinisys.plm.service.plm.ItemService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WorkOrderService implements CrudService<MROWorkOrder, Integer> {
    @Autowired
    private MROWorkOrderRepository mroWorkOrderRepository;
    @Autowired
    private WorkOrderPredicateBuilder workOrderPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MROObjectAttributeRepository mroObjectAttributeRepository;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private MROWorkOrderTypeRepository workOrderTypeRepository;
    @Autowired
    private MROObjectTypeAttributeRepository mroObjectTypeAttributeRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MROAssetRepository mroAssetRepository;
    @Autowired
    private MROWorkRequestRepository mroWorkRequestRepository;
    @Autowired
    private MROWorkOrderPartRepository mroWorkOrderPartRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MROMaintenancePlanRepository mroMaintenancePlanRepository;
    @Autowired
    private MROMaintenanceOperationRepository maintenanceOperationRepository;
    @Autowired
    private MROWorkOrderOperationRepository workOrderOperationRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private WorkflowDefinitionMasterRepository workflowDefinitionMasterRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private MROObjectTypeRepository mroObjectTypeRepository;
    @Autowired
    private MROWorkOrderResourceRepository workOrderResourceRepository;
    @Autowired
    private MESMachineRepository machineRepository;
    @Autowired
    private MESEquipmentRepository equipmentRepository;
    @Autowired
    private MESInstrumentRepository instrumentRepository;
    @Autowired
    private MESMaterialRepository materialRepository;
    @Autowired
    private MESToolRepository toolRepo;
    @Autowired
    private ManufacturerDataRepository manufacturerDataRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private MROWorkOrderInstructionsRepository mroWorkOrderInstructionsRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#workOrder,'create')")
    public MROWorkOrder create(MROWorkOrder workOrder) {
        MROWorkOrder existWorkOrderNumber = mroWorkOrderRepository.findByNumber(workOrder.getNumber());
        if (existWorkOrderNumber != null) {
            String message = messageSource.getMessage("workorder_number_already_exists", null, "{0} Workorder plan Number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existWorkOrderNumber.getNumber());
            throw new CassiniException(result);
        }
        MROWorkOrder existingPart = mroWorkOrderRepository.findByName(workOrder.getName());
        Integer workflowDef = null;
        if (workOrder.getWorkflow() != null) {
            workflowDef = workOrder.getWorkflow();
        }
        if (existingPart != null) {
            throw new CassiniException(messageSource.getMessage("work_order_already_exist", null, "Work Order name already exist", LocaleContextHolder.getLocale()));

        } else {
            workOrder.setWorkflow(null);
            autoNumberService.saveNextNumber(workOrder.getType().getAutoNumberSource().getId(), workOrder.getNumber());
            workOrder = mroWorkOrderRepository.save(workOrder);
        }

        if (workflowDef != null) {
            attachWorkOrderWorkflow(workOrder.getId(), workflowDef);
        }

        if (workOrder.getPlan() != null) {
            createWorkOrderOperation(workOrder.getPlan(), workOrder.getId());
        }

        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderCreatedEvent(workOrder));
        sendEmailToAssignedPerson(workOrder);
        return workOrder;
    }

    private void createWorkOrderOperation(Integer maintenancePlanId, Integer workOrderId) {
        MROMaintenancePlan maintenancePlan = mroMaintenancePlanRepository.findOne(maintenancePlanId);
        if (maintenancePlan != null) {
            List<MROMaintenanceOperation> maintenanceOperations = maintenanceOperationRepository.findByMaintenancePlanOrderByModifiedDateDesc(maintenancePlanId);
            List<MROWorkOrderOperation> operations = new LinkedList<>();
            for (MROMaintenanceOperation maintenanceOperation : maintenanceOperations) {
                MROWorkOrderOperation workOrderOperation = new MROWorkOrderOperation();
                workOrderOperation.setWorkOrder(workOrderId);
                workOrderOperation.setName(maintenanceOperation.getName());
                workOrderOperation.setDescription(maintenanceOperation.getDescription());
                workOrderOperation.setParamName(maintenanceOperation.getParamName());
                workOrderOperation.setParamType(maintenanceOperation.getParamType());
                workOrderOperation.setLov(maintenanceOperation.getLov());

                operations.add(workOrderOperation);
            }
            if (operations.size() > 0) {
                operations = workOrderOperationRepository.save(operations);
            }
        }
    }

    private void sendEmailToAssignedPerson(MROWorkOrder workOrder) {
        Person person = personRepository.findOne(workOrder.getAssignedTo());
        Person createdBy = personRepository.findOne(workOrder.getCreatedBy());

        if (person != null && person.getEmail() != null) {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            URL companyLogo = null;
            Preference preference = preferenceRepository.findByPreferenceKey("SYSTEM.LOGO");
            if (preference != null) {
                if (preference.getCustomLogo() != null) {
                    URL url1 = ItemService.class
                            .getClassLoader().getResource("templates/email/share/" + "dummy_logo.png");
                    File file = new File(url1.getPath());
                    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                        outputStream.write(preference.getCustomLogo());
                        companyLogo = ItemService.class.getClassLoader().getResource("templates/email/share/" + "dummy_logo.png");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String createdDate = df.format(workOrder.getCreatedDate());
            final URL companyLogos = companyLogo;
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("workOrder", workOrder);
                model.put("companyLogo", companyLogos);
                model.put("personName", person.getFullName());
                model.put("createdBy", createdBy.getFullName());
                model.put("createdDate", createdDate);
                Mail mail = new Mail();
                mail.setMailTo(person.getEmail());
                mail.setMailSubject("New Work Order");
                mail.setTemplatePath("email/workOrderNotification.html");
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#workOrder.id ,'edit')")
    public MROWorkOrder update(MROWorkOrder workOrder) {
        MROWorkOrder oldPart = JsonUtils.cloneEntity(mroWorkOrderRepository.findOne(workOrder.getId()), MROWorkOrder.class);
        MROWorkOrder existingPart = mroWorkOrderRepository.findByName(workOrder.getName());
        if (existingPart != null && !existingPart.getId().equals(workOrder.getId())) {
            throw new CassiniException(messageSource.getMessage("work_order_already_exist", null, "Work Order name already exist", LocaleContextHolder.getLocale()));

        } else {
            workOrder = mroWorkOrderRepository.save(workOrder);
        }
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderBasicInfoUpdatedEvent(oldPart, workOrder));
        return workOrder;
    }

    @Override
    @PreAuthorize("hasPermission(#workOrderId,'delete')")
    public void delete(Integer workOrderId) {
        MROWorkOrder workOrder = mroWorkOrderRepository.findOne(workOrderId);
        if (workOrder.getStatus().equals(WorkOrderStatusType.FINISH)) {
            throw new CassiniException(messageSource.getMessage("cannot_delete_finished_work_order", null, "You cannot delete finished work order", LocaleContextHolder.getLocale()));
        } else {
            mroWorkOrderRepository.delete(workOrderId);
        }
    }


    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MROWorkOrder get(Integer partId) {
        MROWorkOrder workOrder = mroWorkOrderRepository.findOne(partId);
        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(workOrder.getId());
        if (workflow != null) {
            if (workflow.getStart() != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
                if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    workOrder.setStartWorkflow(true);
                }
            }
        }
        return workOrder;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MROWorkOrder> getAll() {
        return mroWorkOrderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getTabCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setOperations(workOrderOperationRepository.findByWorkOrder(id).size());
        detailsDto.setSpareParts(mroWorkOrderPartRepository.findByWorkOrderOrderByModifiedDateDesc(id).size());
        detailsDto.setResources(workOrderResourceRepository.findByWorkOrder(id).size());

        return detailsDto;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'promote','mroworkorder')")
    public MROWorkOrder promoteWorkOrderStatus(Integer workOrderId) {
        MROWorkOrder workOrder = mroWorkOrderRepository.findOne(workOrderId);
        if (workOrder.getStatus().equals(WorkOrderStatusType.OPEN)) {
            workOrder.setStatus(WorkOrderStatusType.INPROGRESS);
            workOrder = mroWorkOrderRepository.save(workOrder);
            applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderStartedEvent(workOrder));
        } else if (workOrder.getStatus().equals(WorkOrderStatusType.INPROGRESS)) {
            workOrder.setStatus(WorkOrderStatusType.FINISH);

            if (workOrder.getType().getType().equals(WorkOrderType.MAINTENANCE)) {
                List<MROWorkOrderOperation> operations = workOrderOperationRepository.findByWorkOrderAndResult(workOrder.getId(), OperationResult.NONE);
                if (operations.size() > 0) {
                    throw new CassiniException(messageSource.getMessage("finish_all_operations_to_finish_workorder", null, "Finish all operations to finish work order", LocaleContextHolder.getLocale()));
                } else {
                    workOrder = mroWorkOrderRepository.save(workOrder);
                }
            } else {
                workOrder = mroWorkOrderRepository.save(workOrder);
                if (workOrder.getRequest() != null) {
                    MROWorkRequest workRequest = mroWorkRequestRepository.findOne(workOrder.getRequest());
                    workRequest.setStatus(WorkRequestStatusType.FINISH);
                    workRequest = mroWorkRequestRepository.save(workRequest);
                }
            }
            applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderFinishedEvent(workOrder));
        }

        return workOrder;
    }

    @Transactional
    public MROWorkOrder holdWorkOrder(Integer workOrderId) {
        MROWorkOrder workOrder = mroWorkOrderRepository.findOne(workOrderId);
        workOrder.setStatus(WorkOrderStatusType.ONHOLD);
        workOrder = mroWorkOrderRepository.save(workOrder);
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderHoldEvent(workOrder));
        return workOrder;
    }

    @Transactional
    public MROWorkOrder removeOnHoldWorkOrder(Integer workOrderId) {
        MROWorkOrder workOrder = mroWorkOrderRepository.findOne(workOrderId);
        workOrder.setStatus(WorkOrderStatusType.INPROGRESS);
        workOrder = mroWorkOrderRepository.save(workOrder);
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderUnholdEvent(workOrder));
        return workOrder;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<WorkOrderDto> getAllWorkOrders(Pageable pageable, WorkOrderCriteria criteria) {
        Predicate predicate = workOrderPredicateBuilder.build(criteria, QMROWorkOrder.mROWorkOrder);
        Page<MROWorkOrder> workOrderPage = mroWorkOrderRepository.findAll(predicate, pageable);
        List<WorkOrderDto> workOrderDtos = new ArrayList<>();
        workOrderPage.getContent().forEach(mroWorkOrder -> {
            WorkOrderDto workOrderDto = new WorkOrderDto();
            workOrderDto.setId(mroWorkOrder.getId());
            workOrderDto.setTypeName(mroWorkOrder.getType().getName());
            workOrderDto.setName(mroWorkOrder.getName());
            workOrderDto.setNumber(mroWorkOrder.getNumber());
            workOrderDto.setDescription(mroWorkOrder.getDescription());
            workOrderDto.setNotes(mroWorkOrder.getNotes());
            workOrderDto.setObjectType(mroWorkOrder.getObjectType().toString());
            if (mroWorkOrder.getAsset() != null) {
                workOrderDto.setAsset(mroWorkOrder.getAsset());
                workOrderDto.setAssetName(mroAssetRepository.findOne(mroWorkOrder.getAsset()).getName());
            }
            if (mroWorkOrder.getRequest() != null) {
                workOrderDto.setRequest(mroWorkOrder.getRequest());
                workOrderDto.setRequestNumber(mroWorkRequestRepository.findOne(mroWorkOrder.getRequest()).getNumber());
            }
            if (mroWorkOrder.getPlan() != null) {
                workOrderDto.setPlan(mroWorkOrder.getPlan());
                workOrderDto.setPlanNumber(mroMaintenancePlanRepository.findOne(mroWorkOrder.getPlan()).getNumber());
            }
            if (mroWorkOrder.getAssignedTo() != null) {
                workOrderDto.setAssignedToName(personRepository.findOne(mroWorkOrder.getAssignedTo()).getFullName());
            }
            workOrderDto.setPriority(mroWorkOrder.getPriority());
            workOrderDto.setStatus(mroWorkOrder.getStatus());
            workOrderDto.setModifiedDate(mroWorkOrder.getModifiedDate());
            workOrderDto.setModifiedByName(personRepository.findOne(mroWorkOrder.getModifiedBy()).getFullName());
            workOrderDtos.add(workOrderDto);
        });

        return new PageImpl<WorkOrderDto>(workOrderDtos, pageable, workOrderPage.getTotalElements());
    }

    @Transactional
    public void savemroObjectAttributes(List<MROObjectAttribute> attributes) {
        for (MROObjectAttribute attribute : attributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {
                mroObjectAttributeRepository.save(attribute);
            }
        }
    }

    @Transactional
    public MROWorkOrder saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        MROWorkOrder workOrder = mroWorkOrderRepository.findOne(objectId);
        if (workOrder != null) {
            MROObjectAttribute mroObjectAttribute = new MROObjectAttribute();
            mroObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, mroObjectAttribute);

        }

        return workOrder;
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public MROObjectAttribute updateObjectAttribute(MROObjectAttribute attribute) {
        MROObjectAttribute oldValue = mroObjectAttributeRepository.findByObjectAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, MROObjectAttribute.class);
        MROObjectTypeAttribute mroObjectTypeAttribute = mroObjectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());

        if (mroObjectTypeAttribute.getMeasurement() != null) {
            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(mroObjectTypeAttribute.getMeasurement().getId());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(attribute.getMeasurementUnit().getId());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(mroObjectTypeAttribute.getMeasurement().getId());

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
        attribute = mroObjectAttributeRepository.save(attribute);

        MROWorkOrder part = mroWorkOrderRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderAttributesUpdatedEvent(part, oldValue, attribute));
        return attribute;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MROWorkOrder> getWorkOrdersByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MROWorkOrderType type = workOrderTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return mroWorkOrderRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MROWorkOrderType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MROWorkOrderType> children = workOrderTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MROWorkOrderType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    /*
    *
    * WorkOrder Workflow Events
    * */
    @EventListener(condition = "#event.attachedToType.name() == 'MROWORKORDER'")
    public void workOrderWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        MROWorkOrder workOrder = (MROWorkOrder) event.getAttachedToObject();
        workOrder.setStatus(WorkOrderStatusType.INPROGRESS);
        workOrder = mroWorkOrderRepository.save(workOrder);
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderWorkflowStartedEvent(workOrder, event.getPlmWorkflow()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MROWORKORDER'")
    public void workOrderWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        MROWorkOrder workOrder = (MROWorkOrder) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        if (fromStatus.getType().equals(WorkflowStatusType.RELEASED) || fromStatus.getType().equals(WorkflowStatusType.REJECTED)) {
            workOrder.setStatus(WorkOrderStatusType.FINISH);
            if (workOrder.getType().getType().equals(WorkOrderType.MAINTENANCE)) {
                List<MROWorkOrderOperation> operations = workOrderOperationRepository.findByWorkOrderAndResult(workOrder.getId(), OperationResult.NONE);
                if (operations.size() > 0) {
                    throw new CassiniException(messageSource.getMessage("finish_all_operations_to_finish_workorder", null, "Finish all operations to finish work order", LocaleContextHolder.getLocale()));
                } else {
                    workOrder = mroWorkOrderRepository.save(workOrder);
                }
            } else {
                workOrder = mroWorkOrderRepository.save(workOrder);
                if (workOrder.getRequest() != null) {
                    MROWorkRequest workRequest = mroWorkRequestRepository.findOne(workOrder.getRequest());
                    workRequest.setStatus(WorkRequestStatusType.FINISH);
                    workRequest = mroWorkRequestRepository.save(workRequest);
                }
            }
        }
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderWorkflowPromotedEvent(workOrder, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MROWORKORDER'")
    public void workOrderWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        MROWorkOrder workOrder = (MROWorkOrder) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderWorkflowDemotedEvent(workOrder, event.getPlmWorkflow(), event.getFromStatus(), event.getToStatus()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MROWORKORDER'")
    public void workOrderWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        MROWorkOrder workOrder = (MROWorkOrder) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        if (fromStatus.getType().equals(WorkflowStatusType.RELEASED) || fromStatus.getType().equals(WorkflowStatusType.REJECTED)) {
            workOrder.setStatus(WorkOrderStatusType.FINISH);
            if (workOrder.getType().getType().equals(WorkOrderType.MAINTENANCE)) {
                List<MROWorkOrderOperation> operations = workOrderOperationRepository.findByWorkOrderAndResult(workOrder.getId(), OperationResult.NONE);
                if (operations.size() > 0) {
                    throw new CassiniException(messageSource.getMessage("finish_all_operations_to_finish_workorder", null, "Finish all operations to finish work order", LocaleContextHolder.getLocale()));
                } else {
                    workOrder = mroWorkOrderRepository.save(workOrder);
                }
            } else {
                workOrder = mroWorkOrderRepository.save(workOrder);
                if (workOrder.getRequest() != null) {
                    MROWorkRequest workRequest = mroWorkRequestRepository.findOne(workOrder.getRequest());
                    workRequest.setStatus(WorkRequestStatusType.FINISH);
                    workRequest = mroWorkRequestRepository.save(workRequest);
                }
            }
        }
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderWorkflowFinishedEvent(workOrder, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MROWORKORDER'")
    public void workOrderWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        MROWorkOrder workOrder = (MROWorkOrder) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        workOrder.setStatus(WorkOrderStatusType.ONHOLD);
        workOrder = mroWorkOrderRepository.save(workOrder);
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderWorkflowHoldEvent(workOrder, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MROWORKORDER'")
    public void workOrderWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        MROWorkOrder workOrder = (MROWorkOrder) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        workOrder.setStatus(WorkOrderStatusType.INPROGRESS);
        workOrder = mroWorkOrderRepository.save(workOrder);
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderWorkflowUnholdEvent(workOrder, plmWorkflow, fromStatus));
    }

    @Transactional(readOnly = true)
    public List<MROWorkOrderPart> getWorkOrderSpareParts(Integer workOrderId) {
        return mroWorkOrderPartRepository.findByWorkOrderOrderByModifiedDateDesc(workOrderId);
    }

    @Transactional
    public MROWorkOrderOperation updateWorkOrderOperation(MROWorkOrderOperation workOrderOperation) {
        MROWorkOrderOperation oldOperation = JsonUtils.cloneEntity(workOrderOperationRepository.findOne(workOrderOperation.getId()), MROWorkOrderOperation.class);
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderOperationUpdatedEvent(workOrderOperation.getWorkOrder(), oldOperation, workOrderOperation));
        return workOrderOperationRepository.save(workOrderOperation);
    }

    @Transactional(readOnly = true)
    public List<MROWorkOrderOperation> getWorkOrderOperations(Integer workOrderId) {
        return workOrderOperationRepository.findByWorkOrder(workOrderId);
    }

    @Transactional
    public PLMWorkflow attachWorkOrderWorkflow(Integer workOrderId, Integer wfDefId) {
        PLMWorkflow workflow = null;
        MROWorkOrder workOrder = mroWorkOrderRepository.findOne(workOrderId);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (workOrder != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(MROEnumObject.MROWORKORDER, workOrder.getId(), wfDef);
            workOrder.setWorkflow(workflow.getId());
            mroWorkOrderRepository.save(workOrder);
        }
        return workflow;
    }

    @Transactional
    public List<MROWorkOrderPart> createWorkOrderSpareParts(Integer workOrderId, List<MROWorkOrderPart> workOrderParts) {
        List<MROWorkOrderPart> orderParts = new ArrayList<>();
        for (MROWorkOrderPart workOrderPart : workOrderParts) {
            MROWorkOrderPart existPart = mroWorkOrderPartRepository.findByWorkOrderAndSparePart(workOrderPart.getWorkOrder(), workOrderPart.getSparePart());
            if (existPart == null) {
                orderParts.add(mroWorkOrderPartRepository.save(workOrderPart));
            }
        }
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderSparePartCreatedEvent(workOrderId, orderParts));
        return workOrderParts;
    }

    @Transactional
    public MROWorkOrderPart createWorkOrderSparePart(MROWorkOrderPart workOrderPart) {
        MROWorkOrderPart existPart = mroWorkOrderPartRepository.findByWorkOrderAndSparePart(workOrderPart.getWorkOrder(), workOrderPart.getSparePart());
        if (existPart == null) {
            workOrderPart = mroWorkOrderPartRepository.save(workOrderPart);
            List<MROWorkOrderPart> orderParts = new ArrayList<>();
            orderParts.add(workOrderPart);
            applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderSparePartCreatedEvent(workOrderPart.getWorkOrder(), orderParts));
        } else {
            throw new CassiniException("Spare part already exist");
        }
        return workOrderPart;
    }

    @Transactional
    public MROWorkOrderPart updateWorkOrderSparePart(MROWorkOrderPart workOrderPart) {
        MROWorkOrderPart oldPart = JsonUtils.cloneEntity(mroWorkOrderPartRepository.findOne(workOrderPart.getId()), MROWorkOrderPart.class);
        workOrderPart = mroWorkOrderPartRepository.save(workOrderPart);
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderSparePartUpdatedEvent(workOrderPart.getWorkOrder(), oldPart, workOrderPart));
        return workOrderPart;
    }

    @Transactional
    public void deleteWorkOrderSparePart(Integer workOrderPart) {
        MROWorkOrderPart orderPart = mroWorkOrderPartRepository.findOne(workOrderPart);
        applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderSparePartDeletedEvent(orderPart.getWorkOrder(), orderPart));
        mroWorkOrderPartRepository.delete(workOrderPart);
    }

    @Transactional
    public PLMWorkflow attachWOWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        MROWorkOrder workOrder = mroWorkOrderRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        PLMWorkflow workflow1 = plmWorkflowRepository.findByAttachedTo(id);
        if (workflow1 != null) {
            workflowService.deleteWorkflow(id);
        }
        if (workOrder != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(MROEnumObject.MROWORKORDER, workOrder.getId(), wfDef);
            workOrder.setWorkflow(workflow.getId());
            workOrder = mroWorkOrderRepository.save(workOrder);
        }
        return workflow;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getHierarchyWorkflows(Integer typeId, String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        MROObjectType objectType = mroObjectTypeRepository.findOne(typeId);
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
        List<PLMWorkflowDefinition> workflowDefinition1 = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
        if (workflowDefinition1.size() > 0) {
            workflowDefinition1.forEach(workflowDefinition -> {
                PLMWorkflowDefinitionMaster definitionMaster = workflowDefinitionMasterRepository.findOne(workflowDefinition.getMaster().getId());
                if (definitionMaster.getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                    workflowDefinitions.add(workflowDefinition);
                }
            });
        }
        if (objectType.getParentType() != null) {
            getWorkflowsFromHierarchy(workflowDefinitions, objectType.getParentType(), type);
        }
        return workflowDefinitions;
    }


    private void getWorkflowsFromHierarchy(List<PLMWorkflowDefinition> definitions, Integer typeId, String type) {
        MROObjectType mroObjectType = mroObjectTypeRepository.findOne(typeId);
        if (mroObjectType != null) {
            PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
            if (workflowType != null) {
                List<PLMWorkflowDefinition> workflowDefinition2 = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
                if (workflowDefinition2.size() > 0) {
                    workflowDefinition2.forEach(workflowDefinition -> {
                        PLMWorkflowDefinitionMaster definitionMaster = workflowDefinitionMasterRepository.findOne(workflowDefinition.getMaster().getId());
                        if (definitionMaster.getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                            definitions.add(workflowDefinition);
                        }
                    });
                }
            }
            if (mroObjectType.getParentType() != null) {
                getWorkflowsFromHierarchy(definitions, mroObjectType.getParentType(), type);
            }
        }
    }

/*
*
* Work Order Resources
* */

    @Transactional
    public List<MROWorkOrderResource> createWorkOrderResources(Integer workOrderId, List<MROWorkOrderResource> workOrderResources) {
        List<MROWorkOrderResource> orderParts = new ArrayList<>();
        workOrderResourceRepository.save(workOrderResources);
        //applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderSparePartCreatedEvent(workOrderId, orderParts));
        return workOrderResources;
    }


    @Transactional
    public void deleteWorkOrderResource(Integer workOrderResource) {
        MROWorkOrderResource orderPart = workOrderResourceRepository.findOne(workOrderResource);
        // applicationEventPublisher.publishEvent(new WorkOrderEvents.WorkOrderSparePartDeletedEvent(orderPart.getWorkOrder(), orderPart));
        workOrderResourceRepository.delete(workOrderResource);
    }

    @Transactional(readOnly = true)
    public List<MROWorkOrderResource> getAllWorkOrderResources(Integer workOrderId) {
        List<MROWorkOrderResource> resourceElements = workOrderResourceRepository.findByWorkOrder(workOrderId);
        MROWorkOrder workOrder = mroWorkOrderRepository.findOne(workOrderId);
        for (MROWorkOrderResource resource : resourceElements) {
            resource.setWorkOrderObject(workOrder);
            if (resource.getResourceType().toString().equals("MACHINE")) {
                MESMachine machine = machineRepository.findOne(resource.getResourceId());
                machine.setManufacturerData(manufacturerDataRepository.findOne(machine.getId()));
                resource.setResourceObject(machine);
                resource.setResourceName("Machine");
            } else if (resource.getResourceType().toString().equals("EQUIPMENT")) {
                MESEquipment equipment = equipmentRepository.findOne(resource.getResourceId());
                resource.setResourceObject(equipment);
                equipment.setManufacturerData(manufacturerDataRepository.findOne(equipment.getId()));
                resource.setResourceName("Equipment");
            } else if (resource.getResourceType().toString().equals("INSTRUMENT")) {
                MESInstrument instrument = instrumentRepository.findOne(resource.getResourceId());
                instrument.setManufacturerData(manufacturerDataRepository.findOne(instrument.getId()));
                resource.setResourceObject(instrument);
                resource.setResourceName("Instrument");
            } else if (resource.getResourceType().toString().equals("TOOL")) {
                MESTool tool = toolRepo.findOne(resource.getResourceId());
                resource.setResourceObject(tool);
                tool.setManufacturerData(manufacturerDataRepository.findOne(tool.getId()));
                resource.setResourceName("Tool");
            } else if (resource.getResourceType().toString().equals("MATERIAL")) {
                MESMaterial material = materialRepository.findOne(resource.getResourceId());
                resource.setResourceObject(material);
                material.setManufacturerData(manufacturerDataRepository.findOne(material.getId()));
                resource.setResourceName("Material");
            }
        }


        return resourceElements;
    }

    @Transactional
    public MROWorkOrderInstructions getWorkOrderInstructions(Integer workorderId) {
        return mroWorkOrderInstructionsRepository.findByworkOrder(workorderId);
    }
    
    @Transactional
    public MROWorkOrderInstructions createWorkOrderInstructions(Integer workorderId, MROWorkOrderInstructions instructions) {
        MROWorkOrderInstructions mroInstructions = mroWorkOrderInstructionsRepository.findByworkOrder(workorderId);
        if (mroInstructions != null) {
            mroInstructions.setInstructions(instructions.getInstructions());
            mroInstructions = mroWorkOrderInstructionsRepository.save(mroInstructions);
        } else {
            mroInstructions = mroWorkOrderInstructionsRepository.save(instructions);
        }
        return mroInstructions;
    }


}

