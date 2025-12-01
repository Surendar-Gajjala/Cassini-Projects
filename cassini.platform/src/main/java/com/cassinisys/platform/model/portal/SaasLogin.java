package com.cassinisys.platform.model.portal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaasLogin extends SaasObject {
    private String email;
    private String password;
    private Person person;
    @JsonIgnore
    private String verificationCode;
    @JsonIgnore
    private Instant verificationCodeExpiration;
    @JsonIgnore
    private Integer forgotPasswordCode;
    @JsonIgnore
    private Instant forgotPasswordCodeExpiration;
    @JsonIgnore
    private Boolean verified = Boolean.FALSE;

    public SaasLogin() {
    }
}
