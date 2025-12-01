package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.QPLMLifeCycle;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyamreddy on 018 18-May -17.
 */
@Component
public class LifeCyclePredicateBuilder implements PredicateBuilder<LifeCycleCriteria, QPLMLifeCycle> {

    @Override
    public Predicate build(LifeCycleCriteria lifeCycleCriteria, QPLMLifeCycle qplmLifeCycle) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(lifeCycleCriteria.getName())) {
            predicates.add(qplmLifeCycle.name.equalsIgnoreCase(lifeCycleCriteria.getName()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}

