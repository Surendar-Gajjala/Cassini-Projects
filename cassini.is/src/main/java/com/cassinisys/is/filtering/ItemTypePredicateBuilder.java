package com.cassinisys.is.filtering;
/**
 * The class is for ItemTypePredicateBuilder
 */

import com.cassinisys.is.model.procm.QISItemType;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemTypePredicateBuilder implements PredicateBuilder<ItemTypeCriteria, QISItemType> {

    /**
     * The method used to build Predicate
     */
    @Override
    public Predicate build(ItemTypeCriteria criteria, QISItemType pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.equalsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getParentType())) {
            predicates.add(pathBase.parentType.eq(criteria.getParentType()));
        }
        return ExpressionUtils.allOf(predicates);

    }
}
