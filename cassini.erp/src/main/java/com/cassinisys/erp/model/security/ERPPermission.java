package com.cassinisys.erp.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by reddy on 9/8/15.
 */
@Entity
@Table(name = "ERP_PERMISSION")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "SECURITY")
public class ERPPermission implements Serializable {
    @ApiObjectField(required = true)
    private String id;

    @ApiObjectField(required = true)
    private String name;

    @ApiObjectField(required = true)
    private String description;

    @Id
    @Column(name = "PERMISSION_ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column( name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
