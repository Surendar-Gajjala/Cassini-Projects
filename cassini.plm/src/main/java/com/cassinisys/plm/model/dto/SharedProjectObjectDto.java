package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.SharePermission;
import com.cassinisys.plm.model.plm.ShareType;
import com.cassinisys.plm.model.pm.ProjectMemberDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CassiniSystems on 23-09-2020.
 */
@Data
public class SharedProjectObjectDto {

    private Integer id;
    private String name;
    private String description;
    private Double percentComplete = 0.0;
    private String status = null;
    private String phaseName;
    private String activityName;
    private String projectName;
    private String manager;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date plannedStartDate;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date plannedFinishDate;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualStartDate;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualFinishDate;
    private Boolean makeConversationPrivate = Boolean.FALSE;
    private Boolean showConversationCount = Boolean.FALSE;
    private Integer projectManager;
    private Integer assignedTo;
    private String managerFirstName;
    private String managerLastName;
    private Integer activity;
    private Integer project;
    private String managerFullName;
    private Boolean hasManagerImage = Boolean.FALSE;
    private List<ProjectMemberDto> projectMembers = new ArrayList<>();
    private Integer tasks = 0;
    private Integer deliverables = 0;
    private Integer comments = 0;
    private ShareType shareType;
    private SharePermission permission;
    private String sharedBy;
    private String type;
    private String sharedTo;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sharedOn;
}
