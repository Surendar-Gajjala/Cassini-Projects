package com.cassinisys.is.filtering;
/**
 * The class is for BoqItemPredicateBuilder
 */

import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.is.model.procm.QISBoqItem;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BoqItemPredicateBuilder implements PredicateBuilder<BoqItemCriteria, QISBoqItem> {

    /**
     * The method used to build the Predicate
     */

    @Override
    public Predicate build(BoqItemCriteria boqItemCriteria, QISBoqItem pathBase) {
        if (boqItemCriteria.getSearchQuery() != null) {
            return getFreeTextSearchPredicate(boqItemCriteria, pathBase);
        } else {
            return getDefaultPredicate(boqItemCriteria, pathBase);
        }
    }

    /**
     * The method used to getFreeTextSearchPredicate of  the Predicate
     */

    private Predicate getFreeTextSearchPredicate(BoqItemCriteria boqItemCriteria, QISBoqItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (boqItemCriteria.getSearchQuery() != null) {
            String[] arr = boqItemCriteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.itemNumber.containsIgnoreCase(s).
                        or(pathBase.itemName.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.boqName.containsIgnoreCase(s)).
                        or(pathBase.quantity.like(s));
                if (boqItemCriteria.getItemType() != null)
                    predicates.add(pathBase.itemType.eq(ResourceType.valueOf(boqItemCriteria.getItemType())));
                predicates.add(predicate);
            }
        }
        if (boqItemCriteria.getProject() != null) {
            predicates.add(pathBase.project.eq(boqItemCriteria.getProject()));
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of  the Predicate
     */

    private Predicate getDefaultPredicate(BoqItemCriteria boqItemCriteria, QISBoqItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(boqItemCriteria.getItemNumber())) {
            predicates.add(pathBase.itemNumber.containsIgnoreCase(boqItemCriteria.getItemNumber()));
        }
        if (!Criteria.isEmpty(boqItemCriteria.getItemName())) {
            predicates.add(pathBase.itemName.containsIgnoreCase(boqItemCriteria.getItemName()));
        }
        if (!Criteria.isEmpty(boqItemCriteria.getBoq())) {
            predicates.add(pathBase.boq.eq(boqItemCriteria.getBoq()));
        }
        if (!Criteria.isEmpty(boqItemCriteria.getProject())) {
            predicates.add(pathBase.project.eq(boqItemCriteria.getProject()));
        }
        if (!Criteria.isEmpty(boqItemCriteria.getItemType())) {
            predicates.add(pathBase.itemType.eq(ResourceType.valueOf(boqItemCriteria.getItemType())));
        }
        if (!Criteria.isEmpty(boqItemCriteria.getItemDescription())) {
            predicates.add(pathBase.description.equalsIgnoreCase(boqItemCriteria.getItemDescription()));
        }
        if (!Criteria.isEmpty(boqItemCriteria.getUnits())) {
            predicates.add(pathBase.units.equalsIgnoreCase(boqItemCriteria.getUnits()));
        }
        if (!Criteria.isEmpty(boqItemCriteria.getUnitPrice())) {
            String s = boqItemCriteria.getUnitPrice().trim();
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
        return ExpressionUtils.allOf(predicates);
    }
}


