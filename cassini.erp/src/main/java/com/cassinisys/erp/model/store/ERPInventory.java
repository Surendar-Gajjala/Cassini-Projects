package com.cassinisys.erp.model.store;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ERP_INVENTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class ERPInventory implements Serializable {

    @ApiObjectField(required = true)
    @Id
    @SequenceGenerator(name = "INVENTORY_ID_GEN", sequenceName = "INVENTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVENTORY_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ITEM", nullable = false)
    @ApiObjectField(required = true)
    private Integer item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STORE")
    @ApiObjectField(required = true)
    private ERPStore store;

    @Column(name = " STOCK_ON_HAND", nullable = false)
    @ApiObjectField(required = true)
    private Integer storeOnHand = 0;

    @Column(name = "STOCK_ON_ORDER", nullable = false)
    @ApiObjectField(required = true)
    private Integer storeOnOrder = 0;

    @Transient
    private StockMovementDTO stockMovementDTO;

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

    public ERPStore getStore() {
        return store;
    }

    public void setStore(ERPStore store) {
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

    public StockMovementDTO getStockMovementDTO() {
        return stockMovementDTO;
    }

    public void setStockMovementDTO(StockMovementDTO stockMovementDTO) {
        this.stockMovementDTO = stockMovementDTO;
    }

}
