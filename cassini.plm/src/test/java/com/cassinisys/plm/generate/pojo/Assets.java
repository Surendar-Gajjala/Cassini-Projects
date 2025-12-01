package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Assets extends BaseClass {

    private String resourceType = "Plant";
    private String resourceNumber = GenerateUtils.generateSpecNumber("PT-");
    private String assetType = GenerateUtils.generateRandomTypeString("assetType", 2);
    private String assetTypePath = "Asset/" + GenerateUtils.generateRandomTypeString("assetType", 2);
    private String assetNumber = GenerateUtils.generateSpecNumber("ASSET-");
    private String assetName = GenerateUtils.generateRandomString(20);
    private String description = GenerateUtils.generateRandomString(20);
    private String metered = "Yes";
    private String meters = "Meter1";


}
