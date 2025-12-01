package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.wf.PLMWorkflowType;
import com.cassinisys.plm.model.wf.QPLMWorkflowDefinition;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyamreddy on 011 11-Sep -17.
 */
@Component
public class WorkflowDefinitionPredicateBuilder implements PredicateBuilder<WorkflowDefinitionCriteria, QPLMWorkflowDefinition> {

    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;

    @Override
    public Predicate build(WorkflowDefinitionCriteria criteria, QPLMWorkflowDefinition pathBase) {
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            Predicate predicate = getFreeTextSearchPredicate(criteria, pathBase);
            return predicate;
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(WorkflowDefinitionCriteria criteria, QPLMWorkflowDefinition pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.master.number.containsIgnoreCase(s)).
                        or(pathBase.workflowType.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }

            Predicate predicate = pathBase.master.latestRevision.eq(pathBase.id);
            predicates.add(predicate);
        }
        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(WorkflowDefinitionCriteria criteria, QPLMWorkflowDefinition pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.master.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getWorkflowType())) {
            PLMWorkflowType plmWorkflowType = workflowTypeRepository.findOne(Integer.parseInt(criteria.getWorkflowType()));
            predicates.add(pathBase.workflowType.eq(plmWorkflowType));
        }
        predicates.add(pathBase.master.latestRevision.eq(pathBase.id));
        return ExpressionUtils.allOf(predicates);
    }

    public Predicate getPredicates(WorkflowDefinitionCriteria criteria, QPLMWorkflowDefinition pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            Predicate predicate = pathBase.name.containsIgnoreCase(criteria.getSearchQuery()).
                    or(pathBase.master.number.containsIgnoreCase(criteria.getSearchQuery())).
                    or(pathBase.workflowType.name.containsIgnoreCase(criteria.getSearchQuery())).
                    or(pathBase.description.containsIgnoreCase(criteria.getSearchQuery()))
                    .and(pathBase.workflowType.id.eq(criteria.getType()));
            predicates.add(predicate);
        } else {
            predicates.add(pathBase.workflowType.id.eq(criteria.getType()));
        }
        Predicate predicate = pathBase.master.latestRevision.eq(pathBase.id);
        predicates.add(predicate);
        return ExpressionUtils.allOf(predicates);
    }
}
