package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASMROPropertyChangeDTO implements Serializable {
    private String property;
    private String name;
    private String oldValue;
    private String newValue;
}
