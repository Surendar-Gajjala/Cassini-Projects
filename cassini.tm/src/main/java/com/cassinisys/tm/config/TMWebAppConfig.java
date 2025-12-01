package com.cassinisys.tm.config;

import com.cassinisys.platform.config.WebAppConfig;

public class TMWebAppConfig extends WebAppConfig {

    protected Class getConfigClass() {
        return TMConfig.class;
    }

}
