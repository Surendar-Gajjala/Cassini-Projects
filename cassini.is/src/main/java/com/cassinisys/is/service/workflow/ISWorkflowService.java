package com.cassinisys.is.service.workflow;

import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.tm.ISProjectTask;
import com.cassinisys.is.model.workflow.*;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.is.repo.workflow.*;
import com.cassinisys.is.service.tm.ProjectTaskService;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@Service
public class ISWorkflowService implements CrudService<ISWorkflow, Integer> {

	@Autowired
	private ISWorkflowRepository isWorkflowRepository;
	@Autowired
	private ISWorkflowDefinitionRepository isWorkflowDefinitionRepository;
	@Autowired
	private ISWorkflowStatusService isWorkflowStatusService;
	@Autowired
	private ISWorkflowTransitionService isWorkflowTransitionService;
	@Autowired
	private ISWorkflowStartRepository isWorkflowStartRepository;
	@Autowired
	private ISWorkflowFinishRepository isWorkflowFinishRepository;

	@Autowired
	private ISWorkFlowStatusApproverRepository isWorkFlowStatusApproverRepository;

	@Autowired
	private ISWorkflowStatusHistoryRepository isWorkflowStatusHistoryRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private ObjectRepository objectRepository;

	@Autowired
	private MailService mailService;
	@Autowired
	private SessionWrapper sessionWrapper;


	@Autowired
	private ProjectTaskService projectTaskService;

	@Autowired
	private ISWorkflowStatusRepository isWorkflowStatusRepository;

	@Autowired
	private ISWorkflowTransitionRepository workflowTransitionRepository;

	@Autowired
	private ISWorkflowStatusActionHistoryRepository isWorkflowStatusActionHistoryRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Override
	@Transactional
	public ISWorkflow create(ISWorkflow plmWorkflow) {
		return plmWorkflow;
	}

	@Override
	@Transactional
	public ISWorkflow update(ISWorkflow plmWorkflow) {
		checkNotNull(plmWorkflow);
		checkNotNull(plmWorkflow.getId());
		return create(plmWorkflow);
	}

	@Override
	public void delete(Integer integer) {
	}

	@Override
	public ISWorkflow get(Integer id) {
		checkNotNull(id);
		ISWorkflow plmWorkflow = isWorkflowRepository.findOne(id);
		if (plmWorkflow == null) {
			throw new ResourceNotFoundException();
		}
		List<ISWorkflowStatus> statuses = isWorkflowStatusService.getByWorkflow(id);
		List<ISWorkflowStatus> filtered = new ArrayList<>();
		statuses.forEach(s -> {
			if (s.getType() != WorkflowStatusType.UNDEFINED) {
				filtered.add(s);
			}
		});
		plmWorkflow.setStatuses(filtered);
		plmWorkflow.setTransitions(isWorkflowTransitionService.getByWorkflow(id));
		return plmWorkflow;
	}

	@Override
	public List<ISWorkflow> getAll() {
		List<ISWorkflow> list = new ArrayList<>();
		List<ISWorkflow> workflows = isWorkflowRepository.findAll();
		workflows.forEach(w -> list.add(get(w.getId())));
		return list;
	}

	@Transactional
	public ISWorkflow attachWorkflow(Integer attachedTo, ISWorkflowDefinition workflowDefinition) {
		ISWorkflow workflow = new ISWorkflow();
		Map<Integer, ISWorkflowStatus> mapStatuses = new HashMap<>();
		workflow.setName(workflowDefinition.getName());
		workflow.setDescription(workflowDefinition.getDescription());
		workflow.setAttachedTo(attachedTo);
		workflow.setDiagram(workflowDefinition.getDiagram());
		workflow.setDiagramId(workflowDefinition.getDiagramID());
		ISWorkflow savedWorkflow = isWorkflowRepository.save(workflow);
		ISWorkflowStart start = new ISWorkflowStart();
		start.setWorkflow(savedWorkflow.getId());
		start.setName(workflowDefinition.getStart().getName());
		start.setDescription(workflowDefinition.getStart().getDescription());
		start.setDiagramId(workflowDefinition.getStart().getDiagramId());
		start.setType(workflowDefinition.getStart().getType());
		start = isWorkflowStartRepository.save(start);
		savedWorkflow.setStart(start);
		mapStatuses.put(workflowDefinition.getStart().getId(), start);
		ISWorkflowFinish finish = new ISWorkflowFinish();
		finish.setWorkflow(savedWorkflow.getId());
		finish.setName(workflowDefinition.getFinish().getName());
		finish.setDescription(workflowDefinition.getFinish().getDescription());
		finish.setDiagramId(workflowDefinition.getFinish().getDiagramId());
		finish.setType(workflowDefinition.getFinish().getType());
		finish = isWorkflowFinishRepository.save(finish);
		savedWorkflow.setFinish(finish);
		mapStatuses.put(workflowDefinition.getFinish().getId(), finish);
		List<ISWorkflowDefinitionStatus> definitionStatuses = workflowDefinition.getStatuses();
		definitionStatuses.forEach(s -> {
			ISWorkflowStatus status = new ISWorkflowStatus();
			status.setWorkflow(savedWorkflow.getId());
			status.setName(s.getName());
			status.setDescription(s.getDescription());
			status.setDiagramId(s.getDiagramId());
			status.setType(s.getType());
			status = isWorkflowStatusService.create(status);
			savedWorkflow.getStatuses().add(status);
			mapStatuses.put(s.getId(), status);
		});
		List<ISWorkflowDefinitionTransition> definitionTransitions = workflowDefinition.getTransitions();
		definitionTransitions.forEach(t -> {
			ISWorkflowTransition transition = new ISWorkflowTransition();
			transition.setWorkflow(savedWorkflow.getId());
			transition.setDiagramId(t.getDiagramId());
			transition.setFromStatus(mapStatuses.get(t.getFromStatus()).getId());
			transition.setToStatus(mapStatuses.get(t.getToStatus()).getId());
			transition = isWorkflowTransitionService.create(transition);
			savedWorkflow.getTransitions().add(transition);

		});
		savedWorkflow.setCurrentStatus(start.getId());
		return isWorkflowRepository.save(savedWorkflow);
	}

	public ISWorkflow getAttachedWorkflow(Integer id) {
		return isWorkflowRepository.findByAttachedTo(id);
	}

	@Transactional
	public ISWorkflow startWorkflow(Integer wfId) {
		ISWorkflow workflow = isWorkflowRepository.findOne(wfId);
		if (workflow != null) {
			List<ISWorkflowTransition> list = isWorkflowTransitionService.getByFromStatus(workflow.getStart().getId());
			if (list.size() == 1) {
				Integer loginId = sessionWrapper.getSession().getLogin().getPerson().getId();
				workflow.setStarted(Boolean.TRUE);
				workflow.setStartedOn(new Date());
				workflow.getStart().setFlag(WorkflowStatusFlag.COMPLETED);
				workflow.getStart().setCreatedBy(loginId);
				workflow.getStart().setModifiedBy(loginId);
				isWorkflowStartRepository.save(workflow.getStart());
				ISWorkflowStatus status = isWorkflowStatusService.get(list.get(0).getToStatus());
				status.setFlag(WorkflowStatusFlag.INPROCESS);
				status.setCreatedBy(loginId);
				status.setModifiedBy(loginId);
				isWorkflowStatusService.update(status);
				workflow.setCurrentStatus(status.getId());

				/*notifyUsers(workflow, status);*/
			}
			workflow = isWorkflowRepository.save(workflow);
		}
		return workflow;
	}

	public ISWorkflowStatus getWorkflowStatus(Integer id) {
		return isWorkflowStatusService.get(id);
	}

	@Transactional
	public Boolean promoteWorkflow(Integer fromStatusId, Integer toStatusId) {
		ISWorkflowStatus fromStatus = isWorkflowStatusService.get(fromStatusId);
		ISWorkflowStatus toStatus = isWorkflowStatusService.get(toStatusId);
		ISWorkflow workflow = isWorkflowRepository.findOne(fromStatus.getWorkflow());
		Integer loginId = sessionWrapper.getSession().getLogin().getPerson().getId();
		fromStatus.setFlag(WorkflowStatusFlag.COMPLETED);
		fromStatus.setCreatedBy(loginId);
		isWorkflowStatusService.update(fromStatus);
		if (fromStatus.getType() == WorkflowStatusType.RELEASED) {
			releaseAttachedObject(workflow);
		}
		toStatus.setFlag(WorkflowStatusFlag.INPROCESS);
		toStatus.setCreatedBy(loginId);
		isWorkflowStatusService.update(toStatus);
		workflow.setCurrentStatus(toStatusId);
		workflow = isWorkflowRepository.save(workflow);
		ISWorkflowStatusHistory history = new ISWorkflowStatusHistory();
		history.setWorkflow(workflow.getId());
		history.setStatus(fromStatusId);
		history.setTimestamp(new Date());
		isWorkflowStatusHistoryRepository.save(history);
		notifyUsers(workflow, toStatus);
		return true;
	}

	private void releaseAttachedObject(ISWorkflow workflow) {
		CassiniObject object = objectRepository.findOne(workflow.getAttachedTo());
		if (object != null && object.getObjectType().toString().equalsIgnoreCase("TASK")) {
			ISProjectTask projectTask = projectTaskService.get(workflow.getAttachedTo());
			if (projectTask != null) {
			}
		}
	}

	@Transactional
	public ISWorkflow finishWorkflow(Integer wfId) {
		ISWorkflow workflow = isWorkflowRepository.findOne(wfId);
		if (workflow != null) {
			List<ISWorkflowTransition> list = workflowTransitionRepository.findByFromStatusAndToStatus(workflow.getCurrentStatus(), workflow.getFinish().getId());
			if (list.size() == 1) {
				Integer loginId = sessionWrapper.getSession().getLogin().getPerson().getId();
				workflow.setFinished(Boolean.TRUE);
				workflow.setFinishedOn(new Date());
				workflow.getFinish().setFlag(WorkflowStatusFlag.COMPLETED);
				workflow.getFinish().setCreatedBy(loginId);
				isWorkflowFinishRepository.save(workflow.getFinish());
				ISWorkflowStatus status = isWorkflowStatusService.get(list.get(0).getFromStatus());
				status.setFlag(WorkflowStatusFlag.COMPLETED);
				status.setCreatedBy(loginId);
				status.setModifiedBy(loginId);
				isWorkflowStatusService.update(status);
				if (status.getType() == WorkflowStatusType.RELEASED) {
					releaseAttachedObject(workflow);
				}
				ISWorkflowStatusHistory history = new ISWorkflowStatusHistory();
				history.setWorkflow(workflow.getId());
				history.setStatus(status.getId());
				history.setTimestamp(new Date());
				isWorkflowStatusHistoryRepository.save(history);
				workflow = isWorkflowRepository.save(workflow);
			}
		}
		return workflow;
	}

	public List<ISWorkFlowStatusApprover> getApprovers(Integer statusId) {
		return isWorkFlowStatusApproverRepository.findByStatus(statusId);
	}

	@Transactional
	public List<ISWorkFlowStatusApprover> addApprovers(Integer statusId, List<ISWorkFlowStatusApprover> approvers) {
		return isWorkFlowStatusApproverRepository.save(approvers);
	}

	public List<ISWorkflowStatusHistory> getWorkflowHistory(Integer wfId) {
		List<ISWorkflowStatusHistory> history = isWorkflowStatusHistoryRepository.findByWorkflowOrderByTimestampDesc(wfId);
		history.forEach(h -> {
			h.setStatusObject(isWorkflowStatusService.get(h.getStatus()));
			List<ISWorkflowStatusActionHistory> actionHistory =
					isWorkflowStatusActionHistoryRepository.findByWorkflowAndStatusOrderByTimestampDesc(wfId, h.getStatus());
			h.setStatusActionHistory(actionHistory);
		});
		return history;
	}

	@Transactional
	public void notifyUsers(ISWorkflow workflow, ISWorkflowStatus status) {
		try {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = attr.getRequest();
			StringBuffer url = request.getRequestURL();
			String uri = request.getRequestURI();
			String host = url.substring(0, url.indexOf(uri));
			Map<String, Object> model = new HashMap<>();
			model.put("host", host);
			CassiniObject object = objectRepository.findOne(workflow.getAttachedTo());
			if (object != null && object.getObjectType().toString().equalsIgnoreCase("TASK")) {
				ISProjectTask task = projectTaskService.get(workflow.getAttachedTo());
				if (task != null) {
					model.put("task", task);
				}
				ISProject project = projectRepository.findOne(task.getProject());
				if (project != null) {
					model.put("project", project);
				}

			}
			List<ISWorkflowStatusHistory> histories = getWorkflowHistory(workflow.getId());
			histories.forEach(h -> {
				h.setStatusObject(isWorkflowStatusService.get(h.getStatus()));
			});
			if (workflow.getStarted()) {
				ISWorkflowStatusHistory start = new ISWorkflowStatusHistory();
				ISWorkflowStatus s = new ISWorkflowStatus();
				s.setName("Workflow Started");
				start.setStatusObject(s);
				start.setTimestamp(workflow.getStartedOn());
				histories.add(start);
			}
			if (workflow.getFinished()) {
				ISWorkflowStatusHistory finish = new ISWorkflowStatusHistory();
				ISWorkflowStatus s = new ISWorkflowStatus();
				s.setName("Workflow Finished");
				finish.setStatusObject(s);
				finish.setTimestamp(workflow.getFinishedOn());
				histories.add(0, finish);
			}
			model.put("histories", histories);
			List<ISWorkFlowStatusApprover> approvers = isWorkFlowStatusApproverRepository.findByStatus(status.getId());
			approvers.forEach(approver -> {
				Person person = personRepository.findOne(approver.getPerson());
				if (person != null && person.getEmail() != null && !person.getEmail().trim().isEmpty()) {
					Mail mail = new Mail();
					mail.setMailTo(person.getEmail());
					mail.setMailSubject("Cassini.IS Workflow Notification");
					mail.setTemplatePath("email/workflow/workflowApprover.html");
					mail.setModel(model);
					mailService.sendEmail(mail);
				}
			});
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

}
