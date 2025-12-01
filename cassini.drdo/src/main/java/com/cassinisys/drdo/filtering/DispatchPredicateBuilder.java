package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.transactions.DispatchStatus;
import com.cassinisys.drdo.model.transactions.QDispatch;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyam reddy on 27-11-2018.
 */
@Component
public class DispatchPredicateBuilder implements PredicateBuilder<DispatchCriteria, QDispatch> {

    @Override
    public Predicate build(DispatchCriteria criteria, QDispatch dispatch) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            predicates.add(dispatch.number.containsIgnoreCase(criteria.getSearchQuery())
//                    .or(dispatch.bom.item.itemMaster.itemName.containsIgnoreCase(criteria.getSearchQuery()))
                    .or(dispatch.gatePassNumber.containsIgnoreCase(criteria.getSearchQuery())));
        }

        List<Predicate> predicates1 = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getStatus())) {
            predicates1.add(dispatch.status.eq(DispatchStatus.valueOf(criteria.getStatus())));
        }

        if (criteria.getNotification()) {
            predicates1.add(dispatch.status.ne(DispatchStatus.DISPATCHED));
        }

        List<Predicate> predicates2 = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getFromDate()) && !Criteria.isEmpty(criteria.getToDate())) {
            try {
                Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getToDate());
                Date tomorrow = new Date(dt.getTime() + (1000 * 60 * 60 * 24));
                predicates2.add(dispatch.createdDate.after(new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getFromDate())));
                predicates2.add(dispatch.createdDate.before(tomorrow));
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }
        }

        if (!Criteria.isEmpty(criteria.getMonth())) {
            try {
                Date dt = new SimpleDateFormat("MM/yyyy").parse(criteria.getMonth());
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                String firstDate = dateFormat.format(dt);

                LocalDate lastDate = LocalDate.parse(firstDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                lastDate = lastDate.withDayOfMonth(lastDate.getMonth().length(lastDate.isLeapYear()));

                Date lastDateOfMonth = java.sql.Date.valueOf(lastDate);
                Date tomorrow = new Date(lastDateOfMonth.getTime() + (1000 * 60 * 60 * 24));

                predicates2.add(dispatch.createdDate.after(dt));
                predicates2.add(dispatch.createdDate.before(tomorrow));
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }
        }


        Predicate predicate = ExpressionUtils.and(ExpressionUtils.anyOf(predicates), ExpressionUtils.anyOf(predicates1));
        return ExpressionUtils.and(ExpressionUtils.anyOf(predicate), ExpressionUtils.allOf(predicates2));
    }
}
