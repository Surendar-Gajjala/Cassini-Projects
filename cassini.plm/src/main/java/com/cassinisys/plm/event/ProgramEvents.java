package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


public final class ProgramEvents {

    @Data
    @AllArgsConstructor
    public static class ProgramCreatedEvent {
        private PLMProgram program;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramBasicInfoUpdatedEvent {
        private PLMProgram oldProgram;
        private PLMProgram newProgram;

    }

    // @Data
    // @AllArgsConstructor
    // public static class ProgramAttributesUpdatedEvent {
    //     private PLMProgram supplierAudit;
    //     private PLMProgram oldAttribute;
    //     private PLMProgram newAttribute;
    // }

    // Program team events
    @Data
    @AllArgsConstructor
    public static class ProgramMembersAddedEvent {
        private Integer program;
        private List<PLMProgramResource> persons;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramMemberUpdatedEvent {
        private PLMProgramResource OldResource;
        private PLMProgramResource newResource;
    }


    @Data
    @AllArgsConstructor
    public static class ProgramMemberDeletedEvent {
        private Integer program;
        private PLMProgramResource person;
    }

    // Program project events
    @Data
    @AllArgsConstructor
    public static class ProgramProjectAddedEvent {
        private Integer program;
        private PLMProgramProject programProject;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramProjectUpdateEvent {
        private PLMProgramProject newProgramProject;
        private PLMProgramProject oldProgramProject;

    }

    @Data
    @AllArgsConstructor
    public static class ProgramAttributesUpdatedEvent {
        private Integer program;
        private ObjectAttribute oldAttribute;
        private ObjectAttribute newAttribute;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramProjectDeletedEvent {
        private Integer program;
        private PLMProgramProject programProject;
    }

    // Program file events
    @Data
    @AllArgsConstructor
    public static class ProgramFileDeletedEvent {
        private Integer program;
        private PLMProgramFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramFilesVersionedEvent {
        private Integer program;
        private List<PLMProgramFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramFileRenamedEvent {
        private Integer program;
        private String type;
        private PLMProgramFile oldFile;
        private PLMProgramFile newFile;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramFileLockedEvent {
        private Integer program;
        private PLMProgramFile projectFile;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramFileUnlockedEvent {
        private Integer program;
        private PLMProgramFile projectFile;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramFileDownloadedEvent {
        private Integer Program;
        private PLMProgramFile projectFile;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramFilesAddedEvent {
        private Integer program;
        private List<PLMProgramFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramFoldersAddedEvent {
        private Integer program;
        private PLMProgramFile file;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramFoldersDeletedEvent {
        private Integer program;
        private PLMFile file;
    }

    // Program workflow events

    @Data
    @AllArgsConstructor
    public static class ProgramWorkflowStartedEvent {
        private PLMProgram Program;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramWorkflowPromotedEvent {
        private PLMProgram Program;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramWorkflowDemotedEvent {
        private PLMProgram Program;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus fromActivity;
        private PLMWorkflowStatus toActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramWorkflowFinishedEvent {
        private PLMProgram Program;
        private PLMWorkflow plmWorkflow;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramWorkflowHoldEvent {
        private PLMProgram Program;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramWorkflowUnholdEvent {
        private PLMProgram Program;
        private PLMWorkflow plmWorkflow;
        private PLMWorkflowStatus workflowActivity;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramWorkflowChangeEvent {
        private PLMProgram program;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramActivityWorkflowChangeEvent {
        private PLMProgram program;
        private PLMActivity activity;
        private PLMWorkflow oldWorkflow;
        private PLMWorkflow workflow;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class ProgramCommentAddedEvent {
        private PLMProgram Program;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramSubscribeEvent {
        private PLMProgram program;
    }

    @Data
    @AllArgsConstructor
    public static class ProgramUnSubscribeEvent {
        private PLMProgram program;
    }
}
