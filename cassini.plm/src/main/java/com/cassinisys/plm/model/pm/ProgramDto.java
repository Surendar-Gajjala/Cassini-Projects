package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
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
public class ProgramDto {
    Integer tagsCount = 0;
    private Integer id;
    private String type;
    private String name;
    private String description;
    private String objectType = "program";
    private Double percentComplete = 0.0;
    private String percent;
    private String status = null;
    private Boolean showConversationCount = Boolean.FALSE;
    private Integer programManager;
    private String managerFirstName;
    private String managerLastName;
    private String managerFullName;
    private Boolean hasManagerImage = Boolean.FALSE;
    private Boolean isSubscribed = Boolean.FALSE;
    private List<ProjectMemberDto> projectMembers = new ArrayList<>();
    private Integer projects = 0;
    private Integer comments = 0;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    private Boolean started = false;
    private String modifiedByName;
}
