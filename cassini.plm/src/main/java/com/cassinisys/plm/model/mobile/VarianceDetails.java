package com.cassinisys.plm.model.mobile;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.cassinisys.plm.model.cm.VarianceEffectivityType;
import com.cassinisys.plm.model.cm.VarianceFor;
import com.cassinisys.plm.model.cm.VarianceType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by subramanyam on 02-12-2020.
 */
@Data
public class VarianceDetails {
    private Integer id;
    private String title;
    private String type;
    private VarianceType varianceType;
    private String varianceNumber;
    private String description;
    private String reasonForVariance;
    private String currentRequirement;
    private String requirementDeviation;
    private String status;
    private VarianceFor varianceFor;
    private Boolean isRecurring;
    private String originator;
    private VarianceEffectivityType effectivityType;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveFrom;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveTo;

    private String correctiveAction;
    private String preventiveAction;
    private String notes;
    private String createdBy;
    private String modifiedBy;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    private Integer workflow;
}
