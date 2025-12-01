package com.cassinisys.tms.config;

import com.cassinisys.platform.config.AppConfig;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.EnumExtender;
import com.cassinisys.tms.model.TMSObjectType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

/**
 * Created by CassiniSystems on 20-10-2016.
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableWebMvc
@EnableJpaRepositories(basePackages = {"com.cassinisys.platform.repo", "com.cassinisys.tms.repo"})
@PropertySource({"classpath:application.properties"})
@ComponentScan({"com.cassinisys.platform","com.cassinisys.tms"})
public class TMSConfig extends AppConfig {

	static {
		EnumExtender.extendEnum(ObjectType.class, TMSObjectType.class);
	}
	@Override
	protected List<String> getPackagesToScan() {
		return Arrays.asList("com.cassinisys.tms.model");
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		return new CommonsMultipartResolver();
	}
}
