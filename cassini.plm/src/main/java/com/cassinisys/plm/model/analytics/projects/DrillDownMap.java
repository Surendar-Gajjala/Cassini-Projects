package com.cassinisys.plm.model.analytics.projects;

import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.pm.ProgramProjectDto;
import com.cassinisys.plm.model.pm.WBSDto;
import lombok.Data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by spolamreddy on 10-08-2022.
 */
@Data
public class DrillDownMap {
    private String dataGroupId;
    private List<Object> data;
    List<DrillDownMap> list = new LinkedList<>();
    private String finalLevel;
    private String colorByLevel;
    private String legend;
    private Boolean stage = Boolean.FALSE;
    private Boolean confirmedPath = Boolean.FALSE;
}
