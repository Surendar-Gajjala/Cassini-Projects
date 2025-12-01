package com.cassinisys.is.model.pm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 11-03-2020.
 */
public class ProjectFolderDto {

    private Integer id;

    private String name;

    private List<ProjectFolderDto> children = new ArrayList<>();

    private List<ProjectFileDto> files = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProjectFolderDto> getChildren() {
        return children;
    }

    public void setChildren(List<ProjectFolderDto> children) {
        this.children = children;
    }

    public List<ProjectFileDto> getFiles() {
        return files;
    }

    public void setFiles(List<ProjectFileDto> files) {
        this.files = files;
    }
}

