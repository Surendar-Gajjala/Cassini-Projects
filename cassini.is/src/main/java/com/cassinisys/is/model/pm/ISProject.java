package com.cassinisys.is.model.pm;
/* Model for ISProject */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "IS_PROJECT")
@PrimaryKeyJoinColumn(name = "PROJECT_ID")
@ApiObject(name = "PM")
public class ISProject extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION")
    @ApiObjectField(required = true)
    private String description;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @ApiObjectField(required = true)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_STARTDATE")
    private Date plannedStartDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_FINISHDATE")
    @ApiObjectField(required = true)
    private Date plannedFinishDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTUAL_STARTDATE")
    @ApiObjectField
    private Date actualStartDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTUAL_FINISHDATE")
    @ApiObjectField
    private Date actualFinishDate;

    @Column(name = "STATUS")
    @ApiObjectField(required = true)
    private String status;

    @Column(name = "PROJECT_OWNER", nullable = false)
    @ApiObjectField(required = true)
    private Integer projectOwner;

    @Column(name = "PORTFOLIO")
    @ApiObjectField(required = true)
    private Integer portfolio;

    @Column(name = "LOCKED")
    private Boolean locked = false;

    @Transient
    private Double percentComplete = 0.0;

    @Transient
    private Integer totalTasks = 0;

    @Transient
    private Integer pendingTasks = 0;

    @Transient
    private Integer finishedTasks = 0;

    @Transient
    private Integer delayedTasks = 0;

    @Transient
    private Integer media = 0;

    @Transient
    private Integer problems = 0;

    @Transient
    private Integer sites = 0;

    @Transient
    private Integer documents = 0;

    public ISProject() {
        super(ISObjectType.PROJECT);
    }

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(Integer projectOwner) {
        this.projectOwner = projectOwner;
    }

    public Integer getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Integer portfolio) {
        this.portfolio = portfolio;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(Double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public Integer getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Integer totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Integer getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(Integer pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public Integer getFinishedTasks() {
        return finishedTasks;
    }

    public void setFinishedTasks(Integer finishedTasks) {
        this.finishedTasks = finishedTasks;
    }

    public Integer getDelayedTasks() {
        return delayedTasks;
    }

    public void setDelayedTasks(Integer delayedTasks) {
        this.delayedTasks = delayedTasks;
    }

    public Integer getMedia() {
        return media;
    }

    public void setMedia(Integer media) {
        this.media = media;
    }

    public Integer getProblems() {
        return problems;
    }

    public void setProblems(Integer problems) {
        this.problems = problems;
    }

    public Integer getSites() {
        return sites;
    }

    public void setSites(Integer sites) {
        this.sites = sites;
    }

    public Integer getDocuments() {
        return documents;
    }

    public void setDocuments(Integer documents) {
        this.documents = documents;
    }
}
