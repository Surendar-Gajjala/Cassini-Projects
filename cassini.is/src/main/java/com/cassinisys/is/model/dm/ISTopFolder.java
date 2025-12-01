package com.cassinisys.is.model.dm;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyamreddy on 018 18-Nov -17.
 */
@Entity
@Table(name = "IS_TOPFOLDER")
@PrimaryKeyJoinColumn(name = "FOLDER_ID")
@ApiObject(name = "DOCUMENT")
public class ISTopFolder extends CassiniObject {

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION")
    @ApiObjectField
    private String description;

    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.dm.DocType")})
    @Column(name = "FOLDER_TYPE", nullable = false)
    @ApiObjectField(required = true)
    private DocType folderType = DocType.DEFAULT;

    @Column(name = "PARENT_FOLDER")
    @ApiObjectField
    private Integer parent;

    @Column(name = "IS_LOCKED", nullable = false)
    @ApiObjectField(required = true)
    private boolean isLocked;

    @Column(name = "LOCKED_BY")
    @ApiObjectField
    private Integer lockedBy;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOCKED_DATE")
    @ApiObjectField
    private Date lockedDate;

    @Transient
    private List<ISTopFolder> children = new ArrayList<>();

    public ISTopFolder() {
        super(ISObjectType.FOLDER);
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

    public DocType getFolderType() {
        return folderType;
    }

    public void setFolderType(DocType folderType) {
        this.folderType = folderType;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
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

    public List<ISTopFolder> getChildren() {
        return children;
    }

    public void setChildren(List<ISTopFolder> children) {
        this.children = children;
    }
}
