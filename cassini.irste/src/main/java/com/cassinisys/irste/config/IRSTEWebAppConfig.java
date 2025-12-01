package com.cassinisys.irste.config;

import com.cassinisys.platform.config.WebAppConfig;

public class IRSTEWebAppConfig extends WebAppConfig {

    protected Class getConfigClass() {
        return IRSTEConfig.class;
    }
}