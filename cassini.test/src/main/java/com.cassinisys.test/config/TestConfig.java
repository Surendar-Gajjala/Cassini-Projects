package com.cassinisys.test.config;

import com.cassinisys.platform.config.AppConfig;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.EnumExtender;
import com.cassinisys.test.model.TestObjectType;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableWebMvc
@EnableJpaRepositories(basePackages = {"com.cassinisys.platform.repo", "com.cassinisys.test.repo"})
@PropertySource({"classpath:application.properties"})
@ComponentScan({"com.cassinisys.platform", "com.cassinisys.test"})
public class TestConfig extends AppConfig {

    static {
        EnumExtender.extendEnum(ObjectType.class, TestObjectType.class);
    }

    @Override
    protected List<String> getPackagesToScan() {
        return Arrays.asList("com.cassinisys.test.model");
    }
}
