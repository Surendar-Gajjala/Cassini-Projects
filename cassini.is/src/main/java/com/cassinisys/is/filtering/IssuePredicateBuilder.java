package com.cassinisys.is.filtering;
/**
 * The class is for IssuePredicateBuilder
 */

import com.cassinisys.is.model.im.IssuePriority;
import com.cassinisys.is.model.im.QISIssue;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.model.core.ObjectType;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IssuePredicateBuilder implements PredicateBuilder<IssueCriteria, QISIssue> {
    /**
     * The method used to build Predicate
     */
    @Override
    public Predicate build(IssueCriteria criteria, QISIssue pathBase) {
        if (criteria.getFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */
    private Predicate getFreeTextSearchPredicate(IssueCriteria criteria, QISIssue pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getFreeTextSearch() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.title.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        predicates.add(pathBase.targetObjectType.eq(ObjectType.valueOf(criteria.getTargetObjectType())));
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */
    public Predicate getDefaultPredicate(IssueCriteria criteria, QISIssue pathBase) {
        if (criteria.getFreeTextSearch()) {
        }
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getPriority())) {
            predicates.add(pathBase.priority.eq(IssuePriority.valueOf(criteria.getPriority())));
        }
        if (!Criteria.isEmpty(criteria.getTitle())) {
            predicates.add(pathBase.title.equalsIgnoreCase(criteria.getTitle()));
        }
        if (!Criteria.isEmpty(criteria.getType())) {
            String s = criteria.getType().trim();
            if (s.startsWith("=")) {
                Integer d = Integer.parseInt(s.substring(1));
                predicates.add(pathBase.type.eq(d));
            }
            if (s.startsWith(">")) {
                Integer d = Integer.parseInt(s.substring(1));
                predicates.add(pathBase.type.goe(d));
            }
            if (s.startsWith("<")) {
                Integer d = Integer.parseInt(s.substring(1));
                predicates.add(pathBase.type.loe(d));
            }
        }
        if (!Criteria.isEmpty(criteria.getResolution())) {
            predicates.add(pathBase.resolution.equalsIgnoreCase(criteria.getResolution()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
