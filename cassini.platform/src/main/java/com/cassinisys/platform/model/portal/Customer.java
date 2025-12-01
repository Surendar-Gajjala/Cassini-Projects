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
public class Customer extends SaasObject {
    private String name;
    private String description;
    private String phoneNumber;
    private String email;
    private String website;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String activationCode;
    private Boolean active = Boolean.FALSE;
    private Person contact;

    public Customer() {
        setObjectType("CUSTOMER");
    }

}
