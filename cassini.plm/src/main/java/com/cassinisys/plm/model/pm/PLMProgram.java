package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by smukka on 02-06-2022.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_PROGRAM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMProgram extends CassiniObject {

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PMObjectType type;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PROGRAM_MANAGER")
    private Integer programManager;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Transient
    private Integer workflowDefId;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;
    @Transient
    private Double percentComplete = 0.0;
    @Transient
    private String percent = "0";
    @Transient
    private Integer programTemplate;
    @Transient
    private String createdByName;
    @Transient
    private String programManagerName;
    @Transient
    private String workflowStatus="None";
    @Transient
    private Boolean resources = Boolean.FALSE;

    @Transient
    private Boolean projects = Boolean.FALSE;

    @Transient
    private Boolean team = Boolean.FALSE;
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
    private Boolean assignedTo = Boolean.FALSE;

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
    private Boolean programResource = Boolean.FALSE;

    public PLMProgram() {
        super(PLMObjectType.PROGRAM);
    }
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";


}
