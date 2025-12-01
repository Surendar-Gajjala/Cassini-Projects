package com.cassinisys.erp.model.production;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_MATERIALPURCHASEORDERDETAILS")
@ApiObject(group = "PRODUCTION")
public class ERPMaterialPurchaseOrderDetails {

    @ApiObjectField(required = true)
    private ERPMaterialPurchaseOrder materialPurchaseOrder;
    @ApiObjectField(required = true)
    private ERPMaterial material;
    @ApiObjectField(required = true)
    private Integer quantity;
    @ApiObjectField(required = true)
    private Integer rowId;
    @ApiObjectField(required = true)
    private Double unitPrice;
    private Double itemTotal;
    private Boolean issued = Boolean.FALSE;
    private Integer issQuantity = 0;

    @Column(name = "NOTES")
    private String notes;

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ROWID")
    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    public ERPMaterialPurchaseOrder getMaterialPurchaseOrder() {
        return materialPurchaseOrder;
    }

    public void setMaterialPurchaseOrder(
            ERPMaterialPurchaseOrder materialPurchaseOrder) {
        this.materialPurchaseOrder = materialPurchaseOrder;
    }

    //@Column(name = "MATERIAL_ID")
    @ManyToOne
    @JoinColumn(name = "MATERIAL_ID", nullable = false)
    public ERPMaterial getMaterial() {
        return material;
    }

    public void setMaterial(ERPMaterial material) {
        this.material = material;
    }

    @Column(name = "QUANTITY")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(name = "UNIT_PRICE")
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

    @Column(name = "ISSUED")
    public Boolean getIssued() {
        return issued;
    }

    public void setIssued(Boolean issued) {
        this.issued = issued;
    }

    @Column(name = "ISSQUANTITY")
    public Integer getIssQuantity() {
        return issQuantity;
    }

    public void setIssQuantity(Integer issQuantity) {
        this.issQuantity = issQuantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
