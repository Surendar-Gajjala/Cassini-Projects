package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASMbomItem implements Serializable {
    private String number;
    private String name;
    private Integer quantity;
    private String type;
}
