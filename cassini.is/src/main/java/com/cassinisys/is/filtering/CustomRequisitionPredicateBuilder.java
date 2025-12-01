package com.cassinisys.is.filtering;

import com.cassinisys.is.model.store.QCustomRequisition;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapna on 31/12/18.
 */
@Component
public class CustomRequisitionPredicateBuilder implements PredicateBuilder<CustomRequisitionCriteria, QCustomRequisition> {

    public Predicate build(CustomRequisitionCriteria criteria, QCustomRequisition pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */

    private Predicate getFreeTextSearchPredicate(CustomRequisitionCriteria criteria, QCustomRequisition pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.requisitionNumber.containsIgnoreCase(s).
                        or(pathBase.notes.containsIgnoreCase(s)).
                        or(pathBase.requestedBy.containsIgnoreCase(s)).
                        or(pathBase.approvedBy.containsIgnoreCase(s)).
                        or(pathBase.store.storeName.containsIgnoreCase(s)).
                        or(pathBase.project.name.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */

    private Predicate getDefaultPredicate(CustomRequisitionCriteria criteria, QCustomRequisition pathBase) {
        return null;
    }
}
