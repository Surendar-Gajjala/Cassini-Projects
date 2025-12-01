package com.cassinisys.plm.model.wf;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_WORKFLOWSTATUS")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PLMWorkflowStart.class, name = "PLMWorkflowStart"),
        @JsonSubTypes.Type(value = PLMWorkflowFinish.class, name = "PLMWorkflowFinish"),
        @JsonSubTypes.Type(value = PLMWorkflowTerminate.class, name = "PLMWorkflowTerminate")
})
public class PLMWorkflowStatus extends CassiniObject {

    @Column(name = "WORKFLOW")
    private Integer workflow;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusType")})
    @Column(name = "TYPE", nullable = false)
    private WorkflowStatusType type = WorkflowStatusType.NORMAL;

    @Column(name = "DIAGRAM_ID")
    private String diagramId;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.wf.WorkflowStatusFlag")})
    @Column(name = "FLAG", nullable = false)
    private WorkflowStatusFlag flag = WorkflowStatusFlag.UNASSIGNED;

    @Column(name = "TRANSITIONED_FROM")
    private Integer transitionedFrom;

    @Column(name = "ITERATION")
    private Integer iteration = 0;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STARTED_TIMESTAMP")
    private Date startedTimestamp;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINISHED_TIMESTAMP")
    private Date finishedTimestamp;

    public PLMWorkflowStatus() {
        super(PLMObjectType.PLMWORKFLOWSTATUS);
    }

    public PLMWorkflowStatus(PLMObjectType type) {
        super(type);
    }


}
