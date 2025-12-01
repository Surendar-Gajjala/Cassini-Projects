package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.pm.PLMActivity;
import com.cassinisys.plm.model.pm.PLMActivityFile;
import com.cassinisys.plm.model.pm.PLMProgramProject;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.PLMProjectFile;
import com.cassinisys.plm.model.pm.PLMProjectMember;
import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.pm.PLMWbsElement;
import com.cassinisys.plm.model.req.PLMProjectRequirementDocument;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.service.activitystream.dto.ASBomItem;
import com.cassinisys.plm.service.activitystream.dto.ASNewActivityAndMilestoneDTO;
import com.cassinisys.plm.service.activitystream.dto.ASNewMemberDTO;
import com.cassinisys.plm.service.activitystream.dto.ASNewTaskDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


public final class ProjectEvents {

    @Data
    @AllArgsConstructor
    public static class ProjectCreatedEvent {
        private PLMProject project;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectBasicInfoUpdatedEvent {
        private PLMProject oldProject;
        private PLMProject newproject;

    }

    // Project team events
    @Data
    @AllArgsConstructor
    public static class ProjectMembersAddedEvent {
        private PLMProject project;
        private List<Person> persons;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectMemberDeletedEvent {
        private PLMProject project;
        private Person person;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectMemberAddedEvent {
        private PLMProject project;
        private Person person;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectMemberUpdatedEvent {
        private  PLMProjectMember oldProjectMember;
        private PLMProjectMember newProjectMember;
    }

    // Project plan events
    @Data
    @AllArgsConstructor
    public static class ProjectPhasesAddedEvent {
        private PLMProject project;
        private List<PLMWbsElement> wbsElements;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectActivitiesAddedEvent {
        private PLMProject project;
        private List<ASNewActivityAndMilestoneDTO> asNewActivityAndMilestoneDTOs;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectActivitiesUpdatedEvent {
        private PLMProject project;
        private PLMActivity oldActivity;
        private  PLMActivity  newActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectActivityFinishedEvent {
        private PLMProject project;
        private ASNewActivityAndMilestoneDTO asNewActivityAndMilestoneDTOs;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectMilestoneFinishedEvent {
        private PLMProject project;
        private ASNewActivityAndMilestoneDTO asNewActivityAndMilestoneDTOs;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectTaskFinishedEvent {
        private PLMProject project;
        private ASNewTaskDTO asNewTaskDTO;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectTaskPercentageUpdatedEvent {
        private PLMProject project;
        private ASBomItem asNewTaskDTO;
    }


    @Data
    @AllArgsConstructor
    public static class ProjectTasksAddedEvent {
        private PLMProject project;
        private List<ASNewTaskDTO> newTasks;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectMilestonesAddedEvent {
        private PLMProject project;
        private List<ASNewActivityAndMilestoneDTO> asNewActivityAndMilestoneDTOs;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectReqDocumentsAddedEvent {
        private PLMProject project;
        private List<PLMProjectRequirementDocument> requirementDocuments;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectReqDocumentsDeletedEvent {
        private PLMProject project;
        private PLMProjectRequirementDocument requirementDocument;
    }

    // Project deliverables events

    @Data
    @AllArgsConstructor
    public static class ProjectDeliverablesAddedEvent {
        private PLMProject project;
        private List<PLMItem> projectDeliverables;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectDeliverableDeletedEvent {
        private PLMProject project;
        private PLMItem projectDeliverable;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectDeliverableFinishedEvent {
        private PLMProject project;
        private PLMItem projectDeliverable;
    }

    // Project reference items events
    @Data
    @AllArgsConstructor
    public static class ProjectReferenceItemsAddedEvent {
        private PLMProject project;
        private List<ASNewMemberDTO> projectItemReferences;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectReferenceItemDeletedEvent {
        private PLMProject project;
        private PLMItem projectItemReference;
    }

    // Project file events
    @Data
    @AllArgsConstructor
    public static class ProjectFileDeletedEvent {
        private PLMProject project;
        private PLMProjectFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectFilesVersionedEvent {
        private PLMProject project;
        private List<PLMProjectFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectFileRenamedEvent {
        private PLMProject project;
        private String type;
        private PLMProjectFile oldFile;
        private PLMProjectFile newFile;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectFileLockedEvent {
        private PLMProject project;
        private PLMProjectFile projectFile;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectFileUnlockedEvent {
        private PLMProject project;
        private PLMProjectFile projectFile;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectFileDownloadedEvent {
        private PLMProject Project;
        private PLMProjectFile projectFile;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectFilesAddedEvent {
        private PLMProject project;
        private List<PLMProjectFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectFoldersAddedEvent {
        private PLMProject project;
        private PLMProjectFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectFoldersDeletedEvent {
        private PLMProject project;
        private PLMFile file;
    }

    // Project workflow events

    @Data
    @AllArgsConstructor
    public static class ProjectWorkflowStartedEvent {
        private PLMProject Project;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityWorkflowStartedEvent {
        private PLMProject Project;
        private PLMActivity plmActivity;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityWorkflowPromotedEvent {
        private PLMProject Project;
        private PLMActivity activity;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityWorkflowDemotedEvent {
        private PLMProject Project;
        private PLMActivity activity;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityWorkflowFinishedEvent {
        private PLMProject Project;
        private PLMActivity activity;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityWorkflowHoldEvent {
        private PLMProject Project;
        private PLMActivity activity;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ActivityWorkflowUnholdEvent {
        private PLMProject Project;
        private PLMActivity activity;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }


    @Data
    @AllArgsConstructor
    public static class ProjectWorkflowPromotedEvent {
        private PLMProject Project;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectWorkflowDemotedEvent {
        private PLMProject Project;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectWorkflowFinishedEvent {
        private PLMProject Project;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectWorkflowHoldEvent {
        private PLMProject Project;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectWorkflowUnholdEvent {
        private PLMProject Project;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectWorkflowChangeEvent {
        private PLMProject project;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectActivityWorkflowChangeEvent {
        private PLMProject project;
        private PLMActivity activity;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class ProjectCommentAddedEvent {
        private PLMProject Project;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectSubscribeEvent {
        private PLMProject project;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectUnSubscribeEvent {
        private PLMProject project;
    }

    @Data
    @AllArgsConstructor
    public static class ProjectAttributesUpdatedEvent {
        private Integer project;
        private ObjectAttribute oldAttribute;
        private ObjectAttribute newAttribute;
    }
}
