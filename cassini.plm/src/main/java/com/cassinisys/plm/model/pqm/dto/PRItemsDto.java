package com.cassinisys.plm.model.pqm.dto;

import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import lombok.Data;

/**
 * Created by subramanyam on 16-06-2020.
 */
@Data
public class PRItemsDto {

    private Integer id;

    private Integer item;

    private Integer material;

    private Integer qcr;

    private Integer problemReport;

    private Integer ncr;

    private String itemName;

    private String itemNumber;

    private String itemType;

    private String notes;

    private String description;

    private String revision;

    private PLMLifeCyclePhase lifeCyclePhase;

    private String prNumber;

    private String ncrNumber;

    private Integer inspection;

    private String partName;

    private String partNumber;

    private String partType;

    private Integer manufacturer;

    private String manufacturerName;

   }
