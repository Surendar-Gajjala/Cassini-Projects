package com.cassinisys.is.model.pm;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by subramanyam on 21-02-2020.
 */
public class ProjectDto {

    private Integer id;

    private String name;

    private String description;

    private Double percentComplete;

    private String assignedTo;

    private String phoneNumber;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @ApiObjectField(required = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date plannedStartDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @ApiObjectField(required = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date plannedFinishDate;

    private Date actualStartDate;

    private Date actualFinishDate;

    private Integer totalTasks = 0;

    private Integer pendingTasks = 0;

    private Integer finishedTasks = 0;

    private Integer files = 0;

    private Integer media = 0;

    private Integer problems = 0;

    private Integer sites = 0;

    private Integer comments = 0;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(Double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
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

    public Integer getFiles() {
        return files;
    }

    public void setFiles(Integer files) {
        this.files = files;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }
}
