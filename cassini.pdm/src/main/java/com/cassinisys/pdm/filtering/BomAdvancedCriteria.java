package com.cassinisys.pdm.filtering;

import com.cassinisys.pdm.model.PDMItem;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 14-06-2016.
 */
@Component
public class BomAdvancedCriteria {

    public Predicate getBomPredicate(ParameterCriteria parameterCriteria, CriteriaBuilder builder, CriteriaQuery cq,
                                      Root<PDMItem> from) {

        Subquery<Integer> bomSq = cq.subquery(Integer.class);
        Root<PDMItem> sFrom = bomSq.from(PDMItem.class);
        bomSq.select(sFrom.get("parent").get("id")).distinct(true);

        List<Predicate> bomPredicates = new ArrayList<>();

        if (parameterCriteria.getField().equals("bom.itemNumber")) {
            if (parameterCriteria.getOperator().equals("equals")) {
                Predicate bp4 = builder.equal(sFrom.get("item").get("itemNumber"), parameterCriteria.getValue());
                bomPredicates.add(bp4);
            } else if (parameterCriteria.getOperator().equals("contains")) {
                Predicate bp4 = builder.like(sFrom.get("item").get("itemNumber"), "%" + parameterCriteria.getValue() + "%");
                bomPredicates.add(bp4);
            } else if (parameterCriteria.getOperator().equals("startswith")) {
                Predicate bp4 = builder.like(sFrom.get("item").get("itemNumber"), parameterCriteria.getValue() + "%");
                bomPredicates.add(bp4);
            } else if (parameterCriteria.getOperator().equals("endswith")) {
                Predicate bp4 = builder.like(sFrom.get("item").get("itemNumber"), "%" + parameterCriteria.getValue());
                bomPredicates.add(bp4);
            }
        }
        else if (parameterCriteria.getField().equals("bom.notes")) {
            if (parameterCriteria.getOperator().equals("contains")) {
                Predicate bp2 = builder.like(sFrom.get("notes"), "%" + parameterCriteria.getValue() + "%");
                bomPredicates.add(bp2);
            } else if (parameterCriteria.getOperator().equals("startswith")) {
                Predicate bp2 = builder.like(sFrom.get("notes"), parameterCriteria.getValue() + "%");
                bomPredicates.add(bp2);
            } else if (parameterCriteria.getOperator().equals("endswith")) {
                Predicate bp2 = builder.like(sFrom.get("notes"), "%" + parameterCriteria.getValue());
                bomPredicates.add(bp2);
            } else if (parameterCriteria.getOperator().equals("equals")) {
                Predicate bp2 = builder.equal(sFrom.get("notes"), parameterCriteria.getValue());
                bomPredicates.add(bp2);
            }
        } else if (parameterCriteria.getField().equals("bom.refdes")) {
            if (parameterCriteria.getOperator().equals("contains")) {
                Predicate bp3 = builder.like(sFrom.get("refdes"), "%" + parameterCriteria.getValue() + "%");
                bomPredicates.add(bp3);
            } else if (parameterCriteria.getOperator().equals("startswith")) {
                Predicate bp3 = builder.like(sFrom.get("refdes"), parameterCriteria.getValue() + "%");
                bomPredicates.add(bp3);
            } else if (parameterCriteria.getOperator().equals("endswith")) {
                Predicate bp3 = builder.like(sFrom.get("refdes"), "%" + parameterCriteria.getValue());
                bomPredicates.add(bp3);
            } else if (parameterCriteria.getOperator().equals("equals")) {
                Predicate bp3 = builder.equal(sFrom.get("refdes"), parameterCriteria.getValue());
                bomPredicates.add(bp3);
            }
        }

        bomSq.where(bomPredicates.toArray(new Predicate[]{}));

        Predicate p4 = builder.in(from.get("id")).value(bomSq);
        return p4;
    }
}
