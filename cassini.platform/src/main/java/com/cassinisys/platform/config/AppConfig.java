package com.cassinisys.platform.config;

import com.cassinisys.platform.service.security.AuthorizationInterceptor;
import com.cassinisys.platform.service.security.TenantInterceptor;
import com.cassinisys.platform.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.groovy.GroovyScriptEvaluator;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.sql.DataSource;
import java.util.*;

/**
 * Created by reddy on 6/26/15.
 */
public abstract class AppConfig extends WebMvcConfigurationSupport {
    @Autowired
    private Environment env;

    public AppConfig() {
        super();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(converter());
    }


    @Bean
    DispatcherServlet dispatcherServlet() {
        DispatcherServlet srvl = new DispatcherServlet();
        srvl.setThreadContextInheritable(true);
        return srvl;
    }


    @Bean
    MappingJackson2HttpMessageConverter converter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //converter.getObjectMapper().registerModule(new JavaTimeModule());
        //converter.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //converter.setObjectMapper(new HibernateAwareObjectMapper());
        return converter;
    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JsonUtils.setObjectMapper(objectMapper);
        return objectMapper;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                       MultiTenantConnectionProvider multiTenantConnectionProvider,
                                                                       CurrentTenantIdentifierResolver tenantIdentifierResolver) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);

        List<String> packages = new ArrayList<>(getPackagesToScan());
        packages.add("com.cassinisys.platform.model");
        em.setPackagesToScan(packages.toArray(new String[0]));

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

    protected abstract List<String> getPackagesToScan();

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(getProperty("jdbc.driverClassName"));
        dataSource.setUrl(getProperty("jdbc.url"));
        dataSource.setUsername(getProperty("jdbc.user"));
        dataSource.setPassword(getProperty("jdbc.pass"));

        return dataSource;
    }

    private String getProperty(String name) {
        String value = System.getProperty(name);
        if (value == null) {
            value = env.getProperty(name);
        }

        return value;
    }

    @Bean
    @Autowired
    public EntityManager entityManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        EntityManager em = localContainerEntityManagerFactoryBean.getNativeEntityManagerFactory().createEntityManager();
        em.setFlushMode(FlushModeType.AUTO);
        return em;
    }

    @Bean
    @Autowired
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) throws Exception {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf.getObject());
        // The below line would generate javax.persistence.TransactionRequiredException: no transaction is in progress
        // transactionManager.setEntityManagerFactory(emf.getNativeEntityManagerFactory());
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = super.requestMappingHandlerMapping();
        return handlerMapping;
    }

    public Map<String, Object> getJpaPropertiesMap() {
        final Map<String, Object> props = new HashMap<>();

        props.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        props.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        props.put("hibernate.default_schema", env.getProperty("hibernate.default_schema"));
        props.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        props.put("hibernate.jdbc.log.warnings", env.getProperty("hibernate.jdbc.log.warnings"));

        props.put("hibernate.multiTenancy", env.getProperty("hibernate.multiTenancy"));
        props.put("hibernate.tenant_identifier_resolver", env.getProperty("hibernate.tenant_identifier_resolver"));
        props.put("hibernate.multi_tenant_connection_provider", env.getProperty("hibernate.multi_tenant_connection_provider"));

        props.put("hibernate.c3p0.min_size", env.getProperty("hibernate.c3p0.min_size"));
        props.put("hibernate.c3p0.max_size", env.getProperty("hibernate.c3p0.min_size"));
        props.put("hibernate.c3p0.acquire_increment", env.getProperty("hibernate.c3p0.min_size"));
        props.put("hibernate.c3p0.max_statements", env.getProperty("hibernate.c3p0.min_size"));
        props.put("hibernate.c3p0.idle_test_period", env.getProperty("hibernate.c3p0.min_size"));


        return props;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new APIInterceptor());
        //registry.addInterceptor(new TenantInterceptor());
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
        CommonsMultipartResolver multipartResolver
                = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(2048576000);
        return multipartResolver;
    }

    @Bean
    public JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setUsername(getProperty("mail.username"));
        mailSender.setPassword(getProperty("mail.password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.host", getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", getProperty("mail.smtp.port"));
        props.put("mail.smtp.ssl.trust", getProperty("mail.smtp.ssl.trust"));
        props.put("mail.smtp.from", getProperty("mail.username"));

        return mailSender;
    }

    @Bean
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean fmConfigFactoryBean = new FreeMarkerConfigurationFactoryBean();
        fmConfigFactoryBean.setTemplateLoaderPath("/templates/");
        return fmConfigFactoryBean;
    }

    @Bean
    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("This is the test email template for your email:\n%s\n");
        return message;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH); // change this
        return localeResolver;
    }
}
