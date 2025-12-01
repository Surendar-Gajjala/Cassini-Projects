package com.cassinisys.is.filtering;

import com.cassinisys.is.model.procm.QISSupplier;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuko on 04-10-2018.
 */

@Component
public class SupplierPredicateBuilder implements PredicateBuilder<SupplierCriteria, QISSupplier> {

    @Override
    public Predicate build(SupplierCriteria criteria, QISSupplier pathBase) {
        if (criteria.getFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }

    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */
    private Predicate getFreeTextSearchPredicate(SupplierCriteria criteria, QISSupplier pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.contactPerson.containsIgnoreCase(s)).
                        or(pathBase.contactPhone.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */
    public Predicate getDefaultPredicate(SupplierCriteria criteria, QISSupplier pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.equalsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.equalsIgnoreCase(criteria.getDescription()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
