package com.cassinisys.erp.model.production;

import java.util.Date;

import javax.persistence.*;

import com.cassinisys.erp.model.hrm.ERPEmployee;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_MATERIALINVENTORYHISTORY")
@ApiObject(group = "PRODUCTION")
public class ERPMaterialInventoryHistory {

	@ApiObjectField(required = true)
	private Integer material;
	@ApiObjectField(required = true)
	private InventoryType type = InventoryType.STOCKIN;
	@ApiObjectField(required = true)
	private Integer quantity = 0;
	@ApiObjectField(required = true)
	private Date timestamp;
	@ApiObjectField(required = true)
	private ERPEmployee employee;
	@ApiObjectField(required = true)
	private Integer rowId;
	@ApiObjectField
	private ERPMaterialPurchaseOrder materialPO;

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

	@Column(name = "IN_OR_OUT")
	@Type(type = "com.cassinisys.erp.converters.EnumUserType", parameters = { @org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.erp.model.production.InventoryType") })
	public InventoryType getType() {
		return type;
	}

	@Column(name = "MATERIAL_ID")
	public Integer getMaterial() {
		return material;
	}

	public void setMaterial(Integer material) {
		this.material = material;
	}

	public void setType(InventoryType type) {
		this.type = type;
	}

	@Column(name = "QUANTITY")
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Column(name = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMPLOYEE", nullable = false)
	public ERPEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(ERPEmployee employee) {
		this.employee = employee;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MATERIALPO", nullable = true)
	public ERPMaterialPurchaseOrder getMaterialPO() {
		return materialPO;
	}

	public void setMaterialPO(ERPMaterialPurchaseOrder materialPO) {
		this.materialPO = materialPO;
	}
}
