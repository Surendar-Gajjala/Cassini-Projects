package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.req.QPLMRequirement;
import com.cassinisys.plm.model.req.QPLMRequirementVersion;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequirementPredicateBuilder implements PredicateBuilder<RequirementCriteria, QPLMRequirementVersion> {
    @Override
    public Predicate build(RequirementCriteria requirementVersionCriteria, QPLMRequirementVersion pathBase) {
        return getDefaultPredicate(requirementVersionCriteria, pathBase);

    }

    public Predicate getDefaultPredicate(RequirementCriteria criteria, QPLMRequirementVersion pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.master.type.name.containsIgnoreCase(s).
                        or(pathBase.master.number.containsIgnoreCase(s)).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.master.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getType())) {
            predicates.add(pathBase.master.type.id.eq(criteria.getType()));
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getAssignedTo())) {
            predicates.add(pathBase.assignedTo.in(criteria.getAssignedTo()));
        }
        
        if (!Criteria.isEmpty(criteria.getPriority())) {
            predicates.add(pathBase.priority.containsIgnoreCase(criteria.getPriority()));
        }

        if (!Criteria.isEmpty(criteria.getPhase())) {
            predicates.add(pathBase.lifeCyclePhase.phase.containsIgnoreCase(criteria.getPhase()));
        }

        predicates.add(pathBase.latest.eq(true));
        /*if (!Criteria.isEmpty(criteria.getRequirementDocument())) {
            predicates.add(pathBase.master.requirementDocumentRevision.id.eq(criteria.getRequirementDocument()));
        }*/

        return ExpressionUtils.allOf(predicates);
    }

    public Predicate getPredicate(RequirementCriteria criteria, QPLMRequirement pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getSearchQuery())
                    .or(pathBase.name.containsIgnoreCase(criteria.getSearchQuery()))
                    .or(pathBase.description.containsIgnoreCase(criteria.getSearchQuery())));
        }
        if (!Criteria.isEmpty(criteria.getType())) {
            predicates.add(pathBase.type.id.eq(criteria.getType()));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
