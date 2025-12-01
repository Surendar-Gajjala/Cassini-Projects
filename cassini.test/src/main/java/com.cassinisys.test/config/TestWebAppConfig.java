package com.cassinisys.test.config;

import com.cassinisys.platform.config.WebAppConfig;

public class TestWebAppConfig extends WebAppConfig {

    protected Class getConfigClass() {
        return TestConfig.class;
    }
}
