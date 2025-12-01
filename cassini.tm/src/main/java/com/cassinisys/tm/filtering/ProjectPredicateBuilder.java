package com.cassinisys.tm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.tm.model.QTMProject;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 07-07-2016.
 */
@Component
public class ProjectPredicateBuilder implements PredicateBuilder<ProjectCriteria, QTMProject> {


    @Override
    public Predicate build(ProjectCriteria criteria, QTMProject pathBase) {
        if (criteria.isFreeTextSearch()) {
            List<Predicate> predicates = new ArrayList<>();
            if (criteria.getSearchQuery() != null) {
                Predicate predicate1 = getFreeTextSearchPredicate(criteria, pathBase);
                predicates.add(predicate1);
            }
            return ExpressionUtils.allOf(predicates);

        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(ProjectCriteria criteria, QTMProject pathBase) {
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

    private Predicate getDefaultPredicate(ProjectCriteria criteria, QTMProject pathBase) {
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