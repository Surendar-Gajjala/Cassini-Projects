package com.cassinisys.plm.controller.analytics.projects;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.analytics.projects.DrillDownDTO;
import com.cassinisys.plm.model.analytics.projects.DrillDownMap;
import com.cassinisys.plm.model.analytics.projects.ProgramProjectStatusCount;
import com.cassinisys.plm.model.analytics.projects.ProjectTypeDto;
import com.cassinisys.plm.service.analytics.projects.ProjectAnalyticsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by GSR on 19-07-2020.
 */
@RestController
@RequestMapping("/dashboards/projects")
@Api(tags = "PLM.ANALYTICS", description = "Analytics Related")
public class ProjectAnalyticsController extends BaseController {

    @Autowired
    private ProjectAnalyticsService projectAnalyticsService;

    @RequestMapping(value = "/counts", method = RequestMethod.GET)
    public ProjectTypeDto getProjectDashboardCounts() {
        return projectAnalyticsService.getProjectDashboardCounts();
    }

    @RequestMapping(value = "/program/counts", method = RequestMethod.GET)
    public ProgramProjectStatusCount getProgramProjectStatusCount() {
        return projectAnalyticsService.getProgramProjectStatusCount();
    }
    @RequestMapping(value = "/program/drilldown/report", method = RequestMethod.GET)
    public  List<DrillDownMap> getProgramDrillDownReport() {
        return projectAnalyticsService.getProgramDrillDownReport();
    }
}
