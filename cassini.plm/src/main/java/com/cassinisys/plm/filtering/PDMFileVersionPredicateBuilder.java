package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pdm.QPDMFileVersion;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PDMFileVersionPredicateBuilder implements PredicateBuilder<PDMFileVersionCriteria, QPDMFileVersion> {

    @Override
    public Predicate build(PDMFileVersionCriteria criteria, QPDMFileVersion pathBase) {
        if (criteria.getSearchQuery() != null) {
            Predicate predicate = getFreeTextSearchPredicate(criteria, pathBase);
            return predicate;
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(PDMFileVersionCriteria criteria, QPDMFileVersion pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }

            predicates.add(pathBase.latest.eq(criteria.getLatest()));

            if (!Criteria.isEmpty(criteria.getFileType())) {
                predicates.add(pathBase.file.fileType.eq(criteria.getFileType()));
            }

        }
        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(PDMFileVersionCriteria criteria, QPDMFileVersion pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        predicates.add(pathBase.latest.eq(criteria.getLatest()));

        if (!Criteria.isEmpty(criteria.getFileType())) {
            predicates.add(pathBase.file.fileType.eq(criteria.getFileType()));
        }

        return ExpressionUtils.allOf(predicates);
    }
}
