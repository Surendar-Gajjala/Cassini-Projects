package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pdm.QPDMFolder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 24-01-2021.
 */
@Component
public class PDMFolderPredicateBuilder implements PredicateBuilder<PDMObjectCriteria, QPDMFolder> {

    @Override
    public Predicate build(PDMObjectCriteria criteria, QPDMFolder pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(PDMObjectCriteria criteria, QPDMFolder pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        if (criteria.getId() != null) {
            predicates.add(pathBase.id.eq(Integer.parseInt(criteria.getId())));
        }
        if (criteria.getValutID() != null) {
            predicates.add(pathBase.vault.eq(Integer.parseInt(criteria.getValutID())));
        }
        if (criteria.getParent() == null) {
            predicates.add(pathBase.parent.isNull());
        } else {
            predicates.add(pathBase.parent.eq(Integer.parseInt(criteria.getParent())));
        }

        return ExpressionUtils.allOf(predicates);
    }

}
