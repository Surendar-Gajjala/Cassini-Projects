package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.activitystream.QActivityStream;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyam on 02-05-2020.
 */
@Component
public class ActivityStreamPredicateBuilder implements PredicateBuilder<ActivityStreamCriteria, QActivityStream> {

    @Override
    public Predicate build(ActivityStreamCriteria criteria, QActivityStream pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(ActivityStreamCriteria criteria, QActivityStream activityStream) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getObjectId())) {
            predicates.add(activityStream.object.object.eq(criteria.getObjectId()));
        }

        if (criteria.getObjectIds().size() > 0) {
            predicates.add(activityStream.object.object.in(criteria.getObjectIds()));
        }
        if (!Criteria.isEmpty(criteria.getDate())) {
            try {
                Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getDate());
                Date tomorrow = new Date(dt.getTime() + (1000 * 60 * 60 * 24));

                predicates.add(activityStream.timestamp.after(dt).and(activityStream.timestamp.before(tomorrow)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (!Criteria.isEmpty(criteria.getUser())) {
            predicates.add(activityStream.actor.id.eq(criteria.getUser()));
        }
        if (!Criteria.isEmpty(criteria.getType())) {
            predicates.add(activityStream.object.type.eq(criteria.getType()));
        }

        if (!Criteria.isEmpty(criteria.getAction())) {
            predicates.add(activityStream.activity.startsWith(criteria.getAction()));
        }

        return ExpressionUtils.allOf(predicates);
    }
}
