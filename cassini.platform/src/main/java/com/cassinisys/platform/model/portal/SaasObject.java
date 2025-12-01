package com.cassinisys.platform.model.portal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@type")
public class SaasObject implements Serializable {
    private Long id;
    private String objectType;
    private Long createdBy = 0L;
    private Instant createdDate = Instant.now();
    private Long modifiedBy = 0L;
    private Instant modifiedDate = Instant.now();
    private Boolean deleted = Boolean.FALSE;
}
