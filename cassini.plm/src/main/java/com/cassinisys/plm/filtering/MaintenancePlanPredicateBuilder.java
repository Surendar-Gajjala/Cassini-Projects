package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.mro.QMROMaintenancePlan;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suresh Cassini on 18-11-2020.
 */
@Component
public class MaintenancePlanPredicateBuilder implements PredicateBuilder<MaintenancePlanCriteria, QMROMaintenancePlan> {
    @Override
    public Predicate build(MaintenancePlanCriteria sparePartCriteria, QMROMaintenancePlan pathBase) {
        return getDefaultPredicate(sparePartCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(MaintenancePlanCriteria criteria, QMROMaintenancePlan pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.number.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }


        return ExpressionUtils.allOf(predicates);
    }
}