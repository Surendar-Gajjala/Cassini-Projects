package com.cassinisys.platform.service.wfm;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.wfm.ActionDefinition;
import com.cassinisys.platform.model.wfm.ActivityDefinition;
import com.cassinisys.platform.model.wfm.TaskDefinition;
import com.cassinisys.platform.model.wfm.WorkFlowDefinition;
import com.cassinisys.platform.repo.wfm.ActionDefinitionRepository;
import com.cassinisys.platform.repo.wfm.ActivityDefinitionRepository;
import com.cassinisys.platform.repo.wfm.TaskDefinitionRepository;
import com.cassinisys.platform.repo.wfm.WorkFlowDefinitionRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;


@Service
public class WorkFlowDefinitionService implements CrudService<WorkFlowDefinition, Integer>,
		PageableService<WorkFlowDefinition, Integer> {

	@Autowired
	private WorkFlowDefinitionRepository workFlowDefRepository;

	@Autowired
	private ActivityDefinitionRepository activityDefRepository;
	
	@Autowired
	private ActionDefinitionRepository actionDefRepository;

	@Autowired
	private TaskDefinitionRepository taskDefRepository;


	@Override
	@Transactional
	public WorkFlowDefinition create(WorkFlowDefinition workFlowDef) {
		checkNotNull(workFlowDef);
		workFlowDef = save(workFlowDef);
		return workFlowDef;
	}

	@Override
	@Transactional
	public WorkFlowDefinition update(WorkFlowDefinition workFlowDef) {
		checkNotNull(workFlowDef);
		checkNotNull(workFlowDef.getId());
		workFlowDef = save(workFlowDef);
		return workFlowDef;
	}

	private WorkFlowDefinition save(WorkFlowDefinition workflow) {
		WorkFlowDefinition savedWorkflow = workFlowDefRepository.save(workflow);
		final Integer workflowId = savedWorkflow.getId();

		List<ActivityDefinition> newActivities = workflow.getActivities();
		Map<Integer, ActivityDefinition> map = new HashMap<>();
		newActivities.forEach(a -> {
			a.setWorkflow(workflowId);
			if(a.getId() != null) {
				map.put(a.getId(), a);
			}
		});

		List<ActivityDefinition> currentActivities = getActivities(workflow.getId());
		currentActivities.forEach(a -> {
			if(map.get(a.getId()) == null) {
				activityDefRepository.delete(a.getId());
			}
		});

		Map<String, ActivityDefinition> mapDiagramIds = new HashMap<>();
		newActivities.forEach(a -> {
			if(a.getDiagramId() != null) {
				mapDiagramIds.put(a.getDiagramId(), a);
			}
		});

		List<ActivityDefinition> savedActivities = activityDefRepository.save(newActivities);
		savedActivities.forEach(a -> {
			ActivityDefinition activity = mapDiagramIds.get(a.getDiagramId());
			if(activity != null) {
				activity.setId(a.getId());
			}
		});

		newActivities.forEach(a -> saveActivity(workflow, a));

		workflow.setId(savedWorkflow.getId());
		return workflow;
	}

	private void saveActivity(WorkFlowDefinition workflow, ActivityDefinition activity) {
		final Integer activityId = activity.getId();

		List<ActivityDefinition> activities = workflow.getActivities();
		Map<String, ActivityDefinition> mapActivityDiagramIds = new HashMap<>();
		activities.forEach(a -> {
			if(a.getDiagramId() != null) {
				mapActivityDiagramIds.put(a.getDiagramId(), a);
			}
		});

		List<ActionDefinition> actions = activity.getActions();
		Map<Integer, ActionDefinition> map = new HashMap<>();
		actions.forEach(a -> {
			ActivityDefinition sourceRef = a.getSourceRef();
			ActivityDefinition source = mapActivityDiagramIds.get(sourceRef.getDiagramId());
			if(source != null) {
				sourceRef.setId(source.getId());
				a.setSource(source.getId());
			}

			ActivityDefinition targetRef = a.getTargetRef();
			ActivityDefinition target = mapActivityDiagramIds.get(targetRef.getDiagramId());
			if(target != null) {
				targetRef.setId(target.getId());
				a.setTarget(target.getId());
			}
			map.put(a.getId(), a);
		});

		List<ActionDefinition> currentActions = actionDefRepository.findBySource(activityId);
		currentActions.forEach(a -> {
			if(map.get(a.getId()) == null) {
				actionDefRepository.delete(a.getId());
			}
		});

		Map<String, ActionDefinition> mapDiagramIds = new HashMap<>();
		actions.forEach(a -> {
			if(a.getDiagramId() != null) {
				mapDiagramIds.put(a.getDiagramId(), a);
			}
		});


		List<ActionDefinition> savedActions = actionDefRepository.save(actions);
		savedActions.forEach(a -> {
			ActionDefinition action = mapDiagramIds.get(a.getDiagramId());
			if(action != null) {
				action.setId(a.getId());
			}
		});

		List<TaskDefinition> tasks = activity.getTasks();
		Map<Integer, TaskDefinition> mapTasks = new HashMap<>();
		tasks.forEach(t -> {
            t.setActivity(activity.getId());
			if(t.getId() != null) {
				mapTasks.put(t.getId(), t);
			}
		});

		List<TaskDefinition> currentTasks = taskDefRepository.findByActivity(activity.getId());
		currentTasks.forEach(t -> {
			TaskDefinition found = mapTasks.get(t.getId());
			if(found == null) {
				taskDefRepository.delete(t);
			}
		});

		List<TaskDefinition> savedTasks = taskDefRepository.save(tasks);
		activity.setTasks(savedTasks);

	}



	@Override
	@Transactional
	public void delete(Integer id) {
		checkNotNull(id);
		WorkFlowDefinition workFlowDef = workFlowDefRepository.findOne(id);
		workFlowDefRepository.delete(id);
	}

	@Override
	@Transactional(readOnly = true)
	public WorkFlowDefinition get(Integer id) {
		checkNotNull(id);
		WorkFlowDefinition workFlowDef = workFlowDefRepository.findOne(id);
		if (workFlowDef == null) {
			throw new ResourceNotFoundException();
		}

		List<ActivityDefinition> activities = activityDefRepository.findByWorkflow(id);
		activities.forEach(a -> {
			List<ActionDefinition> actions = actionDefRepository.findBySource(a.getId());
			a.setActions(actions);

            List<TaskDefinition> tasks = taskDefRepository.findByActivity(a.getId());
            a.setTasks(tasks != null ? tasks : new ArrayList<>());
		});
		workFlowDef.setActivities(activities);
		return workFlowDef;
	}

	@Override
	@Transactional(readOnly = true)
	public List<WorkFlowDefinition> getAll() {
		return workFlowDefRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<WorkFlowDefinition> findAll(Pageable pageable) {
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order(Direction.DESC,
							"name")));
		}
		return workFlowDefRepository.findAll(pageable);
	}


	@Transactional
	public ActivityDefinition createActivity(ActivityDefinition activityDef) {
		checkNotNull(activityDef);
		activityDef.setId(null);
        
		return activityDefRepository.save(activityDef);
	}


	@Transactional
	public ActivityDefinition updateActivity(ActivityDefinition activityDef) {
		checkNotNull(activityDef);
		checkNotNull(activityDef.getId());
		return activityDefRepository.save(activityDef);
	}


	@Transactional
	public void deleteActivity(Integer id) {
		checkNotNull(id);
		activityDefRepository.delete(id);
	}

	@Transactional(readOnly = true)
	public ActivityDefinition getActivity(Integer id) {
		checkNotNull(id);
		ActivityDefinition activityDef = activityDefRepository.findOne(id);
		if (activityDef == null) {
			throw new ResourceNotFoundException();
		}
		return activityDef;
	}

	@Transactional(readOnly = true)
	public List<ActivityDefinition> getActivities(Integer id) {
		checkNotNull(id);
		
		return activityDefRepository.findByWorkflow(id);
	}

	@Transactional(readOnly = true)
	public List<ActivityDefinition> getAllActivity() {
		return activityDefRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Page<ActivityDefinition> findAllActivity(Pageable pageable) {
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order(Direction.DESC,
							"name")));
		}
		return activityDefRepository.findAll(pageable);
	}



	@Transactional
	public ActionDefinition createAction(ActionDefinition activityDef) {
		checkNotNull(activityDef);
		activityDef.setId(null);
        
		return actionDefRepository.save(activityDef);
	}


	@Transactional
	public ActionDefinition updateAction(ActionDefinition activityDef) {
		checkNotNull(activityDef);
		checkNotNull(activityDef.getId());
		return actionDefRepository.save(activityDef);
	}

	@Transactional
	public void deleteAction(Integer id) {
		checkNotNull(id);
		actionDefRepository.delete(id);
	}

	@Transactional
	public ActionDefinition getAction(Integer id) {
		checkNotNull(id);
		ActionDefinition activityDef = actionDefRepository.findOne(id);
		if (activityDef == null) {
			throw new ResourceNotFoundException();
		}
		return activityDef;
	}

	@Transactional
	public List<ActionDefinition> getActions(Integer actvId) {
		return actionDefRepository.findBySource(actvId);
	}

	@Transactional
	public List<ActionDefinition> getAllActions() {
		return actionDefRepository.findAll();
	}

	@Transactional
	public Page<ActionDefinition> findAllActions(Pageable pageable) {
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order(Direction.DESC,
							"name")));
		}
		return actionDefRepository.findAll(pageable);
	}

	@Transactional
	public TaskDefinition createTask(TaskDefinition task) {
		checkNotNull(task);
		task.setId(null);

		return taskDefRepository.save(task);
	}


	@Transactional
	public TaskDefinition updateTask(TaskDefinition task) {
		checkNotNull(task);
		checkNotNull(task.getId());
		return taskDefRepository.save(task);
	}


	@Transactional
	public void deleteTask(Integer id) {
		checkNotNull(id);
		taskDefRepository.delete(id);
	}

	@Transactional
	public TaskDefinition getTask(Integer id) {
		checkNotNull(id);
		TaskDefinition task = taskDefRepository.findOne(id);
		if (task == null) {
			throw new ResourceNotFoundException();
		}
		return task;
	}

	@Transactional
	public List<TaskDefinition> getTasks() {
		return taskDefRepository.findAll();
	}

	@Transactional
	public List<TaskDefinition> getTasksByActivity(Integer actvId) {
		return taskDefRepository.findByActivity(actvId);
	}

	@Transactional
	public Page<TaskDefinition> findTasks(Pageable pageable) {
		if (pageable.getSort() == null) {
			pageable = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), new Sort(new Order(Direction.ASC,
					"name")));
		}
		return taskDefRepository.findAll(pageable);
	}
	
}
