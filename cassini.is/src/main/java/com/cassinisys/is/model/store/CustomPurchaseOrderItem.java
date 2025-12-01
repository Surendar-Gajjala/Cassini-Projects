package com.cassinisys.is.model.store;

import com.cassinisys.is.model.procm.ISMaterialItem;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */

@Entity
@Table(name = "CUSTOM_PURCHASEORDERITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(name = "STORE")
public class CustomPurchaseOrderItem implements Serializable {

    @ApiObjectField(required = true)
    @Id
    @SequenceGenerator(name = "CUSTOMPURCHASEORDERITEM_ID_GEN", sequenceName = "CUSTOMPURCHASEORDERITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMPURCHASEORDERITEM_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PURCHASE_ORDER")
    @ApiObjectField(required = true)
    @JsonBackReference
    private CustomPurchaseOrder customPurchaseOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MATERIAL")
    @ApiObjectField(required = true)
    private ISMaterialItem materialItem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REQUISITION", nullable = false, updatable = false)
    @ApiObjectField(required = true)
    private CustomRequisition requisition;
    @Transient
    private String receiveDate;
    @Transient
    private List<CustomRequisition> requisitionNumbers;
    @ApiObjectField(required = true)
    @Column(name = "QUANTITY")
    private Double quantity = 1.0;
    @ApiObjectField(required = true)
    @Column(name = "NOTES")
    private String notes;

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public List<CustomRequisition> getRequisitionNumbers() {
        return requisitionNumbers;
    }

    public void setRequisitionNumbers(List<CustomRequisition> requisitionNumbers) {
        this.requisitionNumbers = requisitionNumbers;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomPurchaseOrder getCustomPurchaseOrder() {
        return customPurchaseOrder;
    }

    public void setCustomPurchaseOrder(CustomPurchaseOrder customPurchaseOrder) {
        this.customPurchaseOrder = customPurchaseOrder;
    }

    public CustomRequisition getRequisition() {
        return requisition;
    }

    public void setRequisition(CustomRequisition requisition) {
        this.requisition = requisition;
    }

    public ISMaterialItem getMaterialItem() {
        return materialItem;
    }

    public void setMaterialItem(ISMaterialItem materialItem) {
        this.materialItem = materialItem;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
