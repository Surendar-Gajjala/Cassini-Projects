package com.cassinisys.tm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.tm.model.QTMShift;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajabrahmachary on 21-07-2016.
 */
@Component
public class ShiftPredicateBuilder implements PredicateBuilder<ShiftCriteria, QTMShift> {

    @Override
    public Predicate build(ShiftCriteria criteria, QTMShift pathBase) {
        if (criteria.isFreeTextSearch()) {
            List<Predicate> predicates = new ArrayList<>();
            if (criteria.getSearchQuery() != null) {
                Predicate predicate1 = getFreeTextSearchPredicate(criteria, pathBase);
                predicates.add(predicate1);
            }
            if (criteria.getName() != null) {
                Predicate predicate2 = getDefaultPredicate(criteria, pathBase);
                predicates.add(predicate2);
            }
            return ExpressionUtils.allOf(predicates);

        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(ShiftCriteria criteria, QTMShift pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s);
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(ShiftCriteria criteria, QTMShift pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.equalsIgnoreCase(criteria.getName()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
