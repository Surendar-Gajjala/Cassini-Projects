package com.cassinisys.is.model.pm;
/* Model for ISProject */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_PROJECTBOQATTACHMENT")
@PrimaryKeyJoinColumn(name = "ID")
@ApiObject(name = "PM")
public class ProjectBoqAttachment extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    @Column(name = "NAME")
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "SIZE")
    @ApiObjectField(required = true)
    private Long size;

    @Column(name = "DESCRIPTION")
    @ApiObjectField(required = true)
    private String description;

    public ProjectBoqAttachment() {
        super(ISObjectType.FILE);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
