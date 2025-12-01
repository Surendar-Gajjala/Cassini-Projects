package com.cassinisys.pdm.filtering;

import com.cassinisys.pdm.model.QPDMVault;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyamreddy on 26-Jan-17.
 */
@Component
public class VaultPredicateBuilder implements PredicateBuilder<VaultCriteria, QPDMVault>{

    @Override
    public Predicate build(VaultCriteria criteria , QPDMVault pathBase){
        if(criteria.isFreeTextSearch()){
            return getFreeTextSearchPredicate(criteria, pathBase);
        }else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(VaultCriteria criteria , QPDMVault pathBase){
        List<Predicate> predicates = new ArrayList<Predicate>();

        if(criteria.getSearchQuery() != null){
            String [] arr = criteria.getSearchQuery().split(" ");
            for(String s : arr){
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(VaultCriteria criteria , QPDMVault pathBase){
        List<Predicate> predicates = new ArrayList<Predicate>();

        if(!Criteria.isEmpty(criteria.getName())){
            predicates.add(pathBase.name.equalsIgnoreCase(criteria.getName()));
        }
        if(!Criteria.isEmpty(criteria.getDescription())){
            predicates.add(pathBase.description.equalsIgnoreCase(criteria.getDescription()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
