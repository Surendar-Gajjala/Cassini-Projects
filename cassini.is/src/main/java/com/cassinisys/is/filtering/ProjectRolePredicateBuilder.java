package com.cassinisys.is.filtering;
/**
 * The class is for ProjectRolePredicateBuilder
 */

import com.cassinisys.is.model.pm.QISProjectRole;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

import java.util.ArrayList;
import java.util.List;

public class ProjectRolePredicateBuilder implements PredicateBuilder<ProjectRoleCriteria, QISProjectRole> {
    /**
     * The method used to build Predicate
     */
    @Override
    public Predicate build(ProjectRoleCriteria criteria, QISProjectRole pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */
    private Predicate getFreeTextSearchPredicate(ProjectRoleCriteria criteria, QISProjectRole pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split("");
            for (String s : arr) {
                Predicate predicate = pathBase.role.eq(criteria.getRole());
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */
    public Predicate getDefaultPredicate(ProjectRoleCriteria criteria, QISProjectRole pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getRole())) {
            predicates.add(pathBase.role.eq(criteria.getRole()));
        }
        return ExpressionUtils.allOf(predicates);

    }

}
