package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by smukka on 23-08-2022.
 */
@Data
public class DrillDownProjectDto {
    /*Program Project*/
    private Integer id;
    private Integer program;
    private String name;
    private String description;
    private Integer parent;
    private Integer project;
    private ProgramProjectType projectType = ProgramProjectType.GROUP;
    private String percent = "0";
    private Integer projectManager;
    /*WBS*/

    private Integer sequenceNumber;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date plannedFinishDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date plannedStartDate;

    /*Activity*/
    private Integer wbs;
    private String type;
    private Integer duration;
    private Integer[] predecessors;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualFinishDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualStartDate;

    private Integer assignedTo;
    private String status;
    private Double percentComplete = 0.0;
    private Integer workflow;

    private Integer activity;
    private Boolean required = Boolean.FALSE;
    private Boolean finishMilestone = Boolean.FALSE;
    private Boolean finishedWorkflow = Boolean.FALSE;
    private String assignedToName;
    private String objectType;
    private Integer childCount = 0;
    private List<DrillDownProjectDto> children = new ArrayList<>();
    private Boolean overDue = false;
    private Boolean validUser = true;
    private String typeName;
    private Boolean isShared = Boolean.FALSE;
    private Boolean hasFiles = Boolean.FALSE;
}
