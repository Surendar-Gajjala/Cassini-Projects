package com.cassinisys.platform.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ASFileReplaceDto {
    private Integer oldFileId;
    private String oldFileName;
    private Integer newFileId;
    private String newFileName;
}
