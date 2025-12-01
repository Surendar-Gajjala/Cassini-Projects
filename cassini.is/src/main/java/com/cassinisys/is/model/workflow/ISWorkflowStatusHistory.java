package com.cassinisys.is.model.workflow;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by reddy on 5/30/17.
 */
@Entity
@Table(name = "IS_WORKFLOWSTATUSHISTORY")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISWorkflowStatusHistory implements Serializable {
    @Id
    @SequenceGenerator(name = "WORKFLOWSTATUSASSIGNMENT_ID_GEN", sequenceName = "WORKFLOWSTATUSASSIGNMENT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WORKFLOWSTATUSASSIGNMENT_ID_GEN")
    @Column(name = "ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @ApiObjectField
    @Column(name = "WORKFLOW")
    private Integer workflow;

    @ApiObjectField
    @Column(name = "STATUS")
    private Integer status;

    @ApiObjectField
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp;

    @Transient
    private List<ISWorkflowStatusActionHistory> statusActionHistory = new ArrayList<>();

    @Transient
    private ISWorkflowStatus statusObject;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Integer workflow) {
        this.workflow = workflow;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<ISWorkflowStatusActionHistory> getStatusActionHistory() {
        return statusActionHistory;
    }

    public void setStatusActionHistory(List<ISWorkflowStatusActionHistory> statusActionHistory) {
        this.statusActionHistory = statusActionHistory;
    }

    public ISWorkflowStatus getStatusObject() {
        return statusObject;
    }

    public void setStatusObject(ISWorkflowStatus statusObject) {
        this.statusObject = statusObject;
    }
}
