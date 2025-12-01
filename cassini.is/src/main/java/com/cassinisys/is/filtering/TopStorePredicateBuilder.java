package com.cassinisys.is.filtering;
/**
 * The class is for StorePredicateBuilder
 */

import com.cassinisys.is.model.store.QISTopStore;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TopStorePredicateBuilder implements PredicateBuilder<TopStoreCriteria, QISTopStore> {
    /**
     * The method used to build Predicate
     */

    @Override
    public Predicate build(TopStoreCriteria criteria, QISTopStore pathBase) {
        if (criteria.getFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */

    public Predicate getDefaultPredicate(TopStoreCriteria criteria, QISTopStore pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getStoreName())) {
            predicates.add(pathBase.storeName.containsIgnoreCase(criteria.getStoreName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getLocationName())) {
            predicates.add(pathBase.locationName.containsIgnoreCase(criteria.getLocationName()));
        }
        if (!Criteria.isEmpty(criteria.getPerson())) {
            predicates.add(pathBase.createdBy.eq(criteria.getPerson()));
        }
        return ExpressionUtils.allOf(predicates);

    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */

    private Predicate getFreeTextSearchPredicate(TopStoreCriteria criteria, QISTopStore pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getFreeTextSearch() != false) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.storeName.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.locationName.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        if (criteria.getPerson() != null) {
            predicates.add(pathBase.createdBy.eq(criteria.getPerson()));
        }
        return ExpressionUtils.allOf(predicates);

    }

}
