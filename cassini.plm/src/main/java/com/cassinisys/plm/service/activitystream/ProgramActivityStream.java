package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamObject;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.ProgramEvents;
import com.cassinisys.plm.model.mfr.PLMSupplierAttribute;
import com.cassinisys.plm.model.mfr.PLMSupplierTypeAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.pm.ProgramProjectRepository;
import com.cassinisys.plm.repo.pm.ProgramRepository;
import com.cassinisys.plm.repo.pm.ProjectRepository;
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
import java.util.List;

@Component
public class ProgramActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProgramProjectRepository programProjectRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Async
    @EventListener
    public void programCreated(ProgramEvents.ProgramCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram().getId());
        object.setType("program");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programBasicInfoUpdated(ProgramEvents.ProgramBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMProgram oldProject = event.getOldProgram();
        PLMProgram newProject = event.getNewProgram();

        object.setObject(newProject.getId());
        object.setType("program");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getProgramBasicInfoUpdatedJson(oldProject, newProject));
        if (as.getData() != null) activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programFilesAdded(ProgramEvents.ProgramFilesAddedEvent event) {
        List<PLMProgramFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram());
        object.setType("program");
        as.setObject(object);
        as.setData(getProgramFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programFoldersAdded(ProgramEvents.ProgramFoldersAddedEvent event) {
        PLMProgramFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram());
        object.setType("program");
        as.setObject(object);
        as.setData(getProgramFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programFoldersDeleted(ProgramEvents.ProgramFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram());
        object.setType("program");
        as.setObject(object);
        as.setData(getProgramFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programFileDeleted(ProgramEvents.ProgramFileDeletedEvent event) {
        PLMProgramFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram());
        object.setType("program");
        as.setObject(object);
        as.setData(getProgramFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programFilesVersioned(ProgramEvents.ProgramFilesVersionedEvent event) {
        List<PLMProgramFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram());
        object.setType("program");
        as.setObject(object);
        as.setData(getProgramFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programFileRenamed(ProgramEvents.ProgramFileRenamedEvent event) {
        PLMProgramFile oldFile = event.getOldFile();
        PLMProgramFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.program.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.program.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram());
        object.setType("program");
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
    public void programFileLocked(ProgramEvents.ProgramFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram());
        object.setType("program");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getProjectFile().getId(), event.getProjectFile().getName(), FileUtils.byteCountToDisplaySize(event.getProjectFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programFileUnlocked(ProgramEvents.ProgramFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram());
        object.setType("program");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getProjectFile().getId(), event.getProjectFile().getName(), FileUtils.byteCountToDisplaySize(event.getProjectFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programFileDownloaded(ProgramEvents.ProgramFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram());
        object.setType("program");
        as.setObject(object);

        ASNewFileDTO asNewFileDTO = new ASNewFileDTO(event.getProjectFile().getId(), event.getProjectFile().getName(), FileUtils.byteCountToDisplaySize(event.getProjectFile().getSize()));
        try {
            as.setData(objectMapper.writeValueAsString(asNewFileDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programWorkflowStarted(ProgramEvents.ProgramWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram().getId());
        object.setType("program");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programWorkflowFinished(ProgramEvents.ProgramWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram().getId());
        object.setType("program");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programWorkflowPromoted(ProgramEvents.ProgramWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram().getId());
        object.setType("program");
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
    public void programWorkflowDemoted(ProgramEvents.ProgramWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram().getId());
        object.setType("program");
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
    public void programWorkflowHold(ProgramEvents.ProgramWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram().getId());
        object.setType("program");
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
    public void programWorkflowUnhold(ProgramEvents.ProgramWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram().getId());
        object.setType("program");
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
    public void manufacturerPartWorkflowChange(ProgramEvents.ProgramWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram().getId());
        object.setType("program");
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
            as.setActivity("plm.program.workflow.change");
        } else {
            as.setActivity("plm.program.workflow.add");
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
    public void programActivityWorkflowChange(ProgramEvents.ProgramActivityWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram().getId());
        object.setType("program");
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
            as.setActivity("plm.program.activities.workflow.change");
        } else {
            as.setActivity("plm.program.activities.workflow.add");
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

        ASPropertyChangeDTO asPropertyChangeDTO = new ASPropertyChangeDTO(event.getActivity().getName(), oldWorkflowName, newWorkflowName);

        try {
            as.setData(objectMapper.writeValueAsString(asPropertyChangeDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programCommentAdded(ProgramEvents.ProgramCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram().getId());
        object.setType("program");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programMembersAdded(ProgramEvents.ProgramMembersAddedEvent event) {
        List<PLMProgramResource> persons = event.getPersons();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.resources.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram());
        object.setType("program");
        as.setObject(object);
        as.setData(getProgramMembersAddedJson(persons));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programMemberDeleted(ProgramEvents.ProgramMemberDeletedEvent event) {
        PLMProgramResource programResource = event.getPerson();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.resources.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram());
        object.setType("program");
        as.setObject(object);
        as.setData(getProgramMembersDeletedJson(programResource));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programProjectAdded(ProgramEvents.ProgramProjectAddedEvent event) {
        PLMProgramProject programProject = event.getProgramProject();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.projects.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram());
        object.setType("program");
        as.setObject(object);
        as.setData(getProgramProjectAddedJson(programProject));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programProjectDeleted(ProgramEvents.ProgramProjectDeletedEvent event) {
        PLMProgramProject programProject = event.getProgramProject();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.projects.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram());
        object.setType("program");
        as.setObject(object);
        as.setData(getProgramProjectDeletedJson(programProject));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programProjectUpdated(ProgramEvents.ProgramProjectUpdateEvent event) {

        ActivityStreamObject object = new ActivityStreamObject();
        PLMProgramProject oldProgramProject = event.getOldProgramProject();
        PLMProgramProject newProgramProject = event.getNewProgramProject();

        object.setObject(newProgramProject.getProgram());
        object.setType("program");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.projects.update");
        as.setObject(object);
        as.setConverter(getConverterKey());

        as.setData(getProgramProjectUpdateJson(oldProgramProject, newProgramProject));

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void supplierAttributesUpdated(ProgramEvents.ProgramAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        ObjectAttribute oldAtt = event.getOldAttribute();
        ObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(event.getProgram());
        object.setType("program");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getProgramAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    private String getProgramAttributesUpdatedJson(ObjectAttribute oldAttribute,
                                                   ObjectAttribute newAttribute) {
        List<ASAttributeChangeDTO> changes = new ArrayList<>();

        String oldValue = "";
        if (oldAttribute != null) {
            oldValue = oldAttribute.getValueAsString();
        }
        String newValue = newAttribute.getValueAsString();

        ObjectTypeAttribute attDef = objectTypeAttributeRepository
                .findOne(newAttribute.getId().getAttributeDef());
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


    @Async
    @EventListener
    public void programubscribed(ProgramEvents.ProgramSubscribeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.subscribe");
        as.setConverter(getConverterKey());
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram().getId());
        object.setType("program");
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programMemberUpdated(ProgramEvents.ProgramMemberUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMProgramResource oldValue = event.getOldResource();
        PLMProgramResource newValue = event.getNewResource();

        object.setObject(newValue.getProgram());
        object.setType("program");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.resources.update");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getProgramMemberUpdatedStringJSON(oldValue, newValue));
        if (as.getData() != null) activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void programUnSubscribed(ProgramEvents.ProgramUnSubscribeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.program.unsubscribe");
        as.setConverter(getConverterKey());
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProgram().getId());
        object.setType("program");
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.program";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMProgram project = programRepository.findOne(object.getObject());

            if (project == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.program.create":
                    convertedString = getProgramCreatedString(messageString, actor, project);
                    break;
                case "plm.program.update.basicinfo":
                    convertedString = getProgramBasicInfoUpdatedString(messageString, actor, project, as);
                    break;
                case "plm.program.update.attributes":
                    convertedString = getProgramAttributeUpdatedString(messageString, actor, project, as);
                    break;
                case "plm.program.files.add":
                    convertedString = getProgramFilesAddedString(messageString, project, as);
                    break;
                case "plm.program.files.delete":
                    convertedString = getProgramFileDeletedString(messageString, project, as);
                    break;
                case "plm.program.files.folders.add":
                    convertedString = getProgramFoldersAddedString(messageString, project, as);
                    break;
                case "plm.program.files.folders.delete":
                    convertedString = getProgramFoldersDeletedString(messageString, project, as);
                    break;
                case "plm.program.files.version":
                    convertedString = getProgramFilesVersionedString(messageString, project, as);
                    break;
                case "plm.program.files.rename":
                    convertedString = getProgramFileRenamedString(messageString, project, as);
                    break;
                case "plm.program.files.replace":
                    convertedString = getProgramFileRenamedString(messageString, project, as);
                    break;
                case "plm.program.files.lock":
                    convertedString = getProgramFileString(messageString, project, as);
                    break;
                case "plm.program.files.unlock":
                    convertedString = getProgramFileString(messageString, project, as);
                    break;
                case "plm.program.files.download":
                    convertedString = getProgramFileString(messageString, project, as);
                    break;
                case "plm.program.workflow.start":
                    convertedString = getProgramWorkflowStartString(messageString, project, as);
                    break;
                case "plm.program.workflow.finish":
                    convertedString = getProgramWorkflowFinishString(messageString, project, as);
                    break;
                case "plm.program.workflow.promote":
                    convertedString = getProgramWorkflowPromoteDemoteString(messageString, project, as);
                    break;
                case "plm.program.workflow.demote":
                    convertedString = getProgramWorkflowPromoteDemoteString(messageString, project, as);
                    break;
                case "plm.program.workflow.hold":
                    convertedString = getProgramWorkflowHoldUnholdString(messageString, project, as);
                    break;
                case "plm.program.workflow.unhold":
                    convertedString = getProgramWorkflowHoldUnholdString(messageString, project, as);
                    break;
                case "plm.program.workflow.add":
                    convertedString = getProgramWorkflowAddedString(messageString, project, as);
                    break;
                case "plm.program.workflow.change":
                    convertedString = getProgramWorkflowChangeString(messageString, project, as);
                    break;
                case "plm.program.activities.workflow.add":
                    convertedString = getProgramActivityWorkflowAddedString(messageString, project, as);
                    break;
                case "plm.program.activities.workflow.change":
                    convertedString = getProgramActivityWorkflowChangeString(messageString, project, as);
                    break;
                case "plm.program.comment":
                    convertedString = getProgramCommentString(messageString, project, as);
                    break;
                case "plm.program.resources.add":
                    convertedString = getProgramMembersAddedString(messageString, project, as);
                    break;
                case "plm.program.resources.delete":
                    convertedString = getProgramMemberDeletedString(messageString, project, as);
                    break;
                case "plm.program.resources.update":
                    convertedString = getProgramMemberUpdatedString(messageString, actor, project, as);
                    break;
                case "plm.program.projects.add":
                    convertedString = getProgramProjectAddedDeletedString(messageString, project, as);
                    break;
                case "plm.program.projects.delete":
                    convertedString = getProgramProjectAddedDeletedString(messageString, project, as);
                    break;
                case "plm.program.projects.update":
                    convertedString = getProgramProjectUpdatedString(messageString, actor, project, as);
                    break;
                case "plm.program.subscribe":
                    convertedString = getProgramCreatedString(messageString, actor, project);
                    break;
                case "plm.program.unsubscribe":
                    convertedString = getProgramCreatedString(messageString, actor, project);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }


    private String getProgramAttributeUpdatedString(String messageString, Person actor, PLMProgram program,
                                                     ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), program.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.program.update.attributes.attribute");

        String json = as.getData();
        try {
            List<ASAttributeChangeDTO> propChanges = objectMapper.readValue(json,
                    new TypeReference<List<ASAttributeChangeDTO>>() {
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

    private String getProgramCreatedString(String messageString, Person actor, PLMProgram project) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());
    }

    private String getProgramBasicInfoUpdatedString(String messageString, Person actor, PLMProgram project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.program.update.basicinfo.property");

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

    public String getProgramFilesAddedJson(List<PLMProgramFile> files) {
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

    public String getProgramMembersAddedJson(List<PLMProgramResource> programResources) {
        String json = null;

        List<ASNewMemberDTO> asNewMemberDTOs = new ArrayList<>();
        programResources.forEach(f -> {
            Person person = personRepository.findOne(f.getPerson());
            asNewMemberDTOs.add(new ASNewMemberDTO(f.getId(), person.getFullName()));
        });

        try {
            json = objectMapper.writeValueAsString(asNewMemberDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProgramFoldersAddedJson(PLMProgramFile file) {
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

    private String getProgramFoldersDeletedJson(PLMFile file) {
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

    private String getProgramFilesAddedString(String messageString, PLMProgram project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.program.files.add.file");

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

    private String getProgramFoldersAddedString(String messageString, PLMProgram project, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), project.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getProgramFoldersDeletedString(String messageString, PLMProgram project, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), project.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }


    private String getProgramFileDeletedString(String messageString, PLMProgram project, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewFileDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewFileDTO>>() {
            });
            ASNewFileDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), project.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    public String getProgramFilesVersionedJson(List<PLMProgramFile> files) {
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

    private String getProgramFilesVersionedString(String messageString, PLMProgram project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.program.files.version.file");

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

    private String getProgramFileRenamedString(String messageString, PLMProgram project, ActivityStream as) {
        try {
            ASFileReplaceDto asFileReplaceDto = objectMapper.readValue(as.getData(), new TypeReference<ASFileReplaceDto>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(asFileReplaceDto.getOldFileName()),
                    highlightValue(asFileReplaceDto.getNewFileName()),
                    project.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getProgramFileString(String messageString, PLMProgram project, ActivityStream as) {
        try {
            ASNewFileDTO newFileDTO = objectMapper.readValue(as.getData(), new TypeReference<ASNewFileDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(newFileDTO.getName()),
                    project.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return messageString;
    }

    private String getProgramWorkflowStartString(String messageString, PLMProgram project, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                project.getName());
    }

    private String getProgramWorkflowFinishString(String messageString, PLMProgram project, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                project.getName());
    }

    private String getProgramWorkflowPromoteDemoteString(String messageString, PLMProgram project, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                project.getName(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getProgramWorkflowHoldUnholdString(String messageString, PLMProgram project, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                project.getName());
    }

    private String getProgramWorkflowAddedString(String messageString, PLMProgram project, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    project.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    public String getProgramWorkflowChangeString(String messageString, PLMProgram project, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    project.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    public String getProgramActivityWorkflowAddedString(String messageString, PLMProgram project, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getNewValue()),
                    highlightValue(workflow.getProperty()),
                    project.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    public String getProgramActivityWorkflowChangeString(String messageString, PLMProgram project, ActivityStream as) {
        try {
            ASPropertyChangeDTO workflow = objectMapper.readValue(as.getData(), new TypeReference<ASPropertyChangeDTO>() {
            });
            messageString = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                    highlightValue(workflow.getOldValue()),
                    highlightValue(workflow.getNewValue()),
                    highlightValue(workflow.getProperty()),
                    project.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return messageString;
    }

    private String getProgramCommentString(String messageString, PLMProgram project, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                project.getName());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getProgramMembersAddedString(String messageString, PLMProgram project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.program.resources.add.team");

        String json = as.getData();
        try {
            List<ASNewMemberDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMemberDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getProgramMemberDeletedString(String messageString, PLMProgram project, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASNewMemberDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMemberDTO>>() {
            });
            ASNewMemberDTO fileDTO = files.get(0);
            activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), highlightValue(fileDTO.getName()), project.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getProgramProjectAddedDeletedString(String messageString, PLMProgram project, ActivityStream as) {

        String activityString = "";
        String json = as.getData();
        try {
            List<ASProgramProjectDTO> files = objectMapper.readValue(json, new TypeReference<List<ASProgramProjectDTO>>() {
            });
            ASProgramProjectDTO programProjectDTO = files.get(0);
            if (programProjectDTO.getType().equals(ProgramProjectType.PROJECT.name())) {
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), programProjectDTO.getType().toLowerCase(), highlightValue(programProjectDTO.getName()) + " in group " + highlightValue(programProjectDTO.getGroupName()), project.getName());
            } else {
                activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), programProjectDTO.getType().toLowerCase(), highlightValue(programProjectDTO.getName()), project.getName());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return activityString;
    }

    private String getProgramMembersDeletedJson(PLMProgramResource programResource) {
        String json = null;

        List<ASNewMemberDTO> ASNewMemberDtos = new ArrayList<>();
        Person person = personRepository.findOne(programResource.getPerson());
        ASNewMemberDtos.add(new ASNewMemberDTO(person.getId(), person.getFullName()));
        try {
            json = objectMapper.writeValueAsString(ASNewMemberDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProgramProjectAddedJson(PLMProgramProject programProject) {
        String json = null;

        List<ASProgramProjectDTO> asProgramProjectDTOs = new ArrayList<>();
        if (programProject.getType().equals(ProgramProjectType.PROJECT)) {
            PLMProject project = projectRepository.findOne(programProject.getProject());
            if (programProject.getParent() != null) {
                PLMProgramProject plmProgramProject = programProjectRepository.findOne(programProject.getParent());
                asProgramProjectDTOs.add(new ASProgramProjectDTO(programProject.getId(), project.getName(), programProject.getType().name(), plmProgramProject.getName()));
            } else {
                asProgramProjectDTOs.add(new ASProgramProjectDTO(programProject.getId(), project.getName(), programProject.getType().name(), ""));
            }
        } else {
            asProgramProjectDTOs.add(new ASProgramProjectDTO(programProject.getId(), programProject.getName(), programProject.getType().name(), ""));
        }
        try {
            json = objectMapper.writeValueAsString(asProgramProjectDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProgramProjectDeletedJson(PLMProgramProject programProject) {
        String json = null;

        List<ASProgramProjectDTO> asProgramProjectDTOs = new ArrayList<>();
        if (programProject.getType().equals(ProgramProjectType.PROJECT)) {
            PLMProject project = projectRepository.findOne(programProject.getProject());
            if (programProject.getParent() != null) {
                PLMProgramProject plmProgramProject = programProjectRepository.findOne(programProject.getParent());
                asProgramProjectDTOs.add(new ASProgramProjectDTO(programProject.getId(), project.getName(), programProject.getType().name(), plmProgramProject.getName()));
            } else {
                asProgramProjectDTOs.add(new ASProgramProjectDTO(programProject.getId(), project.getName(), programProject.getType().name(), ""));
            }
        } else {
            asProgramProjectDTOs.add(new ASProgramProjectDTO(programProject.getId(), programProject.getName(), programProject.getType().name(), ""));
        }
        try {
            json = objectMapper.writeValueAsString(asProgramProjectDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public String getProgramBasicInfoUpdatedJson(PLMProgram oldProject, PLMProgram newProject) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String oldValue = oldProject.getName();
        String newValue = newProject.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldProject.getDescription();
        newValue = newProject.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }
        Integer oldValue1 = oldProject.getProgramManager();
        Integer newValue1 = newProject.getProgramManager();
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Program Manager", personRepository.findOne(oldValue1).getFullName(), personRepository.findOne(newValue1).getFullName()));
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

    private String getProgramMemberUpdatedString(String messageString, Person actor, PLMProgram project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.program.resources.update.property");

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

    public String getProgramMemberUpdatedStringJSON(PLMProgramResource oldResource, PLMProgramResource newResource) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldResource.getRole();
        String newValue = newResource.getRole();
        if(oldValue == null){
            oldValue = "";
        }
        if(newValue == null){
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Role", oldValue, newValue));
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

    public String getProgramProjectUpdateJson(PLMProgramProject oldProgramProject, PLMProgramProject newProgramProject) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();

        String oldValue = oldProgramProject.getName();
        String newValue = newProgramProject.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldProgramProject.getDescription();
        newValue = newProgramProject.getDescription();
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

    private String getProgramProjectUpdatedString(String messageString, Person actor, PLMProgram project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.program.projects.update.property");

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


}
