package com.cassinisys.platform.model.portal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use= JsonTypeInfo.Id.NONE)
public class CustomerUser extends SaasObject {
    private Long customer;
    private String username;
    private Boolean admin = Boolean.FALSE;
    private Boolean active = Boolean.TRUE;
    private Instant lastLogin = Instant.now();

    public CustomerUser() {
        setObjectType("CUSTOMERUSER");
    }
}
