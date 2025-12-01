package com.cassinisys.tm.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.tm.filtering.ProjectTaskCriteria;
import com.cassinisys.tm.model.*;
import com.cassinisys.tm.service.PrintService;
import com.cassinisys.tm.service.TaskService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Api(name = "Project tasks",
        description = "Project tasks endpoint",
        group = "Project")
@RestController
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private TaskService taskService;

    @Autowired
    private PrintService printService;


    @RequestMapping(method = RequestMethod.POST)
    public TMProjectTask create(@PathVariable("projectId") Integer projectId,
                                @RequestBody TMProjectTask projectTask) {
        projectTask.setProject(projectId);
        return taskService.createTask(projectTask);
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.PUT)
    public TMProjectTask update(@PathVariable("projectId") Integer projectId,
                                @PathVariable("taskId") Integer taskId,
                                @RequestBody TMProjectTask projectTask) {
        projectTask.setId(taskId);

        return taskService.updateTask(projectTask);

    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("projectId") Integer projectId,
                       @PathVariable("taskId") Integer taskId) {
        taskService.deleteTask(taskId);
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public TMProjectTask get(@PathVariable("projectId") Integer projectId,
                             @PathVariable("taskId") Integer taskId) {
        return taskService.getTask(taskId);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<TMProjectTask> getMultiple(@PathVariable Integer[] ids) {
        return taskService.findMultipleTasks(Arrays.asList(ids));
    }


    @RequestMapping(value = "/persons/[{ids}]", method = RequestMethod.GET)
    public List<TMProjectTask> getDepartmentTasksByPersonIds(@PathVariable Integer[] ids) {
        return taskService.getDepartmentTasksByPersonIds(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<TMProjectTask> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return taskService.findAllTasks(pageable);
    }


    @RequestMapping(value = "/allTasks/{personId}", method = RequestMethod.GET)
    public List<TMProjectTask> getTasksByPerson(@PathVariable("personId") Integer personId) {
        return taskService.findTasksByPerson(personId);
    }


    @RequestMapping(method = RequestMethod.GET)
    public Page<TMProjectTask> getProjectTasks(@PathVariable("projectId") Integer projectId,
                                               PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return taskService.getTasks(projectId, pageable);
    }

    @RequestMapping(value = "/projecttasks", method = RequestMethod.GET)
    public List<TMProjectTask> getTasks(ProjectTaskCriteria criteria) {
        return taskService.findTasks(criteria);
    }

    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<TMProjectTask> getAllTasks(ProjectTaskCriteria criteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);

        return taskService.findAll(criteria, pageable);
    }

    @RequestMapping(value = "/{taskId}/history", method = RequestMethod.GET)
    public List<TMTaskHistory> getTaskHistory(@PathVariable("projectId") Integer projectId,
                                              @PathVariable("taskId") Integer taskId) {
        return taskService.getHistory(taskId);
    }

    @RequestMapping(value = "/{taskId}/finish", method = RequestMethod.GET)
    public TMProjectTask finish(@PathVariable("projectId") Integer projectId,
                                @PathVariable("taskId") Integer taskId,
                                @RequestBody TMProjectTask projectTask) {
        projectTask.setId(taskId);
        return taskService.finishTask(projectTask);
    }

    @RequestMapping(value = "/{taskId}/verify", method = RequestMethod.GET)
    public TMProjectTask verify(@PathVariable("projectId") Integer projectId,
                                @PathVariable("taskId") Integer taskId,
                                @RequestBody TMProjectTask projectTask) {
        projectTask.setId(taskId);
        return taskService.verifyTask(projectTask);
    }

    @RequestMapping(value = "/{taskId}/approve", method = RequestMethod.GET)
    public TMProjectTask approve(@PathVariable("projectId") Integer projectId,
                                 @PathVariable("taskId") Integer taskId,
                                 @RequestBody TMProjectTask projectTask) {
        projectTask.setId(taskId);
        return taskService.approveTask(projectTask);
    }

    @RequestMapping(value = "/{taskId}/reject", method = RequestMethod.GET)
    public TMProjectTask reject(@PathVariable("projectId") Integer projectId,
                                @PathVariable("taskId") Integer taskId,
                                @RequestBody TMProjectTask projectTask) {
        projectTask.setId(taskId);
        return taskService.rejectTask(projectTask);
    }

    @RequestMapping(value = "/{taskId}/images", method = RequestMethod.GET)
    public List<TMTaskImage> getTaskImages(@PathVariable("taskId") Integer taskId) {
        return taskService.getTaskImages(taskId);
    }

    @RequestMapping(value = "/{taskId}/images", method = RequestMethod.POST)
    public TMTaskImage addTaskImage(@RequestBody TMTaskImage image) {
        return taskService.addTaskImage(image);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<TMProjectTask> freeTextSearch(ProjectTaskCriteria criteria,
                                              PageRequest pageRequest) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return taskService.findAll(criteria, pageable);
    }

    @RequestMapping(value = "/locations", method = RequestMethod.GET)
    public List<String> getLocations() {

        List<String> locations = taskService.getLocations();
        return locations;
    }


    @RequestMapping(value = "/pendingreason", method = RequestMethod.POST)
    public TMPendingReason getLocations(@RequestBody TMPendingReason pendingReason) {
        return taskService.createPendingReason(pendingReason);
    }

    @RequestMapping(value = "/pendingreason/reasons", method = RequestMethod.GET)
    public List<TMPendingReason> getReasons(){
        return taskService.getReasons();
    }

    @RequestMapping(value = "/pendingreason/{reasonId}", method = RequestMethod.GET)
    public TMPendingReason getReason(@PathVariable("reasonId") Integer reasonId) {
       return taskService.getReason(reasonId);
    }

    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public void printTask(@RequestParam("assignedTo") Integer assignedTo,
                          @RequestParam("assignedDate") Date assignedDate,
                          HttpServletResponse response) {
        try {
            String html = printService.printTask(assignedTo,assignedDate);
            response.setContentType("text/html");
            response.getWriter().write(html);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    public List<Object[]> getAllTaskStats() {
        return taskService.getAllTaskStats();
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public List<TMProjectTask> getTasksByLocationAndStatus(@RequestParam("location")String location,
                                                           @RequestParam("status")TaskStatus[] statuses) {
        return taskService.getTasksByLocationAndStatus(location, Arrays.asList(statuses));
    }

}
