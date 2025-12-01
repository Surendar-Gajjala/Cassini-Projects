package com.cassinisys.documents.config;
/**
 * The class is for DMWebAppConfig
 */

import com.cassinisys.platform.config.WebAppConfig;

public class DMWebAppConfig extends WebAppConfig {

    /**
     * The method used to getConfigClass
     */
    protected Class getConfigClass() {
        return DMConfig.class;
    }

}
