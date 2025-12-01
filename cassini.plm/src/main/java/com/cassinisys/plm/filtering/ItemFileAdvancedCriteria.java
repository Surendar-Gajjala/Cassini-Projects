package com.cassinisys.plm.filtering;

import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemFile;
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
                                          CriteriaQuery cq, Root<PLMItem> from) {
        Subquery<Integer> itemSq = cq.subquery(Integer.class);
        Root<PLMItemFile> iSFrom = itemSq.from(PLMItemFile.class);
        EntityType<PLMItemFile> type = entityManager.getMetamodel().entity(PLMItemFile.class);
        itemSq.select(iSFrom.get("item").get("itemMaster")).distinct(true);
        List<Predicate> itemPredicates = new ArrayList<>();
        if (parameterCriteria.getField().equals("itemFile.version")) {
            Predicate ip22 = builder.equal(iSFrom.get("latest"), Boolean.TRUE);
            itemPredicates.add(ip22);
            if (parameterCriteria.getOperator().equals("equals")) {
                Predicate ip1 = builder.equal(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                itemPredicates.add(ip1);
            } else if (parameterCriteria.getOperator().equals("greaterthan")) {
                Predicate ip1 = builder.greaterThan(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                itemPredicates.add(ip1);
            } else if (parameterCriteria.getOperator().equals("lessthan")) {
                Predicate ip1 = builder.lessThan(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                itemPredicates.add(ip1);
            } else if (parameterCriteria.getOperator().equals("greaterthanorequals")) {
                Predicate ip1 = builder.greaterThanOrEqualTo(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                itemPredicates.add(ip1);
            } else if (parameterCriteria.getOperator().equals("lessthanorequals")) {
                Predicate ip1 = builder.lessThanOrEqualTo(iSFrom.get("version"), Integer.parseInt(parameterCriteria.getValue()));
                itemPredicates.add(ip1);
            }
        }
        if (parameterCriteria.getField().equals("itemFile.name")) {
            Predicate ip22 = builder.equal(iSFrom.get("latest"), Boolean.TRUE);
            itemPredicates.add(ip22);
            if (parameterCriteria.getOperator().equals("contains")) {
                Predicate ip2 = builder.like(builder.lower(iSFrom.get("name")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                itemPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("startswith")) {
                Predicate ip2 = builder.like(builder.lower(iSFrom.get("name")), parameterCriteria.getValue().toLowerCase() + "%");
                itemPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("endswith")) {
                Predicate ip2 = builder.like(builder.lower(iSFrom.get("name")), "%" + parameterCriteria.getValue().toLowerCase());
                itemPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("equals")) {
                Predicate ip2 = builder.equal(builder.lower(iSFrom.get("name")), parameterCriteria.getValue().toLowerCase());
                itemPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("notequal")) {
                Predicate ip2 = builder.notEqual(builder.lower(iSFrom.get("name")), parameterCriteria.getValue().toLowerCase());
                itemPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("doesnotcontain")) {
                Predicate ip2 = builder.notLike(builder.lower(iSFrom.get("name")), "%" + parameterCriteria.getValue().toLowerCase() + "%");
                itemPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("doesnotstartwith")) {
                Predicate ip2 = builder.notLike(builder.lower(iSFrom.get("name")), parameterCriteria.getValue().toLowerCase() + "%");
                itemPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("doesnotendwith")) {
                Predicate ip2 = builder.notLike(builder.lower(iSFrom.get("name")), "%" + parameterCriteria.getValue().toLowerCase());
                itemPredicates.add(ip2);
            } else if (parameterCriteria.getOperator().equals("isempty")) {
                itemPredicates.add(builder.or(builder.isNull(iSFrom.get("name")), builder.equal(iSFrom.get("name"), "")));
            } else if (parameterCriteria.getOperator().equals("isnotempty")) {
                itemPredicates.add(builder.and(builder.isNotNull(iSFrom.get("name")), builder.notEqual(iSFrom.get("name"), "")));
            }
        }
        itemSq.where(itemPredicates.toArray(new Predicate[]{}));
        Predicate p5 = builder.in(from.get("id")).value(itemSq);
        return p5;
    }
}
