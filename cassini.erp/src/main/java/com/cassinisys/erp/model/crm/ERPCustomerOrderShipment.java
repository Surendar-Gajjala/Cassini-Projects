package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.model.common.ERPAddress;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

@Entity
@Table(name = "ERP_CUSTOMERORDERSHIPMENT")
@PrimaryKeyJoinColumn(name = "SHIPMENT_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "CRM")
public class ERPCustomerOrderShipment extends ERPObject implements Serializable {

	@ApiObjectField(required = true)
	private ERPCustomerOrder order;
    @ApiObjectField
    private ShipmentStatus status;
    @ApiObjectField
    private Double discount;
    @ApiObjectField
    private Double specialDiscount;
    @ApiObjectField
    private Double invoiceAmount;
    @ApiObjectField
    private String invoiceNumber;
    @ApiObjectField
    private ERPAddress shippingAddress;
    @ApiObjectField
    private ERPAddress billingAddress;
    @ApiObjectField
    private String notes;
    @ApiObjectField
    private SortedSet<ERPCustomerOrderShipmentDetails> details;
    @ApiObjectField
    private ERPConsignment consignment;


    public ERPCustomerOrderShipment() {
        super();
        setObjectType(ObjectType.SHIPMENT);
    }

    @JsonBackReference (value = "orderShipments")
    @ManyToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "ORDER_ID", nullable = false, updatable = true, insertable = true)
	public ERPCustomerOrder getOrder() {
		return order;
	}

    public void setOrder(ERPCustomerOrder order) {
        this.order = order;
    }

    @Column(name = "STATUS")
    @Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.crm.ShipmentStatus") })
    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    @Column(name = "DISCOUNT")
    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Column(name = "SPECIAL_DISCOUNT")
    public Double getSpecialDiscount() {
        return specialDiscount;
    }

    public void setSpecialDiscount(Double specialDiscount) {
        this.specialDiscount = specialDiscount;
    }

    @Column(name = "INVOICE_AMOUNT")
    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    @Column(name = "INVOICE_NUMBER")
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="SHIPPING_ADDRESS",nullable = false)
    public ERPAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ERPAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="BILLING_ADDRESS",nullable = false)
    public ERPAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(ERPAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    @Column(name = "NOTES")
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shipment", cascade=CascadeType.ALL )
    @OrderBy("serialNumber ASC")
    public SortedSet<ERPCustomerOrderShipmentDetails> getDetails() {
        return details;
    }

    public void setDetails(SortedSet<ERPCustomerOrderShipmentDetails> details) {
        this.details = details;
    }

    @JsonBackReference (value = "consignmentShipments")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CONSIGNMENT", nullable = false, updatable = true, insertable = true)
    public ERPConsignment getConsignment() {
        return consignment;
    }

    public void setConsignment(ERPConsignment consignment) {
        this.consignment = consignment;
    }

}
