package com.cassinisys.plm.config;

import com.cassinisys.platform.config.SwaggerConfig;
import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Set;

public class PLMSwaggerConfig extends SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).tags(

                new Tag("PLM.ANALYTICS", "Analytics endpoints"),
                new Tag("PLM.CLASSIFICATION", "Classification endpoints"),
                new Tag("PLM.CM", "Changes endpoints"),
                new Tag("PLM.EXIM", "Export Import endpoints"),
                new Tag("PLM.INTEGRATION", "Integration endpoints"),
                new Tag("PLM.MES", "MES endpoints"),
                new Tag("PLM.MFR", "Mfr endpoints"),
                new Tag("PLM.PDM", "PDM endpoints"),
                new Tag("PLM.ITEMS", "Items endpoints"),
                new Tag("PLM.PM", "Project endpoints"),
                new Tag("PLM.PQM", "Quality endpoints"),
                new Tag("PLM.RM", "Requirement endpoints"),
                new Tag("PLM.WF", "Workflow endpoints")

        )
                .select()
                .apis(customRequestHandlers())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CassiniPLM")
                .description("Product LifeCycle Management")
                .version("4.0.0")
                .termsOfServiceUrl("https://www.cassiniplm.com/")
                .license("LICENSE")
                .licenseUrl("http://url-to-license.com")
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