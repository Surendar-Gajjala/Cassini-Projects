package com.cassinisys.plm.controller.analytics.oems;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.analytics.oems.OEMTypeDto;
import com.cassinisys.plm.model.analytics.oems.TopManufacturerDto;
import com.cassinisys.plm.model.analytics.oems.TopProblemPartsDto;
import com.cassinisys.plm.model.analytics.oems.TopRecurringPartsDto;
import com.cassinisys.plm.service.analytics.oems.OEMsAnalyticsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by GSR on 19-07-2020.
 */
@RestController
@RequestMapping("/dashboards/oems")
@Api(tags = "PLM.ANALYTICS",description = "Analytics Related")
public class OEMsAnalyticsController extends BaseController {

	@Autowired
	private OEMsAnalyticsService oeMsAnalyticsService;

	@RequestMapping(value = "/counts", method = RequestMethod.GET)
	public OEMTypeDto getStaticOemDashboardCounts() {
		return oeMsAnalyticsService.getStaticOemDashboardCounts();
	}

	@RequestMapping(value = "/parts/status", method = RequestMethod.GET)
	public OEMTypeDto getMfrPartsByStatus() {
		return oeMsAnalyticsService.getMfrPartsByStatus();
	}

	@RequestMapping(value = "/mfrs/status", method = RequestMethod.GET)
	public OEMTypeDto getMfrsByStatus() {
		return oeMsAnalyticsService.getMfrsByStatus();
	}

	@RequestMapping(value = "/mfrs/parts", method = RequestMethod.GET)
	public List<TopManufacturerDto> getMfrsByParts() {
		return oeMsAnalyticsService.getMfrsByParts();
	}

	@RequestMapping(value = "/problem/parts", method = RequestMethod.GET)
	public List<TopProblemPartsDto> getProblemParts() {
		return oeMsAnalyticsService.getProblemParts();
	}

	@RequestMapping(value = "/problem/mfrs", method = RequestMethod.GET)
	public List<TopProblemPartsDto> getProblemMfrs() {
		return oeMsAnalyticsService.getProblemMfrs();
	}

	@RequestMapping(value = "/recurring/parts", method = RequestMethod.GET)
	public List<TopRecurringPartsDto> getRecurringParts() {
		return oeMsAnalyticsService.getRecurringParts();
	}

}
