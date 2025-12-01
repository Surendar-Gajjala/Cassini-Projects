package com.cassinisys.platform.service.portal;

import com.cassinisys.platform.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient(new okhttp3.OkHttpClient.Builder()
                .addInterceptor(new SessionIdRequestInterceptor())
                .build());
    }

    @Bean
    public PortalService portalService() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JsonUtils.setObjectMapper(objectMapper);

        return Feign.builder()
                .client(okHttpClient())
                .encoder(new JacksonEncoder(JsonUtils.getObjectMapper()))
                .decoder(new JacksonDecoder(JsonUtils.getObjectMapper()))
                .errorDecoder(new PortalErrorDecoder())
                .target(PortalService.class, "http://portal.cassiniplm.com/saas/api");
    }
}
