package com.cassinisys.tm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.tm.model.QTMDepartmentPerson;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 03-08-2016.
 */

@Component
public class DepartmentPersonPredicateBuilder implements PredicateBuilder<DepartmentPersonCriteria, QTMDepartmentPerson> {

    @Override
    public Predicate build(DepartmentPersonCriteria criteria, QTMDepartmentPerson pathBase) {
        if (criteria.isFreeTextSearch()) {
            List<Predicate> predicates = new ArrayList<>();
            if (criteria.getSearchQuery() != null) {
                Predicate predicate1 = getFreeTextSearchPredicate(criteria, pathBase);
                predicates.add(predicate1);
            }
            if (criteria.getDepartment() != null) {
                Predicate predicate2 = getDefaultPredicate(criteria, pathBase);
                predicates.add(predicate2);
            }
            return ExpressionUtils.allOf(predicates);

        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(DepartmentPersonCriteria criteria, QTMDepartmentPerson pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.department.eq(Integer.parseInt(s)).
                        or(pathBase.person.eq(Integer.parseInt(s)));
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(DepartmentPersonCriteria criteria, QTMDepartmentPerson pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getDepartment())) {
            predicates.add(pathBase.department.eq(Integer.parseInt(criteria.getDepartment())));
        }
        if (!Criteria.isEmpty(criteria.getPerson())) {
            predicates.add(pathBase.person.eq(Integer.parseInt(criteria.getPerson())));
        }
        return ExpressionUtils.allOf(predicates);
    }
}