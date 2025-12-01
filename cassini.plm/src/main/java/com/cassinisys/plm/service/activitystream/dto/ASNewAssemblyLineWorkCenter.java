package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by GSR on 16-02-2021.
 */
@Data
@AllArgsConstructor
public class ASNewAssemblyLineWorkCenter {
    private Integer id;
    private String workCenterName;
    private String workCenterNumber;
}
