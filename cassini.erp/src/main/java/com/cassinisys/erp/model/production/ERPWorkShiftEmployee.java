package com.cassinisys.erp.model.production;

import javax.persistence.*;

import com.cassinisys.erp.model.hrm.ERPEmployee;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;


@Entity
@Table(name = "ERP_WORKSHIFTEMPLOYEE")
@ApiObject(group = "PRODUCTION")
public class ERPWorkShiftEmployee {	
	
	@ApiObjectField(required = true)
	private ERPEmployee employee;
	@ApiObjectField(required = true)
	private Integer shiftId;
	@ApiObjectField(required = true)
	private ERPWorkCenter workcenter;
	@ApiObjectField(required = true)
	private Integer rowId;

	@ManyToOne
	@JoinColumn(name = "EMPLOYEE", nullable = false)
	public ERPEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(ERPEmployee employee) {
		this.employee = employee;
	}

	@Column(name = "SHIFT", nullable = false)
	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}

	@ManyToOne
	@JoinColumn(name = "WORKCENTER", nullable = false)
	public ERPWorkCenter getWorkcenter() {
		return workcenter;
	}

	public void setWorkcenter(ERPWorkCenter workcenter) {
		this.workcenter = workcenter;
	}


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
	
	
}
