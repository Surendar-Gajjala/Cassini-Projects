package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smukka on 18-06-2022.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PROGRAM_TEMPLATE")
@PrimaryKeyJoinColumn(name = "ID")
public class ProgramTemplate extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Transient
    private Boolean resources = Boolean.FALSE;

    @Transient
    private Boolean projects = Boolean.FALSE;

    @Transient
    private Boolean team = Boolean.FALSE;

    @Transient
    private Boolean assignedTo = Boolean.FALSE;
    @Transient
    private Boolean copyFolders = Boolean.FALSE;
    @Transient
    private Boolean allFolders = Boolean.FALSE;
    @Transient
    private Boolean programFolders = Boolean.FALSE;
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
    private Boolean programWorkflows = Boolean.FALSE;
    @Transient
    private Boolean projectWorkflows = Boolean.FALSE;
    @Transient
    private Boolean activityWorkflows = Boolean.FALSE;
    @Transient
    private Boolean taskWorkflows = Boolean.FALSE;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private Boolean finishWorkflow = Boolean.FALSE;

    @Transient
    private String workflowStatus="None";

    @Transient
    private Integer program;
    @Transient
    private List<Integer> selectedProjectIds = new ArrayList<>();

    public ProgramTemplate() {
        super(PLMObjectType.PROGRAMTEMPLATE);
    }
}
