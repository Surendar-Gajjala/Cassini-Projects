package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pqm.PQMQCRAggregateNCR;
import com.cassinisys.plm.model.pqm.QPQMNCR;
import com.cassinisys.plm.repo.pqm.QCRAggregateNCRRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NCRPredicateBuilder implements PredicateBuilder<NCRCriteria, QPQMNCR> {

    @Autowired
    private QCRAggregateNCRRepository qcrAggregateNCRRepository;

    @Override
    public Predicate build(NCRCriteria criteria, QPQMNCR pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(NCRCriteria criteria, QPQMNCR pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.ncrNumber.containsIgnoreCase(s).
                        or(pathBase.ncrType.name.containsIgnoreCase(s)).
                        or(pathBase.title.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.failureType.containsIgnoreCase(s)).
                        or(pathBase.severity.containsIgnoreCase(s)).
                        or(pathBase.disposition.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getNcrNumber())) {
            predicates.add(pathBase.ncrNumber.containsIgnoreCase(criteria.getNcrNumber()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getTitle())) {
            predicates.add(pathBase.title.containsIgnoreCase(criteria.getTitle()));
        }
        if (!Criteria.isEmpty(criteria.getNcrType())) {
            predicates.add(pathBase.ncrType.id.eq(criteria.getNcrType()));
        }

        if (!Criteria.isEmpty(criteria.getQcr())) {
            List<PQMQCRAggregateNCR> aggregateNCRList = qcrAggregateNCRRepository.findByQcr(criteria.getQcr());
            for (PQMQCRAggregateNCR pqmqcrAggregatePR : aggregateNCRList) {
                predicates.add(pathBase.id.ne(pqmqcrAggregatePR.getNcr().getId()));
            }
        }

        if (criteria.getReleased()) {
            predicates.add(pathBase.released.eq(true).and(pathBase.isImplemented.eq(false)));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
