package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.procurement.QSupplier;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 17-01-2019.
 */
@Component
public class SupplierPredicateBuilder implements PredicateBuilder<ProcurementCriteria, QSupplier> {

    @Override
    public Predicate build(ProcurementCriteria criteria, QSupplier pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(ProcurementCriteria criteria, QSupplier pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.supplierName.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.supplierCode.containsIgnoreCase(s)).
                        or(pathBase.contactPerson.containsIgnoreCase(s)).
                        or(pathBase.email.containsIgnoreCase(s)).
                        or(pathBase.phoneNumber.contains(s));
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.anyOf(predicates);
    }

    public Predicate getDefaultPredicate(ProcurementCriteria criteria, QSupplier pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        return ExpressionUtils.allOf(predicates);
    }
}
