package com.cassinisys.is.config;
/**
 * The class is for ISWebAppConfig
 */

import com.cassinisys.platform.config.WebAppConfig;

public class ISWebAppConfig extends WebAppConfig {

    /**
     * The method used to getConfigClass
     */
    protected Class getConfigClass() {
        return ISConfig.class;
    }

}
