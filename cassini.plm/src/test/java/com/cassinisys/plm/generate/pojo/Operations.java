package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Operations extends BaseClass {

    private String operationType = GenerateUtils.generateRandomTypeString("operationType", 2);
    private String operationNumber = GenerateUtils.generateSpecNumber("OP-");
    private String typePath = "Operations/" + GenerateUtils.generateRandomTypeString("operationType", 2);
    private String operationName = GenerateUtils.generateRandomString(20);
    private String operationDescription = GenerateUtils.generateRandomString(20);

}
