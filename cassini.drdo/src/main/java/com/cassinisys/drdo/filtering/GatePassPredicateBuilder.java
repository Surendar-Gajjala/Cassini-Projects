package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.transactions.QGatePass;
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
 * Created by Nageshreddy on 27-10-2018.
 */
@Component
public class GatePassPredicateBuilder implements PredicateBuilder<GatePassCriteria, QGatePass> {

    @Override
    public Predicate build(GatePassCriteria criteria, QGatePass pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(GatePassCriteria criteria, QGatePass pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.gatePass.name.containsIgnoreCase(s).
                        or(pathBase.gatePassNumber.containsIgnoreCase(s).and(pathBase.finish.eq(criteria.getFinish())));
                predicates.add(predicate);
            }
        }

        List<Predicate> predicates2 = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getFromDate()) && !Criteria.isEmpty(criteria.getToDate())) {
            try {
                Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getToDate());
                Date tomorrow = new Date(dt.getTime() + (1000 * 60 * 60 * 24));
                predicates2.add(pathBase.createdDate.after(new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getFromDate())));
                predicates2.add(pathBase.createdDate.before(tomorrow));
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }

        }

        predicates2.add(pathBase.finish.eq(criteria.getFinish()));

        return ExpressionUtils.and(ExpressionUtils.allOf(predicates), ExpressionUtils.allOf(predicates2));
    }

    public Predicate getDefaultPredicate(GatePassCriteria criteria, QGatePass pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            predicates.add(pathBase.gatePass.name.containsIgnoreCase(criteria.getSearchQuery())
                    .or(pathBase.gatePassNumber.containsIgnoreCase(criteria.getSearchQuery())));
        }

        if (!Criteria.isEmpty(criteria.getGatePassNumber())) {
            predicates.add(pathBase.gatePassNumber.containsIgnoreCase(criteria.getGatePassNumber()));
        }

        if (!Criteria.isEmpty(criteria.getGatePassName())) {
            predicates.add(pathBase.gatePass.name.containsIgnoreCase(criteria.getGatePassNumber()));
        }

        predicates.add(pathBase.finish.eq(criteria.getFinish()));

        List<Predicate> predicates2 = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getFromDate()) && !Criteria.isEmpty(criteria.getToDate())) {
            try {
                Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getToDate());
                Date tomorrow = new Date(dt.getTime() + (1000 * 60 * 60 * 24));
                predicates2.add(pathBase.createdDate.after(new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getFromDate())));
                predicates2.add(pathBase.createdDate.before(tomorrow));
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

                predicates2.add(pathBase.createdDate.after(dt));
                predicates2.add(pathBase.createdDate.before(tomorrow));
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }
        }

        return ExpressionUtils.and(ExpressionUtils.allOf(predicates), ExpressionUtils.allOf(predicates2));
    }


}
