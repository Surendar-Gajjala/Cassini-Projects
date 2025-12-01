package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.col.QGroupMessage;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapna on 08/02/19.
 */
@Component
public class GroupMessagePredicateBuilder implements PredicateBuilder<GroupMessageCriteria, QGroupMessage> {

    public Predicate build(GroupMessageCriteria criteria, QGroupMessage pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(GroupMessageCriteria criteria, QGroupMessage pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.msgText.containsIgnoreCase(s).
                        and(pathBase.msgGrpId.eq(criteria.getMsgGrpId())).
                        and(pathBase.ctxObjectType.eq(criteria.getCtxObjectType())).
                        and(pathBase.ctxObjectId.eq(criteria.getCtxObjectId()));
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(GroupMessageCriteria criteria, QGroupMessage pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getMsgText())) {
            predicates.add(pathBase.msgText.equalsIgnoreCase(criteria.getMsgText()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
