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
@Table(name = "PLM_TASK")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMTask extends CassiniObject {

    @Column
    private Integer activity;

    @Column
    private String name;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private PMObjectType type;

    @Column
    private String description;

    @Column(name = "SEQUENCE_NUMBER")
    private Integer sequenceNumber;

    @Column(name = "DURATION")
    private Integer duration;

    @Column
    private Boolean required = Boolean.FALSE;

    @Column(name = "PERCENT_COMPLETE")
    private Double percentComplete = 0.0;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.pm.ProjectTaskStatus")})
    @Column(name = "STATUS", nullable = false)
    private ProjectTaskStatus status = ProjectTaskStatus.PENDING;

    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Transient
    private Integer level = 0;

    @Transient
    private String project;

    @Transient
    private String taskStatus="None";

    @Transient
    private String activityName;
    @Transient
    private List<Person> persons = new ArrayList<>();
    @Transient
    private Person person;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_FINISHDATE")
    private Date plannedFinishDate;

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

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTUAL_FINISHDATE")
    private Date actualFinishDate;

    @Transient
    private Long ganttId;

    @Transient
    private Boolean hasFiles = Boolean.FALSE;

    @Transient
    private Boolean isShared = Boolean.FALSE;

    @Transient
    private Boolean startWorkflow = Boolean.FALSE;

    @Transient
    private Boolean finishedWorkflow = Boolean.FALSE;

    @Transient
    private Boolean fileTab = Boolean.FALSE;

    public PLMTask() {
        super(PLMObjectType.PROJECTTASK);
    }


}
