package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Shifts extends BaseClass {

    private String shiftNumber = GenerateUtils.generateSpecNumber("SHFT-");
    private String shiftName = GenerateUtils.generateRandomString(20);
    private String shiftDescription = GenerateUtils.generateRandomString(20);
    private String startTime = "18:30";
    private String endTime = "18:30";

}
