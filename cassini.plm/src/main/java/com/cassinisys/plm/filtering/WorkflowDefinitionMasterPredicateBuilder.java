package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.QPLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.QPLMWorkflowDefinitionMaster;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyamreddy on 011 11-Sep -17.
 */
@Component
public class WorkflowDefinitionMasterPredicateBuilder implements PredicateBuilder<WorkflowDefinitionCriteria, QPLMWorkflowDefinitionMaster> {

	@Autowired
	private WorkflowTypeRepository workflowTypeRepository;

	@Autowired
	private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;

	@Autowired
	private WorkflowDefinitionPredicateBuilder workflowDefinitionPredicateBuilder;

	@Override
	public Predicate build(WorkflowDefinitionCriteria criteria, QPLMWorkflowDefinitionMaster pathBase) {
		Predicate predicate = getFreeTextSearchPredicate(criteria, pathBase);
		return predicate;

	}

	private Predicate getFreeTextSearchPredicate(WorkflowDefinitionCriteria criteria, QPLMWorkflowDefinitionMaster pathBase) {
		List<Predicate> predicates = new ArrayList<>();
		List<Integer> integers = getDefIds(criteria);
		if (!Criteria.isEmpty(criteria.getSearchQuery())) {
			String[] arr = criteria.getSearchQuery().split(" ");
			for (String s : arr) {
				Predicate predicate = pathBase.number.containsIgnoreCase(s).or(pathBase.id.in(integers));
				predicates.add(predicate);
			}
		}
		return ExpressionUtils.allOf(predicates);
	}

	private List<Integer> getDefIds(WorkflowDefinitionCriteria criteria) {
		List<Integer> mfrIds = new ArrayList<>();
		WorkflowDefinitionCriteria workflowDefinitionCriteria = new WorkflowDefinitionCriteria();
		workflowDefinitionCriteria.setSearchQuery(criteria.getSearchQuery());
		Pageable pageable = new PageRequest(0, 1000,
				new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
		Predicate predicate = workflowDefinitionPredicateBuilder.build(workflowDefinitionCriteria, QPLMWorkflowDefinition.pLMWorkflowDefinition);
		Page<PLMWorkflowDefinition> workflowDefinitions = workFlowDefinitionRepository.findAll(predicate, pageable);

		for (PLMWorkflowDefinition item : workflowDefinitions.getContent()) {
			mfrIds.add(item.getMaster().getId());
		}

		return mfrIds;
	}
}
