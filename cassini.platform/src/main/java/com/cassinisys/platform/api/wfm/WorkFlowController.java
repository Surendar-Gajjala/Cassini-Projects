package com.cassinisys.platform.api.wfm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.wfm.*;
import com.cassinisys.platform.service.wfm.WorkFlowService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/wfm/workflows")
@Api(tags = "PLATFORM.WORKFLOW",description = "Workflow endpoints")
public class WorkFlowController extends BaseController {

	@Autowired
	private PageRequestConverter pageRequestConverter;

	@Autowired
	private WorkFlowService workFlowService;

	@RequestMapping(method = RequestMethod.POST)
	public WorkFlow create(@RequestBody WorkFlow workFlow) {
		workFlow.setId(null);
		return workFlowService.create(workFlow);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public WorkFlow update(@PathVariable("id") Integer id,
			@RequestBody WorkFlow workFlow) {
		workFlow.setId(id);
		return workFlowService.update(workFlow);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		workFlowService.delete(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public WorkFlow get(@PathVariable("id") Integer id) {
		return workFlowService.get(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<WorkFlow> getAll() {
		
		return workFlowService.getAll();
	}

	@RequestMapping(value = "/pagable", method = RequestMethod.GET)
	public Page<WorkFlow> findAll(PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return workFlowService.findAll(pageable);
	}

	@RequestMapping (value = "/{id}//history", method = RequestMethod.GET)
	public List<WorkflowHistory> getHistory(@PathVariable("id") Integer id) {
		return workFlowService.getHistory(id);
	}
	
	
	@RequestMapping(value = "/{wfId}/activities", method = RequestMethod.POST)
	public Activity createactivity(@PathVariable("wfId") Integer wfId, @RequestBody Activity activity) {
		activity.setId(null);
		activity.setWorkflow(wfId);
		return workFlowService.createActivity(activity);
	}
	
	@RequestMapping(value = "/{wfId}/activities/{id}", method = RequestMethod.PUT)
	public Activity update(@PathVariable("wfId") Integer wfId, @PathVariable("id") Integer id,
			@RequestBody Activity activity) {
		activity.setWorkflow(wfId);
		activity.setId(id);
		return workFlowService.updateActivity(activity);
	}

	@RequestMapping(value = "/{wfId}/activities/{id}", method = RequestMethod.DELETE)
	public void deleteActivity(@PathVariable("wfId") Integer wfId, @PathVariable("id") Integer id) {
		workFlowService.deleteActivity(id);
	}

	@RequestMapping(value = "/{wfId}/activities/{id}", method = RequestMethod.GET)
	public Activity getactivity(@PathVariable("wfId") Integer wfId, @PathVariable("id") Integer id) {
		return workFlowService.getActivity(id);
	}


	@RequestMapping(value = "/{wfId}/activities", method = RequestMethod.GET)
	public List<Activity> getActivities(@PathVariable("wfId") Integer wfId) {
		
		return workFlowService.getActivities(wfId);
	}

	@RequestMapping(value = "/{wfId}/activities/pagable", method = RequestMethod.GET)
	public Page<Activity> findActivities(@PathVariable("wfId") Integer wfId, PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return workFlowService.findActivities(pageable);
	}
	
	@RequestMapping(value = "/{wfId}/activities/{actvId}/actions", method = RequestMethod.POST)
	public Action createAction(@PathVariable("actvId") Integer actvId, @RequestBody Action action) {
		action.setId(null);
		
		return workFlowService.createAction(action);
	}
	
	@RequestMapping(value = "/{wfId}/activities/{actvId}/actions/{id}", method = RequestMethod.PUT)
	public Action update( @PathVariable("actvId") Integer actvId, @PathVariable("id") Integer id,
			@RequestBody Action action) {
		action.setId(id);
		return workFlowService.updateAction(action);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/actions/{id}", method = RequestMethod.DELETE)
	public void deleteAction( @PathVariable("actvId") Integer actvId, @PathVariable("id") Integer id) {
		workFlowService.deleteAction(id);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/actions/{id}", method = RequestMethod.GET)
	public Action getAction(@PathVariable("id") Integer id) {
		return workFlowService.getAction(id);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/actions/{id}/perform", method = RequestMethod.GET)
	public Action performAction(@PathVariable("wfId") Integer wfId,
								@PathVariable("actvId") Integer actvId,
								@PathVariable("id") Integer id) {
		return workFlowService.performAction(wfId, actvId, id);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/actions", method = RequestMethod.GET)
	public List<Action> getActions( @PathVariable("actvId") Integer actvId) {
		
		return workFlowService.getActionsBySource(actvId);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/actions/pageable", method = RequestMethod.GET)
	public Page<Action> findActions(PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return workFlowService.findActions(pageable);
	}

	
	@RequestMapping(value = "/{wfId}/activities/{actvId}/tasks", method = RequestMethod.POST)
	public WorkflowTask createTask(@PathVariable("actvId") Integer actvId, @RequestBody WorkflowTask task) {
		task.setId(null);
		
		return workFlowService.createTask(task);
	}
	
	@RequestMapping(value = "/{wfId}/activities/{actvId}/tasks/{id}", method = RequestMethod.PUT)
	public WorkflowTask update( @PathVariable("actvId") Integer actvId, @PathVariable("id") Integer id,
			@RequestBody WorkflowTask task) {
		task.setId(id);
		return workFlowService.updateTask(task);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/tasks/{id}", method = RequestMethod.DELETE)
	public void deleteTask( @PathVariable("actvId") Integer actvId, @PathVariable("id") Integer id) {
		workFlowService.deleteTask(id);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/tasks/{id}", method = RequestMethod.GET)
	public WorkflowTask getTask(@PathVariable("id") Integer id) {
		return workFlowService.getTask(id);
	}

	@RequestMapping(value = "/{wfId}/activities/{actvId}/tasks", method = RequestMethod.GET)
	public List<WorkflowTask> getTasks( @PathVariable("actvId") Integer actvId) {
		
		return workFlowService.getTasksByActivity(actvId);
	}
	@RequestMapping(value = "/{wfId}/activities/{actvId}/tasks/pageable", method = RequestMethod.GET)
	public Page<WorkflowTask> findTasks(PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return workFlowService.findTasks(pageable);
	}

	@RequestMapping(value = "/{wfId}/activities/{actv}", method = RequestMethod.GET)
	public List<ActivityAssignment> findActivities(@PathVariable Integer actv) {
		return workFlowService.getByActivity(actv);
	}

	@RequestMapping(value = "/{wfId}/activities/{actv}/assignments", method = RequestMethod.GET)
	public List<ActivityAssignment> findAssignments(@PathVariable Integer actv) {
		return workFlowService.getByAssignedTo(actv);
	}

	@RequestMapping(value = "/{wfId}/activities/{actv}/assignments", method = RequestMethod.POST)
	public List<ActivityAssignment> createAssignments(@RequestBody List<ActivityAssignment> activityAssignments) {
		return workFlowService.create(activityAssignments);

	}
}

