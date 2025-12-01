package com.cassinisys.is.model.procm;
/* Model for ISProjectRfq */

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "IS_PROJECTRFQ")
@PrimaryKeyJoinColumn(name = "RFQ_ID")
@ApiObject(name = "PROCM")
public class ISProjectRfq extends ISRfq {

    private static final long serialVersionUID = 1L;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    public ISProjectRfq() {
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

}
