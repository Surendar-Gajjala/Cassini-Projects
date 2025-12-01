package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.model.security.IDtoType;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.req.PLMRequirementDocumentRevision;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by subramanyam on 03-08-2021.
 */
@Data
public class RequirementDocumentsDto implements IDtoType {
    Integer tagsCount = 0;
    private Integer id;
    private String number;
    private String name;
    private String type;
    private String description;
    private String owner;
    private Integer latestRevision;
    private PLMLifeCyclePhase lifeCyclePhase;
    private PLMRequirementDocumentRevision revision;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    private String createdByName;
    private String modifiedByName;
    private String objectType = "REQUIREMENTDOCUMENT";

    @Override
    public String getObjectType() {
        return "REQUIREMENTDOCUMENT";
    }
}
