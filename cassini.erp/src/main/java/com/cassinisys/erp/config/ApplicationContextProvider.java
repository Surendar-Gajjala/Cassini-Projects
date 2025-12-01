package com.cassinisys.erp.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by reddy on 8/23/15.
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware{
    private static ApplicationContext ctx = null;

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    public void setApplicationContext(ApplicationContext c) throws BeansException {
        ctx = c;
    }
}
