package com.cassinisys.plm.controller.analytics.oems;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.analytics.workflow.WorkflowCountsDto;
import com.cassinisys.plm.model.analytics.workflow.WorkflowTypesDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.analytics.oems.OEMsAnalyticsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by CassiniSystems on 23-07-2020.
 */
@RestController
@RequestMapping("/dashboards/workflow")
@Api(tags = "PLM.ANALYTICS",description = "Analytics Related")
public class WorkflowAnalyticsController extends BaseController {

	@Autowired
	private OEMsAnalyticsService oeMsAnalyticsService;

	@RequestMapping(value = "/counts", method = RequestMethod.GET)
	public WorkflowTypesDto getStaticWorkflowDashboardCounts() {
		return oeMsAnalyticsService.getStaticWorkflowDashboardCounts();
	}

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public WorkflowTypesDto getWorkflowByStatus() {
		return oeMsAnalyticsService.getWorkflowByStatus();
	}

	@RequestMapping(value = "/objectType/status", method = RequestMethod.GET)
	public WorkflowTypesDto getObjectTypeWorkflows() {
		return oeMsAnalyticsService.getObjectTypeWorkflows();
	}

	@RequestMapping(value = "/objectType/change", method = RequestMethod.GET)
	public WorkflowTypesDto getChangeTypeWorkflows() {
		return oeMsAnalyticsService.getChangeTypeWorkflows();
	}

	@RequestMapping(value = "/started/all", method = RequestMethod.GET)
	public List<PLMWorkflow> getAllStartedWorkflows() {
		return oeMsAnalyticsService.getAllStartedWorkflows();
	}

	@RequestMapping(value = "/type/card/counts", method = RequestMethod.GET)
	public WorkflowCountsDto getTypeByCounts() {
		return oeMsAnalyticsService.getTypeByCounts();

	}
}
