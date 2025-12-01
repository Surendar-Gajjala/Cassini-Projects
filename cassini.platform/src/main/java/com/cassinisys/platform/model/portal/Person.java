package com.cassinisys.platform.model.portal;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Person extends SaasObject {
    private String firstName;
    private String lastName;
    private String phoneOffice;
    private String phoneMobile;
    private String email;

    @JsonIgnore
    private byte[] image;

    public Person() {
        setObjectType("PERSON");
    }

}
