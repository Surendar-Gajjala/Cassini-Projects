package com.cassinisys.plm.model.dto;

import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import lombok.Data;

/**
 * Created by CassiniSystems on 11-08-2020.
 */
@Data
public class RelatedItemDto {

	private Integer itemId;

	private Integer objectId;

	private String itemNumber;

	private String itemName;

	private PLMLifeCyclePhase lifeCyclePhase;

	private String revision;
	private String phase;

	private String itemType;

	private String description;

}
