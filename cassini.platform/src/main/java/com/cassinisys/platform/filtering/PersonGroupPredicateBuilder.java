package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.common.QPersonGroup;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 27-11-2020.
 */
@Component
public class PersonGroupPredicateBuilder implements PredicateBuilder<PersonCriteria, QPersonGroup> {

    @Override
    public Predicate build(PersonCriteria declarationCriteria, QPersonGroup pathBase) {
        return getDefaultPredicate(declarationCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(PersonCriteria criteria, QPersonGroup pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }
}
