package com.cassinisys.is.model.dm;
/**
 * Model for ISProjectDocument
 */

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_PROJECTDOCUMENT")
@PrimaryKeyJoinColumn(name = "DOCUMENT_ID")
@ApiObject(name = "DM")
public class ISProjectDocument extends ISDocument {

    private static final long serialVersionUID = 1L;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    public ISProjectDocument() {
    }

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

}
