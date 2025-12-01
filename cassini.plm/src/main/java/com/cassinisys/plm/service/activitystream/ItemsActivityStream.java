package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.ItemEvents;
import com.cassinisys.plm.model.cm.PLMChange;
import com.cassinisys.plm.model.cm.PLMDCO;
import com.cassinisys.plm.model.cm.PLMECO;
import com.cassinisys.plm.model.mfr.PLMItemManufacturerPart;
import com.cassinisys.plm.model.pgc.PGCItemSpecification;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.cm.ChangeRepository;
import com.cassinisys.plm.repo.plm.*;
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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ItemsActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ChangeRepository changeRepository;
    @Autowired
    private ItemFileRepository itemFileRepository;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private MessageSource messageSource;

    @Async
    @EventListener
    public void itemCreated(ItemEvents.ItemCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemRevisionCreated(ItemEvents.ItemRevisionCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.revision");
        as.setObject(object);
        as.setConverter(getConverterKey());
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemBasicInfoUpdated(ItemEvents.ItemBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMItem oldItem = event.getPlmOldItem();
        PLMItem newItem = event.getItem();
        PLMItemRevision itemRevision = event.getItemRevision();

        object.setObject(itemRevision.getId());
        object.setType("item");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getItemBasicInfoUpdatedJson(oldItem, newItem));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void itemEffectiveDatesUpdated(ItemEvents.ItemEffectiveDatesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMItem item = event.getItem();
        PLMItemRevision oldRevision = event.getOldRevision();
        PLMItemRevision itemRevision = event.getItemRevision();

        object.setObject(itemRevision.getId());
        object.setType("item");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getItemEffectiveDatesUpdatedJson(item, oldRevision, itemRevision));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void itemAttributesUpdated(ItemEvents.ItemAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMItemRevision plmItemRevision = event.getItemRevision();

        PLMItemAttribute oldAtt = event.getOldAttribute();
        PLMItemAttribute newAtt = event.getNewAttribute();

        object.setObject(plmItemRevision.getId());
        object.setType("item");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getItemAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void itemCopied(ItemEvents.ItemCopiedEvent event) {
        PLMItemRevision newPlmItem = event.getItemRevision();
        PLMItemRevision fromPlmItemRevision = event.getFromPlmItemRevision();

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(newPlmItem.getId());
        object.setType("item");

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(fromPlmItemRevision.getId());
        source.setType("item");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.copy");
        as.setObject(object);
        as.setSource(source);
        as.setConverter(getConverterKey());
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemReleased(ItemEvents.ItemReleasedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        PLMChange change = event.getChangeOrder();
        if (change != null) {
            as.setActivity("plm.items.release.withchangeorder");
            ActivityStreamObject context = new ActivityStreamObject();
            context.setObject(change.getId());
            if (change instanceof PLMECO) {
                context.setType("eco");
            } else if (change instanceof PLMDCO) {
                context.setType("dco");
            }
            as.setContext(context);
        } else {
            as.setActivity("plm.items.release.withoutchangeorder");
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemStatusChanged(ItemEvents.ItemStatusChangedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        PLMChange change = event.getChangeOrder();
        if (change != null) {
            as.setActivity("plm.items.status.withchangeorder");
            ActivityStreamObject context = new ActivityStreamObject();
            context.setObject(change.getId());
            if (change instanceof PLMECO) {
                context.setType("eco");
            } else if (change instanceof PLMDCO) {
                context.setType("dco");
            }
            as.setContext(context);
        } else {
            as.setActivity("plm.items.status.withoutchangeorder");
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
    public void itemIncorporateChanged(ItemEvents.ItemIncorporateUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        if (!event.getOldRevision().getIncorporate() && event.getItemRevision().getIncorporate()) {
            as.setActivity("plm.items.incorporate");
            activityStreamService.create(as);
        } else if (event.getOldRevision().getIncorporate() && !event.getItemRevision().getIncorporate()) {
            as.setActivity("plm.items.unincorporate");
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void itemFilesAdded(ItemEvents.ItemFilesAddedEvent event) {
        List<PLMItemFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        as.setData(getItemFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileDeleted(ItemEvents.ItemFileDeletedEvent event) {
        PLMItemFile file = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
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
    public void itemFilesVersioned(ItemEvents.ItemFilesVersionedEvent event) {
        List<PLMItemFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        as.setData(getItemFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileRenamed(ItemEvents.ItemFileRenamedEvent event) {
        PLMItemFile oldFile = event.getOldFile();
        PLMItemFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.files.rename");
        if (event.getType().equals("Rename")) as.setActivity("plm.items.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.items.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
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
    public void itemFileLocked(ItemEvents.ItemFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getItemFile().getId(), event.getItemFile().getName(), FileUtils.byteCountToDisplaySize(event.getItemFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileUnlocked(ItemEvents.ItemFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getItemFile().getId(), event.getItemFile().getName(), FileUtils.byteCountToDisplaySize(event.getItemFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemFileDownloaded(ItemEvents.ItemFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getItemFile().getId(), event.getItemFile().getName(), FileUtils.byteCountToDisplaySize(event.getItemFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void itemWorkflowStarted(ItemEvents.ItemWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemWorkflowFinished(ItemEvents.ItemWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemWorkflowPromoted(ItemEvents.ItemWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
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
    public void itemWorkflowDemoted(ItemEvents.ItemWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
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
    public void itemWorkflowHold(ItemEvents.ItemWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
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
    public void itemWorkflowUnhold(ItemEvents.ItemWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
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
    public void itemWorkflowChange(ItemEvents.ItemWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
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
            as.setActivity("plm.items.workflow.change");
        } else {
            as.setActivity("plm.items.workflow.add");
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
    public void itemCommentAdded(ItemEvents.ItemCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void itemBomItemAdded(ItemEvents.ItemBomItemAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.bom.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        List<ASBomItem> asBomItems = new ArrayList<>();
        asBomItems.add(new ASBomItem(event.getBomItem().getItem().getItemNumber(),
                event.getBomItem().getQuantity(), event.getBomItem().getRefdes(), event.getBomItem().getNotes()));
        try {
            as.setData(objectMapper.writeValueAsString(asBomItems));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemBomItemDeleted(ItemEvents.ItemBomItemDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.bom.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        ASBomItem asBomItem = new ASBomItem(event.getBomItem().getItem().getItemNumber(),
                event.getBomItem().getQuantity(), event.getBomItem().getRefdes(), event.getBomItem().getNotes());
        try {
            as.setData(objectMapper.writeValueAsString(asBomItem));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemBomItemsAdded(ItemEvents.ItemBomItemsAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.bom.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        List<ASBomItem> asBomItems = new ArrayList<>();
        event.getBomItems().forEach(bom -> {
            asBomItems.add(new ASBomItem(bom.getItem().getItemNumber(),
                    bom.getQuantity(), bom.getRefdes(), bom.getNotes()));
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
    public void itemBomItemsUpdated(ItemEvents.ItemBomItemUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.bom.update");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        as.setData(getBomItemUpdatedJson(event.getOldBomItem(), event.getBomItem()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void itemSubscribed(ItemEvents.ItemSubscribeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.subscribe");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        ASNewPRProblemItem item = new ASNewPRProblemItem(event.getItemRevision().getId(), event.getItem().getItemName(), event.getItem().getItemNumber(),
                event.getItemRevision().getRevision(), event.getItemRevision().getLifeCyclePhase().getPhase());
        try {
            as.setData(objectMapper.writeValueAsString(item));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemUnSubscribed(ItemEvents.ItemUnSubscribeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.unsubscribe");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        ASNewPRProblemItem item = new ASNewPRProblemItem(event.getItemRevision().getId(), event.getItem().getItemName(), event.getItem().getItemNumber(),
                event.getItemRevision().getRevision(), event.getItemRevision().getLifeCyclePhase().getPhase());
        try {
            as.setData(objectMapper.writeValueAsString(item));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void itemMfrPartsAdded(ItemEvents.ItemMfrPartsAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.parts.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        as.setData(getItemMfrPartsAddedJson(event.getItemManufacturerParts()));
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemMfrPartDeleted(ItemEvents.ItemMfrPartDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.parts.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        try {
            as.setData(objectMapper.writeValueAsString(new ASNewNCRProblemItem(event.getItemManufacturerPart().getManufacturerPart().getId(), event.getItemManufacturerPart().getManufacturerPart().getPartName(),
                    event.getItemManufacturerPart().getManufacturerPart().getPartNumber(), event.getItemManufacturerPart().getManufacturerPart().getMfrPartType().getName())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemRelatedItemsAdded(ItemEvents.ItemRelatedItemsAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.relateditems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        as.setData(getRelatedItemsAddedJson(event.getItemRevision(), event.getRelatedItems()));
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemRelatedItemDeleted(ItemEvents.ItemRelatedItemDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.relateditems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        PLMItem toItem;
        PLMItemRevision toRevision;
        if (event.getRelatedItem().getFromItem().equals(event.getItemRevision().getId())) {
            toItem = itemRepository.findOne(event.getRelatedItem().getToItem().getItemMaster());
            toRevision = event.getRelatedItem().getToItem();
        } else {
            toRevision = itemRevisionRepository.findOne(event.getRelatedItem().getFromItem());
            toItem = itemRepository.findOne(toRevision.getItemMaster());
        }
        try {
            as.setData(objectMapper.writeValueAsString(new ASNewPRProblemItem(toRevision.getId(), toItem.getItemName(), toItem.getItemNumber(),
                    toRevision.getRevision(), toRevision.getLifeCyclePhase().getPhase())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemAlternatePartAdded(ItemEvents.ItemAlternatePartsAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.alternateParts.add");
        as.setConverter(getConverterKey());
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        as.setData(getAlternatePartsAddedJson(event.getItemRevision(), event.getParts()));
        activityStreamService.create(as);
    }
    @Async
    @EventListener
    public void itemAlternatePartDeleted(ItemEvents.ItemAlternatePartsDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.alternateParts.delete");
        as.setConverter(getConverterKey());
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        as.setData(getAlternatePartsAddedJson(event.getItemRevision(), event.getParts()));
        activityStreamService.create(as);
    }
    @Async
    @EventListener
    public void itemAlternatePartUpdated(ItemEvents.ItemAlternatePartUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.alternateParts.update");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        as.setData(getAlternatePartUpdatedJson(event.getOldAlternatePart(), event.getNewAlternatePart()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    @Async
    @EventListener
    public void planFoldersAdded(ItemEvents.ItemFoldersAddedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        as.setData(getItemFoldersAddOrDeleteJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void planFoldersDeleted(ItemEvents.ItemFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        as.setData(getItemFoldersAddOrDeleteJson(files));

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void itemInstancesAdded(ItemEvents.ItemInstanceAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.instances.create");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        List<ASNewPRProblemItem> instances = new ArrayList<>();
        event.getInstances().forEach(item -> {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
            instances.add(new ASNewPRProblemItem(itemRevision.getId(), item.getItemNumber(), item.getItemName(),
                    itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase()));
        });
        try {
            as.setData(objectMapper.writeValueAsString(instances));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemInstancesDeleted(ItemEvents.ItemInstanceDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.instances.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(event.getInstance().getLatestRevision());
        ASNewPRProblemItem instance = new ASNewPRProblemItem(itemRevision.getId(), event.getInstance().getItemName(), event.getInstance().getItemNumber(),
                itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());
        try {
            as.setData(objectMapper.writeValueAsString(instance));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void bomConfigurationAdded(ItemEvents.BomConfigurationAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.bom.configuration.create");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        ASBomConfiguration asBomConfiguration = new ASBomConfiguration(event.getBomConfiguration().getId(), event.getBomConfiguration().getName(),
                event.getBomConfiguration().getDescription(), null, null, event.getBomConfigValues());
        try {
            as.setData(objectMapper.writeValueAsString(asBomConfiguration));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void bomNonConfigItemInclusionsAdded(ItemEvents.BomConfigItemInclusionsEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.bom.configuration.configurableitems");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        try {
            as.setData(objectMapper.writeValueAsString(event.getConfigItemInclusions()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void bomNonConfigItemInclusionsAdded(ItemEvents.BomNonConfigItemInclusionsEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.bom.configuration.nonconfigurableitems");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        try {
            as.setData(objectMapper.writeValueAsString(event.getConfigItemInclusions()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void bomConfigAttributeExclusionsAdded(ItemEvents.BomConfigAttributeExclusionsEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.bom.configuration.attributes");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        try {
            as.setData(objectMapper.writeValueAsString(event.getConfigItemInclusions()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void bomConfigAttributeExclusionsAdded(ItemEvents.BomItemResolvedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.bom.resolve");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);
        PLMItemRevision fromRevision = itemRevisionRepository.findOne(event.getFromItem().getLatestRevision());
        PLMItemRevision toRevision = itemRevisionRepository.findOne(event.getToItem().getLatestRevision());
        ASBomResolve asBomResolve = new ASBomResolve(event.getFromItem().getItemName(), fromRevision.getRevision(), fromRevision.getLifeCyclePhase().getPhase(),
                event.getToItem().getItemName(), toRevision.getRevision(), toRevision.getLifeCyclePhase().getPhase());
        try {
            as.setData(objectMapper.writeValueAsString(asBomResolve));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void bomConfigurationUpdated(ItemEvents.BomConfigurationUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.bom.configuration.update");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        ASBomConfiguration asBomConfiguration = new ASBomConfiguration(event.getBomConfiguration().getId(), event.getBomConfiguration().getName(),
                event.getBomConfiguration().getDescription(), event.getExistBomConfig().getName(), event.getExistBomConfig().getDescription(), event.getBomConfigValues());
        try {
            as.setData(objectMapper.writeValueAsString(asBomConfiguration));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemLifeCyclePromoted(ItemEvents.ItemPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.lifeCyclePhase.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromLifeCyclePhase().getId());
        source.setType("item");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToLifeCyclePhase().getId());
        target.setType("item");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemLifeCycleDemoted(ItemEvents.ItemDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.lifeCyclePhase.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItemRevision().getId());
        object.setType("item");
        as.setObject(object);

        ActivityStreamObject source = new ActivityStreamObject();
        source.setObject(event.getFromLifeCyclePhase().getId());
        source.setType("item");
        as.setSource(source);

        ActivityStreamObject target = new ActivityStreamObject();
        target.setObject(event.getToLifeCyclePhase().getId());
        target.setType("item");
        as.setTarget(target);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void itemSpecificationAddedEvent(ItemEvents.ItemComplianceAddEvent event) {

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.specifications.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItem());
        object.setType("item");
        as.setObject(object);
        as.setData(getItemSpecificationAddedJson(event.getSpecifications(), event.getItem()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void declarationSpecificationDeleted(ItemEvents.ItemComplianceDeleteEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.items.specifications.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getItem());
        object.setType("item");
        as.setObject(object);
        ASItemSpecification itemSpecification = new ASItemSpecification(event.getItemSpecification().getSpecification().getName(), event.getItemSpecification().getSpecification().getNumber());
        try {
            as.setData(objectMapper.writeValueAsString(itemSpecification));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.items";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(object.getObject());

            if (itemRevision == null) return "";

            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());

            if (item == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.items.create":
                    convertedString = getItemCreatedString(messageString, actor, item);
                    break;
                case "plm.items.revision":
                    convertedString = getItemRevisionCreatedString(messageString, actor, item, itemRevision);
                    break;
                case "plm.items.update.basicinfo":
                    convertedString = getItemBasicInfoUpdatedString(messageString, actor, item, itemRevision, as);
                    break;
                case "plm.items.update.attributes":
                    convertedString = getItemAttributeUpdatedString(messageString, actor, item, itemRevision, as);
                    break;
                case "plm.items.copy":
                    convertedString = getItemCopiedString(messageString, as);
                    break;
                case "plm.items.release.withchangeorder":
                    convertedString = getItemReleasedWithChangeOrderString(messageString, as);
                    break;
                case "plm.items.release.withoutchangeorder":
                    convertedString = getItemReleasedWithoutChangeOrderString(messageString, as);
                    break;
                case "plm.items.status.withchangeorder":
                    convertedString = getItemStatusWithChangeOrderString(messageString, as);
                    break;
                case "plm.items.files.add":
                    convertedString = getItemFilesAddedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.files.delete":
                    convertedString = getItemFilesDeletedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.files.version":
                    convertedString = getItemFilesVersionedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.files.rename":
                    convertedString = getItemFileRenamedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.files.replace":
                    convertedString = getItemFileRenamedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.files.lock":
                    convertedString = getItemFileString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.files.unlock":
                    convertedString = getItemFileString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.subscribe":
                    convertedString = getItemSubscribeString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.unsubscribe":
                    convertedString = getItemSubscribeString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.files.folders.add":
                    convertedString = getItemFoldersAddedOrDeletedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.files.folders.delete":
                    convertedString = getItemFoldersAddedOrDeletedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.files.download":
                    convertedString = getItemFileString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.workflow.start":
                    convertedString = getItemWorkflowStartString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.workflow.finish":
                    convertedString = getItemWorkflowFinishString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.workflow.promote":
                    convertedString = getItemWorkflowPromoteDemoteString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.workflow.demote":
                    convertedString = getItemWorkflowPromoteDemoteString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.workflow.hold":
                    convertedString = getItemWorkflowHoldUnholdString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.workflow.unhold":
                    convertedString = getItemWorkflowHoldUnholdString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.workflow.add":
                    convertedString = getItemWorkflowAddedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.workflow.change":
                    convertedString = getItemWorkflowChangeString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.comment":
                    convertedString = getItemCommentString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.bom.add":
                    convertedString = getItemBomItemAddedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.bom.update":
                    convertedString = getBomItemUpdatedString(messageString, actor, item, itemRevision, as);
                    break;
                case "plm.items.bom.delete":
                    convertedString = getItemBomItemDeletedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.parts.add":
                    convertedString = getMfrPartsAddedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.parts.delete":
                    convertedString = getMfrPartDeletedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.relateditems.add":
                    convertedString = getRelatedItemsAddedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.relateditems.delete":
                    convertedString = getRelatedItemDeletedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.alternateParts.add":
                    convertedString = getAlternatePartsAddedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.alternateParts.delete":
                    convertedString = getAlternatePartsDeletedString(messageString, item, itemRevision, as);
                    break;
                 case "plm.items.alternateParts.update":
                    convertedString = getAlternatePartsUpdatedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.instances.create":
                    convertedString = getInstancesCreatedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.instances.delete":
                    convertedString = getInstanceDeletedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.bom.configuration.configurableitems":
                    convertedString = getBomConfigItemInclusionAddedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.bom.configuration.nonconfigurableitems":
                    convertedString = getBomNonConfigItemInclusionAddedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.bom.configuration.attributes":
                    convertedString = getBomConfigAttributeExclusionsString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.bom.configuration.create":
                    convertedString = getBomConfigurationAddedOrDeletedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.bom.configuration.update":
                    convertedString = getBomConfigurationUpdatedString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.bom.resolve":
                    convertedString = getBomItemResolvedString(messageString, actor, item, itemRevision, as);
                    break;
                case "plm.items.lifeCyclePhase.promote":
                    convertedString = getItemPromoteDemoteString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.lifeCyclePhase.demote":
                    convertedString = getItemPromoteDemoteString(messageString, item, itemRevision, as);
                    break;
                case "plm.items.incorporate":
                    convertedString = getItemIncorporateString(messageString, as);
                    break;
                case "plm.items.unincorporate":
                    convertedString = getItemIncorporateString(messageString, as);
                    break;
                case "plm.items.specifications.add":
                    convertedString = getItemSpectificationAddedString(messageString, item, as);
                    break;
                case "plm.items.specifications.delete":
                    convertedString = getItemSpecificationDeletedString(messageString, item, as);
                    break;        
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getItemCreatedString(String messageString, Person actor, PLMItem item) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), item.getItemNumber());
    }

    private String getItemRevisionCreatedString(String messageString, Person actor, PLMItem item, PLMItemRevision itemRevision) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), item.getItemNumber(),
                itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());
    }

    private String getItemReleasedWithChangeOrderString(String messageString, ActivityStream as) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(as.getObject().getObject());
        PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());
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
                plmItem.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase(), highlightValue(changeNumber));
    }

    private String getItemStatusWithChangeOrderString(String messageString, ActivityStream as) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(as.getObject().getObject());
        PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());
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
                    plmItem.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase(), highlightValue(statusChangeDto.getFromStatus()), highlightValue(statusChangeDto.getToStatus()), highlightValue(changeNumber));
        } else {
            return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    plmItem.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase(), highlightValue(changeNumber));
        }
    }

    private String getItemReleasedWithoutChangeOrderString(String messageString, ActivityStream as) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(as.getObject().getObject());
        PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                plmItem.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());
    }

    private String getItemIncorporateString(String messageString, ActivityStream as) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(as.getObject().getObject());
        PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                plmItem.getItemNumber(), itemRevision.getRevision(), itemRevision.getLifeCyclePhase().getPhase());
    }

    private String getItemBasicInfoUpdatedJson(PLMItem plmOldItem, PLMItem plmItem) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = plmOldItem.getItemName();
        String newValue = plmItem.getItemName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = plmOldItem.getDescription();
        newValue = plmItem.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        MakeOrBuy oldMakeOrBuy = plmOldItem.getMakeOrBuy();
        MakeOrBuy newMakeOrBuy = plmItem.getMakeOrBuy();
        if (!oldMakeOrBuy.equals(newMakeOrBuy)) {
            changes.add(new ASPropertyChangeDTO("Make / Buy", oldMakeOrBuy.toString(), newMakeOrBuy.toString()));
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

    private String getItemEffectiveDatesUpdatedJson(PLMItem item, PLMItemRevision oldRevision, PLMItemRevision itemRevision) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        Date oldDate = oldRevision.getEffectiveFrom();
        Date newDate = itemRevision.getEffectiveFrom();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (oldDate == null && newDate != null) {
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Effective From", "", newDateValue));
        } else if (oldDate != null && newDate != null) {
            if (!oldDate.equals(newDate)) {
                String oldDateValue = dateFormat.format(oldDate);
                String newDateValue = dateFormat.format(newDate);
                changes.add(new ASPropertyChangeDTO("Effective From", oldDateValue, newDateValue));
            }
        } else if (oldDate != null) {
            String oldDateValue = dateFormat.format(oldDate);
            changes.add(new ASPropertyChangeDTO("Effective From", oldDateValue, ""));
        }


        oldDate = oldRevision.getEffectiveTo();
        newDate = itemRevision.getEffectiveTo();


        if (oldDate == null && newDate != null) {
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Effective To", "", newDateValue));
        } else if (oldDate != null && newDate != null) {
            if (!oldDate.equals(newDate)) {
                String oldDateValue = dateFormat.format(oldDate);
                String newDateValue = dateFormat.format(newDate);
                changes.add(new ASPropertyChangeDTO("Effective To", oldDateValue, newDateValue));
            }
        } else if (oldDate != null) {
            String oldDateValue = dateFormat.format(oldDate);
            changes.add(new ASPropertyChangeDTO("Effective To", oldDateValue, ""));
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

    private String getItemBasicInfoUpdatedString(String messageString, Person actor, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), plmItem.getItemNumber(),
                plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.items.update.basicinfo.property");

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

    private String getBomItemResolvedString(String messageString, Person actor, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), plmItem.getItemNumber(),
                plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.items.bom.resolve.item");

        String json = as.getData();
        try {
            ASBomResolve bomResolve = objectMapper.readValue(json, new TypeReference<ASBomResolve>() {
            });
            String s = addMarginToMessage(MessageFormat.format(updateString,
                    highlightValue(bomResolve.getOldName()), highlightValue(bomResolve.getOldRevision()),
                    highlightValue(bomResolve.getOldLifecycle()), highlightValue(bomResolve.getNewName()),
                    highlightValue(bomResolve.getNewRevision()), highlightValue(bomResolve.getNewLifecycle())));
            sb.append(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getItemAttributesUpdatedJson(PLMItemAttribute oldAttribute, PLMItemAttribute newAttribute) {
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

    private String getItemAttributeUpdatedString(String messageString, Person actor, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), plmItem.getItemNumber(),
                plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.items.update.attributes.attribute");

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

    private String getItemCopiedString(String messageString, ActivityStream as) {
        ActivityStreamObject object = as.getObject();
        ActivityStreamObject source = as.getSource();

        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(object.getObject());
        PLMItemRevision sourcePlmItemRevision = itemRevisionRepository.findOne(source.getObject());

        PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
        PLMItem sourcePlmItem = itemRepository.findOne(sourcePlmItemRevision.getItemMaster());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(), plmItem.getItemNumber(), highlightValue(sourcePlmItem.getItemNumber()));
    }

    private String getItemFilesAddedJson(List<PLMItemFile> files) {
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

    private String getItemFilesAddedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), plmItem.getItemNumber(),
                plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.items.files.add.file");

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

    private String getItemFilesDeletedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = "";

        String json = as.getData();
        try {
            ASNewFileDTO asNewFileDTO = objectMapper.readValue(json, new TypeReference<ASNewFileDTO>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(asNewFileDTO.getName()),
                    plmItem.getItemNumber(), plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return activityString;
    }


    private String getItemFilesVersionedJson(List<PLMItemFile> files) {
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

    private String getItemFilesVersionedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), plmItem.getItemNumber(),
                plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.items.files.version.file");

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

    private String getItemFileRenamedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    plmItem.getItemNumber(),
                    plmItemRevision.getRevision(),
                    plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getItemFileString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    plmItem.getItemNumber(),
                    plmItemRevision.getRevision(),
                    plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getItemSubscribeString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        try {
            ASNewPRProblemItem problemItem = objectMapper.readValue(as.getData(), new TypeReference<ASNewPRProblemItem>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    problemItem.getItemNumber(),
                    problemItem.getRevision(),
                    problemItem.getLifecyclePhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getItemWorkflowStartString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());
    }

    private String getItemWorkflowFinishString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());
    }

    private String getItemWorkflowPromoteDemoteString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getItemWorkflowHoldUnholdString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());
    }

    private String getItemWorkflowAddedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    plmItem.getItemNumber(),
                    plmItemRevision.getRevision(),
                    plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getItemWorkflowChangeString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    plmItem.getItemNumber(),
                    plmItemRevision.getRevision(),
                    plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getItemCommentString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getItemBomItemAddedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());
        StringBuffer sb = new StringBuffer();
        sb.append(message);

        String itemString = activityStreamResourceBundle.getString("plm.items.bom.item");

        String json = as.getData();
        try {
            List<ASBomItem> items = objectMapper.readValue(as.getData(), new TypeReference<List<ASBomItem>>() {
            });
            for (ASBomItem item : items) {
                String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(item.getNumber()),
                        item.getQuantity(), item.getRefdes(), item.getNotes()));
                sb.append(s);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getBomItemUpdatedString(String messageString, Person actor, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String updateString = activityStreamResourceBundle.getString("plm.items.bom.update.property");
        StringBuffer sb = new StringBuffer();
        String json = as.getData();
        try {
            List<ASBomItemUpdate> propChanges = objectMapper.readValue(json, new TypeReference<List<ASBomItemUpdate>>() {
            });
            String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(propChanges.get(0).getNumber()), plmItem.getItemNumber(),
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

    private String getItemBomItemDeletedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String message = "";
        try {
            ASBomItem item = objectMapper.readValue(as.getData(), new TypeReference<ASBomItem>() {
            });
            message = MessageFormat.format(messageString, as.getActor().getFullName().trim(), highlightValue(item.getNumber()),
                    plmItem.getItemNumber(),
                    plmItemRevision.getRevision(),
                    plmItemRevision.getLifeCyclePhase().getPhase());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return message;
    }

    private String getBomItemUpdatedJson(PLMBom oldBomItem, PLMBom bomItem) {
        List<ASBomItemUpdate> changes = new ArrayList<>();

        String oldValue = oldBomItem.getQuantity().toString();
        String newValue = bomItem.getQuantity().toString();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASBomItemUpdate(bomItem.getItem().getItemNumber(), "Quantity", oldValue, newValue));
        }

        oldValue = oldBomItem.getRefdes();
        newValue = bomItem.getRefdes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASBomItemUpdate(bomItem.getItem().getItemNumber(), "Reference Designator", oldValue, newValue));
        }

        oldValue = oldBomItem.getNotes();
        newValue = bomItem.getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!oldValue.equals(newValue)) {
            changes.add(new ASBomItemUpdate(bomItem.getItem().getItemNumber(), "Notes", oldValue, newValue));
        }

        Date oldDate = oldBomItem.getEffectiveFrom();
        Date newDate = bomItem.getEffectiveFrom();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (oldDate == null && newDate != null) {
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASBomItemUpdate(bomItem.getItem().getItemNumber(), "Effective From", "", newDateValue));
        } else if (oldDate != null && newDate != null) {
            if (!oldDate.equals(newDate)) {
                String oldDateValue = dateFormat.format(oldDate);
                String newDateValue = dateFormat.format(newDate);
                changes.add(new ASBomItemUpdate(bomItem.getItem().getItemNumber(), "Effective From", oldDateValue, newDateValue));
            }
        } else if (oldDate != null) {
            String oldDateValue = dateFormat.format(oldDate);
            changes.add(new ASBomItemUpdate(bomItem.getItem().getItemNumber(), "Effective From", oldDateValue, ""));
        }


        oldDate = oldBomItem.getEffectiveTo();
        newDate = bomItem.getEffectiveTo();


        if (oldDate == null && newDate != null) {
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASBomItemUpdate(bomItem.getItem().getItemNumber(), "Effective To", "", newDateValue));
        } else if (oldDate != null && newDate != null) {
            if (!oldDate.equals(newDate)) {
                String oldDateValue = dateFormat.format(oldDate);
                String newDateValue = dateFormat.format(newDate);
                changes.add(new ASBomItemUpdate(bomItem.getItem().getItemNumber(), "Effective To", oldDateValue, newDateValue));
            }
        } else if (oldDate != null) {
            String oldDateValue = dateFormat.format(oldDate);
            changes.add(new ASBomItemUpdate(bomItem.getItem().getItemNumber(), "Effective To", oldDateValue, ""));
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


    private String getItemMfrPartsAddedJson(List<PLMItemManufacturerPart> manufacturerParts) {
        List<ASNewNCRProblemItem> asNewNCRProblemItems = new ArrayList<>();
        for (PLMItemManufacturerPart manufacturerPart : manufacturerParts) {
            ASNewNCRProblemItem asNewNCRProblemItem = new ASNewNCRProblemItem(manufacturerPart.getManufacturerPart().getId(), manufacturerPart.getManufacturerPart().getPartName(),
                    manufacturerPart.getManufacturerPart().getPartNumber(), manufacturerPart.getManufacturerPart().getMfrPartType().getName());
            asNewNCRProblemItems.add(asNewNCRProblemItem);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewNCRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getMfrPartsAddedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.items.parts.add.part");

        String json = as.getData();
        try {
            List<ASNewNCRProblemItem> parts = objectMapper.readValue(json, new TypeReference<List<ASNewNCRProblemItem>>() {
            });
            parts.forEach(part -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(part.getPartName()), highlightValue(part.getPartNumber()),
                        highlightValue(part.getPartType())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getMfrPartDeletedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewNCRProblemItem part = objectMapper.readValue(json, new TypeReference<ASNewNCRProblemItem>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(part.getPartNumber()),
                    plmItem.getItemNumber(), plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getRelatedItemsAddedJson(PLMItemRevision itemRevision, List<PLMRelatedItem> relatedItems) {
        List<ASNewPRProblemItem> asNewPRProblemItems = new ArrayList<>();
        for (PLMRelatedItem relatedItem : relatedItems) {
            PLMItem toItem;
            PLMItemRevision toRevision;
            if (relatedItem.getFromItem().equals(itemRevision.getId())) {
                toRevision = itemRevisionRepository.findOne(relatedItem.getToItem().getId());
                toItem = itemRepository.findOne(toRevision.getItemMaster());
            } else {
                toRevision = itemRevisionRepository.findOne(relatedItem.getFromItem());
                toItem = itemRepository.findOne(toRevision.getItemMaster());
            }
            ASNewPRProblemItem relatedItemData = new ASNewPRProblemItem(toRevision.getId(), toItem.getItemName(), toItem.getItemNumber(),
                    toRevision.getRevision(), toRevision.getLifeCyclePhase().getPhase());
            asNewPRProblemItems.add(relatedItemData);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewPRProblemItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public String getAlternatePartsAddedJson(PLMItemRevision itemRevision, List<PLMAlternatePart> parts) {
        List<PLMAlternatePart> plmAlternateParts  = new ArrayList<>();
        for (PLMAlternatePart  part : parts) {
            PLMAlternatePart  plmAlternatePart  = new PLMAlternatePart ();
            plmAlternateParts .add(plmAlternatePart);
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(plmAlternateParts);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public String getAlternatePartsDeletedJson(PLMItemRevision itemRevision, List<PLMAlternatePart> parts) {
        List<PLMAlternatePart> plmAlternateParts  = new ArrayList<>();
        for (PLMAlternatePart  part : parts) {
            PLMAlternatePart  plmAlternatePart  = new PLMAlternatePart ();
            plmAlternateParts .add(plmAlternatePart);
        }

        String json = null;
        try {
            json = objectMapper.writeValueAsString(plmAlternateParts);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    private String getAlternatePartsAddedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());
        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.items.alternateParts.add.item");

        String json = as.getData();
        try {
            List<ASNewPRProblemItem> items = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
            });
            items.forEach(item -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(item.getItemNumber()), highlightValue(item.getRevision()),
                        highlightValue(item.getLifecyclePhase())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getAlternatePartsDeletedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());
        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.items.alternateParts.delete.item");

        String json = as.getData();
        try {
            List<ASNewPRProblemItem> items = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
            });
            items.forEach(item -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(item.getItemNumber()), highlightValue(item.getRevision()),
                        highlightValue(item.getLifecyclePhase())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


 public String getAlternatePartUpdatedJson(PLMAlternatePart oldAlternatePart, PLMAlternatePart newAlternatePart) {
        List<ASMROPropertyChangeDTO> changes = new ArrayList<>();

        PLMItem item = itemRepository.findOne(newAlternatePart.getPart());
        String oldValue = oldAlternatePart.getDirection().toString();
        String newValue = newAlternatePart.getDirection().toString();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASMROPropertyChangeDTO("Direction", item.getItemNumber(),
                    oldValue, newValue));
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


private String getAlternatePartsUpdatedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
    String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
            plmItem.getItemNumber(),
            plmItemRevision.getRevision(),
            plmItemRevision.getLifeCyclePhase().getPhase());
    StringBuffer sb = new StringBuffer();
    sb.append(message);

String updateString = activityStreamResourceBundle.getString("plm.items.alternateParts.update.property");

String json = as.getData();
try {
    List<ASMROPropertyChangeDTO> propChanges = objectMapper.readValue(json,
            new TypeReference<List<ASMROPropertyChangeDTO>>() {
            });
    propChanges.forEach(p -> {
        String s = addMarginToMessage(MessageFormat.format(updateString,
                highlightValue(p.getProperty()),
                highlightValue(p.getName()),
                highlightValue(p.getOldValue()),
                highlightValue(p.getNewValue())));
        sb.append(s);
    });
} catch (JsonProcessingException e) {
    e.printStackTrace();
}

return sb.toString();
 }

    private String getRelatedItemsAddedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.items.relateditems.add.item");

        String json = as.getData();
        try {
            List<ASNewPRProblemItem> items = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
            });
            items.forEach(item -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(item.getItemNumber()), highlightValue(item.getRevision()),
                        highlightValue(item.getLifecyclePhase())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getRelatedItemDeletedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASNewPRProblemItem item = objectMapper.readValue(json, new TypeReference<ASNewPRProblemItem>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(item.getItemNumber()),
                    plmItem.getItemNumber(), plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getItemFoldersAddOrDeleteJson(PLMFile file) {
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


    private String getItemFoldersAddedOrDeletedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), plmItem.getItemNumber(),
                    plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getInstancesCreatedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.items.instances.create.instance");

        String json = as.getData();
        try {
            List<ASNewPRProblemItem> items = objectMapper.readValue(json, new TypeReference<List<ASNewPRProblemItem>>() {
            });
            items.forEach(item -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(item.getItemNumber()), highlightValue(item.getItemName()), highlightValue(item.getRevision()),
                        highlightValue(item.getLifecyclePhase())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getInstanceDeletedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = "";
        String json = as.getData();
        try {
            ASNewPRProblemItem item = objectMapper.readValue(json, new TypeReference<ASNewPRProblemItem>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(item.getItemNumber()), highlightValue(item.getItemName()),
                    plmItem.getItemNumber(), plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getBomConfigurationAddedOrDeletedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = "";
        String json = as.getData();
        try {
            ASBomConfiguration bomConfiguration = objectMapper.readValue(json, new TypeReference<ASBomConfiguration>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(bomConfiguration.getName()),
                    highlightValue(bomConfiguration.getDescription()), plmItem.getItemNumber(), plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getBomConfigurationUpdatedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String activityString = "";
        String json = as.getData();
        StringBuffer sb = new StringBuffer();
        try {
            ASBomConfiguration bomConfiguration = objectMapper.readValue(json, new TypeReference<ASBomConfiguration>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(bomConfiguration.getName()),
                    plmItem.getItemNumber(), plmItemRevision.getRevision(), plmItemRevision.getLifeCyclePhase().getPhase());

            sb.append(activityString);
            if (bomConfiguration.getOldName() != null && bomConfiguration.getName() != null && !bomConfiguration.getOldName().equals(bomConfiguration.getName())) {
                String message = activityStreamResourceBundle.getString("plm.items.bom.configuration.update.property");
                String s = addMarginToMessage(MessageFormat.format(message, highlightValue("Name"), highlightValue(bomConfiguration.getOldName()),
                        highlightValue(bomConfiguration.getName())));
                sb.append(s);
            }

            if (bomConfiguration.getOldDescription() != null && bomConfiguration.getDescription() != null && !bomConfiguration.getOldDescription().equals(bomConfiguration.getDescription())) {
                String message = activityStreamResourceBundle.getString("plm.items.bom.configuration.update.property");
                String s = addMarginToMessage(MessageFormat.format(message, highlightValue("Description"), highlightValue(bomConfiguration.getOldDescription()),
                        highlightValue(bomConfiguration.getDescription())));
                sb.append(s);
            }

            if (bomConfiguration.getValues() != null && bomConfiguration.getValues().size() > 0) {
                String message = activityStreamResourceBundle.getString("plm.items.bom.configuration.update.values");
                bomConfiguration.getValues().forEach(value -> {
                    String s = addMarginToMessage(MessageFormat.format(message, highlightValue(value.getAttribute()), highlightValue(value.getOldValue()),
                            highlightValue(value.getNewValue())));
                    sb.append(s);
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getBomConfigItemInclusionAddedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());
        StringBuffer sb = new StringBuffer();
        sb.append(message);

        String itemString = activityStreamResourceBundle.getString("plm.items.bom.configuration.configurableitems.rules");
        String included = messageSource.getMessage("included", null, "included", LocaleContextHolder.getLocale());
        String excluded = messageSource.getMessage("excluded", null, "excluded", LocaleContextHolder.getLocale());

        try {
            List<ASBOMConfigItemInclusion> configItemInclusions = objectMapper.readValue(as.getData(), new TypeReference<List<ASBOMConfigItemInclusion>>() {
            });
            for (ASBOMConfigItemInclusion configItemInclusion : configItemInclusions) {
                if (configItemInclusion.getAdded()) {
                    String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(configItemInclusion.getCombination()), included));
                    sb.append(s);
                } else {
                    String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(configItemInclusion.getCombination()), excluded));
                    sb.append(s);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getBomNonConfigItemInclusionAddedString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());
        StringBuffer sb = new StringBuffer();
        sb.append(message);

        String itemString = activityStreamResourceBundle.getString("plm.items.bom.configuration.nonconfigurableitems.rules");
        String included = messageSource.getMessage("included", null, "included", LocaleContextHolder.getLocale());
        String excluded = messageSource.getMessage("excluded", null, "excluded", LocaleContextHolder.getLocale());
        try {
            List<ASBOMConfigItemInclusion> configItemInclusions = objectMapper.readValue(as.getData(), new TypeReference<List<ASBOMConfigItemInclusion>>() {
            });
            for (ASBOMConfigItemInclusion configItemInclusion : configItemInclusions) {
                if (configItemInclusion.getAdded()) {
                    String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(configItemInclusion.getCombination()), included));
                    sb.append(s);
                } else {
                    String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(configItemInclusion.getCombination()), excluded));
                    sb.append(s);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getBomConfigAttributeExclusionsString(String messageString, PLMItem plmItem, PLMItemRevision plmItemRevision, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                plmItem.getItemNumber(),
                plmItemRevision.getRevision(),
                plmItemRevision.getLifeCyclePhase().getPhase());
        StringBuffer sb = new StringBuffer();
        sb.append(message);

        String itemString = activityStreamResourceBundle.getString("plm.items.bom.configuration.attributes.rules");
        String included = messageSource.getMessage("included", null, "included", LocaleContextHolder.getLocale());
        String excluded = messageSource.getMessage("excluded", null, "excluded", LocaleContextHolder.getLocale());
        try {
            List<ASBOMConfigItemInclusion> configItemInclusions = objectMapper.readValue(as.getData(), new TypeReference<List<ASBOMConfigItemInclusion>>() {
            });
            for (ASBOMConfigItemInclusion configItemInclusion : configItemInclusions) {
                if (configItemInclusion.getAdded()) {
                    String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(configItemInclusion.getCombination()), excluded));
                    sb.append(s);
                } else {
                    String s = addMarginToMessage(MessageFormat.format(itemString, highlightValue(configItemInclusion.getCombination()), included));
                    sb.append(s);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getItemPromoteDemoteString(String messageString, PLMItem item, PLMItemRevision itemRevision, ActivityStream as) {
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMLifeCyclePhase fromLifeCyclePhase = lifeCyclePhaseRepository.findOne(source.getObject());
        PLMLifeCyclePhase toLifeCyclePhase = lifeCyclePhaseRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(item.getItemNumber()),
                highlightValue(itemRevision.getRevision()),
                highlightValue(fromLifeCyclePhase.getPhase()),
                highlightValue(toLifeCyclePhase.getPhase()));
    }

    private String getItemSpecificationAddedJson(List<PGCItemSpecification> itemSpecifications, Integer item) {
        List<ASItemSpecification> asItemSpecifications = new ArrayList<>();
        itemSpecifications.forEach(itemSpecification -> {
            ASItemSpecification asItemSpecification = new ASItemSpecification(itemSpecification.getSpecification().getName(), itemSpecification.getSpecification().getNumber());
            asItemSpecifications.add(asItemSpecification);
        });
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asItemSpecifications);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getItemSpectificationAddedString(String messageString, PLMItem item, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), item.getItemName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.items.specifications.add.specification");

        String json = as.getData();
        try {
            List<ASItemSpecification> itemSpecifications = objectMapper.readValue(json, new TypeReference<List<ASItemSpecification>>() {
            });
            itemSpecifications.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getNumber()), highlightValue(f.getName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getItemSpecificationDeletedString(String messageString, PLMItem item, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            ASItemSpecification itemSpecification = objectMapper.readValue(json, new TypeReference<ASItemSpecification>() {
            });
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(itemSpecification.getNumber() + " - " + itemSpecification.getName()), highlightValue(item.getItemName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }
}
