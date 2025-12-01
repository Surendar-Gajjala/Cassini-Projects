package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.repo.wf.*;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSR.
 */
@Component
public class WorkflowStatusAssignmentPredicateBuilder implements PredicateBuilder<WorkflowCriteria, QPLMWorkFlowStatusAssignment> {

	@Autowired
	private WorkflowTypeRepository workflowTypeRepository;

	@Autowired
	private PLMWorkflowRepository plmWorkflowRepository;

	@Autowired
	private PLMWorkflowDefinitionRepository plmWorkflowDefinitionRepository;

	@Autowired
	private PLMWorkFlowStatusAssignmentRepository plmWorkFlowStatusAssignmentRepository;

	@Autowired
	private PLMWorkFlowStatusApproverRepository plmWorkFlowStatusApproverRepository;

	@Autowired
	private PLMWorkFlowStatusObserverRepository plmWorkFlowStatusObserverRepository;

	@Autowired
	private PLMWorkFlowStatusAcknowledgerRepository plmWorkFlowStatusAcknowledgerRepository;

	@Override
	public Predicate build(WorkflowCriteria criteria, QPLMWorkFlowStatusAssignment pathBase) {
		if (criteria.getSearchQuery() != null) {
			Predicate predicate = getFreeTextSearchPredicate(criteria, pathBase);
			return predicate;
		} else {
			return getDefaultPredicate(criteria, pathBase);
		}
	}

	private Predicate getFreeTextSearchPredicate(WorkflowCriteria criteria, QPLMWorkFlowStatusAssignment pathBase) {
		List<Predicate> predicates = new ArrayList<>();
		if (criteria.getSearchQuery() != null) {

		}
		return ExpressionUtils.allOf(predicates);
	}

	private Predicate getDefaultPredicate(WorkflowCriteria criteria, QPLMWorkFlowStatusAssignment pathBase) {
		List<Predicate> predicates = new ArrayList<>();
		if (!Criteria.isEmpty(criteria.getPersonId())) {
			List<PLMWorkFlowStatusAssignment> workFlowStatusAssignments = plmWorkFlowStatusAssignmentRepository.findByPerson(criteria.getPersonId());
			List<Integer> ids = new ArrayList<>();
			for (PLMWorkFlowStatusAssignment workFlowStatusAssignment : workFlowStatusAssignments) {
				if(workFlowStatusAssignment.getAssignmentType().equals(WorkflowAssigementType.ACKNOWLEDGER)){
					PLMWorkFlowStatusAcknowledger workFlowStatusAcknowledger = plmWorkFlowStatusAcknowledgerRepository.findOne(workFlowStatusAssignment.getId());
					if(!workFlowStatusAcknowledger.isAcknowledged()) ids.add(workFlowStatusAssignment.getId());
				}
				if(workFlowStatusAssignment.getAssignmentType().equals(WorkflowAssigementType.APPROVER)){
					PLMWorkFlowStatusApprover workFlowStatusApprover = plmWorkFlowStatusApproverRepository.findOne(workFlowStatusAssignment.getId());
					if(workFlowStatusApprover.getVote() == null) ids.add(workFlowStatusAssignment.getId());
				}
				if(workFlowStatusAssignment.getAssignmentType().equals(WorkflowAssigementType.OBSERVER)){
					PLMWorkFlowStatusObserver workFlowStatusObserver = plmWorkFlowStatusObserverRepository.findOne(workFlowStatusAssignment.getId());
					if(workFlowStatusObserver.getComments() == null) ids.add(workFlowStatusAssignment.getId());
				}
			}
			predicates.add(pathBase.id.in(ids));
		}
		return ExpressionUtils.allOf(predicates);
	}
}
