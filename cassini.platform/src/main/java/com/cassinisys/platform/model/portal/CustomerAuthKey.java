package com.cassinisys.platform.model.portal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAuthKey implements Serializable {
    private Long customer;
    private String authKey;
}
