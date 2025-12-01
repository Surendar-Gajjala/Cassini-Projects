package com.cassinisys.plm.model.analytics.quality;

import lombok.Data;

import java.math.BigInteger;

/**
 * Created by subramanyam on 25-07-2020.
 */
@Data
public class ManufacturersForNCR {
    private Integer mfrId;
    private String manufacturer;
    private BigInteger count;
}
