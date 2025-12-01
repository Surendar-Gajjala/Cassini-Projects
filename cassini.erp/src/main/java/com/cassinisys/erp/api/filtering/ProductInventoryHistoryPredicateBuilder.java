package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.api.criteria.ProductInventoryHistoryCriteria;
import com.cassinisys.erp.model.production.InventoryType;
import com.cassinisys.erp.model.production.QERPProductInventoryHistory;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 22-04-2016.
 */
@Component
public class ProductInventoryHistoryPredicateBuilder implements
        PredicateBuilder<ProductInventoryHistoryCriteria, QERPProductInventoryHistory> {

    @Override
    public Predicate build(ProductInventoryHistoryCriteria criteria, QERPProductInventoryHistory pathBase) {

        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getStockType())) {
            predicates.add(pathBase.type.eq(InventoryType.valueOf(criteria.getStockType().trim())));
        }

        if (!Criteria.isEmpty(criteria.getProduct())) {
            predicates.add(pathBase.product.eq(criteria.getProduct()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}