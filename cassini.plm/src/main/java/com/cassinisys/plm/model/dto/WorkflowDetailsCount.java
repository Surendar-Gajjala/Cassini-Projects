package com.cassinisys.plm.model.dto;

import lombok.Data;

/**
 * Created by CassiniSystems on 03-06-2020.
 */
@Data
public class WorkflowDetailsCount {

	private Integer items = 0;

	private Integer ecos = 0;

	private Integer projects = 0;

	private Integer projectActivitys = 0;

	private Integer manufacturers = 0;

	private Integer manufacturerParts = 0;

	private Integer specifications = 0;

	private Integer requirements = 0;

}
