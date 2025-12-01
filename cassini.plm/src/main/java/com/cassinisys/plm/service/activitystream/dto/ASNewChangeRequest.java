package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by subramanyam on 23-07-2020.
 */
@Data
@AllArgsConstructor
public class ASNewChangeRequest {
    private Integer id;
    private String crNumber;
    private String title;
}
