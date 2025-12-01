package com.cassinisys.plm.model.dto;

import com.cassinisys.plm.model.pm.PLMActivity;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.PLMTask;
import lombok.Data;

/**
 * Created by CassiniSystems on 09-08-2019.
 */
@Data
public class ProjectItemsDto {

    PLMProject project;
    PLMActivity activity;
    PLMTask task;

}
