package com.cassinisys.is.model.pm;
/**
 * Model for ISWbs
 */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "IS_WBS")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "WBS_ID")
@ApiObject(name = "PM")
public class ISWbs extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Transient
    private Integer cassiniId;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    @Column(name = "PARENT_WBS")
    @ApiObjectField(required = true)
    private Integer parent;

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION")
    @ApiObjectField
    private String description;

    @Column(name = "WEIGHTAGE")
    @ApiObjectField
    private Double weightage;  //must be a decimal greater than 0 and less than 1. e.g. 0.25

    @Column(name = "DURATION")
    @ApiObjectField(required = false)
    private Integer duration;

    @Column(name = "PREDECESSORS", nullable = false)
    @ApiObjectField(required = false)
    private String predecessors;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_START_DATE")
    @ApiObjectField(required = true)
    private Date plannedStartDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_FINISH_DATE")
    @ApiObjectField(required = true)
    private Date plannedFinishDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTUAL_START_DATE")
    @ApiObjectField
    private Date actualStartDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTUAL_FINISH_DATE")
    @ApiObjectField
    private Date actualFinishDate;

    @ApiObjectField(required = true)
    @Column(name = "WBS_ITEM")
    private Integer wbsItem;

    @ApiObjectField(required = true)
    @Column(name = "PERCENTAGECOMPLETE")
    private Double percentageComplete;

    @Column(name = "ASSIGNED_TO")
    private Integer assignedTo;

    @Transient
    private List<ISWbs> children = new ArrayList<>();

    public ISWbs() {
        super(ISObjectType.WBS);
    }

    public Double getPercentageComplete() {
        return percentageComplete;
    }

    public void setPercentageComplete(Double percentageComplete) {
        this.percentageComplete = percentageComplete;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getWeightage() {
        return weightage;
    }

    public void setWeightage(Double weightage) {
        this.weightage = weightage;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPredecessors() {
        return predecessors;
    }

    public void setPredecessors(String predecessors) {
        this.predecessors = predecessors;
    }

    public Date getPlannedStartDate() {
        return plannedStartDate;
    }

    public void setPlannedStartDate(Date plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public Date getPlannedFinishDate() {
        return plannedFinishDate;
    }

    public void setPlannedFinishDate(Date plannedFinishDate) {
        this.plannedFinishDate = plannedFinishDate;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Date getActualFinishDate() {
        return actualFinishDate;
    }

    public void setActualFinishDate(Date actualFinishDate) {
        this.actualFinishDate = actualFinishDate;
    }

    public List<ISWbs> getChildren() {
        return children;
    }

    public void setChildren(List<ISWbs> children) {
        this.children = children;
    }

    public Integer getWbsItem() {
        return wbsItem;
    }

    public void setWbsItem(Integer wbsItem) {
        this.wbsItem = wbsItem;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Integer getCassiniId() {
        return cassiniId;
    }

    public void setCassiniId(Integer cassiniId) {
        this.cassiniId = cassiniId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ISWbs other = (ISWbs) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }

}