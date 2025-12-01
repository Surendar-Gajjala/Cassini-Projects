package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASBomItem implements Serializable {
    private String number;
    private Integer quantity;
    private String refdes = "";
    private String notes = "";
}
