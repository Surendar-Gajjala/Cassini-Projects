package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.mfr.QPLMManufacturer;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 5/3/2016.
 */
@Component
public class ManufacturerPredicateBuilder implements PredicateBuilder<ManufacturerCriteria, QPLMManufacturer> {

    @Override
    public Predicate build(ManufacturerCriteria criteria, QPLMManufacturer pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(ManufacturerCriteria criteria, QPLMManufacturer pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.mfrType.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.phoneNumber.contains(s)).
                        or(pathBase.contactPerson.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(ManufacturerCriteria criteria, QPLMManufacturer pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.equalsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getPhoneNumber())) {
            predicates.add(pathBase.phoneNumber.equalsIgnoreCase(criteria.getPhoneNumber()));
        }
        if (!Criteria.isEmpty(criteria.getType())) {
            predicates.add(pathBase.mfrType.id.eq(criteria.getType()));
        }
        if (!Criteria.isEmpty(criteria.getContactPerson())) {
            predicates.add(pathBase.contactPerson.equalsIgnoreCase(criteria.getContactPerson()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.equalsIgnoreCase(criteria.getDescription()));
        }
        return ExpressionUtils.allOf(predicates);
    }

    public Predicate getSearchPredicate(ManufacturerCriteria criteria, QPLMManufacturer pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s);
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    public Predicate getPredicates(ManufacturerCriteria criteria, QPLMManufacturer pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            Predicate predicate = pathBase.name.containsIgnoreCase(criteria.getSearchQuery()).
                    or(pathBase.mfrType.name.containsIgnoreCase(criteria.getSearchQuery())).
                    or(pathBase.description.containsIgnoreCase(criteria.getSearchQuery()))
                    .and(pathBase.mfrType.id.eq(criteria.getType()));
            predicates.add(predicate);
        } else {
            predicates.add(pathBase.mfrType.id.eq(criteria.getType()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
