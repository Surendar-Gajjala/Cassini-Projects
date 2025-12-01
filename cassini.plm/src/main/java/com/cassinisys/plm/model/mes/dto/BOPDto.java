package com.cassinisys.plm.model.mes.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@Data
public class BOPDto {
    private Integer id;
    private Integer type;
    private String name;
    private String number;
    private String description;
    private String typeName;
    private String revision;
    private Integer latestRevision;
    private Integer latestReleasedRevision;
    private PLMLifeCyclePhase lifeCyclePhase;
    private Date releasedDate;
    private Boolean released = Boolean.FALSE;
    private Boolean rejected = Boolean.FALSE;
    private String status = "NONE";
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;

    private String mbomName;
    private String mbomNumber;
    private String mbomRevision;
    private Integer mbomRevisionId;

    private String createdByName;
    private String modifiedByName;
    private String createdDateString;
    private String modifiedDateString;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private List<FileDto> itemFiles = new LinkedList<>();
    private List<BOPRevisionDto> bopRevisions = new LinkedList<>();
    private String objectType;
    
    private Boolean onHold = false;
    private Boolean startWorkflow = Boolean.FALSE;
    private Boolean finishWorkflow = Boolean.FALSE;
    private String workflowSettingStatus = "NONE";
    private String workflowSettingStatusType = "UNDEFINED";
    private Boolean cancelWorkflow = false;
}
