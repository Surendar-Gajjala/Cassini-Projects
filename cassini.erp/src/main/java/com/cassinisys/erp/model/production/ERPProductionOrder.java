package com.cassinisys.erp.model.production;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import com.cassinisys.erp.model.crm.OrderType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_PRODUCTIONORDER")
@PrimaryKeyJoinColumn(name = "ORDER_ID")
@ApiObject(group = "PRODUCTION")
public class ERPProductionOrder extends ERPObject {

	@ApiObjectField(required = true)
	private ProductionOrderStatus status;
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
	@ApiObjectField
	private Date startDate;
	@ApiObjectField(required = true)
	private Integer startedBy;
	@ApiObjectField
	private Date completedDate;
	@ApiObjectField(required = true)
	private Integer completedBy;

	private Set<ERPProductionOrderItem> details;

	public ERPProductionOrder() {
		super.setObjectType(ObjectType.PRODUCTIONORDER);
	}

	@Column(name = "STATUS")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.production.ProductionOrderStatus") })
	public ProductionOrderStatus getStatus() {
		return status;
	}

	public void setStatus(ProductionOrderStatus status) {
		this.status = status;
	}

	@Column(name = "ORDERED_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getOrderedDate() {
		return orderedDate;
	}

	public void setOrderedDate(Date orderedDate) {
		this.orderedDate = orderedDate;
	}

	@Column(name = "ORDER_NUMBER")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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


	@Column(name = "ORDERED_BY", nullable = false)
	public Integer getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(Integer orderedBy) {
		this.orderedBy = orderedBy;
	}

	@Column(name = "APPROVED_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	@Column(name = "APPROVED_BY", nullable = false)
	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Column(name = "COMPLETED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	@Column(name = "COMPLETED_BY", nullable = false)
	public Integer getCompletedBy() {
		return completedBy;
	}

	public void setCompletedBy(Integer completedBy) {
		this.completedBy = completedBy;
	}

	@Column(name = "START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "STARTED_BY", nullable = false)
	public Integer getStartedBy() {
		return startedBy;
	}

	public void setStartedBy(Integer startedBy) {
		this.startedBy = startedBy;
	}


	@JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "productionOrder", cascade = CascadeType.ALL)
	public Set<ERPProductionOrderItem> getDetails() {
		return details;
	}

	public void setDetails(Set<ERPProductionOrderItem> details) {
		this.details = details;
	}
}
