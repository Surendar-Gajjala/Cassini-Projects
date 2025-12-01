package com.cassinisys.is.model.pm;
/* Model for ISProjectWbs */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

@Entity
@Table(name = "IS_PROJECTWBS")
@PrimaryKeyJoinColumn(name = "WBS_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "PM")
public class ISProjectWbs extends ISWbs {

    private static final long serialVersionUID = 1L;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    @Transient
    private Double percentageComplete = 0.0;

    public ISProjectWbs() {
    }

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public Double getPercentageComplete() {
        return percentageComplete;
    }

    public void setPercentageComplete(Double percentageComplete) {
        this.percentageComplete = percentageComplete;
    }

    @Override
    public Integer getProject() {
        return project;
    }

    @Override
    public void setProject(Integer project) {
        this.project = project;
    }
}
