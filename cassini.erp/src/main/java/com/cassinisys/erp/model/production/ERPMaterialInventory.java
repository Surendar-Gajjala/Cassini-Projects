package com.cassinisys.erp.model.production;

import javax.persistence.*;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_MATERIALINVENTORY")
@ApiObject(group = "PRODUCTION")
public class ERPMaterialInventory {

	@ApiObjectField(required = true)
	private ERPMaterial material;
	@ApiObjectField(required = true)
	private Integer inventory;
	@ApiObjectField
	private Integer threshold;
	@ApiObjectField(required = true)
	private Integer rowId;

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


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MATERIAL_ID", nullable = false)
	public ERPMaterial getMaterial() {
		return material;
	}

	public void setMaterial(ERPMaterial material) {
		this.material = material;
	}

	@Column(name = "INVENTORY")
	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	@Column(name = "THRESHOLD")
	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}
}
