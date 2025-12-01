package com.cassinisys.platform.model.portal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use= JsonTypeInfo.Id.NONE)
public class CustomerLogin extends SaasLogin {
    private Customer customer;

    public CustomerLogin() {
        setObjectType("CUSTOMERLOGIN");
    }
}
