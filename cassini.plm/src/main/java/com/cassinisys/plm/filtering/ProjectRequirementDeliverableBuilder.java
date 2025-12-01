package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PLMActivityDeliverable;
import com.cassinisys.plm.model.pm.PLMTaskDeliverable;
import com.cassinisys.plm.model.rm.QRequirement;
import com.cassinisys.plm.model.rm.RequirementDeliverable;
import com.cassinisys.plm.model.rm.RequirementType;
import com.cassinisys.plm.repo.pm.ActivityDeliverableRepository;
import com.cassinisys.plm.repo.pm.TaskDeliverableRepository;
import com.cassinisys.plm.repo.rm.RequirementDeliverableRepository;
import com.cassinisys.plm.repo.rm.RequirementRepository;
import com.cassinisys.plm.repo.rm.RequirementTypeRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectRequirementDeliverableBuilder implements PredicateBuilder<RequirementBuildCriteria, QRequirement> {

    @Autowired
    private RequirementDeliverableRepository requirementDeliverableRepository;

    @Autowired
    private RequirementRepository requirementRepository;

    @Autowired
    private ActivityDeliverableRepository activityDeliverableRepository;

    @Autowired
    private TaskDeliverableRepository taskDeliverableRepository;

    @Autowired
    private RequirementTypeRepository requirementTypeRepository;

    @Override
    public Predicate build(RequirementBuildCriteria criteria, QRequirement pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(RequirementBuildCriteria criteria, QRequirement pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getProject())) {
            List<RequirementDeliverable> specificationDeliverables = requirementDeliverableRepository.findByObjectId(criteria.getProject());
            if (specificationDeliverables.size() > 0) {
                for (RequirementDeliverable projectDeliverable : specificationDeliverables) {
                    predicates.add(pathBase.id.ne(projectDeliverable.getRequirement().getId()));
                }
            }
        }
        if (criteria.getType() != null) {
            if (!Criteria.isEmpty(criteria.getType())) {
                RequirementType rmObjectType = requirementTypeRepository.findOne(criteria.getType());
                predicates.add(pathBase.type.eq(rmObjectType));
            }
        }


		/*----------------  For Project,Activity and Task Deliverables ----------------*/
        if (!Criteria.isEmpty(criteria.getObjectType()) && !Criteria.isEmpty(criteria.getObjectId())) {
            if (criteria.getObjectType().equals(PLMObjectType.PROJECT.toString())) {
                List<RequirementDeliverable> specificationDeliverables = requirementDeliverableRepository.findByObjectId(criteria.getObjectId());
                if (specificationDeliverables.size() > 0) {
                    for (RequirementDeliverable projectDeliverable : specificationDeliverables) {
                        predicates.add(pathBase.id.ne(projectDeliverable.getRequirement().getId()));
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
        }
        return ExpressionUtils.allOf(predicates);
    }
}