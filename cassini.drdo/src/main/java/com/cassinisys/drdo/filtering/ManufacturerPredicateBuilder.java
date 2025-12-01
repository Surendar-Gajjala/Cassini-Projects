package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.procurement.QManufacturer;
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
public class ManufacturerPredicateBuilder implements PredicateBuilder<ProcurementCriteria, QManufacturer> {

    @Override
    public Predicate build(ProcurementCriteria criteria, QManufacturer manufacturer) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearch(criteria, manufacturer);
        } else {
            return getDefaultSearch(criteria, manufacturer);
        }
    }

    private Predicate getFreeTextSearch(ProcurementCriteria criteria, QManufacturer manufacturer) {
        List<Predicate> predicates = new ArrayList();
        String[] strs = criteria.getSearchQuery().split(" ");
        for (String s : strs) {
            Predicate predicate = manufacturer.name.containsIgnoreCase(s).
                    or(manufacturer.description.containsIgnoreCase(s)).
                    or(manufacturer.mfrCode.containsIgnoreCase(s)).
                    or(manufacturer.phoneNumber.contains(s)).
                    or(manufacturer.email.containsIgnoreCase(s));
            predicates.add(predicate);

        }
        return ExpressionUtils.anyOf(predicates);
    }

    private Predicate getDefaultSearch(ProcurementCriteria criteria, QManufacturer manufacturer) {
        return null;
    }
}
