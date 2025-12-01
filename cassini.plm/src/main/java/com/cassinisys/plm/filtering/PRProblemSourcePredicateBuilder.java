package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.QPLMItem;
import com.cassinisys.plm.model.pqm.QPQMProblemReport;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
@Component
public class PRProblemSourcePredicateBuilder implements PredicateBuilder<QCRProblemSourceCriteria, QPQMProblemReport> {

    @Autowired
    private ItemPredicateBuilder predicateBuilder;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Predicate build(QCRProblemSourceCriteria criteria, QPQMProblemReport pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(QCRProblemSourceCriteria criteria, QPQMProblemReport pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getProduct())) {
            ItemCriteria itemCriteria = new ItemCriteria();
            itemCriteria.setItemName(criteria.getProduct());
            Pageable pageable = new PageRequest(0, 1000,
                    new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
            Predicate predicate = predicateBuilder.build(itemCriteria, QPLMItem.pLMItem);
            Page<PLMItem> plmItems = itemRepository.findAll(predicate, pageable);

            List<Integer> itemIds = new ArrayList<>();

            for (PLMItem item : plmItems.getContent()) {
                itemIds.add(item.getLatestRevision());
            }

            predicates.add(pathBase.product.in(itemIds));
        }

        if (!Criteria.isEmpty(criteria.getProblem())) {
            predicates.add(pathBase.problem.containsIgnoreCase(criteria.getProblem()));
        }

        if (!Criteria.isEmpty(criteria.getType())) {
            predicates.add(pathBase.prType.id.eq(criteria.getType()));
        }

        return ExpressionUtils.allOf(predicates);
    }
}
