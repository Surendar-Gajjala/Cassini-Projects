package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.QPLMVariance;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
@Component
public class VariancePredicateBuilder implements PredicateBuilder<VarianceCriteria, QPLMVariance> {


    @Autowired
    private PLMWorkflowService workflowService;

    @Override
    public Predicate build(VarianceCriteria criteria, QPLMVariance pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(VarianceCriteria criteria, QPLMVariance pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.varianceNumber.containsIgnoreCase(s).
                        or(pathBase.title.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        if (!Criteria.isEmpty(criteria.getVarianceNumber())) {
            predicates.add(pathBase.varianceNumber.containsIgnoreCase(criteria.getVarianceNumber()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getOriginator())) {
            predicates.add(pathBase.originator.eq(Integer.parseInt(criteria.getOriginator())));
        }

        if (!Criteria.isEmpty(criteria.getStatus())) {
            List<Integer> ids = workflowService.getWorkflowAttachedToIdsByStatusAndType(criteria.getStatus(), PLMObjectType.CHANGE);
            predicates.add(pathBase.id.in(ids));
        }

        if (!Criteria.isEmpty(criteria.getVarianceFor())) {
            predicates.add(pathBase.varianceFor.eq(criteria.getVarianceFor()));
        }
        if (!Criteria.isEmpty(criteria.getEffectivityType())) {
            predicates.add(pathBase.effectivityType.eq(criteria.getEffectivityType()));
        }

        if (!Criteria.isEmpty(criteria.getTitle())) {
            predicates.add(pathBase.title.eq(criteria.getTitle()));
        }
        if (!Criteria.isEmpty(criteria.getVarianceType())) {
            predicates.add(pathBase.varianceType.eq(criteria.getVarianceType()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
