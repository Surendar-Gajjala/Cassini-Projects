package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.mes.MESBOPRouteOperation;
import com.cassinisys.plm.model.mes.QMESMaterial;
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

@Component
public class MaterialPredicateBuilder implements PredicateBuilder<MaterialCriteria, QMESMaterial> {
    @Autowired
    private MROWorkOrderResourceRepository workOrderResourceRepository;
    @Autowired
    private BOPRouteOperationRepository bopRouteOperationRepository;
    @Autowired
    private BOPOperationResourceRepository bopOperationResourceRepository;

    @Override
    public Predicate build(MaterialCriteria materialCriteria, QMESMaterial pathBase) {
        return getDefaultPredicate(materialCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(MaterialCriteria criteria, QMESMaterial pathBase) {
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
        if (!Criteria.isEmpty(criteria.getType())) {
            predicates.add(pathBase.type.id.eq(criteria.getType()));
            if (!Criteria.isEmpty(criteria.getBopRoute())) {
                MESBOPRouteOperation plan = bopRouteOperationRepository.findOne(criteria.getBopRoute());
                List<Integer> ids = bopOperationResourceRepository.getResourceIdsByPlanAndOperationAndResourceType(criteria.getBopRoute(), plan.getOperation(), criteria.getType());
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
            List<MROWorkOrderResource> itemSpecs = workOrderResourceRepository.findByWorkOrderAndResourceType(criteria.workOrder, ObjectType.valueOf("MATERIAL"));

            //List<Integer> itemSpecs = workOrderResourceRepository.getResourceIdsByWorkOrder(criteria.workOrder);
            for (MROWorkOrderResource itemSpecification : itemSpecs) {
                predicates.add(pathBase.id.ne(itemSpecification.getResourceId()));
            }
        }


        return ExpressionUtils.allOf(predicates);
    }
}