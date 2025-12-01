package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PLMGlossaryDeliverable;
import com.cassinisys.plm.model.rm.QPLMGlossary;
import com.cassinisys.plm.repo.pm.ActivityDeliverableRepository;
import com.cassinisys.plm.repo.pm.GlossaryDeliverableRepository;
import com.cassinisys.plm.repo.pm.TaskDeliverableRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 12-09-2018.
 */
@Component
public class ProjectGlossaryDeliverableBuilder implements PredicateBuilder<ProjectDeliverableCriteria, QPLMGlossary> {

    @Autowired
    private GlossaryDeliverableRepository glossaryDeliverableRepository;

    @Autowired
    private ActivityDeliverableRepository activityDeliverableRepository;

    @Autowired
    private TaskDeliverableRepository taskDeliverableRepository;

    @Override
    public Predicate
    build(ProjectDeliverableCriteria criteria, QPLMGlossary pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(ProjectDeliverableCriteria criteria, QPLMGlossary pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.defaultDetail.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.defaultDetail.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getProject())) {
            List<PLMGlossaryDeliverable> glossaryDeliverables = glossaryDeliverableRepository.findByObjectId(criteria.getProject());
            if (glossaryDeliverables.size() > 0) {
                for (PLMGlossaryDeliverable glossaryDeliverable : glossaryDeliverables) {
                    predicates.add(pathBase.id.ne(glossaryDeliverable.getGlossary().getId()));
                }

            }
        }
        if (!Criteria.isEmpty(criteria.getObjectType()) && !Criteria.isEmpty(criteria.getObjectId())) {
            if (criteria.getObjectType().equals(PLMObjectType.PROJECT.toString())) {
                List<PLMGlossaryDeliverable> glossaryDeliverables = glossaryDeliverableRepository.findByObjectIdAndObjectType(criteria.getObjectId(), criteria.getObjectType());
                if (glossaryDeliverables.size() > 0) {
                    for (PLMGlossaryDeliverable glossaryDeliverable : glossaryDeliverables) {
                        predicates.add(pathBase.id.ne(glossaryDeliverable.getGlossary().getId()));
                    }
                }
            } else if (criteria.getObjectType().equals(PLMObjectType.PROJECTACTIVITY.toString())) {
                List<PLMGlossaryDeliverable> activityGlossaryDeliverables = glossaryDeliverableRepository.findByObjectIdAndObjectType(criteria.getObjectId(), criteria.getObjectType());
                if (activityGlossaryDeliverables.size() > 0) {
                    for (PLMGlossaryDeliverable activityDeliverable : activityGlossaryDeliverables) {
                        predicates.add(pathBase.id.ne(activityDeliverable.getGlossary().getId()));
                    }
                }
            } else if (criteria.getObjectType().equals(PLMObjectType.PROJECTTASK.toString())) {
                List<PLMGlossaryDeliverable> taskGlossaryDeliverables = glossaryDeliverableRepository.findByObjectIdAndObjectType(criteria.getObjectId(), criteria.getObjectType());
                if (taskGlossaryDeliverables.size() > 0) {
                    for (PLMGlossaryDeliverable taskDeliverable : taskGlossaryDeliverables) {
                        predicates.add(pathBase.id.ne(taskDeliverable.getGlossary().getId()));
                    }
                }
            }
        }
        predicates.add(pathBase.latest.eq(true));
        return ExpressionUtils.allOf(predicates);
    }

}
