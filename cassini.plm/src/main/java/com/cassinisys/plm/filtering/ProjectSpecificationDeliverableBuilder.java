package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PLMActivityDeliverable;
import com.cassinisys.plm.model.pm.PLMTaskDeliverable;
import com.cassinisys.plm.model.rm.QSpecification;
import com.cassinisys.plm.model.rm.SpecificationDeliverable;
import com.cassinisys.plm.model.rm.SpecificationType;
import com.cassinisys.plm.repo.pm.ActivityDeliverableRepository;
import com.cassinisys.plm.repo.pm.TaskDeliverableRepository;
import com.cassinisys.plm.repo.rm.*;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectSpecificationDeliverableBuilder implements PredicateBuilder<SpecificationBuilderCriteria, QSpecification> {

    @Autowired
    private SpecificationDeliverableRepository specificationDeliverableRepository;

    @Autowired
    private SpecificationRepository specificationRepository;
    @Autowired
    private SpecificationTypeRepository specificationTypeRepository;

    @Autowired
    private ActivityDeliverableRepository activityDeliverableRepository;

    @Autowired
    private TaskDeliverableRepository taskDeliverableRepository;

    @Autowired
    private RmObjectTypeRepository rmObjectTypeRepository;

    @Autowired
    private RmObjectRepository rmObjectRepository;

    private Predicate getFreeTextSearchPredicate(SpecificationBuilderCriteria criteria, QSpecification pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.objectNumber.containsIgnoreCase(s));
                predicates.add(predicate);
                predicates.add(pathBase.latest.eq(true));
            }

        }
        return ExpressionUtils.allOf(predicates);
    }

    @Override
    public Predicate build(SpecificationBuilderCriteria criteria, QSpecification pathBase) {
        if (criteria.getSearchQuery() != null) {
            Predicate predicate = getFreeTextSearchPredicate(criteria, pathBase);
            return predicate;
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }

    }

    private Predicate getDefaultPredicate(SpecificationBuilderCriteria criteria, QSpecification pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getProject())) {
            List<SpecificationDeliverable> specificationDeliverables = specificationDeliverableRepository.findByObjectId(criteria.getProject());
            if (specificationDeliverables.size() > 0) {
                for (SpecificationDeliverable projectDeliverable : specificationDeliverables) {
                    predicates.add(pathBase.id.ne(projectDeliverable.getSpecification().getId()));
                }
            }
        }
        if (criteria.getType() != null) {
            if (!Criteria.isEmpty(criteria.getType())) {
                SpecificationType rmObjectType = specificationTypeRepository.findOne(criteria.getType());
                predicates.add(pathBase.type.eq(rmObjectType));
            }
        }
        /*----------------  For Project,Activity and Task Deliverables ----------------*/
        if (!Criteria.isEmpty(criteria.getObjectType()) && !Criteria.isEmpty(criteria.getObjectId())) {
            if (criteria.getObjectType().equals(PLMObjectType.PROJECT.toString())) {
                List<SpecificationDeliverable> specificationDeliverables = specificationDeliverableRepository.findByObjectId(criteria.getObjectId());
                if (specificationDeliverables.size() > 0) {
                    for (SpecificationDeliverable projectDeliverable : specificationDeliverables) {
                        predicates.add(pathBase.id.ne(projectDeliverable.getSpecification().getId()));
                    }
                }
            } else if (criteria.getObjectType().equals(PLMObjectType.PROJECTACTIVITY.toString())) {
                List<PLMActivityDeliverable> itemDeliverables = activityDeliverableRepository.findByActivity(criteria.getObjectId());
                if (itemDeliverables.size() > 0) {
                    for (PLMActivityDeliverable activityDeliverable : itemDeliverables) {
                        predicates.add(pathBase.latestRevision.ne(activityDeliverable.getItemRevision()));
                    }
                }
            } else if (criteria.getObjectType().equals(PLMObjectType.PROJECTTASK.toString())) {
                List<PLMTaskDeliverable> itemDeliverables = taskDeliverableRepository.findByTask(criteria.getObjectId());
                if (itemDeliverables.size() > 0) {
                    for (PLMTaskDeliverable taskDeliverable : itemDeliverables) {
                        predicates.add(pathBase.latestRevision.ne(taskDeliverable.getItemRevision()));
                    }
                }
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

}