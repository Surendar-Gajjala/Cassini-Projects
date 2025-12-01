package com.cassinisys.is.model.pm;
/* Model for ISProjectSite */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.common.LocationAwareObject;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

@Entity
@Table(name = "IS_PROJECTSITE")
@PrimaryKeyJoinColumn(name = "SITE_ID")
@ApiObject(name = "PM")
public class ISProjectSite extends LocationAwareObject {

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    @ApiObjectField(required = true)
    private String description;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    @Transient
    private Integer totalTasks = 0;

    @Transient
    private Integer finishedTasks = 0;

    @Transient
    private Integer resources = 0;

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public ISProjectSite() {
        super(ISObjectType.SITE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Integer totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Integer getFinishedTasks() {
        return finishedTasks;
    }

    public void setFinishedTasks(Integer finishedTasks) {
        this.finishedTasks = finishedTasks;
    }

    public Integer getResources() {
        return resources;
    }

    public void setResources(Integer resources) {
        this.resources = resources;
    }
}
