package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Plants extends BaseClass {
    private String plantType = GenerateUtils.generateRandomTypeString("plantType", 2);
    private String plantNumber = GenerateUtils.generateSpecNumber("PT-");
    private String typePath = "Plant/" + plantType;
    private String plantName = GenerateUtils.generateRandomString(20);
    private String Description = GenerateUtils.generateRandomString(20);
    private String plantManager = "Administrator";
    private String requiresMaintenance = "yes";
    private String Address = "Hastinapuram";
    private String city = "Hyderabad";
    private String country = "India";
    private String postalCode = GenerateUtils.getRandomPhone();
    private String phoneNumber = GenerateUtils.getRandomPhone();
    private String mobileNumber = GenerateUtils.getRandomPhone();
    private String faxNumber = GenerateUtils.getRandomPhone();
    private String email = GenerateUtils.generateRandomString(10);
    private String notes = GenerateUtils.generateRandomString(10);

    private String assetType = GenerateUtils.generateRandomTypeString("platAssetType", 1);
    private String assetTypePath = "Asset/" + assetType;
    private String assetNumber = GenerateUtils.generateSpecNumber("ASSETPT-");
    private String assetName = GenerateUtils.generateRandomString(20);
    private String description = GenerateUtils.generateRandomString(20);
    private String metered = GenerateUtils.getMeterValue();
    private String Meters = GenerateUtils.generateMROMeter("Meter");

}
