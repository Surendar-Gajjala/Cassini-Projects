package com.cassinisys.plm.model.dto;

import lombok.Data;

/**
 * Created by CassiniSystems on 11-08-2020.
 */
@Data
public class ProblemSourceDto {

	private String objectNumer;
	private String objectType;
	private String product;
	private String problem;
	private String failureType;
	private String severity;
	private String disposition;

}
