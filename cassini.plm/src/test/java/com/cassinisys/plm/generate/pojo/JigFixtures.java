package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class JigFixtures extends BaseClass {

    private String jigOrFixture = GenerateUtils.getJigOrFixture(1);
    private String JigsFixtureType = GenerateUtils.generateRandomTypeString("jigsFixtureType", 2);
    private String JigsFixtureNumber = GenerateUtils.generateSpecNumber("JF-");
    private String typePath = "Jigs & Fixtures/" + JigsFixtureType;
    private String JigsFixtureName = GenerateUtils.generateRandomString(20);
    private String JigsFixturesDescription = GenerateUtils.generateRandomString(20);
    private String requiresMaintenance = "Yes";

    private String assetType = GenerateUtils.generateRandomTypeString("jfAssetType", 1);
    private String assetTypePath = "Asset/" + GenerateUtils.generateRandomTypeString("jfAssetType", 1);
    private String assetNumber = GenerateUtils.generateSpecNumber("ASSETJF-");
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
