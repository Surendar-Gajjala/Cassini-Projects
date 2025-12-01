package com.cassinisys.tm.model;

import com.cassinisys.platform.model.core.CassiniObject;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 01-08-2016.
 */
@Entity
@Table(name = "DEPARTMENT")
@PrimaryKeyJoinColumn(name = "DEPT_ID")
@ApiObject(name = "TM")
public class TMDepartment extends CassiniObject {

    @Column(name = "NAME", nullable = false)
    @ApiObjectField(required = true)
    private  String name;

    @Column(name = "DESCRIPTION", nullable = false)
    @ApiObjectField(required = true)
    private  String description;

    public TMDepartment() {
        super(TMObjectType.DEPARTMENT);
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
}
