package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.mes.MESBOPRouteOperation;
import com.cassinisys.plm.model.mes.QMESTool;
import com.cassinisys.plm.model.mro.MROWorkOrderResource;
import com.cassinisys.plm.repo.mes.BOPOperationResourceRepository;
import com.cassinisys.plm.repo.mes.BOPRouteOperationRepository;
import com.cassinisys.plm.repo.mro.MROWorkOrderResourceRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suresh CassiniPLM on 27-10-2020.
 */
@Component
public class ToolPredicateBuilder implements PredicateBuilder<ToolCriteria, QMESTool> {
    @Autowired
    private MROWorkOrderResourceRepository workOrderResourceRepository;
    @Autowired
    private BOPRouteOperationRepository bopRouteOperationRepository;
    @Autowired
    private BOPOperationResourceRepository bopOperationResourceRepository;

    @Override
    public Predicate build(ToolCriteria toolCriteria, QMESTool pathBase) {
        return getDefaultPredicate(toolCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(ToolCriteria criteria, QMESTool pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.type.name.containsIgnoreCase(s).
                        or(pathBase.number.containsIgnoreCase(s)).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getToolType())) {
            predicates.add(pathBase.type.id.eq(criteria.getToolType()));
            if (!Criteria.isEmpty(criteria.getBopRoute())) {
                MESBOPRouteOperation plan = bopRouteOperationRepository.findOne(criteria.getBopRoute());
                List<Integer> ids = bopOperationResourceRepository.getResourceIdsByPlanAndOperationAndResourceType(criteria.getBopRoute(), plan.getOperation(), criteria.getToolType());
                if (ids.size() > 0) {
                    predicates.add(pathBase.id.notIn(ids));
                }
            }
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getWorkOrder())) {
            List<MROWorkOrderResource> itemSpecs = workOrderResourceRepository.findByWorkOrderAndResourceType(criteria.workOrder, ObjectType.valueOf("TOOL"));

            //List<Integer> itemSpecs = workOrderResourceRepository.getResourceIdsByWorkOrder(criteria.workOrder);
            for (MROWorkOrderResource itemSpecification : itemSpecs) {
                predicates.add(pathBase.id.ne(itemSpecification.getResourceId()));
            }
        }

        return ExpressionUtils.allOf(predicates);
    }
}