package com.cassinisys.is.filtering;

import com.cassinisys.is.model.pm.QISProjectSite;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anusha on 07-06-2017.
 */

@Component
public class SitePredicateBuilder implements PredicateBuilder<SiteCriteria, QISProjectSite> {

    /**
     * The method used to build Predicate
     */
    @Override
    public Predicate build(SiteCriteria criteria, QISProjectSite pathBase) {
        if (criteria.getFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }

    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */
    private Predicate getFreeTextSearchPredicate(SiteCriteria criteria, QISProjectSite pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */
    public Predicate getDefaultPredicate(SiteCriteria criteria, QISProjectSite pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.equalsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.equalsIgnoreCase(criteria.getDescription()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
