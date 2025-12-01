package com.cassinisys.erp.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

/**
 * Created by reddy on 12/8/14.
 */
public class HibernateAwareObjectMapper extends ObjectMapper {
    public HibernateAwareObjectMapper() {
        super.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        registerModule(new Hibernate4Module());
    }
}
