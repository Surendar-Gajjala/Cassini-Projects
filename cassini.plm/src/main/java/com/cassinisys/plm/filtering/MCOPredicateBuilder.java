package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.QPLMMCO;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
@Component
public class MCOPredicateBuilder implements PredicateBuilder<MCOCriteria, QPLMMCO> {

    @Override
    public Predicate build(MCOCriteria criteria, QPLMMCO pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(MCOCriteria criteria, QPLMMCO pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.mcoNumber.containsIgnoreCase(s).
                        or(pathBase.title.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.mcoType.name.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getMcoNumber())) {
            predicates.add(pathBase.mcoNumber.containsIgnoreCase(criteria.getMcoNumber()));
        }
        if (!Criteria.isEmpty(criteria.getMcoType())) {
            predicates.add(pathBase.mcoType.id.eq(criteria.getMcoType()));
        }
        if (!Criteria.isEmpty(criteria.getTitle())) {
            predicates.add(pathBase.title.eq(criteria.getTitle()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
