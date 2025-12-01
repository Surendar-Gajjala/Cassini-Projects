package com.cassinisys.plm.generate.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 25-11-2021.
 */
@Data
@AllArgsConstructor
public class Items extends BaseClass{

    private String itemNumber;
    private String type;
    private String itemName;
    private String description;
    private String typeClass;
    private String typePath;
    private String configuration;
    private String revision;
    private String itemLifeCycle;
    private String units;
    private String makeOrBuy;
}
