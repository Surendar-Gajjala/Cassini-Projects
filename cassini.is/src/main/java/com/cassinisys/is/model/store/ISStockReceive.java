package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.procm.ISMaterialReceiveType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.List;

/**
 * Created by swapna on 31/07/18.
 */
@Entity
@Table(name = "IS_STOCKRECEIVE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISStockReceive extends CassiniObject {

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

    @Column
    private Integer supplier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RECEIVE_TYPE")
    @ApiObjectField(required = true)
    private ISMaterialReceiveType materialReceiveType;

    @Transient
    private List<ISStockReceiveItem> stockReceiveItems;

    public ISStockReceive() {
        super(ISObjectType.RECEIVE);
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

    public Integer getSupplier() {
        return supplier;
    }

    public void setSupplier(Integer supplier) {
        this.supplier = supplier;
    }

    public List<ISStockReceiveItem> getStockReceiveItems() {
        return stockReceiveItems;
    }

    public void setStockReceiveItems(List<ISStockReceiveItem> stockReceiveItems) {
        this.stockReceiveItems = stockReceiveItems;
    }

    public ISMaterialReceiveType getMaterialReceiveType() {
        return materialReceiveType;
    }

    public void setMaterialReceiveType(ISMaterialReceiveType materialReceiveType) {
        this.materialReceiveType = materialReceiveType;
    }
}
