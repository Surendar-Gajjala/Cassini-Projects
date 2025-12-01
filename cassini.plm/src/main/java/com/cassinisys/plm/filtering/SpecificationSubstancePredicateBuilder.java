package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pgc.PGCSpecificationSubstance;
import com.cassinisys.plm.model.pgc.PGCSubstanceType;
import com.cassinisys.plm.model.pgc.QPGCSubstance;
import com.cassinisys.plm.repo.pgc.PGCSpecificationSubstanceRepository;
import com.cassinisys.plm.repo.pgc.PGCSubstanceTypeRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 20-05-2020.
 */
@Component
public class SpecificationSubstancePredicateBuilder implements PredicateBuilder<SpecificationSubstanceCriteria, QPGCSubstance> {

    @Autowired
    private PGCSubstanceTypeRepository substanceTypeRepository;

    @Autowired
    private PGCSpecificationSubstanceRepository specificationSubstanceRepository;

    public Predicate build(SpecificationSubstanceCriteria criteria, QPGCSubstance pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(SpecificationSubstanceCriteria criteria, QPGCSubstance pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getType())) {
            PGCSubstanceType type = substanceTypeRepository.findOne(criteria.getType());
            predicates.add(pathBase.type.eq(type));
        }

        if (!Criteria.isEmpty(criteria.getSpecification())) {
            List<PGCSpecificationSubstance> specificationSubstances = specificationSubstanceRepository.findBySpecification(criteria.getSpecification());

            if (specificationSubstances.size() > 0) {
                for (PGCSpecificationSubstance specificationSubstance : specificationSubstances) {
                    predicates.add(pathBase.id.ne(specificationSubstance.getSubstance().getId()));
                }
            }

        }
        return ExpressionUtils.allOf(predicates);
    }
}
