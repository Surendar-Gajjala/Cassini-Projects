package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.Date;

/**
 * Created by swapna on 12/31/17.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_PROJECT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMProject extends CassiniObject {

    @Transient
    Double percentComplete = 0.0;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PMObjectType type;
    @Column
    private String name;
    @Column
    private String description;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_FINISHDATE")
    private Date plannedFinishDate;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTUAL_FINISHDATE")
    private Date actualFinishDate;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_STARTDATE")
    private Date plannedStartDate;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTUAL_STARTDATE")
    private Date actualStartDate;
    @Column(name = "PROJECT_MANAGER")
    private Integer projectManager;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "MAKE_CONVERSATION_PRIVATE")
    private Boolean makeConversationPrivate = Boolean.FALSE;

    @Column(name = "PROGRAM")
    private Integer program;

    @Transient
    private Integer workflowDefId;

    @Transient
    private String printPerson;

    @Transient
    private String projectManagerPrint;

    @Transient
    private String Date;

    @Transient
    private String workflowStatus="None";

    @Transient
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    private WorkflowStatusType statusType = WorkflowStatusType.NORMAL;

    @Transient
    private String plannedStartDatePrint;
    @Transient
    private String plannedFinishDatePrint;

    @Transient
    private String actualStartDatePrint;

    @Transient
    private String actualFinishDatePrint;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Transient
    private Date minDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Transient
    private Date maxDate;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private Boolean team = Boolean.FALSE;

    @Transient
    private Boolean assignedTo = Boolean.FALSE;

    @Transient
    private Boolean teamMember = Boolean.FALSE;
    @Transient
    private Integer programProject;
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


    public PLMProject() {
        super(PLMObjectType.PROJECT);
    }
    @Transient
    private String workflowSettingStatus = "NONE";
    @Transient
    private String workflowSettingStatusType = "UNDEFINED";


}
