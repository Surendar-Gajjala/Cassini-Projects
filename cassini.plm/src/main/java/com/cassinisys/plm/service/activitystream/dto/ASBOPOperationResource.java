package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASBOPOperationResource implements Serializable {
    private String resource;
    private String resourceType;
    private String number;
}
