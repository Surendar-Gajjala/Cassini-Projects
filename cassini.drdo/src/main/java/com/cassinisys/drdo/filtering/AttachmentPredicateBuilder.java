package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.model.col.QAttachment;
import com.cassinisys.platform.model.core.ObjectType;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 09-12-2019.
 */
@Component
public class AttachmentPredicateBuilder implements PredicateBuilder<ItemInstanceCriteria, QAttachment> {

    @Override
    public Predicate build(ItemInstanceCriteria criteria, QAttachment attachment) {

        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getObjectType().equals(DRDOObjectType.ITEMINSTANCE)) {
            if (!Criteria.isEmpty(criteria.getSearchQuery())) {
                predicates.add(attachment.name.containsIgnoreCase(criteria.getSearchQuery())
                        .and(attachment.objectType.eq(ObjectType.valueOf(criteria.getObjectType().toString()))));
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

}
