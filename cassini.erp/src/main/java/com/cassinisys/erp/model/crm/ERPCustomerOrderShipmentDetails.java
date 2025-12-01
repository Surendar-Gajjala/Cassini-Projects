package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.model.production.ERPProduct;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by reddy on 10/2/15.
 */

@Entity
@Table(name = "ERP_CUSTOMERORDERSHIPMENTDETAILS")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "CRM")
public class ERPCustomerOrderShipmentDetails implements Serializable, Comparable<ERPCustomerOrderShipmentDetails> {
    @ApiObjectField(required = true)
    private Integer id;
    @ApiObjectField(required = true)
    private Integer serialNumber;
    @ApiObjectField(required = true)
    private ERPCustomerOrderShipment shipment;
    @ApiObjectField(required = true)
    private Integer quantityShipped = 0;
    @ApiObjectField(required = true)
    private ERPProduct product;
    @ApiObjectField(required = true)
    private Double unitPrice;
    @ApiObjectField
    private Double discount;
    @ApiObjectField
    private Double itemTotal;
    private Integer index;
    @ApiObjectField
    private Integer boxes;
    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ROWID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer rowId) {
        this.id = rowId;
    }

    @Column(name = "SERIAL_NUMBER")
    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SHIPMENT", nullable = false, updatable = true, insertable = true)
    public ERPCustomerOrderShipment getShipment() {
        return shipment;
    }

    public void setShipment(ERPCustomerOrderShipment shipment) {
        this.shipment = shipment;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    public ERPProduct getProduct() {
        return product;
    }

    public void setProduct(ERPProduct product) {
        this.product = product;
    }

    @Column(name = "QUANTITY_SHIPPED", nullable = false)
    public Integer getQuantityShipped() {
        return quantityShipped;
    }

    public void setQuantityShipped(Integer quantityShipped) {
        this.quantityShipped = quantityShipped;
    }

    @Column(name = "UNIT_PRICE", nullable = false)
    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Column(name = "DISCOUNT")
    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Column(name = "ITEM_TOTAL", nullable = false)
    public Double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(Double itemTotal) {
        this.itemTotal = itemTotal;
    }

    @Column(name = "BOXES", nullable = true)
    public Integer getBoxes() {
        return boxes;
    }

    public void setBoxes(Integer boxes) {
        this.boxes = boxes;
    }

    @Transient
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public int compareTo(ERPCustomerOrderShipmentDetails o) {
        if(this.getId() != null && o.getId() != null) {
            return Integer.compare(this.getId(), o.getId());
        }
        else if(this.getIndex() != null && o.getIndex() != null) {
            return Integer.compare(this.getIndex(), o.getIndex());
        }
        else {
            return Integer.compare(this.hashCode(), o.hashCode());
        }
    }
}
