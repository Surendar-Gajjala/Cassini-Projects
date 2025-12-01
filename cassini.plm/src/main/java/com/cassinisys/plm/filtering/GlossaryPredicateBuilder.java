package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.rm.QPLMGlossary;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 27-06-2018.
 */
@Component
public class GlossaryPredicateBuilder implements PredicateBuilder<GlossaryCriteria, QPLMGlossary> {

    @Override
    public Predicate build(GlossaryCriteria criteria, QPLMGlossary pathBase) {
        if (criteria.getSearchQuery() != null) {
            Predicate predicate = getFreeTextSearchPredicate(criteria, pathBase);
            return predicate;
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(GlossaryCriteria criteria, QPLMGlossary pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.defaultDetail.name.containsIgnoreCase(s).
                        or(pathBase.defaultDetail.description.containsIgnoreCase(s)).
                        and(pathBase.latest.eq(true));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(GlossaryCriteria criteria, QPLMGlossary pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.defaultDetail.name.containsIgnoreCase(criteria.getName()).and(pathBase.latest.eq(true)));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.defaultDetail.description.containsIgnoreCase(criteria.getDescription()).and(pathBase.latest.eq(true)));
        }
        return ExpressionUtils.allOf(predicates);
    }

}
