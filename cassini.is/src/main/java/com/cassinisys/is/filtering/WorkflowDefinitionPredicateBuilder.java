package com.cassinisys.is.filtering;

import com.cassinisys.is.model.workflow.QISWorkflowDefinition;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSR on 011 01-03 -2020.
 */
@Component
public class WorkflowDefinitionPredicateBuilder implements PredicateBuilder<WorkflowDefinitionCriteria, QISWorkflowDefinition> {

    @Override
    public Predicate build(WorkflowDefinitionCriteria criteria, QISWorkflowDefinition pathBase) {
        if (criteria.getSearchQuery() != null) {
            Predicate predicate = getFreeTextSearchPredicate(criteria, pathBase);
            return predicate;
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(WorkflowDefinitionCriteria criteria, QISWorkflowDefinition pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(WorkflowDefinitionCriteria criteria, QISWorkflowDefinition pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
