package com.cassinisys.is.model.workflow;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Entity
@Table(name = "IS_WORKFLOW")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISWorkflow extends CassiniObject {

    @ApiObjectField(required = true)
    @Column(name = "NAME", nullable = false)
    private String name;

    @ApiObjectField(required = true)
    @Column(name = "DESCRIPTION")
    private String description;

    @ApiObjectField(required = true)
    @Column(name = "DIAGRAM")
    private String diagram;

    @ApiObjectField(required = true)
    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    @ApiObjectField(required = true)
    @Column(name = "STARTED", nullable = false)
    private Boolean started = false;

    @ApiObjectField(required = true)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STARTED_ON")
    private Date startedOn;

    @ApiObjectField(required = true)
    @Column(name = "FINISHED", nullable = false)
    private Boolean finished = false;

    @ApiObjectField(required = true)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINISHED_ON")
    private Date finishedOn;

    @ApiObjectField(required = true)
    @Column(name = "ATTACHED_TO")
    private Integer attachedTo;

    @ApiObjectField(required = true)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "START")
    private ISWorkflowStart start;

    @ApiObjectField(required = true)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "FINISH")
    private ISWorkflowFinish finish;

    @ApiObjectField(required = true)
    @Column(name = "CURRENT_STATUS")
    private Integer currentStatus;
    @Transient
    private List<ISWorkflowStatus> statuses = new ArrayList<>();
    @Transient
    private List<ISWorkflowTransition> transitions = new ArrayList<>();

    public ISWorkflow() {
        super(ISObjectType.ISWORKFLOW);
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

    public String getDiagram() {
        return diagram;
    }

    public void setDiagram(String diagram) {
        this.diagram = diagram;
    }

    public String getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(String diagramId) {
        this.diagramId = diagramId;
    }

    public Boolean getStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public Date getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(Date startedOn) {
        this.startedOn = startedOn;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Date getFinishedOn() {
        return finishedOn;
    }

    public void setFinishedOn(Date finishedOn) {
        this.finishedOn = finishedOn;
    }

    public Integer getAttachedTo() {
        return attachedTo;
    }

    public void setAttachedTo(Integer attachedTo) {
        this.attachedTo = attachedTo;
    }

    public ISWorkflowStart getStart() {
        return start;
    }

    public void setStart(ISWorkflowStart start) {
        this.start = start;
    }

    public ISWorkflowFinish getFinish() {
        return finish;
    }

    public void setFinish(ISWorkflowFinish finish) {
        this.finish = finish;
    }

    public Integer getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }

    public List<ISWorkflowStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<ISWorkflowStatus> statuses) {
        this.statuses = statuses;
    }

    public List<ISWorkflowTransition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<ISWorkflowTransition> transitions) {
        this.transitions = transitions;
    }

}
