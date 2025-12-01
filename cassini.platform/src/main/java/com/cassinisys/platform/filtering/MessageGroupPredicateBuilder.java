package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.col.QMessageGroup;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapna on 04/02/19.
 */
@Component
public class MessageGroupPredicateBuilder implements PredicateBuilder<MessageGroupCriteria, QMessageGroup> {

    public Predicate build(MessageGroupCriteria criteria, QMessageGroup pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(MessageGroupCriteria criteria, QMessageGroup pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s)).
                        and(pathBase.ctxObjectType.eq(criteria.getCtxObjectType())).
                        and(pathBase.ctxObjectId.eq(criteria.getCtxObjectId()));
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(MessageGroupCriteria criteria, QMessageGroup pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.equalsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.equalsIgnoreCase(criteria.getDescription()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
