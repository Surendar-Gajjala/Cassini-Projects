package com.cassinisys.drdo.model.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Nageshreddy on 26-02-2019.
 */
public class SummaryStatusDTO implements Serializable {
    private Integer id;
    private String name;
    private Double percentComplete = 0.0;
    private Boolean onHold = false;
    private String toolTip;
    private String partTrackingStep;
    @JsonSerialize(
            using = CustomDateSerializer.class
    )
    @JsonDeserialize(
            using = CustomDateDeserializer.class
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Date initialiseDate;
    @JsonSerialize(
            using = CustomDateSerializer.class
    )
    @JsonDeserialize(
            using = CustomDateDeserializer.class
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedDate;

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(Double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public Boolean getOnHold() {
        return onHold;
    }

    public void setOnHold(Boolean onHold) {
        this.onHold = onHold;
    }

    public String getPartTrackingStep() {
        return partTrackingStep;
    }

    public void setPartTrackingStep(String partTrackingStep) {
        this.partTrackingStep = partTrackingStep;
    }

    public Date getInitialiseDate() {
        return initialiseDate;
    }

    public void setInitialiseDate(Date initialiseDate) {
        this.initialiseDate = initialiseDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}
