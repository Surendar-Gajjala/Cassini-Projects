package com.cassinisys.pdm.filtering;


import com.cassinisys.pdm.model.QPDMItemType;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Surendar reddy on 27-01-2017.
 */
@Component
public class ItemTypePredicateBuilder implements PredicateBuilder<ItemTypeCriteria, QPDMItemType> {

    @Override
    public Predicate build(ItemTypeCriteria criteria, QPDMItemType pathBase) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.equalsIgnoreCase(criteria.getName()));
        }

        if (!Criteria.isEmpty(criteria.getParentType())) {
            predicates.add(pathBase.parentType.eq(criteria.getParentType()));
        }

        return ExpressionUtils.allOf(predicates);
    }


}
