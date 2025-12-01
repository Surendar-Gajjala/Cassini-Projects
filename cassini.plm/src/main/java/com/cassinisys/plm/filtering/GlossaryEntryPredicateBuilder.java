package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.rm.QPLMGlossaryEntry;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 08-08-2018.
 */
@Component
public class GlossaryEntryPredicateBuilder implements PredicateBuilder<GlossaryEntryCriteria, QPLMGlossaryEntry> {

    @Override
    public Predicate build(GlossaryEntryCriteria criteria, QPLMGlossaryEntry pathBase) {
        if (criteria.getSearchQuery() != null) {
            Predicate predicate = getFreeTextSearchPredicate(criteria, pathBase);
            return predicate;
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(GlossaryEntryCriteria criteria, QPLMGlossaryEntry pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.defaultDetail.name.containsIgnoreCase(s).
                        or(pathBase.defaultDetail.description.containsIgnoreCase(s)).
                        or(pathBase.defaultDetail.notes.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(GlossaryEntryCriteria criteria, QPLMGlossaryEntry pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.defaultDetail.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.defaultDetail.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.defaultDetail.notes.containsIgnoreCase(criteria.getNotes()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
