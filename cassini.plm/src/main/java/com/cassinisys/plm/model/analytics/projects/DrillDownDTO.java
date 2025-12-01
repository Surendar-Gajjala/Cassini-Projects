package com.cassinisys.plm.model.analytics.projects;

import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.pm.ProgramProjectDto;
import com.cassinisys.plm.model.pm.WBSCountDto;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by smukka on 15-06-2022.
 */
@Data
public class DrillDownDTO {
    Map<String, List<ProgramProjectDto>> ppMap = new ConcurrentHashMap<>();
    Map<String, List<WBSCountDto>> projectsMap = new ConcurrentHashMap<>();
    Map<String, List<PLMTask>> activityMap = new ConcurrentHashMap<>();
    DrillDownMap drillDownMap;
    List<DrillDownMap> listData = new LinkedList<>();
}
