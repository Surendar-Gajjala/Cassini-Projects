package com.cassinisys.plm.service.wf;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.QLogin;
import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.repo.custom.CustomObjectRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SecurityPermissionService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.plm.event.PushNotificationEvents;
import com.cassinisys.plm.event.RequirementWorkflowEvents;
import com.cassinisys.plm.filtering.WorkflowCriteria;
import com.cassinisys.plm.filtering.WorkflowPredicateBuilder;
import com.cassinisys.plm.filtering.WorkflowStatusAssignmentPersonPredicateBuilder;
import com.cassinisys.plm.filtering.WorkflowStatusAssignmentPredicateBuilder;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.cm.dto.AffectedItemDto;
import com.cassinisys.plm.model.cm.dto.MCOProductAffectedItemDto;
import com.cassinisys.plm.model.dto.WorkflowDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mro.MROEnumObject;
import com.cassinisys.plm.model.mro.MROWorkOrder;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.LifecycleDto;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.req.PLMRequirementDocumentChildren;
import com.cassinisys.plm.model.rm.RecentlyVisited;
import com.cassinisys.plm.model.rm.Requirement;
import com.cassinisys.plm.model.rm.Specification;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.dto.WorkflowStatusAssignmentsDto;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.mro.MROWorkOrderRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.req.RequirementDocumentChildrenRepository;
import com.cassinisys.plm.repo.rm.RecentlyVisitedRepository;
import com.cassinisys.plm.repo.rm.RequirementRepository;
import com.cassinisys.plm.repo.rm.SpecificationRepository;
import com.cassinisys.plm.repo.wf.*;
import com.cassinisys.plm.service.cm.ECOService;
import com.cassinisys.plm.service.plm.BomService;
import com.cassinisys.plm.service.plm.ItemService;
import com.cassinisys.plm.service.tm.UserTaskEvents;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Service
public class PLMWorkflowService implements CrudService<PLMWorkflow, Integer> {

    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository plmWorkflowDefinitionRepository;
    @Autowired
    private PLMWorkflowStatusService plmWorkflowStatusService;
    @Autowired
    private PLMWorkflowTransitionService plmWorkflowTransitionService;
    @Autowired
    private PLMWorkflowStartRepository plmWorkflowStartRepository;
    @Autowired
    private PLMWorkflowFinishRepository plmWorkflowFinishRepository;
    @Autowired
    private PLMWorkFlowStatusAssignmentRepository plmWorkFlowStatusAssignmentRepository;
    @Autowired
    private PLMWorkFlowStatusObserverRepository plmWorkFlowStatusObserverRepository;
    @Autowired
    private PLMWorkFlowStatusAcknowledgerRepository plmWorkFlowStatusAcknowledgerRepository;
    @Autowired
    private PLMWorkflowStatusActionHistoryRepository plmWorkflowStatusActionHistoryRepository;
    @Autowired
    private PLMWorkFlowStatusApproverRepository plmWorkFlowStatusApproverRepository;
    @Autowired
    private PLMWorkflowStatusHistoryRepository plmWorkflowStatusHistoryRepository;
    @Autowired
    private AffectedItemRepository ecoAffectedItemRepository;
    @Autowired
    private ItemRevisionStatusHistoryRepository revisionStatusHistoryRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BomService bomService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private ECOService ecoService;
    @Autowired
    private MailService mailService;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private PLMWorkflowAttributeRepository workflowAttributeRepository;
    @Autowired
    private RecentlyVisitedRepository recentlyVisitedRepository;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private PLMWorkflowTransitionRepository workflowTransitionRepository;
    @Autowired
    private EMailTemplateConfigRepository eMailTemplateConfigRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private ChangeRepository changeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private PLMWorkFlowStatusAssignmentRepository workFlowStatusAssignmentRepository;
    @Autowired
    private PLMWorkFlowStatusApproverRepository workFlowStatusApproverRepository;
    @Autowired
    private PLMWorkFlowStatusAcknowledgerRepository workFlowStatusAcknowledgerRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProgramTemplateRepository programTemplateRepository;
    @Autowired
    private ProjectTemplateTaskRepository projectTemplateTaskRepository;
    @Autowired
    private ProjectTemplateActivityRepository projectTemplateActivityRepository;
    @Autowired
    private SpecificationRepository specificationRepository;
    @Autowired
    private RequirementRepository requirementRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private MessageSource messageSources;
    @Autowired
    private ChangeTypeRepository changeTypeRepository;
    @Autowired
    private WorkflowPredicateBuilder workflowPredicateBuilder;
    @Autowired
    private WorkflowStatusAssignmentPredicateBuilder workflowStatusAssignmentPredicateBuilder;
    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private DCRRepository dcrRepository;
    @Autowired
    private InspectionPlanRepository inspectionPlanRepository;
    @Autowired
    private ProductInspectionPlanRepository productInspectionPlanRepository;
    @Autowired
    private MaterialInspectionPlanRepository materialInspectionPlanRepository;
    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;
    @Autowired
    private InspectionRepository inspectionRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private NCRRepository ncrRepository;
    @Autowired
    private QCRRepository qcrRepository;
    @Autowired
    private VarianceRepository varianceRepository;
    @Autowired
    private MCORepository mcoRepository;
    @Autowired
    private ECRRepository ecrRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private WorkflowDefinitionMasterRepository workflowDefinitionMasterRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private ItemMCORepository itemMCORepository;
    @Autowired
    private ManufacturerMCORepository manufacturerMCORepository;
    @Autowired
    private MROWorkOrderRepository workOrderRepository;
    @Autowired
    private NprRepository nprRepository;
    @Autowired
    private SecurityPermissionService securityPermissionService;
    @Autowired
    private PLMUserTaskRepository userTaskRepository;
    @Autowired
    private PLMWorkflowEventRepository plmWorkflowEventRepository;
    @Autowired
    private PLMWorkflowDefinitionEventRepository plmWorkflowDefinitionEventRepository;
    @Autowired
    private CustomObjectRepository customObjectRepository;
    @Autowired
    private WorkflowStatusAssignmentPersonPredicateBuilder workflowStatusAssignmentPersonPredicateBuilder;
    @Autowired
    private PLMWorkflowActivityFormFieldsRepository workflowActivityFormFieldsRepository;
    @Autowired
    private PLMWorkflowActivityFormDataRepository workflowActivityFormDataRepository;
    @Autowired
    private SupplierAuditRepository supplierAuditRepository;
    @Autowired
    private RequirementDocumentChildrenRepository requirementDocumentChildrenRepository;
    @Autowired
    private ProjectTemplateRepository projectTemplateRepository;
    @Autowired
    private ProgramProjectRepository programProjectRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#plmWorkflow,'create')")
    public PLMWorkflow create(PLMWorkflow plmWorkflow) {
        return plmWorkflow;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#plmWorkflow.id ,'edit')")
    public PLMWorkflow update(PLMWorkflow plmWorkflow) {
        checkNotNull(plmWorkflow);
        checkNotNull(plmWorkflow.getId());
        return create(plmWorkflow);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        List<RecentlyVisited> recentlyVisiteds = recentlyVisitedRepository.findByObjectId(id);
        for (RecentlyVisited recentlyVisited : recentlyVisiteds) {
            recentlyVisitedRepository.delete(recentlyVisited.getId());
        }
        checkNotNull(id);
        plmWorkflowRepository.delete(id);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMWorkflow get(Integer id) {
        checkNotNull(id);
        PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(id);
        if (plmWorkflow == null) {
            throw new ResourceNotFoundException();
        }
        PLMWorkflowDefinition workflowDefinition = plmWorkflowDefinitionRepository.findOne(plmWorkflow.getWorkflowRevision());
        if (workflowDefinition != null) {
            plmWorkflow.setWorkflowTypeId(workflowDefinition.getWorkflowType().getId());
        }
        List<PLMWorkflowStatus> statuses = plmWorkflowStatusService.getByWorkflow(id);
        List<PLMWorkflowStatus> filtered = new ArrayList<>();
        statuses.forEach(s -> {
            if (s.getType() != WorkflowStatusType.UNDEFINED) {
                filtered.add(s);
            }
        });
        plmWorkflow.setStatuses(filtered);
        plmWorkflow.setTransitions(plmWorkflowTransitionService.getByWorkflow(id));
        return plmWorkflow;
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMWorkflow> getAll() {
        return plmWorkflowRepository.findAll();
    }

    @Transactional
    public PLMWorkflow attachWorkflow(Enum attachedToType, Integer attachedTo, PLMWorkflowDefinition workflowDefinition) {
        PLMWorkflow workflow = new PLMWorkflow();
        Integer workflowInstanceCount = workflowDefinition.getInstances();
        Integer workflowMaster = workflowDefinition.getMaster().getInstances();
        Map<Integer, PLMWorkflowStatus> mapStatuses = new HashMap<>();
        workflow.setName(workflowDefinition.getName());
        workflow.setDescription(workflowDefinition.getDescription());
        workflow.setAttachedTo(attachedTo);
        workflow.setAttachedToType(attachedToType);
        workflow.setDiagram(workflowDefinition.getDiagram());
        workflow.setDiagramId(workflowDefinition.getDiagramID());
        workflow.setWorkflowRevision(workflowDefinition.getId());
        PLMWorkflow savedWorkflow = plmWorkflowRepository.save(workflow);
        PLMWorkflowStart start = new PLMWorkflowStart();
        start.setWorkflow(savedWorkflow.getId());
        start.setName(workflowDefinition.getStart().getName());
        start.setDescription(workflowDefinition.getStart().getDescription());
        start.setDiagramId(workflowDefinition.getStart().getDiagramId());
        start.setType(workflowDefinition.getStart().getType());
        start = plmWorkflowStartRepository.save(start);
        savedWorkflow.setStart(start);
        mapStatuses.put(workflowDefinition.getStart().getId(), start);
        PLMWorkflowFinish finish = new PLMWorkflowFinish();
        finish.setWorkflow(savedWorkflow.getId());
        finish.setName(workflowDefinition.getFinish().getName());
        finish.setDescription(workflowDefinition.getFinish().getDescription());
        finish.setDiagramId(workflowDefinition.getFinish().getDiagramId());
        finish.setType(workflowDefinition.getFinish().getType());
        finish = plmWorkflowFinishRepository.save(finish);
        savedWorkflow.setFinish(finish);
        mapStatuses.put(workflowDefinition.getFinish().getId(), finish);
        List<PLMWorkflowDefinitionStatus> definitionStatuses = workflowDefinition.getStatuses();
        definitionStatuses.forEach(s -> {
            if (s.getType() != WorkflowStatusType.START && s.getType() != WorkflowStatusType.FINISH) {
                PLMWorkflowStatus status = null;
                if (s.getType() == WorkflowStatusType.TERMINATE) {
                    status = new PLMWorkflowTerminate();
                } else {
                    status = new PLMWorkflowStatus();
                }
                status.setWorkflow(savedWorkflow.getId());
                status.setName(s.getName());
                status.setDescription(s.getDescription());
                status.setDiagramId(s.getDiagramId());
                status.setType(s.getType());
                status = plmWorkflowStatusService.create(status);
                if (status.getType().equals(WorkflowStatusType.NORMAL) || status.getType().equals(WorkflowStatusType.RELEASED) || status.getType().equals(WorkflowStatusType.REJECTED)) {
                    List<PLMWorkflowActivityFormFields> activityFormFields = workflowActivityFormFieldsRepository.findByWorkflowActivityOrderByName(s.getId());
                    List<PLMWorkflowActivityFormFields> statusFormFields = new ArrayList<PLMWorkflowActivityFormFields>();
                    for (PLMWorkflowActivityFormFields activityFormField : activityFormFields) {
                        PLMWorkflowActivityFormFields formFields = JsonUtils.cloneEntity(activityFormField, PLMWorkflowActivityFormFields.class);
                        formFields.setId(null);
                        formFields.setWorkflowActivity(status.getId());
                        formFields.setObjectType(status.getObjectType());
                        statusFormFields.add(formFields);
                    }
                    if (statusFormFields.size() > 0) {
                        statusFormFields = workflowActivityFormFieldsRepository.save(statusFormFields);
                    }
                }
                savedWorkflow.getStatuses().add(status);
                mapStatuses.put(s.getId(), status);
            }
        });
        List<PLMWorkflowDefinitionTransition> definitionTransitions = workflowDefinition.getTransitions();
        definitionTransitions.forEach(t -> {
            PLMWorkflowTransition transition = new PLMWorkflowTransition();
            transition.setWorkflow(savedWorkflow.getId());
            transition.setDiagramId(t.getDiagramId());
            transition.setFromStatus(mapStatuses.get(t.getFromStatus()).getId());
            transition.setToStatus(mapStatuses.get(t.getToStatus()).getId());
            transition = plmWorkflowTransitionService.create(transition);
            savedWorkflow.getTransitions().add(transition);

        });
        workflowDefinition.setInstances(workflowInstanceCount + 1);
        PLMWorkflowDefinitionMaster definitionMaster = workflowDefinition.getMaster();
        definitionMaster.setInstances(workflowMaster + 1);
        plmWorkflowDefinitionRepository.save(workflowDefinition);
        workflowDefinitionMasterRepository.save(definitionMaster);
        savedWorkflow.setCurrentStatus(start.getId());

        List<PLMWorkflowDefinitionEvent> workflowDefinitionEvents = plmWorkflowDefinitionEventRepository.findByWorkflowOrderByIdAsc(workflowDefinition.getId());
        for (PLMWorkflowDefinitionEvent workflowDefinitionEvent : workflowDefinitionEvents) {
            PLMWorkflowEvent workflowEvent = new PLMWorkflowEvent();
            workflowEvent.setWorkflow(savedWorkflow.getId());
            workflowEvent.setEventType(workflowDefinitionEvent.getEventType());
            workflowEvent.setActionType(workflowDefinitionEvent.getActionType());
            if (workflowDefinitionEvent.getActivity() != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findByWorkflowAndNameAndType(savedWorkflow.getId(), workflowDefinitionEvent.getActivity().getName(), workflowDefinitionEvent.getActivity().getType());
                workflowEvent.setActivity(workflowStatus);
            }
            if (workflow.getAttachedToType().name().equals(PLMObjectType.BOPREVISION.name())) {
                workflowEvent.setActionData(workflowDefinitionEvent.getActionData());
            }
            if (workflowEvent.getActionType().equals("ExecuteScript")) {
                workflowEvent.setActionData(workflowDefinitionEvent.getActionData());
            }
            workflowEvent = plmWorkflowEventRepository.save(workflowEvent);
        }

        return plmWorkflowRepository.save(savedWorkflow);
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMWorkflow getAttachedWorkflow(Integer id) {
        return plmWorkflowRepository.findByAttachedTo(id);
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMWorkflow startWorkflow(Integer wfId) {
        PLMWorkflow workflow = plmWorkflowRepository.findOne(wfId);
        if (workflow != null) {
            List<PLMWorkflowTransition> list = plmWorkflowTransitionService.getByFromStatus(workflow.getStart().getId());
            if (list.size() == 1) {
                Integer loginId = sessionWrapper.getSession().getLogin().getPerson().getId();
                workflow.setStarted(Boolean.TRUE);
                workflow.setStartedOn(new Date());
                workflow.getStart().setFlag(WorkflowStatusFlag.COMPLETED);
                workflow.getStart().setCreatedBy(loginId);
                workflow.getStart().setModifiedBy(loginId);
                plmWorkflowStartRepository.save(workflow.getStart());
                PLMWorkflowStatus status = plmWorkflowStatusService.get(list.get(0).getToStatus());
                status.setFlag(WorkflowStatusFlag.INPROCESS);
                status.setCreatedBy(loginId);
                status.setModifiedBy(loginId);
                plmWorkflowStatusService.update(status);
                workflow.setCurrentStatus(status.getId());
                notifyUsers(workflow, status, false, WorkflowStatusType.NORMAL, WorkflowStatusFlag.UNASSIGNED, null, null, null, true);
            }
            if (workflow.getAttachedToType() != null && (workflow.getAttachedToType().name().equals("CHANGE") || workflow.getAttachedToType().name().equals("ITEMMCO"))) {
                PLMChange change = changeRepository.findOne(workflow.getAttachedTo());
                if (change != null && (change.getChangeType().equals(ChangeType.DCO) || change.getChangeType().equals(ChangeType.ECO) || change.getChangeType().equals(ChangeType.MCO))) {
                    List<PLMWorkflowStatus> workflowStatuses = plmWorkflowStatusRepository.findByWorkflowAndType(workflow.getId(), WorkflowStatusType.RELEASED);
                    for (PLMWorkflowStatus fromStatus : workflowStatuses) {
                        if (fromStatus.getType().equals(WorkflowStatusType.RELEASED)) {
                            PLMWorkflowEvent workflowEvent = plmWorkflowEventRepository.findByWorkflowAndActivityAndEventTypeAndActionType(workflow.getId(), fromStatus, "WorkflowActivityFinish", "SetLifecyclePhase");
                            if (workflowEvent != null) {
                                ObjectMapper objectMapper = new ObjectMapper();
                                if (change.getChangeType().equals(ChangeType.ECO) || change.getChangeType().equals(ChangeType.DCO)) {
                                    try {
                                        List<AffectedItemDto> affectedItemDto = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<AffectedItemDto>>() {
                                        });
                                        if (affectedItemDto.size() > 0) {
                                            for (AffectedItemDto itemDto : affectedItemDto) {
                                                if (itemDto.getToLifecyclePhase() == null) {
                                                    throw new CassiniException("One of the affected item does not have lifecycle phase in [ " + fromStatus.getName() + " ] workflow activity finish event");
                                                }
                                            }
                                        } else {
                                            throw new CassiniException("Please add workflow activity finish event to [ " + fromStatus.getName() + " ]  and set lifecycle phases to affected items");
                                        }
                                    } catch (JsonProcessingException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        List<MCOProductAffectedItemDto> affectedItemDto = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<MCOProductAffectedItemDto>>() {
                                        });
                                        if (affectedItemDto.size() > 0) {
                                            for (MCOProductAffectedItemDto itemDto : affectedItemDto) {
                                                if (itemDto.getToLifecyclePhase() == null) {
                                                    throw new CassiniException("One of the affected item does not have lifecycle phase in [ " + fromStatus.getName() + " ] workflow activity finish event");
                                                }
                                            }
                                        } else {
                                            throw new CassiniException("Please add workflow activity finish event to [ " + fromStatus.getName() + " ]  and set lifecycle phases to affected items");
                                        }
                                    } catch (JsonProcessingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                throw new CassiniException("Please add workflow activity finish event to [ " + fromStatus.getName() + " ]  and set lifecycle phases to affected items");
                            }
                        }
                    }
                }
            } else if (workflow.getAttachedToType() != null && (workflow.getAttachedToType().name().equals("BOPREVISION"))) {
                List<PLMWorkflowStatus> workflowStatuses = plmWorkflowStatusRepository.findByWorkflowAndType(workflow.getId(), WorkflowStatusType.RELEASED);
                for (PLMWorkflowStatus fromStatus : workflowStatuses) {
                    if (fromStatus.getType().equals(WorkflowStatusType.RELEASED)) {
                        PLMWorkflowEvent workflowEvent = plmWorkflowEventRepository.findByWorkflowAndActivityAndEventTypeAndActionType(workflow.getId(), fromStatus, "WorkflowActivityFinish", "SetLifecyclePhase");
                        if (workflowEvent != null) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            try {
                                List<LifecycleDto> lifecycleDtos = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<LifecycleDto>>() {
                                });
                                if (lifecycleDtos.size() > 0) {
                                    for (LifecycleDto itemDto : lifecycleDtos) {
                                        if (itemDto.getLifecyclePhase() == null) {
                                            throw new CassiniException("BOP does not have lifecycle phase in [ " + fromStatus.getName() + " ] workflow activity finish event");
                                        }
                                    }
                                } else {
                                    throw new CassiniException("Please add workflow activity finish event to [ " + fromStatus.getName() + " ]  and set lifecycle phase to BOP");
                                }
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            throw new CassiniException("Please add workflow activity finish event to [ " + fromStatus.getName() + " ]  and set lifecycle phase to BOP");
                        }
                    }
                }
            }

            workflow = plmWorkflowRepository.save(workflow);
            if (workflow.getAttachedToType().equals(PLMObjectType.REQUIREMENT)) {
                PLMRequirementDocumentChildren requirement = requirementDocumentChildrenRepository.findOne(workflow.getAttachedTo());
                applicationEventPublisher.publishEvent(new RequirementWorkflowEvents.ReqWorkflowStartedEvent(PLMObjectType.REQUIREMENT, requirement, workflow));
            } else {
                CassiniObject attachedTo = objectRepository.findById(workflow.getAttachedTo());
                if (attachedTo != null) {
                    applicationEventPublisher.publishEvent(new WorkflowEvents.WorkflowStartedEvent(attachedTo.getObjectType(), attachedTo, workflow));
                }
            }
        }

        return workflow;
    }

    private void checkApproversAndAcknowledgers(Integer currentStatus) {
        List<PLMWorkFlowStatusAssignment> workFlowStatusAssignments = workFlowStatusAssignmentRepository.findByStatusOrderByIdDesc(currentStatus);
        if (workFlowStatusAssignments.size() > 0) {
            for (PLMWorkFlowStatusAssignment workFlowStatusAssignment : workFlowStatusAssignments) {
                if (workFlowStatusAssignment.getAssignmentType().equals(WorkflowAssigementType.APPROVER)) {
                    List<PLMWorkFlowStatusApprover> statusAssignments = plmWorkFlowStatusApproverRepository.findByStatusAndPersonOrderByIdDesc(currentStatus, workFlowStatusAssignment.getPerson());
                    if (statusAssignments.get(0).getVote() == null || statusAssignments.get(0).getVote().equals(ApproverVote.REJECT)) {
                        throw new CassiniException(messageSources.getMessage("workflow_approved_msg", null, "Workflow status should be approved by all the users assigned", LocaleContextHolder.getLocale()));
                    }
                }
                if (workFlowStatusAssignment.getAssignmentType().equals(WorkflowAssigementType.ACKNOWLEDGER)) {
                    PLMWorkFlowStatusAcknowledger workFlowStatusAcknowledger = plmWorkFlowStatusAcknowledgerRepository.findOne(workFlowStatusAssignment.getId());
                    if (!workFlowStatusAcknowledger.isAcknowledged()) {
                        throw new CassiniException(messageSources.getMessage("workflow_acknowledged_msg", null, "Workflow status should be acknowledged by all the users assigned", LocaleContextHolder.getLocale()));
                    }
                }
            }
        }
    }


    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMWorkflow finishWorkflow(Integer wfId) {
        PLMWorkflow workflow = plmWorkflowRepository.findOne(wfId);

        PLMWorkflowStatus fromStatus = plmWorkflowStatusService.get(workflow.getCurrentStatus());
        checkApproversAndAcknowledgers(workflow.getCurrentStatus());
        if (workflow.getAttachedToType() != null && (workflow.getAttachedToType().name().equals("CHANGE") || workflow.getAttachedToType().name().equals("ITEMMCO"))) {
            PLMChange change = changeRepository.findOne(workflow.getAttachedTo());
            if (change != null && (change.getChangeType().equals(ChangeType.DCO) || change.getChangeType().equals(ChangeType.ECO) || change.getChangeType().equals(ChangeType.MCO))) {
                if (fromStatus.getType().equals(WorkflowStatusType.RELEASED)) {
                    PLMWorkflowEvent workflowEvent = plmWorkflowEventRepository.findByWorkflowAndActivityAndEventTypeAndActionType(workflow.getId(), fromStatus, "WorkflowActivityFinish", "SetLifecyclePhase");
                    if (workflowEvent != null) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        if (change.getChangeType().equals(ChangeType.DCO) || change.getChangeType().equals(ChangeType.ECO)) {
                            try {
                                List<AffectedItemDto> affectedItemDto = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<AffectedItemDto>>() {
                                });
                                if (affectedItemDto.size() > 0) {
                                    for (AffectedItemDto itemDto : affectedItemDto) {
                                        if (itemDto.getToLifecyclePhase() == null) {
                                            throw new CassiniException("One of the affected item does not have lifecycle phase in [ " + fromStatus.getName() + " ] workflow activity finish event");
                                        }
                                    }
                                } else {
                                    throw new CassiniException("Please add workflow activity finish event to [ " + fromStatus.getName() + " ] and set lifecycle phases to affected items");
                                }
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                List<MCOProductAffectedItemDto> affectedItemDto = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<MCOProductAffectedItemDto>>() {
                                });
                                if (affectedItemDto.size() > 0) {
                                    for (MCOProductAffectedItemDto itemDto : affectedItemDto) {
                                        if (itemDto.getToLifecyclePhase() == null) {
                                            throw new CassiniException("One of the affected item does not have lifecycle phase in [ " + fromStatus.getName() + " ] workflow activity finish event");
                                        }
                                    }
                                } else {
                                    throw new CassiniException("Please add workflow activity finish event to [ " + fromStatus.getName() + " ] and set lifecycle phases to affected items");
                                }
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        throw new CassiniException("Please add workflow activity finish event to [ " + fromStatus.getName() + " ] and set lifecycle phases to affected items");
                    }
                }
            }
        }
        PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getCurrentStatus());
        checkWorkflowActivityFormData(workflowStatus);
        List<PLMWorkflowTransition> list = workflowTransitionRepository.findByFromStatusAndToStatus(workflow.getCurrentStatus(), workflow.getFinish().getId());
        if (list.size() == 1) {
            PLMWorkflowStatus status = plmWorkflowStatusService.get(list.get(0).getFromStatus());
            if (workflow.getAttachedToType().equals(PLMObjectType.REQUIREMENT)) {
                PLMRequirementDocumentChildren requirement = requirementDocumentChildrenRepository.findOne(workflow.getAttachedTo());
                applicationEventPublisher.publishEvent(new RequirementWorkflowEvents.ReqWorkflowFinishedEvent(PLMObjectType.REQUIREMENT, requirement, workflow, status));
            } else {
                CassiniObject attachedTo = objectRepository.findById(workflow.getAttachedTo());
                if (attachedTo != null) {
                    applicationEventPublisher.publishEvent(new WorkflowEvents.WorkflowFinishedEvent(attachedTo.getObjectType(), attachedTo, workflow, status));
                }
            }
            Integer loginId = sessionWrapper.getSession().getLogin().getPerson().getId();
            workflow.setFinished(Boolean.TRUE);
            Date finished = new Date();
            workflow.setFinishedOn(finished);
            workflow.getFinish().setFlag(WorkflowStatusFlag.COMPLETED);
            workflow.getFinish().setCreatedBy(loginId);
            plmWorkflowFinishRepository.save(workflow.getFinish());
            status.setFlag(WorkflowStatusFlag.COMPLETED);
            status.setCreatedBy(loginId);
            status.setModifiedBy(loginId);
            plmWorkflowStatusService.update(status);

            PLMWorkflowStatusHistory history = new PLMWorkflowStatusHistory();
            history.setWorkflow(workflow.getId());
            history.setStatus(status.getId());
            history.setTimestamp(finished);
            List<PLMWorkFlowStatusAssignment> assignments = plmWorkFlowStatusAssignmentRepository.findByStatusOrderByIdDesc(fromStatus.getId());
            history.setAssignments(JsonUtils.toJson(assignments));
            plmWorkflowStatusHistoryRepository.save(history);
            workflow = plmWorkflowRepository.save(workflow);
            checkUserTasksStatus(workflow);
        }

        return workflow;
    }

    private void checkUserTasksStatus(PLMWorkflow workflow) {
        List<PLMUserTask> userTasks = userTaskRepository.findByContextAndStatus(workflow.getId(), UserTaskStatus.PENDING);
        if (userTasks.size() > 0) {
            userTasks.forEach(plmUserTask -> {
                plmUserTask.setStatus(UserTaskStatus.CANCELLED);
            });
            userTaskRepository.save(userTasks);
        }

    }

    @Transactional
    public PLMWorkflowStatus promoteWorkflow(Integer fromStatusId, Integer toStatusId) {
        checkApproversAndAcknowledgers(fromStatusId);

        PLMWorkflowStatus fromStatus = plmWorkflowStatusService.get(fromStatusId);
        PLMWorkflowStatus toStatus = plmWorkflowStatusService.get(toStatusId);
        PLMWorkflow workflow = plmWorkflowRepository.findOne(fromStatus.getWorkflow());
        Integer loginId = sessionWrapper.getSession().getLogin().getPerson().getId();
        fromStatus.setFlag(WorkflowStatusFlag.COMPLETED);
        fromStatus.setCreatedBy(loginId);
        if (fromStatus.getType().equals(WorkflowStatusType.NORMAL) ||
                fromStatus.getType().equals(WorkflowStatusType.RELEASED) ||
                fromStatus.getType().equals(WorkflowStatusType.REJECTED)) {
            fromStatus.setFinishedTimestamp(new Date());
        }
        if (workflow.getAttachedToType() != null && (workflow.getAttachedToType().name().equals("CHANGE") || workflow.getAttachedToType().name().equals("ITEMMCO"))) {
            PLMChange change = changeRepository.findOne(workflow.getAttachedTo());
            if (change != null && (change.getChangeType().equals(ChangeType.DCO) || change.getChangeType().equals(ChangeType.ECO) || change.getChangeType().equals(ChangeType.MCO))) {
                if (fromStatus.getType().equals(WorkflowStatusType.RELEASED)) {
                    PLMWorkflowEvent workflowEvent = plmWorkflowEventRepository.findByWorkflowAndActivityAndEventTypeAndActionType(workflow.getId(), fromStatus, "WorkflowActivityFinish", "SetLifecyclePhase");
                    if (workflowEvent != null) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        if (change.getChangeType().equals(ChangeType.DCO) || change.getChangeType().equals(ChangeType.ECO)) {
                            try {
                                List<AffectedItemDto> affectedItemDto = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<AffectedItemDto>>() {
                                });
                                if (affectedItemDto.size() > 0) {
                                    for (AffectedItemDto itemDto : affectedItemDto) {
                                        if (itemDto.getToLifecyclePhase() == null) {
                                            throw new CassiniException("One of the affected item does not have lifecycle phase in [ " + fromStatus.getName() + " ] workflow activity finish event");
                                        }
                                    }
                                } else {
                                    throw new CassiniException("Please add workflow activity finish event to [ " + fromStatus.getName() + " ] and set lifecycle phases to affected items");
                                }
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                List<MCOProductAffectedItemDto> affectedItemDto = objectMapper.readValue(workflowEvent.getActionData(), new TypeReference<List<MCOProductAffectedItemDto>>() {
                                });
                                if (affectedItemDto.size() > 0) {
                                    for (MCOProductAffectedItemDto itemDto : affectedItemDto) {
                                        if (itemDto.getToLifecyclePhase() == null) {
                                            throw new CassiniException("One of the affected item does not have lifecycle phase in [ " + fromStatus.getName() + " ] workflow activity finish event");
                                        }
                                    }
                                } else {
                                    throw new CassiniException("Please add workflow activity finish event to [ " + fromStatus.getName() + " ] and set lifecycle phases to affected items");
                                }
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        throw new CassiniException("Please add workflow activity finish event to [ " + fromStatus.getName() + " ] and set lifecycle phases to affected items");
                    }
                }
            }
        }

        checkWorkflowActivityFormData(fromStatus);
        plmWorkflowStatusService.update(fromStatus);

        toStatus.setTransitionedFrom(fromStatus.getId());
        toStatus.setIteration(toStatus.getIteration() + 1);
        if (toStatus.getType().equals(WorkflowStatusType.NORMAL) ||
                toStatus.getType().equals(WorkflowStatusType.RELEASED) ||
                toStatus.getType().equals(WorkflowStatusType.REJECTED)) {
            toStatus.setStartedTimestamp(new Date());
        }
        plmWorkflowStatusService.update(toStatus);


        if (workflow.getAttachedToType().equals(PLMObjectType.REQUIREMENT)) {
            PLMRequirementDocumentChildren requirement = requirementDocumentChildrenRepository.findOne(workflow.getAttachedTo());
            applicationEventPublisher.publishEvent(new RequirementWorkflowEvents.ReqWorkflowPromotedEvent(PLMObjectType.REQUIREMENT, requirement, workflow, fromStatus, toStatus));
        } else {
            CassiniObject attachedTo = objectRepository.findById(workflow.getAttachedTo());
            if (attachedTo != null) {
                applicationEventPublisher.publishEvent(new WorkflowEvents.WorkflowPromotedEvent(attachedTo.getObjectType(), attachedTo, workflow, fromStatus, toStatus));
            }
        }

        if (toStatus.getType() == WorkflowStatusType.TERMINATE) {
            toStatus.setFlag(WorkflowStatusFlag.TERMINATED);
            workflow.setCancelled(Boolean.TRUE);
            workflow.setCancelledOn(new Date());
            checkUserTasksStatus(workflow);
        } else {
            toStatus.setFlag(WorkflowStatusFlag.INPROCESS);
        }
        toStatus.setCreatedBy(loginId);
        plmWorkflowStatusService.update(toStatus);
        workflow.setCurrentStatus(toStatusId);
        workflow = plmWorkflowRepository.save(workflow);
        PLMWorkflowStatusHistory history = new PLMWorkflowStatusHistory();
        history.setWorkflow(workflow.getId());
        history.setStatus(fromStatusId);
        history.setTimestamp(new Date());
        List<PLMWorkFlowStatusAssignment> assignments = plmWorkFlowStatusAssignmentRepository.findByStatusOrderByIdDesc(fromStatusId);
        history.setAssignments(JsonUtils.toJson(assignments));
        plmWorkflowStatusHistoryRepository.save(history);

        reAddWorkflowAssignments(toStatus);

        if (toStatus.getType() != WorkflowStatusType.TERMINATE) {
            notifyUsers(workflow, toStatus, false, WorkflowStatusType.NORMAL, WorkflowStatusFlag.UNASSIGNED, null, null, null, true);
        }

        return toStatus;
    }

    private void checkWorkflowActivityFormData(PLMWorkflowStatus fromStatus) {
        List<PLMWorkflowActivityFormFields> workflowActivityFormFields = workflowActivityFormFieldsRepository.findByWorkflowActivityAndRequiredTrue(fromStatus.getId());
        workflowActivityFormFields.forEach(workflowActivityFormField -> {
            PLMWorkflowActivityFormData activityFormData = workflowActivityFormDataRepository.findByWorkflowActivityAndAttribute(fromStatus.getId(), workflowActivityFormField.getId());
            if (activityFormData != null) {
                if ((activityFormData.getStringValue() == null || activityFormData.getStringValue().equals("")) && (activityFormData.getIntegerValue() == null) && (activityFormData.getDoubleValue() == null)
                        && (activityFormData.getDateValue() == null) && (activityFormData.getTimeValue() == null) && (activityFormData.getTimestampValue() == null) && (activityFormData.getRichTextValue() == null || activityFormData.getRichTextValue().equals(""))
                        && (activityFormData.getLongTextValue() == null || activityFormData.getLongTextValue().equals("")) && (activityFormData.getListValue() == null || activityFormData.getListValue().equals(""))
                        && (activityFormData.getMListValue() == null || activityFormData.getMListValue().length == 0) && (activityFormData.getMListValue() == null || activityFormData.getMListValue().length == 0)
                        && (activityFormData.getRefValue() == null) && (activityFormData.getImageValue() == null) && (activityFormData.getAttachmentValues() == null || activityFormData.getAttachmentValues().length == 0)
                        && (activityFormData.getHyperLinkValue() == null || activityFormData.getHyperLinkValue().equals("")) && (activityFormData.getCurrencyValue() == null) && (activityFormData.getBooleanValue() == null)) {
                    throw new CassiniException(" [ " + fromStatus.getName() + " ] activity form data is missing");
                }
            } else {
                throw new CassiniException(" [ " + fromStatus.getName() + " ] activity form data is missing");
            }
        });
    }

    private void reAddWorkflowAssignments(PLMWorkflowStatus toStatus) {
        PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(toStatus.getWorkflow());
        List<Integer> added = new ArrayList<>();
        List<PLMWorkFlowStatusAssignment> workFlowStatusAssignments = workFlowStatusAssignmentRepository.findByStatusOrderByIdDesc(toStatus.getId());
        workFlowStatusAssignments.forEach(assignment -> {
            if (!added.contains(assignment.getPerson())) {
                added.add(assignment.getPerson());
                if (assignment.getAssignmentType().equals(WorkflowAssigementType.APPROVER)) {
                    if (workFlowStatusApproverRepository.findByStatusAndPersonAndVoteIsNull(toStatus.getId(), assignment.getPerson()) == null) {
                        PLMWorkFlowStatusApprover approver = new PLMWorkFlowStatusApprover();
                        approver.setPerson(assignment.getPerson());
                        approver.setStatus(assignment.getStatus());
                        workFlowStatusApproverRepository.save(approver);

                        Person person = personRepository.findOne(assignment.getPerson());
                        applicationEventPublisher.publishEvent(new UserTaskEvents.WorkflowTaskAssignedEvent(person, plmWorkflow, toStatus));
                        CassiniObject cassiniObject = objectRepository.findById(plmWorkflow.getAttachedTo());
                        securityPermissionService.addLoginSecurityPermission(person.getId(), cassiniObject, "all");
                    }
                } else if (assignment.getAssignmentType().equals(WorkflowAssigementType.ACKNOWLEDGER)) {
                    if (workFlowStatusAcknowledgerRepository.findByStatusAndPersonAndAcknowledgedIsFalse(toStatus.getId(), assignment.getPerson()) == null) {
                        PLMWorkFlowStatusAcknowledger acknowledger = new PLMWorkFlowStatusAcknowledger();
                        acknowledger.setPerson(assignment.getPerson());
                        acknowledger.setStatus(assignment.getStatus());
                        workFlowStatusAcknowledgerRepository.save(acknowledger);
                    }
                }
            }
        });
    }

    @Transactional
    public PLMWorkflowStatus demoteWorkflow(int wfId) {
        PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(wfId);
        PLMWorkflowStatus currentStatus = plmWorkflowStatusRepository.findOne(plmWorkflow.getCurrentStatus());
        if (currentStatus != null && currentStatus.getTransitionedFrom() != null) {
            PLMWorkflowStatus demotedToStatus = plmWorkflowStatusRepository.findOne(currentStatus.getTransitionedFrom());
            if (demotedToStatus != null) {
                currentStatus.setFlag(WorkflowStatusFlag.UNASSIGNED);
                demotedToStatus.setFlag(WorkflowStatusFlag.INPROCESS);
                demotedToStatus.setIteration(demotedToStatus.getIteration() + 1);
                plmWorkflowStatusRepository.save(Arrays.asList(currentStatus, demotedToStatus));

                plmWorkflow.setCurrentStatus(demotedToStatus.getId());
                plmWorkflowRepository.save(plmWorkflow);

                PLMWorkflowStatusHistory history = new PLMWorkflowStatusHistory();
                history.setWorkflow(plmWorkflow.getId());
                history.setStatus(currentStatus.getId());
                history.setTimestamp(new Date());
                history.setDemoted(Boolean.TRUE);
                plmWorkflowStatusHistoryRepository.save(history);

                reAddWorkflowAssignments(demotedToStatus);

                notifyUsers(plmWorkflow, demotedToStatus, true, WorkflowStatusType.NORMAL, WorkflowStatusFlag.UNASSIGNED, null, null, null, true);

                if (plmWorkflow.getAttachedToType().equals(PLMObjectType.REQUIREMENT)) {
                    PLMRequirementDocumentChildren requirement = requirementDocumentChildrenRepository.findOne(plmWorkflow.getAttachedTo());
                    applicationEventPublisher.publishEvent(new RequirementWorkflowEvents.ReqWorkflowDemotedEvent(PLMObjectType.REQUIREMENT, requirement, plmWorkflow, currentStatus, demotedToStatus));
                } else {
                    CassiniObject attachedTo = objectRepository.findById(plmWorkflow.getAttachedTo());
                    if (attachedTo != null) {
                        applicationEventPublisher.publishEvent(new WorkflowEvents.WorkflowDemotedEvent(attachedTo.getObjectType(), attachedTo,
                                plmWorkflow, currentStatus, demotedToStatus));
                    }
                }
                return demotedToStatus;
            }
        }

        return null;
    }

    @Transactional
    public Boolean putWorkflowOnHold(Integer currentStatusId, String notes) {
        PLMWorkflowStatus currentStatus = plmWorkflowStatusService.get(currentStatusId);
        PLMWorkflow workflow = plmWorkflowRepository.findOne(currentStatus.getWorkflow());
        currentStatus.setFlag(WorkflowStatusFlag.ONHOLD);
        plmWorkflowStatusRepository.save(currentStatus);
        workflow.setOnhold(Boolean.TRUE);
        workflow.setOnholdOn(new Date());
        workflow.setHoldBy(sessionWrapper.getSession().getLogin().getPerson().getId());
        plmWorkflowRepository.save(workflow);

        PLMWorkflowStatusHistory history = new PLMWorkflowStatusHistory();
        history.setWorkflow(workflow.getId());
        history.setStatus(currentStatus.getId());
        history.setTimestamp(new Date());
        history.setHold(Boolean.TRUE);
        history.setNotes(notes);
        plmWorkflowStatusHistoryRepository.save(history);

        if (workflow.getAttachedToType().equals(PLMObjectType.REQUIREMENT)) {
            PLMRequirementDocumentChildren requirement = requirementDocumentChildrenRepository.findOne(workflow.getAttachedTo());
            applicationEventPublisher.publishEvent(new RequirementWorkflowEvents.ReqWorkflowHoldEvent(PLMObjectType.REQUIREMENT, requirement, workflow, currentStatus));
        } else {
            CassiniObject attachedTo = objectRepository.findById(workflow.getAttachedTo());
            if (attachedTo != null) {
                applicationEventPublisher.publishEvent(new WorkflowEvents.WorkflowHoldEvent(attachedTo.getObjectType(), attachedTo,
                        workflow, currentStatus));
            }
        }

        return true;
    }

    @Transactional
    public Boolean removeWorkflowOnHold(Integer currentStatusId, String notes) {
        PLMWorkflowStatus currentStatus = plmWorkflowStatusService.get(currentStatusId);
        PLMWorkflow workflow = plmWorkflowRepository.findOne(currentStatus.getWorkflow());
        currentStatus.setFlag(WorkflowStatusFlag.INPROCESS);
        plmWorkflowStatusRepository.save(currentStatus);
        workflow.setOnhold(Boolean.FALSE);
        workflow.setOnholdOn(null);
        workflow.setHoldBy(null);
        plmWorkflowRepository.save(workflow);

        PLMWorkflowStatusHistory history = new PLMWorkflowStatusHistory();
        history.setWorkflow(workflow.getId());
        history.setStatus(currentStatus.getId());
        history.setTimestamp(new Date());
        history.setUnhold(Boolean.TRUE);
        history.setNotes(notes);
        plmWorkflowStatusHistoryRepository.save(history);

        if (workflow.getAttachedToType().equals(PLMObjectType.REQUIREMENT)) {
            PLMRequirementDocumentChildren requirement = requirementDocumentChildrenRepository.findOne(workflow.getAttachedTo());
            applicationEventPublisher.publishEvent(new RequirementWorkflowEvents.ReqWorkflowUnHoldEvent(PLMObjectType.REQUIREMENT, requirement, workflow, currentStatus));
        } else {
            CassiniObject attachedTo = objectRepository.findById(workflow.getAttachedTo());
            if (attachedTo != null) {
                applicationEventPublisher.publishEvent(new WorkflowEvents.WorkflowUnHoldEvent(attachedTo.getObjectType(), attachedTo,
                        workflow, currentStatus));
            }
        }

        return true;
    }


    public PLMWorkflowStatus getWorkflowStatus(Integer id) {
        return plmWorkflowStatusService.get(id);
    }

    public List<PLMWorkflow> getMultipleWorkflows(List<Integer> ids) {
        return plmWorkflowRepository.findByIdIn(ids);
    }

    public List<PLMWorkFlowStatusApprover> getApprovers(Integer statusId) {
        return plmWorkFlowStatusApproverRepository.findByStatus(statusId);
    }

    @Transactional
    public List<PLMWorkFlowStatusApprover> addApprovers(Integer statusId, List<PLMWorkFlowStatusApprover> approvers) {
        PLMWorkflowStatus status = plmWorkflowStatusRepository.findOne(statusId);
        List<Person> persons = new ArrayList<>();
        if (status != null) {
            PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(status.getWorkflow());

            for (PLMWorkFlowStatusApprover approver : approvers) {
                if (approver.getVote() != null) {
                    approver.setTimeStamp(new Date());
                }
                if (approver.getId() == null) {
                    Person person = personRepository.findOne(approver.getPerson());
                    persons.add(person);
                    applicationEventPublisher.publishEvent(new UserTaskEvents.WorkflowTaskAssignedEvent(person, plmWorkflow, status));
                    CassiniObject cassiniObject = objectRepository.findById(plmWorkflow.getAttachedTo());
                    securityPermissionService.addLoginSecurityPermission(person.getId(), cassiniObject, "all");
                }
            }
            if (status.getFlag().equals(WorkflowStatusFlag.INPROCESS)) {
                notifyUsers(plmWorkflow, status, false, WorkflowStatusType.NORMAL, WorkflowStatusFlag.INPROCESS, persons, WorkflowAssigementType.APPROVER, null, false);
            }
        }
        approvers = plmWorkFlowStatusApproverRepository.save(approvers);
        approvers.forEach(approver -> {
            approver.setPersonName(personRepository.findOne(approver.getPerson()).getFullName());
        });
        return approvers;
    }

    @Transactional
    public PLMWorkFlowStatusApprover updateApprover(PLMWorkFlowStatusApprover approver) {
        if (approver.getVote() != null) {
            approver.setTimeStamp(new Date());

            PLMWorkflowStatus status = plmWorkflowStatusRepository.findOne(approver.getStatus());
            approver.setIteration(status.getIteration());

            Person person = personRepository.findOne(approver.getPerson());
            PLMWorkflow workflow = plmWorkflowRepository.findOne(status.getWorkflow());
            applicationEventPublisher.publishEvent(new UserTaskEvents.WorkflowTaskFinishedEvent(person, workflow, status));
            CassiniObject cassiniObject = objectRepository.findById(workflow.getAttachedTo());
            securityPermissionService.addLoginSecurityPermission(person.getId(), cassiniObject, "all");
            if (approver.getVote().equals(ApproverVote.APPROVE)) {
                notifyUsers(workflow, status, false, WorkflowStatusType.RELEASED, WorkflowStatusFlag.UNASSIGNED, null, WorkflowAssigementType.APPROVER, person, false);
            } else {
                notifyUsers(workflow, status, false, WorkflowStatusType.REJECTED, WorkflowStatusFlag.UNASSIGNED, null, WorkflowAssigementType.APPROVER, person, false);
            }
        }
        return plmWorkFlowStatusApproverRepository.save(approver);
    }

    @Transactional
    public PLMWorkFlowStatusObserver updateObserver(PLMWorkFlowStatusObserver observer) {
        if (observer.getComments() != null) {
            observer.setTimeStamp(new Date());
        }
        PLMWorkflowStatus status = plmWorkflowStatusRepository.findOne(observer.getStatus());
        observer.setIteration(status.getIteration());
        Person person = personRepository.findOne(observer.getPerson());
        PLMWorkflow workflow = plmWorkflowRepository.findOne(status.getWorkflow());
        if (observer.getComments() != null) {
            notifyUsers(workflow, status, false, WorkflowStatusType.RELEASED, WorkflowStatusFlag.UNASSIGNED, null, WorkflowAssigementType.OBSERVER, person, false);
        }
        return plmWorkFlowStatusObserverRepository.save(observer);
    }

    @Transactional
    public PLMWorkFlowStatusAcknowledger updateAcknowledger(PLMWorkFlowStatusAcknowledger acknowledger) {
        if (acknowledger.isAcknowledged()) {
            acknowledger.setTimeStamp(new Date());
        }
        PLMWorkflowStatus status = plmWorkflowStatusRepository.findOne(acknowledger.getStatus());
        acknowledger.setIteration(status.getIteration());
        Person person = personRepository.findOne(acknowledger.getPerson());
        PLMWorkflow workflow = plmWorkflowRepository.findOne(status.getWorkflow());
        if (acknowledger.isAcknowledged()) {
            notifyUsers(workflow, status, false, WorkflowStatusType.RELEASED, WorkflowStatusFlag.UNASSIGNED, null, WorkflowAssigementType.ACKNOWLEDGER, person, false);
        }

        return plmWorkFlowStatusAcknowledgerRepository.save(acknowledger);
    }

    public List<PLMWorkFlowStatusObserver> getObservers(Integer statusId) {
        return plmWorkFlowStatusObserverRepository.findByStatus(statusId);
    }

    @Transactional
    public List<PLMWorkFlowStatusObserver> addObservers(Integer statusId, List<PLMWorkFlowStatusObserver> observers) {
        PLMWorkflowStatus status = plmWorkflowStatusRepository.findOne(statusId);
        PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(status.getWorkflow());
        List<Person> persons = new ArrayList<>();
        observers.forEach(observer -> {
            if (observer.getId() == null) {
                Person person = personRepository.findOne(observer.getPerson());
                observer.setPersonName(person.getFullName());
                persons.add(person);
            }
        });
        observers = plmWorkFlowStatusObserverRepository.save(observers);
        if (status.getFlag().equals(WorkflowStatusFlag.INPROCESS)) {
            notifyUsers(plmWorkflow, status, false, WorkflowStatusType.NORMAL, WorkflowStatusFlag.INPROCESS, persons, WorkflowAssigementType.OBSERVER, null, false);
        }
        //notifyObserversAndAcknowledger(plmWorkflow, status, "OBSERVER", persons);
        return observers;
    }


    public List<PLMWorkFlowStatusAcknowledger> getAcknowledgers(Integer statusId) {
        return plmWorkFlowStatusAcknowledgerRepository.findByStatus(statusId);
    }

    @Transactional
    public List<PLMWorkFlowStatusAcknowledger> addAcknowledgers(Integer statusId, List<PLMWorkFlowStatusAcknowledger> acknowledgers) {
        PLMWorkflowStatus status = plmWorkflowStatusRepository.findOne(statusId);
        List<Person> persons = new ArrayList<>();
        if (status != null) {
            PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(status.getWorkflow());
            for (PLMWorkFlowStatusAcknowledger approver : acknowledgers) {
                if (approver.isAcknowledged()) {
                    approver.setTimeStamp(new Date());
                }
                if (approver.getId() == null) {
                    Person person = personRepository.findOne(approver.getPerson());
                    persons.add(person);
                    applicationEventPublisher.publishEvent(new UserTaskEvents.WorkflowTaskAssignedEvent(person, plmWorkflow, status));
                    CassiniObject cassiniObject = objectRepository.findById(plmWorkflow.getAttachedTo());
                    securityPermissionService.addLoginSecurityPermission(person.getId(), cassiniObject, "all");
                }

            }
            if (status.getFlag().equals(WorkflowStatusFlag.INPROCESS)) {
                notifyUsers(plmWorkflow, status, false, WorkflowStatusType.NORMAL, WorkflowStatusFlag.INPROCESS, persons, WorkflowAssigementType.ACKNOWLEDGER, null, false);
            }

        }
        acknowledgers = plmWorkFlowStatusAcknowledgerRepository.save(acknowledgers);
        acknowledgers.forEach(acknowledger -> {
            acknowledger.setPersonName(personRepository.findOne(acknowledger.getPerson()).getFullName());
        });
        return acknowledgers;
    }


    @Transactional
    public void removeWorkflowAssignment(Integer id) {
        PLMWorkFlowStatusAssignment workFlowStatusAssignment = plmWorkFlowStatusAssignmentRepository.findOne(id);
        Person person = personRepository.findOne(workFlowStatusAssignment.getPerson());
        PLMWorkflowStatus plmWorkflowStatus = plmWorkflowStatusRepository.findOne(workFlowStatusAssignment.getStatus());
        PLMWorkflow workflow = plmWorkflowRepository.findOne(plmWorkflowStatus.getWorkflow());

        applicationEventPublisher.publishEvent(new UserTaskEvents.WorkflowTaskDeletedEvent(person, workflow, plmWorkflowStatus));

        plmWorkFlowStatusAssignmentRepository.delete(id);
    }


    public List<PLMWorkflowStatusHistory> getWorkflowHistory(Integer wfId) {
        List<PLMWorkflowStatusHistory> history = plmWorkflowStatusHistoryRepository.findByWorkflowOrderByTimestampDesc(wfId);
        history.forEach(h -> {
            h.setStatusObject(plmWorkflowStatusService.get(h.getStatus()));
            //h.setWorkflowStatusAssignments(plmWorkFlowStatusAssignmentRepository.findByStatus(h.getStatus()));
        });
        return history;
    }

    private void notifyObserversAndAcknowledger(PLMWorkflow workflow, PLMWorkflowStatus status, String type, List<Person> personList) {
        List<Integer> list = new ArrayList<>();
        List<Person> persons = new ArrayList<>();
        CassiniObject attachedObject = objectRepository.findById(workflow.getAttachedTo());
        for (Person person : personList) {
            if (isValidMailAddress(person.getEmail())) {
                persons.add(person);
                list.add(person.getId());
            }
        }
        String[] recipientAddress = new String[persons.size()];
        List<String> tokens = new ArrayList();
        String email = "";
        if (persons.size() > 0) {
            for (int i = 0; i < persons.size(); i++) {
                Person person = persons.get(i);
                if (email == "") {
                    email = person.getEmail();
                } else {
                    email = email + "," + person.getEmail();
                }
                if (person.getMobileDevice() != null && person.getMobileDevice().getDeviceId() != null) {
                    tokens.add(person.getMobileDevice().getDeviceId());
                }

            }
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            URL companyLogo = null;
            String templatePath = null;
            EmailTemplateConfiguration emailTemplateConfiguration = eMailTemplateConfigRepository.findByTemplateName("workflowApprover.html");
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
            if (emailTemplateConfiguration != null) {
                if (emailTemplateConfiguration.getTemplateSourceCode() != null && emailTemplateConfiguration.getTemplateSourceCode() != "") {
                    byte[] data = DatatypeConverter.parseBase64Binary(emailTemplateConfiguration.getTemplateSourceCode());
                    URL url1 = ItemService.class
                            .getClassLoader().getResource("templates/email/share/" + "customTemplate.html");
                    File file = new File(url1.getPath());
                    try {
                        OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
                        Writer writer = new OutputStreamWriter(outputStream);
                        writer.write(emailTemplateConfiguration.getTemplateSourceCode());
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    templatePath = "email/share/customTemplate.html";
                } else {
                    templatePath = "email/workflow/workflowEmailTemplates.html";
                }
            } else {
                templatePath = "email/workflow/workflowEmailTemplates.html";
            }
            boolean observer = false;
            boolean approver = false;
            boolean acknowledger = false;
            if (type.equals("APPROVER")) {
                approver = true;
            } else if (type.equals("ACKNOWLEDGER")) {
                acknowledger = true;
            } else if (type.equals("OBSERVER")) {
                observer = true;
            }
            final String templatePaths = templatePath;
            final URL companyLogos = companyLogo;
            final boolean approvers = approver;
            final boolean acknowledgers = acknowledger;
            final boolean observers = observer;

            HashMap model = new HashMap();
                /*CassiniObject attachedObject = objectRepository.findById(workflow.getAttachedTo());*/
            if (attachedObject != null) {
                if (attachedObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMREVISION.toString()))) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(attachedObject.getId());
                    CassiniObject attachedObject1 = objectRepository.findById(itemRevision.getItemMaster());
                    model.put("attachedObject", attachedObject1);
                } else {
                    model.put("attachedObject", attachedObject);
                }
                if (attachedObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROBLEMREPORT.toString()))) {
                    PQMProblemReport problemReport = problemReportRepository.findOne(attachedObject.getId());
                    if (problemReport.getProduct() != null) {
                        PLMItemRevision itemRevision = itemRevisionRepository.findOne(problemReport.getProduct());
                        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                        model.put("product", item.getItemName());
                    } else {
                        model.put("product", "");
                    }
                } else if (attachedObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.INSPECTIONPLANREVISION.toString()))) {
                    PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(attachedObject.getId());
                    if (inspectionPlanRevision != null) {
                        if (inspectionPlanRevision.getPlan().getObjectType().name().equals("PRODUCTINSPECTIONPLAN")) {
                            PQMProductInspectionPlan productInspectionPlan = productInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
                            PLMItemRevision itemRevision = itemRevisionRepository.findOne(productInspectionPlan.getProduct());
                            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                            model.put("product", item.getItemName());
                        } else if (inspectionPlanRevision.getPlan().getObjectType().name().equals("MATERIALINSPECTIONPLAN")) {
                            PQMMaterialInspectionPlan materialInspectionPlan = materialInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
                            model.put("product", materialInspectionPlan.getMaterial().getPartName());
                        }
                    }
                } else if (attachedObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString())) || attachedObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MATERIALINSPECTION.toString()))) {
                    PQMInspection inspection = inspectionRepository.findOne(attachedObject.getId());
                    PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspection.getInspectionPlan());
                    if (inspectionPlanRevision != null) {
                        model.put("planName", inspectionPlanRevision.getPlan().getName());
                        if (inspectionPlanRevision.getPlan().getObjectType().name().equals("PRODUCTINSPECTIONPLAN")) {
                            PQMProductInspectionPlan productInspectionPlan = productInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
                            PLMItemRevision itemRevision = itemRevisionRepository.findOne(productInspectionPlan.getProduct());
                            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                            model.put("product", item.getItemName());
                        } else if (inspectionPlanRevision.getPlan().getObjectType().name().equals("MATERIALINSPECTIONPLAN")) {
                            PQMMaterialInspectionPlan materialInspectionPlan = materialInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
                            model.put("product", materialInspectionPlan.getMaterial().getPartName());
                        }
                    }
                } else if (attachedObject.getObjectType().name().equals("ITEMMCO")) {
                    PLMItemMCO itemMCO = itemMCORepository.findOne(attachedObject.getId());
                    if (itemMCO != null) {
                        if (itemMCO.getItem() != null) {
                            PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemMCO.getItem());
                            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                            model.put("product", item.getItemNumber());
                        } else {
                            String name = "Product";
                            model.put("product", name);
                        }
                    }

                } else if (attachedObject.getObjectType().name().equals("OEMPARTMCO")) {
                    PLMManufacturerMCO manufacturerMCO = manufacturerMCORepository.findOne(attachedObject.getId());
                    if (manufacturerMCO != null) {
                        if (manufacturerMCO.getPart() != null) {
                            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(manufacturerMCO.getPart());
                            model.put("product", manufacturerPart.getPartNumber());
                        } else {
                            String name = "Material";
                            model.put("product", name);
                        }
                    }

                }
                // else if (attachedObject.getObjectType().name().equals("SUPPLIERAUDIT")) {
                //     PQMSupplierAudit manufacturerMCO = supplierAuditRepository.findOne(attachedObject.getId());
                //     if (manufacturerMCO != null) {
                //         model.put("product", manufacturerMCO.getNumber());
                //     }

                // }
            }
            List<PLMWorkflowStatusHistory> histories = getWorkflowHistory(workflow.getId());
            histories.forEach(h -> {
                h.setStatusObject(plmWorkflowStatusService.get(h.getStatus()));
            });
            if (workflow.getStarted()) {
                PLMWorkflowStatusHistory start = new PLMWorkflowStatusHistory();
                PLMWorkflowStatus s = new PLMWorkflowStatus();
                s.setName("Workflow Started");
                start.setStatusObject(s);
                start.setTimestamp(workflow.getStartedOn());
                histories.add(start);
            }
            if (workflow.getFinished()) {
                PLMWorkflowStatusHistory finish = new PLMWorkflowStatusHistory();
                PLMWorkflowStatus s = new PLMWorkflowStatus();
                s.setName("Workflow Finished");
                finish.setStatusObject(s);
                finish.setTimestamp(workflow.getFinishedOn());
                histories.add(0, finish);
            }

            model.put("histories", histories);
            model.put("workflowStatus", status);
            model.put("host", host);
            model.put("cssIncludes", this.getCss(host));
            model.put("companyLogo", companyLogos);
            model.put("approver", approvers);
            model.put("acknowledger", acknowledgers);
            model.put("observer", observers);

            new Thread(() -> {
                Mail mail = new Mail();
                mail.setMailToList(recipientAddress);
                String mailSubject = getMailSubject(attachedObject);
                mail.setMailSubject(mailSubject);
                mail.setModel(model);
                mail.setTemplatePath(templatePaths);
                mailService.sendEmail(mail);
            }).start();

        }
    }

    private void notifyUsers(PLMWorkflow workflow, PLMWorkflowStatus status, Boolean demoted, WorkflowStatusType type, WorkflowStatusFlag flag, List<Person> personList,
                             WorkflowAssigementType assigementType, Person statusPerson, Boolean allUser) {
        List<Integer> list = new ArrayList<>();
        List<Integer> acknowledgerList = new ArrayList<>();
        List<Integer> observersList = new ArrayList<>();
        List<Person> persons = new ArrayList<>();
        List<Person> acknowledgerPersons = new ArrayList<>();
        List<Person> observersPersons = new ArrayList<>();
        CassiniObject attachedObject = objectRepository.findById(workflow.getAttachedTo());
        if (flag.equals(WorkflowStatusFlag.INPROCESS)) {
            for (Person person : personList) {
                if (assigementType.equals(WorkflowAssigementType.APPROVER)) {
                    if (isValidMailAddress(person.getEmail())) {
                        persons.add(person);
                        list.add(person.getId());
                    }
                }
                if (assigementType.equals(WorkflowAssigementType.ACKNOWLEDGER)) {
                    if (isValidMailAddress(person.getEmail())) {
                        acknowledgerPersons.add(person);
                        acknowledgerList.add(person.getId());
                    }
                }
                if (assigementType.equals(WorkflowAssigementType.OBSERVER)) {
                    if (isValidMailAddress(person.getEmail())) {
                        observersPersons.add(person);
                        observersList.add(person.getId());
                    }
                }
            }

        } else {
            if (assigementType != null && allUser.equals(false)) {
                if (assigementType.equals(WorkflowAssigementType.APPROVER)) {
                    List<PLMWorkFlowStatusApprover> approvers = plmWorkFlowStatusApproverRepository.findByStatus(status.getId());
                    approvers.forEach(approver -> {
                        if (!list.contains(approver.getPerson())) {
                            Person person = personRepository.findOne(approver.getPerson());
                            if (isValidMailAddress(person.getEmail())) {
                                persons.add(person);
                                list.add(approver.getPerson());
                            }
                        }
                        if (statusPerson != null) {
                            persons.remove(statusPerson);
                            list.remove(statusPerson.getId());
                        }
                    });
                }
                if (assigementType.equals(WorkflowAssigementType.ACKNOWLEDGER)) {
                    List<PLMWorkFlowStatusAcknowledger> acknowledgers = plmWorkFlowStatusAcknowledgerRepository.findByStatus(status.getId());
                    acknowledgers.forEach(acknowledger -> {
                        if (!acknowledgerList.contains(acknowledger.getPerson())) {
                            Person person = personRepository.findOne(acknowledger.getPerson());
                            if (isValidMailAddress(person.getEmail())) {
                                acknowledgerPersons.add(person);
                                acknowledgerList.add(acknowledger.getPerson());
                            }
                        }
                        if (statusPerson != null) {
                            acknowledgerPersons.remove(statusPerson);
                            acknowledgerList.remove(statusPerson.getId());
                        }
                    });
                }
                if (assigementType.equals(WorkflowAssigementType.OBSERVER)) {
                    List<PLMWorkFlowStatusObserver> observers = plmWorkFlowStatusObserverRepository.findByStatus(status.getId());
                    observers.forEach(observer -> {
                        if (!observersList.contains(observer.getPerson())) {
                            Person person = personRepository.findOne(observer.getPerson());
                            if (isValidMailAddress(person.getEmail())) {
                                observersPersons.add(person);
                                observersList.add(observer.getPerson());
                            }
                        }
                        if (statusPerson != null) {
                            observersPersons.remove(statusPerson);
                            observersList.remove(statusPerson.getId());
                        }
                    });
                }

            } else if (allUser.equals(true)) {
                List<PLMWorkFlowStatusApprover> approvers = plmWorkFlowStatusApproverRepository.findByStatus(status.getId());
                approvers.forEach(approver -> {
                    if (!list.contains(approver.getPerson())) {
                        Person person = personRepository.findOne(approver.getPerson());
                        if (isValidMailAddress(person.getEmail())) {
                            persons.add(person);
                            list.add(approver.getPerson());
                        }
                    }
                });

                List<PLMWorkFlowStatusAcknowledger> acknowledgers = plmWorkFlowStatusAcknowledgerRepository.findByStatus(status.getId());
                acknowledgers.forEach(acknowledger -> {
                    if (!acknowledgerList.contains(acknowledger.getPerson())) {
                        Person person = personRepository.findOne(acknowledger.getPerson());
                        if (isValidMailAddress(person.getEmail())) {
                            acknowledgerPersons.add(person);
                            acknowledgerList.add(acknowledger.getPerson());
                        }
                    }
                });

                List<PLMWorkFlowStatusObserver> observers = plmWorkFlowStatusObserverRepository.findByStatus(status.getId());
                observers.forEach(observer -> {
                    if (!observersList.contains(observer.getPerson())) {
                        Person person = personRepository.findOne(observer.getPerson());
                        if (isValidMailAddress(person.getEmail())) {
                            observersPersons.add(person);
                            observersList.add(observer.getPerson());
                        }
                    }
                });
            }

        }
        String[] recipientAddress = new String[persons.size()];
        String[] acknowledgerAddress = new String[acknowledgerPersons.size()];
        String[] observerAddress = new String[observersPersons.size()];
        List<String> tokens = new ArrayList();
        String email = "";
        String acknowledgerEmail = "";
        String observerEmail = "";
        if (persons.size() > 0 || acknowledgerPersons.size() > 0 || observersPersons.size() > 0) {
            if (persons.size() > 0) {
                for (int i = 0; i < persons.size(); i++) {
                    Person person = persons.get(i);
                    if (email == "") {
                        email = person.getEmail();
                    } else {
                        email = email + "," + person.getEmail();
                    }
                    if (person.getMobileDevice() != null && person.getMobileDevice().getDeviceId() != null) {
                        tokens.add(person.getMobileDevice().getDeviceId());
                    }

                }
                String[] recipientList = email.split(",");
                int counter = 0;
                for (String recipient : recipientList) {
                    recipientAddress[counter] = recipient;
                    counter++;
                }
            }

            if (acknowledgerPersons.size() > 0) {
                for (int i = 0; i < acknowledgerPersons.size(); i++) {
                    Person person = acknowledgerPersons.get(i);
                    if (acknowledgerEmail == "") {
                        acknowledgerEmail = person.getEmail();
                    } else {
                        acknowledgerEmail = acknowledgerEmail + "," + person.getEmail();
                    }
                    if (person.getMobileDevice() != null && person.getMobileDevice().getDeviceId() != null) {
                        tokens.add(person.getMobileDevice().getDeviceId());
                    }

                }
                String[] recipientList1 = acknowledgerEmail.split(",");
                int counter1 = 0;
                for (String recipient : recipientList1) {
                    acknowledgerAddress[counter1] = recipient;
                    counter1++;
                }

            }
            if (observersPersons.size() > 0) {
                for (int i = 0; i < observersPersons.size(); i++) {
                    Person person = observersPersons.get(i);
                    if (observerEmail == "") {
                        observerEmail = person.getEmail();
                    } else {
                        observerEmail = observerEmail + "," + person.getEmail();
                    }
                    if (person.getMobileDevice() != null && person.getMobileDevice().getDeviceId() != null) {
                        tokens.add(person.getMobileDevice().getDeviceId());
                    }

                }

                String[] recipientList2 = observerEmail.split(",");
                int counter2 = 0;
                for (String recipient : recipientList2) {
                    observerAddress[counter2] = recipient;
                    counter2++;
                }
            }

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            URL companyLogo = null;
            String templatePath = null;
            EmailTemplateConfiguration emailTemplateConfiguration = eMailTemplateConfigRepository.findByTemplateName("workflowApprover.html");
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
            if (emailTemplateConfiguration != null) {
                if (emailTemplateConfiguration.getTemplateSourceCode() != null && emailTemplateConfiguration.getTemplateSourceCode() != "") {
                    byte[] data = DatatypeConverter.parseBase64Binary(emailTemplateConfiguration.getTemplateSourceCode());
                    URL url1 = ItemService.class
                            .getClassLoader().getResource("templates/email/share/" + "customTemplate.html");
                    File file = new File(url1.getPath());
                    try {
                        OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
                        Writer writer = new OutputStreamWriter(outputStream);
                        writer.write(emailTemplateConfiguration.getTemplateSourceCode());
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    templatePath = "email/share/customTemplate.html";
                } else {
                    templatePath = "email/workflow/workflowEmailTemplates.html";
                }
            } else {
                templatePath = "email/workflow/workflowEmailTemplates.html";
            }
            boolean pending = false;
            boolean approved = false;
            boolean rejected = false;
            if (type.equals(WorkflowStatusType.NORMAL)) {
                pending = true;
            } else if (type.equals(WorkflowStatusType.RELEASED)) {
                approved = true;
            } else if (type.equals(WorkflowStatusType.REJECTED)) {
                rejected = true;
            }
            final String templatePaths = templatePath;
            final URL companyLogos = companyLogo;
            final boolean normal = pending;
            final boolean approve = approved;
            final boolean reject = rejected;

            //HashMap model = new HashMap();
                /*CassiniObject attachedObject = objectRepository.findById(workflow.getAttachedTo());*/
            HashMap model = getWorkflowAttachedObject(attachedObject);
            List<PLMWorkflowStatusHistory> histories = getWorkflowHistory(workflow.getId());
            histories.forEach(h -> {
                h.setStatusObject(plmWorkflowStatusService.get(h.getStatus()));
            });
            if (workflow.getStarted()) {
                PLMWorkflowStatusHistory start = new PLMWorkflowStatusHistory();
                PLMWorkflowStatus s = new PLMWorkflowStatus();
                s.setName("Workflow Started");
                start.setStatusObject(s);
                start.setTimestamp(workflow.getStartedOn());
                histories.add(start);
            }
            if (workflow.getFinished()) {
                PLMWorkflowStatusHistory finish = new PLMWorkflowStatusHistory();
                PLMWorkflowStatus s = new PLMWorkflowStatus();
                s.setName("Workflow Finished");
                finish.setStatusObject(s);
                finish.setTimestamp(workflow.getFinishedOn());
                histories.add(0, finish);
            }
            model.put("histories", histories);
            model.put("demoted", demoted);
            model.put("workflowStatus", status);
            model.put("host", host);
            model.put("cssIncludes", this.getCss(host));
            model.put("companyLogo", companyLogos);
            model.put("pending", normal);
            model.put("approved", approve);
            model.put("rejected", reject);
            if (statusPerson != null) {
                model.put("statusPerson", statusPerson.getFullName());
            }

            if (recipientAddress.length > 0) {
                String notificationMsg = getMobileNotificationMsg(workflow, status, demoted, pending, approved, rejected);

                applicationEventPublisher.publishEvent(new PushNotificationEvents.WorkflowUpdateNotification(workflow, notificationMsg, tokens));
                new Thread(() -> {
                    Mail mail = new Mail();
                    mail.setMailToList(recipientAddress);
                    String mailSubject = getMailSubject(attachedObject);
                    mail.setMailSubject(mailSubject);
                    model.put("approver", true);
                    model.put("acknowledger", false);
                    model.put("observer", false);
                    mail.setModel(model);
                    mail.setTemplatePath(templatePaths);
                    mailService.sendEmail(mail);
                }).start();
            }
            if (acknowledgerAddress.length > 0) {
                String notificationMsg = getMobileNotificationMsg(workflow, status, demoted, pending, approved, rejected);

                applicationEventPublisher.publishEvent(new PushNotificationEvents.WorkflowUpdateNotification(workflow, notificationMsg, tokens));
                new Thread(() -> {
                    Mail mail = new Mail();
                    mail.setMailToList(acknowledgerAddress);
                    String mailSubject = getMailSubject(attachedObject);
                    mail.setMailSubject(mailSubject);
                    model.put("approver", false);
                    model.put("acknowledger", true);
                    model.put("observer", false);
                    mail.setModel(model);
                    mail.setTemplatePath(templatePaths);
                    mailService.sendEmail(mail);
                }).start();
            }
            if (observerAddress.length > 0) {
                String notificationMsg = getMobileNotificationMsg(workflow, status, demoted, pending, approved, rejected);

                applicationEventPublisher.publishEvent(new PushNotificationEvents.WorkflowUpdateNotification(workflow, notificationMsg, tokens));
                new Thread(() -> {
                    Mail mail = new Mail();
                    mail.setMailToList(observerAddress);
                    String mailSubject = getMailSubject(attachedObject);
                    mail.setMailSubject(mailSubject);
                    model.put("approver", false);
                    model.put("acknowledger", false);
                    model.put("observer", true);
                    mail.setModel(model);
                    mail.setTemplatePath(templatePaths);
                    mailService.sendEmail(mail);
                }).start();
            }

        }
    }


    private HashMap getWorkflowAttachedObject(CassiniObject attachedObject) {
        HashMap model = new HashMap();
        if (attachedObject != null) {
            if (attachedObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMREVISION.toString()))) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(attachedObject.getId());
                CassiniObject attachedObject1 = objectRepository.findById(itemRevision.getItemMaster());
                model.put("attachedObject", attachedObject1);
            } else {
                model.put("attachedObject", attachedObject);
            }
            if (attachedObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROBLEMREPORT.toString()))) {
                PQMProblemReport problemReport = problemReportRepository.findOne(attachedObject.getId());
                if (problemReport.getProduct() != null) {
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(problemReport.getProduct());
                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                    model.put("product", item.getItemName());
                } else {
                    model.put("product", "");
                }
            } else if (attachedObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.INSPECTIONPLANREVISION.toString()))) {
                PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(attachedObject.getId());
                if (inspectionPlanRevision != null) {
                    if (inspectionPlanRevision.getPlan().getObjectType().name().equals("PRODUCTINSPECTIONPLAN")) {
                        PQMProductInspectionPlan productInspectionPlan = productInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
                        PLMItemRevision itemRevision = itemRevisionRepository.findOne(productInspectionPlan.getProduct());
                        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                        model.put("product", item.getItemName());
                    } else if (inspectionPlanRevision.getPlan().getObjectType().name().equals("MATERIALINSPECTIONPLAN")) {
                        PQMMaterialInspectionPlan materialInspectionPlan = materialInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
                        model.put("product", materialInspectionPlan.getMaterial().getPartName());
                    }
                }
            } else if (attachedObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString())) || attachedObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MATERIALINSPECTION.toString()))) {
                PQMInspection inspection = inspectionRepository.findOne(attachedObject.getId());
                PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspection.getInspectionPlan());
                if (inspectionPlanRevision != null) {
                    model.put("planName", inspectionPlanRevision.getPlan().getName());
                    if (inspectionPlanRevision.getPlan().getObjectType().name().equals("PRODUCTINSPECTIONPLAN")) {
                        PQMProductInspectionPlan productInspectionPlan = productInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
                        PLMItemRevision itemRevision = itemRevisionRepository.findOne(productInspectionPlan.getProduct());
                        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                        model.put("product", item.getItemName());
                    } else if (inspectionPlanRevision.getPlan().getObjectType().name().equals("MATERIALINSPECTIONPLAN")) {
                        PQMMaterialInspectionPlan materialInspectionPlan = materialInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
                        model.put("product", materialInspectionPlan.getMaterial().getPartName());
                    }
                }
            } else if (attachedObject.getObjectType().name().equals("ITEMMCO")) {
                PLMItemMCO itemMCO = itemMCORepository.findOne(attachedObject.getId());
                if (itemMCO != null) {
                    if (itemMCO.getItem() != null) {
                        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemMCO.getItem());
                        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                        model.put("product", item.getItemNumber());
                    } else {
                        String name = "Product";
                        model.put("product", name);
                    }
                }

            } else if (attachedObject.getObjectType().name().equals("OEMPARTMCO")) {
                PLMManufacturerMCO manufacturerMCO = manufacturerMCORepository.findOne(attachedObject.getId());
                if (manufacturerMCO != null) {
                    if (manufacturerMCO.getPart() != null) {
                        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(manufacturerMCO.getPart());
                        model.put("product", manufacturerPart.getPartNumber());
                    } else {
                        String name = "Material";
                        model.put("product", name);
                    }
                }

            } else if (attachedObject.getObjectType().name().equals("SUPPLIERAUDIT")) {
                PQMSupplierAudit manufacturerMCO = supplierAuditRepository.findOne(attachedObject.getId());
                if (manufacturerMCO != null) {
                    model.put("product", manufacturerMCO.getNumber());
                }

            }

        }
        return model;
    }

    private String getMobileNotificationMsg(PLMWorkflow workflow, PLMWorkflowStatus workflowStatus, Boolean demoted, Boolean pending, Boolean approved, Boolean rejected) {
        String message = "";
        String objectType = "";
        CassiniObject cassiniObject = objectRepository.findById(workflow.getAttachedTo());
        if (cassiniObject != null) {
            if (cassiniObject.getObjectType().toString().equals(PLMObjectType.CHANGE.toString())) {
                PLMChange change = changeRepository.findOne(workflow.getAttachedTo());
                if (change.getChangeType().equals(ChangeType.ECO)) {
                    PLMECO plmeco = ecoService.get(workflow.getAttachedTo());
                    objectType = "ECO - " + plmeco.getEcoNumber();
                } else if (change.getChangeType().equals(ChangeType.ECR)) {
                    PLMECR plmecr = ecrRepository.findOne(workflow.getAttachedTo());
                    objectType = "ECR - " + plmecr.getCrNumber();
                } else if (change.getChangeType().equals(ChangeType.DCR)) {
                    PLMDCR plmdcr = dcrRepository.findOne(workflow.getAttachedTo());
                    objectType = "DCR - " + plmdcr.getCrNumber();
                } else if (change.getChangeType().equals(ChangeType.DCO)) {
                    PLMDCO plmdco = dcoRepository.findOne(workflow.getAttachedTo());
                    objectType = "DCO - " + plmdco.getDcoNumber();
                } else if (change.getChangeType().equals(ChangeType.MCO)) {
                    PLMMCO plmmco = mcoRepository.findOne(workflow.getAttachedTo());
                    objectType = "MCO - " + plmmco.getMcoNumber();
                } else if (change.getChangeType().equals(ChangeType.DEVIATION)) {
                    PLMVariance variance = varianceRepository.findOne(workflow.getAttachedTo());
                    objectType = "Deviation - " + variance.getVarianceNumber();
                } else if (change.getChangeType().equals(ChangeType.WAIVER)) {
                    PLMVariance variance = varianceRepository.findOne(workflow.getAttachedTo());
                    objectType = "Waiver - " + variance.getVarianceNumber();
                }
            } else if (cassiniObject.getObjectType().toString().equals(PLMObjectType.ITEMREVISION.toString())) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(workflow.getAttachedTo());
                PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                objectType = "Item - " + item.getItemNumber();
            }
        }

        if (pending) {
            message = "CassiniPLM " + objectType + " Workflow Notification - Pending Approval." +
                    " Workflow status [ " + workflowStatus.getName() + " ] requires your approval. Please review and take action.";
        } else if (approved) {
            message = "CassiniPLM " + objectType + " Workflow Notification - Approved Confirmation." +
                    "This is to notify that the workflow status [ " + workflowStatus.getName() + " ] has been approved.";
        } else if (rejected) {
            message = "CassiniPLM " + objectType + " Workflow Notification - Reject Confirmation." +
                    "This is to notify that the workflow status [ " + workflowStatus.getName() + " ] has been rejected";
        }

        return message;
    }

    private String getMailSubject(CassiniObject attachedObject) {
        String mailSubject = null;
        if (attachedObject.getObjectType().name().equals("CHANGE")) {
            PLMChange change = changeRepository.findOne(attachedObject.getId());
            if (change.getChangeType().name().equals("ECO")) {
                String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
                mailSubject = MessageFormat.format(message + ".", "ECO");
            } else if (change.getChangeType().name().equals("DCO")) {
                String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
                mailSubject = MessageFormat.format(message + ".", "DCO");
            } else if (change.getChangeType().name().equals("ECR")) {
                String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
                mailSubject = MessageFormat.format(message + ".", "ECR");
            } else if (change.getChangeType().name().equals("DCR")) {
                String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
                mailSubject = MessageFormat.format(message + ".", "DCR");
            } else if (change.getChangeType().name().equals("DEVIATION")) {
                String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
                mailSubject = MessageFormat.format(message + ".", "DEVIATION");
            } else if (change.getChangeType().name().equals("WAIVER")) {
                String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
                mailSubject = MessageFormat.format(message + ".", "WAIVER");
            }
        } else if (attachedObject.getObjectType().name().equals("ITEMREVISION")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "Item");
        } else if (attachedObject.getObjectType().name().equals("MANUFACTURER")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "Manufacturer");
        } else if (attachedObject.getObjectType().name().equals("MANUFACTURERPART")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "Manufacturer Part");
        } else if (attachedObject.getObjectType().name().equals("SUPPLIERAUDIT")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "Supplier Audit");
        } else if (attachedObject.getObjectType().name().equals("PROJECT")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "Project");
        } else if (attachedObject.getObjectType().name().equals("PROJECTACTIVITY")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "Project Activity");
        } else if (attachedObject.getObjectType().name().equals("PROJECTTASK")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "Project Task");
        } else if (attachedObject.getObjectType().name().equals("REQUIREMENT")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "Requirement");
        } else if (attachedObject.getObjectType().name().equals("SPECIFICATION")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "Specification");
        } else if (attachedObject.getObjectType().name().equals("PROBLEMREPORT")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "ProblemReport");
        } else if (attachedObject.getObjectType().name().equals("NCR")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "NCR");
        } else if (attachedObject.getObjectType().name().equals("QCR")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "QCR");
        } else if (attachedObject.getObjectType().name().equals("ITEMINSPECTION")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "Inspection(Products)");
        } else if (attachedObject.getObjectType().name().equals("MATERIALINSPECTION")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "Inspection(Materials)");
        } else if (attachedObject.getObjectType().name().equals("INSPECTIONPLANREVISION")) {
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(attachedObject.getId());
            if (inspectionPlanRevision.getPlan().getObjectType().name().equals("PRODUCTINSPECTIONPLAN")) {
                String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
                mailSubject = MessageFormat.format(message + ".", "InspectionPlan(Products)");
            } else if (inspectionPlanRevision.getPlan().getObjectType().name().equals("MATERIALINSPECTIONPLAN")) {
                String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
                mailSubject = MessageFormat.format(message + ".", "InspectionPlan(Materials)");
            }
        } else if (attachedObject.getObjectType().name().equals("ITEMMCO")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "MCO(Products)");
        } else if (attachedObject.getObjectType().name().equals("OEMPARTMCO")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "MCO(Materials)");
        } else if (attachedObject.getObjectType().name().equals("MROWORKORDER")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "WorkOrder");
        } else if (attachedObject.getObjectType().name().equals("PLMNPR")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "NPR");
        } else if (attachedObject.getObjectType().name().equals("CUSTOMOBJECT")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "CustomObject");
        } else if (attachedObject.getObjectType().name().equals("PROGRAM")) {
            String message = messageSources.getMessage("workflow_notification", null, "CassiniPLM {0} Workflow Notification", LocaleContextHolder.getLocale());
            mailSubject = MessageFormat.format(message + ".", "Program");
        }

        return mailSubject;
    }

    private Boolean isValidMailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private String getCss(String host) {
        String css = "";
        String[] urls = new String[]{host + "/app/assets/bower_components/bootstrap/dist/css/bootstrap.min.css", host + "/app/assets/bower_components/cassini-platform/template/css/bootstrap-override.css", host + "/app/assets/bower_components/cassini-platform/template/css/style.default.css", host + "/app/assets/bower_components/cassini-platform/template/css/style.katniss.css"};
        try {
            String[] e = urls;
            int var5 = urls.length;
            for (int var6 = 0; var6 < var5; ++var6) {
                String url = e[var6];
                css = css + IOUtils.toString(new URL(url), (Charset) null);
                css = css + "\n\n";
            }
        } catch (IOException var8) {
            var8.printStackTrace();
        }
        return css;
    }

    public List<PLMWorkflowAttribute> getWorkflowUsedAttributes(Integer attributeId) {
        List<PLMWorkflowAttribute> workflowAttributes = workflowAttributeRepository.findByAttributeId(attributeId);
        return workflowAttributes;
    }

    @Transactional
    public void deleteWorkflow(Integer id) {
        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(id);
        if (workflow != null) {
            PLMWorkflowDefinition workflowDefinition = plmWorkflowDefinitionRepository.findOne(workflow.getWorkflowRevision());
            Integer workflowInstances = workflowDefinition.getInstances();
            Integer workflowMaster = workflowDefinition.getMaster().getInstances();
            workflowDefinition.setInstances(workflowInstances - 1);
            PLMWorkflowDefinitionMaster definitionMaster = workflowDefinition.getMaster();
            plmWorkflowDefinitionRepository.save(workflowDefinition);
            definitionMaster.setInstances(workflowMaster - 1);
            workflowDefinitionMasterRepository.save(definitionMaster);
            plmWorkflowRepository.deleteById(workflow.getId());
        }
    }

    @Transactional
    public void deleteProjectActivityTaskWorkflow(Integer id) {
        List<Integer> taskIds = taskRepository.getProjectTaskIds(id);
        taskIds.addAll(taskRepository.getProjectTaskActivityIdsCount(id));
        taskIds.forEach(taskId -> {
            PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(id);
            if (workflow != null) {
                PLMWorkflowDefinition workflowDefinition = plmWorkflowDefinitionRepository.findOne(workflow.getWorkflowRevision());
                Integer workflowInstances = workflowDefinition.getInstances();
                Integer workflowMaster = workflowDefinition.getMaster().getInstances();
                workflowDefinition.setInstances(workflowInstances - 1);
                PLMWorkflowDefinitionMaster definitionMaster = workflowDefinition.getMaster();
                plmWorkflowDefinitionRepository.save(workflowDefinition);
                definitionMaster.setInstances(workflowMaster - 1);
                workflowDefinitionMasterRepository.save(definitionMaster);
                plmWorkflowRepository.deleteById(workflow.getId());
            }
        });
    }

    @Transactional
    public void deleteProgramProjectActivityTaskWorkflow(Integer id) {
        List<Integer> projectIds = programProjectRepository.getProgramProjectIds(id);
        if (projectIds.size() > 0) {
            List<Integer> taskIds = taskRepository.getProjectsTaskIds(projectIds);
            taskIds.addAll(taskRepository.getProjectsTaskActivityIdsCount(projectIds));
            taskIds.addAll(projectIds);
            taskIds.forEach(taskId -> {
                PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(id);
                if (workflow != null) {
                    PLMWorkflowDefinition workflowDefinition = plmWorkflowDefinitionRepository.findOne(workflow.getWorkflowRevision());
                    Integer workflowInstances = workflowDefinition.getInstances();
                    Integer workflowMaster = workflowDefinition.getMaster().getInstances();
                    workflowDefinition.setInstances(workflowInstances - 1);
                    PLMWorkflowDefinitionMaster definitionMaster = workflowDefinition.getMaster();
                    plmWorkflowDefinitionRepository.save(workflowDefinition);
                    definitionMaster.setInstances(workflowMaster - 1);
                    workflowDefinitionMasterRepository.save(definitionMaster);
                    plmWorkflowRepository.deleteById(workflow.getId());
                }
            });
        }
    }

    @Transactional(readOnly = true)
    public List<WorkflowDto> getWorkflowsByWfDef(Integer id) {
        List<WorkflowDto> workflowDtos = new ArrayList<>();
        PLMWorkflowDefinition workflowDefinition = plmWorkflowDefinitionRepository.findOne(id);
        List<PLMWorkflow> workflows = null;
        if (workflowDefinition != null) {
            workflows = plmWorkflowRepository.findByWorkflowRevision(workflowDefinition.getId());
            for (PLMWorkflow workflow1 : workflows) {
                WorkflowDto workflowDto = new WorkflowDto();
                workflowDto.setWorkflow(workflow1);
                PLMWorkflowType workflowType = workflowTypeRepository.findOne(workflowDefinition.getWorkflowType().getId());
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow1.getCurrentStatus());
                List<PLMWorkflowStatusHistory> statusHistories = plmWorkflowStatusHistoryRepository.findByWorkflowOrderByTimestampDesc(workflow1.getId());
                if (statusHistories.size() > 0) {
                    workflowDto.setTimeStamp(statusHistories.get(0).getTimestamp());
                } else if (!workflowStatus.getType().equals(WorkflowStatusType.START)) {
                    workflowDto.setTimeStamp(workflow1.getModifiedDate());
                }
                if (workflowStatus != null) {
                    workflowDto.setCurrentStatus(workflowStatus.getName());
                }

                WorkflowDto dto = getWorkflowDto(workflowDto, workflowType, workflow1);
                workflowDtos.add(dto);
            }
        }
        return workflowDtos;
    }

    private WorkflowDto getWorkflowDto(WorkflowDto workflowDto, PLMWorkflowType workflowType, PLMWorkflow workflow1) {
        if (workflowType.getAssignable().equals("ITEMS")) {
            PLMItemRevision revision = itemRevisionRepository.findOne(workflow1.getAttachedTo());
            PLMItem item = itemRepository.findOne(revision.getItemMaster());
            workflowDto.setNumber(item.getItemNumber());
            workflowDto.setType(item.getItemType().getName());
            workflowDto.setRevision(revision.getRevision());
            workflowDto.setObjectType("ITEM");
        } else if (workflowType.getAssignable().equals("CHANGES")) {
            PLMECO eco = ecoRepository.findOne(workflow1.getAttachedTo());
            if (eco != null) {
                workflowDto.setNumber(eco.getEcoNumber());
                PLMChangeType change = changeTypeRepository.findOne(eco.getEcoType());
                workflowDto.setType(change.getName());
                workflowDto.setObjectType("ECO");
            }
            PLMDCO dco = dcoRepository.findOne(workflow1.getAttachedTo());
            if (dco != null) {
                workflowDto.setNumber(dco.getDcoNumber());
                PLMChangeType change = changeTypeRepository.findOne(dco.getDcoType());
                workflowDto.setType(change.getName());
                workflowDto.setObjectType("DCO");
            }
            PLMDCR dcr = dcrRepository.findOne(workflow1.getAttachedTo());
            if (dcr != null) {
                workflowDto.setNumber(dcr.getCrNumber());
                PLMChangeType change = changeTypeRepository.findOne(dcr.getCrType());
                workflowDto.setType(change.getName());
                workflowDto.setObjectType("DCR");
            }
            PLMVariance plmVariance = varianceRepository.findOne(workflow1.getAttachedTo());
            if (plmVariance != null) {
                workflowDto.setNumber(plmVariance.getVarianceNumber());
                PLMChangeType change = changeTypeRepository.findOne(plmVariance.getChangeClass().getId());
                workflowDto.setType(change.getName());
                workflowDto.setObjectType("VARIANCES");
            }
            PLMMCO mco = mcoRepository.findOne(workflow1.getAttachedTo());
            if (mco != null) {
                workflowDto.setNumber(mco.getMcoNumber());
                PLMChangeType change = changeTypeRepository.findOne(mco.getMcoType().getId());
                workflowDto.setType(change.getName());
                workflowDto.setObjectType("MCO");
            }
            PLMECR ecr = ecrRepository.findOne(workflow1.getAttachedTo());
            if (ecr != null) {
                workflowDto.setNumber(ecr.getCrNumber());
                PLMChangeType change = changeTypeRepository.findOne(ecr.getCrType());
                workflowDto.setType(change.getName());
                workflowDto.setObjectType("ECR");
            }
        } else if (workflowType.getAssignable().equals("QUALITY")) {
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(workflow1.getAttachedTo());
            if (inspectionPlanRevision != null) {
                PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
                if (inspectionPlan.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PRODUCTINSPECTIONPLAN.toString()))) {
                    PQMProductInspectionPlan productInspectionPlan = productInspectionPlanRepository.findOne(inspectionPlan.getId());
                    workflowDto.setNumber(productInspectionPlan.getNumber());
                    workflowDto.setType(productInspectionPlan.getPlanType().getName());
                    workflowDto.setObjectType("INSPECTIONPLANREVISION");
                } else {
                    PQMMaterialInspectionPlan materialInspectionPlan = materialInspectionPlanRepository.findOne(inspectionPlan.getId());
                    workflowDto.setNumber(materialInspectionPlan.getNumber());
                    workflowDto.setType(materialInspectionPlan.getPlanType().getName());
                    workflowDto.setObjectType("INSPECTIONPLANREVISION");
                }
            }
            PQMProblemReport problemReport = problemReportRepository.findOne(workflow1.getAttachedTo());
            if (problemReport != null) {
                workflowDto.setNumber(problemReport.getPrNumber());
                workflowDto.setType(problemReport.getPrType().getName());
                workflowDto.setObjectType("PROBLEMREPORT");
            }
            PQMNCR pqmncr = ncrRepository.findOne(workflow1.getAttachedTo());
            if (pqmncr != null) {
                workflowDto.setNumber(pqmncr.getNcrNumber());
                workflowDto.setType(pqmncr.getNcrType().getName());
                workflowDto.setObjectType("NCR");
            }
            PQMQCR pqmqcr = qcrRepository.findOne(workflow1.getAttachedTo());
            if (pqmqcr != null) {
                workflowDto.setNumber(pqmqcr.getQcrNumber());
                workflowDto.setType(pqmqcr.getQcrType().getName());
                workflowDto.setObjectType("QCR");
            }
        } else if (workflowType.getAssignable().equals("MATERIAL INSPECTIONS") || workflowType.getAssignable().equals("ITEM INSPECTIONS")) {
            PQMInspection inspection = inspectionRepository.findOne(workflow1.getAttachedTo());
            if (inspection != null) {
                workflowDto.setNumber(inspection.getInspectionNumber());
                workflowDto.setType(inspection.getObjectType().toString());
                workflowDto.setObjectType("ITEMINSPECTION");
            }
        } else if (workflowType.getAssignable().equals("PROJECTS")) {
            if (workflow1.getAttachedToType().name().equals("TEMPLATE")) {
                ProjectTemplate projectTemplate = projectTemplateRepository.findOne(workflow1.getAttachedTo());
                if (projectTemplate != null) {
                    workflowDto.setNumber(projectTemplate.getName());
                    workflowDto.setType("TEMPLATE");
                    workflowDto.setObjectType("TEMPLATE");
                }
            } else {
                PLMProject project = projectRepository.findOne(workflow1.getAttachedTo());
                if (project != null) {
                    workflowDto.setNumber(project.getName());
                    workflowDto.setType("PROJECT");
                    workflowDto.setObjectType("PROJECT");
                }
            }
        } else if (workflowType.getAssignable().equals("PROGRAM")) {
            if (workflow1.getAttachedToType().name().equals("PROGRAMTEMPLATE")) {
                ProgramTemplate projectTemplate = programTemplateRepository.findOne(workflow1.getAttachedTo());
                if (projectTemplate != null) {
                    workflowDto.setNumber(projectTemplate.getName());
                    workflowDto.setType("PROGRAMTEMPLATE");
                    workflowDto.setObjectType("PROGRAMTEMPLATE");
                }
            } else {
                PLMProgram program = programRepository.findOne(workflow1.getAttachedTo());
                if (program != null) {
                    workflowDto.setNumber(program.getName());
                    workflowDto.setType("PROGRAM");
                    workflowDto.setObjectType("PROGRAM");
                }
            }
        } else if (workflowType.getAssignable().equals("MANUFACTURERS")) {
            PLMManufacturer manufacturer = manufacturerRepository.findOne(workflow1.getAttachedTo());
            if (manufacturer != null) {
                workflowDto.setNumber(manufacturer.getName());
                workflowDto.setType(manufacturer.getMfrType().getName());
                workflowDto.setObjectType("MANUFACTURERS");
            }
        } else if (workflowType.getAssignable().equals("MANUFACTURER PARTS")) {
            PLMManufacturerPart part = manufacturerPartRepository.findOne(workflow1.getAttachedTo());
            if (part != null) {
                workflowDto.setNumber(part.getPartNumber());
                workflowDto.setType(part.getMfrPartType().getName());
                workflowDto.setObjectType("MANUFACTURERPARTS");
            }
        } else if (workflowType.getAssignable().equals("SUPPLIER AUDITS")) {
            PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(workflow1.getAttachedTo());
            if (supplierAudit != null) {
                workflowDto.setNumber(supplierAudit.getNumber());
                workflowDto.setType(supplierAudit.getType().getName());
                workflowDto.setObjectType("SUPPLIERAUDIT");
            }
        } else if (workflowType.getAssignable().equals("PROJECT ACTIVITIES")) {
            PLMActivity activity = activityRepository.findOne(workflow1.getAttachedTo());
            if (activity != null) {
                workflowDto.setNumber(activity.getName());
                workflowDto.setType("PROJECTACTIVITY");
                workflowDto.setObjectType("PROJECTACTIVITY");
            }
        } else if (workflowType.getAssignable().equals("PROJECT TASKS")) {
            if (workflow1.getAttachedToType().name().equals("TEMPLATETASK")) {
                ProjectTemplateTask projectTemplate = projectTemplateTaskRepository.findOne(workflow1.getAttachedTo());
                if (projectTemplate != null) {
                    workflowDto.setNumber(projectTemplate.getName());
                    workflowDto.setType("TEMPLATETASK");
                    workflowDto.setObjectType("TEMPLATETASK");
                }
            } else {
                PLMTask task = taskRepository.findOne(workflow1.getAttachedTo());
                if (task != null) {
                    workflowDto.setNumber(task.getName());
                    workflowDto.setType("PROJECTTASK");
                    workflowDto.setObjectType("PROJECTTASK");
                    workflowDto.setTask(task);
                }
            }

        } else if (workflowType.getAssignable().equals("PROJECT TEMPLATE")) {
            ProjectTemplate projectTemplate = projectTemplateRepository.findOne(workflow1.getAttachedTo());
            if (projectTemplate != null) {
                workflowDto.setNumber(projectTemplate.getName());
                workflowDto.setType("TEMPLATE");
                workflowDto.setObjectType("TEMPLATE");
            }
        } else if (workflowType.getAssignable().equals("PROGRAM TEMPLATE")) {
            ProgramTemplate projectTemplate = programTemplateRepository.findOne(workflow1.getAttachedTo());
            if (projectTemplate != null) {
                workflowDto.setNumber(projectTemplate.getName());
                workflowDto.setType("PROGRAMTEMPLATE");
                workflowDto.setObjectType("PROGRAMTEMPLATE");
            }
        } else if (workflowType.getAssignable().equals("TASK TEMPLATE")) {
            ProjectTemplateTask projectTemplate = projectTemplateTaskRepository.findOne(workflow1.getAttachedTo());
            if (projectTemplate != null) {
                workflowDto.setNumber(projectTemplate.getName());
                workflowDto.setType("TEMPLATETASK");
                workflowDto.setObjectType("TEMPLATETASK");
            }
        } else if (workflowType.getAssignable().equals("REQUIREMENTS")) {
            Requirement requirement = requirementRepository.findOne(workflow1.getAttachedTo());
            Specification specification = specificationRepository.findOne(workflow1.getAttachedTo());
            if (requirement != null) {
                workflowDto.setNumber(requirement.getObjectNumber());
                workflowDto.setType(requirement.getType().getName());
                workflowDto.setObjectType("REQUIREMENT");
            }
            if (specification != null) {
                workflowDto.setNumber(specification.getObjectNumber());
                workflowDto.setType(specification.getType().getName());
                workflowDto.setObjectType("SPECIFICATION");
            }
        } else if (workflowType.getAssignable().equals("MAINTENANCE&REPAIR")) {
            MROWorkOrder workOrder = workOrderRepository.findOne(workflow1.getAttachedTo());
            if (workOrder != null) {
                workflowDto.setNumber(workOrder.getNumber());
                workflowDto.setType(workOrder.getType().getName());
                workflowDto.setObjectType("WORKORDER");
            }
        } else if (workflowType.getAssignable().equals("NPR")) {
            PLMNpr npr = nprRepository.findOne(workflow1.getAttachedTo());
            if (npr != null) {
                workflowDto.setNumber(npr.getNumber());
                workflowDto.setType("NPR");
                workflowDto.setObjectType("PLMNPR");
            }
        } else if (workflowType.getAssignable().equals("CUSTOM OBJECTS")) {
            CustomObject customObject = customObjectRepository.findOne(workflow1.getAttachedTo());
            if (customObject != null) {
                workflowDto.setNumber(customObject.getNumber());
                workflowDto.setType("CUSTOMOBJECT");
                workflowDto.setObjectType("CUSTOMOBJECT");
            }
        }

        return workflowDto;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowStatusHistory> getStatusWorkflowHistory(Integer wfId) {
        List<PLMWorkflowStatusHistory> history = plmWorkflowStatusHistoryRepository.findByWorkflowOrderByTimestampDesc(wfId);
        history.forEach(h -> {
            h.setStatusObject(plmWorkflowStatusService.get(h.getStatus()));
            List<PLMWorkflowStatusActionHistory> actionHistory =
                    plmWorkflowStatusActionHistoryRepository.findByWorkflowAndStatusOrderByTimestampDesc(wfId, h.getStatus());
            h.setStatusActionHistory(actionHistory);
            List<PLMWorkFlowStatusApprover> workflowStatusApprovers = plmWorkFlowStatusApproverRepository.findByStatus(h.getStatus());
            if (workflowStatusApprovers.size() > 0) {
                h.setStatusApprovers(workflowStatusApprovers);
            }
            List<PLMWorkFlowStatusAcknowledger> workflowStatusAcknowledgers = plmWorkFlowStatusAcknowledgerRepository.findByStatus(h.getStatus());
            if (workflowStatusAcknowledgers.size() > 0) {
                h.setStatusAcknowledgers(workflowStatusAcknowledgers);
            }
        });
        return history;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getAllWorkflowsByType(Integer id) {
        PLMWorkflowType workflowType = workflowTypeRepository.findOne(id);
        List<PLMWorkflowDefinition> workflowDefinitions = null;
        if (workflowType != null) {
            workflowDefinitions = plmWorkflowDefinitionRepository.findByWorkflowType(workflowType);
        }
        return workflowDefinitions;
    }

    @Transactional(readOnly = true)
    public List<WorkflowDto> getWorkflowsByMaster(Integer id) {
        List<WorkflowDto> workflowDtos = new ArrayList<>();
        PLMWorkflowDefinitionMaster definitionMaster = workflowDefinitionMasterRepository.findOne(id);
        List<PLMWorkflowDefinition> workflowDefinitions = plmWorkflowDefinitionRepository.findByMaster(definitionMaster);
        List<PLMWorkflow> workflows = null;
        if (workflowDefinitions.size() > 0) {
            for (PLMWorkflowDefinition workflowDefinition : workflowDefinitions) {
                workflows = plmWorkflowRepository.findByWorkflowRevision(workflowDefinition.getId());
                for (PLMWorkflow workflow1 : workflows) {
                    WorkflowDto workflowDto = new WorkflowDto();
                    workflowDto.setWfRevision(workflowDefinition.getRevision());
                    PLMWorkflowType workflowType = workflowTypeRepository.findOne(workflowDefinition.getWorkflowType().getId());
                    PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow1.getCurrentStatus());
                    List<PLMWorkflowStatusHistory> statusHistories = plmWorkflowStatusHistoryRepository.findByWorkflowOrderByTimestampDesc(workflow1.getId());
                    if (statusHistories.size() > 0) {
                        workflowDto.setTimeStamp(statusHistories.get(0).getTimestamp());
                    } else if (!workflowStatus.getType().equals(WorkflowStatusType.START)) {
                        workflowDto.setTimeStamp(workflow1.getModifiedDate());
                    }
                    if (workflowStatus != null) {
                        workflowDto.setCurrentStatus(workflowStatus.getName());
                        workflowDto.setWorkflow(workflow1);
                    }

                    WorkflowDto dto = getWorkflowDto(workflowDto, workflowType, workflow1);
                    workflowDtos.add(dto);
                }
            }
        }
        return workflowDtos;
    }

    @Transactional(readOnly = true)
    public Page<WorkflowDto> getAllWorkflowInstances(Pageable pageable, WorkflowCriteria criteria) {
        List<WorkflowDto> workflowDtos = new LinkedList<>();
        String obj = null;
        Predicate predicate = workflowPredicateBuilder.build(criteria, QPLMWorkflow.pLMWorkflow);
        Page<PLMWorkflow> workflows = plmWorkflowRepository.findAll(predicate, pageable);
        Map<Integer, WorkflowDto> map = new LinkedHashMap<>();
        List<Integer> cassiniObjectIds = new LinkedList<>();
        for (PLMWorkflow workflow1 : workflows.getContent()) {
            WorkflowDto workflowDto = new WorkflowDto();
            cassiniObjectIds.add(workflow1.getAttachedTo());
            workflowDto.setWorkflowNumber(plmWorkflowDefinitionRepository.findOne(workflow1.getWorkflowRevision()).getMaster().getNumber());
            PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow1.getCurrentStatus());
            List<PLMWorkflowStatusHistory> statusHistories = plmWorkflowStatusHistoryRepository.findByWorkflowOrderByTimestampDesc(workflow1.getId());
            if (statusHistories.size() > 0) {
                workflowDto.setTimeStamp(statusHistories.get(0).getTimestamp());
                workflowDto.setPreviousStatus(plmWorkflowStatusRepository.findOne(statusHistories.get(0).getStatus()).getName());
                workflowDto.setPreviousStatuType(plmWorkflowStatusRepository.findOne(statusHistories.get(0).getStatus()).getType());
            } else if (!workflowStatus.getType().equals(WorkflowStatusType.START)) {
                workflowDto.setTimeStamp(workflow1.getModifiedDate());
            }
            if (workflow1.getOnhold()) {
                workflowDto.setCurrentStatus(workflowStatus.getName());
                workflowDto.setStatusType(workflowStatus.getType());
                if (workflow1.getHoldBy() != null) {
                    workflowDto.setHoldBy(personRepository.findOne(workflow1.getHoldBy()).getFullName());
                }
            } else {
                workflowDto.setCurrentStatus(workflowStatus.getName());
                workflowDto.setStatusType(workflowStatus.getType());
            }
            workflowDto.setWorkflow(workflow1);
            map.put(workflow1.getAttachedTo(), workflowDto);
            workflowDtos.add(workflowDto);
        }
        return new PageImpl<WorkflowDto>(getCassiniObjects(objectRepository.findByIdInOrderByModifiedDateDesc(cassiniObjectIds), map, obj), pageable, workflows.getTotalElements());

    }

    private List<WorkflowDto> getCassiniObjects(List<CassiniObject> cassiniObjects, Map<Integer, WorkflowDto> finalMap, String obj) {
        List<WorkflowDto> workflowDtos = new LinkedList<>();
        for (CassiniObject cassiniObject : cassiniObjects) {
            WorkflowDto workflowDto = finalMap.get(cassiniObject.getId());
            if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMREVISION.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.ITEM.toString())) && cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMREVISION.toString()))) {
                PLMItemRevision revision = itemRevisionRepository.findOne(cassiniObject.getId());
                if (revision != null) {
                    PLMItem item = itemRepository.findOne(revision.getItemMaster());
                    workflowDto.setNumber(item.getItemNumber());
                    workflowDto.setType(item.getItemType().getName());
                    workflowDto.setDescription(item.getItemName());
                    workflowDto.setRevision(revision.getRevision());
                    workflowDto.setObjectType("ITEM");
                }
            } else if (cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.CHANGE.toString()))) {
                PLMChange change = changeRepository.findOne(cassiniObject.getId());
                if ((change.getChangeType().equals(ChangeType.ECO) && obj == null) || (obj != null && obj.equalsIgnoreCase(PLMObjectType.ECO.toString())) && change.getChangeType().toString().equals(obj)) {
                    PLMECO eco = ecoRepository.findOne(cassiniObject.getId());
                    if (eco != null) {
                        workflowDto.setNumber(eco.getEcoNumber());
                        workflowDto.setType(change.getChangeType().toString());
                        workflowDto.setNumber(eco.getEcoNumber());
                        workflowDto.setDescription(eco.getTitle());
                        workflowDto.setObjectType("ECO");
                    }
                } else if ((change.getChangeType().equals(ChangeType.DCO) && obj == null) || (obj != null && obj.equalsIgnoreCase(PLMObjectType.DCO.toString())) && change.getChangeType().toString().equals(obj)) {
                    PLMDCO dco = dcoRepository.findOne(cassiniObject.getId());
                    if (dco != null) {
                        workflowDto.setNumber(dco.getDcoNumber());
                        workflowDto.setType(change.getChangeType().toString());
                        workflowDto.setNumber(dco.getDcoNumber());
                        workflowDto.setDescription(dco.getTitle());
                        workflowDto.setObjectType("DCO");
                    }
                } else if ((change.getChangeType().equals(ChangeType.DCR) && obj == null) || (obj != null && obj.equalsIgnoreCase(PLMObjectType.DCR.toString())) && change.getChangeType().toString().equals(obj)) {
                    PLMDCR dcr = dcrRepository.findOne(cassiniObject.getId());
                    if (dcr != null) {
                        workflowDto.setNumber(dcr.getCrNumber());
                        workflowDto.setType(change.getChangeType().toString());
                        workflowDto.setNumber(dcr.getCrNumber());
                        workflowDto.setDescription(dcr.getTitle());
                        workflowDto.setObjectType("DCR");
                    }
                } else if ((change.getChangeType().equals(ChangeType.DEVIATION) && obj == null) || (obj != null && obj.equalsIgnoreCase(PLMObjectType.DEVIATION.toString())) && change.getChangeType().toString().equals(obj)) {
                    PLMVariance plmVariance = varianceRepository.findOne(cassiniObject.getId());
                    if (plmVariance != null) {
                        workflowDto.setType(change.getChangeType().toString());
                        workflowDto.setNumber(plmVariance.getVarianceNumber());
                        workflowDto.setDescription(plmVariance.getTitle());
                        workflowDto.setObjectType("DEVIATION");
                    }
                } else if ((change.getChangeType().equals(ChangeType.WAIVER) && obj == null) || (obj != null && obj.equalsIgnoreCase(PLMObjectType.WAIVER.toString())) && change.getChangeType().toString().equals(obj)) {
                    PLMVariance plmVariance = varianceRepository.findOne(cassiniObject.getId());
                    if (plmVariance != null) {
                        workflowDto.setType(change.getChangeType().toString());
                        workflowDto.setNumber(plmVariance.getVarianceNumber());
                        workflowDto.setDescription(plmVariance.getTitle());
                        workflowDto.setObjectType("DEVIATION");
                    }
                } else if ((change.getChangeType().equals(ChangeType.ECR) && obj == null) || (obj != null && obj.equalsIgnoreCase(PLMObjectType.ECR.toString())) && change.getChangeType().toString().equals(obj)) {
                    PLMECR ecr = ecrRepository.findOne(cassiniObject.getId());
                    if (ecr != null) {
                        workflowDto.setType(change.getChangeType().toString());
                        workflowDto.setNumber(ecr.getCrNumber());
                        workflowDto.setDescription(ecr.getTitle());
                        workflowDto.setObjectType("ECR");
                    }
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMMCO.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.ITEMMCO.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PLMItemMCO itemMCO = itemMCORepository.findOne(cassiniObject.getId());
                if (itemMCO != null) {
                    workflowDto.setType(itemMCO.getObjectType().toString());
                    workflowDto.setNumber(itemMCO.getMcoNumber());
                    workflowDto.setDescription(itemMCO.getTitle());
                    workflowDto.setObjectType("MCO");
                }

            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.OEMPARTMCO.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.OEMPARTMCO.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PLMManufacturerMCO plmManufacturerMCO = manufacturerMCORepository.findOne(cassiniObject.getId());
                if (plmManufacturerMCO != null) {
                    workflowDto.setType(plmManufacturerMCO.getObjectType().toString());
                    workflowDto.setNumber(plmManufacturerMCO.getMcoNumber());
                    workflowDto.setDescription(plmManufacturerMCO.getTitle());
                    workflowDto.setObjectType("MCO");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.INSPECTIONPLANREVISION.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.INSPECTIONPLANREVISION.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(cassiniObject.getId());
                if (inspectionPlanRevision != null) {
                    PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
                    if (inspectionPlan != null) {
                        workflowDto.setType(inspectionPlanRevision.getObjectType().toString());
                        workflowDto.setNumber(inspectionPlan.getNumber());
                        workflowDto.setDescription(inspectionPlan.getName());
                        workflowDto.setRevision(inspectionPlanRevision.getRevision());
                        workflowDto.setObjectType("INSPECTIONPLANREVISION");
                    }
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.ITEMINSPECTION.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PQMInspection inspection = inspectionRepository.findOne(cassiniObject.getId());
                if (inspection != null) {
                    workflowDto.setType(inspection.getObjectType().toString());
                    workflowDto.setNumber(inspection.getInspectionNumber());
                    workflowDto.setDescription(inspection.getDeviationSummary());
                    workflowDto.setObjectType("ITEMINSPECTION");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MATERIALINSPECTION.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.MATERIALINSPECTION.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PQMInspection inspection = inspectionRepository.findOne(cassiniObject.getId());
                if (inspection != null) {
                    workflowDto.setType(inspection.getObjectType().toString());
                    workflowDto.setNumber(inspection.getInspectionNumber());
                    workflowDto.setDescription(inspection.getDeviationSummary());
                    workflowDto.setObjectType("ITEMINSPECTION");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROBLEMREPORT.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.PROBLEMREPORT.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PQMProblemReport problemReport = problemReportRepository.findOne(cassiniObject.getId());
                if (problemReport != null) {
                    workflowDto.setType(problemReport.getObjectType().toString());
                    workflowDto.setNumber(problemReport.getPrNumber());
                    workflowDto.setDescription(problemReport.getDescription());
                    workflowDto.setObjectType("PROBLEMREPORT");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.NCR.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.NCR.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PQMNCR pqmncr = ncrRepository.findOne(cassiniObject.getId());
                if (pqmncr != null) {
                    workflowDto.setType(pqmncr.getObjectType().toString());
                    workflowDto.setNumber(pqmncr.getNcrNumber());
                    workflowDto.setDescription(pqmncr.getTitle());
                    workflowDto.setObjectType("NCR");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.QCR.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.QCR.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PQMQCR pqmqcr = qcrRepository.findOne(cassiniObject.getId());
                if (pqmqcr != null) {
                    workflowDto.setType(pqmqcr.getObjectType().toString());
                    workflowDto.setNumber(pqmqcr.getQcrNumber());
                    workflowDto.setDescription(pqmqcr.getTitle());
                    workflowDto.setObjectType("QCR");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROJECT.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.PROJECT.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PLMProject project = projectRepository.findOne(cassiniObject.getId());
                if (project != null) {
                    workflowDto.setNumber("");
                    workflowDto.setDescription(project.getName());
                    workflowDto.setType("PROJECT");
                    workflowDto.setProject(project);
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MANUFACTURER.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.MANUFACTURER.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PLMManufacturer manufacturer = manufacturerRepository.findOne(cassiniObject.getId());
                if (manufacturer != null) {
                    workflowDto.setType(manufacturer.getMfrType().getName());
                    workflowDto.setDescription(manufacturer.getName());
                    workflowDto.setObjectType("MANUFACTURER");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MANUFACTURERPART.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.MANUFACTURERPART.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PLMManufacturerPart part = manufacturerPartRepository.findOne(cassiniObject.getId());
                if (part != null) {
                    workflowDto.setNumber(part.getPartNumber());
                    workflowDto.setType(part.getMfrPartType().getName());
                    workflowDto.setDescription(part.getPartName());
                    workflowDto.setId(part.getManufacturer());
                    workflowDto.setObjectType("MANUFACTURERPART");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.SUPPLIERAUDIT.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.SUPPLIERAUDIT.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(cassiniObject.getId());
                if (supplierAudit != null) {
                    workflowDto.setNumber(supplierAudit.getNumber());
                    workflowDto.setType(supplierAudit.getType().getName());
                    workflowDto.setDescription(supplierAudit.getName());
                    workflowDto.setObjectType("SUPPLIERAUDIT");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(MROEnumObject.MROWORKORDER.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(MROEnumObject.MROWORKORDER.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                MROWorkOrder workOrder = workOrderRepository.findOne(cassiniObject.getId());
                if (workOrder != null) {
                    workflowDto.setNumber(workOrder.getNumber());
                    workflowDto.setType(workOrder.getType().getName());
                    workflowDto.setDescription(workOrder.getName());
                    workflowDto.setObjectType("WORKORDER");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PLMNPR.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.PLMNPR.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PLMNpr npr = nprRepository.findOne(cassiniObject.getId());
                if (npr != null) {
                    workflowDto.setNumber(npr.getNumber());
                    workflowDto.setType("PLMNPR");
                    workflowDto.setDescription(npr.getDescription());
                    workflowDto.setObjectType("PLMNPR");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROGRAMTEMPLATE.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.PROGRAMTEMPLATE.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                ProgramTemplate npr = programTemplateRepository.findOne(cassiniObject.getId());
                if (npr != null) {
                    workflowDto.setNumber("");
                    workflowDto.setType("PROGRAMTEMPLATE");
                    workflowDto.setDescription(npr.getName());
                    workflowDto.setObjectType("PROGRAMTEMPLATE");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROGRAM.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.PROGRAM.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PLMProgram npr = programRepository.findOne(cassiniObject.getId());
                if (npr != null) {
                    workflowDto.setNumber("");
                    workflowDto.setType("PROGRAM");
                    workflowDto.setDescription(npr.getName());
                    workflowDto.setObjectType("PROGRAM");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.TEMPLATE.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.TEMPLATE.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                ProjectTemplate npr = projectTemplateRepository.findOne(cassiniObject.getId());
                if (npr != null) {
                    workflowDto.setNumber("");
                    workflowDto.setType("TEMPLATE");
                    workflowDto.setDescription(npr.getName());
                    workflowDto.setObjectType("TEMPLATE");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROJECTTASK.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.PROJECTTASK.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PLMTask npr = taskRepository.findOne(cassiniObject.getId());
                if (npr != null) {
                    workflowDto.setNumber("");
                    workflowDto.setType("PROJECTTASK");
                    workflowDto.setDescription(npr.getName());
                    workflowDto.setObjectType("PROJECTTASK");
                    workflowDto.setTask(npr);
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PROJECTACTIVITY.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.PROJECTACTIVITY.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                PLMActivity npr = activityRepository.findOne(cassiniObject.getId());
                if (npr != null) {
                    workflowDto.setNumber("");
                    workflowDto.setType("PROJECTACTIVITY");
                    workflowDto.setDescription(npr.getName());
                    workflowDto.setObjectType("PROJECTACTIVITY");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.TEMPLATETASK.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.TEMPLATETASK.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                ProjectTemplateTask npr = projectTemplateTaskRepository.findOne(cassiniObject.getId());
                if (npr != null) {
                    workflowDto.setNumber("");
                    workflowDto.setType("TEMPLATETASK");
                    workflowDto.setDescription(npr.getName());
                    workflowDto.setObjectType("TEMPLATETASK");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.TEMPLATEACTIVITY.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(PLMObjectType.TEMPLATEACTIVITY.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                ProjectTemplateActivity npr = projectTemplateActivityRepository.findOne(cassiniObject.getId());
                if (npr != null) {
                    workflowDto.setNumber(npr.getName());
                    workflowDto.setType("TEMPLATEACTIVITY");
                    workflowDto.setDescription(npr.getDescription());
                    workflowDto.setObjectType("TEMPLATEACTIVITY");
                }
            } else if ((cassiniObject.getObjectType().equals(ObjectType.valueOf(ObjectType.CUSTOMOBJECT.toString())) && obj == null)
                    || (obj != null && obj.equalsIgnoreCase(ObjectType.CUSTOMOBJECT.toString())) && cassiniObject.getObjectType().toString().equals(obj)) {
                CustomObject customObject = customObjectRepository.findOne(cassiniObject.getId());
                if (customObject != null) {
                    workflowDto.setNumber(customObject.getNumber());
                    workflowDto.setType("CUSTOMOBJECT");
                    workflowDto.setDescription(customObject.getDescription());
                    workflowDto.setObjectType("CUSTOMOBJECT");
                }
            }

            if (workflowDto.getNumber() != null) {
                workflowDtos.add(workflowDto);
            }
        }

        return workflowDtos;
    }

    @Transactional
    public List<Person> getWorkflowAssignmentPersons(Integer id, String type) {
        List<Person> persons = new ArrayList<>();
        List<Login> logins = loginRepository.findByIsActive(true);
        List<PLMWorkFlowStatusApprover> approvers = workFlowStatusApproverRepository.findByStatus(id);
        List<PLMWorkFlowStatusAcknowledger> acknowledgers = workFlowStatusAcknowledgerRepository.findByStatus(id);
        List<PLMWorkFlowStatusObserver> observers = plmWorkFlowStatusObserverRepository.findByStatus(id);
        for (Login login : logins) {
            Boolean exitPerson = false;
            for (PLMWorkFlowStatusApprover approver : approvers) {
                if (approver.getPerson().equals(login.getPerson().getId())) {
                    exitPerson = true;
                }
            }
            for (PLMWorkFlowStatusAcknowledger acknowledger : acknowledgers) {
                if (acknowledger.getPerson().equals(login.getPerson().getId())) {
                    exitPerson = true;
                }
            }
            for (PLMWorkFlowStatusObserver observer : observers) {
                if (observer.getPerson().equals(login.getPerson().getId())) {
                    exitPerson = true;
                }
            }
            if (!exitPerson) {
                if (login.getExternal()) {
                    login.getPerson().setExternal(true);
                }
                persons.add(login.getPerson());
            }

        }
        return persons;
    }

    @Async
    @EventListener
    public void attachedToObjectDeletedEvent(WorkflowEvents.AttachedToObjectDeletedEvent event) {
        deleteWorkflow(event.getAttachedTo());
    }

    @Async
    @EventListener
    public void ProjectActivityTaskWorkflowDeletedEvent(WorkflowEvents.ProjectActivityTaskWorkflowDeletedEvent event) {
        deleteProjectActivityTaskWorkflow(event.getProject());
    }

    @Async
    @EventListener
    public void ProgramProjectActivityTaskWorkflowDeletedEvent(WorkflowEvents.ProgramProjectActivityTaskWorkflowDeletedEvent event) {
        deleteProgramProjectActivityTaskWorkflow(event.getProgram());
    }

    private PLMObjectType getFilterObjectType(String obj) {
        PLMObjectType objectType = null;
        if (obj.equals("ECO")) {
            objectType = PLMObjectType.ECO;
        } else if (obj.equals("DCO")) {
            objectType = PLMObjectType.DCO;
        } else if (obj.equals("ECR")) {
            objectType = PLMObjectType.ECR;
        } else if (obj.equals("DCR")) {
            objectType = PLMObjectType.DCR;
        } else if (obj.equals("ITEM")) {
            objectType = PLMObjectType.ITEMREVISION;
        } else if (obj.equals("DEVIATION")) {
            objectType = PLMObjectType.DEVIATION;
        } else if (obj.equals("WAIVER")) {
            objectType = PLMObjectType.WAIVER;
        } else if (obj.equals("ITEMMCO")) {
            objectType = PLMObjectType.ITEMMCO;
        } else if (obj.equals("OEMPARTMCO")) {
            objectType = PLMObjectType.OEMPARTMCO;
        } else if (obj.equals("INSPECTIONPLANREVISION")) {
            objectType = PLMObjectType.INSPECTIONPLANREVISION;
        } else if (obj.equals("ITEMINSPECTION")) {
            objectType = PLMObjectType.ITEMINSPECTION;
        } else if (obj.equals("MATERIALINSPECTION")) {
            objectType = PLMObjectType.MATERIALINSPECTION;
        } else if (obj.equals("PROBLEMREPORT")) {
            objectType = PLMObjectType.PROBLEMREPORT;
        } else if (obj.equals("NCR")) {
            objectType = PLMObjectType.NCR;
        } else if (obj.equals("QCR")) {
            objectType = PLMObjectType.QCR;
        } else if (obj.equals("PROJECT")) {
            objectType = PLMObjectType.PROJECT;
        } else if (obj.equals("PROJECTACTIVITY")) {
            objectType = PLMObjectType.PROJECTACTIVITY;
        } else if (obj.equals("PROJECTTASK")) {
            objectType = PLMObjectType.PROJECTTASK;
        } else if (obj.equals("MANUFACTURER")) {
            objectType = PLMObjectType.MANUFACTURER;
        } else if (obj.equals("MANUFACTURERPART")) {
            objectType = PLMObjectType.MANUFACTURERPART;
        } else if (obj.equals("SUPPLIERAUDIT")) {
            objectType = PLMObjectType.SUPPLIERAUDIT;
        } else if (obj.equals("WORKORDER")) {
            objectType = PLMObjectType.MROWORKORDER;
        } else if (obj.equals("PLMNPR")) {
            objectType = PLMObjectType.PLMNPR;
        } else if (obj.equals("CUSTOMOBJECT")) {
            objectType = PLMObjectType.CUSTOMOBJECT;
        }
        return objectType;
    }

    @Transactional(readOnly = true)
    public Page<WorkflowDto> getFilterWorkflowInstancess(String obj, Pageable pageable, WorkflowCriteria criteria) {
        PLMObjectType objectType = getFilterObjectType(obj);
        criteria.setObjectType(objectType);
        Predicate predicate = workflowPredicateBuilder.getWfObjectSearchPredicate(criteria, QPLMWorkflow.pLMWorkflow);
        Page<PLMWorkflow> workflows = plmWorkflowRepository.findAll(predicate, pageable);
        List<WorkflowDto> workflowDtos = new LinkedList();
        Map<Integer, WorkflowDto> map = new LinkedHashMap<>();
        List<Integer> cassiniObjectIds = new LinkedList<>();
        for (PLMWorkflow workflow1 : workflows) {
            WorkflowDto workflowDto = new WorkflowDto();
            cassiniObjectIds.add(workflow1.getAttachedTo());
            workflowDto.setWorkflowNumber(plmWorkflowDefinitionRepository.findOne(workflow1.getWorkflowRevision()).getMaster().getNumber());
            PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow1.getCurrentStatus());
            List<PLMWorkflowStatusHistory> statusHistories = plmWorkflowStatusHistoryRepository.findByWorkflowOrderByTimestampAsc(workflow1.getId());
            if (statusHistories.size() > 0) {
                workflowDto.setTimeStamp(statusHistories.get(0).getTimestamp());
                workflowDto.setPreviousStatus(plmWorkflowStatusRepository.findOne(statusHistories.get(0).getStatus()).getName());
                workflowDto.setPreviousStatuType(plmWorkflowStatusRepository.findOne(statusHistories.get(0).getStatus()).getType());
            } else if (!workflowStatus.getType().equals(WorkflowStatusType.START)) {
                workflowDto.setTimeStamp(workflow1.getModifiedDate());
            }
            if (workflow1.getOnhold()) {
                workflowDto.setCurrentStatus(workflowStatus.getName());
                workflowDto.setStatusType(workflowStatus.getType());
                if (workflow1.getHoldBy() != null) {
                    workflowDto.setHoldBy(personRepository.findOne(workflow1.getHoldBy()).getFullName());
                }
            } else {
                workflowDto.setCurrentStatus(workflowStatus.getName());
                workflowDto.setStatusType(workflowStatus.getType());
            }
            workflowDto.setWorkflow(workflow1);
            map.put(workflow1.getAttachedTo(), workflowDto);
            workflowDtos.add(workflowDto);

        }
        // return workflowDtos;
        return new PageImpl<WorkflowDto>(getCassiniObjects(objectRepository.findByIdInOrderByModifiedDateDesc(cassiniObjectIds), map, obj), pageable, workflowDtos.size());
    }


    @Transactional(readOnly = true)
    public List<PLMWorkflowStatus> getWorkflowStatuses(Integer id) {
        List<PLMWorkflowStatus> statuses = plmWorkflowStatusRepository.getWorkflowStatuses(id);
        return statuses;
    }

    @Transactional(readOnly = true)
    public Map<Integer, WorkflowStatusAssignmentsDto> getWorkflowAssignments(Integer workflow) {
        Map<Integer, WorkflowStatusAssignmentsDto> assignmentsDtoMap = new LinkedHashMap<>();
        List<PLMWorkflowStatus> statuses = plmWorkflowStatusService.getByWorkflow(workflow);
        statuses.forEach(workflowStatus -> {
            WorkflowStatusAssignmentsDto assignmentsDto = new WorkflowStatusAssignmentsDto();
            List<PLMWorkFlowStatusApprover> approvers = plmWorkFlowStatusApproverRepository.findByStatus(workflowStatus.getId());
            approvers.forEach(approver -> {
                approver.setPersonName(personRepository.findOne(approver.getPerson()).getFullName());
            });
            List<PLMWorkFlowStatusObserver> observers = plmWorkFlowStatusObserverRepository.findByStatus(workflowStatus.getId());
            observers.forEach(observer -> {
                observer.setPersonName(personRepository.findOne(observer.getPerson()).getFullName());
            });
            List<PLMWorkFlowStatusAcknowledger> acknowledgers = plmWorkFlowStatusAcknowledgerRepository.findByStatus(workflowStatus.getId());
            acknowledgers.forEach(acknowledger -> {
                acknowledger.setPersonName(personRepository.findOne(acknowledger.getPerson()).getFullName());
            });
            assignmentsDto.getApprovers().addAll(approvers);
            assignmentsDto.getObservers().addAll(observers);
            assignmentsDto.getAcknowledgers().addAll(acknowledgers);
            assignmentsDtoMap.put(workflowStatus.getId(), assignmentsDto);
        });

        return assignmentsDtoMap;
    }

    @Transactional
    public Page<Person> getWorkflowStatusAssignmentPersons(Pageable pageable, WorkflowCriteria criteria) {
        Predicate predicate = workflowStatusAssignmentPersonPredicateBuilder.build(criteria, QLogin.login);
        Page<Login> logins = loginRepository.findAll(predicate, pageable);
        List<Person> persons = new LinkedList<>();
        for (Login login : logins) {
            if (login.getExternal()) {
                login.getPerson().setExternal(true);
            }
            persons.add(login.getPerson());
        }
        return new PageImpl<Person>(persons, pageable, logins.getTotalElements());
    }

    @Transactional(readOnly = true)
    public WorkflowStatusDTO setWorkflowStatusSettings(Integer objectId) {
        WorkflowStatusDTO dto = new WorkflowStatusDTO();
        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(objectId);
        if (workflow != null) {
            dto.setOnHold(workflow.getOnhold());
            setSettings(dto, workflow);
            if (workflow.getStart().getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                dto.setStartWorkflow(true);
            }
            if (workflow.getFinish().getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                dto.setFinishWorkflow(true);
            }
            dto.setCancelWorkflow(workflow.getCancelled());
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public void setSettings(WorkflowStatusDTO dto, PLMWorkflow workflow) {
        Preference preference = preferenceRepository.findByPreferenceKey("DEFAULT_WORKFLOW_STATUS");
        PLMWorkflowStatus workflowCurrentStatus = plmWorkflowStatusRepository.findOne(workflow.getCurrentStatus());
        if (preference != null && !preference.getBooleanValue()) {
            if (!workflow.getStart().getId().equals(workflow.getCurrentStatus())) {
                if (workflowCurrentStatus.getFlag().equals(WorkflowStatusFlag.TERMINATED)) {
                    PLMWorkflowStatus workflowStatusFinished = plmWorkflowStatusRepository.findOne(workflowCurrentStatus.getTransitionedFrom());
                    dto.setWorkflowSettingStatus(workflowStatusFinished.getName());
                    dto.setWorkflowSettingStatusType(workflowStatusFinished.getType().name());
                } else {
                    dto.setWorkflowSettingStatus(workflowCurrentStatus.getName());
                    dto.setWorkflowSettingStatusType(workflowCurrentStatus.getType().name());
                }
            }
        } else {
            if (workflow.getFinished()) {
                dto.setWorkflowSettingStatus(workflowCurrentStatus.getName());
                dto.setWorkflowSettingStatusType(workflowCurrentStatus.getType().name());
            } else if (workflowCurrentStatus.getTransitionedFrom() != null) {
                PLMWorkflowStatus workflowStatusFinished = plmWorkflowStatusRepository.findOne(workflowCurrentStatus.getTransitionedFrom());
                dto.setWorkflowSettingStatus(workflowStatusFinished.getName());
                dto.setWorkflowSettingStatusType(workflowStatusFinished.getType().name());
            } else if (workflowCurrentStatus.getFlag().equals(WorkflowStatusFlag.INPROCESS) && !workflow.getStart().getId().equals(workflow.getCurrentStatus())) {
                dto.setWorkflowSettingStatus(workflow.getStart().getName());
                dto.setWorkflowSettingStatusType(workflow.getStart().getType().name());
            }

        }
    }

    @Transactional(readOnly = true)
    public List<String> getObjectWorkflowStatus(PLMObjectType objectType) {
        List<String> status = new ArrayList<>();
        List<Integer> workflows = new ArrayList<>();
        Preference preference = preferenceRepository.findByPreferenceKey("DEFAULT_WORKFLOW_STATUS");
        if (objectType.equals(PLMObjectType.ECR) || objectType.equals(PLMObjectType.ECO) || objectType.equals(PLMObjectType.DCR)
                || objectType.equals(PLMObjectType.DCO) || objectType.equals(PLMObjectType.DEVIATION) || objectType.equals(PLMObjectType.WAIVER)) {
            List<Integer> attachedToIds = plmWorkflowRepository.getAttachedToIdsByType(PLMObjectType.CHANGE);
            if (attachedToIds.size() > 0) {
                List<Integer> changeIds = changeRepository.getIdsByIdsAndType(attachedToIds, ChangeType.valueOf(objectType.name()));
                if (changeIds.size() > 0) {
                    workflows = plmWorkflowRepository.getIdsByAttachedToIds(changeIds);
                }
            }
        } else if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLAN) || objectType.equals(PLMObjectType.MATERIALINSPECTIONPLAN)) {
            List<Integer> attachedToIds = plmWorkflowRepository.getAttachedToIdsByType(PLMObjectType.INSPECTIONPLANREVISION);
            if (attachedToIds.size() > 0) {
                List<Integer> inspectionPlanRevision = new ArrayList<>();
                if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLAN)) {
                    inspectionPlanRevision = productInspectionPlanRepository.getIdsByIds(attachedToIds);
                } else {
                    inspectionPlanRevision = materialInspectionPlanRepository.getIdsByIds(attachedToIds);
                }
                if (inspectionPlanRevision.size() > 0) {
                    workflows = plmWorkflowRepository.getIdsByAttachedToIds(inspectionPlanRevision);
                }
            }
        } else {
            workflows = plmWorkflowRepository.getObjectTypeWorkflows(objectType);
        }
        if (workflows.size() > 0) {
            if (preference != null) {
                if (!preference.getBooleanValue()) {
                    List<Integer> workflowIds = plmWorkflowRepository.getCurrentStatusIdsByIdsAndStartNotEqualToCurrentStatus(workflows);
                    if (workflowIds.size() > 0) {
                        status = plmWorkflowStatusRepository.getWorkflowStatusesNamesNotTerminated(workflowIds);
                        List<Integer> transitionFromIds = plmWorkflowStatusRepository.getWorkflowTransitionFromIdsNamesByIdsAndTerminated(workflowIds);
                        if (transitionFromIds.size() > 0) {
                            status.addAll(plmWorkflowStatusRepository.getWorkflowStatusesNames(transitionFromIds));
                        }
                    }

                    Integer currentStatusCount = plmWorkflowRepository.getCurrentStatusCountByIdsAndStartEqualToCurrentStatus(workflows);
                    if (currentStatusCount > 0) {
                        status.add("None");
                    }
                } else {
                    List<Integer> transitionedFromIds = plmWorkflowStatusRepository.getTransitionedFromIdsByWorkflow(workflows);
                    if (transitionedFromIds.size() > 0) {
                        status = plmWorkflowStatusRepository.getWorkflowStatusesNames(transitionedFromIds);
                    }
                    List<Integer> objectIds = plmWorkflowRepository.getIdsByIdsAndStartNotEqualCurrentStatus(workflows);
                    if (objectIds.size() > 0) {
                        Integer count = plmWorkflowStatusRepository.getTransitionedFromIsNullCountByWorkflow(objectIds);
                        if (count > 0) {
                            status.add("Start");
                        }
                    }
                    objectIds = plmWorkflowRepository.getIdsByIdsAndStartEqualCurrentStatus(workflows);
                    if (objectIds.size() > 0) {
                        status.add("None");
                    }
                    List<String> finishedStatusNames = plmWorkflowStatusRepository.getNamesByTypeAndFlag(workflows);
                    status.addAll(finishedStatusNames);
                }
                status = status.stream().distinct().collect(Collectors.toList());
            }
        }
        return status;
    }

    @Transactional(readOnly = true)
    public List<Integer> getWorkflowAttachedToIdsByStatusAndType(String status, PLMObjectType objectType) {
        List<Integer> objectIds = new ArrayList<>();
        Preference preference = preferenceRepository.findByPreferenceKey("DEFAULT_WORKFLOW_STATUS");
        if (preference != null) {
            if (!preference.getBooleanValue()) {
                if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLAN) || objectType.equals(PLMObjectType.MATERIALINSPECTIONPLAN)) {
                    objectType = PLMObjectType.INSPECTIONPLANREVISION;
                }
                if (status.equals("None")) {
                    objectIds = plmWorkflowRepository.getStartWorkflowIds(objectType);
                } else {
                    List<Integer> ids = plmWorkflowStatusRepository.getIdsByName(status);
                    if (ids.size() > 0) {
                        objectIds = plmWorkflowRepository.getCurrentStatusIdsByTypeAndStatus(objectType, ids);
                        ids = plmWorkflowStatusRepository.getWorkflowIdsNamesByIdsAndTerminated(ids);
                        if (ids.size() > 0) {
                            objectIds.addAll(plmWorkflowRepository.getAttachedToIdsByIds(ids));
                        }
                    }
                }
            } else {
                if (status.equals("None")) {
                    objectIds = plmWorkflowRepository.getStartWorkflowIds(objectType);
                } else if (status.equals("Start")) {
                    List<Integer> workflows = new ArrayList<>();
                    if (objectType.equals(PLMObjectType.ECR) || objectType.equals(PLMObjectType.ECO) || objectType.equals(PLMObjectType.DCR)
                            || objectType.equals(PLMObjectType.DCO) || objectType.equals(PLMObjectType.DEVIATION) || objectType.equals(PLMObjectType.WAIVER)) {
                        List<Integer> attachedToIds = plmWorkflowRepository.getAttachedToIdsByType(PLMObjectType.CHANGE);
                        if (attachedToIds.size() > 0) {
                            List<Integer> changeIds = changeRepository.getIdsByIdsAndType(attachedToIds, ChangeType.valueOf(objectType.name()));
                            if (changeIds.size() > 0) {
                                workflows = plmWorkflowRepository.getIdsByAttachedToIds(changeIds);
                            }
                        }
                    } else if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLAN) || objectType.equals(PLMObjectType.MATERIALINSPECTIONPLAN)) {
                        List<Integer> attachedToIds = plmWorkflowRepository.getAttachedToIdsByType(PLMObjectType.INSPECTIONPLANREVISION);
                        if (attachedToIds.size() > 0) {
                            List<Integer> inspectionPlanRevision = new ArrayList<>();
                            if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLAN)) {
                                inspectionPlanRevision = productInspectionPlanRepository.getIdsByIds(attachedToIds);
                            } else {
                                inspectionPlanRevision = materialInspectionPlanRepository.getIdsByIds(attachedToIds);
                            }
                            if (inspectionPlanRevision.size() > 0) {
                                workflows = plmWorkflowRepository.getIdsByAttachedToIds(inspectionPlanRevision);
                            }
                        }
                    } else {
                        workflows = plmWorkflowRepository.getObjectTypeWorkflows(objectType);
                    }
                    if (workflows.size() > 0) {
                        objectIds = plmWorkflowRepository.getIdsByIdsAndStartNotEqualCurrentStatus(workflows);
                        if (objectIds.size() > 0) {
                            List<Integer> workflowIds = plmWorkflowStatusRepository.getTransitionedFromIsNullByWorkflow(objectIds);
                            if (workflowIds.size() > 0) {
                                objectIds = plmWorkflowRepository.getAttachedToIdsByIds(workflowIds);
                            }
                        }
                    }
                } else {
                    if (objectType.equals(PLMObjectType.PRODUCTINSPECTIONPLAN) || objectType.equals(PLMObjectType.MATERIALINSPECTIONPLAN)) {
                        objectType = PLMObjectType.INSPECTIONPLANREVISION;
                    }
                    List<Integer> ids = plmWorkflowStatusRepository.getIdsByName(status);
                    List<Integer> workflowIds = plmWorkflowStatusRepository.getWorflowsByIds(ids);
                    if (workflowIds.size() > 0) {
                        objectIds = plmWorkflowRepository.getAttachedToIdsByIdsAndType(objectType, workflowIds);
                    } else {
                        List<Integer> finishWorkflowIds = plmWorkflowStatusRepository.getWorkflowIdsByFlagAndStatus(ids);
                        if (finishWorkflowIds.size() > 0) {
                            objectIds = plmWorkflowRepository.getAttachedToIdsByIdsAndType(objectType, finishWorkflowIds);
                        }
                    }
                }
            }
        }

        return objectIds;
    }
}