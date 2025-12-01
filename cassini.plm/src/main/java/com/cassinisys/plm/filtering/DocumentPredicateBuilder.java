package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.QPLMDocument;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentPredicateBuilder implements PredicateBuilder<DocumentCriteria, QPLMDocument> {

    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;

    @Override
    public Predicate build(DocumentCriteria documentCriteria, QPLMDocument pathBase) {
        return getDefaultPredicate(documentCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(DocumentCriteria criteria, QPLMDocument pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getObjectId())) {
            List<Integer> integers = new ArrayList<>();
            if (!Criteria.isEmpty(criteria.getObjectFolder())) {
                integers = objectDocumentRepository.getDocumentIdsByObjectIdAndFolderAndDocumentType(criteria.getObjectId(), criteria.getObjectFolder(), criteria.documentType);
            } else {
                integers = objectDocumentRepository.getDocumentIdsByObjectIdAndFolderIsNullAndDocumentType(criteria.getObjectId(), criteria.documentType);
            }
            if (integers.size() > 0) {
                predicates.add(pathBase.id.notIn(integers));
            }
        }

        if (!Criteria.isEmpty(criteria.getFolder())) {
            predicates.add(pathBase.parentFile.eq(criteria.getFolder()));
        } else {
            predicates.add(pathBase.parentFile.isNull());
        }

        predicates.add(pathBase.fileType.eq("FILE").and(pathBase.latest.eq(true)));

        return ExpressionUtils.allOf(predicates);
    }
}