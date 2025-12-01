package com.cassinisys.erp.scripting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by reddy on 9/14/15.
 */
@Component
public class RepositoriesBean {
    @Autowired
    public CRMRepositories crm;
}
