package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pqm.QPQMNCR;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
@Component
public class NCRProblemSourcePredicateBuilder implements PredicateBuilder<QCRProblemSourceCriteria, QPQMNCR> {

    @Override
    public Predicate build(QCRProblemSourceCriteria criteria, QPQMNCR pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(QCRProblemSourceCriteria criteria, QPQMNCR pathBase) {
        List<Predicate> predicates = new ArrayList<>();


        return ExpressionUtils.allOf(predicates);
    }
}
