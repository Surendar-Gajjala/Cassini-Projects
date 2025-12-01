/*
package com.cassinisys.is.filtering;
*/
/**
 * The class is for ItemInventoryCriteria
 * <p>
 * The method used to build Predicate
 *//*


import com.cassinisys.is.model.procm.QISItemInventory;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemInventoryPredicateBuilder implements PredicateBuilder<ItemInventoryCriteria, QISItemInventory> {
    */
/**
 * The method used to build Predicate
 *//*

    @Override
    public Predicate build(ItemInventoryCriteria criteria,
                           QISItemInventory pathBase) {

        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getBoqItem())) {
            predicates.add(pathBase.boqItem.eq(criteria.getBoqItem()));
        }

        if (!Criteria.isEmpty(criteria.getStore())) {
            predicates.add(pathBase.store.eq(criteria.getStore()));
        }


        if (!Criteria.isEmpty(criteria.getStockOnHand())) {
            String s = criteria.getStockOnHand().trim();
            if (s.startsWith("=")) {
                Integer d = Integer.parseInt(s.substring(1));
                predicates.add(pathBase.storeOnHand.eq(d));
            }
            if (s.startsWith(">")) {
                Integer d = Integer.parseInt(s.substring(1));
                predicates.add(pathBase.storeOnHand.goe(d));
            }
            if (s.startsWith("<")) {
                Integer d = Integer.parseInt(s.substring(1));
                predicates.add(pathBase.storeOnHand.loe(d));
            }
        }

        if (!Criteria.isEmpty(criteria.getStockOnOrder())) {
            String s = criteria.getStockOnOrder().trim();
            if (s.startsWith("=")) {
                Integer d = Integer.parseInt(s.substring(1));
                predicates.add(pathBase.storeOnOrder.eq(d));
            }
            if (s.startsWith(">")) {
                Integer d = Integer.parseInt(s.substring(1));
                predicates.add(pathBase.storeOnOrder.goe(d));
            }
            if (s.startsWith("<")) {
                Integer d = Integer.parseInt(s.substring(1));
                predicates.add(pathBase.storeOnOrder.loe(d));
            }
        }


        return ExpressionUtils.allOf(predicates);
    }

}
*/
