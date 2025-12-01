package com.cassinisys.irste.filtering;

import com.cassinisys.irste.model.ComplaintStatus;
import com.cassinisys.irste.model.QIRSTEComplaint;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 06-11-2018.
 */
@Component
public class ComplaintPredicateBuilder implements PredicateBuilder<ComplaintCriteria, QIRSTEComplaint> {

    @Override
    public Predicate build(ComplaintCriteria criteria, QIRSTEComplaint pathBase) {

        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */

    private Predicate getFreeTextSearchPredicate(ComplaintCriteria criteria, QIRSTEComplaint pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.complaintNumber.containsIgnoreCase(s).
                        or(pathBase.location.containsIgnoreCase(s)).
                        or(pathBase.utility.containsIgnoreCase(s)).
                        or(pathBase.groupUtility.containsIgnoreCase(s)).
                        or(pathBase.details.containsIgnoreCase(s));
                predicates.add(predicate);
                if (getComplaintStatusEnum(s.toUpperCase()) != null) {
                    predicates.add(pathBase.status.eq(getComplaintStatusEnum(s.toUpperCase())));
                }
            }
        }

        return ExpressionUtils.anyOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */

    private Predicate getDefaultPredicate(ComplaintCriteria criteria, QIRSTEComplaint pathBase) {

        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getComplaintNumber())) {
            predicates.add(pathBase.complaintNumber.equalsIgnoreCase(criteria.getComplaintNumber()));
        }
        if (!Criteria.isEmpty(criteria.getLocation())) {
            predicates.add(pathBase.location.equalsIgnoreCase(criteria.getLocation()));
        }
        if (!Criteria.isEmpty(criteria.getResponder())) {
            predicates.add(pathBase.responder.eq(Integer.parseInt(criteria.getResponder())));
        }
        if (!Criteria.isEmpty(criteria.getAssistor())) {
            predicates.add(pathBase.assistor.eq(Integer.parseInt(criteria.getAssistor())));
        }
        if (!Criteria.isEmpty(criteria.getFacilitator())) {
            predicates.add(pathBase.facilitator.eq(Integer.parseInt(criteria.getFacilitator())));
        }
        if (!Criteria.isEmpty(criteria.getUtility())) {
            predicates.add(pathBase.groupUtility.equalsIgnoreCase(criteria.getGroupUtility()));
        }
        if (!Criteria.isEmpty(criteria.getUtility())) {
            predicates.add(pathBase.utility.equalsIgnoreCase(criteria.getUtility()));
        }
        if (!Criteria.isEmpty(criteria.getDetails())) {
            predicates.add(pathBase.details.equalsIgnoreCase(criteria.getDetails()));
        }

        if (!Criteria.isEmpty(criteria.getStatus())) {
            predicates.add(pathBase.status.eq(ComplaintStatus.valueOf(criteria.getStatus())));
        }

        return ExpressionUtils.allOf(predicates);
    }

    private ComplaintStatus getComplaintStatusEnum(String val) {
        if (val != null) {
            if (EnumUtils.isValidEnum(ComplaintStatus.class, val)) {
                return ComplaintStatus.valueOf(val);
            }

        }
        return null;
    }
}
