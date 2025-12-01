package com.cassinisys.is.model.workflow;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by GSR on 19-05-2017.
 */
@Entity
@Table(name = "IS_WORKFLOWDEFINITIONTRANSITION")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISWorkflowDefinitionTransition extends CassiniObject {

    @ApiObjectField(required = true)
    @Column(name = "WORKFLOW")
    private Integer workflow;

    @ApiObjectField(required = true)
    @Column(name = "NAME")
    private String name;

    @ApiObjectField(required = true)
    @Column(name = "FROM_STATUS")
    private Integer fromStatus;

    @ApiObjectField(required = true)
    @Column(name = "TO_STATUS")
    private Integer toStatus;

    @ApiObjectField(required = true)
    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    @Transient
    private String fromStatusDiagramId;

    @Transient
    private String toStatusDiagramId;

    public ISWorkflowDefinitionTransition() {
        super(ISObjectType.ISWORKFLOWDEFINITIONTRANSITION);
    }

    public Integer getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Integer workflow) {
        this.workflow = workflow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(Integer fromStatus) {
        this.fromStatus = fromStatus;
    }

    public Integer getToStatus() {
        return toStatus;
    }

    public void setToStatus(Integer toStatus) {
        this.toStatus = toStatus;
    }

    public String getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(String diagramId) {
        this.diagramId = diagramId;
    }

    public String getFromStatusDiagramId() {
        return fromStatusDiagramId;
    }

    public void setFromStatusDiagramId(String fromStatusDiagramId) {
        this.fromStatusDiagramId = fromStatusDiagramId;
    }

    public String getToStatusDiagramId() {
        return toStatusDiagramId;
    }

    public void setToStatusDiagramId(String toStatusDiagramId) {
        this.toStatusDiagramId = toStatusDiagramId;
    }
}
