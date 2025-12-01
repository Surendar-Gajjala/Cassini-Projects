package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by CassiniSystems on 13-06-2020.
 */
public class DCOChangeRequestCriteria extends Criteria {

    private String crNumber;

    private Integer crType;

    private Integer dco;

    private String title;

    public String getCrNumber() {
        return crNumber;
    }

    public void setCrNumber(String crNumber) {
        this.crNumber = crNumber;
    }

    public Integer getCrType() {
        return crType;
    }

    public void setCrType(Integer crType) {
        this.crType = crType;
    }

    public Integer getDco() {
        return dco;
    }

    public void setDco(Integer dco) {
        this.dco = dco;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
