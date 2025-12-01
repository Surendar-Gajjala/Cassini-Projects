package com.cassinisys.erp.config;

import com.cassinisys.erp.service.security.AuthorizationInterceptor;
import com.cassinisys.erp.service.security.TenantInterceptor;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.jsondoc.core.pojo.JSONDoc.MethodDisplay;
import org.jsondoc.springmvc.controller.JSONDocController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.groovy.GroovyScriptEvaluator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by reddy on 6/26/15.
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableWebMvc
@EnableJpaRepositories(basePackages = "com.cassinisys.erp.repo")
@PropertySource({"classpath:application.properties"})
@ComponentScan({"com.cassinisys.erp"})
public class AppConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private Environment env;

    public AppConfig() {
        super();
    }

    @Override
    public void configureMessageConverters( List<HttpMessageConverter<?>> converters ) {
        converters.add(converter());
    }


    @Bean
    MappingJackson2HttpMessageConverter converter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //converter.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //converter.setObjectMapper(new HibernateAwareObjectMapper());
        return converter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                       MultiTenantConnectionProvider multiTenantConnectionProvider,
                                                                       CurrentTenantIdentifierResolver tenantIdentifierResolver) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(new String[]{"com.cassinisys.erp.model"});

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> map = getJpaPropertiesMap();
        map.put(org.hibernate.cfg.Environment.MULTI_TENANT,
                MultiTenancyStrategy.SCHEMA);
        map.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER,
                multiTenantConnectionProvider);
        map.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER,
                tenantIdentifierResolver);
        em.setJpaPropertyMap(map);

        return em;
    }

    @Bean
    public DataSource dataSource() {
        /*final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(getProperty("jdbc.driverClassName"));
        dataSource.setUrl(getProperty("jdbc.url"));
        dataSource.setUsername(getProperty("jdbc.user"));
        dataSource.setPassword(getProperty("jdbc.pass"));
        */

        final ComboPooledDataSource dataSource;
        try {
            dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(getProperty("jdbc.driverClassName"));
            dataSource.setJdbcUrl(getProperty("jdbc.url"));
            dataSource.setUser(getProperty("jdbc.user"));
            dataSource.setPassword(getProperty("jdbc.pass"));

            dataSource.setMaxPoolSize(50);
            dataSource.setMinPoolSize(10);
            dataSource.setMaxStatements(100);
            dataSource.setTestConnectionOnCheckout(true);

            return dataSource;
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getProperty(String name) {
        String value = System.getProperty(name);
        if(value == null) {
            value = env.getProperty(name);
        }

        return value;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }


    private Map<String, Object> getJpaPropertiesMap() {
        final Map<String, Object> props = new HashMap<>();

        props.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        props.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        props.put("hibernate.default_schema", env.getProperty("hibernate.default_schema"));
        props.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));

        props.put("hibernate.multiTenancy", env.getProperty("hibernate.multiTenancy"));
        props.put("hibernate.tenant_identifier_resolver", env.getProperty("hibernate.tenant_identifier_resolver"));
        props.put("hibernate.multi_tenant_connection_provider", env.getProperty("hibernate.multi_tenant_connection_provider"));

        props.put("hibernate.c3p0.timeout", env.getProperty("hibernate.c3p0.timeout"));
        props.put("hibernate.c3p0.maxIdleTimeExcessConnections", env.getProperty("hibernate.c3p0.maxIdleTimeExcessConnections"));
        props.put("hibernate.c3p0.min_size", env.getProperty("hibernate.c3p0.min_size"));
        props.put("hibernate.c3p0.max_size", env.getProperty("hibernate.c3p0.max_size"));
        props.put("hibernate.c3p0.acquire_increment", env.getProperty("hibernate.c3p0.acquire_increment"));
        props.put("hibernate.c3p0.max_statements", env.getProperty("hibernate.c3p0.max_statements"));
        props.put("hibernate.c3p0.idle_test_period", env.getProperty("hibernate.c3p0.idle_test_period"));
        props.put("hibernate.c3p0.automaticTestTable", env.getProperty("hibernate.c3p0.automaticTestTable"));



        return props;
    }
    
    @Bean
	public JSONDocController jsonDocController() {
		String version = "1.0";
		String basePath = "http://localhost:8080/api/";
		List<String> packages = new ArrayList<String>();
		packages.add("com.cassinisys.erp.api");
		packages.add("com.cassinisys.erp.model");
		JSONDocController docController = new JSONDocController(version,
				basePath, packages);
		docController.setPlaygroundEnabled(true);
		docController.setDisplayMethodAs(MethodDisplay.URI);
		return docController;
	}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new APIInterceptor());TenantInterceptor
        registry.addInterceptor(new TenantInterceptor());
        registry.addInterceptor(getAuthorizationInterceptor());
    }

    @Bean
    public ScriptEvaluator groovyScriptEvaluator() {
        return new GroovyScriptEvaluator();
    }

    @Bean
    public AuthorizationInterceptor getAuthorizationInterceptor() {
        return new AuthorizationInterceptor();
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
}
