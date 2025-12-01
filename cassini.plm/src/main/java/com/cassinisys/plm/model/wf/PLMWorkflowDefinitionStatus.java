package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by GSR on 19-05-2017.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_WORKFLOWDEFINITIONSTATUS")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PLMWorkflowDefinitionStart.class, name = "PLMWorkflowDefinitionStart"),
        @JsonSubTypes.Type(value = PLMWorkflowDefinitionFinish.class, name = "PLMWorkflowDefinitionFinish"),
        @JsonSubTypes.Type(value = PLMWorkflowDefinitionTerminate.class, name = "PLMWorkflowDefinitionTerminate")
})
public class PLMWorkflowDefinitionStatus extends CassiniObject {

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TYPE")
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    private WorkflowStatusType type = WorkflowStatusType.NORMAL;

    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    public PLMWorkflowDefinitionStatus() {
        super(PLMObjectType.PLMWORKFLOWDEFINITIONSTATUS);
    }

    public PLMWorkflowDefinitionStatus(PLMObjectType type) {
        super(type);
    }



}
