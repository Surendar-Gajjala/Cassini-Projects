package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Workcenters extends BaseClass {

    private String plantNumber = GenerateUtils.generateSpecNumber("PT-");
    private String assemblyLineNumber = GenerateUtils.generateSpecNumber("ASSY-");
    private String workCenterType = GenerateUtils.generateRandomTypeString("workCenterType", 2);
    private String typePath = "Work Center/" + workCenterType;
    private String workCenterNumber = GenerateUtils.generateSpecNumber("WC-");
    private String workCenterName = GenerateUtils.generateRandomString(20);
    private String Description = GenerateUtils.generateRandomString(20);
    private String requiresMaintenance = "Yes";

    private String assetType = GenerateUtils.generateRandomTypeString("workAssetType", 1);
    private String assetTypePath = "Asset/" + assetType;
    private String assetNumber = GenerateUtils.generateSpecNumber("ASSETWC-");
    private String assetName = GenerateUtils.generateRandomString(20);
    private String description = GenerateUtils.generateRandomString(20);
    private String metered = GenerateUtils.getMeterValue();
    private String Meters = GenerateUtils.generateMROMeter("Meter");

}
