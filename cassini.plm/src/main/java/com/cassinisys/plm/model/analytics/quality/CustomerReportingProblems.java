package com.cassinisys.plm.model.analytics.quality;

import lombok.Data;

import java.math.BigInteger;

/**
 * Created by subramanyam on 25-07-2020.
 */
@Data
public class CustomerReportingProblems {

    private Integer customerId;
    private String customerName;
    private BigInteger count;
}
