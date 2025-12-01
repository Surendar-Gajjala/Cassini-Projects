package com.cassinisys.is.model.workflow;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Entity
@Table(name = "IS_WORKFLOWSTATUS")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISWorkflowStatus extends CassiniObject {

    @ApiObjectField(required = true)
    @Column(name = "WORKFLOW")
    private Integer workflow;

    @ApiObjectField(required = true)
    @Column(name = "NAME", nullable = false)
    private String name;

    @ApiObjectField(required = true)
    @Column(name = "DESCRIPTION")
    private String description;

    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.is.model.workflow.WorkflowStatusType")})
    @Column(name = "TYPE", nullable = false)
    private WorkflowStatusType type = WorkflowStatusType.UNDEFINED;

    @ApiObjectField(required = true)
    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.is.model.workflow.WorkflowStatusFlag")})
    @Column(name = "FLAG", nullable = false)
    private WorkflowStatusFlag flag = WorkflowStatusFlag.UNASSIGNED;

    public ISWorkflowStatus() {
        super(ISObjectType.ISWORKFLOWSTATUS);
    }

    public ISWorkflowStatus(ISObjectType type) {
        super(type);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WorkflowStatusType getType() {
        return type;
    }

    public void setType(WorkflowStatusType type) {
        this.type = type;
    }

    public String getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(String diagramId) {
        this.diagramId = diagramId;
    }

    public WorkflowStatusFlag getFlag() {
        return flag;
    }

    public void setFlag(WorkflowStatusFlag flag) {
        this.flag = flag;
    }
}
