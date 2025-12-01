package com.cassinisys.platform.service.notification.sms;

import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by reddy on 9/2/15.
 */
public abstract class SMSObject {
    private final String API_URL = "https://api.smsowl.in/v1/sms";
    private String accountId = "55e6691bbb2704154896c906";
    private String apiKey = "wQdWCNKLxBz6FevqBwsiyozseZdNQH82KJkGdMPjkS67qPTmpTj";
    private String dndType = "transactional";
    private String smsType = "normal";
    private String senderId = "CASSYS";
    private String templateId = null;
    private Map<String, String> placeholders = new HashMap<>();

    private String to;
    private String message;

    public String getAccountId() {
        return accountId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getDndType() {
        return dndType;
    }

    public String getSmsType() {
        return smsType;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Map<String, String> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Map<String, String> placeholders) {
        this.placeholders = placeholders;
    }

    public final void sendTo(String phoneNumber) {
        setTo(phoneNumber);
        RestTemplate restTemplate = getRestTemplate();
        String json = restTemplate.postForObject(API_URL, this, String.class);
        //System.out.println(json);
    }

    public boolean validate() {
        boolean proceed = true;

        if(this.templateId == null || this.templateId.trim().isEmpty()) {
            throw new SMSException("SMS template id cannot be empty");
        }

        return proceed;
    }

    private RestTemplate getRestTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Accept", MediaType.APPLICATION_JSON_VALUE));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", MediaType.APPLICATION_JSON_VALUE));


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());


        return restTemplate;
    }

    private class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

        private final String headerName;
        private final String headerValue;

        public HeaderRequestInterceptor(String headerName, String headerValue) {
            this.headerName = headerName;
            this.headerValue = headerValue;
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            HttpRequest wrapper = new HttpRequestWrapper(request);
            wrapper.getHeaders().set(headerName, headerValue);
            return execution.execute(wrapper, body);
        }
    }
}
