/*
package com.cassinisys.is.filtering;
*/
/**
 * The class is for ItemPredicateBuilder
 * <p>
 * The method used to build Predicate
 * <p>
 * The method used to getFreeTextSearchPredicate of Predicate
 * <p>
 * The method used to getDefaultPredicate of Predicate
 *//*


import com.cassinisys.is.model.procm.QISItem;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ItemPredicateBuilder implements PredicateBuilder<ItemCriteria, QISItem> {
    */
/**
 * The method used to build Predicate
 *//*

    @Override
    public Predicate build(ItemCriteria criteria, QISItem pathBase) {

        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    */
/**
 * The method used to getFreeTextSearchPredicate of Predicate
 *//*

    private Predicate getFreeTextSearchPredicate(ItemCriteria criteria, QISItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.itemType.name.containsIgnoreCase(s).
                        or(pathBase.itemNumber.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    */
/**
 * The method used to getDefaultPredicate of Predicate
 *//*

    private Predicate getDefaultPredicate(ItemCriteria criteria, QISItem pathBase) {

        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getItemType())) {
            predicates.add(pathBase.itemType.name.equalsIgnoreCase(criteria.getItemType()));
        }
        if (!Criteria.isEmpty(criteria.getItemNumber())) {
            predicates.add(pathBase.itemNumber.equalsIgnoreCase(criteria.getItemNumber()));
        }

        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.equalsIgnoreCase(criteria.getDescription()));
        }


        if (!Criteria.isEmpty(criteria.getUnitPrice())) {
            String s = criteria.getUnitPrice().trim();
            if (s.startsWith("=")) {
                Double d = Double.parseDouble(s.substring(1));
                predicates.add(pathBase.unitPrice.eq(d));
            }
            if (s.startsWith(">")) {
                Double d = Double.parseDouble(s.substring(1));
                predicates.add(pathBase.unitPrice.goe(d));
            }
            if (s.startsWith("<")) {
                Double d = Double.parseDouble(s.substring(1));
                predicates.add(pathBase.unitPrice.loe(d));
            }
        }

        if (!Criteria.isEmpty(criteria.getUnitCost())) {
            String s = criteria.getUnitCost().trim();
            if (s.startsWith("=")) {
                Double d = Double.parseDouble(s.substring(1));
                predicates.add(pathBase.unitCost.eq(d));
            }
            if (s.startsWith(">")) {
                Double d = Double.parseDouble(s.substring(1));
                predicates.add(pathBase.unitCost.goe(d));
            }
            if (s.startsWith("<")) {
                Double d = Double.parseDouble(s.substring(1));
                predicates.add(pathBase.unitCost.loe(d));
            }
        }

        Predicate p = Criteria.getDateRangePredicate(pathBase.createdDate, criteria.getCreatedDate());
        if (p != null) {
            predicates.add(p);
        }

        p = Criteria.getDateRangePredicate(pathBase.modifiedDate, criteria.getModifiedDate());
        if (p != null) {
            predicates.add(p);
        }
        return ExpressionUtils.allOf(predicates);
    }
}
*/
