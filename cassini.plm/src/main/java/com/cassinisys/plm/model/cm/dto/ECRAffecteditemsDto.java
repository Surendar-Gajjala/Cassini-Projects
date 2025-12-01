package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.pqm.PQMProblemReport;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 12-06-2020.
 */
@Data
public class ECRAffecteditemsDto {

    private Integer id;

    private Integer item;

    private Integer ecr;

    private String notes;

    private String itemNumber;

    private String itemName;

    private String itemType;

    private String description;

    private String revision;
    private String phase;

    private PLMLifeCyclePhase lifeCyclePhase;

    private Integer dco;

    private Boolean qcrItem = false;
    private List<PQMProblemReport> problemReportList = new ArrayList<>();

}
