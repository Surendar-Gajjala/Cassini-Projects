package com.cassinisys.erp.model.production;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@Entity
@Table(name = "ERP_SPAREPARTSINVENTORY")
@ApiObject(group = "PRODUCTION")
public class ERPSparePartsInventory {
		
    @ApiObjectField(required = true)
	private Integer partId;
	@ApiObjectField(required = true)
	private Integer inventory;
    
    @Id
	@SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
	@Column(name = "PART_ID")
	public Integer getPartId() {
		return partId;
	}

    public void setPartId(Integer partId) {
		this.partId = partId;
	}
    
    @Column(name = "INVENTORY")
	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	

}
