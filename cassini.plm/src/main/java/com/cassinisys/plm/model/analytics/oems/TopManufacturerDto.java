 package com.cassinisys.plm.model.analytics.oems;

import lombok.Data;

import java.math.BigInteger;

/**
 * Created by CassiniSystems on 26-07-2020.
 */
@Data
public class TopManufacturerDto {

    private Integer id;
    private BigInteger parts;
    private String mfr;
}
