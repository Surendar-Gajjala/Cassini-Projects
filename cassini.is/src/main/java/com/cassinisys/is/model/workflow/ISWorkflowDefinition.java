package com.cassinisys.is.model.workflow;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 11-01-2020.
 */
@Entity
@Table(name = "IS_WORKFLOWDEFINITION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISWorkflowDefinition extends CassiniObject {

    @ApiObjectField(required = true)
    @Column(name = "NAME")
    private String name;

    @ApiObjectField(required = true)
    @Column(name = "DESCRIPTION")
    private String description;

    @ApiObjectField(required = true)
    @Column(name = "DIAGRAM")
    private String diagram;

    @ApiObjectField(required = true)
    @Column(name = "DIAGRAM_ID")
    private String diagramID;

    @ApiObjectField(required = true)
    @Column(name = "ASSIGNABLE_TO")
    @Type(type = "com.cassinisys.platform.util.converter.StringArrayType")
    private String[] assignableTo;

    @ApiObjectField(required = true)
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "START")
    private ISWorkflowDefinitionStart start;

    @ApiObjectField(required = true)
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "FINISH")
    private ISWorkflowDefinitionFinish finish;
    @Transient
    private List<ISWorkflowDefinitionStatus> statuses = new ArrayList<>();
    @Transient
    private List<ISWorkflowDefinitionTerminate> terminations = new ArrayList<>();
    @Transient
    private List<ISWorkflowDefinitionTransition> transitions = new ArrayList<>();

    public ISWorkflowDefinition() {
        super(ISObjectType.ISWORKFLOWDEFINITION);
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

    public String getDiagramID() {
        return diagramID;
    }

    public void setDiagramID(String diagramID) {
        this.diagramID = diagramID;
    }

    public String[] getAssignableTo() {
        return assignableTo;
    }

    public void setAssignableTo(String[] assignableTo) {
        this.assignableTo = assignableTo;
    }

    public ISWorkflowDefinitionStart getStart() {
        return start;
    }

    public void setStart(ISWorkflowDefinitionStart start) {
        this.start = start;
    }

    public ISWorkflowDefinitionFinish getFinish() {
        return finish;
    }

    public void setFinish(ISWorkflowDefinitionFinish finish) {
        this.finish = finish;
    }

    public List<ISWorkflowDefinitionStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<ISWorkflowDefinitionStatus> statuses) {
        this.statuses = statuses;
    }

    public List<ISWorkflowDefinitionTerminate> getTerminations() {
        return terminations;
    }

    public void setTerminations(List<ISWorkflowDefinitionTerminate> terminations) {
        this.terminations = terminations;
    }

    public List<ISWorkflowDefinitionTransition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<ISWorkflowDefinitionTransition> transitions) {
        this.transitions = transitions;
    }
}
