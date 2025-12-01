package com.cassinisys.plm.model.analytics.workflow;

import lombok.Data;

/**
 * Created by subramanyam on 21-07-2020.
 */
@Data
public class WorkflowCountsDto {
	private Integer items = 0;
	private Integer changes = 0;
	private Integer quality = 0;
	private Integer mfr = 0;
	private Integer mfrParts = 0;
	private Integer projects = 0;
	private Integer requirements = 0;
	private Integer workOrder = 0;
	private Integer npr = 0;

}
