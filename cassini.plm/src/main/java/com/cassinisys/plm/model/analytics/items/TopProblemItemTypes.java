package com.cassinisys.plm.model.analytics.items;

import lombok.Data;

import java.math.BigInteger;

/**
 * Created by subramanyam on 31-07-2020.
 */
@Data
public class TopProblemItemTypes {
    private Integer id;
    private String name;
    private BigInteger count;
}
