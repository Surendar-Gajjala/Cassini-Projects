package com.cassinisys.irste.filtering;

import com.cassinisys.irste.model.QIRSTEUser;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.repo.common.PersonTypeRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 25-11-2018.
 */
@Component
public class UserPredicateBuilder implements PredicateBuilder<UserCriteria, QIRSTEUser> {
    @Autowired
    private PersonTypeRepository personTypeRepository;

    @Override
    public Predicate build(UserCriteria criteria, QIRSTEUser pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(UserCriteria criteria, QIRSTEUser pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.firstName.containsIgnoreCase(s).
                        or(pathBase.designation.containsIgnoreCase(s)).
                        or(pathBase.traineeId.containsIgnoreCase(s));

                        /*or(pathBase.presentStatus.containsIgnoreCase(s));*/
                predicates.add(predicate);
            }

            if (criteria.getPersonTypes().length != 0) {
                List<Integer> personTypeIds = personTypeRepository.getTypeIdsByNames(criteria.getPersonTypes());
                for (Integer typeId : personTypeIds) {

                    Predicate predicate = pathBase.personType.eq(typeId);

                    predicates.add(predicate);
                }

            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    public Predicate getDefaultPredicate(UserCriteria criteria, QIRSTEUser pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getPersonTypes().length != 0) {
            List<Integer> personTypeIds = personTypeRepository.getTypeIdsByNames(criteria.getPersonTypes());
            for (Integer typeId : personTypeIds) {

                Predicate predicate = pathBase.personType.eq(typeId);

                predicates.add(predicate);
            }

        }


        return ExpressionUtils.allOf(predicates);
    }

}
