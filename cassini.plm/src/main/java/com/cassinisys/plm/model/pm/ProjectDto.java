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
 * Created by subramanyam on 23-12-2020.
 */
@Data
public class ProjectDto {
    Integer tagsCount = 0;
    private Integer id;
    private String name;
    private String type;
    private String description;
    private String objectType = "project";
    private Double percentComplete = 0.0;
    private String status = null;
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
    private String managerFirstName;
    private String managerLastName;
    private String managerFullName;
    private Boolean hasManagerImage = Boolean.FALSE;
    private Boolean isSubscribed = Boolean.FALSE;
    private List<ProjectMemberDto> projectMembers = new ArrayList<>();
    private Integer tasks = 0;
    private Integer deliverables = 0;
    private Integer comments = 0;
}
