package com.cassinisys.plm.model.mes.dto;

import com.cassinisys.plm.model.mes.MESBomItemType;
import com.cassinisys.plm.model.mes.MESEnumObject;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by smukka on 07-10-2022.
 */
@Data
public class MBOMInstanceItemDto {
    private Integer id;
    private Integer mbomInstance;
    private Integer mbomItem;
    private Integer parent;
    private MESBomItemType type;
    private Integer quantity;

    private String itemName;
    private String itemNumber;
    private String itemTypeName;
    private String revision;
    private Integer asReleasedRevision;
    private Boolean hasBom = false;
    private Boolean itemRevisionHasBom = false;
    private String phantomNumber;
    private String phantomName;
    private MESEnumObject objectType = MESEnumObject.MBOMINSTANCEITEM;
    private Integer consumedQty = 0;
    private Integer producedQty = 0;
    private Boolean alreadyExist = false;
    private List<MBOMInstanceItemDto> children = new LinkedList<>();
}
