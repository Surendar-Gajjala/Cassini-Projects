package com.cassinisys.mes.config;

import com.cassinisys.mes.model.MESObjectType;
import com.cassinisys.platform.config.AppConfig;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.EnumExtender;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

/**
 * Created by subramanyamreddy on 15-Feb-17.
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableWebMvc
@EnableJpaRepositories(basePackages = {"com.cassinisys.platform.repo","com.cassinisys.mes.repo"})
@PropertySource({"classpath:application.properties"})
@ComponentScan({"com.cassinisys.platform","com.cassinisys.mes"})
public class MESConfig extends AppConfig{

    static{
        EnumExtender.extendEnum(ObjectType.class, MESObjectType.class);
    }

    @Override
    protected List<String> getPackagesToScan() {
        return Arrays.asList("com.cassinisys.mes.model");
    }
}
