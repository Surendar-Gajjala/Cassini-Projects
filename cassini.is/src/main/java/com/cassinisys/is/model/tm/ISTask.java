package com.cassinisys.is.model.tm;
/* Model for  ISTask */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.pm.ISProjectSite;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "IS_TASK")
@PrimaryKeyJoinColumn(name = "TASK_ID")
@ApiObject(name = "TM")
public class ISTask extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION")
    @ApiObjectField
    private String description;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_STARTDATE", nullable = false)
    @ApiObjectField(required = true)
    private Date plannedStartDate;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PLANNED_FINISHDATE", nullable = false)
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
    @ApiObjectField(required = true)
    private Date actualFinishDate;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.is.model.tm.TaskStatus")})
    @Column(name = "STATUS", nullable = false)
    @ApiObjectField(required = true)
    private TaskStatus status = TaskStatus.ASSIGNED;

    @Column(name = "PERCENT_COMPLETE", nullable = false)
    @ApiObjectField(required = true)
    private Double percentComplete;

    @Column(name = "WBS_ITEM", nullable = true)
    @ApiObjectField(required = true)
    private Integer wbsItem;

    @Column(name = "SITE", nullable = false)
    @ApiObjectField(required = true)
    private Integer site;

    @Column(name = "PERSON", nullable = true)
    private Integer person;

    @Column(name = "UNIT_OF_WORK")
    private String unitOfWork;

    @Column(name = "TOTAL_UNITS")
    private Double totalUnits = 0.0;

    @Column(name = "INSPECTED_BY")
    private Integer inspectedBy;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INSPECTED_ON")
    private Date inspectedOn;

    @Column
    private Boolean subContract;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.is.model.tm.InspectionResult")})
    @Column(name = "INSPECTION_RESULT")
    private InspectionResult inspectionResult;

    @Column(name = "INSPECTION_REMARKS")
    private String inspectionRemarks;

    @ApiObjectField(required = true)
    @Column(name = "WORKFLOW")
    private Integer workflow;

    @ApiObjectField(required = true)
    @Column(name = "WORKFLOW_STATUS")
    private String wfStatus;

    @Transient
    private Double totalUnitsCompleted;

    @Transient
    private ISProjectSite projectSite;

    @Transient
    private String wbsStructure;

    @Transient
    private Integer files = 0;

    @Transient
    private Integer media = 0;

    @Transient
    private Integer problems = 0;

    @Transient
    private String siteName;

    @Transient
    private String assignedTo;

    @Transient
    private String assignedToNumber;

    public ISTask() {
        super(ISObjectType.TASK);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
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

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(Double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public Integer getWbsItem() {
        return wbsItem;
    }

    public void setWbsItem(Integer wbsItem) {
        this.wbsItem = wbsItem;
    }

    public Integer getSite() {
        return site;
    }

    public void setSite(Integer site) {
        this.site = site;
    }

    public String getUnitOfWork() {
        return unitOfWork;
    }

    public void setUnitOfWork(String unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public Double getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(Double totalUnits) {
        this.totalUnits = totalUnits;
    }

    public Integer getInspectedBy() {
        return inspectedBy;
    }

    public void setInspectedBy(Integer inspectedBy) {
        this.inspectedBy = inspectedBy;
    }

    public Date getInspectedOn() {
        return inspectedOn;
    }

    public void setInspectedOn(Date inspectedOn) {
        this.inspectedOn = inspectedOn;
    }

    public Boolean getSubContract() {
        return subContract;
    }

    public void setSubContract(Boolean subContract) {
        this.subContract = subContract;
    }

    public InspectionResult getInspectionResult() {
        return inspectionResult;
    }

    public void setInspectionResult(InspectionResult inspectionResult) {
        this.inspectionResult = inspectionResult;
    }

    public String getInspectionRemarks() {
        return inspectionRemarks;
    }

    public void setInspectionRemarks(String inspectionRemarks) {
        this.inspectionRemarks = inspectionRemarks;
    }

    public Double getTotalUnitsCompleted() {
        return totalUnitsCompleted;
    }

    public void setTotalUnitsCompleted(Double totalUnitsCompleted) {
        this.totalUnitsCompleted = totalUnitsCompleted;
    }

    public ISProjectSite getProjectSite() {
        return projectSite;
    }

    public void setProjectSite(ISProjectSite projectSite) {
        this.projectSite = projectSite;
    }

    public String getWbsStructure() {
        return wbsStructure;
    }

    public void setWbsStructure(String wbsStructure) {
        this.wbsStructure = wbsStructure;
    }

    public Integer getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Integer workflow) {
        this.workflow = workflow;
    }

    public String getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(String wfStatus) {
        this.wfStatus = wfStatus;
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

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAssignedToNumber() {
        return assignedToNumber;
    }

    public void setAssignedToNumber(String assignedToNumber) {
        this.assignedToNumber = assignedToNumber;
    }
}
