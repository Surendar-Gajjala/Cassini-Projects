package com.cassinisys.plm.filtering;

import com.cassinisys.plm.model.cm.PLMChangeFile;
import com.cassinisys.plm.model.cm.PLMECO;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 14-06-2016.
 */
@Component
public class ECOFileAdvancedCriteria {

    @PersistenceContext
    private EntityManager entityManager;

    public Predicate getECOFilePredicate(ParameterCriteria parameterCriteria, CriteriaBuilder builder,
                                         CriteriaQuery cq, Root<PLMECO> from) {
        Subquery<Integer> ecoSq = cq.subquery(Integer.class);
        Root<PLMChangeFile> iSFrom = ecoSq.from(PLMChangeFile.class);
        EntityType<PLMChangeFile> type = entityManager.getMetamodel().entity(PLMChangeFile.class);
        ecoSq.select(iSFrom.get("change")).distinct(true);
        List<Predicate> ecoPredicates = new ArrayList<>();
        if (parameterCriteria.getField().equals("ecoFile.version")) {
            if (parameterCriteria.getOperator().equals("equals")) {
                Predicate ip1 = builder.equal(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                ecoPredicates.add(ip1);
            } else if (parameterCriteria.getOperator().equals("greaterthan")) {
                Predicate ip1 = builder.greaterThan(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                ecoPredicates.add(ip1);
            } else if (parameterCriteria.getOperator().equals("lessthan")) {
                Predicate ip1 = builder.lessThan(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                ecoPredicates.add(ip1);
            } else if (parameterCriteria.getOperator().equals("greaterthanorequals")) {
                Predicate ip1 = builder.greaterThanOrEqualTo(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                ecoPredicates.add(ip1);
            } else if (parameterCriteria.getOperator().equals("lessthanorequals")) {
                Predicate ip1 = builder.lessThanOrEqualTo(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                ecoPredicates.add(ip1);
            }
        }
        if (parameterCriteria.getField().equals("ecoFile.name")) {
            if (parameterCriteria.getOperator().equals("contains")) {
//                Predicate ip2 = builder.like(iSFrom.get("name"), "%" + parameterCriteria.getValue() + "%");
                Predicate ip2 = builder.like(builder.lower(iSFrom.get("name")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                ecoPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("startswith")) {
                Predicate ip2 = builder.like(iSFrom.get("name"), parameterCriteria.getValue() + "%");
                ecoPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("endswith")) {
                Predicate ip2 = builder.like(iSFrom.get("name"), "%" + parameterCriteria.getValue() + ".%");
                ecoPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("equals")) {
                Predicate ip2 = builder.like(iSFrom.get("name"), parameterCriteria.getValue() + ".%");
                ecoPredicates.add(ip2);
            }
        }
        ecoSq.where(ecoPredicates.toArray(new Predicate[]{}));
        Predicate p5 = builder.in(from.get("id")).value(ecoSq);
        return p5;
    }
}
