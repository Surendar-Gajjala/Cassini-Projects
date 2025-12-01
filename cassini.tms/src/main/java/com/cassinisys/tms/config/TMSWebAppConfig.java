package com.cassinisys.tms.config;

import com.cassinisys.platform.config.WebAppConfig;

/**
 * Created by CassiniSystems on 20-10-2016.
 */
public class TMSWebAppConfig extends WebAppConfig {
	protected Class getConfigClass() {
		return TMSConfig.class;
	}
}
