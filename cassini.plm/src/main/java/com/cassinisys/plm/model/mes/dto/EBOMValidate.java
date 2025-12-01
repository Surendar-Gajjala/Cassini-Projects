package com.cassinisys.plm.model.mes.dto;

import com.cassinisys.plm.model.plm.MakeOrBuy;
import lombok.Data;

/**
 * Created by smukka on 18-05-2022.
 */
@Data
public class EBOMValidate {
    private Integer id;
    private Integer latestRevision;
    private Integer asReleasedRevision;
    private String itemNumber;
    private String itemName;
    private String description;
    private String itemTypeName;
    private String revision;
    private Integer quantity;
    private Integer totalQuantity;
    private Integer consumedQty = 0;
    private MakeOrBuy makeOrBuy;
    private Integer mfrId;
    private Integer mfrPartId;
    private String mfrPartNumber;
    private String mfrPartName;
}
