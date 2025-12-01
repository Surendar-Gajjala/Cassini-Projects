package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by subramanyam reddy on 14-03-2018.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PROJECT_TEMPLATE")
@PrimaryKeyJoinColumn(name = "ID")
public class ProjectTemplate extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "MANAGER")
    private Integer manager;

    @Column(name = "PROGRAM_TEMPLATE")
    private Integer programTemplate;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Transient
    private Boolean team = Boolean.FALSE;

    @Transient
    private Boolean assignedTo = Boolean.FALSE;
    @Transient
    private Integer programTemplateProject;
    @Transient
    private Boolean copyFolders = Boolean.FALSE;
    @Transient
    private Boolean allFolders = Boolean.FALSE;
    @Transient
    private Boolean projectFolders = Boolean.FALSE;
    @Transient
    private Boolean activityFolders = Boolean.FALSE;
    @Transient
    private Boolean taskFolders = Boolean.FALSE;

    @Transient
    private Boolean copyWorkflows = Boolean.FALSE;
    @Transient
    private Boolean allWorkflows = Boolean.FALSE;
    @Transient
    private Boolean projectWorkflows = Boolean.FALSE;
    @Transient
    private Boolean activityWorkflows = Boolean.FALSE;
    @Transient
    private Boolean taskWorkflows = Boolean.FALSE;

    @Transient
    private String workflowStatus="None";


    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private Boolean finishWorkflow = Boolean.FALSE;

    public ProjectTemplate() {
        super(PLMObjectType.TEMPLATE);
    }

}
