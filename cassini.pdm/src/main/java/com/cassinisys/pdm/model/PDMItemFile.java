package com.cassinisys.pdm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by GSR on 20-01-2017.
 */
@Entity
@Table(name = "PDM_ITEMFILE")
@PrimaryKeyJoinColumn(name = "FILE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMItemFile extends PDMFile {

	@Column(name = "ITEM", nullable = false)
	private Integer item;

	public PDMItemFile(){
		super();
	}


	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}
}
