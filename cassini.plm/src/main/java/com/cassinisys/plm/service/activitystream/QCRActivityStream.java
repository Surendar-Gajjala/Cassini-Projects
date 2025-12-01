package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.QCREvents;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pqm.QCRRepository;
import com.cassinisys.plm.repo.pqm.QualityTypeAttributeRepository;
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
public class QCRActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private QCRRepository qcrRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommonActivityStream commonActivityStream;
    @Autowired
    private PersonRepository personRepository;

    @Async
    @EventListener
    public void qcrCreated(QCREvents.QCRCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void problemReportBasicInfoUpdated(QCREvents.QCRBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMQCR oldQcr = event.getOldQcr();
        PQMQCR qcr = event.getQcr();

        object.setObject(qcr.getId());
        object.setType("qcr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getQcrBasicInfoUpdatedJson(oldQcr, qcr));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void qcrAttributesUpdated(QCREvents.QCRAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PQMQCR qcr = event.getQcr();

        PQMQCRAttribute oldAtt = event.getOldAttribute();
        PQMQCRAttribute newAtt = event.getNewAttribute();

        object.setObject(qcr.getId());
        object.setType("qcr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getQcrAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void qcrFilesAdded(QCREvents.QCRFilesAddedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQcrFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrFoldersAdded(QCREvents.QCRFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQcrFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrFoldersDeleted(QCREvents.QCRFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQcrFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrFileDeleted(QCREvents.QCRFileDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQcrFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrFilesVersioned(QCREvents.QCRFilesVersionedEvent event) {
        List<PLMFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQcrFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrFileRenamed(QCREvents.QCRFileRenamedEvent event) {
        PLMFile oldFile = event.getOldFile();
        PLMFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.quality.qcr.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.quality.qcr.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
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
    public void qcrFileLocked(QCREvents.QCRFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
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
    public void qcrFileUnlocked(QCREvents.QCRFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
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
    public void qcrFileDownloaded(QCREvents.QCRFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
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
    public void qcrWorkflowStarted(QCREvents.QCRWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrWorkflowFinished(QCREvents.QCRWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrWorkflowPromoted(QCREvents.QCRWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
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
    public void qcrWorkflowDemoted(QCREvents.QCRWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
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
    public void qcrWorkflowHold(QCREvents.QCRWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
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
    public void qcrWorkflowUnhold(QCREvents.QCRWorkflowUnHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
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
    public void ncrWorkflowChange(QCREvents.QCRWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
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
            as.setActivity("plm.quality.qcr.workflow.change");
        } else {
            as.setActivity("plm.quality.qcr.workflow.add");
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
    public void qcrCommentAdded(QCREvents.QCRCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrPRProblemSourcesAdded(QCREvents.QCRPrProblemSourceAddedEvent event) {
        List<PQMQCRAggregatePR> aggregatePRList = event.getAggregatePRs();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.sources.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRPRProblemSourcesAddedJson(aggregatePRList));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrNCRProblemSourcesAdded(QCREvents.QCRNCRProblemSourceAddedEvent event) {
        List<PQMQCRAggregateNCR> aggregateNCRList = event.getAggregateNCRs();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.sources.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRNCRProblemSourcesAddedJson(aggregateNCRList));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrNCRProblemSourcesDeleted(QCREvents.QCRNCRProblemSourceDeletedEvent event) {
        PQMQCRAggregateNCR aggregateNCR = event.getAggregateNCR();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.sources.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRNCRProblemSourcesDeletedJson(aggregateNCR));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrPRProblemSourcesDeleted(QCREvents.QCRPRProblemSourceDeletedEvent event) {
        PQMQCRAggregatePR aggregatePR = event.getAggregatePR();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.sources.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRPRProblemSourcesDeletedJson(aggregatePR));

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void qcrProblemItemsAdded(QCREvents.QCRProblemItemAddedEvent event) {
        List<PQMQCRProblemItem> asNewPRProblemItems = event.getProblemItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.problemItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRProblemItemsAddedJson(asNewPRProblemItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void inspectionBasicInfoUpdated(QCREvents.QCRProblemItemUpdateEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        object.setObject(event.getQcr().getId());
        object.setType("qcr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.problemItems.update");
        as.setObject(object);
        as.setConverter(getConverterKey());
        String oldValue = event.getOldProblemItem().getNotes();
        String newValue = event.getProblemItems().getNotes();
        if (oldValue == null) {
            oldValue = "";
        }

        if (newValue == null) {
            newValue = "";
        }
        List<ASProblemItemUpdate> changes = new ArrayList<>();
        if (!oldValue.equals(newValue)) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(event.getProblemItems().getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            changes.add(new ASProblemItemUpdate(item.getItemNumber(), "Notes", oldValue, newValue));
        }
        try {
            if (changes.size() > 0) {
                as.setData(objectMapper.writeValueAsString(changes));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void qcrProblemMaterialUpdated(QCREvents.QCRProblemMaterialUpdateEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        object.setObject(event.getQcr().getId());
        object.setType("qcr");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.problemItems.update");
        as.setObject(object);
        as.setConverter(getConverterKey());
        String oldValue = event.getOldProblemItem().getNotes();
        String newValue = event.getProblemItems().getNotes();
        if (oldValue == null) {
            oldValue = "";
        }

        if (newValue == null) {
            newValue = "";
        }
        List<ASProblemItemUpdate> changes = new ArrayList<>();
        if (!oldValue.equals(newValue)) {
            changes.add(new ASProblemItemUpdate(event.getProblemItems().getMaterial().getPartNumber(), "Notes", oldValue, newValue));
        }
        try {
            if (changes.size() > 0) {
                as.setData(objectMapper.writeValueAsString(changes));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void qcrProblemItemsDeleted(QCREvents.QCRProblemItemDeletedEvent event) {
        PQMQCRProblemItem asNewPRProblemItem = event.getProblemItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.problemItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRProblemItemsDeletedJson(asNewPRProblemItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrRelatedItemsAdded(QCREvents.QCRRelatedItemAddedEvent event) {
        List<PQMQCRRelatedItem> relatedItems = event.getRelatedItems();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.relatedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRRelatedItemsAddedJson(relatedItems));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrRelatedItemsDeleted(QCREvents.QCRRelatedItemDeletedEvent event) {
        PQMQCRRelatedItem relatedItem = event.getRelatedItem();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.relatedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRRelatedItemsDeletedJson(relatedItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrProblemMateralsAdded(QCREvents.QCRProblemMaterialAddedEvent event) {
        List<PQMQCRProblemMaterial> asNewQCRProblemMaterials = event.getProblemMaterials();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.problemItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRProblemMaterialsAddedJson(asNewQCRProblemMaterials));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrProblemMaterialDeleted(QCREvents.QCRProblemMaterialDeletedEvent event) {
        PQMQCRProblemMaterial asNewNCRProblemItem = event.getProblemMaterial();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.problemItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRProblemMaterialsDeletedJson(asNewNCRProblemItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrRelatedItemsAdded(QCREvents.QCRRelatedMaterialAddedEvent event) {
        List<PQMQCRRelatedMaterial> relatedMaterials = event.getRelatedMaterials();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.relatedItems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRRelatedMaterialsAddedJson(relatedMaterials));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrRelatedItemsDeleted(QCREvents.QCRRelatedMaterialDeletedEvent event) {
        PQMQCRRelatedMaterial relatedItem = event.getRelatedMaterial();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.relatedItems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRRelatedMaterialsDeletedJson(relatedItem));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrCAPAAdded(QCREvents.QCRCAPAAddedEvent event) {
        PQMQCRCAPA capa = event.getCapa();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.capa.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRCapaAddedOrDeletedJson(capa));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrCAPAAdded(QCREvents.QCRCAPAUpdatedEvent event) {
        PQMQCRCAPA capa = event.getCapa();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.capa.update");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRCapaUpdatedJson(event.getOldCapa(), event.getCapa()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void qcrCAPAAudit(QCREvents.QCRCAPAAuditEvent event) {
        PQMQCRCAPA capa = event.getCapa();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.capa.audit");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRCapaAddedOrDeletedJson(capa));
        ASCAPAAudit ascapaAudit = new ASCAPAAudit(event.getCapa().getId(), event.getCapa().getRootCauseAnalysis(),
                event.getCapa().getResult().toString(), event.getCapa().getAuditNotes());
        try {
            as.setData(objectMapper.writeValueAsString(ascapaAudit));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void qcrCAPADeleted(QCREvents.QCRCAPADeletedEvent event) {
        PQMQCRCAPA capa = event.getCapa();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.quality.qcr.capa.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getQcr().getId());
        object.setType("qcr");
        as.setObject(object);
        as.setData(getQCRCapaAddedOrDeletedJson(capa));

        activityStreamService.create(as);
    }


    @Override
    public String getConverterKey() {
        return "plm.quality.qcr";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PQMQCR qcr = qcrRepository.findOne(object.getObject());

            if (qcr == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.quality.qcr.create":
                    convertedString = getQcrCreatedString(messageString, actor, qcr);
                    break;
                case "plm.quality.qcr.revision":
                    convertedString = getQcrRevisionCreatedString(messageString, actor, qcr);
                    break;
                case "plm.quality.qcr.update.basicinfo":
                    convertedString = getQcrBasicInfoUpdatedString(messageString, actor, qcr, as);
                    break;
                case "plm.quality.qcr.update.attributes":
                    convertedString = getQcrAttributeUpdatedString(messageString, actor, qcr, as);
                    break;
                case "plm.quality.qcr.files.add":
                    convertedString = getQcrFilesAddedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.files.delete":
                    convertedString = getQcrFileDeletedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.files.folders.add":
                    convertedString = getQcrFoldersAddedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.files.folders.delete":
                    convertedString = getQcrFoldersDeletedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.files.version":
                    convertedString = getQcrFilesVersionedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.files.rename":
                    convertedString = getQcrFileRenamedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.files.replace":
                    convertedString = getQcrFileRenamedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.files.lock":
                    convertedString = getQcrFileString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.files.unlock":
                    convertedString = getQcrFileString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.files.download":
                    convertedString = getQcrFileString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.workflow.start":
                    convertedString = getQcrWorkflowStartString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.workflow.finish":
                    convertedString = getQcrWorkflowFinishString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.workflow.promote":
                    convertedString = getQcrWorkflowPromoteDemoteString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.workflow.demote":
                    convertedString = getQcrWorkflowPromoteDemoteString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.workflow.hold":
                    convertedString = getQcrWorkflowHoldUnholdString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.workflow.unhold":
                    convertedString = getQcrWorkflowHoldUnholdString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.workflow.add":
                    convertedString = getQCRWorkflowAddedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.workflow.change":
                    convertedString = getQCRWorkflowChangeString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.comment":
                    convertedString = getQcrCommentString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.sources.add":
                    convertedString = getQCRProblemSourcesAddedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.sources.delete":
                    convertedString = getQCRProblemSourcesDeletedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.problemItems.add":
                    convertedString = getQCRProblemItemsAddedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.problemItems.update":
                    convertedString = getProblemItemUpdatedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.problemItems.delete":
                    convertedString = getQCRProblemItemsDeletedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.relatedItems.add":
                    convertedString = getQCRRelatedItemsAddedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.relatedItems.delete":
                    convertedString = getQCRRelatedItemsDeletedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.capa.add":
                    convertedString = getQCRCAPAAddedOrDeleteString(messageString, qcr, as, true);
                    break;
                case "plm.quality.qcr.capa.update":
                    convertedString = getCapaUpdatedString(messageString, qcr, as);
                    break;
                case "plm.quality.qcr.capa.delete":
                    convertedString = getQCRCAPAAddedOrDeleteString(messageString, qcr, as, false);
                    break;
                case "plm.quality.qcr.capa.audit":
                    convertedString = getQCRCAPAAuditString(messageString, qcr, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getQcrCreatedString(String messageString, Person actor, PQMQCR qcr) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), qcr.getQcrNumber());
    }

    private String getQcrRevisionCreatedString(String messageString, Person actor, PQMQCR qcr) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), qcr.getQcrNumber());
    }

    private String getQcrBasicInfoUpdatedString(String messageString, Person actor, PQMQCR qcr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), qcr.getQcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.qcr.update.basicinfo.property");

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

    private String getQcrBasicInfoUpdatedJson(PQMQCR oldQcr, PQMQCR qcr) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldQcr.getTitle();
        String newValue = qcr.getTitle();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Title", oldValue, newValue));
        }

        oldValue = oldQcr.getDescription();
        newValue = qcr.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        Integer oldValue1 = oldQcr.getQualityAdministrator();
        Integer newValue1 = qcr.getQualityAdministrator();

        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Quality Administrator", personRepository.findOne(oldValue1).getFullName(), personRepository.findOne(newValue1).getFullName()));
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

    private String getQcrAttributesUpdatedJson(PQMQCRAttribute oldAttribute, PQMQCRAttribute newAttribute) {
        PQMQualityTypeAttribute attDef = qualityTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
        List<ASAttributeChangeDTO> changes = commonActivityStream.getAttributeUpdateJsonData(oldAttribute,newAttribute,attDef);

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

    private String getQcrAttributeUpdatedString(String messageString, Person actor, PQMQCR qcr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), qcr.getQcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.quality.qcr.update.attributes.attribute");
        String propertyUpdateString = activityStreamResourceBundle.getString("plm.quality.qcr.update.attributes.attribute").substring(0, 21);

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

    private String getQcrFilesAddedJson(List<PLMFile> files) {
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

    private String getQcrFoldersAddedJson(PLMFile file) {
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

    private String getQcrFoldersDeletedJson(PLMFile file) {
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

    private String getQcrFilesAddedString(String messageString, PQMQCR qcr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), qcr.getQcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.qcr.files.add.file");

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

    private String getQcrFoldersAddedString(String messageString, PQMQCR qcr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), qcr.getQcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getQcrFoldersDeletedString(String messageString, PQMQCR qcr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), qcr.getQcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getQcrFileDeletedString(String messageString, PQMQCR qcr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), qcr.getQcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getQcrFilesVersionedJson(List<PLMFile> files) {
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

    private String getQcrFilesVersionedString(String messageString, PQMQCR qcr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), qcr.getQcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.quality.qcr.files.version.file");

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

    private String getQcrFileRenamedString(String messageString, PQMQCR qcr, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    qcr.getQcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getQcrFileString(String messageString, PQMQCR qcr, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    qcr.getQcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getQcrWorkflowStartString(String messageString, PQMQCR qcr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                qcr.getQcrNumber());
    }

    private String getQcrWorkflowFinishString(String messageString, PQMQCR qcr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                qcr.getQcrNumber());
    }

    private String getQcrWorkflowPromoteDemoteString(String messageString, PQMQCR qcr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                qcr.getQcrNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getQcrWorkflowHoldUnholdString(String messageString, PQMQCR qcr, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                qcr.getQcrNumber());
    }

    private String getQCRWorkflowAddedString(String messageString, PQMQCR qcr, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    qcr.getQcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getQCRWorkflowChangeString(String messageString, PQMQCR qcr, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    qcr.getQcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getQcrCommentString(String messageString, PQMQCR qcr, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                qcr.getQcrNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }


    private String getQCRProblemMaterialsAddedJson(List<PQMQCRProblemMaterial> problemMaterials) {
        List<ASNewNCRProblemItem> asNewQCRProblemMaterials = new ArrayList<>();
        for (PQMQCRProblemMaterial problemItem : problemMaterials) {
            ASNewNCRProblemItem asNewNCRProblemItem = new ASNewNCRProblemItem(problemItem.getMaterial().getId(), problemItem.getMaterial().getPartName(), problemItem.getMaterial().getPartNumber(), problemItem.getMaterial().getMfrPartType().getName());
            asNewQCRProblemMaterials.add(asNewNCRProblemItem);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewQCRProblemMaterials);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQCRProblemMaterialsDeletedJson(PQMQCRProblemMaterial problemItem) {
        String json = null;
        ASNewNCRProblemItem asNewNCRProblemItem = new ASNewNCRProblemItem(problemItem.getMaterial().getId(), problemItem.getMaterial().getPartName(), problemItem.getMaterial().getPartNumber(), problemItem.getMaterial().getMfrPartType().getName());

        List<ASNewNCRProblemItem> asNewQCRProblemMaterials = new ArrayList<>();
        asNewQCRProblemMaterials.add(asNewNCRProblemItem);
        try {
            json = objectMapper.writeValueAsString(asNewQCRProblemMaterials);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQCRProblemSourcesAddedString(String messageString, PQMQCR qcr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), qcr.getQcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);
        String fileString = "";
        if (qcr.getQcrFor().equals(QCRFor.PR)) {
            fileString = activityStreamResourceBundle.getString("plm.quality.qcr.sources.add.problemReport");
        } else {
            fileString = activityStreamResourceBundle.getString("plm.quality.qcr.sources.add.ncr");
        }
        String json = as.getData();
        try {
            List<ASNewQCRSource> sources = objectMapper.readValue(json, new TypeReference<List<ASNewQCRSource>>() {
            });
            for (ASNewQCRSource source : sources) {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(source.getNumber())));
                sb.append(s);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getQCRProblemItemsAddedString(String messageString, PQMQCR qcr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), qcr.getQcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);
        String fileString = "";
        if (qcr.getQcrFor().equals(QCRFor.PR)) {
            fileString = activityStreamResourceBundle.getString("plm.quality.qcr.problemItems.add.item");
        } else {
            fileString = activityStreamResourceBundle.getString("plm.quality.qcr.problemItems.add.part");
        }
        String json = as.getData();
        try {
            if (qcr.getQcrFor().equals(QCRFor.PR)) {
                List<ASNewPRProblemItem> items = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
                });
                for (ASNewPRProblemItem item : items) {
                    String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(item.getItemName()), highlightValue(item.getRevision()), highlightValue(item.getLifecyclePhase())));
                    sb.append(s);
                }
            } else {
                List<ASNewNCRProblemItem> parts = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
                });
                for (ASNewNCRProblemItem part : parts) {
                    String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(part.getPartName()), highlightValue(part.getPartNumber()), highlightValue(part.getPartType())));
                    sb.append(s);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getProblemItemUpdatedString(String messageString, PQMQCR pqmqcr, ActivityStream as) {
        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.quality.qcr.problemItems.update.property");

        String json = as.getData();
        try {
            List<ASProblemItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASProblemItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, as.getActor().getFullName(), highlightValue(propChanges.get(0).getItemNumber()),
                    pqmqcr.getQcrNumber());
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


    private String getQCRProblemSourcesDeletedString(String messageString, PQMQCR qcr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewQCRSource> asNewQCRSources = objectMapper.readValue(json, new TypeReference<List<ASNewQCRSource>>() {
            });
            ASNewQCRSource newQCRSource = asNewQCRSources.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(newQCRSource.getNumber()), qcr.getQcrNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getQCRProblemItemsDeletedString(String messageString, PQMQCR qcr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            if (qcr.getQcrFor().equals(QCRFor.PR)) {
                List<ASNewPRProblemItem> asNewPRProblemItems = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
                });
                ASNewPRProblemItem asNewNCRProblemItem = asNewPRProblemItems.get(0);
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewNCRProblemItem.getItemName()), highlightValue(asNewNCRProblemItem.getRevision()), highlightValue(asNewNCRProblemItem.getLifecyclePhase()), qcr.getQcrNumber());
            } else {
                List<ASNewNCRProblemItem> asNewQCRProblemMaterials = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
                });
                ASNewNCRProblemItem asNewNCRProblemItem = asNewQCRProblemMaterials.get(0);
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewNCRProblemItem.getPartName()), highlightValue(asNewNCRProblemItem.getPartNumber()), highlightValue(asNewNCRProblemItem.getPartType()), qcr.getQcrNumber());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getQCRRelatedMaterialsAddedJson(List<PQMQCRRelatedMaterial> relatedMaterials) {
        List<ASNewNCRProblemItem> asNewQCRProblemMaterials = new ArrayList<>();
        for (PQMQCRRelatedMaterial relatedItem : relatedMaterials) {
            ASNewNCRProblemItem asNewNCRProblemItem = new ASNewNCRProblemItem(relatedItem.getMaterial().getId(), relatedItem.getMaterial().getPartName(), relatedItem.getMaterial().getPartNumber(), relatedItem.getMaterial().getMfrPartType().getName());
            asNewQCRProblemMaterials.add(asNewNCRProblemItem);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewQCRProblemMaterials);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQCRRelatedMaterialsDeletedJson(PQMQCRRelatedMaterial relatedItem) {
        String json = null;
        ASNewNCRProblemItem asNewNCRProblemItem = new ASNewNCRProblemItem(relatedItem.getMaterial().getId(), relatedItem.getMaterial().getPartName(), relatedItem.getMaterial().getPartNumber(), relatedItem.getMaterial().getMfrPartType().getName());

        List<ASNewNCRProblemItem> asNewQCRProblemMaterials = new ArrayList<>();
        asNewQCRProblemMaterials.add(asNewNCRProblemItem);
        try {
            json = objectMapper.writeValueAsString(asNewQCRProblemMaterials);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQCRRelatedItemsAddedString(String messageString, PQMQCR qcr, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), qcr.getQcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = "";
        if (qcr.getQcrFor().equals(QCRFor.PR)) {
            fileString = activityStreamResourceBundle.getString("plm.quality.qcr.relatedItems.add.item");
        } else {
            fileString = activityStreamResourceBundle.getString("plm.quality.qcr.relatedItems.add.part");
        }

        String json = as.getData();
        try {
            if (qcr.getQcrFor().equals(QCRFor.PR)) {
                List<ASNewPRProblemItem> items = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
                });
                for (ASNewPRProblemItem item : items) {
                    String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(item.getItemName()), highlightValue(item.getRevision()), highlightValue(item.getLifecyclePhase())));
                    sb.append(s);
                }
            } else {
                List<ASNewNCRProblemItem> parts = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
                });
                for (ASNewNCRProblemItem part : parts) {
                    String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(part.getPartName()), highlightValue(part.getPartNumber()), highlightValue(part.getPartType())));
                    sb.append(s);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getQCRRelatedItemsDeletedString(String messageString, PQMQCR qcr, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            if (qcr.getQcrFor().equals(QCRFor.PR)) {
                List<ASNewPRProblemItem> asNewQCRProblemMaterials = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
                });
                ASNewPRProblemItem asNewNCRProblemItem = asNewQCRProblemMaterials.get(0);
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewNCRProblemItem.getItemName()),
                        highlightValue(asNewNCRProblemItem.getRevision()), highlightValue(asNewNCRProblemItem.getLifecyclePhase()), qcr.getQcrNumber());
            } else {
                List<ASNewNCRProblemItem> asNewQCRProblemMaterials = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
                });
                ASNewNCRProblemItem asNewNCRProblemItem = asNewQCRProblemMaterials.get(0);
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewNCRProblemItem.getPartName()),
                        highlightValue(asNewNCRProblemItem.getPartNumber()), highlightValue(asNewNCRProblemItem.getPartType()), qcr.getQcrNumber());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getQCRProblemItemsAddedJson(List<PQMQCRProblemItem> problemItems) {
        List<ASNewPRProblemItem> asNewPRProblemItems = new ArrayList<>();
        for (PQMQCRProblemItem problemItem : problemItems) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(problemItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            ASNewPRProblemItem asNewPRProblemItem = new ASNewPRProblemItem(itemRevision.getId(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(),
                    itemRevision.getLifeCyclePhase().getPhase());
            asNewPRProblemItems.add(asNewPRProblemItem);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewPRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQCRPRProblemSourcesAddedJson(List<PQMQCRAggregatePR> aggregatePRs) {
        List<ASNewQCRSource> asNewQCRSources = new ArrayList<>();
        for (PQMQCRAggregatePR problemItem : aggregatePRs) {
            ASNewQCRSource asNewQCRSource = new ASNewQCRSource(problemItem.getPr().getId(), problemItem.getPr().getPrNumber());
            asNewQCRSources.add(asNewQCRSource);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewQCRSources);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQCRNCRProblemSourcesAddedJson(List<PQMQCRAggregateNCR> aggregateNCRs) {
        List<ASNewQCRSource> asNewQCRSources = new ArrayList<>();
        for (PQMQCRAggregateNCR problemItem : aggregateNCRs) {
            ASNewQCRSource asNewQCRSource = new ASNewQCRSource(problemItem.getNcr().getId(), problemItem.getNcr().getNcrNumber());
            asNewQCRSources.add(asNewQCRSource);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewQCRSources);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQCRProblemItemsDeletedJson(PQMQCRProblemItem problemItem) {
        String json = null;
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(problemItem.getItem());
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        ASNewPRProblemItem asNewPRProblemItem = new ASNewPRProblemItem(itemRevision.getId(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());

        List<ASNewPRProblemItem> asNewPRProblemItems = new ArrayList<>();
        asNewPRProblemItems.add(asNewPRProblemItem);
        try {
            json = objectMapper.writeValueAsString(asNewPRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQCRNCRProblemSourcesDeletedJson(PQMQCRAggregateNCR aggregateNCR) {
        String json = null;
        ASNewQCRSource asNewQCRSource = new ASNewQCRSource(aggregateNCR.getNcr().getId(), aggregateNCR.getNcr().getNcrNumber());

        List<ASNewQCRSource> asNewQCRSources = new ArrayList<>();
        asNewQCRSources.add(asNewQCRSource);
        try {
            json = objectMapper.writeValueAsString(asNewQCRSources);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQCRPRProblemSourcesDeletedJson(PQMQCRAggregatePR aggregatePR) {
        String json = null;
        ASNewQCRSource asNewQCRSource = new ASNewQCRSource(aggregatePR.getPr().getId(), aggregatePR.getPr().getPrNumber());

        List<ASNewQCRSource> asNewQCRSources = new ArrayList<>();
        asNewQCRSources.add(asNewQCRSource);
        try {
            json = objectMapper.writeValueAsString(asNewQCRSources);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQCRRelatedItemsAddedJson(List<PQMQCRRelatedItem> relatedItems) {
        List<ASNewPRProblemItem> asNewPRProblemItems = new ArrayList<>();
        for (PQMQCRRelatedItem problemItem : relatedItems) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(problemItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            ASNewPRProblemItem asNewPRProblemItem = new ASNewPRProblemItem(itemRevision.getId(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());
            asNewPRProblemItems.add(asNewPRProblemItem);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewPRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQCRRelatedItemsDeletedJson(PQMQCRRelatedItem relatedItem) {
        String json = null;
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        ASNewPRProblemItem asNewPRProblemItem = new ASNewPRProblemItem(itemRevision.getId(), item.getItemName(), item.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());

        List<ASNewPRProblemItem> asNewPRProblemItems = new ArrayList<>();
        asNewPRProblemItems.add(asNewPRProblemItem);
        try {
            json = objectMapper.writeValueAsString(asNewPRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQCRCapaAddedOrDeletedJson(PQMQCRCAPA pqmqcrcapa) {
        List<ASNewCAPA> asNewCAPAs = new ArrayList<>();
        ASNewCAPA asNewNCRProblemItem = new ASNewCAPA(pqmqcrcapa.getId(), pqmqcrcapa.getRootCauseAnalysis(), pqmqcrcapa.getCorrectiveAction(), pqmqcrcapa.getPreventiveAction());
        asNewCAPAs.add(asNewNCRProblemItem);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewCAPAs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getQCRCapaUpdatedJson(PQMQCRCAPA oldCapa, PQMQCRCAPA capa) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();
        String oldValue = oldCapa.getRootCauseAnalysis();
        String newValue = capa.getRootCauseAnalysis();

        if (oldValue == null) oldValue = "";
        if (newValue == null) newValue = "";

        if (!oldValue.equals(newValue)) {
            changes.add(new ASPropertyChangeDTO("Root Cause Analysis", oldValue, newValue));
        }

        oldValue = oldCapa.getCorrectiveAction();
        newValue = capa.getCorrectiveAction();
        if (oldValue == null) oldValue = "";
        if (newValue == null) newValue = "";

        if (!oldValue.equals(newValue)) {
            changes.add(new ASPropertyChangeDTO("Corrective Action", oldValue, newValue));
        }

        oldValue = oldCapa.getPreventiveAction();
        newValue = capa.getPreventiveAction();
        if (oldValue == null) oldValue = "";
        if (newValue == null) newValue = "";

        if (!oldValue.equals(newValue)) {
            changes.add(new ASPropertyChangeDTO("Preventive Action", oldValue, newValue));
        }
        oldValue = oldCapa.getCapaNotes();
        newValue = capa.getCapaNotes();
        if (oldValue == null) oldValue = "";
        if (newValue == null) newValue = "";

        if (!oldValue.equals(newValue)) {
            changes.add(new ASPropertyChangeDTO("CAPA Notes", oldValue, newValue));
        }
        oldValue = oldCapa.getAuditNotes();
        newValue = capa.getAuditNotes();
        if (oldValue == null) oldValue = "";
        if (newValue == null) newValue = "";

        if (!oldValue.equals(newValue)) {
            changes.add(new ASPropertyChangeDTO("Audit Notes", oldValue, newValue));
        }
        oldValue = oldCapa.getResult().toString();
        newValue = capa.getResult().toString();
        if (oldValue == null) oldValue = "";
        if (newValue == null) newValue = "";

        if (!oldValue.equals(newValue)) {
            changes.add(new ASPropertyChangeDTO("Audit Result", oldValue, newValue));
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

    private String getQCRCAPAAddedOrDeleteString(String messageString, PQMQCR qcr, ActivityStream as, Boolean added) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), qcr.getQcrNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);
        String fileString = "";
        if (added) {
            fileString = activityStreamResourceBundle.getString("plm.quality.qcr.capa.add.capa");
        } else {
            fileString = activityStreamResourceBundle.getString("plm.quality.qcr.capa.delete.capa");
        }
        String json = as.getData();
        try {
            List<ASNewCAPA> capas = objectMapper.readValue(json, new TypeReference<List<ASNewCAPA>>() {
            });
            for (ASNewCAPA capa : capas) {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(capa.getRootCause()), highlightValue(capa.getCorrectiveAction()), highlightValue(capa.getPreventiveAction())));
                sb.append(s);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getCapaUpdatedString(String messageString, PQMQCR qcr, ActivityStream as) {
        StringBuffer sb = new StringBuffer();
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName(), qcr.getQcrNumber());
        sb.append(activityString);
        String updateString = activityStreamResourceBundle.getString("plm.quality.qcr.capa.update.property");

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


    private String getQCRCAPAAuditString(String messageString, PQMQCR qcr, ActivityStream as) {
        String activityString = "";

        StringBuffer sb = new StringBuffer();

        String fileString = activityStreamResourceBundle.getString("plm.quality.qcr.capa.audit.capa");

        String json = as.getData();
        try {
            ASCAPAAudit capa = objectMapper.readValue(json, new TypeReference<ASCAPAAudit>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(capa.getRootCause()), qcr.getQcrNumber());
            sb.append(activityString);
            String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(capa.getAuditResult()), highlightValue(capa.getAuditNotes())));
            sb.append(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
