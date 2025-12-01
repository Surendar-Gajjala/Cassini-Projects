package com.cassinisys.is.filtering;

import com.cassinisys.is.model.store.QCustomRoadChalan;
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
public class CustomRoadChallanPredicateBuilder implements PredicateBuilder<CustomRoadChallanCriteria, QCustomRoadChalan> {

    public Predicate build(CustomRoadChallanCriteria criteria, QCustomRoadChalan pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */

    private Predicate getFreeTextSearchPredicate(CustomRoadChallanCriteria criteria, QCustomRoadChalan pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.chalanNumber.containsIgnoreCase(s).
                        or(pathBase.goingFrom.containsIgnoreCase(s)).
                        or(pathBase.goingTo.containsIgnoreCase(s)).
                        or(pathBase.issuingAuthority.containsIgnoreCase(s)).
                        or(pathBase.store.storeName.containsIgnoreCase(s)).
                        or(pathBase.vehicleDetails.containsIgnoreCase(s)).
                        or(pathBase.meansOfTrans.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */

    private Predicate getDefaultPredicate(CustomRoadChallanCriteria criteria, QCustomRoadChalan pathBase) {
        return null;
    }
}
