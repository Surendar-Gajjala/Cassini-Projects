package com.cassinisys.plm.filtering;

import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemReference;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 18-06-2016.
 */
@Component
public class ReferenceAvancedCriteria {

    public Predicate getReferencePredicate(ParameterCriteria parameterCriteria, CriteriaBuilder builder,
                                           CriteriaQuery cq, Root<PLMItem> from) {
        Subquery<Integer> referenceSq = cq.subquery(Integer.class);
        Root<PLMItemReference> rSFrom = referenceSq.from(PLMItemReference.class);
        Root<PLMItem> iSFrom = referenceSq.from(PLMItem.class);
        referenceSq.select(rSFrom.get("parent")).distinct(true);
        List<Predicate> referencePredicates = new ArrayList<>();
        if (parameterCriteria.getField().equals("reference.itemNumber")) {
            if (parameterCriteria.getOperator().equals("contains")) {
                Predicate ap1 = builder.and(builder.equal(rSFrom.get("item"), iSFrom.get("id")), builder.like(iSFrom.get("itemNumber"), "%" + parameterCriteria.getValue() + "%"));
                referencePredicates.add(ap1);
            } else if (parameterCriteria.getOperator().equals("endswith")) {
                Predicate ap1 = builder.and(builder.equal(rSFrom.get("item"), iSFrom.get("id")), builder.like(iSFrom.get("itemNumber"), "%" + parameterCriteria.getValue()));
                referencePredicates.add(ap1);
            } else if (parameterCriteria.getOperator().equals("startswith")) {
                Predicate ap1 = builder.and(builder.equal(rSFrom.get("item"), iSFrom.get("id")), builder.like(iSFrom.get("itemNumber"), parameterCriteria.getValue() + "%"));
                referencePredicates.add(ap1);
            } else if (parameterCriteria.getOperator().equals("equals")) {
                Predicate ap1 = builder.and(builder.equal(rSFrom.get("item"), iSFrom.get("id")), builder.equal(iSFrom.get("itemNumber"), parameterCriteria.getValue()));
                referencePredicates.add(ap1);
            } else if (parameterCriteria.getOperator().equals("notequal")) {
                Predicate ap1 = builder.and(builder.equal(rSFrom.get("item"), iSFrom.get("id")), builder.notEqual(builder.lower(iSFrom.get("itemNumber")), parameterCriteria.getValue().toLowerCase()));
                referencePredicates.add(ap1);
            } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                Predicate ap1 = builder.and(builder.equal(rSFrom.get("item"), iSFrom.get("id")), builder.notLike(builder.lower(iSFrom.get("itemNumber")), "%" + parameterCriteria.getValue().toLowerCase() + "%"));
                referencePredicates.add(ap1);
            } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                Predicate ap1 = builder.and(builder.equal(rSFrom.get("item"), iSFrom.get("id")), builder.notLike(builder.lower(iSFrom.get("itemNumber")), parameterCriteria.getValue().toLowerCase() + "%"));
                referencePredicates.add(ap1);
            } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                Predicate ap1 = builder.and(builder.equal(rSFrom.get("item"), iSFrom.get("id")), builder.notLike(builder.lower(iSFrom.get("itemNumber")), "%" + parameterCriteria.getValue().toLowerCase()));
                referencePredicates.add(ap1);
            }
        } else if (parameterCriteria.getField().equals("reference.notes")) {
            if (parameterCriteria.getOperator().equals("contains")) {
                Predicate ap2 = builder.like(rSFrom.get("notes"), "%" + parameterCriteria.getValue() + "%");
                referencePredicates.add(ap2);
            } else if (parameterCriteria.getOperator().equals("startswith")) {
                Predicate ap2 = builder.like(rSFrom.get("notes"), parameterCriteria.getValue() + "%");
                referencePredicates.add(ap2);
            } else if (parameterCriteria.getOperator().equals("endswith")) {
                Predicate ap2 = builder.like(rSFrom.get("notes"), "%" + parameterCriteria.getValue());
                referencePredicates.add(ap2);
            } else if (parameterCriteria.getOperator().equals("equals")) {
                Predicate ap2 = builder.equal(rSFrom.get("notes"), parameterCriteria.getValue());
                referencePredicates.add(ap2);
            } else if (parameterCriteria.getOperator().equals("notequal")) {
                Predicate ap2 = builder.notEqual(builder.lower(rSFrom.get("notes")), parameterCriteria.getValue().toLowerCase());
                referencePredicates.add(ap2);
            } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                Predicate ap2 = builder.notLike(builder.lower(rSFrom.get("notes")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                referencePredicates.add(ap2);
            } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                Predicate ap2 = builder.notLike(builder.lower(rSFrom.get("notes")), parameterCriteria.getValue().toLowerCase() + "%");
                referencePredicates.add(ap2);
            } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                Predicate ap2 = builder.notLike(builder.lower(rSFrom.get("notes")), "%" + parameterCriteria.getValue().toLowerCase());
                referencePredicates.add(ap2);
            }
        } else if (parameterCriteria.getField().equals("reference.status")) {
            if (parameterCriteria.getOperator().equals("contains")) {
                Predicate ap3 = builder.like(rSFrom.get("status"), "%" + parameterCriteria.getValue() + "%");
                referencePredicates.add(ap3);
            } else if (parameterCriteria.getOperator().equals("startswith")) {
                Predicate ap3 = builder.like(rSFrom.get("status"), parameterCriteria.getValue() + "%");
                referencePredicates.add(ap3);
            } else if (parameterCriteria.getOperator().equals("endswith")) {
                Predicate ap3 = builder.like(rSFrom.get("status"), "%" + parameterCriteria.getValue());
                referencePredicates.add(ap3);
            } else if (parameterCriteria.getOperator().equals("equals")) {
                Predicate ap3 = builder.equal(rSFrom.get("status"), parameterCriteria.getValue());
                referencePredicates.add(ap3);
            } else if (parameterCriteria.getOperator().equals("notequal")) {
                Predicate ap3 = builder.notEqual(builder.lower(rSFrom.get("status")), parameterCriteria.getValue().toLowerCase());
                referencePredicates.add(ap3);
            } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                Predicate ap3 = builder.notLike(builder.lower(rSFrom.get("status")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                referencePredicates.add(ap3);
            } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                Predicate ap3 = builder.notLike(builder.lower(rSFrom.get("status")), parameterCriteria.getValue().toLowerCase() + "%");
                referencePredicates.add(ap3);
            } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                Predicate ap3 = builder.notLike(builder.lower(rSFrom.get("status")), "%" + parameterCriteria.getValue().toLowerCase());
                referencePredicates.add(ap3);
            }
        } else if (parameterCriteria.getField().equals("reference.revision")) {
            if (parameterCriteria.getOperator().equals("contains")) {
                Predicate ap4 = builder.like(rSFrom.get("revision"), "%" + parameterCriteria.getValue() + "%");
                referencePredicates.add(ap4);
            } else if (parameterCriteria.getOperator().equals("startswith")) {
                Predicate ap4 = builder.like(rSFrom.get("revision"), parameterCriteria.getValue() + "%");
                referencePredicates.add(ap4);
            } else if (parameterCriteria.getOperator().equals("endswith")) {
                Predicate ap4 = builder.like(rSFrom.get("revision"), "%" + parameterCriteria.getValue());
                referencePredicates.add(ap4);
            } else if (parameterCriteria.getOperator().equals("equals")) {
                Predicate ap4 = builder.equal(rSFrom.get("revision"), parameterCriteria.getValue());
                referencePredicates.add(ap4);
            } else if (parameterCriteria.getOperator().equals("notequal")) {
                Predicate ap4 = builder.notEqual(builder.lower(rSFrom.get("revision")), parameterCriteria.getValue().toLowerCase());
                referencePredicates.add(ap4);
            } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                Predicate ap4 = builder.notLike(builder.lower(rSFrom.get("revision")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                referencePredicates.add(ap4);
            } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                Predicate ap4 = builder.notLike(builder.lower(rSFrom.get("revision")), parameterCriteria.getValue().toLowerCase() + "%");
                referencePredicates.add(ap4);
            } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                Predicate ap4 = builder.notLike(builder.lower(rSFrom.get("revision")), "%" + parameterCriteria.getValue().toLowerCase());
                referencePredicates.add(ap4);
            }
        }
        referenceSq.where(referencePredicates.toArray(new Predicate[]{}));
        Predicate p5 = builder.in(from.get("id")).value(referenceSq);
        return p5;
    }
}