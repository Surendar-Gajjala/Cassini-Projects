package com.cassinisys.plm.model.dto;

import lombok.Data;

/**
 * Created by subramanyam reddy on 16-12-2019.
 */
@Data
public class DetailsCount {

    /*-------------- Common ----------*/

    private Integer files = 0;

    /*--------------  ECO ---------*/

    private Integer affectedItems = 0;

    private Integer relatedItems = 0;


    /*-------------- Project --------*/

    private Integer team = 0;

    private Integer deliverables = 0;

    private Integer referenceItems = 0;

    private Integer tasks = 0;
    private Integer finishedTasks = 0;

    /*-------------- Glossary -----------*/

    private Integer entries = 0;

    /*-------------- MFR ------------------*/

    private Integer parts = 0;

    private Integer whereUsed = 0;

    private Integer requirements = 0;
    private Integer inspectionReports = 0;


    /*----------  Changes --------------*/

    private Integer changeRequests = 0;

    private Integer changes = 0;

    private Integer quality = 0;


      /*----------  Supplier --------------*/

    private Integer contacts = 0;
    private Integer requirementDocuments = 0;
    private Integer ppaps = 0;
    private Integer supplierAudits = 0;
    private Integer checklistCount = 0;
    private Integer planCount = 0;
    private Integer sprCount = 0;
    private Integer cpiCount = 0;
    private Integer fmChangeCount = 0;
}
