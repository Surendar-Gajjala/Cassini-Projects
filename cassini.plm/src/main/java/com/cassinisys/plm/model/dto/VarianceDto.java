package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.cm.VarianceEffectivityType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

/**
 * Created by subramanyam on 20-03-2020.
 */
@Data
public class VarianceDto {

    Integer tagsCount = 0;
    private Integer id;
    private String title;
    private String description;
    private VarianceEffectivityType effecitivityType;
    private String varianceNumber;
    private String reasonForVariance;
    private String currentRequirement;
    private String requirementDeviation;
    private boolean isRecurring;
    private String status;
    private String varianceFor;
    private String statusType;
    private Person originator;
    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    private Date createdDate;
    private Person createdBy;
    private Boolean onHold = false;
    private String objectType;
    private String subType;
    private Boolean startWorkflow = Boolean.FALSE;
    private String workflowSettingStatus = "NONE";
    private String workflowSettingStatusType = "UNDEFINED";
    private Boolean finishWorkflow = false;
    private Boolean cancelWorkflow = false;
}
