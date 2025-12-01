package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.api.criteria.ProductCriteria;
import com.cassinisys.erp.model.production.QERPProduct;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 8/31/15.
 */
@Component
public class ProductPredicateBuilder implements PredicateBuilder<ProductCriteria, QERPProduct> {

    @Override
    public Predicate build(ProductCriteria criteria, QERPProduct pathBase) {
        List<Predicate> predicates = new ArrayList<>();


        if(!Criteria.isEmpty(criteria.getSku())) {
            predicates.add(pathBase.sku.containsIgnoreCase(criteria.getSku()));
        }

        if(!Criteria.isEmpty(criteria.getCategory())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.category.name, criteria.getCategory()));
        }

        if(!Criteria.isEmpty(criteria.getName())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.name, criteria.getName()));
        }

        return ExpressionUtils.allOf(predicates);
    }
}
