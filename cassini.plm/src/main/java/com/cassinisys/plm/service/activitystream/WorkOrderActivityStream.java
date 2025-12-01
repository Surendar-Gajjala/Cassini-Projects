package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.WorkOrderEvents;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.pqm.PQMQualityTypeAttribute;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.mro.MROWorkOrderRepository;
import com.cassinisys.plm.repo.pqm.QualityTypeAttributeRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.activitystream.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class WorkOrderActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private MROWorkOrderRepository mroWorkOrderRepository;

    @Async
    @EventListener
    public void workOrderCreated(WorkOrderEvents.WorkOrderCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkOrder().getId());
        object.setType("mroworkorder");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workOrderBasicInfoUpdated(WorkOrderEvents.WorkOrderBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MROWorkOrder oldPlan = event.getOldWorkOrder();
        MROWorkOrder newPlan = event.getNewWorkOrder();

        object.setObject(newPlan.getId());
        object.setType("mroworkorder");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getWorkOrderBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void workOrderAttributesUpdated(WorkOrderEvents.WorkOrderAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MROObject workOrder = event.getWorkOrder();

        MROObjectAttribute oldAtt = event.getOldAttribute();
        MROObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(workOrder.getId());
        object.setType("mroworkorder");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getWorkOrderAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void workOrderCommentAdded(WorkOrderEvents.WorkOrderCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkOrder().getId());
        object.setType("mroworkorder");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.mro.mroworkorder";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MROWorkOrder workOrder = mroWorkOrderRepository.findOne(object.getObject());

            if (workOrder == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mro.mroworkorder.create":
                    convertedString = getWorkOrderCreatedString(messageString, actor, workOrder);
                    break;
                case "plm.mro.mroworkorder.update.basicinfo":
                    convertedString = getWorkOrderBasicInfoUpdatedString(messageString, actor, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.update.attributes":
                    convertedString = getWorkOrderAttributeUpdatedString(messageString, actor, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.operations.update":
                    convertedString = getWorkOrderOperationUpdatedString(messageString, actor, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.spareparts.create":
                    convertedString = getWorkOrderSparePartCreatedString(messageString, actor, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.spareparts.update":
                    convertedString = getWorkOrderSparePartUpdatedString(messageString, actor, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.spareparts.delete":
                    convertedString = getWorkOrderSparePartDeletedString(messageString, actor, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.files.add":
                    convertedString = getWorkOrderFilesAddedString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.files.delete":
                    convertedString = getWorkOrderFileDeletedString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.files.folders.add":
                    convertedString = getWorkOrderFoldersAddedString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.files.folders.delete":
                    convertedString = getWorkOrderFoldersDeletedString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.files.version":
                    convertedString = getWorkOrderFilesVersionedString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.files.rename":
                    convertedString = getWorkOrderFileRenamedString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.files.replace":
                    convertedString = getWorkOrderFileRenamedString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.files.lock":
                    convertedString = getWorkOrderFileString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.files.unlock":
                    convertedString = getWorkOrderFileString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.files.download":
                    convertedString = getWorkOrderFileString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.start":
                    convertedString = getWorkOrderStartString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.finish":
                    convertedString = getWorkOrderFinishString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.hold":
                    convertedString = getWorkOrderHoldString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.unhold":
                    convertedString = getWorkOrderUnHoldString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.workflow.start":
                    convertedString = getWorkOrderWorkflowStartString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.workflow.finish":
                    convertedString = getWorkOrderWorkflowFinishString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.workflow.promote":
                    convertedString = getWorkOrderWorkflowPromoteDemoteString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.workflow.demote":
                    convertedString = getWorkOrderWorkflowPromoteDemoteString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.workflow.hold":
                    convertedString = getWorkOrderWorkflowHoldUnholdString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.workflow.unhold":
                    convertedString = getWorkOrderWorkflowHoldUnholdString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.workflow.add":
                    convertedString = getWorkOrderWorkflowAddedString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.workflow.change":
                    convertedString = getWorkOrderWorkflowChangeString(messageString, workOrder, as);
                    break;
                case "plm.mro.mroworkorder.comment":
                    convertedString = getWorkOrderCommentString(messageString, workOrder, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getWorkOrderCreatedString(String messageString, Person actor, MROWorkOrder workOrder) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), workOrder.getNumber());
    }

    private String getWorkOrderRevisionCreatedString(String messageString, Person actor, MROWorkOrder workOrder) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), workOrder.getNumber());
    }

    private String getWorkOrderBasicInfoUpdatedString(String messageString, Person actor, MROWorkOrder workOrder, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), workOrder.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mroworkorder.update.basicinfo.property");

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

    private String getWorkOrderBasicInfoUpdatedJson(MROWorkOrder oldWorkOrder, MROWorkOrder workOrder) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldWorkOrder.getName();
        String newValue = workOrder.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldWorkOrder.getDescription();
        newValue = workOrder.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        oldValue = oldWorkOrder.getNotes();
        newValue = workOrder.getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Notes", oldValue, newValue));
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


    private String getWorkOrderAttributesUpdatedJson(MROObjectAttribute oldAttribute, MROObjectAttribute newAttribute) {
        List<ASAttributeChangeDTO> changes = new ArrayList<>();

        String oldValue = "";
        if (oldAttribute != null) {
            oldValue = oldAttribute.getValueAsString();
        }
        String newValue = newAttribute.getValueAsString();

        PQMQualityTypeAttribute attDef = qualityTypeAttributeRepository.findOne(newAttribute.getId().getAttributeDef());
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

    private String getWorkOrderAttributeUpdatedString(String messageString, Person actor, MROWorkOrder workOrder, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), workOrder.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mroworkorder.update.attributes.attribute");

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

    private String getWorkOrderFilesAddedString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), workOrder.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mro.mroworkorder.files.add.file");

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

    private String getWorkOrderFoldersAddedString(String messageString, MROWorkOrder workOrder, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), workOrder.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getWorkOrderFoldersDeletedString(String messageString, MROWorkOrder workOrder, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), workOrder.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getWorkOrderFileDeletedString(String messageString, MROWorkOrder workOrder, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), workOrder.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getWorkOrderFilesVersionedString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), workOrder.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mro.mroworkorder.files.version.file");

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

    private String getWorkOrderFileRenamedString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    workOrder.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getWorkOrderFileString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    workOrder.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getWorkOrderCommentString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                workOrder.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    @Async
    @EventListener
    public void workorderStarted(WorkOrderEvents.WorkOrderStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkOrder().getId());
        object.setType("mroworkorder");
        as.setObject(object);

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void workorderFinished(WorkOrderEvents.WorkOrderFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkOrder().getId());
        object.setType("mroworkorder");
        as.setObject(object);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workorderHold(WorkOrderEvents.WorkOrderHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkOrder().getId());
        object.setType("mroworkorder");
        as.setObject(object);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workorderUnhold(WorkOrderEvents.WorkOrderUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkOrder().getId());
        object.setType("mroworkorder");
        as.setObject(object);

        activityStreamService.create(as);
    }


    /*
    * WorkOrder Workflow ActivityStream
    * */
    @Async
    @EventListener
    public void workorderWorkflowStarted(WorkOrderEvents.WorkOrderWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkOrder().getId());
        object.setType("mroworkorder");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workorderWorkflowFinished(WorkOrderEvents.WorkOrderWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkOrder().getId());
        object.setType("mroworkorder");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workorderWorkflowPromoted(WorkOrderEvents.WorkOrderWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkOrder().getId());
        object.setType("mroworkorder");
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
    public void workorderWorkflowDemoted(WorkOrderEvents.WorkOrderWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkOrder().getId());
        object.setType("mroworkorder");
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
    public void workorderWorkflowHold(WorkOrderEvents.WorkOrderWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkOrder().getId());
        object.setType("mroworkorder");
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
    public void workorderWorkflowUnhold(WorkOrderEvents.WorkOrderWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getWorkOrder().getId());
        object.setType("mroworkorder");
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
    public void workOrderOperationUpdated(WorkOrderEvents.WorkOrderOperationUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.operations.update");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan());
        object.setType("mroworkorder");
        as.setObject(object);
        as.setData(getWorkOrderOperationUpdatedJson(event.getOldWorkOrderOperation(), event.getWorkOrderOperation()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void workOrderSparePartCreated(WorkOrderEvents.WorkOrderSparePartCreatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.spareparts.create");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan());
        object.setType("mroworkorder");
        as.setObject(object);
        as.setData(getWorkOrderSparePartCreatedJson(event.getWorkOrderParts()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workOrderSparePartUpdated(WorkOrderEvents.WorkOrderSparePartUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.spareparts.update");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan());
        object.setType("mroworkorder");
        as.setObject(object);
        as.setData(getWorkOrderSparePartUpdatedJson(event.getOldWorkOrderPart(), event.getWorkOrderPart()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void workOrderSparePartDeleted(WorkOrderEvents.WorkOrderSparePartDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mroworkorder.spareparts.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan());
        object.setType("mroworkorder");
        as.setObject(object);
        as.setData(getWorkOrderSparePartDeletedJson(event.getWorkOrderPart()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    private String getWorkOrderOperationUpdatedJson(MROWorkOrderOperation oldWorkOrderOperation, MROWorkOrderOperation workOrderOperation) {
        List<ASMROPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldWorkOrderOperation.getResult().toString();
        String newValue = workOrderOperation.getResult().toString();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASMROPropertyChangeDTO("Result", workOrderOperation.getName(), oldValue, newValue));
        }
        oldValue = null;
        newValue = null;
        if (workOrderOperation.getParamType().equals(OperationParamValueType.TEXT)) {
            oldValue = oldWorkOrderOperation.getTextValue();
            newValue = workOrderOperation.getTextValue();
        } else if (workOrderOperation.getParamType().equals(OperationParamValueType.INTEGER)) {
            if (oldWorkOrderOperation.getIntegerValue() != null) {
                oldValue = oldWorkOrderOperation.getIntegerValue().toString();
            }
            if (workOrderOperation.getIntegerValue() != null) {
                newValue = workOrderOperation.getIntegerValue().toString();
            }
        } else if (workOrderOperation.getParamType().equals(OperationParamValueType.DECIMAL)) {
            if (oldWorkOrderOperation.getDecimalValue() != null) {
                oldValue = oldWorkOrderOperation.getDecimalValue().toString();
            }
            if (workOrderOperation.getDecimalValue() != null) {
                newValue = workOrderOperation.getDecimalValue().toString();
            }
        } else if (workOrderOperation.getParamType().equals(OperationParamValueType.LIST)) {
            oldValue = oldWorkOrderOperation.getListValue();
            newValue = workOrderOperation.getListValue();
        } else if (workOrderOperation.getParamType().equals(OperationParamValueType.NONE)) {
            oldValue = null;
            newValue = null;
        } else if (workOrderOperation.getParamType().equals(OperationParamValueType.BOOLEAN)) {
            if (oldWorkOrderOperation.getBooleanValue() != null) {
                oldValue = oldWorkOrderOperation.getBooleanValue().toString();
            }
            if (workOrderOperation.getBooleanValue() != null) {
                newValue = workOrderOperation.getBooleanValue().toString();
            }
        }
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASMROPropertyChangeDTO("Value", workOrderOperation.getName(), oldValue, newValue));
        }

        oldValue = oldWorkOrderOperation.getNotes();
        newValue = workOrderOperation.getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASMROPropertyChangeDTO("Notes", workOrderOperation.getName(), oldValue, newValue));
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

    private String getWorkOrderSparePartCreatedJson(List<MROWorkOrderPart> workOrderParts) {
        List<ASSparePartDTO> changes = new ArrayList<>();

        for (MROWorkOrderPart workOrderPart : workOrderParts) {
            changes.add(new ASSparePartDTO(workOrderPart.getSparePart().getName(), workOrderPart.getQuantity()));
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

    private String getWorkOrderSparePartDeletedJson(MROWorkOrderPart workOrderPart) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        changes.add(new ASPropertyChangeDTO("Name", workOrderPart.getSparePart().getName(), null));
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

    private String getWorkOrderSparePartUpdatedJson(MROWorkOrderPart oldWorkOrderPart, MROWorkOrderPart workOrderPart) {
        List<ASMROPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldWorkOrderPart.getNotes();
        String newValue = workOrderPart.getNotes();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASMROPropertyChangeDTO("Notes", workOrderPart.getSparePart().getName(), oldValue, newValue));
        }
        oldValue = oldWorkOrderPart.getDisposition().toString();
        newValue = workOrderPart.getDisposition().toString();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASMROPropertyChangeDTO("Disposition", workOrderPart.getSparePart().getName(), oldValue, newValue));
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


    private String getWorkOrderOperationUpdatedString(String messageString, Person actor, MROWorkOrder workOrder, ActivityStream as) {
        String activityString = "";

        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.mro.mroworkorder.operations.update.property");

        String json = as.getData();
        try {
            List<ASMROPropertyChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASMROPropertyChangeDTO>>() {
            });
            activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(propChanges.get(0).getName()), workOrder.getNumber());
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

    private String getWorkOrderSparePartUpdatedString(String messageString, Person actor, MROWorkOrder workOrder, ActivityStream as) {
        String activityString = "";

        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.mro.mroworkorder.spareparts.update.property");

        String json = as.getData();
        try {
            List<ASMROPropertyChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASMROPropertyChangeDTO>>() {
            });
            activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(propChanges.get(0).getName()), workOrder.getNumber());
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

    private String getWorkOrderSparePartCreatedString(String messageString, Person actor, MROWorkOrder workOrder, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), workOrder.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mroworkorder.spareparts.create.add");
        String json = as.getData();
        try {
            List<ASSparePartDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASSparePartDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(p.getName()),
                        highlightValue(p.getQuantity().toString())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getWorkOrderSparePartDeletedString(String messageString, Person actor, MROWorkOrder workOrder, ActivityStream as) {
        String json = as.getData();
        String activityString = "";
        try {
            List<ASPropertyChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASPropertyChangeDTO>>() {
            });
            activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(propChanges.get(0).getOldValue()), workOrder.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return activityString;
    }

    private String getWorkOrderStartString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        return MessageFormat.format(messageString, as.getActor().getFullName().trim(), workOrder.getNumber());
    }

    private String getWorkOrderFinishString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        return MessageFormat.format(messageString, as.getActor().getFullName().trim(), workOrder.getNumber());
    }

    private String getWorkOrderHoldString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        return MessageFormat.format(messageString, as.getActor().getFullName().trim(), workOrder.getNumber());
    }

    private String getWorkOrderUnHoldString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        return MessageFormat.format(messageString, as.getActor().getFullName().trim(), workOrder.getNumber());
    }

    private String getWorkOrderWorkflowStartString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                workOrder.getNumber());
    }

    private String getWorkOrderWorkflowFinishString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()), workOrder.getNumber());
    }

    private String getWorkOrderWorkflowPromoteDemoteString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                workOrder.getNumber(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getWorkOrderWorkflowHoldUnholdString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                workOrder.getNumber());
    }

    private String getWorkOrderWorkflowAddedString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    workOrder.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getWorkOrderWorkflowChangeString(String messageString, MROWorkOrder workOrder, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    workOrder.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

}
