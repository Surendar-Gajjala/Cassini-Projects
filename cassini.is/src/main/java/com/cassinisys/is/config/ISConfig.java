package com.cassinisys.is.config;
/**
 * The class is for ISConfig
 */

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.config.AppConfig;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.EnumExtender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebMvc
@EnableJpaRepositories(basePackages = {"com.cassinisys.platform.repo", "com.cassinisys.is.repo"})
@PropertySource({"classpath:application.properties"})
@ComponentScan({"com.cassinisys.platform", "com.cassinisys.is"})
public class ISConfig extends AppConfig {

    static {
        EnumExtender.extendEnum(ObjectType.class, ISObjectType.class);
    }

    /**
     * The method used to getPackagesToScan from the list of String
     */
    @Override
    protected List<String> getPackagesToScan() {
        return Arrays.asList("com.cassinisys.is.model");
    }

    /**
     * The method used for multipartResolver from CommonsMultipartResolver
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
