package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.req.PLMRequirementVersion;
import com.cassinisys.plm.model.req.QPLMRequirementDocumentChildren;
import com.cassinisys.plm.model.req.QPLMRequirementVersion;
import com.cassinisys.plm.repo.req.PLMRequirementRepository;
import com.cassinisys.plm.repo.req.PLMRequirementVersionRepository;
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
public class RequirementDocumentChildrenPredicateBuilder implements PredicateBuilder<RequirementCriteria, QPLMRequirementDocumentChildren> {

    @Autowired
    private RequirementPredicateBuilder requirementPredicateBuilder;

    @Autowired
    private PLMRequirementVersionRepository requirementVersionRepository;

    @Autowired
    private PLMRequirementRepository requirementRepository;

    @Override
    public Predicate build(RequirementCriteria requirementDocumentCriteria, QPLMRequirementDocumentChildren pathBase) {
        return getDefaultPredicate(requirementDocumentCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(RequirementCriteria criteria, QPLMRequirementDocumentChildren pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            List<Integer> integers = getVersionIds(criteria);
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.requirementVersion.name.containsIgnoreCase(s).
                        or(pathBase.requirementVersion.description.containsIgnoreCase(s)).or(pathBase.requirementVersion.id.in(integers));
                predicates.add(predicate);
            }
            Predicate predicate = pathBase.document.eq(criteria.getRequirementDocument());
            predicates.add(predicate);
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.requirementVersion.master.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getType())) {
            List<Integer> requirements = requirementRepository.getRequirementIdsByType(criteria.getType());
            predicates.add(pathBase.requirementVersion.master.id.in(requirements));
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.requirementVersion.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.requirementVersion.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getAssignedTo())  ) {
            predicates.add(pathBase.document.eq(criteria.getRequirementDocument()).and(pathBase.requirementVersion.assignedTo.in(criteria.getAssignedTo())));
        }
        if (!Criteria.isEmpty(criteria.getPriority())) {
            predicates.add(pathBase.document.eq(criteria.getRequirementDocument()).and(pathBase.requirementVersion.priority.containsIgnoreCase(criteria.getPriority())));
        }
        if (!Criteria.isEmpty(criteria.getPhase())) {
            predicates.add(pathBase.document.eq(criteria.getRequirementDocument()).and(pathBase.requirementVersion.lifeCyclePhase.phase.containsIgnoreCase(criteria.getPhase())));
        }
        predicates.add(pathBase.requirementVersion.latest.eq(true));

        return ExpressionUtils.allOf(predicates);
    }

    private List<Integer> getVersionIds(RequirementCriteria criteria) {
        List<Integer> versionIds = new ArrayList<>();
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = requirementPredicateBuilder.getDefaultPredicate(criteria, QPLMRequirementVersion.pLMRequirementVersion);
        Page<PLMRequirementVersion> versions = requirementVersionRepository.findAll(predicate, pageable);

        for (PLMRequirementVersion version : versions.getContent()) {
            versionIds.add(version.getId());
        }

        return versionIds;
    }

}
