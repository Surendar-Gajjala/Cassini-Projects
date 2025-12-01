package com.cassinisys.plm.filtering;

import com.cassinisys.plm.model.plm.PLMBom;
import com.cassinisys.plm.model.plm.PLMItem;
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
                                     Root<PLMItem> from) {
        Subquery<Integer> bomSq = cq.subquery(Integer.class);
        Root<PLMBom> sFrom = bomSq.from(PLMBom.class);
        bomSq.select(sFrom.get("parent").get("itemMaster")).distinct(true);
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
            } else if (parameterCriteria.getOperator().equals("notequal")) {
                Predicate bp4 = builder.notEqual(builder.lower(sFrom.get("item").get("itemNumber")), parameterCriteria.getValue().toLowerCase());
                bomPredicates.add(bp4);
            } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                Predicate bp4 = builder.notLike(builder.lower(sFrom.get("item").get("itemNumber")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                bomPredicates.add(bp4);
            } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                Predicate bp4 = builder.notLike(builder.lower(sFrom.get("item").get("itemNumber")), parameterCriteria.getValue().toLowerCase() + "%");
                bomPredicates.add(bp4);
            } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                Predicate bp4 = builder.notLike(builder.lower(sFrom.get("item").get("itemNumber")), "%" + parameterCriteria.getValue().toLowerCase());
                bomPredicates.add(bp4);
            }
        } else if (parameterCriteria.getField().equals("bom.notes")) {
            if (parameterCriteria.getOperator().equals("contains")) {
                Predicate bp2 = builder.like(builder.lower(sFrom.get("notes")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                bomPredicates.add(bp2);
            } else if (parameterCriteria.getOperator().equals("startswith")) {
                Predicate bp2 = builder.like(builder.lower(sFrom.get("notes")), parameterCriteria.getValue().toLowerCase() + "%");
                bomPredicates.add(bp2);
            } else if (parameterCriteria.getOperator().equals("endswith")) {
                Predicate bp2 = builder.like(builder.lower(sFrom.get("notes")), "%" + parameterCriteria.getValue().toLowerCase());
                bomPredicates.add(bp2);
            } else if (parameterCriteria.getOperator().equals("equals")) {
                Predicate bp2 = builder.equal(builder.lower(sFrom.get("notes")), parameterCriteria.getValue().toLowerCase());
                bomPredicates.add(bp2);
            } else if (parameterCriteria.getOperator().equals("notequal")) {
                Predicate bp2 = builder.notEqual(builder.lower(sFrom.get("notes")), parameterCriteria.getValue().toLowerCase());
                bomPredicates.add(bp2);
            } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                Predicate bp2 = builder.notLike(builder.lower(sFrom.get("notes")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                bomPredicates.add(bp2);
            } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                Predicate bp2 = builder.notLike(builder.lower(sFrom.get("notes")), parameterCriteria.getValue().toLowerCase() + "%");
                bomPredicates.add(bp2);
            } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                Predicate bp2 = builder.notLike(builder.lower(sFrom.get("notes")), "%" + parameterCriteria.getValue().toLowerCase());
                bomPredicates.add(bp2);
            } else if (parameterCriteria.getOperator().equals("isempty")) {
                Predicate bp2 = builder.or(builder.isNull(sFrom.get("notes")), builder.equal(sFrom.get("notes"), ""));
                bomPredicates.add(bp2);
            } else if (parameterCriteria.getOperator().equals("isnotempty")) {
                Predicate bp2 = builder.or(builder.isNotNull(sFrom.get("notes")), builder.notEqual(sFrom.get("notes"), ""));
                bomPredicates.add(bp2);
            }
        } else if (parameterCriteria.getField().equals("bom.refdes")) {
            if (parameterCriteria.getOperator().equals("contains")) {
                Predicate bp3 = builder.like(builder.lower(sFrom.get("refdes")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                bomPredicates.add(bp3);
            } else if (parameterCriteria.getOperator().equals("startswith")) {
                Predicate bp3 = builder.like(builder.lower(sFrom.get("refdes")), parameterCriteria.getValue().toLowerCase() + "%");
                bomPredicates.add(bp3);
            } else if (parameterCriteria.getOperator().equals("endswith")) {
                Predicate bp3 = builder.like(builder.lower(sFrom.get("refdes")), "%" + parameterCriteria.getValue().toLowerCase());
                bomPredicates.add(bp3);
            } else if (parameterCriteria.getOperator().equals("equals")) {
                Predicate bp3 = builder.equal(builder.lower(sFrom.get("refdes")), parameterCriteria.getValue().toLowerCase());
                bomPredicates.add(bp3);
            } else if (parameterCriteria.getOperator().equals("notequal")) {
                Predicate bp3 = builder.notEqual(builder.lower(sFrom.get("refdes")), parameterCriteria.getValue().toLowerCase());
                bomPredicates.add(bp3);
            } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                Predicate bp3 = builder.notLike(builder.lower(sFrom.get("refdes")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                bomPredicates.add(bp3);
            } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                Predicate bp3 = builder.notLike(builder.lower(sFrom.get("refdes")), parameterCriteria.getValue().toLowerCase() + "%");
                bomPredicates.add(bp3);
            } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                Predicate bp3 = builder.notLike(builder.lower(sFrom.get("refdes")), "%" + parameterCriteria.getValue().toLowerCase());
                bomPredicates.add(bp3);
            } else if (parameterCriteria.getOperator().equals("isempty")) {
                Predicate bp3 = builder.isNull(sFrom.get("refdes"));
                bomPredicates.add(bp3);
            } else if (parameterCriteria.getOperator().equals("isnotempty")) {
                Predicate bp3 = builder.isNotNull(sFrom.get("refdes"));
                bomPredicates.add(bp3);
            }
        }
        bomSq.where(bomPredicates.toArray(new Predicate[]{}));
        Predicate p4 = builder.in(from.get("id")).value(bomSq);
        return p4;
    }
}
