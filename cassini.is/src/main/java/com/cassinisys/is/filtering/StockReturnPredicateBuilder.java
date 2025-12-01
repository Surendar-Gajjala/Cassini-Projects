package com.cassinisys.is.filtering;

import com.cassinisys.is.model.store.QISStockReturn;
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
public class StockReturnPredicateBuilder implements PredicateBuilder<StockReturnCriteria, QISStockReturn> {

    @Override
    public Predicate build(StockReturnCriteria criteria, QISStockReturn pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(StockReturnCriteria criteria, QISStockReturn pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split("");
            for (String s : arr) {
                Predicate predicate = pathBase.returnNumberSource.containsIgnoreCase(s).
                        or(pathBase.notes.containsIgnoreCase(s)).
                        or(pathBase.approvedBy.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */
    public Predicate getDefaultPredicate(StockReturnCriteria criteria, QISStockReturn pathBase) {
        return null;
    }
}
