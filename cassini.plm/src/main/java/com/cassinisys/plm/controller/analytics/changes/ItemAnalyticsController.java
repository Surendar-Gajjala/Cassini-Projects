package com.cassinisys.plm.controller.analytics.changes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.analytics.items.*;
import com.cassinisys.plm.service.analytics.items.ItemAnalyticsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by subramanyam on 17-07-2020.
 */
@RestController
@RequestMapping("/dashboards/items")
@Api(tags = "PLM.ANALYTICS",description = "Analytics Related")
public class ItemAnalyticsController extends BaseController {

    @Autowired
    private ItemAnalyticsService itemAnalyticsService;

    @RequestMapping(value = "/dashboard/counts", method = RequestMethod.GET)
    public ItemDashboardCountsDto getDashboardCounts() {
        return itemAnalyticsService.getDashboardCounts();
    }

    @RequestMapping(value = "/class/counts", method = RequestMethod.GET)
    public ItemDashboardCountsDto getItemsByClass() {
        return itemAnalyticsService.getItemsByClass();
    }

    @RequestMapping(value = "/class/card/counts", method = RequestMethod.GET)
    public ItemClassDto getItemsByItemClass() {
        return itemAnalyticsService.getItemsByItemClass();
    }

    @RequestMapping(value = "/configuration/counts", method = RequestMethod.GET)
    public ItemConfigurationCounts getItemsByConfigurations() {
        return itemAnalyticsService.getItemsByConfigurations();
    }

    @RequestMapping(value = "/products/status/counts", method = RequestMethod.GET)
    public ItemDashboardCountsDto getProductItemsByStatus() {
        return itemAnalyticsService.getProductItemsByStatus();
    }

    @RequestMapping(value = "/assemblies/status/counts", method = RequestMethod.GET)
    public ItemDashboardCountsDto getAssemblyItemsByStatus() {
        return itemAnalyticsService.getAssemblyItemsByStatus();
    }

    @RequestMapping(value = "/parts/status/counts", method = RequestMethod.GET)
    public ItemDashboardCountsDto getPartItemsByStatus() {
        return itemAnalyticsService.getPartItemsByStatus();
    }

    @RequestMapping(value = "/documents/status/counts", method = RequestMethod.GET)
    public ItemDashboardCountsDto getDocumentItemsByStatus() {
        return itemAnalyticsService.getDocumentItemsByStatus();
    }

    @RequestMapping(value = "/others/status/counts", method = RequestMethod.GET)
    public ItemDashboardCountsDto getOtherItemsByStatus() {
        return itemAnalyticsService.getOtherItemsByStatus();
    }

    @RequestMapping(value = "/lifecycle/counts", method = RequestMethod.GET)
    public ItemsByLifecycleCounts getItemsByLifeCyclePhases() {
        return itemAnalyticsService.getItemsByLifeCyclePhases();
    }

    @RequestMapping(value = "/products/lifecycle/counts", method = RequestMethod.GET)
    public ItemsByLifecycleCounts getProductItemsByLifecyclePhases() {
        return itemAnalyticsService.getProductItemsByLifecyclePhases();
    }

    @RequestMapping(value = "/assemblies/lifecycle/counts", method = RequestMethod.GET)
    public ItemsByLifecycleCounts getAssemblyItemsByLifecyclePhases() {
        return itemAnalyticsService.getAssemblyItemsByLifecyclePhases();
    }

    @RequestMapping(value = "/parts/lifecycle/counts", method = RequestMethod.GET)
    public ItemsByLifecycleCounts getPartItemsByLifecyclePhases() {
        return itemAnalyticsService.getPartItemsByLifecyclePhases();
    }

    @RequestMapping(value = "/documents/lifecycle/counts", method = RequestMethod.GET)
    public ItemsByLifecycleCounts getDocumentItemsByLifecyclePhases() {
        return itemAnalyticsService.getDocumentItemsByLifecyclePhases();
    }

    @RequestMapping(value = "/others/lifecycle/counts", method = RequestMethod.GET)
    public ItemsByLifecycleCounts getOtherItemsByLifecyclePhases() {
        return itemAnalyticsService.getOtherItemsByLifecyclePhases();
    }

    @RequestMapping(value = "/top/problem/items", method = RequestMethod.GET)
    public List<TopProblemItems> getTopProblemItems() {
        return itemAnalyticsService.getTopProblemItems();
    }

    @RequestMapping(value = "/top/problem/itemtypes", method = RequestMethod.GET)
    public List<TopProblemItemTypes> getTopProductItemTypes() {
        return itemAnalyticsService.getTopProblemItemTypes();
    }

    @RequestMapping(value = "/top/changing/itemtypes", method = RequestMethod.GET)
    public List<TopProblemItemTypes> getTopFrequentlyChangingItemTypes() {
        return itemAnalyticsService.getTopFrequentlyChangingItemTypes();
    }
}
