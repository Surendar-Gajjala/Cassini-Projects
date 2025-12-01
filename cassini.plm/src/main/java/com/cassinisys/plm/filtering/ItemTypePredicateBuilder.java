package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.QPLMItemType;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemTypePredicateBuilder implements PredicateBuilder<ItemTypeCriteria, QPLMItemType> {

    @Override
    public Predicate build(ItemTypeCriteria criteria, QPLMItemType pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.equalsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getItemNumberSource().getName())) {
            predicates.add(pathBase.itemNumberSource.eq(criteria.getItemNumberSource()));
        }
        if (!Criteria.isEmpty(criteria.getRevisionSequence().getName())) {
            predicates.add(pathBase.revisionSequence.eq(criteria.getRevisionSequence()));
        }
        if (!Criteria.isEmpty(criteria.getParentType())) {
            predicates.add((pathBase.parentType.eq(criteria.getParentType())));
        }
        return ExpressionUtils.allOf(predicates);
    }

}
