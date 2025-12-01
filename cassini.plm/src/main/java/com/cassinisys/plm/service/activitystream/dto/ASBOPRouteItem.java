package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASBOPRouteItem implements Serializable {
    private String sequenceNumber;
    private String name;
    private String number;
}
