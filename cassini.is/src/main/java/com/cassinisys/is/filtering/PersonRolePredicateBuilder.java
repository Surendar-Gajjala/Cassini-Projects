package com.cassinisys.is.filtering;
/**
 * The class is for PersonRolePredicateBuilder
 */

import com.cassinisys.is.model.pm.QISPersonRole;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonRolePredicateBuilder implements PredicateBuilder<PersonRoleCriteria, QISPersonRole> {
    /**
     * The method used to build Predicate
     */
    @Override
    public Predicate build(PersonRoleCriteria criteria, QISPersonRole pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */
    private Predicate getFreeTextSearchPredicate(PersonRoleCriteria criteria, QISPersonRole pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split("");
            for (String s : arr) {
                Predicate predicate = pathBase.person.eq(criteria.getPerson()).
                        or(pathBase.project.eq(criteria.getProject()));
                predicates.add(predicate);
            }
        }
       /* if(criteria.getProject()!=null){
            predicates.add(pathBase.role.eq(criteria.getProject()));
        }*/
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */
    public Predicate getDefaultPredicate(PersonRoleCriteria criteria, QISPersonRole pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getPerson())) {
            predicates.add(pathBase.person.eq(criteria.getPerson()));
        }
        if (!Criteria.isEmpty(criteria.getProject())) {
            predicates.add(pathBase.project.eq(criteria.getProject()));
        }


      /*  if(!Criteria.isEmpty(criteria.getRole())) {
            predicates.add(pathBase.role.eq(criteria.getRole()));
        }
*/
        return ExpressionUtils.allOf(predicates);

    }

}