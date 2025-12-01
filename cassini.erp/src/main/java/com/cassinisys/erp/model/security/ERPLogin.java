package com.cassinisys.erp.model.security;

import com.cassinisys.erp.config.LoginEntityListener;
import com.cassinisys.erp.model.common.ERPPerson;
import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ERP_LOGIN")
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(LoginEntityListener.class)
@PrimaryKeyJoinColumn(name = "LOGIN_ID")
@ApiObject(group = "COMMON")
public class ERPLogin extends ERPObject {

	@ApiObjectField(required = true)
	private String loginName;
	@ApiObjectField(required = true)
	private ERPPerson person;
	@ApiObjectField(required = true)
	private String password;
	@ApiObjectField(required = true)
	private Boolean isActive = true;
	@ApiObjectField(required = true)
	private Boolean isSuperUser = false;
	@ApiObjectField
	private List<ERPRole> roles = new ArrayList<>();

	public ERPLogin() {
		super.setObjectType(ObjectType.LOGIN);
	}

	@Column(name = "LOGIN_NAME", nullable = false, unique = true)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@OneToOne
	@JoinColumn(name = "PERSON_ID", nullable = false)
	public ERPPerson getPerson() {
		return person;
	}

	public void setPerson(ERPPerson person) {
		this.person = person;
	}

	@Column(name = "PASSWORD", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "IS_ACTIVE", nullable = false)
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Column(name = "IS_SUPERUSER", nullable = false)
	public Boolean getIsSuperUser() {
		return isSuperUser;
	}

	public void setIsSuperUser(Boolean isSuperUser) {
		this.isSuperUser = isSuperUser;
	}

	@Transient
    public List<ERPRole> getRoles() {
        return roles;
    }

    public void setRoles(List<ERPRole> roles) {
        this.roles = roles;
    }
}
