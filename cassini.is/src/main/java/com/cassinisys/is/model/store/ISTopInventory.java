package com.cassinisys.is.model.store;

import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.is.service.store.StockMovementDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Entity
@Table(name = "IS_INVENTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class ISTopInventory implements Serializable {

    @ApiObjectField(required = true)
    @Id
    @SequenceGenerator(name = "TOPINVENTORY_ID_GEN", sequenceName = "TOPINVENTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOPINVENTORY_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ITEM")
    @ApiObjectField(required = true)
    private Integer item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STORE")
    @ApiObjectField(required = true)
    private ISTopStore store;

    @Column(name = " STOCK_ON_HAND", nullable = false)
    @ApiObjectField(required = true)
    private Double storeOnHand = 0.0;

    @Column(name = "STOCK_ON_ORDER", nullable = false)
    @ApiObjectField(required = true)
    private Double storeOnOrder = 0.0;

    @Column
    private Integer project;

    @Transient
    private StockMovementDTO stockMovementDTO;

    @Transient
    private ItemDTO itemDTO;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public ISTopStore getStore() {
        return store;
    }

    public void setStore(ISTopStore store) {
        this.store = store;
    }

    public Double getStoreOnHand() {
        return storeOnHand;
    }

    public void setStoreOnHand(Double storeOnHand) {
        this.storeOnHand = storeOnHand;
    }

    public Double getStoreOnOrder() {
        return storeOnOrder;
    }

    public void setStoreOnOrder(Double storeOnOrder) {
        this.storeOnOrder = storeOnOrder;
    }

    public StockMovementDTO getStockMovementDTO() {
        return stockMovementDTO;
    }

    public void setStockMovementDTO(StockMovementDTO stockMovementDTO) {
        this.stockMovementDTO = stockMovementDTO;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public ItemDTO getItemDTO() {
        return itemDTO;
    }

    public void setItemDTO(ItemDTO itemDTO) {
        this.itemDTO = itemDTO;
    }
}
