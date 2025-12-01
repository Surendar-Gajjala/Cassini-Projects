package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.common.QPerson;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 24-08-2016.
 */
@Component
public class PersonPredicateBuilder implements
        PredicateBuilder<PersonCriteria, QPerson> {

    @Override
    public Predicate build(PersonCriteria criteria, QPerson pathBase) {
        if (criteria.isFreeTextSearch()) {
            List<Predicate> predicates = new ArrayList<>();
            if (!Criteria.isEmpty(criteria.getSearchQuery())) {
                Predicate predicate1 = getFreeTextSearchPredicate(criteria, pathBase);
                predicates.add(predicate1);
            }
            if (!Criteria.isEmpty(criteria.getPersonType())) {
                Predicate predicate2 = getDefaultPredicate(criteria, pathBase);
                predicates.add(predicate2);
            } else {
                Predicate predicate2 = getDefaultPredicate(criteria, pathBase);
                predicates.add(predicate2);
            }
            if (!Criteria.isEmpty(criteria.getObjectPerson())) {
                predicates.add(pathBase.id.ne(criteria.getObjectPerson()));
            }
            return ExpressionUtils.allOf(predicates);

        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(PersonCriteria criteria, QPerson pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.firstName.containsIgnoreCase(s).
                        or(pathBase.lastName.containsIgnoreCase(s)).
                        or(pathBase.phoneMobile.containsIgnoreCase(s)).
                        or(pathBase.phoneOffice.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(PersonCriteria criteria, QPerson pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getFirstName())) {
            predicates.add(pathBase.firstName.equalsIgnoreCase(criteria.getFirstName()));
        }
        if (!Criteria.isEmpty(criteria.getPersonType())) {
            predicates.add(pathBase.personType.eq(Integer.parseInt(criteria.getPersonType())));
        }
        if (!Criteria.isEmpty(criteria.getPhoneNumber())) {
            predicates.add(pathBase.phoneMobile.equalsIgnoreCase(criteria.getPhoneNumber()));
        }
        if (!Criteria.isEmpty(criteria.getPhoneNumber())) {
            predicates.add(pathBase.phoneOffice.equalsIgnoreCase(criteria.getPhoneNumber()));
        }
        if (!Criteria.isEmpty(criteria.getObjectPerson())) {
            predicates.add(pathBase.id.ne(criteria.getObjectPerson()));
        }
        if (criteria.getPersonIds().size() > 0) {
            predicates.add(pathBase.id.notIn(criteria.getPersonIds()));
        }
        if (criteria.getFilterPersons()) {
            predicates.add(pathBase.id.in(criteria.getFilterIds()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
