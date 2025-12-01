package com.cassinisys.plm.model.dto;

import lombok.Data;

/**
 * Created by CassiniSystems on 26-09-2019.
 */
@Data
public class ItemDetailsDto {

    private Integer itemFiles = 0;

    private Integer manufacturerParts = 0;

    private Integer relatedItems = 0;

    private Integer bom = 0;

    private Boolean changes = Boolean.FALSE;

    private Integer whereUsedItems = 0;

    private Integer projectItemsDtos = 0;

    private Integer requirements = 0;

    private Integer comments = 0;

    private Integer configuredItems = 0;

    private Integer changeItems = 0;

    private Integer varianceItems = 0;

    private Integer qualityItems = 0;

    private Integer affectedItems = 0;

    private Integer problemSources = 0;

    private Integer problemItems = 0;

    private Integer checklists = 0;

    private Integer sections = 0;

    private Integer totalChecklists = 0;

    private Integer pendingChecklists = 0;

    private Integer finishedChecklists = 0;

    private Integer passChecklists = 0;

    private Integer failChecklist = 0;

    private Integer capaCount = 0;

    private Boolean capaPass = false;

    private Boolean replacementPartsExist = true;

    private Boolean pendingEco = false;

    private Boolean rejectedOrOldRevision = false;

    private Integer reviewer = 0;

    private Integer workCenters = 0;

    private Integer inspections = 0;
    private Integer operations = 0;
    private Integer spareParts = 0;
    private Integer workOrders = 0;
    private Integer parts = 0;
    private Integer specifications = 0;
    private Integer substances = 0;
    private Integer resources = 0;

    private Integer problemReports = 0;
    private Integer requestedItems = 0;
    private Integer unAssignedItems = 0;
    private Integer items = 0;
    private Integer projectCount = 0;
    private Integer resourcesCount = 0;
    private Integer files = 0;
    private Integer tasks = 0;
    private Integer finishedTasks = 0;
    private Integer persons = 0;
}
