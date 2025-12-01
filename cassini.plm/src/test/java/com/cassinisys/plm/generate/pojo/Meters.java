package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Meters extends BaseClass {

    private String type = GenerateUtils.generateRandomTypeString("meterType", 2);
    private String typePath = "Meter/" + GenerateUtils.generateRandomTypeString("meterType", 2);
    private String meterNumber = GenerateUtils.generateSpecNumber("MTR-");
    private String meterName = GenerateUtils.generateSpecNumber("Meter");
    private String meterDescription = GenerateUtils.generateRandomString(20);
    private String meterType = "CONTINUOUS";
    private String meterReading = "ABSOLUTE";
    private String QOM = "Mass";
    private String UOM = "femtograms";

}
