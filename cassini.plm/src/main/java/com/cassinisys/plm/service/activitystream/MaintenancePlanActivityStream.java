package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.MaintenancePlanEvents;
import com.cassinisys.plm.model.mro.MROMaintenanceOperation;
import com.cassinisys.plm.model.mro.MROMaintenancePlan;
import com.cassinisys.plm.model.mro.MROObject;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.model.pqm.PQMQualityTypeAttribute;
import com.cassinisys.plm.repo.mro.MROMaintenancePlanRepository;
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
public class MaintenancePlanActivityStream extends BaseActivityStream implements ActivityStreamConverter {
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
    private MROMaintenancePlanRepository mroMaintenancePlanRepository;

    @Async
    @EventListener
    public void maintenancePlanCreated(MaintenancePlanEvents.MaintenancePlanCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan().getId());
        object.setType("mromaintenanceplan");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mromaintenanceplan.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void maintenancePlanBasicInfoUpdated(MaintenancePlanEvents.MaintenancePlanBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MROMaintenancePlan oldPlan = event.getOldMaintenancePlan();
        MROMaintenancePlan newPlan = event.getNewMaintenancePlan();

        object.setObject(newPlan.getId());
        object.setType("mromaintenanceplan");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mromaintenanceplan.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getMaintenancePlanBasicInfoUpdatedJson(oldPlan, newPlan));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void maintenancePlanAttributesUpdated(MaintenancePlanEvents.MaintenancePlanAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        MROObject maintenancePlan = event.getMaintenancePlan();

        MROObjectAttribute oldAtt = event.getOldAttribute();
        MROObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(maintenancePlan.getId());
        object.setType("mromaintenanceplan");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mromaintenanceplan.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getMaintenancePlanAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void maintenancePlanCommentAdded(MaintenancePlanEvents.MaintenancePlanCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mromaintenanceplan.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan().getId());
        object.setType("mromaintenanceplan");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void maintenancePlanOperationCreated(MaintenancePlanEvents.MaintenancePlanOperationCreatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mromaintenanceplan.operations.create");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan());
        object.setType("mromaintenanceplan");
        as.setObject(object);
        as.setData(getMaintenancePlanOperationCreatedJson(event.getMaintenanceOperations()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void maintenancePlanOperationUpdated(MaintenancePlanEvents.MaintenancePlanOperationUpdatedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mromaintenanceplan.operations.update");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan());
        object.setType("mromaintenanceplan");
        as.setObject(object);
        as.setData(getMaintenancePlanOperationUpdatedJson(event.getOldMaintenanceOperation(), event.getMaintenanceOperation()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Async
    @EventListener
    public void maintenancePlanOperationDeleted(MaintenancePlanEvents.MaintenancePlanOperationDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mromaintenanceplan.operations.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan());
        object.setType("mromaintenanceplan");
        as.setObject(object);
        as.setData(getMaintenancePlanOperationDeletedJson(event.getMaintenanceOperation()));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }

    @Override
    public String getConverterKey() {
        return "plm.mro.mromaintenanceplan";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            MROMaintenancePlan maintenancePlan = mroMaintenancePlanRepository.findOne(object.getObject());

            if (maintenancePlan == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.mro.mromaintenanceplan.create":
                    convertedString = getMaintenancePlanCreatedString(messageString, actor, maintenancePlan);
                    break;
                case "plm.mro.mromaintenanceplan.update.basicinfo":
                    convertedString = getMaintenancePlanBasicInfoUpdatedString(messageString, actor, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.update.attributes":
                    convertedString = getMaintenancePlanAttributeUpdatedString(messageString, actor, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.files.add":
                    convertedString = getMaintenancePlanFilesAddedString(messageString, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.operations.create":
                    convertedString = getMaintenanceOperationCreatedString(messageString, actor, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.operations.update":
                    convertedString = getMaintenanceOperationUpdatedString(messageString, actor, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.operations.delete":
                    convertedString = getMaintenanceOperationDeletedString(messageString, actor, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.files.delete":
                    convertedString = getMaintenancePlanFileDeletedString(messageString, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.files.folders.add":
                    convertedString = getMaintenancePlanFoldersAddedString(messageString, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.files.folders.delete":
                    convertedString = getMaintenancePlanFoldersDeletedString(messageString, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.files.version":
                    convertedString = getMaintenancePlanFilesVersionedString(messageString, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.files.rename":
                    convertedString = getMaintenancePlanFileRenamedString(messageString, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.files.replace":
                    convertedString = getMaintenancePlanFileRenamedString(messageString, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.files.lock":
                    convertedString = getMaintenancePlanFileString(messageString, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.files.unlock":
                    convertedString = getMaintenancePlanFileString(messageString, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.files.download":
                    convertedString = getMaintenancePlanFileString(messageString, maintenancePlan, as);
                    break;
                case "plm.mro.mromaintenanceplan.comment":
                    convertedString = getMaintenancePlanCommentString(messageString, maintenancePlan, as);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getMaintenancePlanCreatedString(String messageString, Person actor, MROMaintenancePlan maintenancePlan) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), maintenancePlan.getNumber());
    }

    private String getMaintenancePlanBasicInfoUpdatedString(String messageString, Person actor, MROMaintenancePlan maintenancePlan, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), maintenancePlan.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mromaintenanceplan.update.basicinfo.property");

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

    private String getMaintenancePlanBasicInfoUpdatedJson(MROMaintenancePlan oldMaintenancePlan, MROMaintenancePlan maintenancePlan) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldMaintenancePlan.getName();
        String newValue = maintenancePlan.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldMaintenancePlan.getDescription();
        newValue = maintenancePlan.getDescription();
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
        try {
            if (changes.size() > 0) {
                json = objectMapper.writeValueAsString(changes);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getMaintenancePlanAttributesUpdatedJson(MROObjectAttribute oldAttribute, MROObjectAttribute newAttribute) {
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

    private String getMaintenancePlanAttributeUpdatedString(String messageString, Person actor, MROMaintenancePlan maintenancePlan, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), maintenancePlan.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.mro.mromaintenanceplan.update.attributes.attribute");

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

    private String getMaintenancePlanFilesAddedString(String messageString, MROMaintenancePlan maintenancePlan, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), maintenancePlan.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mro.mromaintenanceplan.files.add.file");

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

    private String getMaintenancePlanFoldersAddedString(String messageString, MROMaintenancePlan maintenancePlan, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), maintenancePlan.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getMaintenancePlanFoldersDeletedString(String messageString, MROMaintenancePlan maintenancePlan, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), maintenancePlan.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getMaintenancePlanFileDeletedString(String messageString, MROMaintenancePlan maintenancePlan, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), maintenancePlan.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getMaintenancePlanFilesVersionedString(String messageString, MROMaintenancePlan maintenancePlan, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), maintenancePlan.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.mro.mromaintenanceplan.files.version.file");

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

    private String getMaintenancePlanFileRenamedString(String messageString, MROMaintenancePlan maintenancePlan, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    maintenancePlan.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getMaintenancePlanFileString(String messageString, MROMaintenancePlan maintenancePlan, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    maintenancePlan.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getMaintenancePlanCommentString(String messageString, MROMaintenancePlan maintenancePlan, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                maintenancePlan.getNumber());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    /*
    * MaintenancePlan Workflow ActivityStream
    * */
    @Async
    @EventListener
    public void workorderWorkflowStarted(MaintenancePlanEvents.MaintenancePlanWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mromaintenanceplan.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan().getId());
        object.setType("mromaintenanceplan");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workorderWorkflowFinished(MaintenancePlanEvents.MaintenancePlanWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mromaintenanceplan.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan().getId());
        object.setType("mromaintenanceplan");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void workorderWorkflowPromoted(MaintenancePlanEvents.MaintenancePlanWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mromaintenanceplan.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan().getId());
        object.setType("mromaintenanceplan");
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
    public void workorderWorkflowDemoted(MaintenancePlanEvents.MaintenancePlanWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mromaintenanceplan.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan().getId());
        object.setType("mromaintenanceplan");
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
    public void workorderWorkflowHold(MaintenancePlanEvents.MaintenancePlanWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mromaintenanceplan.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan().getId());
        object.setType("mromaintenanceplan");
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
    public void workorderWorkflowUnhold(MaintenancePlanEvents.MaintenancePlanWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.mro.mromaintenanceplan.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getMaintenancePlan().getId());
        object.setType("mromaintenanceplan");
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


    private String getMaintenancePlanOperationUpdatedJson(MROMaintenanceOperation oldMaintenanceOperation, MROMaintenanceOperation maintenanceOperation) {
        List<ASMROPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldMaintenanceOperation.getName();
        String newValue = maintenanceOperation.getName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASMROPropertyChangeDTO("Name", maintenanceOperation.getName(), oldValue, newValue));
        }

        oldValue = oldMaintenanceOperation.getDescription();
        newValue = maintenanceOperation.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASMROPropertyChangeDTO("Description", maintenanceOperation.getName(), oldValue, newValue));
        }

        oldValue = oldMaintenanceOperation.getParamName();
        newValue = maintenanceOperation.getParamName();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASMROPropertyChangeDTO("Param Name", maintenanceOperation.getName(), oldValue, newValue));
        }

        oldValue = oldMaintenanceOperation.getParamType().toString();
        newValue = maintenanceOperation.getParamType().toString();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASMROPropertyChangeDTO("Param Type", maintenanceOperation.getName(), oldValue, newValue));
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

    private String getMaintenancePlanOperationCreatedJson(List<MROMaintenanceOperation> maintenanceOperations) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();
        for (MROMaintenanceOperation maintenanceOperation : maintenanceOperations) {
            changes.add(new ASPropertyChangeDTO("Name", maintenanceOperation.getName(), null));
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

    private String getMaintenancePlanOperationDeletedJson(MROMaintenanceOperation maintenanceOperation) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        changes.add(new ASPropertyChangeDTO("Name", maintenanceOperation.getName(), null));
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

    private String getMaintenanceOperationUpdatedString(String messageString, Person actor, MROMaintenancePlan maintenancePlan, ActivityStream as) {
        String activityString = "";

        StringBuffer sb = new StringBuffer();

        String updateString = activityStreamResourceBundle.getString("plm.mro.mromaintenanceplan.operations.update.property");

        String json = as.getData();
        try {
            List<ASMROPropertyChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASMROPropertyChangeDTO>>() {
            });
            activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(propChanges.get(0).getName()), maintenancePlan.getNumber());
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

    private String getMaintenanceOperationCreatedString(String messageString, Person actor, MROMaintenancePlan maintenancePlan, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), maintenancePlan.getNumber());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);
        String updateString = activityStreamResourceBundle.getString("plm.mro.mromaintenanceplan.operations.create.add");
        String json = as.getData();
        try {
            List<ASPropertyChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASPropertyChangeDTO>>() {
            });
            propChanges.forEach(p -> {
                String s = addMarginToMessage(MessageFormat.format(updateString,
                        highlightValue(p.getOldValue())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getMaintenanceOperationDeletedString(String messageString, Person actor, MROMaintenancePlan maintenancePlan, ActivityStream as) {
        String json = as.getData();
        String activityString = "";
        try {
            List<ASPropertyChangeDTO> propChanges = objectMapper.readValue(json, new TypeReference<List<ASPropertyChangeDTO>>() {
            });
            activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(propChanges.get(0).getOldValue()), maintenancePlan.getNumber());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return activityString;
    }


}
