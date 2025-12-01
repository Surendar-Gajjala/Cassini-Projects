package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pm.QPLMActivity;
import com.cassinisys.plm.model.pm.QPLMTask;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapna on 1/9/18.
 **/
@Component
public class ProjectTaskPredicateBuilder implements PredicateBuilder<ProjectTaskCriteria, QPLMTask> {

    private Predicate getFreeTextSearchPredicate(ProjectTaskCriteria criteria, QPLMTask pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.searchQuery)) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        return ExpressionUtils.allOf(predicates);
    }

    @Override
    public Predicate build(ProjectTaskCriteria criteria, QPLMTask pathBase) {
        return getFreeTextSearchPredicate(criteria, pathBase);
    }
}
