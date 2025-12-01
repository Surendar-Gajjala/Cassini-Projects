package com.cassinisys.is.filtering;

import com.cassinisys.is.model.procm.QISWorkOrder;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapna on 29/01/19.
 */
@Component
public class WorkOrderPredicateBuilder implements PredicateBuilder<WorkOrderCriteria, QISWorkOrder> {
    /**
     * The method used to build Predicate
     */

    public Predicate build(WorkOrderCriteria criteria, QISWorkOrder pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */

    public Predicate getDefaultPredicate(WorkOrderCriteria criteria, QISWorkOrder pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getProject())) {
            predicates.add(pathBase.project.eq(criteria.getProject()));
        }
        if (!Criteria.isEmpty(criteria.getStatus())) {
            predicates.add(pathBase.status.eq(criteria.getStatus()));
        }
        if (!Criteria.isEmpty(criteria.getContractor())) {
            predicates.add(pathBase.contractor.eq(criteria.getContractor()));
        }
        return ExpressionUtils.allOf(predicates);

    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */

    private Predicate getFreeTextSearchPredicate(WorkOrderCriteria criteria, QISWorkOrder pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.isFreeTextSearch() != false) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.number.containsIgnoreCase(s);
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);

    }

}