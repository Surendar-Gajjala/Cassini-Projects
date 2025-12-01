package com.cassinisys.is.filtering;

import com.cassinisys.is.model.store.QCustomPurchaseOrder;
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
public class CustomPurchaseOrderPredicateBuilder implements PredicateBuilder<CustomPurchaseOrderCriteria, QCustomPurchaseOrder> {

    @Override
    public Predicate build(CustomPurchaseOrderCriteria criteria, QCustomPurchaseOrder pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(CustomPurchaseOrderCriteria criteria, QCustomPurchaseOrder pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split("");
            for (String s : arr) {
                Predicate predicate = pathBase.poNumber.containsIgnoreCase(s).
                        or(pathBase.supplier.containsIgnoreCase(s)).
                        or(pathBase.approvedBy.containsIgnoreCase(s).
                                or(pathBase.notes.containsIgnoreCase(s)));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */
    public Predicate getDefaultPredicate(CustomPurchaseOrderCriteria criteria, QCustomPurchaseOrder pathBase) {
        return null;
    }
}
