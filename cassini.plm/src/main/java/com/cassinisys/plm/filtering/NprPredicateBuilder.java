package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.QPLMNpr;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NprPredicateBuilder implements PredicateBuilder<NprCriteria, QPLMNpr> {

    @Override
    public Predicate build(NprCriteria nprCriteria, QPLMNpr pathBase) {
        return getDefaultPredicate(nprCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(NprCriteria criteria, QPLMNpr pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.number.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.reasonForRequest.containsIgnoreCase(s)).
                        or(pathBase.notes.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getReasonForRequest())) {
            predicates.add(pathBase.reasonForRequest.containsIgnoreCase(criteria.getReasonForRequest()));
        }
        if (!Criteria.isEmpty(criteria.getNotes())) {
            predicates.add(pathBase.notes.containsIgnoreCase(criteria.getNotes()));
        }

        return ExpressionUtils.allOf(predicates);
    }
}