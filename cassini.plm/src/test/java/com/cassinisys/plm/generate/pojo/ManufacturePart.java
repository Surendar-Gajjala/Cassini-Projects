package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;

/**
 * Created by Nageshreddy on 25-11-2021.
 */

@Data
public class ManufacturePart extends BaseClass {

    private String itemNumber = GenerateUtils.getItemNumber(GenerateUtils.getRandomIntegerBetweenRange(1, 2000));
    private String mfrType = GenerateUtils.generateRandomTypeString("mfrType", 2);
    private String mfrPartType = GenerateUtils.generateRandomTypeString("mfrPartType", 2);
    private String mfrTypePath = "mfr" + "path" + GenerateUtils.generateRandomTypeString("mfrType", 2);
    private String mfrPartTypePath = "mfr" + "Part" + "path" + GenerateUtils.generateRandomTypeString("mfrPartType", 2);
    private String mfr = GenerateUtils.generateRandomString(10);
    private String mfrDes = GenerateUtils.generateRandomString(10) + "-Desc";
    private String mfrLifeCycle = "Approved";
    private String mfrPartName = "mfr" + "Part" + GenerateUtils.generateRandomString(10);
    private String mfrPartNumber = "mfr" + "Part" + GenerateUtils.getItemNumber(GenerateUtils.getRandomIntegerBetweenRange(1, 2000));
    private String mfrPartDes = "Part" + GenerateUtils.generateRandomString(10) + "-desc";
    private String mfrPartStatus = "Active";
    private String mfrPartItemStatus = "QUALIFIED";
    private String mfrPartLifeCycle = GenerateUtils.getTypePath(1);

}
