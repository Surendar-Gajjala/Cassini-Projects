package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASNewAuditPlanDTO implements Serializable {
    private Integer id;
    private String supplierName;
}
