package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pgc.PGCDeclaration;
import com.cassinisys.plm.model.pgc.PGCDeclarationPart;
import com.cassinisys.plm.model.pgc.QPGCSubstance;
import com.cassinisys.plm.repo.pgc.*;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubstancePredicateBuilder implements PredicateBuilder<SubstanceCriteria, QPGCSubstance> {

    @Autowired
    private PGCBosItemRepository pgcBosItemRepository;
    @Autowired
    private PGCDeclarationPartRepository pgcDeclarationPartRepository;
    @Autowired
    private PGCDeclarationRepository pgcDeclarationRepository;
    @Autowired
    private PGCDeclarationSpecificationRepository pgcDeclarationSpecificationRepository;
    @Autowired
    private PGCSpecificationSubstanceRepository pgcSpecificationSubstanceRepository;

    @Override
    public Predicate build(SubstanceCriteria substanceCriteria, QPGCSubstance pathBase) {
        return getDefaultPredicate(substanceCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(SubstanceCriteria criteria, QPGCSubstance pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.type.name.containsIgnoreCase(s).
                        or(pathBase.number.containsIgnoreCase(s)).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.casNumber.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getDeclarationPart())) {
            PGCDeclarationPart declarationPart = pgcDeclarationPartRepository.findOne(criteria.declarationPart);
            PGCDeclaration declaration = pgcDeclarationRepository.findOne(declarationPart.getDeclaration());
            List<Integer> substanceIds = new ArrayList<>();
            List<Integer> specificationIds = pgcDeclarationSpecificationRepository.getSpecificationIdsByDeclaration(declaration.getId());
            if (specificationIds.size() > 0) {
                substanceIds = pgcSpecificationSubstanceRepository.getSubstanceIdsBySpecificationIds(specificationIds);
            }

            if (declarationPart.getBos() != null) {
                List<Integer> existSubstanceIds = pgcBosItemRepository.getSubstanceIdsByBOS(declarationPart.getBos());
                for (Integer substanceId : existSubstanceIds) {
                    predicates.add(pathBase.id.ne(substanceId));
                }
            }

            predicates.add(pathBase.id.in(substanceIds));
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
        if (!Criteria.isEmpty(criteria.getCasNumber())) {
            predicates.add(pathBase.casNumber.containsIgnoreCase(criteria.getCasNumber()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }


        return ExpressionUtils.allOf(predicates);
    }
}