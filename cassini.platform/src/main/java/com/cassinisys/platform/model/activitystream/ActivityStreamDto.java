package com.cassinisys.platform.model.activitystream;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by subramanyam on 02-07-2020.
 */
@Data
public class ActivityStreamDto {
    private Map<String, List<ActivityStream>> histories = new LinkedHashMap<>();
    private Boolean first = Boolean.FALSE;
    private Boolean last = Boolean.FALSE;
}
