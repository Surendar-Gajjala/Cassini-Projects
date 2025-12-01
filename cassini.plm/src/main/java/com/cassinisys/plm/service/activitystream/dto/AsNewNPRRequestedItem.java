package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Lenovo on 01/12/2021.
 */
@Data
@AllArgsConstructor
public class AsNewNPRRequestedItem {
    private Integer id;
    private String itemType;
    private String itemName;
    private String itemNumber;
    private String revision;
    private String lifecyclePhase;
}
