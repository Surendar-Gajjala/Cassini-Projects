package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SpareParts extends BaseClass {

    private String sparePartType = GenerateUtils.generateRandomTypeString("sparePartType", 2);
    private String typePath = "Spare Parts/" + sparePartType;
    private String sparePartNumber = GenerateUtils.generateSpecNumber("SPP-");
    private String sparePartName = GenerateUtils.generateRandomString(20);
    private String sparePartDescription = GenerateUtils.generateRandomString(20);

}
