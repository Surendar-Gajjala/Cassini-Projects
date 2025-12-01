package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pdm.QPDMVault;
import com.cassinisys.plm.repo.pdm.PDMFileVersionRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PDMVaultPredicateBuilder implements PredicateBuilder<PDMObjectCriteria, QPDMVault> {

    @Autowired
    private PDMFileVersionRepository pdmFileVersionRepository;

    @Override
    public Predicate build(PDMObjectCriteria criteria, QPDMVault pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(PDMObjectCriteria criteria, QPDMVault pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
//            List<Integer> integers = pdmFileVersionRepository.findAttachedTByLatestIsTrue();
//            predicates.add(pathBase.id.in(integers));
        }


        return ExpressionUtils.allOf(predicates);
    }
}
