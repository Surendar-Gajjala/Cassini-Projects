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
 * Created by GSR on 19-05-2017.
 */
@Entity
@Table(name = "IS_WORKFLOWDEFINITIONSTATUS")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISWorkflowDefinitionStatus extends CassiniObject {

    @ApiObjectField(required = true)
    @Column(name = "WORKFLOW")
    private Integer workflow;

    @ApiObjectField(required = true)
    @Column(name = "NAME")
    private String name;

    @ApiObjectField(required = true)
    @Column(name = "DESCRIPTION")
    private String description;

    @ApiObjectField(required = true)
    @Column(name = "TYPE")
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.is.model.workflow.WorkflowStatusType")})
    private WorkflowStatusType type = WorkflowStatusType.UNDEFINED;

    @ApiObjectField(required = true)
    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    public ISWorkflowDefinitionStatus() {
        super(ISObjectType.ISWORKFLOWDEFINITIONSTATUS);
    }

    public ISWorkflowDefinitionStatus(ISObjectType type) {
        super(type);
    }

    public Integer getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Integer workflow) {
        this.workflow = workflow;
    }

    public String getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(String diagramId) {
        this.diagramId = diagramId;
    }

    public WorkflowStatusType getType() {
        return type;
    }

    public void setType(WorkflowStatusType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
