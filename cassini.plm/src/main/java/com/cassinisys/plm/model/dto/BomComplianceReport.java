package com.cassinisys.plm.model.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 10-05-2020.
 */
@Data
public class BomComplianceReport {

    BomComplianceData bomComplianceData;
    List<BomComplianceData> bomItems = new LinkedList<>();
    List<ComplianceSpecification> specifications = new LinkedList<>();

}
