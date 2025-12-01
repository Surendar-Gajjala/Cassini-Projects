package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pm.QPLMProject;
import com.cassinisys.plm.repo.pm.ProgramProjectRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapna on 1/9/18.
 **/
@Component
public class ProjectPredicateBuilder implements PredicateBuilder<ProjectCriteria, QPLMProject> {

    @Autowired
    private ProgramProjectRepository programProjectRepository;

    private Predicate getFreeTextSearchPredicate(ProjectCriteria criteria, QPLMProject pathBase) {
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
        if (!Criteria.isEmpty(criteria.getType())) {
            predicates.add(pathBase.type.id.eq(criteria.getType()));
        }
        if (!Criteria.isEmpty(criteria.getProjectManager())) {
            predicates.add(pathBase.projectManager.in(criteria.getProjectManager()));
        }
        predicates.add(pathBase.program.isNull());
        return ExpressionUtils.allOf(predicates);
    }

    @Override
    public Predicate build(ProjectCriteria criteria, QPLMProject pathBase) {
        return getFreeTextSearchPredicate(criteria, pathBase);
    }
}
