package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.model.security.IDtoType;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import lombok.Data;

/**
 * Created by subramanyam on 20-03-2020.
 */
@Data
public class ItemRevisionDto implements IDtoType {

    private Integer id;
    private Integer itemMaster;
    private String revision;
    private PLMLifeCyclePhase lifeCyclePhase;
    private String objectType = "itemrevision";

    public String getObjectType() {
        return "itemrevision";
    }
}
