package com.cassinisys.plm.model.pgc.dto;

import com.cassinisys.plm.model.pgc.BosItemType;
import lombok.Data;

/**
 * Created by subramanyam on 07-12-2020.
 */
@Data
public class BosItemDto {
    private Integer id;
    private Integer bos;
    private BosItemType bosItemType;
    private Integer material;
    private Integer substance;
    private Integer substanceGroup;
    private Integer quantity;
    private Double mass;
    private Integer uom;
    private String substanceName;
    private String casNumber;
    private String substanceType;
    private String unitName;
    private String unitSymbol;
    private Double specMass;
    private String specMassUnitName;
    private String specMassUnitSymbol;

    private Double thresholdMass;
    private String thresholdUnitName;
    private String thresholdUnitSymbol;
}
