package com.cassinisys.plm.service.tm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.plm.event.PushNotificationEvents;
import com.cassinisys.plm.model.mfr.PLMMfrPartInspectionReport;
import com.cassinisys.plm.model.plm.PLMDocumentReviewer;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PLMActivity;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.pqm.PQMPPAPChecklist;
import com.cassinisys.plm.model.pqm.PQMSupplierAudit;
import com.cassinisys.plm.model.pqm.PQMSupplierAuditPlan;
import com.cassinisys.plm.model.pqm.PQMSupplierAuditReviewer;
import com.cassinisys.plm.model.req.PLMRequirementReviewer;
import com.cassinisys.plm.model.req.PLMRequirementVersion;
import com.cassinisys.plm.model.wf.PLMUserTask;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.UserTaskStatus;
import com.cassinisys.plm.repo.mfr.MfrPartInspectionReportRepository;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.pm.PLMActivityRepository;
import com.cassinisys.plm.repo.pqm.PPAPChecklistRepository;
import com.cassinisys.plm.repo.pqm.PQMSupplierAuditPlanRepository;
import com.cassinisys.plm.repo.pqm.SupplierAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserTaskEventService {
    @Autowired
    private UserTaskService userTaskService;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private MfrPartInspectionReportRepository mfrPartInspectionReportRepository;
    @Autowired
    private PPAPChecklistRepository ppapChecklistRepository;
    @Autowired
    private PQMSupplierAuditPlanRepository supplierAuditPlanRepository;
    @Autowired
    private SupplierAuditRepository supplierAuditRepository;

    @Async
    @EventListener
    public void projectActivityAssigned(UserTaskEvents.ProjectActivityAssignedEvent event) {
        PLMProject plmProject = event.getProject();
        PLMActivity plmActivity = event.getActivity();

        PLMUserTask task = userTaskService.getBySource(plmActivity.getId());
        if (task != null && task.getAssignedTo() != plmActivity.getAssignedTo()) {
            task.setAssignedTo(plmActivity.getAssignedTo());
            userTaskService.create(task);
        }
        if (task == null && plmActivity.getAssignedTo() != null) {
            task = new PLMUserTask();
            task.setAssignedTo(plmActivity.getAssignedTo());
            task.setDueDate(plmActivity.getPlannedFinishDate());
            task.setSource(plmActivity.getId());
            task.setSourceType(ObjectType.valueOf("PROJECTACTIVITY"));
            task.setContext(plmProject.getId());
            task.setContextType(ObjectType.valueOf("PROJECT"));
            userTaskService.create(task);
        }
    }

    @Async
    @EventListener
    public void projectActivityFinished(UserTaskEvents.ProjectActivityFinishedEvent event) {
        PLMProject plmProject = event.getProject();
        PLMActivity plmActivity = event.getActivity();

        PLMUserTask task = userTaskService.getBySource(plmActivity.getId());
        if (task != null) {
            task.setStatus(UserTaskStatus.FINISHED);
            task.setFinishedOn(new Date());
            userTaskService.update(task);
        }
    }

    @Async
    @EventListener
    public void projectActivityDeleted(UserTaskEvents.ProjectActivityDeletedEvent event) {
        PLMProject plmProject = event.getProject();
        PLMActivity plmActivity = event.getActivity();

        PLMUserTask task = userTaskService.getBySource(plmActivity.getId());
        if (task != null) {
            userTaskService.delete(task);
        }
    }

    @Async
    @EventListener
    public void projectTaskAssigned(UserTaskEvents.ProjectTaskAssignedEvent event) {
        PLMProject plmProject = event.getProject();
        PLMTask plmTask = event.getTask();

        PLMUserTask task = userTaskService.getBySource(plmTask.getId());
        if (task != null && task.getAssignedTo() != plmTask.getAssignedTo()) {
            task.setAssignedTo(plmTask.getAssignedTo());
            userTaskService.create(task);
        }
        List<String> tokens = new ArrayList();
        if (task == null && plmTask.getAssignedTo() != null) {
            task = new PLMUserTask();
            task.setAssignedTo(plmTask.getAssignedTo());
            task.setDueDate(plmTask.getPlannedFinishDate());
            task.setSource(plmTask.getId());
            task.setSourceType(ObjectType.valueOf("PROJECTTASK"));
            task.setContext(plmProject.getId());
            task.setContextType(ObjectType.valueOf("PROJECT"));
            userTaskService.create(task);
            Person person = personRepository.findOne(plmTask.getAssignedTo());
            if (person.getMobileDevice() != null && person.getMobileDevice().getDeviceId() != null) {
                tokens.add(person.getMobileDevice().getDeviceId());
            }
            String notificationMsg = getMobileNotificationMsg(task, plmTask.getName());

            if (tokens.size() > 0) {
                applicationEventPublisher.publishEvent(new PushNotificationEvents.UserTaskNotification("task", notificationMsg, tokens));
            }
            task = userTaskService.getBySource(plmTask.getActivity());
            if (task != null) {
                task.setStatus(UserTaskStatus.PENDING);
                task.setFinishedOn(null);
                userTaskService.update(task);
            }
        }
    }

    @Async
    @EventListener
    public void projectTaskFinished(UserTaskEvents.ProjectTaskFinishedEvent event) {
        PLMProject plmProject = event.getProject();
        PLMTask plmTask = event.getTask();

        PLMUserTask task = userTaskService.getBySource(plmTask.getId());
        if (task != null) {
            task.setStatus(UserTaskStatus.FINISHED);
            task.setFinishedOn(new Date());
            userTaskService.update(task);
        }
    }

    @Async
    @EventListener
    public void projectTaskDeleted(UserTaskEvents.ProjectTaskDeletedEvent event) {
        PLMProject plmProject = event.getProject();
        PLMTask plmTask = event.getTask();

        PLMUserTask task = userTaskService.getBySource(plmTask.getId());
        if (task != null) {
            userTaskService.delete(task);
        }
    }

    @Async
    @EventListener
    public void requirementAssigned(UserTaskEvents.RequirementAssignedEvent event) {
        PLMRequirementVersion requirementVersion = event.getRequirementVersion();
        PLMRequirementReviewer requirementReviewer = event.getRequirementReviewer();

        PLMUserTask task = userTaskService.getBySourceAndAssigned(requirementVersion.getId(), requirementReviewer.getReviewer());
        List<String> tokens = new ArrayList();
        if (task == null && requirementReviewer.getReviewer() != null && requirementReviewer.getApprover()) {
            task = new PLMUserTask();
            task.setAssignedTo(requirementReviewer.getReviewer());
            task.setDueDate(requirementVersion.getPlannedFinishDate());
            task.setSource(requirementVersion.getId());
            task.setSourceType(ObjectType.valueOf("REQUIREMENT"));
            task.setContext(requirementVersion.getRequirementDocumentRevision().getId());
            task.setContextType(ObjectType.valueOf("REQUIREMENTDOCUMENTREVISION"));
            userTaskService.create(task);
            Person person = personRepository.findOne(requirementReviewer.getReviewer());
            if (person.getMobileDevice() != null && person.getMobileDevice().getDeviceId() != null) {
                tokens.add(person.getMobileDevice().getDeviceId());
            }
            String notificationMsg = getMobileNotificationMsg(task, requirementVersion.getName());

            if (tokens.size() > 0) {
                applicationEventPublisher.publishEvent(new PushNotificationEvents.UserTaskNotification("requirement", notificationMsg, tokens));
            }
        } else if (task != null && !requirementReviewer.getApprover()) {
            userTaskService.delete(task);
        }
    }

    @Async
    @EventListener
    public void requirementAssigned(UserTaskEvents.DocumentAssignedEvent event) {
        Integer document = event.getDocument();
        PLMFile plmFile = fileRepository.findOne(document);
        PLMDocumentReviewer documentReviewer = event.getDocumentReviewer();

        PLMUserTask task = userTaskService.getBySourceAndAssigned(document, documentReviewer.getReviewer());
        List<String> tokens = new ArrayList();
        if (task == null && documentReviewer.getReviewer() != null && documentReviewer.getApprover()) {
            task = new PLMUserTask();
            task.setAssignedTo(documentReviewer.getReviewer());
            if (plmFile.getObjectType().name().equals(PLMObjectType.DOCUMENT.name())) {
                task.setSource(document);
                task.setSourceType(ObjectType.valueOf(plmFile.getObjectType().name()));
                task.setContext(document);
                task.setContextType(ObjectType.valueOf(plmFile.getObjectType().name()));
            } else if (plmFile.getObjectType().name().equals(PLMObjectType.MFRPARTINSPECTIONREPORT.name())) {
                PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(plmFile.getId());
                task.setSource(document);
                task.setSourceType(ObjectType.valueOf(plmFile.getObjectType().name()));
                task.setContext(mfrPartInspectionReport.getManufacturerPart());
                task.setContextType(ObjectType.valueOf(PLMObjectType.MANUFACTURERPART.name()));
            } else if (plmFile.getObjectType().name().equals(PLMObjectType.PPAPCHECKLIST.name())) {
                PQMPPAPChecklist checklist = ppapChecklistRepository.findOne(plmFile.getId());
                task.setSource(document);
                task.setSourceType(ObjectType.valueOf(plmFile.getObjectType().name()));
                task.setContext(checklist.getPpap());
                task.setContextType(ObjectType.valueOf(PLMObjectType.PPAP.name()));
            }
            userTaskService.create(task);
            Person person = personRepository.findOne(documentReviewer.getReviewer());
            if (person.getMobileDevice() != null && person.getMobileDevice().getDeviceId() != null) {
                tokens.add(person.getMobileDevice().getDeviceId());
            }
            String notificationMsg = getMobileNotificationMsg(task, plmFile.getName());

            if (tokens.size() > 0) {
                applicationEventPublisher.publishEvent(new PushNotificationEvents.UserTaskNotification("requirement", notificationMsg, tokens));
            }
        } else if (task != null && !documentReviewer.getApprover()) {
            userTaskService.delete(task);
        }
    }

    @Async
    @EventListener
    public void supplierAuditPlanAssigned(UserTaskEvents.SupplierAuditPlanAssignedEvent event) {
        Integer plan = event.getSupplierAuditPlan();
        PQMSupplierAuditPlan supplierAuditPlan = supplierAuditPlanRepository.findOne(plan);
        PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(supplierAuditPlan.getSupplierAudit());
        PQMSupplierAuditReviewer supplierAuditReviewer = event.getSupplierAuditReviewer();

        PLMUserTask task = userTaskService.getBySourceAndAssigned(supplierAuditReviewer.getPlan(), supplierAuditReviewer.getReviewer());
        List<String> tokens = new ArrayList();
        if (task == null && supplierAuditReviewer.getReviewer() != null && supplierAuditReviewer.getApprover()) {
            task = new PLMUserTask();
            task.setAssignedTo(supplierAuditReviewer.getReviewer());
            task.setSource(supplierAuditReviewer.getPlan());
            task.setSourceType(ObjectType.valueOf(PLMObjectType.SUPPLIERAUDITPLAN.name()));
            task.setContext(supplierAuditPlan.getSupplierAudit());
            task.setContextType(ObjectType.valueOf(PLMObjectType.SUPPLIERAUDIT.name()));
            userTaskService.create(task);
            Person person = personRepository.findOne(supplierAuditReviewer.getReviewer());
            if (person.getMobileDevice() != null && person.getMobileDevice().getDeviceId() != null) {
                tokens.add(person.getMobileDevice().getDeviceId());
            }
            String notificationMsg = getMobileNotificationMsg(task, supplierAudit.getName());

            if (tokens.size() > 0) {
                applicationEventPublisher.publishEvent(new PushNotificationEvents.UserTaskNotification("supplier audit", notificationMsg, tokens));
            }
        } else if (task != null && !supplierAuditReviewer.getApprover()) {
            userTaskService.delete(task);
        }
    }

    @Async
    @EventListener
    public void documentDeleted(UserTaskEvents.DocumentDeletedEvent event) {
        Integer requirement = event.getDocument();
        PLMDocumentReviewer documentReviewer = event.getDocumentReviewer();

        PLMUserTask task = userTaskService.getBySourceAndAssigned(requirement, documentReviewer.getReviewer());
        if (task != null) {
            userTaskService.delete(task);
        }
    }

    @Async
    @EventListener
    public void supplierAuditPlanDeleted(UserTaskEvents.SupplierAuditPlanDeletedEvent event) {
        Integer requirement = event.getSupplierAuditPlan();
        PQMSupplierAuditReviewer documentReviewer = event.getSupplierAuditReviewer();

        PLMUserTask task = userTaskService.getBySourceAndAssigned(requirement, documentReviewer.getReviewer());
        if (task != null) {
            userTaskService.delete(task);
        }
    }


    @Async
    @EventListener
    public void documentSubmitted(UserTaskEvents.DocumentSubmittedEvent event) {
        Integer requirement = event.getDocument();
        PLMDocumentReviewer documentReviewer = event.getReviewer();

        PLMUserTask task = userTaskService.getBySourceAndAssigned(requirement, documentReviewer.getReviewer());
        if (task != null) {
            task.setStatus(UserTaskStatus.FINISHED);
            task.setFinishedOn(new Date());
            userTaskService.update(task);
        }
    }

    @Async
    @EventListener
    public void documentSubmitted(UserTaskEvents.SupplierAuditPlanSubmittedEvent event) {
        Integer requirement = event.getSupplierAuditPlan();
        PQMSupplierAuditReviewer documentReviewer = event.getSupplierAuditReviewer();

        PLMUserTask task = userTaskService.getBySourceAndAssigned(requirement, documentReviewer.getReviewer());
        if (task != null) {
            task.setStatus(UserTaskStatus.FINISHED);
            task.setFinishedOn(new Date());
            userTaskService.update(task);
        }
    }


    @Async
    @EventListener
    public void requirementFinished(UserTaskEvents.RequirementFinishedEvent event) {
        Integer requirement = event.getRequirement();
        PLMRequirementReviewer requirementReviewer = event.getRequirementReviewer();

        PLMUserTask task = userTaskService.getBySourceAndAssigned(requirement, requirementReviewer.getReviewer());
        if (task != null) {
            task.setStatus(UserTaskStatus.FINISHED);
            task.setFinishedOn(new Date());
            userTaskService.update(task);
        }
    }

    @Async
    @EventListener
    public void requirementDeleted(UserTaskEvents.RequirementDeletedEvent event) {
        Integer requirement = event.getRequirement();
        PLMRequirementReviewer requirementReviewer = event.getRequirementReviewer();

        PLMUserTask task = userTaskService.getBySourceAndAssigned(requirement, requirementReviewer.getReviewer());
        if (task != null) {
            userTaskService.delete(task);
        }
    }

    @Async
    @EventListener
    public void workflowTaskAssigned(UserTaskEvents.WorkflowTaskAssignedEvent event) {
        PLMWorkflow plmWorkflow = event.getWorkflow();
        PLMWorkflowStatus plmWorkflowActivity = event.getActivity();
        Person assignedTo = event.getAssignedTo();

        PLMUserTask task = userTaskService.getBySourceAndAssigned(plmWorkflowActivity.getId(), assignedTo.getId());
        if (task == null && assignedTo != null) {
            task = new PLMUserTask();
            task.setAssignedTo(assignedTo.getId());
            task.setSource(plmWorkflowActivity.getId());
            task.setSourceType(ObjectType.valueOf("PLMWORKFLOWSTATUS"));
            task.setContext(plmWorkflow.getId());
            task.setContextType(ObjectType.valueOf("PLMWORKFLOW"));
            userTaskService.create(task);
        } else if (task != null) {
            task.setStatus(UserTaskStatus.PENDING);
            task.setFinishedOn(null);
            userTaskService.update(task);
        }
    }

    @Async
    @EventListener
    public void workflowTaskFinished(UserTaskEvents.WorkflowTaskFinishedEvent event) {
        PLMWorkflowStatus plmWorkflowActivity = event.getActivity();
        Person assignedTo = event.getAssignedTo();
        PLMUserTask task = userTaskService.getBySourceAndAssigned(plmWorkflowActivity.getId(), assignedTo.getId());
        if (task != null) {
            task.setStatus(UserTaskStatus.FINISHED);
            task.setFinishedOn(new Date());
            userTaskService.update(task);
        }
    }

    @Async
    @EventListener
    public void workflowTaskDeleted(UserTaskEvents.WorkflowTaskDeletedEvent event) {
        PLMWorkflowStatus plmWorkflowActivity = event.getActivity();
        Person assignedTo = event.getAssignedTo();
        PLMUserTask task = userTaskService.getBySourceAndAssigned(plmWorkflowActivity.getId(), assignedTo.getId());
        if (task != null) {
            userTaskService.delete(task);
        }
    }

    private String getMobileNotificationMsg(PLMUserTask userTask, String name) {
        String message = "";
        if (userTask != null)

            if (userTask.getSourceType().equals(ObjectType.valueOf(PLMObjectType.REQUIREMENT.toString()))) {
                message = "CassiniPLM " + name + " Requirement Notification - Pending Approval." +
                        " Requirement requires your approval. Please review and take action.";
            } else if (userTask.getSourceType().equals(ObjectType.valueOf(PLMObjectType.PROJECTTASK.toString()))) {
                message = "CassiniPLM " + name + " Project Task Notification." + " Task assigned to you. Please review and take action";
            } else if (userTask.getSourceType().equals(ObjectType.valueOf(PLMObjectType.PROJECTACTIVITY.toString()))) {
                message = "CassiniPLM " + name + " Project Activity Notification." + " Activity assigned to you. Please review and take action";
            }

        return message;
    }
}
