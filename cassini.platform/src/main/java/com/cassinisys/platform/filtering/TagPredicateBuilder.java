package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.common.QTag;
import com.cassinisys.platform.model.core.ObjectType;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 20/12/21.
 */
@Component
public class TagPredicateBuilder implements PredicateBuilder<TagCriteria, QTag> {

    @Override
    public Predicate build(TagCriteria criteria, QTag pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicates1 = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                predicates.add(pathBase.label.containsIgnoreCase(s));
            }
        }

        if (!Criteria.isEmpty(criteria.getObjectId())) {
            predicates1.add(pathBase.object.eq(criteria.getObjectId()));
        }
        if (!Criteria.isEmpty(criteria.getObjectType())) {
            predicates1.add(pathBase.objectType.eq(ObjectType.valueOf(criteria.getObjectType())));
        }

        return ExpressionUtils.and(ExpressionUtils.anyOf(predicates), ExpressionUtils.allOf(predicates1));
    }
}
