package com.cassinisys.erp.api.filtering;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.StringPath;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Criteria {
    public static boolean isEmpty(String s) {
        if (s == null || s.trim().isEmpty() ||
                s.equalsIgnoreCase("null") ||
                s.equalsIgnoreCase("undefined")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(Integer number) {
        if (number == null) {
            return true;
        } else {
            return false;
        }
    }

    public static Predicate getSinglePredicate(StringPath path, String term) {
        return path.containsIgnoreCase(term);
    }

    public static Predicate getMultiplePredicates(StringPath path, String terms) {
        List<Predicate> predicates = new ArrayList<>();

        String arr[] = terms.trim().split(" ");
        for (String term : arr) {
            predicates.add(path.isNotNull().and(path.containsIgnoreCase(term.trim())));
        }

        return ExpressionUtils.allOf(predicates);
    }


    public static Predicate getDatePredicate(DateTimePath<Date> dateTimePath, String value) {
        if (value != null) {
            if (value.indexOf(':') == -1) {
                return getNonRangeDatePredicate(dateTimePath, value);
            } else {
                String[] arr = value.split(":");
                if (!isEmpty(arr[0]) && !isEmpty(arr[1])) {
                    return getDateRangePredicate(dateTimePath, value);
                }
            }
        }

        return null;
    }

    public static Predicate getNonRangeDatePredicate(DateTimePath<Date> dateTimePath, String value) {
        Predicate predicate = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        if (!Criteria.isEmpty(value)) {
            try {
                String s = value.trim();
                Date d = format.parse(s.substring(1));
                if (s.startsWith("=")) {
                    predicate = dateTimePath.eq(d);
                }
                if (s.startsWith(">")) {
                    predicate = dateTimePath.gt(d);
                }
                if (s.startsWith("<")) {
                    predicate = dateTimePath.lt(d);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Predicate getDateRangePredicate(DateTimePath<Date> dateTimePath, String value) {
        if (!Criteria.isEmpty(value) &&
                value.indexOf(':') != -1) {
            try {
                String s = value;
                String left = s.split(":")[0].trim();
                String right = s.split(":")[1].trim();

                if (!Criteria.isEmpty(left) && !Criteria.isEmpty(right)) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                    Date lDate = format.parse(left);
                    Date rDate = format.parse(right);

                    lDate = new DateTime(lDate).withTime(0, 0, 0, 0).toDate();
                    rDate = new DateTime(rDate).withTime(23, 59, 59, 999).toDate();

                    return dateTimePath.goe(lDate).and(dateTimePath.loe(rDate));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Predicate getDatePredicate(DatePath<Date> datePath, String value) {
        if (value != null) {
            if (value.indexOf(':') == -1) {
                return getNonRangePredicate(datePath, value);
            } else {
                String[] arr = value.split(":");
                if (!isEmpty(arr[0]) && !isEmpty(arr[1])) {
                    return getDateRangePredicate(datePath, value);
                }

            }
        }
        return null;
    }

    public static Predicate getNonRangePredicate(DatePath<Date> datePath, String value) {
        Predicate predicate = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        if (!Criteria.isEmpty(value)) {
            try {
                String s = value.trim();
                Date d = format.parse(s.substring(1));
                if (s.startsWith("=")) {
                    predicate = datePath.eq(d);
                }
                if (s.startsWith(">")) {
                    predicate = datePath.gt(d);
                }
                if (s.startsWith("<")) {
                    predicate = datePath.lt(d);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Predicate getDateRangePredicate(DatePath<Date> datePath, String value) {
        if (!Criteria.isEmpty(value) &&
                value.indexOf(':') != -1) {
            try {
                String s = value;
                String left = s.split(":")[0].trim();
                String right = s.split(":")[1].trim();

                if (!Criteria.isEmpty(left) && !Criteria.isEmpty(right)) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                    Date lDate = format.parse(left);
                    Date rDate = format.parse(right);

                    lDate = new DateTime(lDate).withTime(0, 0, 0, 0).toDate();
                    rDate = new DateTime(rDate).withTime(23, 59, 59, 999).toDate();

                    return datePath.goe(lDate).and(datePath.loe(rDate));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}





