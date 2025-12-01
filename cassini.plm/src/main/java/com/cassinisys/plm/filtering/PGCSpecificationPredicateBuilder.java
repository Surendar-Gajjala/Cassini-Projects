package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pgc.QPGCSpecification;
import com.cassinisys.plm.repo.pgc.PGCDeclarationSpecificationRepository;
import com.cassinisys.plm.repo.pgc.PGCItemSpecificationRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PGCSpecificationPredicateBuilder implements PredicateBuilder<PGCSpecificationCriteria, QPGCSpecification> {

    @Autowired
    private PGCDeclarationSpecificationRepository pgcDeclarationSpecificationRepository;
    @Autowired
    private PGCItemSpecificationRepository pgcItemSpecificationRepository;

    @Override
    public Predicate build(PGCSpecificationCriteria specificationCriteria, QPGCSpecification pathBase) {
        return getDefaultPredicate(specificationCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(PGCSpecificationCriteria criteria, QPGCSpecification pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.type.name.containsIgnoreCase(s).
                        or(pathBase.number.containsIgnoreCase(s)).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getType())) {
            predicates.add(pathBase.type.id.eq(criteria.getType()));
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }

        if (!Criteria.isEmpty(criteria.getDeclaration())) {
            List<Integer> declarationSpecifications = pgcDeclarationSpecificationRepository.getSpecificationIdsByDeclaration(criteria.declaration);
            for (Integer declarationSpecification : declarationSpecifications) {
                predicates.add(pathBase.id.ne(declarationSpecification));
            }
        }
        if (!Criteria.isEmpty(criteria.getItem())) {
            List<Integer> itemSpecs = pgcItemSpecificationRepository.getSpecificationIdsByItem(criteria.item);
            for (Integer itemSpecification : itemSpecs) {
                predicates.add(pathBase.id.ne(itemSpecification));
            }
        }
        return ExpressionUtils.allOf(predicates);
    }
}