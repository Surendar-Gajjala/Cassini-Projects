package com.cassinisys.plm.model.dto;

import com.cassinisys.plm.model.pgc.dto.BosItemDto;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 12-12-2020.
 */
@Data
public class ComplianceSpecification {
    private Integer id;
    private String name;
    private Boolean compliant = false;
    private Boolean exempt = false;
    private List<BosItemDto> billOfSubstances = new LinkedList<>();
}
