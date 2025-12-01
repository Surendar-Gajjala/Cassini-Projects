package com.cassinisys.pdm.filtering;

import com.cassinisys.pdm.model.PDMItem;
import com.cassinisys.pdm.model.PDMItemFile;
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
public class ItemFileAdvancedCriteria {

    @PersistenceContext
    private EntityManager entityManager;

    public Predicate getItemFilePredicate(ParameterCriteria parameterCriteria, CriteriaBuilder builder,
                                          CriteriaQuery cq, Root<PDMItem> from) {
        Subquery<Integer> itemSq = cq.subquery(Integer.class);
        Root<PDMItemFile> iSFrom = itemSq.from(PDMItemFile.class);
        EntityType<PDMItemFile> type = entityManager.getMetamodel().entity(PDMItemFile.class);

        itemSq.select(iSFrom.get("item").get("id")).distinct(true);

        List<Predicate> itemPredicates = new ArrayList<>();

        if (parameterCriteria.getField().equals("itemFile.version")) {

            if (parameterCriteria.getOperator().equals("equals")) {
                Predicate ip1 = builder.equal(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                itemPredicates.add(ip1);
            } else if (parameterCriteria.getOperator().equals("greterthan")) {
                Predicate ip1 = builder.greaterThan(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                itemPredicates.add(ip1);
            } else if (parameterCriteria.getOperator().equals("lessthan")) {
                Predicate ip1 = builder.lessThan(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                itemPredicates.add(ip1);
            } else if (parameterCriteria.getOperator().equals("greterthanorequals")) {
                Predicate ip1 = builder.greaterThanOrEqualTo(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                itemPredicates.add(ip1);
            } else if (parameterCriteria.getOperator().equals("lessthanorequals")) {
                Predicate ip1 = builder.lessThanOrEqualTo(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                itemPredicates.add(ip1);
            }
        }
        if (parameterCriteria.getField().equals("itemFile.name")) {

            if (parameterCriteria.getOperator().equals("contains")) {
//                Predicate ip2 = builder.like(iSFrom.get("name"), "%" + parameterCriteria.getValue() + "%");
                Predicate ip2 = builder.like(builder.lower(iSFrom.get("name")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                itemPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("startswith")) {
                Predicate ip2 = builder.like(iSFrom.get("name"), parameterCriteria.getValue() + "%");
                itemPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("endswith")) {
                Predicate ip2 = builder.like(iSFrom.get("name"), "%" + parameterCriteria.getValue());
                itemPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("equals")) {
                Predicate ip2 = builder.equal(iSFrom.get("name"), parameterCriteria.getValue());
                itemPredicates.add(ip2);
            }

        }

        itemSq.where(itemPredicates.toArray(new Predicate[]{}));
        Predicate p5 = builder.in(from.get("id")).value(itemSq);
        return p5;
    }
}
