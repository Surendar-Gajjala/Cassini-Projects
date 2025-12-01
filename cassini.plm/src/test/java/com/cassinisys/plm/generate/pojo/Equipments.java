package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Equipments extends BaseClass {

    private String equipmentType = GenerateUtils.generateRandomTypeString("equipmentType", 2);
    private String equipmentNumber = GenerateUtils.generateSpecNumber("EQ-");
    private String typePath = "Equipment/" + equipmentType;
    private String equipmentName = GenerateUtils.generateRandomString(20);
    private String equipmentDescription = GenerateUtils.generateRandomString(20);
    private String requiresMaintenance = "Yes";

    private String assetType = GenerateUtils.generateRandomTypeString("equiAssetType", 1);
    private String assetTypePath = "Asset/" + GenerateUtils.generateRandomTypeString("equiAssetType", 1);
    private String assetNumber = GenerateUtils.generateSpecNumber("ASSETEQ-");
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
    private String MFRDate = "27/12/2021";

}
