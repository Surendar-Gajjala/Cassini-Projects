package com.cassinisys.erp.model.security;

import com.cassinisys.erp.converters.CustomDateSerializer;
import com.cassinisys.erp.model.common.ERPMobileDevice;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ERP_SESSION")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "COMMON")
public class ERPSession implements Serializable {

	@ApiObjectField(required = true)
	private Integer id;
	@ApiObjectField(required = true)
	private ERPLogin login;
	@ApiObjectField(required = true)
	private String ipAddress;
	@ApiObjectField(required = true)
	private Date loginTime;
	@ApiObjectField
	private Date logoutTime;

	@ApiObjectField
	private ERPMobileDevice mobileDevice;

	private String apiKey;


	@Id
	@SequenceGenerator(name = "SESSION_ID_GEN", sequenceName = "SESSION_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SESSION_ID_GEN")
	@Column(name = "SESSION_ID", nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "LOGIN_ID", nullable = false)
	public ERPLogin getLogin() {
		return login;
	}

	public void setLogin(ERPLogin login) {
		this.login = login;
	}

	@Column(name = "IP_ADDRESS", nullable = false)
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Column(name = "LOGIN_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	@Column(name = "LOGOUT_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}


	@Transient
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@OneToOne
	@JoinColumn(name = "MOBILE_DEVICE", nullable = false)
	public ERPMobileDevice getMobileDevice() {
		return mobileDevice;
	}

	public void setMobileDevice(ERPMobileDevice mobileDevice) {
		this.mobileDevice = mobileDevice;
	}
}
