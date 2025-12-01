package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.model.security.IDtoType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 20-03-2020.
 */
@Data
public class ItemDto implements IDtoType {

    private Integer id;

    private Integer itemType;

    private String itemName;

    private String itemNumber;

    private String description;

    private Integer instance;

    private Boolean configurable = Boolean.FALSE;

    private Boolean configured = Boolean.FALSE;

    private Integer latestRevision;

    private String revision;

    private String lifecycle;

    private String phaseType;

    private String objectType = "item";

    private String subType;
    private String createdByPerson;
    private String modifiedByPerson;
    private List<ItemRevisionDto> itemRevisions = new ArrayList<>();

    public String getObjectType() {
        return "item";
    }
}
