package com.cassinisys.documents.model.dm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by swapna on 11/12/18.
 */
@Entity
@Table(name = "DM_DOCUMENT")
@PrimaryKeyJoinColumn(name = "DOCUMENT_ID")
@ApiObject(name = "DOCUMENT")
public class DMDocument extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION")
    @ApiObjectField
    private String description;

    @Column(name = "FOLDER")
    @ApiObjectField
    private Integer folder;

    @Column(name = "SIZE", nullable = false)
    @ApiObjectField(required = true)
    private Long size;

    @Column(name = "VERSION", nullable = false)
    @ApiObjectField(required = true)
    private Integer version;

    @Column(name = "LATEST", nullable = false)
    @ApiObjectField
    private boolean latest = true;

    @Column(name = "IS_LOCKED", nullable = false)
    @ApiObjectField(required = true)
    private boolean locked;

    @Column(name = "LOCKED_BY")
    @ApiObjectField
    private Integer lockedBy;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiObjectField
    @Column(name = "LOCKED_DATE")
    private Date lockedDate;

    public DMDocument() {
        super(DMObjectType.DOCUMENT);
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

    public Integer getFolder() {
        return folder;
    }

    public void setFolder(Integer folder) {
        this.folder = folder;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
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
}

