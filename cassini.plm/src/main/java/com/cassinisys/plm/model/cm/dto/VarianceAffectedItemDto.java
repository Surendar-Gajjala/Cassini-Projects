package com.cassinisys.plm.model.cm.dto;

import lombok.Data;

/**
 * Created by reddy on 08/01/16.
 */
@Data
public class VarianceAffectedItemDto {

    private String itemNumber;
    private String itemType;
    private String itemName;
    private Integer itemId;
    private Integer quantity;
    private String serialsOrLots;
    private boolean recurring;
    private String notes;
    private Integer id;

}
