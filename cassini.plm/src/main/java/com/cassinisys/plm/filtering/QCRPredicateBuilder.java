package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pqm.QPQMQCR;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QCRPredicateBuilder implements PredicateBuilder<QCRCriteria, QPQMQCR> {

    @Override
    public Predicate build(QCRCriteria criteria, QPQMQCR pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(QCRCriteria criteria, QPQMQCR pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.qcrType.name.containsIgnoreCase(s).
                        or(pathBase.qcrNumber.containsIgnoreCase(s)).
                        or(pathBase.title.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getQcrNumber())) {
            predicates.add(pathBase.qcrNumber.containsIgnoreCase(criteria.getQcrNumber()));
        }
        if (!Criteria.isEmpty(criteria.getQcrType())) {
            predicates.add(pathBase.qcrType.id.eq(criteria.getQcrType()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getTitle())) {
            predicates.add(pathBase.title.containsIgnoreCase(criteria.getTitle()));
        }

        if (!Criteria.isEmpty(criteria.getQcrFor())) {
            predicates.add(pathBase.qcrFor.eq(criteria.qcrFor));
        }

        if (criteria.getReleased()) {
            predicates.add(pathBase.released.eq(true));
        }

        return ExpressionUtils.allOf(predicates);
    }
}
