package com.cassinisys.platform.api.wfm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.wfm.ActionDefinition;
import com.cassinisys.platform.model.wfm.ActivityDefinition;
import com.cassinisys.platform.model.wfm.TaskDefinition;
import com.cassinisys.platform.model.wfm.WorkFlowDefinition;
import com.cassinisys.platform.service.wfm.WorkFlowDefinitionService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/wfm/workflowdefinitions")
@Api(tags = "PLATFORM.WORKFLOW",description = "Workflow endpoints")
public class WorkFlowDefinitionController extends BaseController {

	@Autowired
	private PageRequestConverter pageRequestConverter;

	@Autowired
	private WorkFlowDefinitionService workFlowDefService;
	
	@RequestMapping(method = RequestMethod.POST)
	public WorkFlowDefinition create(@RequestBody WorkFlowDefinition workFlowDef) {
		workFlowDef.setId(null);
		return workFlowDefService.create(workFlowDef);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public WorkFlowDefinition update(@PathVariable("id") Integer id,
			@RequestBody WorkFlowDefinition workFlowDef) {
		workFlowDef.setId(id);
		return workFlowDefService.update(workFlowDef);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteWorkFlow(@PathVariable("id") Integer id) {
		workFlowDefService.delete(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public WorkFlowDefinition get(@PathVariable("id") Integer id) {
		return workFlowDefService.get(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<WorkFlowDefinition> getAll() {
		
		return workFlowDefService.getAll();
	}

	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<WorkFlowDefinition> findAll(PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return workFlowDefService.findAll( pageable);
	}

	
	@RequestMapping(value = "/{wfId}/activities/", method = RequestMethod.POST)
	public ActivityDefinition create(@PathVariable("wfId") Integer wfId, @RequestBody ActivityDefinition activityDef) {
		activityDef.setId(null);
		activityDef.setWorkflow(wfId);
		return workFlowDefService.createActivity(activityDef);
	}

	@RequestMapping(value = "/{wfId}/activities/{id}", method = RequestMethod.PUT)
	public ActivityDefinition update(@PathVariable("wfId") Integer wfId, @PathVariable("id") Integer id,
			@RequestBody ActivityDefinition activityDef) {
		activityDef.setId(id);
		activityDef.setWorkflow(wfId);
		return workFlowDefService.updateActivity(activityDef);
	}

	@RequestMapping(value = "/{wfId}/activities/{id}", method = RequestMethod.DELETE)
	public void deleteactivity( @PathVariable("id") Integer id) {
		workFlowDefService.deleteActivity(id);
	}

	@RequestMapping(value = "/{wfId}/activities/{id}", method = RequestMethod.GET)
	public ActivityDefinition getActivity(@PathVariable("id") Integer id) {
		return workFlowDefService.getActivity(id);
	}
	
	@RequestMapping(value = "/{wfId}/activities/", method = RequestMethod.GET)
	public List<ActivityDefinition> getActivities(@PathVariable("wfId") Integer wfId) {
		return workFlowDefService.getActivities(wfId);
	}

	@RequestMapping(value = "/{wfId}/activities/pagable", method = RequestMethod.GET)
	public Page<ActivityDefinition> findAllActivity(PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return workFlowDefService.findAllActivity( pageable);
	}
	
	
	@RequestMapping(value = "/{wfId}/activities/{actvId}/actions", method = RequestMethod.POST)
	public ActionDefinition create(@PathVariable("actvId") Integer actvId, @RequestBody ActionDefinition actionDef) {
		actionDef.setId(null);
		actionDef.setSource(actvId);
		return workFlowDefService.createAction(actionDef);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/actions/{id}", method = RequestMethod.PUT)
	public ActionDefinition update(@PathVariable("actvId") Integer actvId, @PathVariable("id") Integer id,
			@RequestBody ActionDefinition actionDef) {
		actionDef.setId(id);
		actionDef.setSource(actvId);
		return workFlowDefService.updateAction(actionDef);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/actions/{id}", method = RequestMethod.DELETE)
	public void deleteAction(@PathVariable("id") Integer id) {
		workFlowDefService.deleteAction(id);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/actions/{id}", method = RequestMethod.GET)
	public ActionDefinition getAction(@PathVariable("id") Integer id) {
		return workFlowDefService.getAction(id);
	}
	
	@RequestMapping(value = "/{wfId}/activities/{actvId}/actions", method = RequestMethod.GET)
	public List<ActionDefinition> getActions(@PathVariable("actvId") Integer actvId) {
		return workFlowDefService.getActions(actvId);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/actions/pagable", method = RequestMethod.GET)
	public Page<ActionDefinition> findAllActions(PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return workFlowDefService.findAllActions( pageable);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/tasks", method = RequestMethod.POST)
	public TaskDefinition createTask(@PathVariable("actvId") Integer actvId, @RequestBody TaskDefinition task) {
		task.setId(null);

		return workFlowDefService.createTask(task);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/tasks/{id}", method = RequestMethod.PUT)
	public TaskDefinition update( @PathVariable("actvId") Integer actvId, @PathVariable("id") Integer id,
						@RequestBody TaskDefinition task) {
		task.setId(id);
		return workFlowDefService.updateTask(task);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/tasks/{id}", method = RequestMethod.DELETE)
	public void deleteTask( @PathVariable("actvId") Integer actvId, @PathVariable("id") Integer id) {
		workFlowDefService.deleteTask(id);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/tasks/{id}", method = RequestMethod.GET)
	public TaskDefinition getTask(@PathVariable("id") Integer id) {
		return workFlowDefService.getTask(id);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/tasks", method = RequestMethod.GET)
	public List<TaskDefinition> getTasks( @PathVariable("actvId") Integer actvId) {

		return workFlowDefService.getTasksByActivity(actvId);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/tasks/pagable", method = RequestMethod.GET)
	public Page<TaskDefinition> findTasks(PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return workFlowDefService.findTasks( pageable);
	}
	
}
