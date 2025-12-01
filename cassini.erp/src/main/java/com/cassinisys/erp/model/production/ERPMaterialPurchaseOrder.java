package com.cassinisys.erp.model.production;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cassinisys.erp.converters.CustomDateDeserializer;
import com.cassinisys.erp.model.crm.OrderType;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_MATERIALPURCHASEORDER")
@PrimaryKeyJoinColumn(name = "ORDER_ID")
@ApiObject(group = "PRODUCTION")
public class ERPMaterialPurchaseOrder extends ERPObject {

    @ApiObjectField(required = true)
    private ERPSupplier supplier;
    @ApiObjectField(required = true)
    private MaterialPurchaseOrderStatus status;
    private OrderType orderType = OrderType.MPO;
    @ApiObjectField(required = true)
    private Date orderedDate;
    @ApiObjectField(required = true)
    private String orderNumber;
    @ApiObjectField(required = true)
    private Integer orderedBy;
    @ApiObjectField(required = true)
    private Date approvedDate;
    @ApiObjectField(required = true)
    private Integer approvedBy;
    @ApiObjectField(required = true)
    private Date recievedDate;
    @ApiObjectField(required = true)
    private Integer recievedBy;
    @ApiObjectField(required = true)
    private Double orderTotal;

    @Column(name = "NOTES")
    private String notes;

    private Set<ERPMaterialPurchaseOrderDetails> details;

    public ERPMaterialPurchaseOrder() {
        super.setObjectType(ObjectType.MATERIALPURCHASEORDER);
    }

    //@Column(name = "SUPPLIER_ID")
    @ManyToOne
    @JoinColumn(name = "SUPPLIER_ID", nullable = false)
    public ERPSupplier getSupplier() {
        return supplier;
    }

    public void setSupplier(ERPSupplier supplier) {
        this.supplier = supplier;
    }

    @Column(name = "STATUS")
    @Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.production.MaterialPurchaseOrderStatus")})
    public MaterialPurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(MaterialPurchaseOrderStatus status) {
        this.status = status;
    }

    @Column(name = "ORDER_TYPE")
    @Type(type = "com.cassinisys.erp.converters.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.erp.model.crm.OrderType")})
    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }


    @Column(name = "ORDERED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    @Column(name = "ORDERED_BY")
    public Integer getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(Integer orderedBy) {
        this.orderedBy = orderedBy;
    }

    @Column(name = "ORDER_NUMBER")
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }


    @Column(name = "APPROVED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    @Column(name = "APPROVED_BY")
    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    @Column(name = "RECIEVED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    public Date getRecievedDate() {
        return recievedDate;
    }

    public void setRecievedDate(Date recievedDate) {
        this.recievedDate = recievedDate;
    }

    @Column(name = "RECIEVED_BY")
    public Integer getRecievedBy() {
        return recievedBy;
    }

    public void setRecievedBy(Integer recievedBy) {
        this.recievedBy = recievedBy;
    }

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "materialPurchaseOrder", cascade = CascadeType.ALL)
    public Set<ERPMaterialPurchaseOrderDetails> getDetails() {
        return details;
    }

    public void setDetails(
            Set<ERPMaterialPurchaseOrderDetails> listOfPurchaseOrderDetails) {
        this.details = listOfPurchaseOrderDetails;
    }

    @Column(name = "ORDER_TOTAL")
    public Double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
