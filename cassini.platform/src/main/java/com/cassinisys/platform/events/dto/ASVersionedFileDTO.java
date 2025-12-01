package com.cassinisys.platform.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASVersionedFileDTO implements Serializable {
    private Integer id;
    private String name;
    private Integer oldVersion;
    private Integer newVersion;
}
