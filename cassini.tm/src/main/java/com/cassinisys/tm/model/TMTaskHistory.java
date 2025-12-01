package com.cassinisys.tm.model;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Rajabrahmachary on 05-07-2016.
 */
@Entity
@Table(name = "TASK_HISTORY")
@ApiObject(name = "TM")
public class TMTaskHistory implements Serializable {
    @Id
    @SequenceGenerator(name = "TASK_ID_GEN", sequenceName = "TASK_ID_SEQ ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)

    private Integer rowId;

    @Column(name = "TASK", nullable = false)
    @ApiObjectField(required = true)
    private Integer task;

    @Column(name = "TIMESTAMP", nullable = false)
    @ApiObjectField(required = true)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date timeStamp;

    @Column(name = "UPDATED_BY", nullable = false)
    @ApiObjectField(required = true)
    private Integer updatedBy;

    @Column(name = "OLD_STATUS", nullable = false)
    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.tm.model.TaskStatus")})
    private TaskStatus oldStatus;

    @Column(name = "NEW_STATUS", nullable = false)
    @ApiObjectField(required = true)
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.tm.model.TaskStatus")})
    private TaskStatus newStatus;

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public TaskStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(TaskStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public TaskStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(TaskStatus newStatus) {
        this.newStatus = newStatus;
    }
}
