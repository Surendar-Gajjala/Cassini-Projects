package com.cassinisys.drdo.config;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.config.AppConfig;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.EnumExtender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by subramanyam reddy on 02-10-2018.
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableWebMvc
@EnableJpaRepositories(basePackages = {"com.cassinisys.platform.repo", "com.cassinisys.drdo.repo"})
@PropertySource({"classpath:application.properties"})
@ComponentScan({"com.cassinisys.platform", "com.cassinisys.drdo"})
@EnableAsync
public class DRDOConfig extends AppConfig {

    static {
        EnumExtender.extendEnum(ObjectType.class, DRDOObjectType.class);
    }

    @Override
    protected List<String> getPackagesToScan() {
        return Arrays.asList("com.cassinisys.drdo.model");
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Bean
    public ExecutorService fixedThreadPool() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        return executor;
    }
}
