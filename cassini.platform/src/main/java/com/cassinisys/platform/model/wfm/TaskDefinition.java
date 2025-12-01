package com.cassinisys.platform.model.wfm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by lakshmi on 2/1/2016.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "TASKDEFINITION")
@PrimaryKeyJoinColumn(name = "ID")
public class TaskDefinition extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ACTIVITY")
    private Integer activity;

    @Column(name = "OPTIONAL")
    private boolean optional;

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private Integer instanceId;

    public TaskDefinition() {
        super(ObjectType.TASKDEFINITION);
    }

    public WorkflowTask createInstance() {
        WorkflowTask task = new WorkflowTask();
        task.setId(null);
        task.setName(this.getName());
        task.setDescription(this.getDescription());
        task.setOptional(this.isOptional());
        return task;
    }
}
