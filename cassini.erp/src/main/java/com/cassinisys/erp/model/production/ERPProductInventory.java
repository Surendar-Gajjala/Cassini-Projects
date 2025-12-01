package com.cassinisys.erp.model.production;

import com.cassinisys.erp.model.core.ERPObject;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_PRODUCTINVENTORY")
@ApiObject(group = "PRODUCTION")
public class ERPProductInventory implements Serializable {

	@ApiObjectField(required = true)
	private Integer rowId;
	@ApiObjectField(required = true)
	private ERPProduct product;
	@ApiObjectField(required = true)
	private Integer inventory = 0;
	@ApiObjectField
	private Integer threshold = 0;

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
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	public ERPProduct getProduct() {
		return product;
	}

	public void setProduct(ERPProduct product) {
		this.product = product;
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
