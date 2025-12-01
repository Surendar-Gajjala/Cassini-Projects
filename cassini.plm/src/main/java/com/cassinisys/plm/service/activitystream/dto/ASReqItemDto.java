package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by subramanyam on 29-07-2021.
 */
@Data
@AllArgsConstructor
public class ASReqItemDto {
    private Integer id;
    private String itemNumber;
    private String itemName;
    private String revision;
    private String phase;
}
