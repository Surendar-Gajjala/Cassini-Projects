package com.cassinisys.erp.model.production;

import com.cassinisys.erp.model.hrm.ERPBusinessUnit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 7/22/15.
 */
@Entity
@Table(name = "ERP_PRODUCTCATEGORY")
@ApiObject(group = "PRODUCTION")
public class ERPProductCategory {

	@ApiObjectField(required = true)
	private Integer id;
	@ApiObjectField
	private ERPBusinessUnit businessUnit;
	@ApiObjectField(required = true)
	private String code;
	@ApiObjectField(required = true)
	private Integer type;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField
	private String description;
	@ApiObjectField
	private Integer parent;
	@ApiObjectField
	private List<ERPProductCategory> children = new ArrayList<>();

	@Id
	@SequenceGenerator(name = "PRODUCTCATEGORY_ID_GEN", sequenceName = "PRODUCTCATEGORY_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTCATEGORY_ID_GEN")
	@Column(name = "CATEGORY_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BUSINESS_UNIT")
    public ERPBusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(ERPBusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }


    @Column(name = "CATEGORY_CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "PRODUCT_TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "PARENTCATEGORY")
	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	@Transient
	public List<ERPProductCategory> getChildren() {
		return children;
	}

	public void setChildren(List<ERPProductCategory> children) {
		this.children = children;
	}
}
