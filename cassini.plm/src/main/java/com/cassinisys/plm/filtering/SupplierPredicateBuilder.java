package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pqm.QPQMSupplier;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 27-06-2018.
 */
@Component
public class SupplierPredicateBuilder implements PredicateBuilder<CustomerSupplierCriteria, QPQMSupplier> {

    @Override
    public Predicate build(CustomerSupplierCriteria criteria, QPQMSupplier pathBase) {
        Predicate predicate = getFreeTextSearchPredicate(criteria, pathBase);
        return predicate;
    }

    private Predicate getFreeTextSearchPredicate(CustomerSupplierCriteria criteria, QPQMSupplier pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.address.containsIgnoreCase(s)).
                        or(pathBase.notes.containsIgnoreCase(s)).
                        or(pathBase.phone.containsIgnoreCase(s)).
                        or(pathBase.email.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

}
