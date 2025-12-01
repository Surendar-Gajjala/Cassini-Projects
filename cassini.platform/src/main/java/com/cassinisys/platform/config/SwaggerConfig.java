package com.cassinisys.platform.config;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.enable}")
    private boolean enable;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).tags(

                new Tag("PLATFORM.COMMON", "Common endpoints"),
                new Tag("PLATFORM.CORE", "Core endpoints"),
                new Tag("PLATFORM.CUSTOM", "Custom endpoints"),
                new Tag("PLATFORM.PLUGIN", "Plugin endpoints"),
                new Tag("PLATFORM.SECURITY", "Security endpoints"),
                new Tag("PLATFORM.NAVIGATION_MENU", "Navigation menu endpoints"),
                new Tag("PLATFORM.WORKFLOW", "Workflow endpoints")

        )
                .select()
                .apis(customRequestHandlers())
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo())
                .enable(enable);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .build();
    }

    private Predicate<RequestHandler> customRequestHandlers() {
        return new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler input) {
                Set<RequestMethod> methods = input.supportedMethods();
                return methods.contains(RequestMethod.GET)
                        || methods.contains(RequestMethod.POST)
                        || methods.contains(RequestMethod.PUT)
                        || methods.contains(RequestMethod.DELETE);
            }
        };
    }

}