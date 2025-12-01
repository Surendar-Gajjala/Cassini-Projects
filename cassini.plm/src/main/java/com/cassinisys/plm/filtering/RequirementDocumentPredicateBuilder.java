/*
package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.req.QPLMRequirementDocument;
import com.cassinisys.plm.repo.req.PLMRequirementDocumentTypeRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

@Component
public class RequirementDocumentPredicateBuilder implements PredicateBuilder<RequirementDocumentCriteria, QPLMRequirementDocument> {

    @Autowired
    private PLMRequirementDocumentTypeRepository documentTypeRepository;

    private Predicate getFreeTextSearchPredicate(RequirementDocumentCriteria criteria, QPLMRequirementDocument pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.number.containsIgnoreCase(s).
                        //or(pathBase.type.name.containsIgnoreCase(s)).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);

            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    @Override
    public Predicate build(RequirementDocumentCriteria criteria, QPLMRequirementDocument pathBase) {
        if (criteria.getSearchQuery() != null) {
            Predicate predicate = getFreeTextSearchPredicate(criteria, pathBase);
            return predicate;
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getDefaultPredicate(RequirementDocumentCriteria criteria, QPLMRequirementDocument pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getItemNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getItemNumber()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getItemName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getItemName()));
        }
        */
/*if (!Criteria.isEmpty(criteria.getItemType())) {
            PLMRequirementDocumentType documentType = documentTypeRepository.findOne(Integer.parseInt(criteria.getItemType()));
            predicates.add(pathBase.type.eq(documentType));
        }*//*


        if (!Criteria.isEmpty(criteria.getLatestRevision())) {
            predicates.add(pathBase.latestRevision.eq(parseInt(criteria.getLatestRevision())));
        }
        return ExpressionUtils.allOf(predicates);
    }

}
*/


package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.req.PLMProjectRequirementDocument;
import com.cassinisys.plm.model.req.QPLMRequirementDocument;
import com.cassinisys.plm.repo.req.PLMProjectRequirementDocumentRepository;
import com.cassinisys.plm.repo.req.PLMRequirementDocumentRevisionRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequirementDocumentPredicateBuilder implements PredicateBuilder<RequirementDocumentCriteria, QPLMRequirementDocument> {
    @Autowired
    private PLMProjectRequirementDocumentRepository projectRequirementDocumentRepository;
    @Autowired
    private PLMRequirementDocumentRevisionRepository requirementDocumentRevisionRepository;

    @Override
    public Predicate build(RequirementDocumentCriteria requirementDocumentCriteria, QPLMRequirementDocument pathBase) {
        return getDefaultPredicate(requirementDocumentCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(RequirementDocumentCriteria criteria, QPLMRequirementDocument pathBase) {
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
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }

        if (!Criteria.isEmpty(criteria.getPhase())) {
            List<Integer> ids = requirementDocumentRevisionRepository.getRevisionIdsByPhase(criteria.getPhase());
            predicates.add(pathBase.latestRevision.in(ids));
        }

        if (!Criteria.isEmpty(criteria.getOwner())) {
            List<Integer> ids = requirementDocumentRevisionRepository.getRevisionIdsByOwner(criteria.getOwner());
            predicates.add(pathBase.latestRevision.in(ids));
        }

        if (!Criteria.isEmpty(criteria.getProject())) {
            List<PLMProjectRequirementDocument> projectRequirementDocuments = projectRequirementDocumentRepository.getReqDocumentsByProject(criteria.getProject());
            for (PLMProjectRequirementDocument projectRequirementDocument : projectRequirementDocuments) {
                predicates.add(pathBase.id.ne(projectRequirementDocument.getReqDocument().getMaster().getId()));
            }
        }


        return ExpressionUtils.allOf(predicates);
    }
}
