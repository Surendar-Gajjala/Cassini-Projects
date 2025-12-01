package com.cassinisys.is.filtering;

import com.cassinisys.is.model.store.QISLoan;
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
public class ISLoanPredicateBuilder implements PredicateBuilder<ISLoanCriteria, QISLoan> {

    @Override
    public Predicate build(ISLoanCriteria criteria, QISLoan pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(ISLoanCriteria criteria, QISLoan pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split("");
            for (String s : arr) {
                Predicate predicate = pathBase.loanNumber.containsIgnoreCase(s);
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */
    public Predicate getDefaultPredicate(ISLoanCriteria criteria, QISLoan pathBase) {
        return null;
    }
}
