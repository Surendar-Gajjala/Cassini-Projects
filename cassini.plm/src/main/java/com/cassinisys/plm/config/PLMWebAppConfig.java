package com.cassinisys.plm.config;

import com.cassinisys.platform.config.WebAppConfig;

public class PLMWebAppConfig extends WebAppConfig {

    protected Class getConfigClass() {
        return PLMConfig.class;
    }
}
