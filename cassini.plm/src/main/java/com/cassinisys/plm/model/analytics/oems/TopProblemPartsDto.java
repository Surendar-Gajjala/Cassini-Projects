package com.cassinisys.plm.model.analytics.oems;

import lombok.Data;

import java.math.BigInteger;

/**
 * Created by CassiniSystems on 26-07-2020.
 */
@Data
public class TopProblemPartsDto {

    private Integer partId;
    private String partNumber;
    private String partName;
    private BigInteger count;

    private Integer mfrId;
    private String name;
}
