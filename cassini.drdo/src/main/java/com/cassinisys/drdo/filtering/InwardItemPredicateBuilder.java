package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.bom.QBomItem;
import com.cassinisys.drdo.model.transactions.Inward;
import com.cassinisys.drdo.model.transactions.QInwardItem;
import com.cassinisys.drdo.repo.bom.BomItemRepository;
import com.cassinisys.drdo.repo.transactions.InwardRepository;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
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
 * Created by subramanyam reddy on 31-12-2018.
 */
@Component
public class InwardItemPredicateBuilder implements PredicateBuilder<InwardItemCriteria, QInwardItem> {

    @Autowired
    private InwardRepository inwardRepository;

    @Autowired
    private BomSearchPredicateBuilder bomSearchPredicateBuilder;

    @Autowired
    private BomItemRepository bomItemRepository;

    @Override
    public Predicate build(InwardItemCriteria criteria, QInwardItem inwardItem) {
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicates1 = new ArrayList<>();

        List<Integer> inwardIds = new ArrayList<>();
        List<Inward> inwards = inwardRepository.findInwardsByBom(criteria.getBom());

        inwards.forEach(inward -> {
            inwardIds.add(inward.getId());
        });

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {

            BomSearchCriteria bomSearchCriteria = new BomSearchCriteria();
            bomSearchCriteria.setBom(criteria.getBom());
            bomSearchCriteria.setSearchQuery(criteria.getSearchQuery());

            Predicate predicate = bomSearchPredicateBuilder.build(bomSearchCriteria, QBomItem.bomItem);

            Pageable pageable1 = new PageRequest(0, 1000,
                    new Sort(new Sort.Order(Sort.Direction.ASC, "item.itemMaster.itemName")));

            Page<BomItem> bomItems = bomItemRepository.findAll(predicate, pageable1);

            List<BomItem> bomItemIds = new ArrayList<>();
            bomItems.getContent().forEach(bomItem -> {
                bomItemIds.add(bomItem);
            });

            predicates.add(inwardItem.bomItem.in(bomItemIds));
        }

        predicates1.add(inwardItem.inward.in(inwardIds));

        return ExpressionUtils.and(ExpressionUtils.anyOf(predicates), ExpressionUtils.allOf(predicates1));
    }
}
