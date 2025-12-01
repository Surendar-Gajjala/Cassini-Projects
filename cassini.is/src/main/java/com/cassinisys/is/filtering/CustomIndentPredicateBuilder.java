package com.cassinisys.is.filtering;

import com.cassinisys.is.model.store.IndentStatus;
import com.cassinisys.is.model.store.QCustomIndent;
import com.cassinisys.platform.filtering.Criteria;
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
public class CustomIndentPredicateBuilder implements PredicateBuilder<CustomIndentCriteria, QCustomIndent> {

    public Predicate build(CustomIndentCriteria criteria, QCustomIndent pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */

    private Predicate getFreeTextSearchPredicate(CustomIndentCriteria criteria, QCustomIndent pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.indentNumber.containsIgnoreCase(s).
                        or(pathBase.notes.containsIgnoreCase(s)).
                        or(pathBase.project.name.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        if (criteria.getStore() != null) {
            predicates.add(pathBase.store.eq(criteria.getStore()));
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */

    private Predicate getDefaultPredicate(CustomIndentCriteria criteria, QCustomIndent pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getStatus())) {
            List<Predicate> p = new ArrayList<>();
            String[] arr = criteria.getStatus().split(",");
            for (String s : arr) {
                Predicate predicate = pathBase.status.eq(IndentStatus.valueOf(s));
                p.add(predicate);
            }
            Predicate p1 = ExpressionUtils.anyOf(p);
            predicates.add(p1);
        }
        if (!Criteria.isEmpty(criteria.getIndentNumber())) {
            predicates.add(pathBase.indentNumber.equalsIgnoreCase(criteria.getIndentNumber()));
        }
        if (!Criteria.isEmpty(criteria.getNotes())) {
            predicates.add(pathBase.notes.equalsIgnoreCase(criteria.getNotes()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
