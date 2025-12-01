package com.cassinisys.plm.model.analytics.quality;

import lombok.Data;

import java.math.BigInteger;

/**
 * Created by subramanyam on 03-08-2020.
 */
@Data
public class InspectionFailureMaterials {
    private Integer mfrId;
    private Integer mfrPartId;
    private String partNumber;
    private String partName;
    private BigInteger count;
}
