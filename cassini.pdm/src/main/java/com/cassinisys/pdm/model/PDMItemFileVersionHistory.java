package com.cassinisys.pdm.model;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Surendar Reddy on 20-01-2017.
 */
@Entity
@Table(name = "PDM_ITEMFILEVERSIONHISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PDM")
public class PDMItemFileVersionHistory implements Serializable {

    @ApiObjectField
    @Id
    @SequenceGenerator(name = "FILEVERSIONHISTORY_ID_GEN", sequenceName = "FILEVERSIONHISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILEVERSIONHISTORY_ID_GEN")
    @Column(name = "ROWID")
    private Integer id;

    @ApiObjectField(required = true)
    @Column(name = "FILE", nullable = false)
    private Integer file;

    @ApiObjectField(required = true)
    @Column(name = "ITEM", nullable = false)
    private Integer item;

    @ApiObjectField(required = true)
    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    @ApiObjectField(required = true)
    @Column(name = "OLD_VERSION", nullable = false)
    private Integer oldVersion;

    @ApiObjectField(required = true)
    @Column(name = "NEW_VERSION", nullable = false)
    private Integer newVersion;

    @ApiObjectField(required = true)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Column(name = "TIMESTAMP")
    private Date timeStamp = new Date();

    @ApiObjectField(required = true)
    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFile() {
        return file;
    }

    public void setFile(Integer file) {
        this.file = file;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(Integer oldVersion) {
        this.oldVersion = oldVersion;
    }

    public Integer getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(Integer newVersion) {
        this.newVersion = newVersion;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
