package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.bom.BomItemType;
import com.cassinisys.drdo.model.bom.QBomItem;
import com.cassinisys.drdo.repo.bom.BomItemRepository;
import com.cassinisys.drdo.repo.bom.BomRepository;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 05-03-2019.
 */
@Component
public class WorkCenterPredicateBuilder implements PredicateBuilder<BomSearchCriteria, QBomItem> {

    @Autowired
    private BomRepository bomRepository;

    @Autowired
    private BomItemRepository bomItemRepository;

    @Override
    public Predicate build(BomSearchCriteria criteria, QBomItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(pathBase.workCenter.containsIgnoreCase(criteria.getSearchQuery()));

        List<Predicate> predicates1 = new ArrayList<>();

        List<Integer> bomItemIds = new ArrayList<>();
        List<BomItem> bomItems = new ArrayList<>();

        if (criteria.getVersity()) {
            bomItems = bomItemRepository.getVersitySections(criteria.getBom());
        } else {
            bomItems = bomItemRepository.findByBomOrderByCreatedDateAsc(criteria.getBom());
        }


        for (BomItem bomItem : bomItems) {
            bomItemIds = visitBomChildren(bomItem, bomItemIds);
        }

        predicates1.add(pathBase.id.in(bomItemIds));

        return ExpressionUtils.and(ExpressionUtils.allOf(predicates), ExpressionUtils.allOf(predicates1));
    }

    private List<Integer> visitBomChildren(BomItem bomItem, List<Integer> bomItemIds) {

        List<BomItem> bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());

        for (BomItem item : bomItems) {
            if (item.getBomItemType().equals(BomItemType.PART)) {
                bomItemIds.add(item.getId());
            } else {
                bomItemIds = visitBomChildren(item, bomItemIds);
            }
        }
        return bomItemIds;
    }
}
