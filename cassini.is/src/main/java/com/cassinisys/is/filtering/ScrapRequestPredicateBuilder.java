package com.cassinisys.is.filtering;

import com.cassinisys.is.model.store.QISScrapRequest;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapna on 31/12/18.
 */
@Component
public class ScrapRequestPredicateBuilder implements PredicateBuilder<ScrapRequestCriteria, QISScrapRequest> {

    @Override
    public Predicate build(ScrapRequestCriteria criteria, QISScrapRequest pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(ScrapRequestCriteria criteria, QISScrapRequest pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split("");
            for (String s : arr) {
                Predicate predicate = pathBase.scrapNumber.containsIgnoreCase(s).
                        or(pathBase.notes.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */
    public Predicate getDefaultPredicate(ScrapRequestCriteria criteria, QISScrapRequest pathBase) {
        return null;
    }
}
