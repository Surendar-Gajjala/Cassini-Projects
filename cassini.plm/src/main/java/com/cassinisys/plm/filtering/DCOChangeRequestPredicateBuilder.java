package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.PLMDCODCR;
import com.cassinisys.plm.model.cm.QPLMDCR;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.cassinisys.plm.repo.cm.DCODCRRepository;
import com.cassinisys.plm.repo.cm.DCRRepository;
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
public class DCOChangeRequestPredicateBuilder implements PredicateBuilder<DCOChangeRequestCriteria, QPLMDCR> {

    @Autowired
    private DCODCRRepository dcodcrRepository;

    @Autowired
    private DCRRepository dcrRepository;

    @Override
    public Predicate build(DCOChangeRequestCriteria criteria, QPLMDCR pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(DCOChangeRequestCriteria criteria, QPLMDCR pathBase) {
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

       /* if (!Criteria.isEmpty(criteria.getDco())) {
            List<PLMDCODCR> plmdcodcrs = dcodcrRepository.findByDco(criteria.getDco());
            if (plmdcodcrs.size() > 0) {
                for (PLMDCODCR plmdcodcr : plmdcodcrs) {
                    PLMDCR dcr = dcrRepository.findOne(plmdcodcr.getDcr());
                    predicates.add(pathBase.id.ne(dcr.getId()));
                }
            }
        }*/

        List<PLMDCODCR> dcodcrs = dcodcrRepository.findAll();
        dcodcrs.forEach(dcodcr -> {
            predicates.add(pathBase.id.ne(dcodcr.getDcr()));
        });

        predicates.add(pathBase.statusType.eq(WorkflowStatusType.RELEASED).and(pathBase.isImplemented.eq(false)));

        return ExpressionUtils.allOf(predicates);
    }
}
