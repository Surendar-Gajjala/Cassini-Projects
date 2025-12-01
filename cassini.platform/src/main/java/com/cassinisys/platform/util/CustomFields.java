package com.cassinisys.platform.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomFields {

		@JsonProperty("Ticket Type")
		String ticketType;
		@JsonProperty("Customer ID")
		String customerId;
		@JsonProperty("Customer Name")
		String customerName;
		@JsonProperty("Product ID")
		String productId;
		@JsonProperty("Product Name")
		String productName;
		@JsonProperty("Application Area")
		String applicationArea="Other";
		@JsonProperty("Reported By Customer")
		String reportedBy;
		public String getTicketType() {
			return ticketType;
		}
		public void setTicketType(String ticketType) {
			this.ticketType = ticketType;
		}
		public String getCustomerId() {
			return customerId;
		}
		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}
		public String getCustomerName() {
			return customerName;
		}
		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}
		public String getProductId() {
			return productId;
		}
		public void setProductId(String productId) {
			this.productId = productId;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public String getReportedBy() {
			return reportedBy;
		}
		public void setReportedBy(String reportedBy) {
			this.reportedBy = reportedBy;
		}

		public String getApplicationArea() {
			return applicationArea;
		}

		public void setApplicationArea(String applicationArea) {
			this.applicationArea = applicationArea;
		}
}
