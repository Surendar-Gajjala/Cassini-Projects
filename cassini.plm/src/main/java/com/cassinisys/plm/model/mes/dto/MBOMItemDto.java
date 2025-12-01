package com.cassinisys.plm.model.mes.dto;

import com.cassinisys.plm.model.mes.MESBomItemType;
import com.cassinisys.plm.model.mes.MESEnumObject;
import com.cassinisys.plm.model.plm.MakeOrBuy;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by smukka on 12-05-2022.
 */
@Data
public class MBOMItemDto {
    private Integer id;
    private Integer mbomRevision;
    private Integer bomItem;
    private Integer parent;
    private Integer phantom;
    private MESBomItemType type;
    private Integer quantity;
    private Integer manufacturerPart;

    private String itemName;
    private String itemNumber;
    private String itemTypeName;
    private String revision;
    private Integer asReleasedRevision;
    private Boolean hasBom = false;
    private Boolean itemRevisionHasBom = false;
    private String phantomNumber;
    private String phantomName;
    private MESEnumObject objectType = MESEnumObject.MBOMITEM;
    private Integer consumedQty = 0;
    private Integer producedQty = 0;
    private Boolean alreadyExist = false;
    private String mfrPartName;
    private String mfrPartNumber;
    private Integer mfrPartId;
    private Integer mfrId;
    private MakeOrBuy makeOrBuy;
    private List<MBOMItemDto> children = new LinkedList<>();
}

