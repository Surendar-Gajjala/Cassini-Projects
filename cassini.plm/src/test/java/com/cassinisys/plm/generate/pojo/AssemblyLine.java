package com.cassinisys.plm.generate.pojo;

import com.cassinisys.plm.generate.GenerateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 26-11-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AssemblyLine extends BaseClass {

    private String plantNumber = GenerateUtils.generateSpecNumber("PT-");
    private String assemblyLineType =  GenerateUtils.generateRandomTypeString("assemblyLineType", 2);
    private String assemblyLineNumber = GenerateUtils.generateSpecNumber("ASSY-");
    private String typePath = "Assembly Line/" + assemblyLineType;
    private String assemblyLineName = GenerateUtils.generateRandomString(10);
    private String assemblyLineDescription = assemblyLineName + "-Description";
}
