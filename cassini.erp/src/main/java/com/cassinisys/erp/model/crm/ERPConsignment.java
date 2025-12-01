package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.converters.CustomDateDeserializer;
import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.common.ERPAddress;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by reddy on 10/14/15.
 */
@Entity
@Table(name = "ERP_CONSIGNMENT")
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "CONSIGNMENT_ID")
@ApiObject(group = "CRM")
public class ERPConsignment extends ERPObject implements Serializable {
    @ApiObjectField (required = true)
    private String number;
    @ApiObjectField (required = true)
    private ConsignmentStatus status;
    @ApiObjectField (required = true)
    private String consignee;
    @ApiObjectField
    private PaidBy paidBy = PaidBy.CUSTOMER;
    @ApiObjectField
    private DeliveryMode deliveryMode = DeliveryMode.WITH_PASS;
    @ApiObjectField
    private Double shippingCost;
    @ApiObjectField (required = true)
    private ERPAddress shippingAddress;
    @ApiObjectField (required = true)
    private ERPShipper shipper;
    @ApiObjectField (required = true)
    private double value;
    @ApiObjectField (required = true)
    private String contents;
    @ApiObjectField (required = true)
    private String description;
    @ApiObjectField (required = true)
    private String mobilePhone;
    @ApiObjectField (required = true)
    private String officePhone;
    @ApiObjectField (required = true)
    private String through;
    @ApiObjectField(required = true)
    private Date shippedDate;
    @ApiObjectField(required = true)
    private ERPEmployee shippedBy;
    @ApiObjectField
    private Set<ERPCustomerOrderShipment> shipments;
    @ApiObjectField
    private String confirmationNumber;
    @ApiObjectField
    private ERPVehicle vehicle;
    @ApiObjectField
    private ERPEmployee driver;

    public ERPConsignment() {
        super();
        setObjectType(ObjectType.CONSIGNMENT);
    }

    @Column (name = "NUMBER")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SHIPPER", nullable = false)
    public ERPShipper getShipper() {
        return shipper;
    }

    public void setShipper(ERPShipper shipper) {
        this.shipper = shipper;
    }

    @Column(name = "STATUS")
    @Type(type = "com.cassinisys.erp.converters.EnumUserType",
            parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.erp.model.crm.ConsignmentStatus") })
    public ConsignmentStatus getStatus() {
        return status;
    }

    public void setStatus(ConsignmentStatus status) {
        this.status = status;
    }

    @Column(name = "CONSIGNEE")
    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    @Column(name = "PAID_BY")
    @Type(type = "com.cassinisys.erp.converters.EnumUserType",
            parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.erp.model.crm.PaidBy") })
    public PaidBy getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(PaidBy paidBy) {
        this.paidBy = paidBy;
    }

    @Column(name = "DELIVERY_MODE")
    @Type(type = "com.cassinisys.erp.converters.EnumUserType",
            parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.erp.model.crm.DeliveryMode") })
    public DeliveryMode getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(DeliveryMode deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    @Column(name = "SHIPPING_COST")
    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="SHIPPING_ADDRESS",nullable = false)
    public ERPAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ERPAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Column(name = "VALUE")
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Column(name = "CONTENTS")
    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "MOBILE_PHONE")
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Column(name = "OFFICE_PHONE")
    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    @Column(name = "THROUGH")
    public String getThrough() {
        return through;
    }

    public void setThrough(String through) {
        this.through = through;
    }

    @Column(name = "SHIPPED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SHIPPED_BY")
    public ERPEmployee getShippedBy() {
        return shippedBy;
    }

    public void setShippedBy(ERPEmployee shippedBy) {
        this.shippedBy = shippedBy;
    }

    @JsonManagedReference (value = "consignmentShipments")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "consignment",cascade=CascadeType.ALL )
    public Set<ERPCustomerOrderShipment> getShipments() {
        return shipments;
    }

    public void setShipments(Set<ERPCustomerOrderShipment> shipments) {
        this.shipments = shipments;
    }

    @Column (name = "CONFIRMATION_NUMBER")
    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "VEHICLE")
    public ERPVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(ERPVehicle vehicle) {
        this.vehicle = vehicle;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DRIVER")
    public ERPEmployee getDriver() {
        return driver;
    }

    public void setDriver(ERPEmployee driver) {
        this.driver = driver;
    }
}
