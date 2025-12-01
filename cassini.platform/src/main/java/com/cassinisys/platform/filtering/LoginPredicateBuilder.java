package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.core.QLogin;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 27-11-2020.
 */
@Component
public class LoginPredicateBuilder implements PredicateBuilder<LoginCriteria, QLogin> {

    @Override
    public Predicate build(LoginCriteria loginCriteria, QLogin pathBase) {
        return getDefaultPredicate(loginCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(LoginCriteria criteria, QLogin pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.person.firstName.containsIgnoreCase(s).
                        or(pathBase.person.lastName.containsIgnoreCase(s)).
                        or(pathBase.person.email.containsIgnoreCase(s)).
                        or(pathBase.person.phoneMobile.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (criteria.getIsActive() && !criteria.getExternal()) {
            predicates.add(pathBase.isActive.eq(true).and(pathBase.external.eq(false)));
        }
        if (criteria.getIsActive() && criteria.getExternal()) {
            predicates.add(pathBase.isActive.eq(true).and(pathBase.external.eq(true)));
        }
        if (!criteria.getIsActive()) {
            predicates.add(pathBase.isActive.eq(false));
        }
        return ExpressionUtils.allOf(predicates);
    }

    public Predicate getAllActiveLoginsPredicates(LoginCriteria criteria, QLogin pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.person.firstName.containsIgnoreCase(s).
                        or(pathBase.person.lastName.containsIgnoreCase(s)).
                        or(pathBase.person.email.containsIgnoreCase(s)).
                        or(pathBase.person.phoneMobile.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        predicates.add(pathBase.isActive.eq(true));

        return ExpressionUtils.allOf(predicates);
    }
}
