package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.bom.Bom;
import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.bom.BomItemType;
import com.cassinisys.drdo.model.bom.QBomItem;
import com.cassinisys.drdo.repo.bom.BomItemRepository;
import com.cassinisys.drdo.repo.bom.BomRepository;
import com.cassinisys.drdo.repo.bom.ItemTypeRepository;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 16-10-2018.
 */
@Component
public class BomSearchPredicateBuilder implements PredicateBuilder<BomSearchCriteria, QBomItem> {

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private BomRepository bomRepository;

    @Autowired
    private BomItemRepository bomItemRepository;

    @Override
    public Predicate build(BomSearchCriteria criteria, QBomItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicates1 = new ArrayList<>();

        if (criteria.getPlanningFilter()) {
            if (!Criteria.isEmpty(criteria.getSearchQuery()) && !Criteria.isEmpty(criteria.getSearchText())) {
                predicates.add(pathBase.workCenter.containsIgnoreCase(criteria.getSearchQuery())
                        .and(pathBase.item.itemMaster.itemName.containsIgnoreCase(criteria.getSearchText())));
            }

            if (Criteria.isEmpty(criteria.getSearchQuery()) && !Criteria.isEmpty(criteria.getSearchText())) {
                predicates.add(pathBase.item.itemMaster.itemName.containsIgnoreCase(criteria.getSearchText()));
            }

            if (!Criteria.isEmpty(criteria.getSearchQuery()) && Criteria.isEmpty(criteria.getSearchText())) {
                predicates.add(pathBase.workCenter.containsIgnoreCase(criteria.getSearchQuery()));
            }

            if (!Criteria.isEmpty(criteria.getSection())) {
                List<BomItem> bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(criteria.getSection());
                List<Integer> ids = new ArrayList();
                for (BomItem bomItem : bomItems) {
                    if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                        ids.add(bomItem.getId());
                    }
                    ids = visitChildren(bomItem, pathBase, ids);

                }
                predicates1.add(pathBase.id.in(ids));
            }

            if (predicates1.size() == 0) {
                predicates = new ArrayList<>();
            }
        } else {
            if (!Criteria.isEmpty(criteria.getSearchQuery())) {
                String[] strs = criteria.getSearchQuery().split(" ");
                for (String str : strs) {
                    predicates.add(pathBase.item.itemMaster.itemName.containsIgnoreCase(str)
                            .or(pathBase.item.itemMaster.description.containsIgnoreCase(str))
                            .or(pathBase.item.itemMaster.itemCode.containsIgnoreCase(str))
                            .or(pathBase.workCenter.containsIgnoreCase(str))
                            .and(pathBase.bomItemType.eq(BomItemType.PART)));

                }
            }

            if (!Criteria.isEmpty(criteria.getWorkCenter())) {
                predicates.add(pathBase.workCenter.containsIgnoreCase(criteria.getWorkCenter()));
            }

            if (!Criteria.isEmpty(criteria.getUnit())) {
                List<Integer> ints = new ArrayList<>();
                BomItem bomItem = bomItemRepository.findOne(criteria.getUnit());
                ints = visitChildren(bomItem, pathBase, ints);
                predicates1.add(pathBase.id.in(ints));
            } else if (!Criteria.isEmpty(criteria.getSubsystem())) {
                List<Integer> ints = new ArrayList<>();
                BomItem bomItem = bomItemRepository.findOne(criteria.getSubsystem());
                ints = visitChildren(bomItem, pathBase, ints);
                predicates1.add(pathBase.id.in(ints));
            } else if (!Criteria.isEmpty(criteria.getSection())) {
                List<Integer> ints = new ArrayList<>();
                BomItem bomItem = bomItemRepository.findOne(criteria.getSection());
                ints = visitChildren(bomItem, pathBase, ints);
                predicates1.add(pathBase.id.in(ints));
            } else if (!Criteria.isEmpty(criteria.getBom())) {

                Bom bom = bomRepository.findOne(criteria.getBom());
                if (bom != null) {
                    if (criteria.getVersity()) {
                        List<BomItem> bomItems = bomItemRepository.getVersitySections(bom.getId());
                        List<Integer> ids = new ArrayList();
                        for (BomItem bomItem : bomItems) {
                            if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                                ids.add(bomItem.getId());
                            }
                            ids = visitChildren(bomItem, pathBase, ids);

                        }
                        predicates1.add(pathBase.id.in(ids));
                    } else {
                        List<BomItem> bomItems = bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId());
                        List<Integer> ids = new ArrayList();
                        for (BomItem bomItem : bomItems) {
                            if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                                ids.add(bomItem.getId());
                            }
                            ids = visitChildren(bomItem, pathBase, ids);

                        }
                        predicates1.add(pathBase.id.in(ids));
                    }

                }
            }

            if (predicates1.size() == 0) {
                predicates = new ArrayList<>();
            }
        }

        return ExpressionUtils.and(ExpressionUtils.allOf(predicates), ExpressionUtils.anyOf(predicates1));
    }

    private List<Integer> visitChildren(BomItem bomItem, QBomItem pathBase, List<Integer> ids) {

        List<BomItem> children = bomItemRepository.findByParentOrderByCreatedDateAsc(bomItem.getId());
        if (children.size() > 0) {
            for (BomItem child : children) {
                if (child.getBomItemType().equals(BomItemType.PART)) {
                    ids.add(child.getId());
                }
                ids = visitChildren(child, pathBase, ids);
            }
        }

        return ids;
    }
}
