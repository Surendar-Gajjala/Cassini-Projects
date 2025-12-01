package com.cassinisys.erp.model.store;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "ERP_STOCKRECEIVE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ERPStockReceive extends ERPObject {

    @Column
    private String name;

    @Column
    private String notes;

    @Column
    private Integer store;

    @Column(name = "RECEIVENUMBER_SOURCE")
    private String receiveNumberSource;

    @Column
    private Integer project;

    public ERPStockReceive() {
        super.setObjectType(ObjectType.RECEIVE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public String getReceiveNumberSource() {
        return receiveNumberSource;
    }

    public void setReceiveNumberSource(String receiveNumberSource) {
        this.receiveNumberSource = receiveNumberSource;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }
}
