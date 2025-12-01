package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.mes.QMESMBOM;
import com.cassinisys.plm.repo.cm.MCOProductAffectedItemRepository;
import com.cassinisys.plm.repo.mes.MESMBOMRevisionRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MBOMPredicateBuilder implements PredicateBuilder<MBOMCriteria, QMESMBOM> {

    @Autowired
    private MCOProductAffectedItemRepository mcoProductAffectedItemRepository;
    @Autowired
    private MESMBOMRevisionRepository mesmbomRevisionRepository;

    @Override
    public Predicate build(MBOMCriteria mbomCriteria, QMESMBOM pathBase) {
        return getDefaultPredicate(mbomCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(MBOMCriteria criteria, QMESMBOM pathBase) {
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

        if (!Criteria.isEmpty(criteria.getMco())) {
            List<Integer> mbomIds = mcoProductAffectedItemRepository.getItemIdsByMCO(criteria.getMco());
            if (mbomIds.size() > 0) {
                predicates.add(pathBase.latestRevision.notIn(mbomIds));
            }
            List<Integer> revisionIds = mesmbomRevisionRepository.getInitialRevisionIds();
            revisionIds.addAll(mesmbomRevisionRepository.getLatestRevisionIds());
            revisionIds.addAll(mesmbomRevisionRepository.getLatestReleasedRevisionIds());

            predicates.add(pathBase.latestRevision.in(revisionIds));
        }

        return ExpressionUtils.allOf(predicates);
    }
}