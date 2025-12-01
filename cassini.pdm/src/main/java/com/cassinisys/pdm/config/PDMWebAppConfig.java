package com.cassinisys.pdm.config;

import com.cassinisys.platform.config.WebAppConfig;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
public class PDMWebAppConfig extends WebAppConfig{

    protected Class getConfigClass(){ return PDMAppConfig.class;}
}
