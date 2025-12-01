package com.cassinisys.erp.model.crm;

import com.cassinisys.erp.converters.CustomDateDeserializer;
import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.converters.CustomShortDateDeserializer;
import com.cassinisys.erp.converters.CustomShortDateSerializer;
import com.cassinisys.erp.model.common.ERPAddress;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mysema.query.annotations.QueryInit;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ERP_CUSTOMERORDER")
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "ORDER_ID")
@ApiObject(group = "CRM")
public class ERPCustomerOrder extends ERPObject {
    @ApiObjectField(required = true)
    private OrderType orderType = OrderType.PRODUCT;
	@ApiObjectField(required = true)
	private String orderNumber;
	@ApiObjectField(required = true)
	private ERPCustomer customer;
	@ApiObjectField(required = true)
	private Date orderedDate;
	@ApiObjectField(required = true)
	private Integer orderedBy;
	@ApiObjectField(required = false)
	private Date deliveryDate;
	@ApiObjectField(required = true)
	private Double orderTotal;
	@ApiObjectField(required = true)
	private CustomerOrderStatus status = CustomerOrderStatus.NEW;
	@ApiObjectField
	private Double appliedDiscount;
	@ApiObjectField
	private Double netTotal;
	@ApiObjectField
	private String shipTo;
	@ApiObjectField
	private ERPAddress shippingAddress;
	@ApiObjectField
	private ERPAddress billingAddress;
	@ApiObjectField
	private String contactPhone;
	@ApiObjectField
	private String poNumber;
    @ApiObjectField
    private String reference;
	@ApiObjectField
	private Boolean starred = Boolean.FALSE;
	@ApiObjectField
	private Boolean onhold = Boolean.FALSE;
	@ApiObjectField
	private String notes;
	@ApiObjectField
	private Set<ERPCustomerOrderShipment> shipments;
    @ApiObjectField
    private Set<ERPCustomerOrderStatusHistory> history;
	@ApiObjectField
	private Set<ERPOrderVerification> verifications;
	private List<ERPCustomerOrderDetails> details;


	public ERPCustomerOrder() {	
		super.setObjectType(ObjectType.CUSTOMERORDER);
	}

    @Column(name = "ORDER_TYPE")
    @Type(type = "com.cassinisys.erp.converters.EnumUserType",
            parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.erp.model.crm.OrderType") })
    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    @Column(name = "ORDER_NUMBER")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "CUSTOMER_ID", nullable = false)
	@QueryInit("*.*")
	public ERPCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(ERPCustomer customer) {
		this.customer = customer;
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

	@Column(name = "ORDERED_BY", unique = true)
	public Integer getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(Integer orderedBy) {
		this.orderedBy = orderedBy;
	}

	@Column(name = "DELIVERY_DATE")
	@Temporal(TemporalType.DATE)
	@JsonSerialize(using = CustomShortDateSerializer.class)
	@JsonDeserialize(using = CustomShortDateDeserializer.class)
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@Column(name = "ORDER_TOTAL")
	public Double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Double orderTotal) {
		this.orderTotal = orderTotal;
	}

	@Column(name = "STATUS")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.crm.CustomerOrderStatus") })
	public CustomerOrderStatus getStatus() {
		return status;
	}

	public void setStatus(CustomerOrderStatus status) {
		this.status = status;
	}

	@Column(name = "APPLIED_DISCOUNT")
	public Double getAppliedDiscount() {
		return appliedDiscount;
	}

	public void setAppliedDiscount(Double appliedDiscount) {
		this.appliedDiscount = appliedDiscount;
	}

	@Column(name = "NET_TOTAL")
	public Double getNetTotal() {
		return netTotal;
	}

	public void setNetTotal(Double netTotal) {
		this.netTotal = netTotal;
	}

	@Column(name = "CONTACT_PHONE")
	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	@Column (name = "PO_NUMBER")
	public String getPoNumber() {
		return poNumber;
	}

    @Column (name = "REFERENCE")
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	@Column (name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Column (name = "STARRED")
	public Boolean getStarred() {
		return starred;
	}

	public void setStarred(Boolean printed) {
		this.starred = printed;
	}

	@Column (name = "ONHOLD")
	public Boolean getOnhold() {
		return onhold;
	}

	public void setOnhold(Boolean onhold) {
		this.onhold = onhold;
	}

	@Column (name = "SHIP_TO")
	public String getShipTo() {
		return shipTo;
	}

	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

	@OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
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

    @JsonManagedReference (value = "orderShipments")
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "order",cascade=CascadeType.ALL )
	public Set<ERPCustomerOrderShipment> getShipments() {
		return shipments;
	}

	public void setShipments(Set<ERPCustomerOrderShipment> shipments) {
		this.shipments = shipments;
	}

    @JsonManagedReference (value = "orderHistory")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order",cascade=CascadeType.ALL )
    public Set<ERPCustomerOrderStatusHistory> getHistory() {
        return history;
    }

    public void setHistory(Set<ERPCustomerOrderStatusHistory> history) {
        this.history = history;
    }

    @Transient
	public Set<ERPOrderVerification> getVerifications() {
		return verifications;
	}

	public void setVerifications(Set<ERPOrderVerification> verifications) {
		this.verifications = verifications;
	}

	@Transient
	public List<ERPCustomerOrderDetails> getDetails() {
		return details;
	}

	public void setDetails(List<ERPCustomerOrderDetails> details) {
		this.details = details;
	}
}
