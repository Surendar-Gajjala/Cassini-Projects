package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.plm.model.cm.MCOChangeType;
import com.cassinisys.plm.model.plm.PLMItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 12-06-2020.
 */
@Data
public class MCOAffecteditemsDto {

    private Integer id;

    private Integer material;

    private Integer replacement;

    private Integer mco;

    private Integer mfrId;

    private String notes;

    private String materialNumber;

    private String materialType;

    private String materialName;

    private String replacementNumber;

    private String replacementType;

    private String replacementName;

    private MCOChangeType changeType;

    private Boolean qcrItem;

    private String manufacturer;

    private String replaceMfr;
    private String type;

    private List<PLMItem> items = new ArrayList<>();


}
