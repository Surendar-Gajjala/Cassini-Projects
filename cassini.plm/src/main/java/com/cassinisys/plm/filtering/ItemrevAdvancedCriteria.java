package com.cassinisys.plm.filtering;

import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 03-07-2017.
 */
@Component
public class ItemrevAdvancedCriteria {

    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;

    public Predicate getItemPredicate(ParameterCriteria parameterCriteria, CriteriaBuilder builder, CriteriaQuery cq,
                                      Root<PLMItem> from) {
        Subquery<Integer> revSq = cq.subquery(Integer.class);
        Root<PLMItemRevision> sFrom = revSq.from(PLMItemRevision.class);
        revSq.select(sFrom.get("itemMaster")).distinct(true);
        revSq.select(sFrom.get("id"));
        List<Predicate> bomPredicates = new ArrayList<>();
        if (parameterCriteria.getField().equals("item.revision")) {
            if (parameterCriteria.getOperator().equals("equals")) {
                Predicate bp4 = builder.equal(builder.lower(sFrom.get("revision")), parameterCriteria.getValue().toLowerCase());
                bomPredicates.add(bp4);
            } else if (parameterCriteria.getOperator().equals("contains")) {
                Predicate bp4 = builder.like(builder.lower(sFrom.get("revision")), "%" + parameterCriteria.getValue() + "%");
                bomPredicates.add(bp4);
            } else if (parameterCriteria.getOperator().equals("startswith")) {
                Predicate bp4 = builder.like(builder.lower(sFrom.get("revision")), parameterCriteria.getValue() + "%");
                bomPredicates.add(bp4);
            } else if (parameterCriteria.getOperator().equals("endswith")) {
                Predicate bp4 = builder.like(builder.lower(sFrom.get("revision")), "%" + parameterCriteria.getValue());
                bomPredicates.add(bp4);
            } else if (parameterCriteria.getOperator().equals("notequal")) {
                Predicate bp4 = builder.notEqual(builder.lower(sFrom.get("revision")), parameterCriteria.getValue().toLowerCase());
                bomPredicates.add(bp4);
            } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                Predicate bp4 = builder.notLike(builder.lower(sFrom.get("revision")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                bomPredicates.add(bp4);
            } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                Predicate bp4 = builder.notLike(builder.lower(sFrom.get("revision")), parameterCriteria.getValue().toLowerCase() + "%");
                bomPredicates.add(bp4);
            } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                Predicate bp4 = builder.notLike(builder.lower(sFrom.get("revision")), "%" + parameterCriteria.getValue().toLowerCase());
                bomPredicates.add(bp4);
            }
        }
        if (parameterCriteria.getField().equals("item.lifeCycle")) {
            List<Predicate> predicatesTemp = new ArrayList();
            if (parameterCriteria.getOperator().equals("equals")) {
                List<Integer> lifeCyclePhases = lifeCyclePhaseRepository.findIdByPhase(parameterCriteria.getValue());
                for (Integer id : lifeCyclePhases) {
                    Predicate bp4 = builder.equal((sFrom.get("lifeCyclePhase")), id);
                    predicatesTemp.add(bp4);
                }
                bomPredicates.add(builder.or(predicatesTemp.toArray(new Predicate[predicatesTemp.size()])));
            } else {
                List<Integer> lifeCyclePhases = lifeCyclePhaseRepository.findIdByPhase(parameterCriteria.getValue());
                for (Integer id : lifeCyclePhases) {
                    Predicate bp4 = builder.notEqual((sFrom.get("lifeCyclePhase")), id);
                    predicatesTemp.add(bp4);
                }
                bomPredicates.add(builder.and(predicatesTemp.toArray(new Predicate[predicatesTemp.size()])));
            }

        }
        revSq.where(bomPredicates.toArray(new Predicate[]{}));
        Predicate p4 = builder.in(from.get("latestRevision")).value(revSq.select(sFrom.get("id")).distinct(true));
        return p4;
    }
}
