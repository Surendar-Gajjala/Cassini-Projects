package com.cassinisys.tm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.tm.model.QTMShiftPerson;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShiftPersonPredicateBuilder implements PredicateBuilder<ShiftPersonCriteria, QTMShiftPerson> {

    @Override
    public Predicate build(ShiftPersonCriteria criteria, QTMShiftPerson pathBase) {
        if (criteria.isFreeTextSearch()) {
            List<Predicate> predicates = new ArrayList<>();
            if (criteria.getSearchQuery() != null) {
                Predicate predicate1 = getFreeTextSearchPredicate(criteria, pathBase);
                predicates.add(predicate1);
            }
            if (criteria.getShift() != null) {
                Predicate predicate2 = getDefaultPredicate(criteria, pathBase);
                predicates.add(predicate2);
            }
            return ExpressionUtils.allOf(predicates);

        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(ShiftPersonCriteria criteria, QTMShiftPerson pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.person.eq(Integer.parseInt(s)).
                        or(pathBase.shift.eq(Integer.parseInt(s)));
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(ShiftPersonCriteria criteria, QTMShiftPerson pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getPerson())) {
            predicates.add(pathBase.person.eq(Integer.parseInt(criteria.getPerson())));
        }
        if (!Criteria.isEmpty(criteria.getShift())) {
            predicates.add(pathBase.shift.eq(Integer.parseInt(criteria.getShift())));
        }
        return ExpressionUtils.allOf(predicates);
    }
}