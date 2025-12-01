package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.core.QAutoNumber;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSR on 17-01-2021.
 */
@Component
public class AutoNumberPredicateBuilder implements PredicateBuilder<AutoNumberCriteria, QAutoNumber> {

    @Override
    public Predicate build(AutoNumberCriteria autoNumberCriteria, QAutoNumber pathBase) {
        return getDefaultPredicate(autoNumberCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(AutoNumberCriteria criteria, QAutoNumber pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.prefix.containsIgnoreCase(s)).
                        or(pathBase.suffix.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getPrefix())) {
            predicates.add(pathBase.prefix.containsIgnoreCase(criteria.getPrefix()));
        }
        if (!Criteria.isEmpty(criteria.getSuffix())) {
            predicates.add(pathBase.suffix.containsIgnoreCase(criteria.getSuffix()));
        }

        return ExpressionUtils.allOf(predicates);
    }
}
