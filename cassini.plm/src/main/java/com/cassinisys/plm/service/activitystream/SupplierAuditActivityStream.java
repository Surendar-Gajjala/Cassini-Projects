package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.SupplierAuditEvents;
import com.cassinisys.plm.model.mfr.PLMSupplier;
import com.cassinisys.plm.model.plm.DocumentApprovalStatus;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.pqm.PQMSupplierAuditAttributeRepository;
import com.cassinisys.plm.repo.pqm.QualityTypeAttributeRepository;
import com.cassinisys.plm.repo.pqm.SupplierAuditRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.activitystream.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class SupplierAuditActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommonActivityStream commonActivityStream;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private SupplierAuditRepository supplierAuditRepository;
    @Autowired
    private PQMSupplierAuditAttributeRepository supplierAuditAttributeRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private PersonRepository personRepository;

    @Async
    @EventListener
    public void supplierAuditCreated(SupplierAuditEvents.SupplierAuditCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit().getId());
        object.setType("supplierAudit");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportBasicInfoUpdated(SupplierAuditEvents.SupplierAuditBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMSupplierAudit oldSupplierAudit = event.getOldSupplierAudit();
        PQMSupplierAudit supplierAudit = event.getNewSupplierAudit();

        object.setObject(supplierAudit.getId());
        object.setType("supplierAudit");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getSupplierAuditBasicInfoUpdatedJson(oldSupplierAudit, supplierAudit));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void supplierAuditAttributesUpdated(SupplierAuditEvents.SupplieAuditAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMSupplierAudit supplierAudit = event.getSupplierAudit();

        PQMSupplierAuditAttribute oldAtt = event.getOldAttribute();
        PQMSupplierAuditAttribute newAtt = event.getNewAttribute();

        object.setObject(supplierAudit.getId());
        object.setType("supplierAudit");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getSupplierAuditAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    @Async
    @EventListener
    public void supplierAuditPlansAdded(SupplierAuditEvents.SupplierAuditPlansAddedEvent event) {
        List<PQMSupplierAuditPlan> supplierAuditPlans = event.getAuditPlans();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.plan.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit());
        object.setType("supplierAudit");
        as.setObject(object);
        as.setData(getSupplierAuditPlansAddedJson(supplierAuditPlans));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditPlanUpdated(SupplierAuditEvents.SupplierAuditPlanUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMSupplierAuditPlan oldSupplierAuditPlan = event.getOldAuditPlan();
        PQMSupplierAuditPlan supplierAuditPlan = event.getAuditPlan();

        object.setObject(event.getSupplierAudit());
        object.setType("supplierAudit");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.plan.update");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getSupplierAuditPlanUpdatedJson(oldSupplierAuditPlan, supplierAuditPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void supplierAuditPlanDeleted(SupplierAuditEvents.SupplierAuditPlanDeletedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        object.setObject(event.getSupplierAudit());
        object.setType("supplierAudit");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.plan.delete");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getSupplierAuditPlanDeletedJson(event.getAuditPlan()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void supplierAuditFilesAdded(SupplierAuditEvents.SupplierAuditFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit());
        object.setType("supplierAudit");
        as.setObject(object);
        as.setData(getSupplierAuditFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditFoldersAdded(SupplierAuditEvents.SupplierAuditFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit());
        object.setType("supplierAudit");
        as.setObject(object);
        as.setData(getSupplierAuditFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditFoldersDeleted(SupplierAuditEvents.SupplierAuditFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit());
        object.setType("supplierAudit");
        as.setObject(object);
        as.setData(getSupplierAuditFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditFileDeleted(SupplierAuditEvents.SupplierAuditFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit());
        object.setType("supplierAudit");
        as.setObject(object);
        as.setData(getSupplierAuditFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditFilesVersioned(SupplierAuditEvents.SupplierAuditFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit());
        object.setType("supplierAudit");
        as.setObject(object);
        as.setData(getSupplierAuditFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditFileRenamed(SupplierAuditEvents.SupplierAuditFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.quality.supplierAudit.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.quality.supplierAudit.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit());
        object.setType("supplierAudit");
        as.setObject(object);

        ASFileReplaceDto asFileReplaceDto = new ASFileReplaceDto(oldFile.getId(), oldFile.getName(), newFile.getId(), newFile.getName());
        try {
            as.setData(objectMapper.writeValueAsString(asFileReplaceDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditFileLocked(SupplierAuditEvents.SupplierAuditFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit());
        object.setType("supplierAudit");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getFile().getId(), event.getFile().getName(), FileUtils.byteCountToDisplaySize(event.getFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditFileUnlocked(SupplierAuditEvents.SupplierAuditFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit());
        object.setType("supplierAudit");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getFile().getId(), event.getFile().getName(), FileUtils.byteCountToDisplaySize(event.getFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditFileDownloaded(SupplierAuditEvents.SupplierAuditFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit());
        object.setType("supplierAudit");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getFile().getId(), event.getFile().getName(), FileUtils.byteCountToDisplaySize(event.getFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditWorkflowStarted(SupplierAuditEvents.SupplierAuditWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit().getId());
        object.setType("supplierAudit");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditWorkflowFinished(SupplierAuditEvents.SupplierAuditWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit().getId());
        object.setType("supplierAudit");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditWorkflowPromoted(SupplierAuditEvents.SupplierAuditWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit().getId());
        object.setType("supplierAudit");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromActivity().getId());
        source.setType("workflowactivity");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToActivity().getId());
        target.setType("workflowactivity");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditWorkflowDemoted(SupplierAuditEvents.SupplierAuditWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit().getId());
        object.setType("supplierAudit");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromActivity().getId());
        source.setType("workflowactivity");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToActivity().getId());
        target.setType("workflowactivity");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditWorkflowHold(SupplierAuditEvents.SupplierAuditWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit().getId());
        object.setType("supplierAudit");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getWorkflowActivity().getId());
        source.setType("workflowstatus");
        as.setSource(source);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void supplierAuditWorkflowUnhold(SupplierAuditEvents.SupplierAuditWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.supplierAudit.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit().getId());
        object.setType("supplierAudit");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getWorkflowActivity().getId());
        source.setType("workflowstatus");
        as.setSource(source);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentReviewerAdded(SupplierAuditEvents.SupplierAuditPlanReviewerAddedEvent event) {
        ActivityStream as = new ActivityStream();
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAuditPlan().getSupplierAudit());
        as.setObject(object);
        as.setActivity("plm.quality.supplierAudit.plan.reviewer.add");
        as.setConverter(getConverterKey());
        object.setType("supplierAudit");

        as.setData(getReqReviewerAddedJson(event.getAuditPlan(), event.getReviewer()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementDocumentReviewerUpdate(SupplierAuditEvents.SupplierAuditPlanReviewerUpdateEvent event) {
        ActivityStream as = new ActivityStream();
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAuditPlan().getSupplierAudit());
        as.setObject(object);
        as.setActivity("plm.quality.supplierAudit.plan.reviewer.change");
        as.setConverter(getConverterKey());
        object.setType("supplierAudit");

        as.setData(getReqReviewerUpdateJson(event.getAuditPlan(), event.getReviewer()));

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void requirementDocumentReviewerDeleted(SupplierAuditEvents.SupplierAuditPlanReviewerDeletedEvent event) {
        ActivityStream as = new ActivityStream();

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAuditPlan().getSupplierAudit());
        as.setObject(object);
        as.setActivity("plm.quality.supplierAudit.plan.reviewer.delete");
        as.setConverter(getConverterKey());
        object.setType("documents");
        PLMSupplier supplier = supplierRepository.findOne(event.getAuditPlan().getSupplier());
        Person person = personRepository.findOne(event.getReviewer().getReviewer());
        try {
            as.setData(objectMapper.writeValueAsString(new ASNewReviewerDTO(event.getReviewer().getId(), person.getFullName(), "reviewer", supplier.getName())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void requirementReviewerApproved(SupplierAuditEvents.SupplierAuditPlanReviewerSubmittedEvent event) {
        ActivityStream as = new ActivityStream();

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getAuditPlan().getSupplierAudit());
        as.setObject(object);
        PLMSupplier supplier = supplierRepository.findOne(event.getAuditPlan().getSupplier());
        as.setActivity("plm.quality.supplierAudit.plan.reviewer.approved");
        as.setConverter(getConverterKey());
        object.setType("supplierAudit");
        Person person = personRepository.findOne(event.getReviewer().getReviewer());
        String type = null;
        if (event.getReviewer().getStatus().equals(DocumentApprovalStatus.APPROVED)) {
            type = "approved";
        } else if (event.getReviewer().getStatus().equals(DocumentApprovalStatus.REJECTED)) {
            type = "rejected";
        } else if (event.getReviewer().getStatus().equals(DocumentApprovalStatus.REVIEWED)) {
            type = "reviewed";
        }
        try {
            as.setData(objectMapper.writeValueAsString(new ASNewReviewerDTO(event.getReviewer().getId(), person.getFullName(), type, supplier.getName())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void supplierAuditWorkflowChange(SupplierAuditEvents.SupplierAuditWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getSupplierAudit().getId());
        object.setType("supplierAudit");
        as.setObject(object);

        String oldWorkflowName = "";
        if (event.getOldWorkflow() != null) {
            String revision = "";
            if (event.getOldWorkflow().getWorkflowRevision() != null) {
                PLMWorkflowDefinition workflowDefinition = workflowDefinitionRepository.findOne(event.getOldWorkflow().getWorkflowRevision());
                if (workflowDefinition != null) {
                    revision = " ( " + workflowDefinition.getRevision() + " )";
                }
            }
            oldWorkflowName = event.getOldWorkflow().getName() + "" + revision;
            as.setActivity("plm.quality.supplierAudit.workflow.change");
        } else {
            as.setActivity("plm.quality.supplierAudit.workflow.add");
        }
        String newWorkflowName = "";
        String revision = "";
        if (event.getWorkflow().getWorkflowRevision() != null) {
            PLMWorkflowDefinition workflowDefinition = workflowDefinitionRepository.findOne(event.getWorkflow().getWorkflowRevision());
            if (workflowDefinition != null) {
                revision = " ( " + workflowDefinition.getRevision() + " )";
            }
        }
        newWorkflowName = event.getWorkflow().getName() + "" + revision;

        ASPropertyChangeDTO asPropertyChangeDTO = new ASPropertyChangeDTO("Workflow", oldWorkflowName, newWorkflowName);

        try {
            as.setData(objectMapper.writeValueAsString(asPropertyChangeDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    private String getReqReviewerAddedJson(PQMSupplierAuditPlan auditPlan, PQMSupplierAuditReviewer reviewer) {
        List<ASNewReviewerDTO> asNewReviewerDTOs = new ArrayList<>();
        PLMSupplier supplier = supplierRepository.findOne(auditPlan.getSupplier());
        String type = "reviewer";
        Person person = personRepository.findOne(reviewer.getReviewer());
        ASNewReviewerDTO asNewReviewerDTO = new ASNewReviewerDTO(person.getId(), person.getFullName(), type, supplier.getName());
        asNewReviewerDTOs.add(asNewReviewerDTO);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewReviewerDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getReqReviewerUpdateJson(PQMSupplierAuditPlan auditPlan, PQMSupplierAuditReviewer reviewer) {
        List<ASNewReviewerDTO> asNewReviewerDTOs = new ArrayList<>();
        PLMSupplier supplier = supplierRepository.findOne(auditPlan.getSupplier());
        String type = null;
        if (!reviewer.getApprover()) {
            type = "reviewer";
        } else {
            type = "approver";
        }
        Person person = personRepository.findOne(reviewer.getReviewer());
        ASNewReviewerDTO asNewReviewerDTO = new ASNewReviewerDTO(person.getId(), person.getFullName(), type, supplier.getName());
        asNewReviewerDTOs.add(asNewReviewerDTO);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewReviewerDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public String getConverterKey() {
        return "plm.quality.supplierAudit";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(object.getObject());

            if (supplierAudit == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.quality.supplierAudit.create":
                    convertedString = getSupplierAuditCreatedString(messageString, actor, supplierAudit);
                    break;
                case "plm.quality.supplierAudit.update.basicinfo":
                    convertedString = getSupplierAuditBasicInfoUpdatedString(messageString, actor, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.update.attributes":
                    convertedString = getSupplierAuditAttributeUpdatedString(messageString, actor, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.plan.add":
                    convertedString = getSupplierAuditPlansAddedString(messageString, actor, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.plan.update":
                    convertedString = getSupplierAuditPlansUpdatedString(messageString, actor, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.plan.delete":
                    convertedString = getSupplierAuditPlanDeletedString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.files.add":
                    convertedString = getSupplierAuditFilesAddedString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.files.delete":
                    convertedString = getSupplierAuditFileDeletedString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.files.folders.add":
                    convertedString = getSupplierAuditFoldersAddedString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.files.folders.delete":
                    convertedString = getSupplierAuditFoldersDeletedString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.files.version":
                    convertedString = getSupplierAuditFilesVersionedString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.files.rename":
                    convertedString = getSupplierAuditFileRenamedString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.files.replace":
                    convertedString = getSupplierAuditFileRenamedString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.files.lock":
                    convertedString = getSupplierAuditFileString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.files.unlock":
                    convertedString = getSupplierAuditFileString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.files.download":
                    convertedString = getSupplierAuditFileString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.workflow.start":
                    convertedString = getSupplierAuditWorkflowStartString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.workflow.finish":
                    convertedString = getSupplierAuditWorkflowFinishString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.workflow.promote":
                    convertedString = getSupplierAuditWorkflowPromoteDemoteString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.workflow.demote":
                    convertedString = getSupplierAuditWorkflowPromoteDemoteString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.workflow.hold":
                    convertedString = getSupplierAuditWorkflowHoldUnholdString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.workflow.unhold":
                    convertedString = getSupplierAuditWorkflowHoldUnholdString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.workflow.add":
                    convertedString = getSupplierAuditWorkflowAddedString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.workflow.change":
                    convertedString = getSupplierAuditWorkflowChangeString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.plan.reviewer.add":
                    convertedString = getPlanReviewerAddedString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.plan.reviewer.change":
                    convertedString = getPlanReviewerUpdateString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.plan.reviewer.delete":
                    convertedString = getPlanReviewerDeletedString(messageString, supplierAudit, as);
                    break;
                case "plm.quality.supplierAudit.plan.reviewer.approved":
                    convertedString = getPlanReviewerApprovedString(messageString, supplierAudit, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getSupplierAuditCreatedString(String messageString, Person actor, PQMSupplierAudit supplierAudit) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), supplierAudit.getNumber());
    }


    private String getSupplierAuditBasicInfoUpdatedString(String messageString, Person actor, PQMSupplierAudit supplierAudit, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), supplierAudit.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.supplierAudit.update.basicinfo.property");

        String json = as.getData();
        try {
            List<ASPropertyChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASPropertyChangeDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(p.getProperty()),
                        highlightValue(p.getOldValue()),
                        highlightValue(p.getNewValue())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getSupplierAuditPlansAddedString(String messageString, Person actor, PQMSupplierAudit supplierAudit, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), supplierAudit.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.supplierAudit.plan.add.property");

        String json = as.getData();
        try {
            List<ASNewAuditPlanDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASNewAuditPlanDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(p.getSupplierName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getSupplierAuditPlansUpdatedString(String messageString, Person actor, PQMSupplierAudit supplierAudit, ActivityStream as) {
        String updateString = activityStreamResourceBundle.getString("plm.quality.supplierAudit.plan.update.property");
        StringBuffer sb = new StringBuffer();
        String json = as.getData();
        try {
            List<ASAuditPlanChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASAuditPlanChangeDTO>>() {
            });
            String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(propChanges.get(0).getSupplierName()), highlightValue(supplierAudit.getNumber()));

            sb.append(activityString);
            propChanges.forEach(p -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(p.getProperty()),
                        highlightValue(p.getOldValue()),
                        highlightValue(p.getNewValue())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    private String getSupplierAuditBasicInfoUpdatedJson(PQMSupplierAudit oldPpap, PQMSupplierAudit supplierAudit) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldPpap.getName();
        String newValue = supplierAudit.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldPpap.getDescription();
        newValue = supplierAudit.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        Integer oldValue1 = oldPpap.getAssignedTo();
        Integer newValue1 = supplierAudit.getAssignedTo();

        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Assigned To", personRepository.findOne(oldValue1).getFullName(), personRepository.findOne(newValue1).getFullName()));
        }

        oldValue = oldPpap.getPlannedYear();
        newValue = supplierAudit.getPlannedYear();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Planned Year", oldValue, newValue));
        }

        String json = null;
        try {
            if (changes.size() > 0) {
                json = objectMapper.writeValueAsString(changes);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierAuditPlanUpdatedJson(PQMSupplierAuditPlan oldAuditPlan, PQMSupplierAuditPlan auditPlan) {
        List<ASAuditPlanChangeDTO> changes = new ArrayList<>();

        Date oldDate = oldAuditPlan.getPlannedStartDate();
        Date newDate = auditPlan.getPlannedStartDate();
        PLMSupplier supplier = supplierRepository.findOne(oldAuditPlan.getSupplier());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (oldDate == null && newDate != null) {
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASAuditPlanChangeDTO("Planned Date", "", newDateValue, supplier.getName()));
        } else if (oldDate != null && newDate != null) {
            if (!oldDate.equals(newDate)) {
                String oldDateValue = dateFormat.format(oldDate);
                String newDateValue = dateFormat.format(newDate);
                changes.add(new ASAuditPlanChangeDTO("Planned Date", oldDateValue, newDateValue, supplier.getName()));
            }
        } else if (oldDate != null) {
            String oldDateValue = dateFormat.format(oldDate);
            changes.add(new ASAuditPlanChangeDTO("Planned Date", oldDateValue, "", supplier.getName()));
        }


        oldDate = oldAuditPlan.getFinishedDate();
        newDate = auditPlan.getFinishedDate();

        if (oldDate == null && newDate != null) {
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASAuditPlanChangeDTO("Completed Date", "", newDateValue, supplier.getName()));
        } else if (oldDate != null && newDate != null) {
            if (!oldDate.equals(newDate)) {
                String oldDateValue = dateFormat.format(oldDate);
                String newDateValue = dateFormat.format(newDate);
                changes.add(new ASAuditPlanChangeDTO("Completed Date", oldDateValue, newDateValue, supplier.getName()));
            }
        } else if (oldDate != null) {
            String oldDateValue = dateFormat.format(oldDate);
            changes.add(new ASAuditPlanChangeDTO("Completed Date", oldDateValue, "", supplier.getName()));
        }

        String oldValue = oldAuditPlan.getStatus().name();
        String newValue = auditPlan.getStatus().name();

        if (!oldValue.equals(newValue)) {
            changes.add(new ASAuditPlanChangeDTO("Status", oldValue, newValue, supplier.getName()));
        }

        String json = null;
        try {
            if (changes.size() > 0) {
                json = objectMapper.writeValueAsString(changes);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getSupplierAuditAttributesUpdatedJson(PQMSupplierAuditAttribute oldAttribute, PQMSupplierAuditAttribute newAttribute) {
        PQMQualityTypeAttribute attDef = qualityTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
        List<ASAttributeChangeDTO> changes = commonActivityStream.getAttributeUpdateJsonData(oldAttribute, newAttribute, attDef);

        String json = null;
        try {
            if (changes.size() > 0) {
                json = objectMapper.writeValueAsString(changes);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getSupplierAuditAttributeUpdatedString(String messageString, Person actor, PQMSupplierAudit supplierAudit, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), supplierAudit.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.supplierAudit.update.attributes.attribute");

        String json = as.getData();
        try {
            List<ASAttributeChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASAttributeChangeDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = null;
                s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(p.getAttribute()),
                        highlightValue(p.getOldValue()),
                        highlightValue(p.getNewValue())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getSupplierAuditFilesAddedJson(List<PLMFile> files) {
        String json = null;

        List<ASNewFileDTO> ASNewFileDtos = new ArrayList<>();
        files.forEach(f -> ASNewFileDtos.add(new ASNewFileDTO(f.getId(), f.getName(), FileUtils.byteCountToDisplaySize(f.getSize()))));

        try {
            json = objectMapper.writeValueAsString(ASNewFileDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierAuditPlansAddedJson(List<PQMSupplierAuditPlan> supplierAuditPlans) {
        String json = null;

        List<ASNewAuditPlanDTO> auditPlanDTOs = new ArrayList<>();
        supplierAuditPlans.forEach(pqmSupplierAuditPlan -> {
            PLMSupplier supplier = supplierRepository.findOne(pqmSupplierAuditPlan.getSupplier());
            auditPlanDTOs.add(new ASNewAuditPlanDTO(supplier.getId(), supplier.getName()));
        });

        try {
            json = objectMapper.writeValueAsString(auditPlanDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierAuditFoldersAddedJson(PLMFile file) {
        String json = null;

        List<ASNewFileDTO> ASNewFileDtos = new ArrayList<>();
        ASNewFileDtos.add(new ASNewFileDTO(file.getId(), file.getName(), FileUtils.byteCountToDisplaySize(file.getSize())));
        try {
            json = objectMapper.writeValueAsString(ASNewFileDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierAuditFoldersDeletedJson(PLMFile file) {
        String json = null;

        List<ASNewFileDTO> ASNewFileDtos = new ArrayList<>();
        ASNewFileDtos.add(new ASNewFileDTO(file.getId(), file.getName(), FileUtils.byteCountToDisplaySize(file.getSize())));
        try {
            json = objectMapper.writeValueAsString(ASNewFileDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierAuditPlanDeletedJson(PQMSupplierAuditPlan auditPlan) {
        String json = null;

        List<ASNewAuditPlanDTO> auditPlanDTOs = new ArrayList<>();
        PLMSupplier supplier = supplierRepository.findOne(auditPlan.getSupplier());
        auditPlanDTOs.add(new ASNewAuditPlanDTO(supplier.getId(), supplier.getName()));
        try {
            json = objectMapper.writeValueAsString(auditPlanDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierAuditFilesAddedString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), supplierAudit.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.supplierAudit.files.add.file");

        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getName()), f.getSize()));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getSupplierAuditFoldersAddedString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), supplierAudit.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getSupplierAuditFoldersDeletedString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), supplierAudit.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getSupplierAuditFileDeletedString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), supplierAudit.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getSupplierAuditPlanDeletedString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewAuditPlanDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewAuditPlanDTO>>() {
            });
            ASNewAuditPlanDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getSupplierName()), supplierAudit.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getSupplierAuditFilesVersionedJson(List<PLMFile> files) {
        String json = null;

        List<ASVersionedFileDTO> ASVersionedFileDtos = new ArrayList<>();
        files.forEach(f -> ASVersionedFileDtos.add(new ASVersionedFileDTO(f.getId(), f.getName(), f.getVersion() - 1, f.getVersion())));

        try {
            json = objectMapper.writeValueAsString(ASVersionedFileDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getSupplierAuditFilesVersionedString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), supplierAudit.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.supplierAudit.files.version.file");

        String json = as.getData();
        try {
            List<ASVersionedFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASVersionedFileDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString,
                        highlightValue(f.getName()),
                        highlightValue("" + f.getOldVersion()),
                        highlightValue("" + f.getNewVersion())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getSupplierAuditFileRenamedString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    supplierAudit.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getSupplierAuditFileString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    supplierAudit.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getSupplierAuditWorkflowStartString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                supplierAudit.getNumber());
    }

    private String getSupplierAuditWorkflowFinishString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                supplierAudit.getNumber());
    }

    private String getSupplierAuditWorkflowPromoteDemoteString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                supplierAudit.getNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getSupplierAuditWorkflowHoldUnholdString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                supplierAudit.getNumber());
    }

    private String getSupplierAuditWorkflowAddedString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    supplierAudit.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getSupplierAuditWorkflowChangeString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    supplierAudit.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getPlanReviewerAddedString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {


        StringBuffer sb = new StringBuffer();

        String fileString = activityStreamResourceBundle.getString("plm.quality.supplierAudit.plan.reviewer.add.reviewer");

        String json = as.getData();
        try {
            List<ASNewReviewerDTO> reviewerDTOs = objectMapper.readValue(json, new TypeReference<List<ASNewReviewerDTO>>() {
            });
            String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                    highlightValue(reviewerDTOs.get(0).getName()), highlightValue(supplierAudit.getNumber()));
            sb.append(activityString);

            reviewerDTOs.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getFullName()), highlightValue(f.getType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    private String getPlanReviewerUpdateString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {

        StringBuffer sb = new StringBuffer();

        String fileString = activityStreamResourceBundle.getString("plm.quality.supplierAudit.plan.reviewer.change.reviewer");

        String json = as.getData();
        try {
            List<ASNewReviewerDTO> reviewerDTOs = objectMapper.readValue(json, new TypeReference<List<ASNewReviewerDTO>>() {
            });
            String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                    highlightValue(reviewerDTOs.get(0).getName()), highlightValue(supplierAudit.getNumber()));
            sb.append(activityString);
            reviewerDTOs.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getFullName()), highlightValue(f.getType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getPlanReviewerDeletedString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewReviewerDTO reviewer = objectMapper.readValue(json, new TypeReference<ASNewReviewerDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(reviewer.getType()),
                    highlightValue(reviewer.getFullName()), highlightValue(reviewer.getName()), highlightValue(supplierAudit.getNumber()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getPlanReviewerApprovedString(String messageString, PQMSupplierAudit supplierAudit, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewReviewerDTO reviewer = objectMapper.readValue(json, new TypeReference<ASNewReviewerDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(reviewer.getFullName()), highlightValue(reviewer.getType()),
                    highlightValue(reviewer.getName()), highlightValue(supplierAudit.getNumber()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
