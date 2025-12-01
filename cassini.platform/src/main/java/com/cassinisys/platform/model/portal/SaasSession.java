package com.cassinisys.platform.model.portal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@type")
public class SaasSession implements Serializable {
    private Long id;
    private String ipAddress;
    private Instant loginTime = Instant.now();
    private Instant logoutTime;
    private String userAgent;
}
