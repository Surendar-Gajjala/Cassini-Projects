package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 23-12-2020.
 */
@Data
public class ProgramProjectDto {
    private Integer id;
    private Integer program;
    private Integer project;
    private Integer parent;
    private String name;
    private String description;
    private Double percentComplete = 0.0;
    private String percent = "0";
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
    private ProgramProjectType type;
    private Integer level = 0;

    private String pStartDate;
    private String pFinishDate;
    private String aStartDate;
    private String aFinishDate;
    private List<ProgramProjectDto> children = new LinkedList<>();
}
