package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by subramanyam on 12-07-2021.
 */
@Data
@AllArgsConstructor
public class ASStatusChangeDto {
    String fromStatus;
    String toStatus;
}
