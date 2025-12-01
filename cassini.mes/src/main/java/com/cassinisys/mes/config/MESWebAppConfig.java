package com.cassinisys.mes.config;

import com.cassinisys.platform.config.WebAppConfig;

/**
 * Created by subramanyamreddy on 15-Feb-17.
 */
public class MESWebAppConfig extends WebAppConfig{
    protected Class getConfigClass() {
        return MESConfig.class;
    }
}
