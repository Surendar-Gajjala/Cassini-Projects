package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.mes.QMESPlant;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suresh CassiniPLM on 27-10-2020.
 */
@Component
public class PlantPredicateBuilder implements PredicateBuilder<PlantCriteria, QMESPlant> {
    @Override
    public Predicate build(PlantCriteria plantCriteria, QMESPlant pathBase) {
        return getDefaultPredicate(plantCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(PlantCriteria criteria, QMESPlant pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.type.name.containsIgnoreCase(s).
                        or(pathBase.number.containsIgnoreCase(s)).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.address.containsIgnoreCase(s)).
                        or(pathBase.city.containsIgnoreCase(s)).
                        or(pathBase.country.containsIgnoreCase(s)).
                        or(pathBase.postalCode.containsIgnoreCase(s)).
                        or(pathBase.phoneNumber.containsIgnoreCase(s)).
                        or(pathBase.mobileNumber.containsIgnoreCase(s)).
                        or(pathBase.faxAddress.containsIgnoreCase(s)).
                        or(pathBase.email.containsIgnoreCase(s)).
                        or(pathBase.notes.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getPlantType())) {
            predicates.add(pathBase.type.id.eq(criteria.getPlantType()));
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getAddress())) {
            predicates.add(pathBase.address.containsIgnoreCase(criteria.getAddress()));
        }
        if (!Criteria.isEmpty(criteria.getCity())) {
            predicates.add(pathBase.city.containsIgnoreCase(criteria.getCity()));
        }
        if (!Criteria.isEmpty(criteria.getCountry())) {
            predicates.add(pathBase.country.containsIgnoreCase(criteria.getCountry()));
        }
        if (!Criteria.isEmpty(criteria.getPostalCode())) {
            predicates.add(pathBase.postalCode.containsIgnoreCase(criteria.getPostalCode()));
        }
        if (!Criteria.isEmpty(criteria.getPhoneNumber())) {
            predicates.add(pathBase.phoneNumber.containsIgnoreCase(criteria.getPhoneNumber()));
        }
        if (!Criteria.isEmpty(criteria.getMobileNumber())) {
            predicates.add(pathBase.mobileNumber.containsIgnoreCase(criteria.getMobileNumber()));
        }
        if (!Criteria.isEmpty(criteria.getFaxAddress())) {
            predicates.add(pathBase.faxAddress.containsIgnoreCase(criteria.getFaxAddress()));
        }

        if (!Criteria.isEmpty(criteria.getEmail())) {
            predicates.add(pathBase.email.containsIgnoreCase(criteria.getEmail()));
        }
        if (!Criteria.isEmpty(criteria.getNotes())) {
            predicates.add(pathBase.notes.containsIgnoreCase(criteria.getNotes()));
        }


        return ExpressionUtils.allOf(predicates);
    }
}