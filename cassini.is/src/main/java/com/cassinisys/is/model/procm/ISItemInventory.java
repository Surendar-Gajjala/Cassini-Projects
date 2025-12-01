package com.cassinisys.is.model.procm;
/**
 * Model for ISItemInventory
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "IS_ITEMINVENTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "PROCM")
public class ISItemInventory implements Serializable {

    @ApiObjectField(required = true)
    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ROWID")
    private Integer rowId;

    @Column(name = "BOQITEM", nullable = false)
    @ApiObjectField(required = true)
    private Integer boqItem;

    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    @Column(name = "STORE", nullable = false)
    @ApiObjectField(required = true)
    private Integer store;

    @Column(name = " STOCK_ON_HAND", nullable = false)
    @ApiObjectField(required = true)
    private Integer storeOnHand = 0;

    @Column(name = "STOCK_ON_ORDER", nullable = false)
    @ApiObjectField(required = true)
    private Integer storeOnOrder = 0;

    /**
     * The methods getters and setters are used to get and set values of different classes and datatypes
     */
    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getBoqItem() {
        return boqItem;
    }

    public void setBoqItem(Integer boqItem) {
        this.boqItem = boqItem;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public Integer getStoreOnHand() {
        return storeOnHand;
    }

    public void setStoreOnHand(Integer storeOnHand) {
        this.storeOnHand = storeOnHand;
    }

    public Integer getStoreOnOrder() {
        return storeOnOrder;
    }

    public void setStoreOnOrder(Integer storeOnOrder) {
        this.storeOnOrder = storeOnOrder;
    }

}
