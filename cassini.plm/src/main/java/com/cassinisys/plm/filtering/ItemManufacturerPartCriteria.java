package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 20-05-2020.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ItemManufacturerPartCriteria extends Criteria {
    String partNumber;
    String partName;
    String status;
    Integer mfrPartType;
    String description;
    Integer item;
    Integer mco;
    Integer mcoReplacement;
    Integer variance;
    Boolean related = false;
    List<Integer> mcoAffectedItems = new ArrayList<>();
    Integer ncr;
    Integer qcr;
    Integer inspection;
    Integer supplier;
    Integer declaration;
}
