package com.cassinisys.is.filtering;

import com.cassinisys.is.model.procm.QISContractor;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapna on 29/01/19.
 */
@Component
public class ContractorPredicateBuilder implements PredicateBuilder<ContractorCriteria, QISContractor> {
    /**
     * The method used to build Predicate
     */

    @Override
    public Predicate build(ContractorCriteria criteria, QISContractor pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */

    public Predicate getDefaultPredicate(ContractorCriteria criteria, QISContractor pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getContact())) {
            predicates.add(pathBase.contact.eq(criteria.getContact()));
        }
        if (criteria.getActive() != null) {
            predicates.add(pathBase.active.eq(criteria.getActive()));
        }
        return ExpressionUtils.allOf(predicates);

    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */

    private Predicate getFreeTextSearchPredicate(ContractorCriteria criteria, QISContractor pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.isFreeTextSearch() != false) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s);
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);

    }

}