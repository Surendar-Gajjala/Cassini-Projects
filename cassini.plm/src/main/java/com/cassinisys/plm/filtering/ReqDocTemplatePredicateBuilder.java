package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.req.QPLMRequirementDocumentTemplate;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 26-06-2021.
 */
@Component
public class ReqDocTemplatePredicateBuilder implements PredicateBuilder<ReqDocTemplateCriteria, QPLMRequirementDocumentTemplate> {

    @Override
    public Predicate build(ReqDocTemplateCriteria reqDocTemplateCriteria, QPLMRequirementDocumentTemplate pathBase) {
        return getDefaultPredicate(reqDocTemplateCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(ReqDocTemplateCriteria criteria, QPLMRequirementDocumentTemplate pathBase) {
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

        return ExpressionUtils.allOf(predicates);
    }
}

