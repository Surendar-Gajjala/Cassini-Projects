package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pqm.PQMProductInspectionPlan;
import com.cassinisys.plm.model.pqm.QPQMItemInspection;
import com.cassinisys.plm.model.pqm.QPQMProductInspectionPlan;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.cassinisys.plm.repo.pqm.ProductInspectionPlanRepository;
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
public class ItemInspectionPredicateBuilder implements PredicateBuilder<InspectionPlanCriteria, QPQMItemInspection> {

    @Autowired
    private ProductInspectionPlanPredicateBuilder productInspectionPlanPredicateBuilder;
    @Autowired
    private ProductInspectionPlanRepository productInspectionPlanRepository;

    @Override
    public Predicate build(InspectionPlanCriteria criteria, QPQMItemInspection pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(InspectionPlanCriteria criteria, QPQMItemInspection pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            List<Integer> inspectionIds = getInspectionIds(criteria);
            List<Integer> inspectionProductIds = getInspectionProductIds(criteria);

            inspectionIds.addAll(inspectionProductIds);
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                if (inspectionIds.size() > 0) {
                    Predicate predicate = pathBase.inspectionNumber.containsIgnoreCase(s).
                            or(pathBase.deviationSummary.containsIgnoreCase(s)).
                            or(pathBase.notes.containsIgnoreCase(s).or(pathBase.inspectionPlan.in(inspectionIds)));
                    predicates.add(predicate);
                } else {
                    Predicate predicate = pathBase.inspectionNumber.containsIgnoreCase(s).
                            or(pathBase.deviationSummary.containsIgnoreCase(s)).
                            or(pathBase.notes.containsIgnoreCase(s));
                    predicates.add(predicate);
                }
            }
        }

        if (!Criteria.isEmpty(criteria.getProduct()) && criteria.getRejected()) {
            List<Integer> inspectionProductIds = getInspectionPlansByProduct(criteria);
            predicates.add(pathBase.statusType.eq(WorkflowStatusType.REJECTED).and(pathBase.inspectionPlan.in(inspectionProductIds)));
        }

        return ExpressionUtils.allOf(predicates);
    }

    private List<Integer> getInspectionIds(InspectionPlanCriteria criteria) {
        List<Integer> inspectionPlanIds = new ArrayList<>();
        InspectionPlanCriteria itemCriteria = new InspectionPlanCriteria();
        itemCriteria.setName(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = productInspectionPlanPredicateBuilder.build(itemCriteria, QPQMProductInspectionPlan.pQMProductInspectionPlan);
        Page<PQMProductInspectionPlan> inspectionPlans = productInspectionPlanRepository.findAll(predicate, pageable);

        for (PQMProductInspectionPlan item : inspectionPlans.getContent()) {
            inspectionPlanIds.add(item.getLatestRevision());
        }

        return inspectionPlanIds;
    }

    private List<Integer> getInspectionProductIds(InspectionPlanCriteria criteria) {
        List<Integer> inspectionPlanIds = new ArrayList<>();
        InspectionPlanCriteria itemCriteria = new InspectionPlanCriteria();
        itemCriteria.setProductName(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = productInspectionPlanPredicateBuilder.build(itemCriteria, QPQMProductInspectionPlan.pQMProductInspectionPlan);
        Page<PQMProductInspectionPlan> inspectionPlans = productInspectionPlanRepository.findAll(predicate, pageable);

        for (PQMProductInspectionPlan item : inspectionPlans.getContent()) {
            inspectionPlanIds.add(item.getLatestRevision());
        }

        return inspectionPlanIds;
    }

    private List<Integer> getInspectionPlansByProduct(InspectionPlanCriteria criteria) {
        List<Integer> inspectionPlanIds = new ArrayList<>();
        InspectionPlanCriteria itemCriteria = new InspectionPlanCriteria();
        itemCriteria.setProduct(criteria.getProduct());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = productInspectionPlanPredicateBuilder.build(itemCriteria, QPQMProductInspectionPlan.pQMProductInspectionPlan);
        Page<PQMProductInspectionPlan> inspectionPlans = productInspectionPlanRepository.findAll(predicate, pageable);

        for (PQMProductInspectionPlan item : inspectionPlans.getContent()) {
            inspectionPlanIds.add(item.getLatestRevision());
        }

        return inspectionPlanIds;
    }
}
