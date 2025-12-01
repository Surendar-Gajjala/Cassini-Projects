package com.cassinisys.platform.config;

import com.cassinisys.platform.filters.JwtTokenFilter;
import com.cassinisys.platform.security.AbacPermissionEvaluator;
import com.cassinisys.platform.security.SecurityEvaluationContextExtension;
import com.cassinisys.platform.security.SecurityMethodSecurityExpressionHandler;
import com.cassinisys.platform.security.jwt.BCryptPasswordEncoder;
import com.cassinisys.platform.security.jwt.JwtAuthEntryPoint;
import com.cassinisys.platform.security.userDetails.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private AbacPermissionEvaluator permissionEvaluator;

    @Bean
    public JwtTokenFilter authenticationJwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(jwtUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService);
    }

    @Bean
    EvaluationContextExtension securityExtension() {
        return new SecurityEvaluationContextExtension();
    }


    @Bean
    MethodSecurityExpressionHandler securityExpressionHandler() {
        SecurityMethodSecurityExpressionHandler securityMethodSecurityExpressionHandler = new SecurityMethodSecurityExpressionHandler();
        securityMethodSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
        return securityMethodSecurityExpressionHandler;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
                http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/security/logout").permitAll()
                .antMatchers("/api/security/checkportal").permitAll()
                .antMatchers("/api/security/login/validate").permitAll()
                .antMatchers("/api/security/login/resetpwd").permitAll()
                .antMatchers("/api/security/login/resetpwd/verify").permitAll()
                .antMatchers("/api/security/login/newpassword").permitAll()
                .antMatchers("/api/security/language/de").permitAll()
                .antMatchers("/api/security/language/en").permitAll()
                .antMatchers("/api/security/session/current").permitAll()
                .antMatchers("/api/core/currencies").permitAll()
                .antMatchers("/api/security/session/isactive").permitAll()
                .antMatchers("/api/app/details").permitAll()
                .antMatchers("/api/common/persons/preferences").permitAll()
                .antMatchers("/api/common/persons/getSessionTime").permitAll()
                .antMatchers("/api/common/persons/getPreferenceByContext/SYSTEM").permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    public Filter jwtTokenFilter(){
       return new JwtTokenFilter();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

}
