package com.cassinisys.plm.model.analytics.items;

import lombok.Data;

import java.math.BigInteger;

/**
 * Created by subramanyam on 27-07-2020.
 */
@Data
public class TopProblemItems {
    private Integer itemId;
    private String itemName;
    private String itemNumber;
    private String revision;
    private BigInteger count;
}
