package com.cassinisys.is.model.tm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by subramanyam reddy on 24-09-2018.
 */
@Entity
@Table(name = "IS_PROJECTTASKIMAGE")
@ApiObject(name = "TM")
public class ISProjectTaskImage implements Serializable {

    @javax.persistence.Id
    @SequenceGenerator(name = "TASK_IMAGE_ID_GEN", sequenceName = "TASK_IMAGE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_IMAGE_ID_GEN")
    @Column(name = "ID", nullable = false)
    @ApiObjectField(required = true)
    private Integer Id;

    @ApiObjectField
    @Column(name = "TASK")
    private Integer task;

    @JsonIgnore
    @Column(name = "IMAGE")
    private byte[] image;

    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Column(name = "IMAGE_SIZE")
    private Long imageSize;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPLOAD_DATE")
    private Date uploadDate;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Long getImageSize() {
        return imageSize;
    }

    public void setImageSize(Long imageSize) {
        this.imageSize = imageSize;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
}
