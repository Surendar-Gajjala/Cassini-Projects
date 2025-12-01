package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.api.criteria.SessionCriteria;
import com.cassinisys.erp.model.security.QERPSession;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 9/7/15.
 */
@Component
public class SessionPredicateBuilder implements PredicateBuilder<SessionCriteria, QERPSession> {
    @Override
    public Predicate build(SessionCriteria criteria, QERPSession pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if(!Criteria.isEmpty(criteria.getLogin())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.login.loginName, criteria.getLogin()));
        }
        if(!Criteria.isEmpty(criteria.getUser())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.login.person.firstName, criteria.getUser()));
        }
        if(!Criteria.isEmpty(criteria.getIpAddress())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.ipAddress, criteria.getIpAddress()));
        }

        Predicate p = Criteria.getDateRangePredicate(pathBase.loginTime, criteria.getLoginTime());
        if(p != null) {
            predicates.add(p);
        }

        p = Criteria.getDateRangePredicate(pathBase.logoutTime, criteria.getLogoutTime());
        if(p != null) {
            predicates.add(p);
        }


        return ExpressionUtils.allOf(predicates);
    }
}
