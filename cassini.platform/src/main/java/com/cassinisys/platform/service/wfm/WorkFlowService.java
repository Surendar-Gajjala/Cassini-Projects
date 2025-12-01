package com.cassinisys.platform.service.wfm;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.wfm.*;
import com.cassinisys.platform.repo.wfm.*;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


@Service
public class WorkFlowService implements CrudService<WorkFlow, Integer>,
		PageableService<WorkFlow, Integer> {


	@Autowired
	private WorkFlowRepository workFlowRepository;

	@Autowired
	private ActivityAssignmentRepository activityAssignmentRepository;

	@Autowired
	private WorkFlowActivityRepository activityRepository;

	@Autowired
	private ActionRepository actionRepository;

	@Autowired
	private WorkflowTaskRepository taskRepository;

	@Autowired
	private WorkFlowHistoryRepository historyRepository;

	@Autowired
	private SessionWrapper sessionWrapper;

	@Autowired
	private WorkflowUserInboxRepository userInboxRepository;


	@Transactional
	public WorkFlow create(WorkFlow workFlow) {
		checkNotNull(workFlow);
		workFlow.setId(null);

		return workFlowRepository.save(workFlow);
	}

	@Transactional
	public WorkFlow update(WorkFlow workFlow) {
		checkNotNull(workFlow);
		checkNotNull(workFlow.getId());
		return workFlowRepository.save(workFlow);
	}

	@Transactional
	public void delete(Integer id) {
		checkNotNull(id);
		workFlowRepository.delete(id);
	}

	@Transactional(readOnly = true)
	public WorkFlow get(Integer id) {
		checkNotNull(id);
		WorkFlow workFlow = workFlowRepository.findOne(id);
		if (workFlow == null) {
			throw new ResourceNotFoundException();
		}

		List<Activity> activities = activityRepository.findByWorkflow(id);
		activities.forEach(a -> {
			List<Action> actions = actionRepository.findBySource(a.getId());
			a.setActions(actions);

			List<WorkflowTask> tasks = taskRepository.findByActivity(a.getId());
			a.setTasks(tasks != null ? tasks : new ArrayList<WorkflowTask>());
		});
		workFlow.setActivities(activities);
		return workFlow;
	}

	@Transactional(readOnly = true)
	public List<WorkFlow> getAll() {
		return workFlowRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Page<WorkFlow> findAll(Pageable pageable) {
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order(Direction.DESC,
					"name")));
		}
		return workFlowRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public List<WorkflowHistory> getHistory(Integer id) {
		return historyRepository.findByWorkflow(id);
	}


	@Transactional
	public Activity createActivity(Activity activity) {
		checkNotNull(activity);
		activity.setId(null);

		return activityRepository.save(activity);
	}


	@Transactional
	public Activity updateActivity(Activity activity) {
		checkNotNull(activity);
		checkNotNull(activity.getId());
		return activityRepository.save(activity);
	}


	@Transactional
	public void deleteActivity(Integer id) {
		checkNotNull(id);
		activityRepository.delete(id);
	}

	@Transactional(readOnly = true)
	public Activity getActivity(Integer id) {
		checkNotNull(id);
		Activity activity = activityRepository.findOne(id);
		if (activity == null) {
			throw new ResourceNotFoundException();
		}
		return activity;
	}

	@Transactional(readOnly = true)
	public List<Activity> getActivities() {
		return activityRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Activity> getActivities(Integer wfId) {
		return activityRepository.findByWorkflow(wfId);
	}

	@Transactional(readOnly = true)
	public Page<Activity> findActivities(Pageable pageable) {
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order(Direction.DESC,
					"definition")));
		}
		return activityRepository.findAll(pageable);
	}

	@Transactional
	public Action createAction(Action action) {
		checkNotNull(action);
		action.setId(null);

		return actionRepository.save(action);
	}


	@Transactional
	public Action updateAction(Action action) {
		checkNotNull(action);
		checkNotNull(action.getId());
		return actionRepository.save(action);
	}


	@Transactional
	public void deleteAction(Integer id) {
		checkNotNull(id);
		actionRepository.delete(id);
	}

	@Transactional
	public Action getAction(Integer id) {
		checkNotNull(id);
		Action workFlow = actionRepository.findOne(id);
		if (workFlow == null) {
			throw new ResourceNotFoundException();
		}
		return workFlow;
	}

	@Transactional
	public Action performAction(Integer workflowId, Integer activityId, Integer actionId) {
		Action action = null;

		WorkFlow workflow = workFlowRepository.findOne(workflowId);
		Activity activity = activityRepository.findOne(activityId);
		action = actionRepository.findOne(actionId);

		Activity targetActivity = activityRepository.findOne(action.getTarget());


		if (activity.getType() == ActivityType.START) {
			workflow.setStatus(WorkFlowStatus.STARTED);
			workflow.setStartDate(new Date());

			activity.setStatus(ActivityStatus.STARTED);
			activity.setStartDate(new Date());
			activity = activityRepository.save(activity);
		} else if (activity.getType() == ActivityType.END) {
			workflow.setStatus(WorkFlowStatus.FINISHED);
			workflow.setFinishDate(new Date());
		}

		activity.setStatus(ActivityStatus.FINISHED);
		activity.setFinishDate(new Date());
		activity = activityRepository.save(activity);

		targetActivity.setStatus(ActivityStatus.STARTED);
		targetActivity.setStartDate(new Date());

		if (targetActivity.getType() == ActivityType.END) {
			activity.setStatus(ActivityStatus.FINISHED);
			activity.setFinishDate(new Date());
		}

		targetActivity = activityRepository.save(targetActivity);

		workflow.setCurrentActivity(targetActivity.getId());
		workflow = workFlowRepository.save(workflow);

		action.setTimestamp(new Date());
		action.setActor(sessionWrapper.getSession().getLogin().getPerson().getId());
		action = actionRepository.save(action);

		if (targetActivity.getName() != null) {
			WorkflowUserInbox inbox = new WorkflowUserInbox();
			inbox.setWorkflowId(workflow.getId());
			inbox.setMessageType("WORKFLOW");
			inbox.setMessage("Activity " + targetActivity.getName() + " needs your action");
			inbox.setTimeStamp(new Date());
			inbox.setObjectType(ObjectType.ACTIVITY);
			inbox.setObjectId(targetActivity.getId());
		}
		return action;
	}

	@Transactional(readOnly = true)
	public List<Action> getActions() {
		return actionRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Action> getActionsBySource(Integer actvId) {
		return actionRepository.findBySource(actvId);
	}

	@Transactional(readOnly = true)
	public List<Action> getActionsByTarget(Integer actvId) {
		return actionRepository.findByTarget(actvId);
	}

	@Transactional(readOnly = true)
	public Page<Action> findActions(Pageable pageable) {
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order(Direction.DESC,
					"definition")));
		}
		return actionRepository.findAll(pageable);
	}

	@Transactional
	public WorkflowTask createTask(WorkflowTask task) {
		checkNotNull(task);
		task.setId(null);

		return taskRepository.save(task);
	}


	@Transactional
	public WorkflowTask updateTask(WorkflowTask task) {
		checkNotNull(task);
		checkNotNull(task.getId());
		return taskRepository.save(task);
	}


	@Transactional
	public void deleteTask(Integer id) {
		checkNotNull(id);
		taskRepository.delete(id);
	}

	@Transactional(readOnly = true)
	public WorkflowTask getTask(Integer id) {
		checkNotNull(id);
		WorkflowTask task = taskRepository.findOne(id);
		if (task == null) {
			throw new ResourceNotFoundException();
		}
		return task;
	}

	@Transactional(readOnly = true)
	public List<WorkflowTask> getTasks() {
		return taskRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<WorkflowTask> getTasksByActivity(Integer actvId) {
		return taskRepository.findByActivity(actvId);
	}

	@Transactional(readOnly = true)
	public Page<WorkflowTask> findTasks(Pageable pageable) {
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order(Direction.ASC,
					"name")));
		}
		return taskRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public List<ActivityAssignment> getByActivity(Integer activity) {
		return activityAssignmentRepository.findByActivity(activity);
	}

	@Transactional(readOnly = true)
	public List<ActivityAssignment> getByAssignedTo(Integer activity) {
		return activityAssignmentRepository.findByAssignedTo(activity);
	}

	@Transactional
	public List<ActivityAssignment>  create(List<ActivityAssignment> activityAssignments) {
		checkNotNull(activityAssignments);
		List<ActivityAssignment> assignments = new ArrayList<ActivityAssignment>();
		for(ActivityAssignment assignment : activityAssignments){
			 ActivityAssignment assignment1 = activityAssignmentRepository.save(assignment);
			assignments.add(assignment1);
		}
		return assignments;
	}
}
