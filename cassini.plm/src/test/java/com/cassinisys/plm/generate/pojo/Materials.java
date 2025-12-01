package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Materials extends BaseClass {

    private String materialType = GenerateUtils.generateRandomTypeString("materialType", 2);
    private String materialNumber = GenerateUtils.generateSpecNumber("MT-");
    private String typePath = "Material/" + GenerateUtils.generateRandomTypeString("materialType", 2);
    private String materialName = GenerateUtils.generateRandomString(20);
    private String materialDescription = GenerateUtils.generateRandomString(20);
    private String QOM = "Mass";
    private String UOM = "femtograms";

}
