package com.cassinisys.platform.service.activitystream;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASPropertyChangeDTO implements Serializable {
    private String property;
    private String oldValue;
    private String newValue;
}
