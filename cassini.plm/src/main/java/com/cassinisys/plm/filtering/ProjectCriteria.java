package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by swapna on 1/9/18.
 */
public class ProjectCriteria extends Criteria {

    String name;
    String description;
    String searchQuery;
    Integer program;
    Integer type;
    Integer programManager;
    Integer projectManager;

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

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Integer getProgram() {
        return program;
    }

    public void setProgram(Integer program) {
        this.program = program;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getProgramManager() {
        return programManager;
    }

    public void setProgramManager(Integer programManager) {
        this.programManager = programManager;
    }

    public Integer getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(Integer projectManager) {
        this.projectManager = projectManager;
    }
}
