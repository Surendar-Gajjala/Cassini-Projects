package com.cassinisys.plm.model.pm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
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
public class ProgramTemplateProjectDto {
    private Integer id;
    private Integer template;
    private Integer projectTemplate;
    private Integer parent;
    private String name;
    private String description;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    private String createdByName;
    private String modifiedByName;
    private ProgramProjectType type;
    private String managerName;
    private List<ProgramTemplateProjectDto> children = new LinkedList<>();
}
