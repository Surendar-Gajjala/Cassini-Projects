package com.cassinisys.pdm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Entity
@Table(name = "PDM_FOLDER")
@PrimaryKeyJoinColumn(name = "FOLDER_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PDM")
public class PDMFolder extends PDMVersionedObject {

    @Column(name = "PARENT")
    private Integer parent;

    @Column(name = "VAULT")
    private Integer vault;

    @Transient
    private List<PDMFolder> children = new ArrayList<>();

    public PDMFolder() {
        super(PDMObjectType.FOLDER);
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getVault() {
        return vault;
    }

    public void setVault(Integer vault) {
        this.vault = vault;
    }

    public List<PDMFolder> getChildren() {
        return children;
    }

    public void setChildren(List<PDMFolder> children) {
        this.children = children;
    }
}
