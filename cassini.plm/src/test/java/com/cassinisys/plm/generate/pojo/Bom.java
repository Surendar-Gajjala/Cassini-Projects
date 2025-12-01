package com.cassinisys.plm.generate.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Nageshreddy on 25-11-2021.
 */
@Data
@AllArgsConstructor
public class Bom extends BaseClass{

    private String level;
    private String itemNumber;
    private String type = "Part";
    private String itemName;
    private String typeClass;
    private String typePath = "Part";
    private String qty = "1";
    private String refDes = "";
    private String bomNotes = "";

}
