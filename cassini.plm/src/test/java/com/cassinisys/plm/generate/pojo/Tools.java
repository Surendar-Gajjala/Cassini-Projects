package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Tools extends BaseClass {

    private String toolType = GenerateUtils.generateRandomTypeString("toolType", 2);
    private String toolNumber = GenerateUtils.generateSpecNumber("TL-");
    private String typePath = "Tool/" + toolType;
    private String toolName = GenerateUtils.generateRandomString(20);
    private String toolDescription = GenerateUtils.generateRandomString(20);
    private String requiresMaintenance = "Yes";

    private String assetType = GenerateUtils.generateRandomTypeString("toolAssetType", 1);
    private String assetTypePath = "Asset/" + assetType;
    private String assetNumber = GenerateUtils.generateSpecNumber("ASSETTL-");
    private String assetName = GenerateUtils.generateRandomString(20);
    private String description = GenerateUtils.generateRandomString(20);
    private String metered = GenerateUtils.getMeterValue();
    private String meters = GenerateUtils.generateMROMeter("Meter");

}
