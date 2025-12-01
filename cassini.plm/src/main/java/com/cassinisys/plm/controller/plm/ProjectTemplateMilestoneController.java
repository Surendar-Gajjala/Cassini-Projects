package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.pm.ProjectTemplateMilestone;
import com.cassinisys.plm.service.pm.ProjectTemplateMilestoneService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyam reddy on 15-03-2018.
 */
@RestController
@RequestMapping("/plm/templates/wbs/milestones")
@Api(tags = "PLM.ITEMS",description = "Items Related")
public class ProjectTemplateMilestoneController extends BaseController {

    @Autowired
    private ProjectTemplateMilestoneService projectTemplateMilestoneService;

    @RequestMapping(method = RequestMethod.POST)
    public ProjectTemplateMilestone create(@RequestBody ProjectTemplateMilestone projectTemplateMilestone) {
        return projectTemplateMilestoneService.create(projectTemplateMilestone);
    }

    @RequestMapping(value = "/{milestoneId}", method = RequestMethod.PUT)
    public ProjectTemplateMilestone update(@PathVariable("milestoneId") Integer milestoneId, @RequestBody ProjectTemplateMilestone projectTemplateMilestone) {
        return projectTemplateMilestoneService.update(projectTemplateMilestone);
    }

    @RequestMapping(value = "/{milestoneId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("milestoneId") Integer milestoneId) {
        projectTemplateMilestoneService.delete(milestoneId);
    }

    @RequestMapping(value = "/{milestoneId}", method = RequestMethod.GET)
    public ProjectTemplateMilestone getProjectTemplate(@PathVariable("milestoneId") Integer milestoneId) {
        return projectTemplateMilestoneService.get(milestoneId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ProjectTemplateMilestone> getAll() {
        return projectTemplateMilestoneService.getAll();
    }

    @RequestMapping(value = "/{wbsId}/byName", method = RequestMethod.GET)
    public ProjectTemplateMilestone getMilestoneByNameAndWbs(@PathVariable("wbsId") Integer wbsId, @RequestParam("milestoneName") String milestoneName) {
        return projectTemplateMilestoneService.getMilestoneByNameAndWbs(milestoneName, wbsId);
    }

    @RequestMapping(value = "/milestoneList",method = RequestMethod.POST)
    public List<ProjectTemplateMilestone> createMilestones(@RequestBody List<ProjectTemplateMilestone> milestone) {
        return projectTemplateMilestoneService.createMilestones(milestone);
    }
}
