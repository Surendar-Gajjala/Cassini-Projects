package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.core.QSession;
import com.cassinisys.platform.repo.activitystream.ActivityStreamRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by reddy on 9/7/15.
 */
@Component
public class SessionPredicateBuilder implements PredicateBuilder<SessionCriteria, QSession> {
    @Autowired
    private ActivityStreamRepository activityStreamRepository;

    @Override
    public Predicate build(SessionCriteria criteria, QSession pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getLogin())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.login.loginName, criteria.getLogin()));
        }
        if (!Criteria.isEmpty(criteria.getUser())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.login.person.firstName, criteria.getUser()));
        }
        if (!Criteria.isEmpty(criteria.getIpAddress())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.ipAddress, criteria.getIpAddress()));
        }

        Predicate p = Criteria.getDateRangePredicate(pathBase.loginTime, criteria.getLoginTime());
        if (p != null) {
            predicates.add(p);
        }

        p = Criteria.getDateRangePredicate(pathBase.logoutTime, criteria.getLogoutTime());
        if (p != null) {
            predicates.add(p);
        }

        if (!Criteria.isEmpty(criteria.getLoginTime())) {
            try {
                Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getLoginTime());
                Date tomorrow = new Date(dt.getTime() + (1000 * 60 * 60 * 24));

                predicates.add(pathBase.loginTime.after(dt).and(pathBase.loginTime.before(tomorrow)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<Integer> sessionIds = activityStreamRepository.getUniqueSeesionIds();
        predicates.add(pathBase.sessionId.in(sessionIds));

        return ExpressionUtils.allOf(predicates);
    }
}
