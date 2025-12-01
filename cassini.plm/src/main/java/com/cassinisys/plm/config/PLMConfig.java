package com.cassinisys.plm.config;

import com.cassinisys.platform.config.AppConfig;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.EnumExtender;
import com.cassinisys.plm.model.mes.MESEnumObject;
import com.cassinisys.plm.model.mro.MROEnumObject;
import com.cassinisys.plm.model.pdm.PDMObjectType;
import com.cassinisys.plm.model.pgc.PGCEnumObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.req.RequirementEnumObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableJpaRepositories(basePackages = {"com.cassinisys.platform.repo", "com.cassinisys.plm.repo"})
@PropertySource(value = {"classpath:application.properties"})
@ComponentScan(basePackages = {"com.cassinisys.platform", "com.cassinisys.plm"})
@EnableScheduling
public class PLMConfig extends AppConfig {
    public static String schema;

    static {
        EnumExtender.extendEnum(ObjectType.class, PLMObjectType.class);
        EnumExtender.extendEnum(ObjectType.class, PDMObjectType.class);
        EnumExtender.extendEnum(ObjectType.class, MESEnumObject.class);
        EnumExtender.extendEnum(ObjectType.class, MROEnumObject.class);
        EnumExtender.extendEnum(ObjectType.class, PGCEnumObject.class);
        EnumExtender.extendEnum(ObjectType.class, RequirementEnumObject.class);
    }

    @Value("${cassini.default.tenantid}")
    private String defaultSchema;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Override
    protected List<String> getPackagesToScan() {
        this.schema = this.defaultSchema;
        return Arrays.asList("com.cassinisys.plm.model", "com.emm.integrations");
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
}
