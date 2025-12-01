package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pm.QProjectTemplate;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 21-01-2021.
 */
@Component
public class ProjectTemplatePredicateBuilder implements PredicateBuilder<ProjectTemplateCriteria, QProjectTemplate> {

    @Override
    public Predicate build(ProjectTemplateCriteria projectTemplateCriteria, QProjectTemplate pathBase) {
        return getDefaultPredicate(projectTemplateCriteria, pathBase);
    }

    private Predicate getDefaultPredicate(ProjectTemplateCriteria criteria, QProjectTemplate pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
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

        predicates.add(pathBase.programTemplate.isNull());

        return ExpressionUtils.allOf(predicates);
    }
}
