package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.pm.ProjectTemplateActivity;
import com.cassinisys.plm.model.pm.ProjectTemplateTask;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.pm.ProjectTemplateActivityService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subra on 15-03-2018.
 */
@RestController
@RequestMapping("/plm/templates/wbs/activities")
@Api(tags = "PLM.ITEMS",description = "Items Related")
public class ProjectTemplateActivityController extends BaseController {

    @Autowired
    private ProjectTemplateActivityService projectTemplateActivityService;

    @RequestMapping(method = RequestMethod.POST)
    public ProjectTemplateActivity create(@RequestBody ProjectTemplateActivity projectTemplateActivity) {
        return projectTemplateActivityService.create(projectTemplateActivity);
    }

    @RequestMapping(value = "/{activityId}", method = RequestMethod.PUT)
    public ProjectTemplateActivity update(@PathVariable("activityId") Integer activityId, @RequestBody ProjectTemplateActivity projectTemplateActivity) {
        return projectTemplateActivityService.update(projectTemplateActivity);
    }

    @RequestMapping(value = "/{activityId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("activityId") Integer activityId) {
        projectTemplateActivityService.delete(activityId);
    }

    @RequestMapping(value = "/{activityId}", method = RequestMethod.GET)
    public ProjectTemplateActivity getProjectTemplate(@PathVariable("activityId") Integer activityId) {
        return projectTemplateActivityService.get(activityId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ProjectTemplateActivity> getAll() {
        return projectTemplateActivityService.getAll();
    }

    @RequestMapping(value = "/{wbsId}/byName", method = RequestMethod.GET)
    public ProjectTemplateActivity getActivityByNameAndWbs(@PathVariable("wbsId") Integer wbsId, @RequestParam("activityName") String activityName) {
        return projectTemplateActivityService.getActivityByNameAndWbs(activityName, wbsId);
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public ProjectTemplateTask createTask(@RequestBody ProjectTemplateTask projectTemplateTask) {
        return projectTemplateActivityService.createTask(projectTemplateTask);
    }

    @RequestMapping(value = "/task/{taskId}", method = RequestMethod.PUT)
    public ProjectTemplateTask updateTask(@PathVariable("taskId") Integer taskId, @RequestBody ProjectTemplateTask projectTemplateTask) {
        return projectTemplateActivityService.updateTask(taskId, projectTemplateTask);
    }

    @RequestMapping(value = "/task/{taskId}", method = RequestMethod.GET)
    public ProjectTemplateTask getTask(@PathVariable("taskId") Integer taskId) {
        return projectTemplateActivityService.getTask(taskId);
    }

    @RequestMapping(value = "/{activityId}/tasks", method = RequestMethod.GET)
    public List<ProjectTemplateTask> getProjectTemplateTasks(@PathVariable("activityId") Integer activityId) {
        return projectTemplateActivityService.getProjectTemplateTasks(activityId);
    }

    @RequestMapping(value = "/deleteTask/{taskId}", method = RequestMethod.DELETE)
    public void deleteTemplateTasks(@PathVariable("taskId") Integer taskId) {
        projectTemplateActivityService.deleteTemplateTasks(taskId);
    }

    @RequestMapping(value="/activityList", method = RequestMethod.POST)
    public List<ProjectTemplateActivity> createActivities(@RequestBody List<ProjectTemplateActivity> plmActivityList) {
        return projectTemplateActivityService.createActivities(plmActivityList);
    }

    @RequestMapping(value = "/{activityId}/taskList", method = RequestMethod.POST)
    public List<ProjectTemplateTask> createActivityTasks(@PathVariable("activityId") Integer activityId, @RequestBody List<ProjectTemplateTask> activityTaskList) {
        return projectTemplateActivityService.createActivityTasks(activityTaskList);
    }
    @RequestMapping(value = "/task/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return projectTemplateActivityService.attachTaskTemplateWorkflow(id, wfDefId);
    }
}
