package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by swapna on 12/31/17.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_ACTIVITY")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMActivity extends CassiniObject {

    @Column
    private Integer wbs;
    
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PMObjectType type;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "DURATION")
    private Integer duration;

    @Column(name = "SEQUENCE_NUMBER")
    private Integer sequenceNumber;

    @Column
    @Type(
            type = "com.cassinisys.platform.util.converter.IntArrayUserType"
    )
    private Integer[] predecessors;

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

    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pm.ProjectActivityStatus")})
    @Column(name = "STATUS", nullable = false)
    private ProjectActivityStatus status;

    @Column(name = "PERCENT_COMPLETE")
    private Double percentComplete = 0.0;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Transient
    private Integer workflowDefId;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private Long ganttId;

    @Transient
    private Integer level = 0;

    @Transient
    private Person person;

    @Transient
    private String workflowStatus="None";

    @Transient
    private List<PLMTask> activityTasks = new ArrayList<>();

    @Transient
    private List<PLMActivityFile> activityFiles = new ArrayList<>();

    @Transient
    private List<PLMActivityDeliverable> activityDeliverables = new ArrayList<>();

    @Transient
    private List<PLMActivityItemReference> activityItemReferences = new ArrayList<>();

    @Transient
    private PLMProject project;

    public PLMActivity() {
        super(PLMObjectType.PROJECTACTIVITY);
    }


}
