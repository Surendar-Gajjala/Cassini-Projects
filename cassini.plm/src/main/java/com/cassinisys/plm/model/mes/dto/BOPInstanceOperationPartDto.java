package com.cassinisys.plm.model.mes.dto;

import com.cassinisys.plm.model.mes.OperationPartType;
import lombok.Data;

/**
 * Created by smukka on 12-05-2022.
 */
@Data
public class BOPInstanceOperationPartDto {
    private Integer id;
    private Integer bopOperation;
    private Integer mbomInstanceItem;
    private Integer quantity;
    private String notes;
    private OperationPartType type;
    private String itemName;
    private String itemNumber;
    private String itemTypeName;
    private String revision;
    private Integer asReleasedRevision;
    private Boolean hasBom = false;
    private Integer consumedQty = 0;
}

