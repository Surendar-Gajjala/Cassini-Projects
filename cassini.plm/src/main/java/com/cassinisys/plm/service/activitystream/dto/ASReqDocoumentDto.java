package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by subramanyam on 29-07-2021.
 */
@Data
@AllArgsConstructor
public class ASReqDocoumentDto {
    private Integer id;
    private String name;
    private String number;
}
