package com.cassinisys.plm.model.analytics.oems;

import lombok.Data;

import java.math.BigInteger;

/**
 * Created by CassiniSystems on 27-07-2020.
 */
@Data
public class TopRecurringPartsDto {

    private Integer varianceId;
    private String variance;
    private Integer partId;
    private Integer mfrId;
    private String partNumber;
    private BigInteger count;
}
