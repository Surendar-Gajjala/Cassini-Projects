package com.cassinisys.drdo.config;

import com.cassinisys.platform.config.WebAppConfig;

/**
 * Created by subramanyam reddy on 02-10-2018.
 */
public class DRDOWebConfig extends WebAppConfig {
    protected Class getConfigClass() {
        return DRDOConfig.class;
    }
}
