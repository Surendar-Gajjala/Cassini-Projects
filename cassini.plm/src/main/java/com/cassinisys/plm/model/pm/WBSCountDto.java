package com.cassinisys.plm.model.pm;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 20-01-2020.
 */
@Data
public class WBSCountDto {

    private Integer id;

    private String name;

    private List<WBSCountDto> children = new ArrayList<>();
    private List<PLMTask> activityTasks = new ArrayList<>();
}
