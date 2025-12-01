package com.cassinisys.is.model.procm;
/* Model for ISProjectBoq */

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_PROJECTBOQ")
@PrimaryKeyJoinColumn(name = "BOQ_ID")
@ApiObject(name = "PROCM")
public class ISProjectBoq extends ISBoq {

    private static final long serialVersionUID = 1L;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    public ISProjectBoq() {
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

}
