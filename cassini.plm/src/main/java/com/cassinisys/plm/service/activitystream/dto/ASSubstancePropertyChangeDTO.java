package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASSubstancePropertyChangeDTO implements Serializable {
    private String property;
    private String name;
    private String partNumber;
    private String oldValue;
    private String newValue;
}
