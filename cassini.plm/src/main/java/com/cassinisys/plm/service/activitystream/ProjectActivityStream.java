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
import com.cassinisys.plm.event.ProjectEvents;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.pm.PLMActivity;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.PLMProjectFile;
import com.cassinisys.plm.model.pm.PLMProjectMember;
import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.pm.PLMWbsElement;
import com.cassinisys.plm.model.req.PLMProjectRequirementDocument;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.repo.pm.PLMActivityRepository;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Component
public class ProjectActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PLMWorkflowRepository workFlowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository; 

    @Async
    @EventListener
    public void projectCreated(ProjectEvents.ProjectCreatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.create");
        as.setConverter(getConverterKey());
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectBasicInfoUpdated(ProjectEvents.ProjectBasicInfoUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMProject oldProject = event.getOldProject();
        PLMProject newProject = event.getNewproject();

        object.setObject(newProject.getId());
        object.setType("project");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.update.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getProjectBasicInfoUpdatedJson(oldProject, newProject));
        if (as.getData() != null) activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityBasicInfoUpdated(ProjectEvents.ProjectActivitiesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMActivity oldActivity = event.getOldActivity();
        PLMActivity newActivity = event.getNewActivity();
        PLMProject project = event.getProject();

        object.setObject(project.getId());
        object.setType("project");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.activities.basicinfo");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getActivityBasicInfoUpdatedJson(oldActivity, newActivity));
        if (as.getData() != null) activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectAttributesUpdated(ProjectEvents.ProjectAttributesUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        ObjectAttribute oldAtt = event.getOldAttribute();
        ObjectAttribute newAtt = event.getNewAttribute();

        object.setObject(event.getProject());
        object.setType("project");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.update.attributes");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getProjectAttributesUpdatedJson(oldAtt, newAtt));
        if (as.getData() != null) {
            activityStreamService.create(as);
        }
    }


    private String getProjectAttributesUpdatedJson(ObjectAttribute oldAttribute,
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
    public void projectMemberUpdated(ProjectEvents.ProjectMemberUpdatedEvent event) {
        ActivityStreamObject object = new ActivityStreamObject();

        PLMProjectMember oldProjectMember = event.getOldProjectMember();
        PLMProjectMember newProjectMember = event.getNewProjectMember();

        object.setObject(newProjectMember.getProject());
        object.setType("project");

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.teams.update");
        as.setObject(object);
        as.setConverter(getConverterKey());
        as.setData(getProjectMemberUpdatedJson(oldProjectMember, newProjectMember));
        if (as.getData() != null) activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectFilesAdded(ProjectEvents.ProjectFilesAddedEvent event) {
        List<PLMProjectFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.files.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectFilesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectFoldersAdded(ProjectEvents.ProjectFoldersAddedEvent event) {
        PLMProjectFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.files.folders.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectFoldersAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectFoldersDeleted(ProjectEvents.ProjectFoldersDeletedEvent event) {
        PLMFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.files.folders.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectFileDeleted(ProjectEvents.ProjectFileDeletedEvent event) {
        PLMProjectFile files = event.getFile();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.files.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectFoldersDeletedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectFilesVersioned(ProjectEvents.ProjectFilesVersionedEvent event) {
        List<PLMProjectFile> files = event.getFiles();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.files.version");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectFilesVersionedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectFileRenamed(ProjectEvents.ProjectFileRenamedEvent event) {
        PLMProjectFile oldFile = event.getOldFile();
        PLMProjectFile newFile = event.getNewFile();

        ActivityStream as = new ActivityStream();
        if (event.getType().equals("Rename")) as.setActivity("plm.project.files.rename");
        if (event.getType().equals("Replace")) as.setActivity("plm.project.files.replace");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
    public void projectFileLocked(ProjectEvents.ProjectFileLockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.files.lock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
    public void projectFileUnlocked(ProjectEvents.ProjectFileUnlockedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.files.unlock");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
    public void projectFileDownloaded(ProjectEvents.ProjectFileDownloadedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.files.download");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
    public void projectWorkflowStarted(ProjectEvents.ProjectWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityWorkflowStarted(ProjectEvents.ActivityWorkflowStartedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.activities.workflow.start");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectWorkflowFinished(ProjectEvents.ProjectWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void activityWorkflowFinished(ProjectEvents.ActivityWorkflowFinishedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.activities.workflow.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);

        ActivityStreamObject context = new ActivityStreamObject();
        context.setObject(event.getPlmWorkflow().getId());
        context.setType("workflow");
        as.setContext(context);

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectWorkflowPromoted(ProjectEvents.ProjectWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
    public void activityWorkflowPromoted(ProjectEvents.ActivityWorkflowPromotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.activities.workflow.promote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
    public void projectWorkflowDemoted(ProjectEvents.ProjectWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
    public void activityWorkflowDemoted(ProjectEvents.ActivityWorkflowDemotedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.activities.workflow.demote");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
    public void projectWorkflowHold(ProjectEvents.ProjectWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
    public void activityWorkflowHold(ProjectEvents.ActivityWorkflowHoldEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.activities.workflow.hold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
    public void projectWorkflowUnhold(ProjectEvents.ProjectWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
    public void activityWorkflowUnhold(ProjectEvents.ActivityWorkflowUnholdEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.activities.workflow.unhold");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
    public void manufacturerPartWorkflowChange(ProjectEvents.ProjectWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
            as.setActivity("plm.project.workflow.change");
        } else {
            as.setActivity("plm.project.workflow.add");
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
    public void projectActivityWorkflowChange(ProjectEvents.ProjectActivityWorkflowChangeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
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
            as.setActivity("plm.project.activities.workflow.change");
        } else {
            as.setActivity("plm.project.activities.workflow.add");
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
    public void projectCommentAdded(ProjectEvents.ProjectCommentAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.comment");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(event.getComment().getComment());

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectMembersAdded(ProjectEvents.ProjectMembersAddedEvent event) {
        List<Person> persons = event.getPersons();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.teams.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectMembersAddedJson(persons));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectMemberAdded(ProjectEvents.ProjectMemberAddedEvent event) {


        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.teams.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("Project");
        as.setObject(object);
        as.setData(getProjectMemberAddedJson(event.getPerson(), event.getProject()));

        activityStreamService.create(as);
    }


    @Async
    @EventListener
    public void projectMemberDeleted(ProjectEvents.ProjectMemberDeletedEvent event) {
        Person person = event.getPerson();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.teams.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectMembersDeletedJson(person));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectDeliverablesAdded(ProjectEvents.ProjectDeliverablesAddedEvent event) {
        List<PLMItem> deliverables = event.getProjectDeliverables();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.deliverables.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectDeliverablesAddedJson(deliverables));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectDeliverableDeleted(ProjectEvents.ProjectDeliverableDeletedEvent event) {
        PLMItem deliverable = event.getProjectDeliverable();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.deliverables.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectDeliverableDeletedJson(deliverable));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectDeliverableFinished(ProjectEvents.ProjectDeliverableFinishedEvent event) {
        PLMItem deliverable = event.getProjectDeliverable();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.deliverables.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectDeliverableDeletedJson(deliverable));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectReferenceItemsAdded(ProjectEvents.ProjectReferenceItemsAddedEvent event) {
        List<ASNewMemberDTO> itemReferences = event.getProjectItemReferences();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.referenceitems.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectReferenceItemsAddedJson(itemReferences));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectReferenceItemDeleted(ProjectEvents.ProjectReferenceItemDeletedEvent event) {
        PLMItem itemReference = event.getProjectItemReference();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.referenceitems.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectReferenceItemDeletedJson(itemReference));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectPhasesAdded(ProjectEvents.ProjectPhasesAddedEvent event) {
        List<PLMWbsElement> files = event.getWbsElements();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.phases.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectPhasesAddedJson(files));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectActivityAdded(ProjectEvents.ProjectActivitiesAddedEvent event) {
        List<ASNewActivityAndMilestoneDTO> activities = event.getAsNewActivityAndMilestoneDTOs();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.activities.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectActivitiesAddedJson(activities));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectActivityFinished(ProjectEvents.ProjectActivityFinishedEvent event) {
        ASNewActivityAndMilestoneDTO activities = event.getAsNewActivityAndMilestoneDTOs();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.activities.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectActivityFinishedJson(activities));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectTasksAdded(ProjectEvents.ProjectTasksAddedEvent event) {
        List<ASNewTaskDTO> newTaskDTOs = event.getNewTasks();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.tasks.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectTasksAddedJson(newTaskDTOs));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectTaskFinished(ProjectEvents.ProjectTaskFinishedEvent event) {
        ASNewTaskDTO newTaskDTOs = event.getAsNewTaskDTO();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.tasks.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectTaskFinishedJson(newTaskDTOs));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectTaskPercentUpdated(ProjectEvents.ProjectTaskPercentageUpdatedEvent event) {
        ASBomItem newTaskDTOs = event.getAsNewTaskDTO();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.tasks.percent");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectTaskPercentageUpdatedJson(newTaskDTOs));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectMilestoneFinished(ProjectEvents.ProjectMilestoneFinishedEvent event) {
        ASNewActivityAndMilestoneDTO activities = event.getAsNewActivityAndMilestoneDTOs();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.milestones.finish");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectActivityFinishedJson(activities));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectMilestonesAdded(ProjectEvents.ProjectMilestonesAddedEvent event) {
        List<ASNewActivityAndMilestoneDTO> milestones = event.getAsNewActivityAndMilestoneDTOs();

        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.milestones.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectMilestonesAddedJson(milestones));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectReqDocumentsAdded(ProjectEvents.ProjectReqDocumentsAddedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.reqdocuments.add");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        as.setData(getProjectReqDocumentsAddedJson(event.getRequirementDocuments()));

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectReqDocumentDeleted(ProjectEvents.ProjectReqDocumentsDeletedEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.reqdocuments.delete");
        as.setConverter(getConverterKey());

        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        String json = null;
        ASReqDocoumentDto dto = new ASReqDocoumentDto(event.getRequirementDocument().getReqDocument().getId(), event.getRequirementDocument().getReqDocument().getName(), event.getRequirementDocument().getReqDocument().getMaster().getNumber());
        try {
            json = objectMapper.writeValueAsString(dto);
            as.setData(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectubscribed(ProjectEvents.ProjectSubscribeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.subscribe");
        as.setConverter(getConverterKey());
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Async
    @EventListener
    public void projectUnSubscribed(ProjectEvents.ProjectUnSubscribeEvent event) {
        ActivityStream as = new ActivityStream();
        as.setActivity("plm.project.unsubscribe");
        as.setConverter(getConverterKey());
        ActivityStreamObject object = new ActivityStreamObject();
        object.setObject(event.getProject().getId());
        object.setType("project");
        as.setObject(object);
        activityStreamService.create(as);
    }

    @Override
    public String getConverterKey() {
        return "plm.project";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";
        String messageString = activityStreamResourceBundle.getString(as.getActivity());

        if (messageString != null) {
            ActivityStreamObject object = as.getObject();
            Person actor = as.getActor();
            PLMProject project = projectRepository.findOne(object.getObject());

            if (project == null) return "";

            String activity = as.getActivity();
            switch (activity) {
                case "plm.project.create":
                    convertedString = getProjectCreatedString(messageString, actor, project);
                    break;
                case "plm.project.update.basicinfo":
                    convertedString = getProjectBasicInfoUpdatedString(messageString, actor, project, as);
                    break;
                case "plm.project.teams.update":
                    convertedString = getProjectMemberUpdatedString(messageString, actor, project, as);
                    break;
                case "plm.project.update.attributes":
                    convertedString = getProjectAttributeUpdatedString(messageString, actor, project, as);
                    break;
                case "plm.project.files.add":
                    convertedString = getProjectFilesAddedString(messageString, project, as);
                    break;
                case "plm.project.files.delete":
                    convertedString = getProjectFileDeletedString(messageString, project, as);
                    break;
                case "plm.project.files.folders.add":
                    convertedString = getProjectFoldersAddedString(messageString, project, as);
                    break;
                case "plm.project.files.folders.delete":
                    convertedString = getProjectFoldersDeletedString(messageString, project, as);
                    break;
                case "plm.project.files.version":
                    convertedString = getProjectFilesVersionedString(messageString, project, as);
                    break;
                case "plm.project.files.rename":
                    convertedString = getProjectFileRenamedString(messageString, project, as);
                    break;
                case "plm.project.files.replace":
                    convertedString = getProjectFileRenamedString(messageString, project, as);
                    break;
                case "plm.project.files.lock":
                    convertedString = getProjectFileString(messageString, project, as);
                    break;
                case "plm.project.files.unlock":
                    convertedString = getProjectFileString(messageString, project, as);
                    break;
                case "plm.project.files.download":
                    convertedString = getProjectFileString(messageString, project, as);
                    break;
                case "plm.project.workflow.start":
                    convertedString = getProjectWorkflowStartString(messageString, project, as);
                    break;
                case "plm.project.activities.workflow.start":
                    convertedString = getActivityWorkflowStartString(messageString, project, as);
                    break;
                case "plm.project.workflow.finish":
                    convertedString = getProjectWorkflowFinishString(messageString, project, as);
                    break;
                case "plm.project.activities.workflow.finish":
                    convertedString = getActivityWorkflowFinishString(messageString, project, as);
                    break;
                case "plm.project.workflow.promote":
                    convertedString = getProjectWorkflowPromoteDemoteString(messageString, project, as);
                    break;
                case "plm.project.activities.workflow.promote":
                    convertedString = getActivityWorkflowPromoteDemoteString(messageString, project, as);
                    break;
                case "plm.project.workflow.demote":
                    convertedString = getProjectWorkflowPromoteDemoteString(messageString, project, as);
                    break;
                case "plm.project.activities.workflow.demote":
                    convertedString = getActivityWorkflowPromoteDemoteString(messageString, project, as);
                    break;
                case "plm.project.workflow.hold":
                    convertedString = getProjectWorkflowHoldUnholdString(messageString, project, as);
                    break;
                case "plm.project.activities.workflow.hold":
                    convertedString = getActivityWorkflowHoldUnholdString(messageString, project, as);
                    break;
                case "plm.project.workflow.unhold":
                    convertedString = getProjectWorkflowHoldUnholdString(messageString, project, as);
                    break;
                case "plm.project.activities.workflow.unhold":
                    convertedString = getActivityWorkflowHoldUnholdString(messageString, project, as);
                    break;
                case "plm.project.workflow.add":
                    convertedString = getProjectWorkflowAddedString(messageString, project, as);
                    break;
                case "plm.project.workflow.change":
                    convertedString = getProjectWorkflowChangeString(messageString, project, as);
                    break;
                case "plm.project.activities.workflow.add":
                    convertedString = getProjectActivityWorkflowAddedString(messageString, project, as);
                    break;
                case "plm.project.activities.workflow.change":
                    convertedString = getProjectActivityWorkflowChangeString(messageString, project, as);
                    break;
                case "plm.project.comment":
                    convertedString = getProjectCommentString(messageString, project, as);
                    break;
                case "plm.project.teams.add":
                    convertedString = getProjectMembersAddedString(messageString, project, as);
                    break;
                case "plm.project.teams.delete":
                    convertedString = getProjectMemberDeletedString(messageString, project, as);
                    break;
                case "plm.project.teams.add.team":
                    convertedString = getProjectMemberAddedString(messageString, project, as);
                    break;
                case "plm.project.deliverables.add":
                    convertedString = getProjectDeliverablesAddedString(messageString, project, as);
                    break;
                case "plm.project.deliverables.delete":
                    convertedString = getProjectMemberDeletedString(messageString, project, as);
                    break;
                case "plm.project.deliverables.finish":
                    convertedString = getProjectMemberDeletedString(messageString, project, as);
                    break;
                case "plm.project.referenceitems.add":
                    convertedString = getProjectReferenceItemsAddedString(messageString, project, as);
                    break;
                case "plm.project.referenceitems.delete":
                    convertedString = getProjectMemberDeletedString(messageString, project, as);
                    break;
                case "plm.project.phases.add":
                    convertedString = getProjectPhasesAddedString(messageString, actor, project, as);
                    break;
                case "plm.project.activities.add":
                    convertedString = getProjectActivitiesAddedString(messageString, actor, project, as);
                    break;
                case "plm.project.activities.basicinfo":
                    convertedString = getActivityBasicInfoUpdatedString(messageString, actor, project, as);
                    break;
                case "plm.project.activities.finish":
                    convertedString = getProjectActivityFinishedString(messageString, actor, project, as);
                    break;
                case "plm.project.tasks.add":
                    convertedString = getProjectTasksAddedString(messageString, actor, project, as);
                    break;
                case "plm.project.tasks.finish":
                    convertedString = getProjectTaskFinishedString(messageString, actor, project, as);
                    break;
                case "plm.project.tasks.percent":
                    convertedString = getProjectTaskPercentString(messageString, actor, project, as);
                    break;
                case "plm.project.milestones.add":
                    convertedString = getProjectMilestonesAddedString(messageString, actor, project, as);
                    break;
                case "plm.project.milestones.finish":
                    convertedString = getProjectActivityFinishedString(messageString, actor, project, as);
                    break;
                case "plm.project.reqdocuments.add":
                    convertedString = getProjectReqDocumentAddedString(messageString, actor, project, as);
                    break;
                case "plm.project.reqdocuments.delete":
                    convertedString = getProjectReqDocumentDeletedString(messageString, actor, project, as);
                    break;
                case "plm.project.subscribe":
                    convertedString = getProjectCreatedString(messageString, actor, project);
                    break;
                case "plm.project.unsubscribe":
                    convertedString = getProjectCreatedString(messageString, actor, project);
                    break;
                default:
                    break;
            }
        }
        return convertedString;
    }

    private String getProjectAttributeUpdatedString(String messageString, Person actor, PLMProject project,
                                                     ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.project.update.attributes.attribute");

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


    private String getProjectCreatedString(String messageString, Person actor, PLMProject project) {
        return MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());
    }

    private String getProjectActivityFinishedString(String messageString, Person actor, PLMProject project, ActivityStream as) {
        String json = as.getData();
        String s = null;
        try {
            ASNewActivityAndMilestoneDTO asNewActivityAndMilestoneDTO = objectMapper.readValue(json, new TypeReference<ASNewActivityAndMilestoneDTO>() {
            });
            s = MessageFormat.format(messageString, actor.getFullName().trim(), asNewActivityAndMilestoneDTO.getName(), asNewActivityAndMilestoneDTO.getPhase(), project.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    private String getProjectTaskFinishedString(String messageString, Person actor, PLMProject project, ActivityStream as) {
        String json = as.getData();
        String s = null;
        try {
            ASNewTaskDTO asNewTaskDTO = objectMapper.readValue(json, new TypeReference<ASNewTaskDTO>() {
            });
            s = MessageFormat.format(messageString, actor.getFullName().trim(), asNewTaskDTO.getTask(), asNewTaskDTO.getActivity(), asNewTaskDTO.getPhase(), project.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    private String getProjectTaskPercentString(String messageString, Person actor, PLMProject project, ActivityStream as) {
        String json = as.getData();
        String s = null;
        try {
            ASBomItem asNewTaskDTO = objectMapper.readValue(json, new TypeReference<ASBomItem>() {
            });
            s = MessageFormat.format(messageString, highlightValue(actor.getFullName().trim()), highlightValue(asNewTaskDTO.getNotes()), asNewTaskDTO.getQuantity(),
                    highlightValue(asNewTaskDTO.getRefdes()), highlightValue(asNewTaskDTO.getNumber()), highlightValue(project.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    private String getProjectBasicInfoUpdatedString(String messageString, Person actor, PLMProject project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.project.update.basicinfo.property");

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
    private String getActivityBasicInfoUpdatedString(String messageString, Person actor, PLMProject project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.project.activities.basicinfo.property");

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

    private String getTaskBasicInfoUpdatedString(String messageString, Person actor, PLMProject project, ActivityStream as) {
        
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.project.tasks.basicinfo.property");

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

    private String getProjectMemberUpdatedString(String messageString, Person actor, PLMProject project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.project.teams.update.property");

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

    private String getProjectPhasesAddedString(String messageString, Person actor, PLMProject project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.project.phases.add.phase");

        String json = as.getData();
        try {
            List<ASNewMemberDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMemberDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(updateString, highlightValue(f.getName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getProjectActivitiesAddedString(String messageString, Person actor, PLMProject project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.project.activities.add.activity");

        String json = as.getData();
        try {
            List<ASNewActivityAndMilestoneDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewActivityAndMilestoneDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(updateString, highlightValue(f.getName()), f.getPhase()));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
    
    private String getProjectTasksAddedString(String messageString, Person actor, PLMProject project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.project.tasks.add.task");

        String json = as.getData();
        try {
            List<ASNewTaskDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewTaskDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(updateString, highlightValue(f.getTask()), f.getActivity(), f.getPhase()));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getProjectMilestonesAddedString(String messageString, Person actor, PLMProject project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.project.milestones.add.milestone");

        String json = as.getData();
        try {
            List<ASNewActivityAndMilestoneDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewActivityAndMilestoneDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(updateString, highlightValue(f.getName()), f.getPhase()));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getProjectReqDocumentAddedString(String messageString, Person actor, PLMProject project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, actor.getFullName().trim(), highlightValue(project.getName()));

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String updateString = activityStreamResourceBundle.getString("plm.project.reqdocuments.add.requirement");

        String json = as.getData();
        try {
            List<ASReqDocoumentDto> docoumentDtos = objectMapper.readValue(json, new TypeReference<List<ASReqDocoumentDto>>() {
            });
            docoumentDtos.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(updateString, highlightValue(f.getNumber()), highlightValue(f.getName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    private String getProjectReqDocumentDeletedString(String messageString, Person actor, PLMProject project, ActivityStream as) {
        String json = as.getData();
        String s = null;
        try {
            ASReqDocoumentDto asReqDocoumentDto = objectMapper.readValue(json, new TypeReference<ASReqDocoumentDto>() {
            });
            s = MessageFormat.format(messageString, highlightValue(actor.getFullName().trim()), highlightValue(asReqDocoumentDto.getNumber()),
                    highlightValue(asReqDocoumentDto.getName()), highlightValue(project.getName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public String getProjectFilesAddedJson(List<PLMProjectFile> files) {
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

    private String getProjectPhasesAddedJson(List<PLMWbsElement> wbsElements) {
        String json = null;

        List<ASNewMemberDTO> ASNewFileDtos = new ArrayList<>();
        wbsElements.forEach(f -> ASNewFileDtos.add(new ASNewMemberDTO(f.getId(), f.getName())));

        try {
            json = objectMapper.writeValueAsString(ASNewFileDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProjectActivitiesAddedJson(List<ASNewActivityAndMilestoneDTO> asNewActivityAndMilestoneDTOs) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewActivityAndMilestoneDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProjectActivityFinishedJson(ASNewActivityAndMilestoneDTO asNewActivityAndMilestoneDTO) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewActivityAndMilestoneDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProjectTasksAddedJson(List<ASNewTaskDTO> newTaskDTOs) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(newTaskDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProjectTaskFinishedJson(ASNewTaskDTO newTaskDTO) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(newTaskDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProjectTaskPercentageUpdatedJson(ASBomItem newTaskDTO) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(newTaskDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProjectMilestonesAddedJson(List<ASNewActivityAndMilestoneDTO> milestoneDTOs) {
        String json = null;

        try {
            json = objectMapper.writeValueAsString(milestoneDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public String getProjectReqDocumentsAddedJson(List<PLMProjectRequirementDocument> requirementDocuments) {
        String json = null;
        List<ASReqDocoumentDto> dtos = new LinkedList<>();
        requirementDocuments.forEach(plmProjectRequirementDocument -> {
            dtos.add(new ASReqDocoumentDto(plmProjectRequirementDocument.getReqDocument().getId(), plmProjectRequirementDocument.getReqDocument().getName(), plmProjectRequirementDocument.getReqDocument().getMaster().getNumber()));
        });
        try {
            json = objectMapper.writeValueAsString(dtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public String getProjectMembersAddedJson(List<Person> files) {
        String json = null;

        List<ASNewMemberDTO> AsNewMemberDtos = new ArrayList<>();
        files.forEach(f -> AsNewMemberDtos.add(new ASNewMemberDTO(f.getId(), f.getFullName())));

        try {
            json = objectMapper.writeValueAsString(AsNewMemberDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public String getProjectDeliverablesAddedJson(List<PLMItem> projectDeliverables) {
        String json = null;

        List<ASNewMemberDTO> AsNewMemberDtos = new ArrayList<>();
        projectDeliverables.forEach(f -> AsNewMemberDtos.add(new ASNewMemberDTO(f.getId(), f.getItemName())));

        try {
            json = objectMapper.writeValueAsString(AsNewMemberDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProjectReferenceItemsAddedJson(List<ASNewMemberDTO> projectItemReferences) {
        String json = null;

        try {
            json = objectMapper.writeValueAsString(projectItemReferences);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProjectFoldersAddedJson(PLMProjectFile file) {
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

    private String getProjectFoldersDeletedJson(PLMFile file) {
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
 
    private String getProjectFilesAddedString(String messageString, PLMProject project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.project.files.add.file");

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

    private String getProjectFoldersAddedString(String messageString, PLMProject project, ActivityStream as) {

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

    private String getProjectFoldersDeletedString(String messageString, PLMProject project, ActivityStream as) {

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


    private String getProjectFileDeletedString(String messageString, PLMProject project, ActivityStream as) {

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

    public String getProjectFilesVersionedJson(List<PLMProjectFile> files) {
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

    private String getProjectFilesVersionedString(String messageString, PLMProject project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, as.getActor().getFullName().trim(), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.project.files.version.file");

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

    private String getProjectFileRenamedString(String messageString, PLMProject project, ActivityStream as) {
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

    private String getProjectFileString(String messageString, PLMProject project, ActivityStream as) {
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

    private String getProjectWorkflowStartString(String messageString, PLMProject project, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                project.getName());
    }

    private String getActivityWorkflowStartString(String messageString, PLMProject project, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMActivity plmActivity = activityRepository.findOne(plmWorkflow.getAttachedTo());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                plmActivity.getName(),
                project.getName());
    }

    private String getProjectWorkflowFinishString(String messageString, PLMProject project, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                project.getName());
    }

    private String getActivityWorkflowFinishString(String messageString, PLMProject project, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMActivity plmActivity = activityRepository.findOne(plmWorkflow.getAttachedTo());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()), plmActivity.getName(),
                project.getName());
    }

    private String getProjectWorkflowPromoteDemoteString(String messageString, PLMProject project, ActivityStream as) {
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

    private String getActivityWorkflowPromoteDemoteString(String messageString, PLMProject project, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();
        ActivityStreamObject target = as.getTarget();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus fromActivity = workflowStatusRepository.findOne(source.getObject());
        PLMWorkflowStatus toActivity = workflowStatusRepository.findOne(target.getObject());
        PLMActivity plmActivity = activityRepository.findOne(plmWorkflow.getAttachedTo());
        

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflow.getName()),
                plmActivity.getName(),
                project.getName(),
                highlightValue(fromActivity.getName()),
                highlightValue(toActivity.getName()));
    }

    private String getProjectWorkflowHoldUnholdString(String messageString, PLMProject project, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()),
                project.getName());
    }

    private String getActivityWorkflowHoldUnholdString(String messageString, PLMProject project, ActivityStream as) {
        ActivityStreamObject context = as.getContext();
        ActivityStreamObject source = as.getSource();

        PLMWorkflow plmWorkflow = workFlowRepository.findOne(context.getObject());
        PLMWorkflowStatus plmWorkflowActivity = workflowStatusRepository.findOne(source.getObject());
        PLMActivity plmActivity = activityRepository.findOne(plmWorkflow.getAttachedTo());

        return MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                highlightValue(plmWorkflowActivity.getName()),
                highlightValue(plmWorkflow.getName()), plmActivity.getName(),
                project.getName());
    }

    private String getProjectWorkflowAddedString(String messageString, PLMProject project, ActivityStream as) {
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

    public String getProjectWorkflowChangeString(String messageString, PLMProject project, ActivityStream as) {
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

    public String getProjectActivityWorkflowAddedString(String messageString, PLMProject project, ActivityStream as) {
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

    public String getProjectActivityWorkflowChangeString(String messageString, PLMProject project, ActivityStream as) {
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

    private String getProjectCommentString(String messageString, PLMProject project, ActivityStream as) {
        String message = MessageFormat.format(messageString, as.getActor().getFullName().trim(),
                project.getName());

        String s = highlightValue("Comment: ");
        s += as.getData();
        s = addMarginToMessage(s);

        message += s;

        return message;
    }

    private String getProjectMembersAddedString(String messageString, PLMProject project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.project.teams.add.team");

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

    private String getProjectMemberDeletedString(String messageString, PLMProject project, ActivityStream as) {

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

    private String getProjectDeliverablesAddedString(String messageString, PLMProject project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.project.deliverables.add");

        String json = as.getData();
        try {
            List<ASNewMemberDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMemberDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getName()), project.getName()));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getProjectReferenceItemsAddedString(String messageString, PLMProject project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.project.referenceitems.add");

        String json = as.getData();
        try {
            List<ASNewMemberDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMemberDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getName()), project.getName()));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String getProjectMembersDeletedJson(Person person) {
        String json = null;

        List<ASNewMemberDTO> ASNewMemberDtos = new ArrayList<>();
        ASNewMemberDtos.add(new ASNewMemberDTO(person.getId(), person.getFullName()));
        try {
            json = objectMapper.writeValueAsString(ASNewMemberDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProjectMemberAddedJson( Person person,  PLMProject project) {
        List<ASNewMemberDTO> asNewMemberDTOs = new ArrayList<>();
        ASNewMemberDTO asNewMemberDTO = new ASNewMemberDTO(person.getId(), person.getFullName());
        asNewMemberDTOs.add(asNewMemberDTO);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(asNewMemberDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProjectMemberAddedString(String messageString, PLMProject  project, ActivityStream as) {
        String activityString = MessageFormat.format(messageString, highlightValue(as.getActor().getFullName().trim()), project.getName());

        StringBuffer sb = new StringBuffer();
        sb.append(activityString);

        String fileString = activityStreamResourceBundle.getString("plm.project.teams.add.team");

        String json = as.getData();
        try {
            List<ASNewMemberDTO> files = objectMapper.readValue(json, new TypeReference<List<ASNewMemberDTO>>() {
            });
            files.forEach(f -> {
                String s = addMarginToMessage(MessageFormat.format(fileString, highlightValue(f.getName()), highlightValue(f.getName())));
                sb.append(s);
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public String getProjectDeliverableDeletedJson(PLMItem deliverable) {
        String json = null;

        List<ASNewMemberDTO> ASNewMemberDtos = new ArrayList<>();
        ASNewMemberDtos.add(new ASNewMemberDTO(deliverable.getId(), deliverable.getItemName()));
        try {
            json = objectMapper.writeValueAsString(ASNewMemberDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getProjectReferenceItemDeletedJson(PLMItem itemReference) {
        String json = null;

        List<ASNewMemberDTO> ASNewMemberDtos = new ArrayList<>();
        ASNewMemberDtos.add(new ASNewMemberDTO(itemReference.getId(), itemReference.getItemName()));
        try {
            json = objectMapper.writeValueAsString(ASNewMemberDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public String getProjectBasicInfoUpdatedJson(PLMProject oldProject, PLMProject newProject) {
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

        Integer oldValue1 = oldProject.getProjectManager();
        Integer newValue1 = newProject.getProjectManager();
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Project Manager", personRepository.findOne(oldValue1).getFullName(), personRepository.findOne(newValue1).getFullName()));
        }
        Date oldDate = null;
        Date newDate = null;
        if (oldProject.getPlannedStartDate() != null) {
            oldDate = oldProject.getPlannedStartDate();
        }

        if (newProject.getPlannedStartDate() != null) {
            newDate = newProject.getPlannedStartDate();
        }

        if (oldDate == null && newDate != null) {
            String oldDateValue = "";
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Planned Start Date", oldDateValue, newDateValue));
        } else if (oldDate != null && newDate != null && oldDate.compareTo(newDate) != 0) {
            String oldDateValue = dateFormat.format(oldDate);
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Planned Start Date", oldDateValue, newDateValue));
        }
        if (oldProject.getPlannedFinishDate() != null) {
            oldDate = oldProject.getPlannedFinishDate();
        }

        if (newProject.getPlannedFinishDate() != null) {
            newDate = newProject.getPlannedFinishDate();
        }
        if (oldDate == null && newDate != null) {
            String oldDateValue = "";
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Planned Finished Date", oldDateValue, newDateValue));
        } else if (oldDate != null && newDate != null && oldDate.compareTo(newDate) != 0) {
            String oldDateValue = dateFormat.format(oldDate);
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Planned Finished Date", oldDateValue, newDateValue));
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

    public String getActivityBasicInfoUpdatedJson(PLMActivity oldActivity, PLMActivity newActivity) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String oldValue = oldActivity.getName();
        String newValue = newActivity.getName();
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Name", oldValue, newValue));
        }

        oldValue = oldActivity.getDescription();
        newValue = newActivity.getDescription();
        if (oldValue == null) {
            oldValue = "";
        }
        if (newValue == null) {
            newValue = "";
        }
        if (!newValue.equals(oldValue)) {
            changes.add(new ASPropertyChangeDTO("Description", oldValue, newValue));
        }

        Integer oldValue1 = oldActivity.getAssignedTo();
        Integer newValue1 = newActivity.getAssignedTo();
        if (!newValue1.equals(oldValue1)) {
            changes.add(new ASPropertyChangeDTO("Assigned To", personRepository.findOne(oldValue1).getFullName(), personRepository.findOne(newValue1).getFullName()));
        }
        Date oldDate = null;
        Date newDate = null;
        if (oldActivity.getPlannedStartDate() != null) {
            oldDate = oldActivity.getPlannedStartDate();
        }

        if (newActivity.getPlannedStartDate() != null) {
            newDate = newActivity.getPlannedStartDate();
        }

        if (oldDate == null && newDate != null) {
            String oldDateValue = "";
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Planned Start Date", oldDateValue, newDateValue));
        } else if (oldDate != null && newDate != null && oldDate.compareTo(newDate) != 0) {
            String oldDateValue = dateFormat.format(oldDate);
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Planned Start Date", oldDateValue, newDateValue));
        }
        if (oldActivity.getPlannedFinishDate() != null) {
            oldDate = oldActivity.getPlannedFinishDate();
        }

        if (newActivity.getPlannedFinishDate() != null) {
            newDate = newActivity.getPlannedFinishDate();
        }
        if (oldDate == null && newDate != null) {
            String oldDateValue = "";
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Planned Finished Date", oldDateValue, newDateValue));
        } else if (oldDate != null && newDate != null && oldDate.compareTo(newDate) != 0) {
            String oldDateValue = dateFormat.format(oldDate);
            String newDateValue = dateFormat.format(newDate);
            changes.add(new ASPropertyChangeDTO("Planned Finished Date", oldDateValue, newDateValue));
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

   
    public String getProjectMemberUpdatedJson(PLMProjectMember oldProject, PLMProjectMember newProject) {
        List<ASPropertyChangeDTO> changes = new ArrayList<>();
        

        String oldValue = oldProject.getRole();
        String newValue = newProject.getRole();
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
}
