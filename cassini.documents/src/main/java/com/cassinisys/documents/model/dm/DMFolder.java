package com.cassinisys.documents.model.dm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "DM_FOLDER")
@PrimaryKeyJoinColumn(name = "FOLDER_ID")
public class DMFolder extends CassiniObject {

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION")
    @ApiObjectField
    private String description;
    
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
    private List<DMFolder> children = new ArrayList<>();

    public DMFolder() {
        super(DMObjectType.FOLDER);
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

    public List<DMFolder> getChildren() {
        return children;
    }

    public void setChildren(List<DMFolder> children) {
        this.children = children;
    }
}