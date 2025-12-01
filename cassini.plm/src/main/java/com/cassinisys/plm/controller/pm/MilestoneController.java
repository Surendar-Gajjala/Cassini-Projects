package com.cassinisys.plm.controller.pm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.pm.PLMMilestone;
import com.cassinisys.plm.service.pm.MilestoneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyamreddy on 005 5-Feb -18.
 */
@RestController
@RequestMapping("/plm/projects/wbs/{wbsId}/milestones")
@Api(tags = "PLM.PM",description = "Project Related")
public class MilestoneController extends BaseController {

    @Autowired
    private MilestoneService milestoneService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMMilestone createMilestone(@PathVariable("wbsId") Integer wbsId, @RequestBody PLMMilestone milestone) {
        return milestoneService.create(milestone);
    }

    @RequestMapping(value = "/milestoneList/{projectId}",method = RequestMethod.POST)
    public List<PLMMilestone> createMilestones(@PathVariable("projectId") Integer projectId, @PathVariable("wbsId") Integer wbsId, @RequestBody List<PLMMilestone> milestone) throws JsonProcessingException {
        return milestoneService.createMilestones(projectId, milestone);
    }

    @RequestMapping(value = "/{milestoneId}", method = RequestMethod.PUT)
    public PLMMilestone updateMilestone(@PathVariable("wbsId") Integer wbsId, @PathVariable("milestoneId") Integer milestoneId,
                                        @RequestBody PLMMilestone milestone) {
        return milestoneService.update(milestone);
    }

    @RequestMapping(value = "/{milestoneId}", method = RequestMethod.DELETE)
    public void deleteMilestone(@PathVariable("wbsId") Integer wbsId, @PathVariable("milestoneId") Integer milestoneId) {
        milestoneService.delete(milestoneId);
    }

    @RequestMapping(value = "/{milestoneId}", method = RequestMethod.GET)
    public PLMMilestone getMilestone(@PathVariable("wbsId") Integer wbsId, @PathVariable("milestoneId") Integer milestoneId) {
        return milestoneService.get(milestoneId);
    }

    @RequestMapping(value = "/{milestoneId}/finish", method = RequestMethod.PUT)
    public PLMMilestone finishMilestone(@PathVariable("wbsId") Integer wbsId, @PathVariable("milestoneId") Integer milestoneId,
                                        @RequestBody PLMMilestone milestone) throws JsonProcessingException {
        return milestoneService.finishMilestone(milestone);
    }
}
