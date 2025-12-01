package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.BOPEvents;
import com.cassinisys.plm.model.cm.PLMChange;
import com.cassinisys.plm.model.cm.PLMDCO;
import com.cassinisys.plm.model.cm.PLMECO;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItemAttribute;
import com.cassinisys.plm.model.plm.PLMItemTypeAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.cm.ChangeRepository;
import com.cassinisys.plm.repo.mes.BOPRevisionRepository;
import com.cassinisys.plm.repo.mes.MESBOPRepository;
import com.cassinisys.plm.repo.mes.MESOperationRepository;
import com.cassinisys.plm.repo.mes.PhantomRepository;
import com.cassinisys.plm.repo.plm.ItemTypeAttributeRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.activitystream.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class BOPActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MESBOPRepository mesbopRepository;
    @Autowired
    private BOPRevisionRepository bopRevisionRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ChangeRepository changeRepository;
    @Autowired
    private PhantomRepository phantomRepository;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private MESOperationRepository mesOperationRepository;
    @Autowired
    private MessageSource messageSource;

    @Async
    @EventListener
    public void itemCreated(BOPEvents.BOPCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemRevisionCreated(BOPEvents.BOPRevisionCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.revision");
        as.setObject(object);
        as.setConverter(getConverterKey());
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemBasicInfoUpdated(BOPEvents.BOPBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MESBOP oldItem = event.getPlmOldBOP();
        MESBOP newItem = event.getBop();
        Integer bopRevision = event.getBopRevision();

        object.setObject(bopRevision);
        object.setType("bop");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getBOPBasicInfoUpdatedJson(oldItem, newItem));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void itemAttributesUpdated(BOPEvents.BOPAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        PLMItemAttribute oldAtt = event.getOldAttribute();
        PLMItemAttribute newAtt = event.getNewAttribute();

        object.setObject(event.getBopRevision());
        object.setType("bop");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getBOPAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void itemReleased(BOPEvents.BOPReleasedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);

        PLMChange change = event.getChangeOrder();
        if (change != null) {
            as.setActivity("plm.mes.bop.release.withchangeorder");
            ActivityStreamObject context = new ActivityStreamObject();
            context.setObject(change.getId());
            if (change instanceof PLMECO) {
                context.setType("eco");
            } else if (change instanceof PLMDCO) {
                context.setType("dco");
            }
            as.setContext(context);
        } else {
            as.setActivity("plm.mes.bop.release.withoutchangeorder");
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemStatusChanged(BOPEvents.BOPStatusChangedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);

        PLMChange change = event.getChangeOrder();
        if (change != null) {
            as.setActivity("plm.mes.bop.status.withchangeorder");
            ActivityStreamObject context = new ActivityStreamObject();
            context.setObject(change.getId());
            if (change instanceof PLMECO) {
                context.setType("eco");
            } else if (change instanceof PLMDCO) {
                context.setType("dco");
            }
            as.setContext(context);
        } else {
            as.setActivity("plm.mes.bop.status.withoutchangeorder");
        }
        ASStatusChangeDto statusChangeDto = new ASStatusChangeDto(event.getOldStatus().getPhase(), event.getNewStatus().getPhase());
        String json = "";
        try {
            json = objectMapper.writeValueAsString(statusChangeDto);
            as.setData(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFilesAdded(BOPEvents.BOPFilesAddedEvent event) {
        List<MESBOPFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);
        as.setData(getBOPFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileDeleted(BOPEvents.BOPFileDeletedEvent event) {
        MESBOPFile file = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);
        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(file.getId(), file.getName(), FileUtils.byteCountToDisplaySize(file.getSize()));
        String json = "";
        try {
            json = objectMapper.writeValueAsString(asNewFileDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        as.setData(json);
        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void itemFilesVersioned(BOPEvents.BOPFilesVersionedEvent event) {
        List<MESBOPFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);
        as.setData(getBOPFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileRenamed(BOPEvents.BOPFileRenamedEvent event) {
        MESBOPFile oldFile = event.getOldFile();
        MESBOPFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.files.rename");
        if (event.getType().equals("Rename")) as.setActivity("plm.mes.bop.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.mes.bop.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
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
    public void itemFileLocked(BOPEvents.BOPFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getBopFile().getId(), event.getBopFile().getName(), FileUtils.byteCountToDisplaySize(event.getBopFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileUnlocked(BOPEvents.BOPFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getBopFile().getId(), event.getBopFile().getName(), FileUtils.byteCountToDisplaySize(event.getBopFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileDownloaded(BOPEvents.BOPFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getBopFile().getId(), event.getBopFile().getName(), FileUtils.byteCountToDisplaySize(event.getBopFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void itemWorkflowStarted(BOPEvents.BOPWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemWorkflowFinished(BOPEvents.BOPWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemWorkflowPromoted(BOPEvents.BOPWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
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
    public void itemWorkflowDemoted(BOPEvents.BOPWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
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
    public void itemWorkflowHold(BOPEvents.BOPWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
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
    public void itemWorkflowUnhold(BOPEvents.BOPWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
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
    public void itemWorkflowChange(BOPEvents.BOPWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
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
            as.setActivity("plm.mes.bop.workflow.change");
        } else {
            as.setActivity("plm.mes.bop.workflow.add");
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
    public void itemCommentAdded(BOPEvents.BOPCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void operationResourceAdded(BOPEvents.BOPRouteOperationsAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);
        List<ASBOPRouteItem> asBomItems = new ArrayList<>();
        event.getRouteItems().forEach(routeItem -> {
            if (routeItem.getType().equals(BOPPlanTypeEnum.PHANTOM)) {
                MESPhantom phantom = phantomRepository.findOne(routeItem.getPhantom());
                asBomItems.add(new ASBOPRouteItem(routeItem.getSequenceNumber(), phantom.getName(), phantom.getNumber()));
            } else {
                MESOperation operation = mesOperationRepository.findOne(routeItem.getOperation());
                asBomItems.add(new ASBOPRouteItem(routeItem.getSequenceNumber(), operation.getName(), operation.getNumber()));
            }
        });
        try {
            as.setData(objectMapper.writeValueAsString(asBomItems));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void operationResourceAdded(BOPEvents.BOPRouteItemDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);
        ASBOPRouteItem routeItem = null;
        if (event.getRouteItem().getType().equals(BOPPlanTypeEnum.PHANTOM)) {
            MESPhantom phantom = phantomRepository.findOne(event.getRouteItem().getPhantom());
            routeItem = new ASBOPRouteItem(event.getRouteItem().getSequenceNumber(), phantom.getName(), phantom.getNumber());
        } else {
            MESOperation operation = mesOperationRepository.findOne(event.getRouteItem().getOperation());
            routeItem = new ASBOPRouteItem(event.getRouteItem().getSequenceNumber(), operation.getName(), operation.getNumber());
        }
        try {
            as.setData(objectMapper.writeValueAsString(routeItem));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemBomItemsUpdated(BOPEvents.BOPRouteItemUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.route.update.basicinfo");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);
        as.setData(getBopRouteItemUpdatedJson(event.getOldRouteItem(), event.getRouteItem()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void planFoldersAdded(BOPEvents.BOPFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);
        as.setData(getBOPFoldersAddOrDeleteJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planFoldersDeleted(BOPEvents.BOPFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mes.bop.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getBopRevision());
        object.setType("bop");
        as.setObject(object);
        as.setData(getBOPFoldersAddOrDeleteJson(files));

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.mes.bop";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MESBOPRevision bopRevision = bopRevisionRepository.findOne(object.getObject());

            if (bopRevision == null) return "";

            MESBOP bop = mesbopRepository.findOne(bopRevision.getMaster());

            if (bop == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mes.bop.create":
                    convertedString = getBOPCreatedString(messageString, actor, bop);
                    break;
                case "plm.mes.bop.revision":
                    convertedString = getBOPRevisionCreatedString(messageString, actor, bop, bopRevision);
                    break;
                case "plm.mes.bop.update.basicinfo":
                    convertedString = getBOPBasicInfoUpdatedString(messageString, actor, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.update.attributes":
                    convertedString = getBOPAttributeUpdatedString(messageString, actor, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.release.withchangeorder":
                    convertedString = getBOPReleasedWithChangeOrderString(messageString, as);
                    break;
                case "plm.mes.bop.release.withoutchangeorder":
                    convertedString = getBOPReleasedWithoutChangeOrderString(messageString, as);
                    break;
                case "plm.mes.bop.status.withchangeorder":
                    convertedString = getBOPStatusWithChangeOrderString(messageString, as);
                    break;
                case "plm.mes.bop.files.add":
                    convertedString = getBOPFilesAddedString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.files.delete":
                    convertedString = getBOPFilesDeletedString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.files.version":
                    convertedString = getBOPFilesVersionedString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.files.rename":
                    convertedString = getBOPFileRenamedString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.files.replace":
                    convertedString = getBOPFileRenamedString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.files.lock":
                    convertedString = getBOPFileString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.files.unlock":
                    convertedString = getBOPFileString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.files.folders.add":
                    convertedString = getBOPFoldersAddedOrDeletedString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.files.folders.delete":
                    convertedString = getBOPFoldersAddedOrDeletedString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.files.download":
                    convertedString = getBOPFileString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.workflow.start":
                    convertedString = getBOPWorkflowStartString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.workflow.finish":
                    convertedString = getBOPWorkflowFinishString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.workflow.promote":
                    convertedString = getBOPWorkflowPromoteDemoteString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.workflow.demote":
                    convertedString = getBOPWorkflowPromoteDemoteString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.workflow.hold":
                    convertedString = getBOPWorkflowHoldUnholdString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.workflow.unhold":
                    convertedString = getBOPWorkflowHoldUnholdString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.workflow.add":
                    convertedString = getBOPWorkflowAddedString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.workflow.change":
                    convertedString = getBOPWorkflowChangeString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.comment":
                    convertedString = getBOPCommentString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.route.add":
                    convertedString = getBOPBomItemAddedString(messageString, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.route.update.basicinfo":
                    convertedString = getBomItemUpdatedString(messageString, actor, bop, bopRevision, as);
                    break;
                case "plm.mes.bop.route.delete":
                    convertedString = getBOPBomItemDeletedString(messageString, bop, bopRevision, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getBOPCreatedString(String messageString, Person actor, MESBOP bop) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), bop.getNumber());
    }

    private String getBOPRevisionCreatedString(String messageString, Person actor, MESBOP bop, MESBOPRevision bopRevision) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), bop.getNumber(),
                bopRevision.getRevision(), bopRevision.getLifeCyclePhase().getPhase());
    }

    private String getBOPReleasedWithChangeOrderString(String messageString, ActivityStream as) {
        MESBOPRevision bopRevision = bopRevisionRepository.findOne(as.getObject().getObject());
        MESBOP bop = mesbopRepository.findOne(bopRevision.getMaster());
        PLMChange change = changeRepository.findOne(as.getContext().getObject());
        String changeNumber = "";
        if (change != null) {
            if (change instanceof PLMECO) {
                changeNumber = ((PLMECO) change).getEcoNumber();
            } else if (change instanceof PLMDCO) {
                changeNumber = ((PLMDCO) change).getDcoNumber();
            }
        }
        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                bop.getNumber(), bopRevision.getRevision(), bopRevision.getLifeCyclePhase().getPhase(), highlightValue(changeNumber));
    }

    private String getBOPStatusWithChangeOrderString(String messageString, ActivityStream as) {
        MESBOPRevision bopRevision = bopRevisionRepository.findOne(as.getObject().getObject());
        MESBOP bop = mesbopRepository.findOne(bopRevision.getMaster());
        PLMChange change = changeRepository.findOne(as.getContext().getObject());
        String changeNumber = "";
        if (change != null) {
            if (change instanceof PLMECO) {
                changeNumber = ((PLMECO) change).getEcoNumber();
            } else if (change instanceof PLMDCO) {
                changeNumber = ((PLMDCO) change).getDcoNumber();
            }
        }
        ASStatusChangeDto statusChangeDto = null;
        try {
            if (as.getData() != null) {
                statusChangeDto = objectMapper.readValue(as.getData(), new TypeReference<ASStatusChangeDto>() {
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (statusChangeDto != null) {
            return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    bop.getNumber(), bopRevision.getRevision(), bopRevision.getLifeCyclePhase().getPhase(), highlightValue(statusChangeDto.getFromStatus()), highlightValue(statusChangeDto.getToStatus()), highlightValue(changeNumber));
        } else {
            return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    bop.getNumber(), bopRevision.getRevision(), bopRevision.getLifeCyclePhase().getPhase(), highlightValue(changeNumber));
        }
    }

    private String getBOPReleasedWithoutChangeOrderString(String messageString, ActivityStream as) {
        MESBOPRevision bopRevision = bopRevisionRepository.findOne(as.getObject().getObject());
        MESBOP bop = mesbopRepository.findOne(bopRevision.getMaster());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                bop.getNumber(), bopRevision.getRevision(), bopRevision.getLifeCyclePhase().getPhase());
    }

    private String getBOPIncorporateString(String messageString, ActivityStream as) {
        MESBOPRevision bopRevision = bopRevisionRepository.findOne(as.getObject().getObject());
        MESBOP bop = mesbopRepository.findOne(bopRevision.getMaster());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                bop.getNumber(), bopRevision.getRevision(), bopRevision.getLifeCyclePhase().getPhase());
    }

    private String getBOPBasicInfoUpdatedJson(MESBOP plmOldItem, MESBOP bop) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = plmOldItem.getName();
        String newValue = bop.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = plmOldItem.getDescription();
        newValue = bop.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        String json = null;
        if (changes.size() > 0) {
            try {
                json = objectMapper.writeValueAsString(changes);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return json;
    }

    private String getBOPBasicInfoUpdatedString(String messageString, Person actor, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), bop.getNumber(),
                plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.bop.update.basicinfo.property");

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

    private String getBOPAttributesUpdatedJson(PLMItemAttribute oldAttribute, PLMItemAttribute newAttribute) {
        List<ASAttributeChangeDTO> changes = new ArrayList<>();

        String oldValue = "";

        if (oldAttribute != null) {
            oldValue = oldAttribute.getValueAsString();
        }
        String newValue = newAttribute.getValueAsString();

        PLMItemTypeAttribute attDef = itemTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
        if (!newValue.equals(oldValue)) {
            changes.add(new ASAttributeChangeDTO(attDef.getName(), oldValue, newValue));
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

    private String getBOPAttributeUpdatedString(String messageString, Person actor, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), bop.getNumber(),
                plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mes.bop.update.attributes.attribute");

        String json = as.getData();
        try {
            List<ASAttributeChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASAttributeChangeDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
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

    private String getBOPFilesAddedJson(List<MESBOPFile> files) {
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

    private String getBOPFilesAddedString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), bop.getNumber(),
                plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.bop.files.add.file");

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

    private String getBOPFilesDeletedString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        String activityString = "";

        String json = as.getData();
        try {
            ASNewFileDTO asNewFileDTO = objectMapper.readValue(json, new TypeReference<ASNewFileDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewFileDTO.getName()),
                    bop.getNumber(), plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return activityString;
    }


    private String getBOPFilesVersionedJson(List<MESBOPFile> files) {
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

    private String getBOPFilesVersionedString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), bop.getNumber(),
                plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mes.bop.files.version.file");

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

    private String getBOPFileRenamedString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    bop.getNumber(),
                    plmItemRevision.getRevision(),
                    plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getBOPFileString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    bop.getNumber(),
                    plmItemRevision.getRevision(),
                    plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getBOPWorkflowStartString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                bop.getNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());
    }

    private String getBOPWorkflowFinishString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                bop.getNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());
    }

    private String getBOPWorkflowPromoteDemoteString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                bop.getNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getBOPWorkflowHoldUnholdString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                bop.getNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());
    }

    private String getBOPWorkflowAddedString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    bop.getNumber(),
                    plmItemRevision.getRevision(),
                    plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getBOPWorkflowChangeString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    bop.getNumber(),
                    plmItemRevision.getRevision(),
                    plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getBOPCommentString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                bop.getNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getBOPBomItemAddedString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                bop.getNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());
        StringBuffer sb = new StringBuffer();
        sb.append(message);

        String itemString = activityStreamResourceBundle.getString("plm.mes.bop.route.add.item");

        String json = as.getData();
        try {
            List<ASBOPRouteItem> items = objectMapper.readValue(as.getData(), new TypeReference<List<ASBOPRouteItem>>() {
            });
            for (ASBOPRouteItem routeItem : items) {
                String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(routeItem.getSequenceNumber()), highlightValue(routeItem.getName())));
                sb.append(s);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getBomItemUpdatedString(String messageString, Person actor, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        String updateString = activityStreamResourceBundle.getString("plm.mes.bop.route.update.basicinfo.property");
        StringBuffer sb = new StringBuffer();
        String json = as.getData();
        try {
            List<ASBomItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASBomItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(propChanges.get(0).getNumber()), bop.getNumber(),
                    plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());

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

    private String getBOPBomItemDeletedString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {
        String message = "";
        try {
            ASBOPRouteItem routeItem = objectMapper.readValue(as.getData(), new TypeReference<ASBOPRouteItem>() {
            });
            message = MessageFormat.format(messageString, as.getActor().getFullName().trim(), highlightValue(routeItem.getSequenceNumber()),
                    bop.getNumber());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return message;
    }

    private String getBopRouteItemUpdatedJson(MESBOPRouteOperation oldRouteItem, MESBOPRouteOperation routeItem) {
        List<ASBomItemUpdate> changes = new ArrayList<>();
        String number = "";
        String oldValue = oldRouteItem.getSequenceNumber();
        String newValue = routeItem.getSequenceNumber();
        if (routeItem.getType().equals(BOPPlanTypeEnum.OPERATION)) {
            MESOperation operation = mesOperationRepository.findOne(routeItem.getOperation());
            number = oldRouteItem.getSequenceNumber() + " - " + operation.getNumber() + " - " + operation.getName();
        } else {
            MESPhantom phantom = phantomRepository.findOne(routeItem.getPhantom());
            number = oldRouteItem.getSequenceNumber() + " - " + phantom.getNumber();
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASBomItemUpdate(number, "Sequence Number", oldValue, newValue));
        }

        oldValue = oldRouteItem.getName();
        newValue = routeItem.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASBomItemUpdate(number, "Name", oldValue, newValue));
        }

        oldValue = oldRouteItem.getDescription();
        newValue = routeItem.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!oldValue.equals(newValue)) {
            changes.add(new ASBomItemUpdate(number, "Description", oldValue, newValue));
        }


        Integer oldTime = oldRouteItem.getSetupTime();
        Integer newTime = routeItem.getSetupTime();
        if (oldTime == null) {
            oldValue = "";
        } else {
            oldValue = oldRouteItem.getSetupTime().toString();
        }
        if (newTime == null) {
            newValue = "";
        } else {
            newValue = routeItem.getSetupTime().toString();
        }

        if (!oldValue.equals(newValue)) {
            changes.add(new ASBomItemUpdate(number, "Setup Time", oldValue, newValue));
        }

        oldTime = oldRouteItem.getCycleTime();
        newTime = routeItem.getCycleTime();
        if (oldTime == null) {
            oldValue = "";
        } else {
            oldValue = oldRouteItem.getCycleTime().toString();
        }
        if (newTime == null) {
            newValue = "";
        } else {
            newValue = routeItem.getCycleTime().toString();
        }

        if (!oldValue.equals(newValue)) {
            changes.add(new ASBomItemUpdate(number, "Cycle Time", oldValue, newValue));
        }


        String json = null;
        if (changes.size() > 0) {
            try {
                json = objectMapper.writeValueAsString(changes);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return json;
    }

    private String getBOPFoldersAddOrDeleteJson(PLMFile file) {
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


    private String getBOPFoldersAddedOrDeletedString(String messageString, MESBOP bop, MESBOPRevision plmItemRevision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), bop.getNumber(),
                    plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
