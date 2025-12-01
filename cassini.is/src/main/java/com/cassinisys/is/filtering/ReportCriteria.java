package com.cassinisys.is.filtering;

/**
 * Created by Nageshreddy on 25-09-2018.
 */
public class ReportCriteria {
    private Integer id;
    private String fromDate;
    private String toDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
