package com.cassinisys.is.model.tm;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Rajabrahmachary on 16-06-2017.
 */
@Entity
@Table(name = "IS_FILE")
@PrimaryKeyJoinColumn(name = "FILE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISFile extends CassiniObject {

    @ApiObjectField(required = true)
    @Column(name = "NAME")
    private String name;

    @ApiObjectField(required = true)
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "VERSION", nullable = false)
    @ApiObjectField(required = true)
    private Integer version;

    @Column(name = "SIZE", nullable = false)
    @ApiObjectField(required = true)
    private Long size;

    @Column(name = "LATEST", nullable = false)
    @ApiObjectField(required = true)
    private Boolean latest = true;

    @Column(name = "LOCKED", nullable = false)
    @ApiObjectField(required = true)
    private Boolean locked = false;

    @Column(name = "LOCKED_BY", nullable = false)
    @ApiObjectField(required = true)
    private Integer lockedBy;

    @ApiObjectField
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOCKED_DATE")
    private Date lockedDate;

    @ApiObjectField
    @Column(name = "FILE_URN")
    private String urn;

    @ApiObjectField
    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Transient
    private String uploadedBy;

    public ISFile() {
        super(ISObjectType.FILE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Boolean getLatest() {
        return latest;
    }

    public void setLatest(Boolean latest) {
        this.latest = latest;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Integer getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(Integer lockedBy) {
        this.lockedBy = lockedBy;
    }

    public Date getLockedDate() {
        return lockedDate;
    }

    public void setLockedDate(Date lockedDate) {
        this.lockedDate = lockedDate;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
