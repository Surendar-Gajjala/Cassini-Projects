package com.cassinisys.drdo.model.bom;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subra on 07-10-2018.
 */
@Entity
@Table(name = "FILE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class File extends CassiniObject {

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

    @Transient
    List<Comment> comments = new ArrayList<>();

    public File() {
        super(DRDOObjectType.FILE);
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
