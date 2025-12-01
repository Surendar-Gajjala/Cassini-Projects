package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by subramanyam on 28-07-2020.
 */
@Data
@AllArgsConstructor
public class ASBomConfiguration {
    private Integer id;
    private String name;
    private String description;
    private String oldName;
    private String oldDescription;
    private List<ASBomConfigValues> values;
}
