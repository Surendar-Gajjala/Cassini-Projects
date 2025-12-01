package com.cassinisys.plm.model.analytics.quality;

import lombok.Data;

import java.math.BigInteger;

/**
 * Created by subramanyam on 25-07-2020.
 */
@Data
public class ProductProblems {
    private Integer productId;
    private String itemName;
    private String itemNumber;
    private String revision;
    private BigInteger count;
}
