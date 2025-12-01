package com.cassinisys.erp.model.common;

import org.apache.commons.lang.StringUtils;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

/**
 * Created by reddy on 7/1/15.
 */
@Entity
@Table(name = "ERP_AUTONUMBER")
@ApiObject(group = "COMMON")
public class ERPAutoNumber {

	@ApiObjectField(required = true)
	private Integer id;
	@ApiObjectField(required = true)
	private String name;
	@ApiObjectField
	private String description = "";
	@ApiObjectField(required = true)
	private Integer numbers = 5;
	@ApiObjectField(required = true)
	private Integer start = 1;
	@ApiObjectField(required = true)
	private Integer increment = 1;
	@ApiObjectField
	private String padwith = "0";
	@ApiObjectField
	private String prefix = "";
	@ApiObjectField
	private String suffix = "";
	@ApiObjectField(required = true)
	private Integer nextNumber = -1;

	@Id
	@SequenceGenerator(name = "AUTONUMBER_ID_GEN", sequenceName = "AUTONUMBER_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTONUMBER_ID_GEN")
	@Column(name = "AUTONUMBER_ID")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Column(name = "NUMBERS")
	public Integer getNumbers() {
		return numbers;
	}

	public void setNumbers(Integer numbers) {
		this.numbers = numbers;
	}

	@Column(name = "START")
	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	@Column(name = "INCREMENT")
	public Integer getIncrement() {
		return increment;
	}

	public void setIncrement(Integer increment) {
		this.increment = increment;
	}

	@Column(name = "PADWITH")
	public String getPadwith() {
		return padwith;
	}

	public void setPadwith(String padwith) {
		this.padwith = padwith;
	}

	@Column(name = "PREFIX")
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Column(name = "SUFFIX")
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Column(name = "NEXT_NUMBER")
	public Integer getNextNumber() {
		return nextNumber;
	}

	public void setNextNumber(Integer nextNumber) {
		this.nextNumber = nextNumber;
	}

	public String next() {
		String next = "";

		if (prefix != null && !prefix.trim().isEmpty()) {
			next += prefix;
		}

		int n = getNextNumber();

		String s = "" + n;
		if ((numbers - s.length()) > 0) {
			next += StringUtils.repeat(padwith, (numbers - s.length()));
			next += n;
		}

		n = n + increment;
		setNextNumber(n);

		if (suffix != null && !suffix.trim().isEmpty()) {
			next += suffix;
		}

		return next;
	}
}
