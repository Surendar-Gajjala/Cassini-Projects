package com.cassinisys.is.filtering;

import com.cassinisys.is.model.pm.QISProject;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 20-09-2018.
 */

@Component
public class ProjectPredicateBuilder implements PredicateBuilder<ProjectCriteria, QISProject> {

    @Override
    public Predicate build(ProjectCriteria criteria, QISProject pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(ProjectCriteria criteria, QISProject pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split("");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s);
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */
    public Predicate getDefaultPredicate(ProjectCriteria criteria, QISProject pathBase) {
        return null;
    }
}
