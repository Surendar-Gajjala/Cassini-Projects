package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASMbomItemUpdate implements Serializable {
    private String number;
    private String property;
    private String oldValue;
    private String newValue;
}
