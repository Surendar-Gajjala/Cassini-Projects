package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Lenovo on 02/12/2021.
 */
@Data
@AllArgsConstructor
public class ASNPRRequestItemUpdate {
    private String itemNumber;
    private String property;
    private String oldValue;
    private String newValue;
}
