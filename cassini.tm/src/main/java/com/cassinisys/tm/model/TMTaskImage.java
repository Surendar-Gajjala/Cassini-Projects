package com.cassinisys.tm.model;

import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by reddy on 9/6/16.
 */
@Entity
@Table(name = "TASK_IMAGE")
public class TMTaskImage implements Serializable {

    @Id
    @SequenceGenerator(name = "TASK_ID_GEN", sequenceName = "TASK_ID_SEQ ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_ID_GEN")
    @Column(name = "ROWID", nullable = false)
    @ApiObjectField(required = true)
    private Integer id;

    @Column(name = "TASK")
    private Integer task;

    @Column(name = "IMAGE_DATA")
    private String imageData;

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

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
