package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 01-10-2019.
 */
@Data
public class ItemBomExport {

    private String itemName;
    private String itemType;
    private String itemNumber;
    private String quantity;
    private String revision;
    private String lifeCyclePhase;
    private String referenceDesignators;
    private String parent;
    private Integer level;
    private String notes;
    private List<ObjectTypeAttribute> attributes = new ArrayList<>();

}
