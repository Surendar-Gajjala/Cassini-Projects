package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.plm.QPLMItem;
import com.cassinisys.plm.model.pqm.PQMInspectionPlanRevision;
import com.cassinisys.plm.model.pqm.PQMProductInspectionPlan;
import com.cassinisys.plm.model.pqm.QPQMProductInspectionPlan;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.pqm.InspectionPlanRevisionRepository;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
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
public class ProductInspectionPlanPredicateBuilder implements PredicateBuilder<InspectionPlanCriteria, QPQMProductInspectionPlan> {

    @Autowired
    private ItemPredicateBuilder predicateBuilder;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;
    @Autowired
    private PLMWorkflowService workflowService;

    @Override
    public Predicate build(InspectionPlanCriteria criteria, QPQMProductInspectionPlan pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(InspectionPlanCriteria criteria, QPQMProductInspectionPlan pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            List<Integer> itemIds = getItems(criteria);
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                if (itemIds.size() > 0) {
                    Predicate predicate = pathBase.number.containsIgnoreCase(s).
                            or(pathBase.planType.name.containsIgnoreCase(s)).
                            or(pathBase.name.containsIgnoreCase(s)).
                            or(pathBase.description.containsIgnoreCase(s))
                            .or(pathBase.product.in(itemIds));
                    predicates.add(predicate);
                } else {
                    Predicate predicate = pathBase.number.containsIgnoreCase(s).
                            or(pathBase.planType.name.containsIgnoreCase(s)).
                            or(pathBase.name.containsIgnoreCase(s)).
                            or(pathBase.description.containsIgnoreCase(s));
                    predicates.add(predicate);
                }
            }
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getPlanType())) {
            predicates.add(pathBase.planType.id.eq(criteria.getPlanType()));
        }

        if (!Criteria.isEmpty(criteria.getProductName())) {
            InspectionPlanCriteria planCriteria = new InspectionPlanCriteria();
            planCriteria.setSearchQuery(criteria.getProductName());
            List<Integer> items = getItems(planCriteria);
            predicates.add(pathBase.product.in(items));
        }

        if (!Criteria.isEmpty(criteria.getStatus())) {
            List<Integer> ids = workflowService.getWorkflowAttachedToIdsByStatusAndType(criteria.getStatus(), PLMObjectType.PRODUCTINSPECTIONPLAN);
            predicates.add(pathBase.latestRevision.in(ids));
        }

        if (!Criteria.isEmpty(criteria.getPhase())) {
            List<Integer> ids = inspectionPlanRevisionRepository.getRevisionIdsByPhase(criteria.getPhase());
            predicates.add(pathBase.latestRevision.in(ids));
        }

        if (!Criteria.isEmpty(criteria.getProduct())) {
            predicates.add(pathBase.product.eq(criteria.getProduct()));
        }
        return ExpressionUtils.allOf(predicates);
    }


    private List<Integer> getItems(InspectionPlanCriteria criteria) {
        List<Integer> itemRevisionIds = new ArrayList<>();
        ItemCriteria itemCriteria = new ItemCriteria();
        itemCriteria.setItemName(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = predicateBuilder.build(itemCriteria, QPLMItem.pLMItem);
        Page<PLMItem> plmItems = itemRepository.findAll(predicate, pageable);

        for (PLMItem item : plmItems.getContent()) {
            itemRevisionIds.add(item.getLatestRevision());
        }

        return itemRevisionIds;
    }
}
