package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Instruments extends BaseClass {

    private String instrumentType = GenerateUtils.generateRandomTypeString("instrumentType", 2);
    private String instrumentNumber = GenerateUtils.generateSpecNumber("INS-");
    private String typePath = "Instrument/" + instrumentType;
    private String instrumentName = GenerateUtils.generateRandomString(20);
    private String instrumentDescription = GenerateUtils.generateRandomString(20);
    private String requiresMaintenance = "Yes";

    private String assetType = GenerateUtils.generateRandomTypeString("instrAssetType", 1);
    private String assetTypePath = "Asset/" + GenerateUtils.generateRandomTypeString("instrAssetType", 1);
    private String assetNumber = GenerateUtils.generateSpecNumber("ASSET-");
    private String assetName = GenerateUtils.generateRandomString(20);
    private String description = GenerateUtils.generateRandomString(20);
    private String metered = GenerateUtils.getMeterValue();
    private String meters = GenerateUtils.generateSpecNumber("Meter");

    private String MFRName = GenerateUtils.generateRandomString(20);
    private String MFRDescription = GenerateUtils.generateRandomString(20);
    private String MFRModelNumber = GenerateUtils.generateRandomString(5);
    private String MFRPartNumber = GenerateUtils.generateRandomString(5);
    private String MFRSerialNumber = GenerateUtils.generateRandomString(5);
    private String MFRLotNumber = GenerateUtils.generateRandomString(5);
    private String MFRDate = "20/12/2021";

}
