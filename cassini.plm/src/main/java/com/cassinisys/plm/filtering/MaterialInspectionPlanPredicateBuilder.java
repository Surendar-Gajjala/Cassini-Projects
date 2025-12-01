package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mfr.QPLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.QPQMMaterialInspectionPlan;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
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
public class MaterialInspectionPlanPredicateBuilder implements PredicateBuilder<InspectionPlanCriteria, QPQMMaterialInspectionPlan> {

    @Autowired
    private ManufacturerPartPredicateBuilder manufacturerPartPredicateBuilder;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;
    @Autowired
    private PLMWorkflowService workflowService;

    @Override
    public Predicate build(InspectionPlanCriteria criteria, QPQMMaterialInspectionPlan pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(InspectionPlanCriteria criteria, QPQMMaterialInspectionPlan pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.number.containsIgnoreCase(s).
                        or(pathBase.planType.name.containsIgnoreCase(s)).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
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

        if (!Criteria.isEmpty(criteria.getMaterial())) {
            predicates.add(pathBase.material.id.eq(criteria.getMaterial()));
        }

        if (!Criteria.isEmpty(criteria.getStatus())) {
            List<Integer> ids = workflowService.getWorkflowAttachedToIdsByStatusAndType(criteria.getStatus(), PLMObjectType.MATERIALINSPECTIONPLAN);
            predicates.add(pathBase.latestRevision.in(ids));
        }

        if (!Criteria.isEmpty(criteria.getPhase())) {
            List<Integer> ids = inspectionPlanRevisionRepository.getRevisionIdsByPhase(criteria.getPhase());
            predicates.add(pathBase.latestRevision.in(ids));
        }

        if (!Criteria.isEmpty(criteria.getProductName())) {
            InspectionPlanCriteria planCriteria = new InspectionPlanCriteria();
            planCriteria.setSearchQuery(criteria.getProductName());
            List<Integer> parts = getParts(planCriteria);
            predicates.add(pathBase.material.id.in(parts));
        }


        return ExpressionUtils.allOf(predicates);
    }


    private List<Integer> getParts(InspectionPlanCriteria criteria) {
        List<Integer> partIds = new ArrayList<>();
        ManufacturerPartCriteria manufacturerPartCriteria = new ManufacturerPartCriteria();
        manufacturerPartCriteria.setPartName(criteria.getSearchQuery());
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = manufacturerPartPredicateBuilder.build(manufacturerPartCriteria, QPLMManufacturerPart.pLMManufacturerPart);
        Page<PLMManufacturerPart> manufacturerParts = manufacturerPartRepository.findAll(predicate, pageable);

        for (PLMManufacturerPart item : manufacturerParts.getContent()) {
            partIds.add(item.getId());
        }

        return partIds;
    }
}
