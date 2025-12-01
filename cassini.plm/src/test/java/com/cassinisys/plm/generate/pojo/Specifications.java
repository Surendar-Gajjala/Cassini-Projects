package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Nageshreddy on 25-11-2021.
 */

@Data
public class Specifications extends BaseClass {

    private String specificationType = GenerateUtils.generateRandomTypeString("specificationType", 2);
    private String specificationTypePath = "Specification/" + specificationType;
    private String specificationNumber = GenerateUtils.generateSpecNumber("SPEC-");
    private String specificationName = GenerateUtils.generateRandomString(20);
    private String specificationDescription = "Specification Description";
    private String substanceNumber = GenerateUtils.generateSpecNumber("ST-");
    private String thresholdMass = GenerateUtils.getRandomIntegerBetweenRangeInString(1, 50);
    private String subUnits = "grams";



}
