package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;

/**
 * Created by Nageshreddy on 25-11-2021.
 */
@Data
public class Substances extends BaseClass {

    private String substanceType = GenerateUtils.generateRandomTypeString("substanceType", 2);
    private String subTypePath = "Substance/" + substanceType;
    private String substanceNumber = GenerateUtils.generateSpecNumber("ST-");
    private String substanceName = GenerateUtils.generateRandomString(10);
    private String CASNumber = GenerateUtils.generateCASString(10);
    private String substanceDescription = substanceName + "-Description";

}
