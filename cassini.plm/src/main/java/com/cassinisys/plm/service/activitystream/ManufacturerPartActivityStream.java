package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.ManufacturerPartEvents;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartTypeAttributeRepository;
import com.cassinisys.plm.repo.mfr.MfrPartInspectionReportRepository;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ManufacturerPartActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private ManufacturerPartTypeAttributeRepository manufacturerPartTypeAttributeRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private CommonActivityStream commonActivityStream;
    @Autowired
    private MfrPartInspectionReportRepository mfrPartInspectionReportRepository;

    @Async
    @EventListener
    public void manufacturerPartCreated(ManufacturerPartEvents.ManufacturerPartCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerBasicInfoUpdated(ManufacturerPartEvents.ManufacturerPartBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMManufacturerPart oldPart = event.getOldPart();
        PLMManufacturerPart newPart = event.getPart();

        object.setObject(newPart.getId());
        object.setType("manufacturerPart");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getManufacturerPartBasicInfoUpdatedJson(oldPart, newPart));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void manufacturerPartAttributesUpdated(ManufacturerPartEvents.ManufacturerPartAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMManufacturerPart manufacturerPart = event.getPart();

        PLMManufacturerPartAttribute oldAtt = event.getOldAttribute();
        PLMManufacturerPartAttribute newAtt = event.getNewAttribute();

        object.setObject(manufacturerPart.getId());
        object.setType("manufacturerPart");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getManufacturerAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void manufacturerPartFilesAdded(ManufacturerPartEvents.ManufacturerPartFilesAddedEvent event) {
        List<PLMManufacturerPartFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
        as.setObject(object);
        as.setData(getManufacturerFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartFoldersAdded(ManufacturerPartEvents.ManufacturerPartFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
        as.setObject(object);
        as.setData(getManufacturerFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartFoldersDeleted(ManufacturerPartEvents.ManufacturerPartFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
        as.setObject(object);
        as.setData(getManufacturerFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartFileDeleted(ManufacturerPartEvents.ManufacturerPartFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
        as.setObject(object);
        as.setData(getManufacturerFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartFilesVersioned(ManufacturerPartEvents.ManufacturerPartFilesVersionedEvent event) {
        List<PLMManufacturerPartFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
        as.setObject(object);
        as.setData(getManufacturerFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartFileRenamed(ManufacturerPartEvents.ManufacturerPartFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.oem.manufacturerPart.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.oem.manufacturerPart.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
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
    public void manufacturerPartFileLocked(ManufacturerPartEvents.ManufacturerPartFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getPartFile().getId(), event.getPartFile().getName(), FileUtils.byteCountToDisplaySize(event.getPartFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartFileUnlocked(ManufacturerPartEvents.ManufacturerPartFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getPartFile().getId(), event.getPartFile().getName(), FileUtils.byteCountToDisplaySize(event.getPartFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartFileDownloaded(ManufacturerPartEvents.ManufacturerPartFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getPartFile().getId(), event.getPartFile().getName(), FileUtils.byteCountToDisplaySize(event.getPartFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void manufacturerPartReportAdded(ManufacturerPartEvents.ManufacturerPartReportAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.inspectionreport.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart());
        object.setType("manufacturerPart");
        as.setObject(object);
        as.setData(getManufacturerReportAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartReportFoldersAdded(ManufacturerPartEvents.ManufacturerPartReportFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.inspectionreport.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart());
        object.setType("manufacturerPart");
        as.setObject(object);
        as.setData(getManufacturerFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartReportFoldersDeleted(ManufacturerPartEvents.ManufacturerPartReportFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.inspectionreport.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart());
        object.setType("manufacturerPart");
        as.setObject(object);
        as.setData(getManufacturerFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartReportDeleted(ManufacturerPartEvents.ManufacturerPartReportDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.inspectionreport.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart());
        object.setType("manufacturerPart");
        as.setObject(object);
        as.setData(getManufacturerFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartReportVersioned(ManufacturerPartEvents.ManufacturerPartReportVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.inspectionreport.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart());
        object.setType("manufacturerPart");
        as.setObject(object);
        as.setData(getManufacturerReportVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartReportRenamed(ManufacturerPartEvents.ManufacturerPartReportRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.oem.manufacturerPart.inspectionreport.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.oem.manufacturerPart.inspectionreport.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart());
        object.setType("manufacturerPart");
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
    public void manufacturerPartReportLocked(ManufacturerPartEvents.ManufacturerPartReportLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.inspectionReport.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart());
        object.setType("manufacturerPart");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getPartFile().getId(), event.getPartFile().getName(), FileUtils.byteCountToDisplaySize(event.getPartFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartReportUnlocked(ManufacturerPartEvents.ManufacturerPartReportUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.inspectionreport.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart());
        object.setType("manufacturerPart");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getPartFile().getId(), event.getPartFile().getName(), FileUtils.byteCountToDisplaySize(event.getPartFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartReportDownloaded(ManufacturerPartEvents.ManufacturerPartReportDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.inspectionreport.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart());
        object.setType("manufacturerPart");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getPartFile().getId(), event.getPartFile().getName(), FileUtils.byteCountToDisplaySize(event.getPartFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void manufacturerPartWorkflowStarted(ManufacturerPartEvents.ManufacturerPartWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartWorkflowFinished(ManufacturerPartEvents.ManufacturerPartWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPartWorkflowPromoted(ManufacturerPartEvents.ManufacturerPartWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
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
    public void manufacturerPartWorkflowDemoted(ManufacturerPartEvents.ManufacturerPartWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
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
    public void manufacturerPartWorkflowHold(ManufacturerPartEvents.ManufacturerPartWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
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
    public void manufacturerPartWorkflowUnhold(ManufacturerPartEvents.ManufacturerPartWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
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
    public void manufacturerPartWorkflowChange(ManufacturerPartEvents.ManufacturerPartWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
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
            as.setActivity("plm.oem.manufacturerPart.workflow.change");
        } else {
            as.setActivity("plm.oem.manufacturerPart.workflow.add");
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


    @Async
    @EventListener
    public void manufacturerPartCommentAdded(ManufacturerPartEvents.ManufacturerPartCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getPart().getId());
        object.setType("manufacturerPart");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerPromoted(ManufacturerPartEvents.ManufacturerPartPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.lifeCyclePhase.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturerPart().getId());
        object.setType("manufacturerPart");
        as.setObject(object);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromLifeCyclePhase().getId());
        source.setType("manufacturerPart");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToLifeCyclePhase().getId());
        target.setType("manufacturerPart");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void manufacturerDemoted(ManufacturerPartEvents.ManufacturerPartDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.oem.manufacturerPart.lifeCyclePhase.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getManufacturerPart().getId());
        object.setType("manufacturerPart");
        as.setObject(object);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromLifeCyclePhase().getId());
        source.setType("manufacturerPart");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToLifeCyclePhase().getId());
        target.setType("manufacturerPart");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.oem.manufacturerPart";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(object.getObject());

            if (manufacturerPart == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.oem.manufacturerPart.create":
                    convertedString = getManufacturerCreatedString(messageString, actor, manufacturerPart);
                    break;
                case "plm.oem.manufacturerPart.update.basicinfo":
                    convertedString = getManufacturerBasicInfoUpdatedString(messageString, actor, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.update.attributes":
                    convertedString = getManufacturerAttributeUpdatedString(messageString, actor, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.files.add":
                    convertedString = getManufacturerFilesAddedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.files.delete":
                    convertedString = getManufacturerFileDeletedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.files.folders.add":
                    convertedString = getManufacturerFoldersAddedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.files.folders.delete":
                    convertedString = getManufacturerFoldersDeletedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.files.version":
                    convertedString = getManufacturerFilesVersionedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.files.rename":
                    convertedString = getManufacturerFileRenamedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.files.replace":
                    convertedString = getManufacturerFileRenamedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.files.lock":
                    convertedString = getManufacturerFileString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.files.unlock":
                    convertedString = getManufacturerFileString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.add":
                    convertedString = getManufacturerReportAddedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.delete":
                    convertedString = getManufacturerFileDeletedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.folders.add":
                    convertedString = getManufacturerFoldersAddedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.folders.delete":
                    convertedString = getManufacturerFoldersDeletedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.version":
                    convertedString = getManufacturerReportVersionedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.rename":
                    convertedString = getManufacturerFileRenamedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.replace":
                    convertedString = getManufacturerFileRenamedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.lock":
                    convertedString = getManufacturerFileString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.unlock":
                    convertedString = getManufacturerFileString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.download":
                    convertedString = getManufacturerFileString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.promote":
                    convertedString = getManufacturerPartReportPromoteDemoteString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.demote":
                    convertedString = getManufacturerPartReportPromoteDemoteString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.revised":
                    convertedString = getDocumentFileRenamedString(messageString, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.reviewer.add":
                    convertedString = getReqReviewerAddedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.reviewer.change":
                    convertedString = getReqReviewerUpdateString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.reviewer.delete":
                    convertedString = getReqReviewerDeletedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.inspectionreport.reviewer.approved":
                    convertedString = getReqReviewerApprovedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.files.download":
                    convertedString = getManufacturerFileString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.workflow.start":
                    convertedString = getManufacturerWorkflowStartString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.workflow.finish":
                    convertedString = getManufacturerWorkflowFinishString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.workflow.promote":
                    convertedString = getManufacturerWorkflowPromoteDemoteString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.workflow.demote":
                    convertedString = getManufacturerWorkflowPromoteDemoteString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.workflow.hold":
                    convertedString = getManufacturerWorkflowHoldUnholdString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.workflow.unhold":
                    convertedString = getManufacturerWorkflowHoldUnholdString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.workflow.add":
                    convertedString = getManufacturerWorkflowAddedString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.workflow.change":
                    convertedString = getManufacturerWorkflowChangeString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.comment":
                    convertedString = getManufacturerCommentString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.lifeCyclePhase.promote":
                    convertedString = getManufacturerPartPromoteDemoteString(messageString, manufacturerPart, as);
                    break;
                case "plm.oem.manufacturerPart.lifeCyclePhase.demote":
                    convertedString = getManufacturerPartPromoteDemoteString(messageString, manufacturerPart, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getManufacturerCreatedString(String messageString, Person actor, PLMManufacturerPart manufacturerPart) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), manufacturerPart.getPartNumber());
    }

    private String getManufacturerBasicInfoUpdatedString(String messageString, Person actor, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), manufacturerPart.getPartNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.oem.manufacturerPart.update.basicinfo.property");

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

    private String getManufacturerAttributesUpdatedJson(PLMManufacturerPartAttribute oldAttribute, PLMManufacturerPartAttribute newAttribute) {
        PLMManufacturerPartTypeAttribute attDef = manufacturerPartTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
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

    private String getManufacturerAttributeUpdatedString(String messageString, Person actor, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), manufacturerPart.getPartNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.oem.manufacturerPart.update.attributes.attribute");
        String propertyUpdateString = activityStreamResourceBundle.getString("plm.oem.manufacturerPart.update.attributes.attribute").substring(0, 21);
        String json = as.getData();
        try {
            List<ASAttributeChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASAttributeChangeDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = null;
                if (p.getAttribute().equals("MULTIPLELIST") || p.getAttribute().equals("OBJECT") || p.getAttribute().equals("IMAGE") || p.getAttribute().equals("ATTACHMENT")) {
                    s = addMarginToMessage(MessageFormat.format(propertyUpdateString, highlightValue(p.getAttribute().toLowerCase())));
                    sb.append(s);
                } else {
                    s = addMarginToMessage(MessageFormat.format(updateString,
                            highlightValue(p.getAttribute()),
                            highlightValue(p.getOldValue()),
                            highlightValue(p.getNewValue())));
                    sb.append(s);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public String getManufacturerFilesAddedJson(List<PLMManufacturerPartFile> files) {
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

    public String getManufacturerReportAddedJson(List<PLMFile> files) {
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

    private String getManufacturerFoldersAddedJson(PLMFile file) {
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

    private String getManufacturerFoldersDeletedJson(PLMFile file) {
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

    private String getManufacturerFilesAddedString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), manufacturerPart.getPartNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.oem.manufacturerPart.files.add.file");

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

    private String getManufacturerReportAddedString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), manufacturerPart.getPartNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.oem.manufacturerPart.inspectionreport.add.file");

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

    private String getManufacturerFoldersAddedString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), manufacturerPart.getPartNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getManufacturerFoldersDeletedString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), manufacturerPart.getPartNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getManufacturerFileDeletedString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), manufacturerPart.getPartNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    public String getManufacturerFilesVersionedJson(List<PLMManufacturerPartFile> files) {
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

    public String getManufacturerReportVersionedJson(List<PLMFile> files) {
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

    private String getManufacturerFilesVersionedString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), manufacturerPart.getPartNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.oem.manufacturerPart.files.version.file");

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

    private String getManufacturerReportVersionedString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), manufacturerPart.getPartNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.oem.manufacturerPart.inspectionreport.version.file");

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

    private String getManufacturerFileRenamedString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    manufacturerPart.getPartNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getManufacturerFileString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    manufacturerPart.getPartNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getManufacturerWorkflowStartString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                manufacturerPart.getPartNumber());
    }

    private String getManufacturerWorkflowFinishString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                manufacturerPart.getPartNumber());
    }

    private String getManufacturerWorkflowPromoteDemoteString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                manufacturerPart.getPartNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getManufacturerWorkflowHoldUnholdString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                manufacturerPart.getPartNumber());
    }

    private String getManufacturerWorkflowAddedString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    manufacturerPart.getPartNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getManufacturerWorkflowChangeString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    manufacturerPart.getPartNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getManufacturerCommentString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                manufacturerPart.getPartNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    public String getManufacturerPartBasicInfoUpdatedJson(PLMManufacturerPart oldpart, PLMManufacturerPart part) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldpart.getPartName();
        String newValue = part.getPartName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldpart.getDescription();
        newValue = part.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldpart.getPartNumber();
        newValue = part.getPartNumber();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Number", oldValue, newValue));
        }

        String json = "";
        try {
            if (changes.size() > 0) {
                json = objectMapper.writeValueAsString(changes);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getManufacturerPartPromoteDemoteString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMLifeCyclePhase fromLifeCyclePhase = lifeCyclePhaseRepository.findOne(source.getObject());
        PLMLifeCyclePhase toLifeCyclePhase = lifeCyclePhaseRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(manufacturerPart.getPartNumber()),
                highlightValue(fromLifeCyclePhase.getPhase()),
                highlightValue(toLifeCyclePhase.getPhase()));
    }

    private String getManufacturerPartReportPromoteDemoteString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();
        ASNewFileDTO asNewFileDTO = null;
        PLMMfrPartInspectionReport mfrPartInspectionReport = null;
        if (as.getData() != null && !as.getData().equals("")) {
            try {
                asNewFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        PLMLifeCyclePhase fromLifeCyclePhase = lifeCyclePhaseRepository.findOne(source.getObject());
        PLMLifeCyclePhase toLifeCyclePhase = lifeCyclePhaseRepository.findOne(target.getObject());
        if (asNewFileDTO != null) {
            mfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(asNewFileDTO.getId());
        }
        if (mfrPartInspectionReport != null) {
            return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(mfrPartInspectionReport.getName()),
                    highlightValue(mfrPartInspectionReport.getRevision()),
                    highlightValue(mfrPartInspectionReport.getVersion().toString()),
                    highlightValue(fromLifeCyclePhase.getPhase()),
                    highlightValue(toLifeCyclePhase.getPhase()));
        } else {
            return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    "", "", "",
                    highlightValue(fromLifeCyclePhase.getPhase()),
                    highlightValue(toLifeCyclePhase.getPhase()));
        }
    }


    private String getDocumentFileRenamedString(String messageString, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            PLMMfrPartInspectionReport oldDocument = mfrPartInspectionReportRepository.findOne(asFileReplaceDto.getOldFileId());
            PLMMfrPartInspectionReport newDocument = mfrPartInspectionReportRepository.findOne(asFileReplaceDto.getNewFileId());
            if (oldDocument != null && newDocument != null) {
                messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                        highlightValue(asFileReplaceDto.getOldFileName() + " - " + oldDocument.getRevision() + "." + oldDocument.getVersion()),
                        highlightValue(asFileReplaceDto.getNewFileName() + " - " + newDocument.getRevision() + "." + newDocument.getVersion()));
            } else {
                messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                        highlightValue(asFileReplaceDto.getOldFileName()),
                        highlightValue(asFileReplaceDto.getNewFileName()));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }


    private String getReqReviewerAddedString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {

        StringBuffer sb = new StringBuffer();
        String fileString = activityStreamResourceBundle.getString("plm.oem.manufacturerPart.inspectionreport.reviewer.add.reviewer");

        String json = as.getData();
        try {
            List<ASNewReviewerDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewReviewerDTO>>() {
            });
            String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                    highlightValue(files.get(0).getName()), highlightValue(manufacturerPart.getPartNumber()));
            sb.append(activityString);
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getFullName()), highlightValue(f.getType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    private String getReqReviewerUpdateString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {
        StringBuffer sb = new StringBuffer();

        String fileString = activityStreamResourceBundle.getString("plm.oem.manufacturerPart.inspectionreport.reviewer.change.reviewer");

        String json = as.getData();
        try {
            List<ASNewReviewerDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewReviewerDTO>>() {
            });
            String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()),
                    highlightValue(files.get(0).getName()), highlightValue(manufacturerPart.getPartNumber()));
            sb.append(activityString);
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getFullName()), highlightValue(f.getType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getReqReviewerDeletedString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewReviewerDTO reviewer = objectMapper.readValue(json, new TypeReference<ASNewReviewerDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(reviewer.getType()),
                    highlightValue(reviewer.getFullName()), highlightValue(reviewer.getName()), highlightValue(manufacturerPart.getPartNumber()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getReqReviewerApprovedString(String messageString, PLMManufacturerPart manufacturerPart, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewReviewerDTO reviewer = objectMapper.readValue(json, new TypeReference<ASNewReviewerDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(reviewer.getFullName()), highlightValue(reviewer.getType()),
                    highlightValue(reviewer.getName()), highlightValue(manufacturerPart.getPartNumber()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

}
