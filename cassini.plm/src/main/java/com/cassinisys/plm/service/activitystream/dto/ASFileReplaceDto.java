package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by subramanyam on 30-07-2020.
 */
@Data
@AllArgsConstructor
public class ASFileReplaceDto {
    private Integer oldFileId;
    private String oldFileName;
    private Integer newFileId;
    private String newFileName;
}
