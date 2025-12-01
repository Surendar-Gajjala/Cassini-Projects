package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
@Data
public class DCODto {

    Integer tagsCount = 0;
    private Integer id;
    private String dcoNumber;
    private String dcoType;
    private String title;
    private String description;
    private String reasonForChange;
    private String changeAnalyst;
    private String status;
    private Boolean isReleased = Boolean.FALSE;
    private String Workflow;
    private String createdBy;
    private String modifiedBy;
    private Boolean onHold = false;
    private String objectType;
    private String subType;
    private Boolean startWorkflow = Boolean.FALSE;
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    private WorkflowStatusType statusType = WorkflowStatusType.UNDEFINED;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date releasedDate;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    private String workflowSettingStatus = "NONE";
    private String workflowSettingStatusType = "UNDEFINED";
    private Boolean finishWorkflow = false;
    private Boolean cancelWorkflow = false;

}
