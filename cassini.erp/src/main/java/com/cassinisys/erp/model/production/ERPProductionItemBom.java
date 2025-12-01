package com.cassinisys.erp.model.production;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.List;

/**
 * Created by reddy on 27/02/16.
 */

@Entity
@Table(name = "ERP_PRODUCTIONITEMBOM")
@ApiObject(group = "PRODUCTION")
public class ERPProductionItemBom {
    @ApiObjectField(required = true)
    private Integer id;

    @ApiObjectField(required = true)
    private ERPProductionOrderItem productionOrderDetail;

    @ApiObjectField(required = true)
    private ERPMaterial material;

    @ApiObjectField(required = true)
    private Integer quantity;

    @ApiObjectField(required = true)
    private Double unitPrice;

    @ApiObjectField(required = true)
    private Double itemTotal;

    @ApiObjectField(required = true)
    private  ERPBomItem bomItem;

    private List<ERPMaterial> materialList;

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ROWID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PO_ITEM",nullable = false)
    public ERPProductionOrderItem getProductionOrderDetail() {
        return productionOrderDetail;
    }

    public void setProductionOrderDetail(ERPProductionOrderItem productionOrderDetail) {
        this.productionOrderDetail = productionOrderDetail;
    }


    @Column(name = "QUANTITY")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(name = "UNITPRICE")
    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Column(name = "ITEM_TOTAL")
    public Double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(Double itemTotal) {
        this.itemTotal = itemTotal;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOM_ITEM_ID",nullable = false)
    public ERPBomItem getBomItem() {
        return bomItem;
    }

    public void setBomItem(ERPBomItem bomItem) {
        this.bomItem = bomItem;
    }

    public void setMaterial(ERPMaterial material) {
        this.material = material;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MATERIAL" ,nullable = false)
    public ERPMaterial getMaterial() {
        return material;
    }

    @Transient
    public List<ERPMaterial> getMaterialList() {
        return materialList;
    }
    public void setMaterialList(List<ERPMaterial> materialList) {
        this.materialList = materialList;
    }
}
