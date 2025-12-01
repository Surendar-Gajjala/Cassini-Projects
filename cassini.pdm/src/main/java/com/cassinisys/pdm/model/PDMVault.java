package com.cassinisys.pdm.model;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Entity
@Table(name = "PDM_VAULT")
@PrimaryKeyJoinColumn(name = "VAULT_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PDM")
public class PDMVault extends CassiniObject{

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    public PDMVault(){super(PDMObjectType.VAULT);}

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
