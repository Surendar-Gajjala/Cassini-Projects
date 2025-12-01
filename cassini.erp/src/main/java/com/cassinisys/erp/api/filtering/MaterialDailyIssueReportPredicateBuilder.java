package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.api.criteria.MaterialDailyIssueReportCriteria;
import com.cassinisys.erp.model.production.QERPMaterialDailyIssueReport;
import com.cassinisys.erp.repo.production.MaterialRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 22-08-2018.
 */
@Component
public class MaterialDailyIssueReportPredicateBuilder implements PredicateBuilder<MaterialDailyIssueReportCriteria, QERPMaterialDailyIssueReport> {

    @Autowired
    private MaterialRepository materialRepository;

    @Override
    public Predicate build(MaterialDailyIssueReportCriteria criteria, QERPMaterialDailyIssueReport pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(MaterialDailyIssueReportCriteria criteria, QERPMaterialDailyIssueReport pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = null;
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(MaterialDailyIssueReportCriteria criteria, QERPMaterialDailyIssueReport pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        Predicate p = Criteria.getDatePredicate(pathBase.timestamp, criteria.getTimestamp());
        if (p != null) {
            predicates.add(p);
        }

        if (!Criteria.isEmpty(criteria.getConsumeQty()) && criteria.getConsumeQty() != null) {
            predicates.add(pathBase.consumeQty.eq(Integer.parseInt(criteria.getConsumeQty())));
        }

        if (!Criteria.isEmpty(criteria.getIssuedQty()) && criteria.getIssuedQty() != null) {
            predicates.add(pathBase.quantity.eq(Integer.parseInt(criteria.getIssuedQty())));
        }

        if (!Criteria.isEmpty(criteria.getRemainingQty()) && criteria.getRemainingQty() != null) {
            predicates.add(pathBase.remainingQty.eq(Integer.parseInt(criteria.getRemainingQty())));
        }

        if (!Criteria.isEmpty(criteria.getSku())) {
            List<Integer> ids = materialRepository.findBySkuContatins(criteria.getSku());
            predicates.add(pathBase.material.in(ids));
        }

        if (!Criteria.isEmpty(criteria.getName())) {
            List<Integer> ids = materialRepository.findByNameContatins(criteria.getName());
            predicates.add(pathBase.material.in(ids));
        }

        return ExpressionUtils.allOf(predicates);

    }
}
