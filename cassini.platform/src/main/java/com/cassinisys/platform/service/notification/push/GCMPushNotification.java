package com.cassinisys.platform.service.notification.push;

import com.cassinisys.platform.config.AutowiredLogger;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by reddy on 16/02/16.
 */
@Component
public class GCMPushNotification {
    private static final String URL = "https://gcm-http.googleapis.com/gcm/send";
    private static final String SERVER_KEY = "AIzaSyA52LR_LoIMccMKk_nChMXCJqzBLhox2-I";

    @AutowiredLogger
    private Logger LOGGER;

    public void sendMessage(GCMMessage message) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "key=" + SERVER_KEY);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        HttpEntity<GCMMessage> request = new HttpEntity<>(message, headers);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, request, String.class);

        if(response.getStatusCode() != HttpStatus.OK) {
            LOGGER.error("Error sending GCM message", response.getBody());
        }
    }
}
