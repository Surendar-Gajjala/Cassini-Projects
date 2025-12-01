package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Nageshreddy on 25-11-2021.
 */
@Data
public class Suppliers extends BaseClass {

    private String supplierType = GenerateUtils.generateRandomTypeString("supplierType", 2);
    private String supplierName = GenerateUtils.generateRandomString(20);
    private String typePath = "Supplier/"+supplierType;
    private String supplierDescription = GenerateUtils.generateRandomString(20);
    private String supplierLifecycle = supplierName.substring(1, 8);
    private String address = GenerateUtils.generateRandomString(25);
    private String city = address.substring(5, 15);
    private String country = GenerateUtils.generateRandomString(6);
    private String postalCode = GenerateUtils.getRandomPincode();
    private String phoneNumber = GenerateUtils.getRandomPhone();
    private String mobileNumber = GenerateUtils.getRandomPhone();
    private String faxNumber = GenerateUtils.getRandomPhone();
    private String email = supplierName.substring(1, 6) + "@gmail.com";
    private String website = "www." + supplierName.substring(6, 12) + ".com";
    private String supplierPartNumber = "";
    private String mfrPartNumber = "";

}
