package com.cassinisys.pdm.filtering;

import com.cassinisys.pdm.model.PDMItem;
import com.cassinisys.pdm.model.PDMItemAttribute;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 14-06-2016.
 */
@Component
public class AttributeAdvancedCriteria {

    public Predicate getAttributePredicate(ParameterCriteria parameterCriteria, CriteriaBuilder builder,
                                           CriteriaQuery cq, Root<PDMItem> from) {

        Subquery<Integer> attSq = cq.subquery(Integer.class);
        Root<PDMItemAttribute> aSFrom = attSq.from(PDMItemAttribute.class);
        attSq.select(aSFrom.get("id").get("objectId")).distinct(true);
        List<Predicate> attributePredicates = new ArrayList<>();

        if (parameterCriteria.getField().equals("attribute.doubleValue")) {
            if (parameterCriteria.getOperator().equals("equals")) {
                Predicate ap1 = builder.equal(aSFrom.get("doubleValue"), Double.parseDouble(parameterCriteria.getValue()));
                attributePredicates.add(ap1);
            } else if (parameterCriteria.getOperator().equals("greterthan")) {
                Predicate ap1 = builder.greaterThan(aSFrom.get("doubleValue"), Double.parseDouble(parameterCriteria.getValue()));
                attributePredicates.add(ap1);
            } else if (parameterCriteria.getOperator().equals("lessthan")) {
                Predicate ap1 = builder.lessThan(aSFrom.get("doubleValue"), Double.parseDouble(parameterCriteria.getValue()));
                attributePredicates.add(ap1);
            } else if (parameterCriteria.getOperator().equals("greterthanorequals")) {
                Predicate ap1 = builder.greaterThanOrEqualTo(aSFrom.get("doubleValue"), Double.parseDouble(parameterCriteria.getValue()));
                attributePredicates.add(ap1);
            } else if (parameterCriteria.getOperator().equals("lessthanorequals")) {
                Predicate ap1 = builder.lessThanOrEqualTo(aSFrom.get("doubleValue"), Double.parseDouble(parameterCriteria.getValue()));
                attributePredicates.add(ap1);
            }
        } else if (parameterCriteria.getField().equals("attribute.attributeDef")) {
            if (parameterCriteria.getOperator().equals("equals")) {
                Predicate ap2 = builder.equal(aSFrom.get("id").get("attributeDef"), Integer.parseInt(parameterCriteria.getValue()));
                attributePredicates.add(ap2);
            } else if (parameterCriteria.getOperator().equals("greterthan")) {
                Predicate ap2 = builder.greaterThan(aSFrom.get("id").get("attributeDef"), Integer.parseInt(parameterCriteria.getValue()));
                attributePredicates.add(ap2);
            } else if (parameterCriteria.getOperator().equals("lessthan")) {
                Predicate ap2 = builder.lessThan(aSFrom.get("id").get("attributeDef"), Integer.parseInt(parameterCriteria.getValue()));
                attributePredicates.add(ap2);
            } else if (parameterCriteria.getOperator().equals("greterthanorequals")) {
                Predicate ap2 = builder.greaterThanOrEqualTo(aSFrom.get("id").get("attributeDef"), Integer.parseInt(parameterCriteria.getValue()));
                attributePredicates.add(ap2);
            } else if (parameterCriteria.getOperator().equals("lessthanorequals")) {
                Predicate ap2 = builder.lessThanOrEqualTo(aSFrom.get("id").get("attributeDef"), Integer.parseInt(parameterCriteria.getValue()));
                attributePredicates.add(ap2);
            }
        }
        attSq.where(attributePredicates.toArray(new Predicate[]{}));
        Predicate p6 = builder.in(from.get("id")).value(attSq);
        return p6;
    }
}
