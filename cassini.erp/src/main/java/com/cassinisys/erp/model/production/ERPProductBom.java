package com.cassinisys.erp.model.production;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_PRODUCTBOM")
@ApiObject(group = "PRODUCTION")
public class ERPProductBom {
	@ApiObjectField(required = true)
	private Integer rowId;
	@ApiObjectField(required = true)
	private Integer product;
	@ApiObjectField(required = true)
	private Integer material;
	@ApiObjectField(required = true)
	private Integer quantity;


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

	@Column(name = "PRODUCT_ID")
	public Integer getProduct() {
		return product;
	}

	public void setProduct(Integer product) {
		this.product = product;
	}

	@Column(name = "MATERIAL_ID")
	public Integer getMaterial() {
		return material;
	}

	public void setMaterial(Integer material) {
		this.material = material;
	}

	@Column(name = "QUANTITY")
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
