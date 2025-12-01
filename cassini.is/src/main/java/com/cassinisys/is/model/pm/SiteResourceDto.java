package com.cassinisys.is.model.pm;

import com.cassinisys.is.model.tm.ISProjectTask;

/**
 * Created by subramanyam on 05-03-2020.
 */
public class SiteResourceDto {

    private ISProjectTask task;

    private ISProjectResource projectResource;

    private String resource;

    public ISProjectTask getTask() {
        return task;
    }

    public void setTask(ISProjectTask task) {
        this.task = task;
    }

    public ISProjectResource getProjectResource() {
        return projectResource;
    }

    public void setProjectResource(ISProjectResource projectResource) {
        this.projectResource = projectResource;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
