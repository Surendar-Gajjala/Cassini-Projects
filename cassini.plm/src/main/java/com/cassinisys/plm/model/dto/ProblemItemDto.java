package com.cassinisys.plm.model.dto;

import lombok.Data;

/**
 * Created by CassiniSystems on 11-08-2020.
 */
@Data
public class ProblemItemDto {

	private Integer itemId;

	private Integer objectId;

	private String itemNumber;

	private String itemName;

	private String lifeCyclePhase;

	private String revision;
	private String notes;

	private String itemType;

	private String description;


}
