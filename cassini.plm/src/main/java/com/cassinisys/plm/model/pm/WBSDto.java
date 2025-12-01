package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyam reddy on 20-01-2020.
 */
@Data
public class WBSDto {

    private Integer id;

    private String name;

    private String description;

    private PLMProject project;

    private Integer parent;

    private Integer parentId;

    private Integer sequenceNumber;
    @Transient
    private String plannedStartDatePrint;
    @Transient
    private String plannedFinishDatePrint;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date plannedFinishDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualFinishDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date plannedStartDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualStartDate;

    private Person person;

    private String status;

    private Double percentComplete = 0.0;

    private Integer level = 0;

    private Integer count = 0;

    private Boolean hasBom = Boolean.FALSE;

    private Boolean expanded = Boolean.FALSE;

    private Boolean finishMilestone = Boolean.FALSE;

    private List<WBSDto> children = new ArrayList<>();

    private List<PLMTask> activityTasks = new ArrayList<>();

    private List<ProjectTemplateTask> templateActivityTasks = new ArrayList<>();

    private String objectType;

    private Integer createdBy;

    private Integer modifiedBy;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private Integer workflow;

    private Integer duration;

    private Boolean hasFiles = Boolean.FALSE;

    private Boolean isShared = Boolean.FALSE;

    private Boolean fileTab = Boolean.FALSE;
}
