package com.cassinisys.erp.model.crm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "ERP_CUSTOMERADDRESS")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "CRM")
public class ERPCustomerAddress {

	@EmbeddedId
	private ERPCustomerAddressPK customerAddressPk;

	public ERPCustomerAddressPK getCustomerAddressPk() {
		return customerAddressPk;
	}

	public void setCustomerAddressPk(ERPCustomerAddressPK customerAddressPk) {
		this.customerAddressPk = customerAddressPk;
	}

	@Embeddable
	public class ERPCustomerAddressPK implements Serializable {

		@Column(name = "CUSTOMER_ID")
		@ApiObjectField(required = true)
		private Integer customerId;

		@Column(name = "ADDRESS_ID")
		@ApiObjectField(required = true)
		private Integer addressId;

		public Integer getCustomerId() {
			return customerId;
		}

		public void setCustomerId(Integer customerId) {
			this.customerId = customerId;
		}

		public Integer getAddressId() {
			return addressId;
		}

		public void setAddressId(Integer addressId) {
			this.addressId = addressId;
		}

	}
}
