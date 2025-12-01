package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.PLMECOECR;
import com.cassinisys.plm.model.cm.QPLMECR;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.cassinisys.plm.repo.cm.ECOECRRepository;
import com.cassinisys.plm.repo.cm.ECRRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by CassiniSystems on 13-06-2020.
 */
@Component
public class ECOChangeRequestPredicateBuilder implements PredicateBuilder<ECOChangeRequestCriteria, QPLMECR> {

    @Autowired
    private ECOECRRepository ecoecrRepository;

    @Autowired
    private ECRRepository ecrRepository;

    @Override
    public Predicate build(ECOChangeRequestCriteria criteria, QPLMECR pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(ECOChangeRequestCriteria criteria, QPLMECR pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getCrNumber())) {
            predicates.add(pathBase.crNumber.containsIgnoreCase(criteria.getCrNumber()));
        }

        if (!Criteria.isEmpty(criteria.getCrType())) {
            predicates.add(pathBase.crType.eq(criteria.getCrType()));
        }

        if (!Criteria.isEmpty(criteria.getTitle())) {
            predicates.add(pathBase.title.containsIgnoreCase(criteria.getTitle()));
        }

        /*if (!Criteria.isEmpty(criteria.getEco())) {
            List<PLMECOECR> plmecoecrs = ecoecrRepository.findByEco(criteria.getEco());
            if (plmecoecrs.size() > 0) {
                for (PLMECOECR plmdcodcr : plmecoecrs) {
                    PLMECR ecr = ecrRepository.findOne(plmdcodcr.getEcr());
                    predicates.add(pathBase.id.ne(ecr.getId()));
                }
            }
        }*/

        List<PLMECOECR> ecoecrs = ecoecrRepository.findAll();
        ecoecrs.forEach(ecoecr -> {
            predicates.add(pathBase.id.ne(ecoecr.getEcr()));
        });

        predicates.add(pathBase.statusType.eq(WorkflowStatusType.RELEASED).and(pathBase.isImplemented.eq(false)));

        return ExpressionUtils.allOf(predicates);
    }
}
