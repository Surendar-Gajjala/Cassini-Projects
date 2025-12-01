package com.cassinisys.is.model.dm;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by subramanyamreddy on 018 18-Nov -17.
 */
@Entity
@Table(name = "IS_TOPDOCUMENT")
@PrimaryKeyJoinColumn(name = "DOCUMENT_ID")
@ApiObject(name = "DOCUMENT")
public class ISTopDocument extends CassiniObject {

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

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.dm.DocType")})
    @Column(name = "DOCUMENT_TYPE", nullable = false)
    @ApiObjectField(required = true)
    private DocType documentType = DocType.DEFAULT;

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

    @ApiObjectField
    @Column(name = "FILE_URN")
    private String urn;

    @ApiObjectField
    @Column(name = "THUMBNAIL")
    private String thumbnail;

    public ISTopDocument() {
        super(ISObjectType.DOCUMENT);
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

    public DocType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocType documentType) {
        this.documentType = documentType;
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
}
