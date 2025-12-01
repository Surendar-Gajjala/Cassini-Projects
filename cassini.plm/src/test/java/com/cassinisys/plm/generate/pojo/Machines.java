package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Machines extends BaseClass {

    private String workCenterNumber = GenerateUtils.generateSpecNumber("WC-");
    private String machineType = GenerateUtils.generateRandomTypeString("manchineType", 2);
    private String machineNumber = GenerateUtils.generateSpecNumber("MC-");
    private String typePath = "Machine/" + machineType;
    private String machineName = GenerateUtils.generateRandomString(20);
    private String machineDescription = GenerateUtils.generateRandomString(20);
    private String requiresMaintenance = "Yes";

    private String assetType = GenerateUtils.generateRandomTypeString("machinAssetType", 1);
    private String assetTypePath = "Asset/" + GenerateUtils.generateRandomTypeString("machinAssetType", 1);
    private String assetNumber = GenerateUtils.generateSpecNumber("ASSETMC-");
    private String assetName = GenerateUtils.generateRandomString(20);
    private String description = GenerateUtils.generateRandomString(20);
    private String metered = GenerateUtils.getMeterValue();
    private String meters = GenerateUtils.generateMROMeter("Meter");

    private String MFRName = GenerateUtils.generateRandomString(20);
    private String MFRDescription = GenerateUtils.generateRandomString(20);
    private String MFRModelNumber = GenerateUtils.generateRandomString(5);
    private String MFRPartNumber = GenerateUtils.generateRandomString(5);
    private String MFRSerialNumber = GenerateUtils.generateRandomString(5);
    private String MFRLotNumber = GenerateUtils.generateRandomString(5);
    private String MFRDate = "20/12/2021";

}
