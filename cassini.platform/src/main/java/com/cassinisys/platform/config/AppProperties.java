package com.cassinisys.platform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author reddy
 */
@Component
@PropertySource("classpath:application.properties")
public class AppProperties {

	@Autowired
	private Environment env;

	public String getProperty(String name) {
		return env.getProperty(name);
	}
	
	public String getStorePath() {
		return getProperty("cassiniis.store.path");
	}
}
