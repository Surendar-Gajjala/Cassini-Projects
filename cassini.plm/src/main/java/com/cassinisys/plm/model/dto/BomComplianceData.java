package com.cassinisys.plm.model.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 12-12-2020.
 */
@Data
public class BomComplianceData {
    private Integer id;
    private String itemName;
    private String itemNumber;
    private Integer parent;
    private Integer itemId;
    private Integer itemRevision;
    private String partNumber;
    private String partName;
    private String mfrName;
    private Integer partId;
    private Integer level = 0;
    private Boolean hasBom = false;
    private Boolean requireCompliance = false;
    private List<ComplianceSpecification> specifications = new LinkedList<>();
    private List<BomComplianceData> children = new LinkedList<>();

}
