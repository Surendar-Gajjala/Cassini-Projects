package com.cassinisys.is.model.tm;

import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 30-01-2020.
 */
@Entity
@Table(name = "IS_TASKCOMPLETIONRESOURCE")
public class ISTaskCompletionResource implements Serializable {

    @Id
    @SequenceGenerator(name = "IS_TASKCOMPLETIONRESOURCE_ID_GEN", sequenceName = "IS_TASKCOMPLETIONRESOURCE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IS_TASKCOMPLETIONRESOURCE_ID_GEN")
    @Column(name = "ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "TASK")
    private Integer task;

    @Column(name = "TASK_HISTORY")
    private Integer taskHistory;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "RESOUTCE_TYPE")
    private String resourceType;

    @Column(name = "RESOUTCE_ID")
    private Integer resourceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public Integer getTaskHistory() {
        return taskHistory;
    }

    public void setTaskHistory(Integer taskHistory) {
        this.taskHistory = taskHistory;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }
}
