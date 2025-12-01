package com.cassinisys.plm.controller.analytics.changes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.analytics.changes.ChangeCardCounts;
import com.cassinisys.plm.model.analytics.changes.ChangeTypesDto;
import com.cassinisys.plm.service.analytics.changes.ChangeAnalyticsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by subramanyam on 17-07-2020.
 */
@RestController
@RequestMapping("/dashboards/changes")
@Api(tags = "PLM.ANALYTICS",description = "Analytics Related")
public class ChangeAnalyticsController extends BaseController {

    @Autowired
    private ChangeAnalyticsService changeAnalyticsService;

    @RequestMapping(value = "/counts", method = RequestMethod.GET)
    public ChangeTypesDto getChangeDashboardCounts() {
        return changeAnalyticsService.getChangeDashboardCounts();
    }

    @RequestMapping(value = "/card/counts", method = RequestMethod.GET)
    public ChangeCardCounts getChangeDashboardCardCounts() {
        return changeAnalyticsService.getChangeDashboardCardCounts();
    }

    @RequestMapping(value = "/type/counts", method = RequestMethod.GET)
    public ChangeTypesDto getChangeTypeCounts() {
        return changeAnalyticsService.getChangeTypeCounts();
    }
}
