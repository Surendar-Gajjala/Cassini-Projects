package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASAttributeChangeDTO implements Serializable {
    private String attribute;
    private String oldValue;
    private String newValue;
}
