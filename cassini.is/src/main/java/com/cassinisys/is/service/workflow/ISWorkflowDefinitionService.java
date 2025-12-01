package com.cassinisys.is.service.workflow;

import com.cassinisys.is.filtering.WorkflowDefinitionCriteria;
import com.cassinisys.is.filtering.WorkflowDefinitionPredicateBuilder;
import com.cassinisys.is.model.workflow.*;
import com.cassinisys.is.repo.tm.TaskRepository;
import com.cassinisys.is.repo.workflow.ISWorkflowDefinitionFinishRepository;
import com.cassinisys.is.repo.workflow.ISWorkflowDefinitionRepository;
import com.cassinisys.is.repo.workflow.ISWorkflowDefinitionStartRepository;
import com.cassinisys.is.repo.workflow.ISWorkflowRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by GSR on 19-05-2017.
 */
@Service
public class ISWorkflowDefinitionService implements CrudService<ISWorkflowDefinition, Integer> {

	@Autowired
	private ISWorkflowDefinitionRepository isWorkflowDefinitionRepository;

	@Autowired
	private ISWorkflowDefinitionStartRepository isWorkflowDefinitionStartRepository;

	@Autowired
	private ISWorkflowDefinitionFinishRepository isWorkflowDefinitionFinishRepository;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SessionWrapper sessionWrapper;


	@Autowired
	private ISWorkflowDefinitionStatusService isWorkflowDefinitionStatusService;

	@Autowired
	private ISWorkflowDefinitionTransitionService isWorkflowDefinitionTransitionService;

	@Autowired
	private ISWorkflowRepository isWorkflowRepository;

	@Autowired
	private ISWorkflowStatusService isWorkflowStatusService;

	@Autowired
	private ISWorkflowTransitionService isWorkflowTransitionService;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private WorkflowDefinitionPredicateBuilder predicateBuilder;

	@Override
	@Transactional
	public ISWorkflowDefinition create(ISWorkflowDefinition isWorkflowDefinition) {
		checkNotNull(isWorkflowDefinition);
		if (isWorkflowDefinition.getId() == null &&
				isWorkflowDefinitionRepository.findByName(isWorkflowDefinition.getName()) != null) {
			throw new CassiniException(isWorkflowDefinition.getName() + " : " +
					messageSource.getMessage("workflow_already_exists", null, "Workflow already exists", LocaleContextHolder.getLocale()));
		}
		ISWorkflowDefinitionStart start = isWorkflowDefinition.getStart();
		ISWorkflowDefinitionFinish finish = isWorkflowDefinition.getFinish();
		if (isWorkflowDefinition.getStart().getId() == null) {
			isWorkflowDefinition.setStart(null);
			isWorkflowDefinition.setFinish(null);
		}
		ISWorkflowDefinition isWorkflowDef = isWorkflowDefinitionRepository.save(isWorkflowDefinition);
		start.setWorkflow(isWorkflowDef.getId());
		start = isWorkflowDefinitionStartRepository.save(start);
		isWorkflowDef.setStart(start);
		finish.setWorkflow(isWorkflowDef.getId());
		finish = isWorkflowDefinitionFinishRepository.save(finish);
		isWorkflowDef.setFinish(finish);
		Map<String, ISWorkflowDefinitionStatus> map = new HashMap<>();
		map.put(start.getDiagramId(), start);
		map.put(finish.getDiagramId(), finish);
		List<ISWorkflowDefinitionStatus> savedStatuses = new ArrayList<>();
		List<ISWorkflowDefinitionStatus> statuses = isWorkflowDefinition.getStatuses();
		statuses.forEach(s -> {
			s.setWorkflow(isWorkflowDef.getId());
			s = isWorkflowDefinitionStatusService.create(s);
			savedStatuses.add(s);
			map.put(s.getDiagramId(), s);
		});
		List<ISWorkflowDefinitionTransition> savedTransitions = new ArrayList<>();
		List<ISWorkflowDefinitionTransition> transitions = isWorkflowDefinition.getTransitions();
		transitions.forEach(t -> {
			t.setWorkflow(isWorkflowDef.getId());
			t = createTransition(map, t);
			savedTransitions.add(t);
		});
		isWorkflowDef.setStatuses(savedStatuses);
		isWorkflowDef.setTransitions(savedTransitions);
		return isWorkflowDef;
	}

	@Override
	public ISWorkflowDefinition update(ISWorkflowDefinition isWorkflowDefinition) {
		checkNotNull(isWorkflowDefinition);
		checkNotNull(isWorkflowDefinition.getId());
		return create(isWorkflowDefinition);
	}

	@Override
	public void delete(Integer id) {
		ISWorkflowDefinition workflowDefinition = isWorkflowDefinitionRepository.findOne(id);
		List<ISWorkflow> workflows = isWorkflowRepository.findByName(workflowDefinition.getName());
		if (workflows.size() > 0) {
			throw new CassiniException(messageSource.getMessage("workflow_already_used_in_Task's", null, "Workflow already used in Task's", LocaleContextHolder.getLocale()));
		} else {
			isWorkflowDefinitionRepository.delete(id);
		}
	}

	@Override
	public ISWorkflowDefinition get(Integer id) {
		checkNotNull(id);
		ISWorkflowDefinition isWorkflowDefinition = isWorkflowDefinitionRepository.findOne(id);
		if (isWorkflowDefinition == null) {
			throw new ResourceNotFoundException();
		}
		List<ISWorkflowDefinitionStatus> statuses = isWorkflowDefinitionStatusService.getByWorkflow(id);
		List<ISWorkflowDefinitionStatus> filtered = new ArrayList<>();
		statuses.forEach(s -> {
			if (s.getType() != WorkflowStatusType.UNDEFINED) {
				filtered.add(s);
			}
		});
		isWorkflowDefinition.setStatuses(filtered);
		isWorkflowDefinition.setTransitions(isWorkflowDefinitionTransitionService.getByWorkflow(id));
		return isWorkflowDefinition;
	}

	public ISWorkflow getWorkflowInstances(Integer id) {
		checkNotNull(id);
		ISWorkflow isWorkflow = isWorkflowRepository.findOne(id);
		if (isWorkflow == null) {
			throw new ResourceNotFoundException();
		}
		List<ISWorkflowStatus> statuses = isWorkflowStatusService.getByWorkflow(isWorkflow.getId());
		List<ISWorkflowStatus> filtered = new ArrayList<>();
		statuses.forEach(s -> {
			if (s.getType() != WorkflowStatusType.UNDEFINED) {
				filtered.add(s);
			}
		});
		isWorkflow.setStatuses(filtered);
		isWorkflow.setTransitions(isWorkflowTransitionService.getByWorkflow(isWorkflow.getId()));
		return isWorkflow;
	}

	@Override
	public List<ISWorkflowDefinition> getAll() {
		List<ISWorkflowDefinition> list = new ArrayList<>();
		List<ISWorkflowDefinition> workflows = isWorkflowDefinitionRepository.findAll();
		workflows.forEach(w -> list.add(get(w.getId())));
		return list;
	}

	public Page<ISWorkflowDefinition> getAll(Pageable pageable) {
		List<ISWorkflowDefinition> list = new ArrayList<>();
		Page<ISWorkflowDefinition> workflows = isWorkflowDefinitionRepository.findAll(pageable);
		for (ISWorkflowDefinition workflowDefinition : workflows.getContent()) {
			workflowDefinition = get(workflowDefinition.getId());
		}
		return workflows;
	}

	@Transactional
	private ISWorkflowDefinitionTransition createTransition(Map<String, ISWorkflowDefinitionStatus> map,
	                                                        ISWorkflowDefinitionTransition t) {
		String fromDiagramId = t.getFromStatusDiagramId();
		String toDiagramId = t.getToStatusDiagramId();
		if (fromDiagramId != null && toDiagramId != null) {
			ISWorkflowDefinitionStatus fromStatus = map.get(fromDiagramId);
			ISWorkflowDefinitionStatus toStatus = map.get(toDiagramId);
			if (fromStatus != null && toStatus != null) {
				t.setFromStatus(fromStatus.getId());
				t.setToStatus(toStatus.getId());
				return isWorkflowDefinitionTransitionService.create(t);
			}
		}
		return null;
	}

	public Page<ISWorkflowDefinition> freeTextSearch(Pageable pageable, WorkflowDefinitionCriteria criteria) {
		Predicate predicate = predicateBuilder.build(criteria, QISWorkflowDefinition.iSWorkflowDefinition);
		return isWorkflowDefinitionRepository.findAll(predicate, pageable);
	}

}
