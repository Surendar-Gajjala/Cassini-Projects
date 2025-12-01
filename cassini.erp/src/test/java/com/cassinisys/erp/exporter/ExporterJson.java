package com.cassinisys.erp.exporter;

import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.model.security.ERPLogin;

import java.util.List;

/**
 * Created by reddy on 11/8/15.
 */
public class ExporterJson {
    private List<ERPEmployee> employees;
    private List<ERPLogin> logins;

    public ExporterJson() {

    }

    public List<ERPEmployee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<ERPEmployee> employees) {
        this.employees = employees;
    }

    public List<ERPLogin> getLogins() {
        return logins;
    }

    public void setLogins(List<ERPLogin> logins) {
        this.logins = logins;
    }
}
