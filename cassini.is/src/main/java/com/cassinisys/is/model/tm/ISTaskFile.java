package com.cassinisys.is.model.tm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by Rajabrahmachary on 16-06-2017.
 */
@Entity
@Table(name = "IS_TASKFILE")
@PrimaryKeyJoinColumn(name = "FILE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "TM")
public class ISTaskFile extends ISFile {

    @Column(name = "TASK_ID")
    @ApiObjectField(required = true)
    private Integer task;

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

}


