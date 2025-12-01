package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.QPLMItem;
import com.cassinisys.plm.model.plm.QPLMItemRevision;
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

@Component
public class ItemRevisionPredicateBuilder implements PredicateBuilder<ItemCriteria, QPLMItemRevision> {

    @Autowired
    private ItemPredicateBuilder itemPredicateBuilder;
    @Autowired
    private ItemRepository itemRepository;

    private Predicate getDefaultPredicate(ItemCriteria criteria, QPLMItemRevision pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            List<Integer> itemIds = getItems(criteria);
            if (criteria.getBomCompare()) {
                if (criteria.getLatest()) {
                    predicates.add(pathBase.id.in(itemIds).and(pathBase.itemMaster.ne(criteria.getItemId())).and(pathBase.hasBom.eq(true)));
                } else {
                    predicates.add(pathBase.itemMaster.in(itemIds).and(pathBase.itemMaster.ne(criteria.getItemId())).and(pathBase.hasBom.eq(true)));
                }
            } else {
                if (criteria.getLatest()) {
                    if (!Criteria.isEmpty(criteria.getItemId())) {
                        predicates.add(pathBase.id.in(itemIds).and(pathBase.itemMaster.ne(criteria.getItemId())));
                    } else {
                        predicates.add(pathBase.id.in(itemIds));
                    }
                } else {
                    if (!Criteria.isEmpty(criteria.getItemId())) {
                        predicates.add(pathBase.itemMaster.in(itemIds).and(pathBase.itemMaster.ne(criteria.getItemId())));
                    } else {
                        predicates.add(pathBase.itemMaster.in(itemIds));
                    }
                }
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    @Override
    public Predicate build(ItemCriteria criteria, QPLMItemRevision pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private List<Integer> getItems(ItemCriteria criteria) {
        List<Integer> itemRevisionIds = new ArrayList<>();
        ItemCriteria itemCriteria = new ItemCriteria();
        itemCriteria.setSearchQuery(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = itemPredicateBuilder.build(itemCriteria, QPLMItem.pLMItem);
        Page<PLMItem> plmItems = itemRepository.findAll(predicate, pageable);
        PLMItem plmItem = itemRepository.findOne(criteria.getItemId());
        for (PLMItem item : plmItems.getContent()) {
            if (criteria.getBomCompare()) {
                if (criteria.getLatest()) {
                    itemRevisionIds.add(item.getLatestRevision());
                } else if (!criteria.getLatest()) {
                    itemRevisionIds.add(item.getId());
                }
            } else {
                if (plmItem != null) {
                    if (criteria.getLatest() && item.getItemType().getId().equals(plmItem.getItemType().getId())) {
                        itemRevisionIds.add(item.getLatestRevision());
                    } else if (!criteria.getLatest() && item.getItemType().getId().equals(plmItem.getItemType().getId())) {
                        itemRevisionIds.add(item.getId());
                    }
                } else {
                    if (criteria.getLatest()) {
                        itemRevisionIds.add(item.getLatestRevision());
                    } else if (!criteria.getLatest()) {
                        itemRevisionIds.add(item.getId());
                    }
                }
            }
        }

        return itemRevisionIds;
    }
}
