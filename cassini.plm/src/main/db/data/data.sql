DO $$
DECLARE
    prodSource   INTEGER;
    partSource  INTEGER;
    docSource   INTEGER;
    assySource  INTEGER;
    itemSource INTEGER;

    ecoSource  INTEGER;
    mcoSource  INTEGER;

    revSequence INTEGER;
    docRevSequence INTEGER;
    lifeCycle   INTEGER;
    defaultLifeCycle   INTEGER;
    inspectionPlanLifeCycle  INTEGER;
    documentLifeCycle  INTEGER;

    prodType    INTEGER;
    assyType    INTEGER;
    partType    INTEGER;
    docType     INTEGER;
    mechType    INTEGER;
    elecType    INTEGER;
    electronicType  INTEGER;
    softType    INTEGER;

    ecoType     INTEGER;
    itemMcoType     INTEGER;
    oemPartMcoType     INTEGER;
    ecrType     INTEGER;
    dcoType     INTEGER;
    dcrType     INTEGER;
    deviationType  INTEGER;
    waiverType  INTEGER;
    terSource     INTEGER;
    reqSource     INTEGER;
    specSource     INTEGER;
    fileSource     INTEGER;
    folderSource     INTEGER;
    measurementSource     INTEGER;
    mfrLifecycle   INTEGER;
    supplierLifecycle   INTEGER;
    mfrPartLifecycle   INTEGER;
    wfLifecycle   INTEGER;
    reqDocLifecycle   INTEGER;
    reqLifecycle   INTEGER;

    ecrSource   INTEGER;
    dcrSource   INTEGER;
    dcoSource   INTEGER;
    deviationSource  INTEGER;
    waiverSource  INTEGER;

    planSource   INTEGER;
    inspectionSource   INTEGER;
    prSource   INTEGER;
    ncrSource   INTEGER;
    qcrSource   INTEGER;
    ppapSource INTEGER;
    wfSource   INTEGER;
    plantSource   INTEGER;
    assemblySource   INTEGER;
    shiftSource   INTEGER;
    workCenterSource   INTEGER;
    machineSource   INTEGER;
    toolSource   INTEGER;
    materialSource   INTEGER;
    jigsFixtureSource   INTEGER;
    productionOrderSource   INTEGER;
    serviceOrderSource   INTEGER;
    operationSource   INTEGER;
    manpowerSource   INTEGER;
    equipmentSource   INTEGER;
    instrumentSource   INTEGER;
    reqDocSource   INTEGER;
    sparePartSource   INTEGER;
    mbomSource INTEGER;
    bopSource INTEGER;
    assetSource   INTEGER;
    meterSource   INTEGER;
    assetTypeId   INTEGER;
    meterTypeId   INTEGER;

    failureTypeLov INTEGER;
    severityLov INTEGER;
    dispositionsLov INTEGER;
    changeRequestLov INTEGER;

    productInspectionPLanTypeId INTEGER;
    materialInspectionPLanTypeId INTEGER;
    problemReportWorkflowId INTEGER;
    ncrWorkflowId INTEGER;
    qcrWorkflowId INTEGER;
    mbomWorkflowId INTEGER;
    bopWorkflowId INTEGER;
    changeRequestReasons INTEGER;
    ppapTypeId INTEGER;

    materialTypeId INTEGER;
    jigsFixtureTypeId INTEGER;
    toolTypeId INTEGER;
    plantTypeId INTEGER;
    assemblyLineTypeId INTEGER;
    machineTypeId INTEGER;
    workCenterTypeId INTEGER;
    productionOrderTypeId INTEGER;
    manpowerTypeId INTEGER;
    machineOperatorTypeId INTEGER;
    qualityInspectorTypeId INTEGER;
    productionManagerTypeId INTEGER;
    operationTypeId INTEGER;
    assemblyOperationTypeId INTEGER;
    machiningOperationTypeId INTEGER;
    qualityOperationTypeId INTEGER;
    productionEngineerTypeId INTEGER;
    equipmentTypeId INTEGER;
    instrumentTypeId INTEGER;
    makeToOrderId     INTEGER;
    makeToStockId     INTEGER;
    engineerToOrderId     INTEGER;
    assembleToOrderId     INTEGER;
    configureToOrderId     INTEGER;
    mbomTypeId     INTEGER;
    bopTypeId      INTEGER;
    sparePartTypeId   INTEGER;

    programSource  INTEGER;
    projectSource  INTEGER;
    phaseSource  INTEGER;
    activitySource  INTEGER;
    taskSource  INTEGER;
    milestoneSource  INTEGER;
    projectId  INTEGER;
    phaseId  INTEGER;
    activityId  INTEGER;
    taskId  INTEGER;
    milestoneId INTEGER;
    projectReqId  INTEGER;
    productReqId  INTEGER;
    businessReqId  INTEGER;
    functionalReqId  INTEGER;
    marketReqId  INTEGER;
    systemReqId  INTEGER;
    userReqId  INTEGER;
    technicalReqId  INTEGER;
    qualityReqId  INTEGER;
    softwareReqId  INTEGER;
    customerReqId  INTEGER;

    requirementsSectionId  INTEGER;
    designRequirementId  INTEGER;
    businessRequirementId  INTEGER;
    solutionRequirementId  INTEGER;
    productRequirementId  INTEGER;
    stakeholderRequirementId  INTEGER;
    functionalRequirementId  INTEGER;
    nonFunctionalRequirementId  INTEGER;
    systemRequirementId  INTEGER;
    userRequirementId  INTEGER;
    userInterfaceRequirementId  INTEGER;
    usabilityRequirementId  INTEGER;
    qualityRequirementId  INTEGER;
    safetyRequirementId  INTEGER;
    efficiencyRequirementId  INTEGER;
    processRequirementId  INTEGER;
    implementationRequirementId  INTEGER;
    standardsRequirementId  INTEGER;
    reliabilityRequirementId  INTEGER;
    portabilityRequirementId  INTEGER;
    performanceRequirementId  INTEGER;
    securityRequirementId  INTEGER;
    operationalRequirementId  INTEGER;
    complianceRequirementId  INTEGER;
    regulatoryRequirementId  INTEGER;
    interoperabilityRequirementId  INTEGER;
    maintenanceRequirementId  INTEGER;
    externalRequirementId  INTEGER;


    workRequestSource  INTEGER;
    workOrderSource INTEGER;
    workRequestTypeId  INTEGER;
    workOrderTypeId  INTEGER;
    repairTypeId  INTEGER;
    maintenanceTypeId INTEGER;



    substanceSource  INTEGER;
    substanceGroupSource  INTEGER;
    declarationSource  INTEGER;

    substanceTypeId INTEGER;
    substanceGroupTypeId INTEGER;
    pgcSpecTypeId INTEGER;
    declarationTypeId INTEGER;


    oemSupplierId INTEGER;
    contractManSupplierId INTEGER;
    distributorSupplierId INTEGER;
    traderSupplierId INTEGER;
    wholesalerSupplierId INTEGER;
    maintenancePlanSource INTEGER;

    requirementPriorityList INTEGER;

    nprSource  INTEGER;
    docManagementLifecycle  INTEGER;
    ppapLifecycle INTEGER;
    supplierAuditTypeLifecycle INTEGER;
    supplierAuditSource INTEGER;
    supAuditTypeId INTEGER;
    ppapChecklistLifecycle  INTEGER;
    bopLifecycle  INTEGER;
    productionOrderLifecycle INTEGER;
    mbomLifecycle  INTEGER;
    ppapLovChecklist INTEGER;

    suppOEMSource  INTEGER;
    suppContactManSource   INTEGER;
    suppDistributorSource   INTEGER;
    suppTraderSource   INTEGER;
    suppWholesalerSource   INTEGER;
    programId INTEGER;

BEGIN

    prodSource      := nextval('AUTONUMBER_ID_SEQ');
    partSource      := nextval('AUTONUMBER_ID_SEQ');
    docSource       := nextval('AUTONUMBER_ID_SEQ');
    assySource      := nextval('AUTONUMBER_ID_SEQ');
    itemSource      := nextval('AUTONUMBER_ID_SEQ');

    ecoSource      := nextval('AUTONUMBER_ID_SEQ');
    mcoSource      := nextval('AUTONUMBER_ID_SEQ');
    itemMcoType     := nextval('OBJECT_ID_SEQ');
	  oemPartMcoType  := nextval('OBJECT_ID_SEQ');
    terSource      := nextval('AUTONUMBER_ID_SEQ');
    reqSource      := nextval('AUTONUMBER_ID_SEQ');
    specSource     := nextval('AUTONUMBER_ID_SEQ');
    fileSource     := nextval('AUTONUMBER_ID_SEQ');
    folderSource   := nextval('AUTONUMBER_ID_SEQ');
    measurementSource   := nextval('MEASUREMENT_ID_SEQ');

    ecrSource   := nextval('AUTONUMBER_ID_SEQ');
    dcrSource   := nextval('AUTONUMBER_ID_SEQ');
    dcoSource   := nextval('AUTONUMBER_ID_SEQ');
    deviationSource   := nextval('AUTONUMBER_ID_SEQ');
    waiverSource   := nextval('AUTONUMBER_ID_SEQ');

    planSource   := nextval('AUTONUMBER_ID_SEQ');
    inspectionSource   := nextval('AUTONUMBER_ID_SEQ');
    prSource   := nextval('AUTONUMBER_ID_SEQ');
    ncrSource   := nextval('AUTONUMBER_ID_SEQ');
    qcrSource   := nextval('AUTONUMBER_ID_SEQ');
    ppapSource   := nextval('AUTONUMBER_ID_SEQ');
    wfSource   := nextval('AUTONUMBER_ID_SEQ');
    plantSource   := nextval('AUTONUMBER_ID_SEQ');
    assemblySource   := nextval('AUTONUMBER_ID_SEQ');
    shiftSource:= nextval('AUTONUMBER_ID_SEQ');
    workCenterSource   := nextval('AUTONUMBER_ID_SEQ');
    machineSource   := nextval('AUTONUMBER_ID_SEQ');
    toolSource   := nextval('AUTONUMBER_ID_SEQ');
    materialSource   := nextval('AUTONUMBER_ID_SEQ');
    jigsFixtureSource   := nextval('AUTONUMBER_ID_SEQ');
    productionOrderSource   := nextval('AUTONUMBER_ID_SEQ');
    serviceOrderSource   := nextval('AUTONUMBER_ID_SEQ');
    operationSource   := nextval('AUTONUMBER_ID_SEQ');
    manpowerSource   := nextval('AUTONUMBER_ID_SEQ');
    equipmentSource   := nextval('AUTONUMBER_ID_SEQ');
    instrumentSource   := nextval('AUTONUMBER_ID_SEQ');
    mbomSource   := nextval('AUTONUMBER_ID_SEQ');
    bopSource   := nextval('AUTONUMBER_ID_SEQ');
    reqDocSource   := nextval('AUTONUMBER_ID_SEQ');
    sparePartSource   := nextval('AUTONUMBER_ID_SEQ');
    assetSource   := nextval('AUTONUMBER_ID_SEQ');
    meterSource   := nextval('AUTONUMBER_ID_SEQ');
    workRequestSource   := nextval('AUTONUMBER_ID_SEQ');
    workOrderSource   := nextval('AUTONUMBER_ID_SEQ');

    substanceSource   := nextval('AUTONUMBER_ID_SEQ');
    substanceGroupSource   := nextval('AUTONUMBER_ID_SEQ');
    declarationSource   := nextval('AUTONUMBER_ID_SEQ');


    programSource  := nextval('AUTONUMBER_ID_SEQ');
    projectSource  := nextval('AUTONUMBER_ID_SEQ');
    phaseSource  := nextval('AUTONUMBER_ID_SEQ');
    activitySource  := nextval('AUTONUMBER_ID_SEQ');
    taskSource  := nextval('AUTONUMBER_ID_SEQ');
    milestoneSource  := nextval('AUTONUMBER_ID_SEQ');
    maintenancePlanSource  := nextval('AUTONUMBER_ID_SEQ');
    nprSource   := nextval('AUTONUMBER_ID_SEQ');
    supplierAuditSource   := nextval('AUTONUMBER_ID_SEQ');

    suppOEMSource   := nextval('AUTONUMBER_ID_SEQ');
    suppContactManSource   := nextval('AUTONUMBER_ID_SEQ');
    suppDistributorSource   := nextval('AUTONUMBER_ID_SEQ');
    suppTraderSource   := nextval('AUTONUMBER_ID_SEQ');
    suppWholesalerSource   := nextval('AUTONUMBER_ID_SEQ');

    /* Autonumbers */
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (prodSource, 'Default Product Number Source', 'Auto product number', 5, 1, 1, 1, '0', 'PROD-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (assySource, 'Default Assembly Number Source', 'Auto assembly number', 5, 1, 1, 1, '0', 'ASSY-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (partSource, 'Default Part Number Source', 'Auto part number', 5, 1, 1, 1, '0', 'PART-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (docSource, 'Default Document Number Source', 'Auto document number', 5, 1, 1, 1, '0', 'DOC-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (itemSource, 'Default Item Number Source', 'Auto item number', 5, 1, 1, 1, '0', 'ITEM-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (ecoSource, 'Default ECO Number Source', 'Auto ECO number', 5, 1, 1, 1, '0', 'ECO-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (mcoSource, 'Default MCO Number Source', 'Auto MCO number', 5, 1, 1, 1, '0', 'MCO-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (terSource, 'Default Terminology Number Source', 'Auto terminology number', 5, 1, 1, 1, '0', 'TER-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (specSource, 'Default Specification Number Source', 'Auto specification number', 5, 1, 1, 1, '0', 'SPEC-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (reqDocSource, 'Default Requirements Document Source', 'Auto requirement document number', 5, 1, 1, 1, '0', 'REQDOC-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (reqSource, 'Default Requirement Number Source', 'Auto requirement number', 5, 1, 1, 1, '0', 'REQ-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (fileSource, 'Default File Number Source', 'Auto file number', 5, 1, 1, 1, '0', 'FILE-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (folderSource, 'Default Folder Number Source', 'Auto folder number', 5, 1, 1, 1, '0', 'FOLDER-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (ecrSource, 'Default ECR Number Source', 'Auto ECR number', 5, 1, 1, 1, '0', 'ECR-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (dcrSource, 'Default DCR Number Source', 'Auto DCR number', 5, 1, 1, 1, '0', 'DCR-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (dcoSource, 'Default DCO Source', 'Auto DCO number', 5, 1, 1, 1, '0', 'DCO-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (deviationSource, 'Default Deviation Number Source', 'Auto deviation number', 5, 1, 1, 1, '0', 'DEV-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (waiverSource, 'Default Waiver Number Source', 'Auto waiver number', 5, 1, 1, 1, '0', 'WAIV-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (planSource, 'Default Inspection Plan Number Source', 'Auto inspection plan number', 5, 1, 1, 1, '0', 'INSPLAN-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (inspectionSource, 'Default Inspection Number Source', 'Auto inspection number', 5, 1, 1, 1, '0', 'INS-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (prsource, 'Default PR Number Source', 'Auto PR number', 5, 1, 1, 1, '0', 'PR-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (ncrsource, 'Default NCR Number Source', 'Auto NCR number', 5, 1, 1, 1, '0', 'NCR-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (qcrsource, 'Default QCR Number Source', 'Auto QCR number', 5, 1, 1, 1, '0', 'QCR-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (ppapSource, 'Default PPAP Number Source', 'Auto PPAP number', 5, 1, 1, 1, '0', 'PPAP-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (wfSource, 'Default Workflow Number Source', 'Auto workflow number', 5, 1, 1, 1, '0', 'WF-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (plantSource, 'Default Plant Number Source', 'Auto plant number', 5, 1, 1, 1, '0', 'PT-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (assemblySource, 'Default Assembly Line Number Source', 'Auto assemblyLine number', 5, 1, 1, 1, '0', 'ASSYL-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (shiftSource, 'Default Shift Number Source', 'Auto shift number', 5, 1, 1, 1, '0', 'SHFT-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (workCenterSource, 'Default Work Center Number Source', 'Auto work center number', 5, 1, 1, 1, '0', 'WC-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (machineSource, 'Default Machine Number Source', 'Auto machine number', 5, 1, 1, 1, '0', 'MC-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (equipmentSource, 'Default Equipment Number Source', 'Auto machine number', 5, 1, 1, 1, '0', 'EQ-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (instrumentSource, 'Default Instrument Number Source', 'Auto machine number', 5, 1, 1, 1, '0', 'IT-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (toolSource, 'Default Tool Number Source', 'Auto tool number', 5, 1, 1, 1, '0', 'TL-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (materialSource, 'Default Material Number Source', 'Auto material number', 5, 1, 1, 1, '0', 'MT-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (jigsFixtureSource, 'Default Jigs and Fixture Number Source', 'Auto jig and fixture number', 5, 1, 1, 1, '0', 'JF-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (productionOrderSource, 'Default Production Order Number Source', 'Auto production order number', 5, 1, 1, 1, '0', 'PO-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (serviceOrderSource, 'Default Service Order Number Source', 'Auto service order number', 5, 1, 1, 1, '0', 'SO-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (operationSource, 'Default Operation Number Source', 'Auto operation number', 5, 1, 1, 1, '0', 'OP-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (manpowerSource, 'Default Manpower Number Source', 'Auto manpower number', 5, 1, 1, 1, '0', 'MP-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (sparePartSource, 'Default Spare Part Number Source', 'Auto Spare part plan number', 5, 1, 1, 1, '0', 'SPP-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (assetSource, 'Default Asset Number Source', 'Auto asset number', 5, 1, 1, 1, '0', 'AST-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (meterSource, 'Default Meter Number Source', 'Auto meter number', 5, 1, 1, 1, '0', 'MTR-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (programSource, 'Default Program Number Source', 'Auto program number', 5, 1, 1, 1, '0', 'PRG-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (projectSource, 'Default Project Number Source', 'Auto project number', 5, 1, 1, 1, '0', 'PRJ-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (phaseSource, 'Default Phase Number Source', 'Auto phase number', 5, 1, 1, 1, '0', 'PH-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (activitySource, 'Default Activity Number Source', 'Auto activity number', 5, 1, 1, 1, '0', 'ACT-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (taskSource, 'Default Task Number Source', 'Auto task number', 5, 1, 1, 1, '0', 'TASK-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (milestoneSource, 'Default Milestone Number Source', 'Auto milestone number', 5, 1, 1, 1, '0', 'MS-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (workRequestSource, 'Default Work Request Number Source', 'Auto work request number', 5, 1, 1, 1, '0', 'WR-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (workOrderSource, 'Default Work Order Number Source', 'Auto work order number', 5, 1, 1, 1, '0', 'WO-', '');    
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (mbomSource, 'Default MBOM Number Source', 'Auto MBOM number', 5, 1, 1, 1, '0', 'MBOM-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (bopSource, 'Default BOP Number Source', 'Auto BOP number', 5, 1, 1, 1, '0', 'BOP-', '');
    
    
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (substanceSource, 'Default Substance Number Source', 'Auto Substance number', 5, 1, 1, 1, '0', 'ST-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (substanceGroupSource, 'Default Substance Group Number Source', 'Auto Substance Group number', 5, 1, 1, 1, '0', 'STG-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (declarationSource, 'Default Declaration Number Source', 'Auto Declaration number', 5, 1, 1, 1, '0', 'DEC-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (maintenancePlanSource, 'Default Maintenance Plan Number Source', 'Auto Maintenance Plan number', 5, 1, 1, 1, '0', 'MPLAN-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (nprSource, 'Default NPR Number Source', 'Auto NPR Number', 5, 1, 1, 1, '0', 'NPR-', '');
    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (supplierAuditSource, 'Default Supplier Audit Number Source', 'Auto supplier audit number source', 5, 1, 1, 1, '0', 'SUPPA-', '');


    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (suppOEMSource, 'Default Supplier OEM Number Source', 'Auto supplier number source', 5, 1, 1, 1, '0', 'SUPOEM-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (suppContactManSource, 'Default Supplier Contract Manufacturer Number Source', 'Auto supplier number source', 5, 1, 1, 1, '0', 'SUPCM-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (suppDistributorSource, 'Default Supplier Distributor Number Source', 'Auto supplier number source', 5, 1, 1, 1, '0', 'SUPD-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (suppTraderSource, 'Default Supplier Trader Number Source', 'Auto supplier number source', 5, 1, 1, 1, '0', 'SUPT-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (suppWholesalerSource, 'Default Supplier Wholesaler Number Source', 'Auto supplier number source', 5, 1, 1, 1, '0', 'SUPW-', '');

    INSERT INTO AUTONUMBER (AUTONUMBER_ID, NAME, DESCRIPTION, NUMBERS, START, INCREMENT, NEXT_NUMBER, PADWITH, PREFIX, SUFFIX)
    VALUES (nextval('AUTONUMBER_ID_SEQ'), 'Default Phantom Number Source', 'Auto Phantom number', 5, 1, 1, 1, '0', 'PHTM-', '');


    /* LOVs */
    revSequence     := nextval('LOV_ID_SEQ');
    docRevSequence     := nextval('LOV_ID_SEQ');
    requirementPriorityList     := nextval('LOV_ID_SEQ');
    lifeCycle       := nextval('LIFECYCLE_ID_SEQ');
    defaultLifeCycle      := nextval('LIFECYCLE_ID_SEQ');


    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (revSequence, 'Revision Sequence', 'Default Revision Sequence', 'Default revision sequence',
            '{-,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z}', '-');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (docRevSequence, 'Revision Sequence', 'Default Document Revision Sequence', 'Default document revision sequence',
            '{A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z}', 'A');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (requirementPriorityList, 'Requirement Priority List', 'Default Requirement Priority List', 'Default requirement priority list',
            '{LOW, MEDIUM, HIGH,CRITICAL}', 'LOW');

    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (lifeCycle, 'Default Lifecycle - Legacy', 'Default Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), lifeCycle, 'Preliminary', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), lifeCycle, 'Prototype', 'RELEASED');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), lifeCycle, 'Production', 'RELEASED');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), lifeCycle, 'Obsolete', 'OBSOLETE');

    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (defaultLifeCycle, 'Default Lifecycle', 'Default Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), defaultLifeCycle, 'In Work', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), defaultLifeCycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), defaultLifeCycle, 'Approved', 'APPROVED');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), defaultLifeCycle, 'Released', 'RELEASED');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), defaultLifeCycle, 'Obsolete', 'OBSOLETE');

    mfrLifecycle  := nextval('LIFECYCLE_ID_SEQ');

    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (mfrLifecycle, 'Default Manufacturer Lifecycle', 'Default Manufacturer Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), mfrLifecycle, 'Unqualified', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), mfrLifecycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), mfrLifecycle, 'Approved', 'RELEASED');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), mfrLifecycle, 'Disqualified', 'CANCELLED');


    supplierLifecycle  := nextval('LIFECYCLE_ID_SEQ');

    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (supplierLifecycle, 'Default Supplier Lifecycle', 'Default Supplier Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), supplierLifecycle, 'Unqualified', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), supplierLifecycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), supplierLifecycle, 'Approved', 'RELEASED');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), supplierLifecycle, 'Disqualified', 'CANCELLED');

    mfrPartLifecycle  := nextval('LIFECYCLE_ID_SEQ');

    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (mfrPartLifecycle, 'Default ManufacturerPart Lifecycle', 'Default Manufacturer Part Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), mfrPartLifecycle, 'Unqualified', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), mfrPartLifecycle, 'Qualified', 'RELEASED');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), mfrPartLifecycle, 'Disqualified', 'CANCELLED');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), mfrPartLifecycle, 'Obsolete', 'OBSOLETE');

    wfLifecycle  := nextval('LIFECYCLE_ID_SEQ');
    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (wfLifecycle, 'Default Workflow Lifecycle', 'Default Workflow Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), wfLifecycle, 'Draft', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), wfLifecycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), wfLifecycle, 'Approved', 'RELEASED');

    inspectionPlanLifeCycle       := nextval('LIFECYCLE_ID_SEQ');
    documentLifeCycle       := nextval('LIFECYCLE_ID_SEQ');
    changeRequestReasons :=nextval('LOV_ID_SEQ');


    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (inspectionPlanLifeCycle, 'Default Inspection Plan Lifecycle', 'Default Inspection Plan Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), inspectionPlanLifeCycle, 'Draft', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), inspectionPlanLifeCycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), inspectionPlanLifeCycle, 'Approved', 'RELEASED');

    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (documentLifeCycle, 'Default Document Lifecycle', 'Default Document Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), documentLifeCycle, 'Draft', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), documentLifeCycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), documentLifeCycle, 'Approved', 'RELEASED');

    docManagementLifecycle  := nextval('LIFECYCLE_ID_SEQ');
    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (docManagementLifecycle, 'Default Document Management Lifecycle', 'Default Document Management Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), docManagementLifecycle, 'Draft', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), docManagementLifecycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), docManagementLifecycle, 'Released', 'RELEASED');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (nextval('LOV_ID_SEQ'), 'Change Request Types', 'Default Change Request Types', 'Default change request types',
            '{Engineering Change Request,Document Change Request,Process Change Request,Compliance Change Request,Corrective Action Change Request,Manufacturing Change Request,Field Failure Request}',
            'Engineering Change Request');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (changeRequestReasons, 'Change Request Reasons', 'Default Change Request Reasons', 'Default change request reasons',
            '{Initial Release,Product Correction,Cost Reduction,Customer/Scope Change,Manufacturing Improvement,Quality Improvement,Other}',
            'Initial Release');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (nextval('LOV_ID_SEQ'), 'Change Object Statuses', 'Default Change Object Statuses', 'Default change object statuses',
            '{Open,Pending,Working,Completed,Rejected,Approved}',
            'Open');

    reqDocLifecycle  := nextval('LIFECYCLE_ID_SEQ');
    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (reqDocLifecycle, 'Default Requirements Document Lifecycle', 'Default Requirements Document Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), reqDocLifecycle, 'Draft', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), reqDocLifecycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), reqDocLifecycle, 'Released', 'RELEASED');

    reqLifecycle  := nextval('LIFECYCLE_ID_SEQ');
    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (reqLifecycle, 'Default Requirement Lifecycle', 'Default Requirement Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), reqLifecycle, 'Draft', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), reqLifecycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), reqLifecycle, 'Approved', 'RELEASED');

    ppapLifecycle   := nextval('LIFECYCLE_ID_SEQ');
    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (ppapLifecycle, 'Default PPAP Lifecycle', 'Default PPAP Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), ppapLifecycle, 'Draft', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), ppapLifecycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), ppapLifecycle, 'Approved', 'RELEASED');

    supplierAuditTypeLifecycle   := nextval('LIFECYCLE_ID_SEQ');
    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (supplierAuditTypeLifecycle, 'Default Supplier Audit Lifecycle', 'Default Supplier Audit Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), supplierAuditTypeLifecycle, 'None', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), supplierAuditTypeLifecycle, 'Planned', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), supplierAuditTypeLifecycle, 'Completed', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), supplierAuditTypeLifecycle, 'Approved', 'RELEASED');

    ppapChecklistLifecycle   := nextval('LIFECYCLE_ID_SEQ');
    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (ppapChecklistLifecycle, 'Default PPAP Checklist Lifecycle', 'Default PPAP Checklist Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), ppapChecklistLifecycle, 'None', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), ppapChecklistLifecycle, 'Initiate', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), ppapChecklistLifecycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), ppapChecklistLifecycle, 'Completed', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), ppapChecklistLifecycle, 'Approved', 'RELEASED');

    mbomLifecycle   := nextval('LIFECYCLE_ID_SEQ');
    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (mbomLifecycle, 'Default MBOM Lifecycle', 'Default MBOM Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), mbomLifecycle, 'In Work', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), mbomLifecycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), mbomLifecycle, 'Approved', 'APPROVED');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), mbomLifecycle, 'Released', 'RELEASED');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), mbomLifecycle, 'Obsolete', 'OBSOLETE');

    bopLifecycle   := nextval('LIFECYCLE_ID_SEQ');
    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (bopLifecycle, 'Default BOP Lifecycle', 'Default BOP Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), bopLifecycle, 'In Work', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), bopLifecycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), bopLifecycle, 'Approved', 'APPROVED');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), bopLifecycle, 'Released', 'RELEASED');

    productionOrderLifecycle  := nextval('LIFECYCLE_ID_SEQ');
    INSERT INTO PLM_LIFECYCLE (ID, NAME, DESCRIPTION)
    VALUES (productionOrderLifecycle, 'Default Production Order Lifecycle', 'Default Production Order Lifecycle');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), productionOrderLifecycle, 'In Work', 'PRELIMINARY');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), productionOrderLifecycle, 'Review', 'REVIEW');

    INSERT INTO PLM_LIFECYCLEPHASE (ID, LIFECYCLE, PHASE, PHASE_TYPE)
    VALUES (nextval('LIFECYCLEPHASE_ID_SEQ'), productionOrderLifecycle, 'Approved', 'RELEASED');

    /* Standard Classification */
    prodType    := nextval('OBJECT_ID_SEQ');
    assyType    := nextval('OBJECT_ID_SEQ');
    partType    := nextval('OBJECT_ID_SEQ');
    docType     := nextval('OBJECT_ID_SEQ');
    mechType    := nextval('OBJECT_ID_SEQ');
    elecType    := nextval('OBJECT_ID_SEQ');
    electronicType  := nextval('OBJECT_ID_SEQ');
    softType    := nextval('OBJECT_ID_SEQ');

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (prodType, default, 1, default, 1, 'ITEMTYPE');
    INSERT INTO PLM_ITEMTYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, ITEMNUMBER_SOURCE, REVISION_SEQUENCE, LIFECYCLE,ITEM_CLASS)
    VALUES (prodType, 'Product', 'Product type', NULL, prodSource, revSequence, defaultLifeCycle,'PRODUCT');

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (assyType, default, 1, default, 1, 'ITEMTYPE');
    INSERT INTO PLM_ITEMTYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, ITEMNUMBER_SOURCE, REVISION_SEQUENCE, LIFECYCLE,ITEM_CLASS)
    VALUES (assyType, 'Assembly', 'Assembly type', NULL, assySource, revSequence, defaultLifeCycle,'ASSEMBLY');

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (partType, default, 1, default, 1, 'ITEMTYPE');
    INSERT INTO PLM_ITEMTYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, ITEMNUMBER_SOURCE, REVISION_SEQUENCE, LIFECYCLE,ITEM_CLASS)
    VALUES (partType, 'Part', 'Part type', NULL, partSource, revSequence, defaultLifeCycle,'PART');

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (docType, default, 1, default, 1, 'ITEMTYPE');
    INSERT INTO PLM_ITEMTYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, ITEMNUMBER_SOURCE, REVISION_SEQUENCE, LIFECYCLE,ITEM_CLASS)
    VALUES (docType, 'Document', 'Document type', NULL, docSource, revSequence, documentLifeCycle,'DOCUMENT');

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (mechType, default, 1, default, 1, 'ITEMTYPE');
    INSERT INTO PLM_ITEMTYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, ITEMNUMBER_SOURCE, REVISION_SEQUENCE, LIFECYCLE,ITEM_CLASS)
    VALUES (mechType, 'Mechanical', 'Mechanical parts', partType, partSource, revSequence, defaultLifeCycle,'PART');

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (elecType, default, 1, default, 1, 'ITEMTYPE');
    INSERT INTO PLM_ITEMTYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, ITEMNUMBER_SOURCE, REVISION_SEQUENCE, LIFECYCLE,ITEM_CLASS)
    VALUES (elecType, 'Electrical', 'Electrical parts', partType, partSource, revSequence, defaultLifeCycle,'PART');

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (electronicType, default, 1, default, 1, 'ITEMTYPE');
    INSERT INTO PLM_ITEMTYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, ITEMNUMBER_SOURCE, REVISION_SEQUENCE, LIFECYCLE,ITEM_CLASS)
    VALUES (electronicType, 'Electronic ', 'Electronic  parts', partType, partSource, revSequence, defaultLifeCycle,'PART');

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (softType, default, 1, default, 1, 'ITEMTYPE');
    INSERT INTO PLM_ITEMTYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, ITEMNUMBER_SOURCE, REVISION_SEQUENCE, LIFECYCLE,ITEM_CLASS, IS_SOFTWARE_TYPE)
    VALUES (softType, 'Software', 'Software component', partType, partSource, revSequence, defaultLifeCycle,'PART', TRUE);

    ecoType     := nextval('OBJECT_ID_SEQ');
    ecrType     := nextval('OBJECT_ID_SEQ');
    dcoType     := nextval('OBJECT_ID_SEQ');
    dcrType     := nextval('OBJECT_ID_SEQ');
    deviationType  := nextval('OBJECT_ID_SEQ');
    waiverType  := nextval('OBJECT_ID_SEQ');

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (ecoType, default, 1, default, 1, 'ECOTYPE');
    INSERT INTO PLM_CHANGETYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (ecoType, 'ECO', 'ECO', NULL, ecoSource);
    INSERT INTO PLM_ECO_TYPE(ID)
    VALUES (ecoType);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (itemMcoType, default, 1, default, 1, 'MCOTYPE');
    INSERT INTO PLM_CHANGETYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (itemMcoType, 'Item MCO', 'Item MCO', NULL, mcoSource);
    INSERT INTO PLM_MCO_TYPE(id, MCO_TYPE)
    VALUES (itemMcoType, 'ITEMMCO');

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (oemPartMcoType, default, 1, default, 1, 'MCOTYPE');
    INSERT INTO PLM_CHANGETYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (oemPartMcoType, 'Mfr. Part MCO', 'Mfr. Part MCO', NULL, mcoSource);
    INSERT INTO PLM_MCO_TYPE(id, MCO_TYPE)
    VALUES (oemPartMcoType, 'OEMPARTMCO');

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (ecrType, default, 1, default, 1, 'ECRTYPE');
    INSERT INTO PLM_CHANGETYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE,CHANGE_REASON_TYPES)
    VALUES (ecrType, 'ECR', 'ECR', NULL, ecrSource,changeRequestReasons);
    INSERT INTO PLM_ECR_TYPE(ID)
    VALUES (ecrType);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (dcrType, default, 1, default, 1, 'DCRTYPE');
    INSERT INTO PLM_CHANGETYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE,CHANGE_REASON_TYPES)
    VALUES (dcrType, 'DCR', 'DCR', NULL, dcrSource,changeRequestReasons);
    INSERT INTO PLM_DCR_TYPE(ID)
    VALUES (dcrType);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (dcoType, default, 1, default, 1, 'DCOTYPE');
    INSERT INTO PLM_CHANGETYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (dcoType, 'DCO', 'DCO', NULL, dcoSource);
    INSERT INTO PLM_DCO_TYPE(ID)
    VALUES (dcoType);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (deviationType, default, 1, default, 1, 'DEVIATIONTYPE');
    INSERT INTO PLM_CHANGETYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (deviationType, 'Deviation', 'Deviation', NULL, deviationSource);
    INSERT INTO PLM_DEVIATION_TYPE(ID)
    VALUES (deviationType);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (waiverType, default, 1, default, 1, 'WAIVERTYPE');
    INSERT INTO PLM_CHANGETYPE (TYPE_ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (waiverType, 'Waiver', 'Waiver', NULL, waiverSource);
    INSERT INTO PLM_WAIVER_TYPE(ID)
    VALUES (waiverType);



    INSERT INTO PLM_GLOSSARYLANGUAGES (ID,CODE,LANGUAGE,CREATED_DATE,DEFAULT_LANGUAGE)
    VALUES (nextval('GLOSSARY_LANGUAGE_ID_SEQ'),'EN','ENGLISH',CURRENT_TIMESTAMP,TRUE);

    failureTypeLov := nextval('LOV_ID_SEQ');
    severityLov := nextval('LOV_ID_SEQ');
    dispositionsLov := nextval('LOV_ID_SEQ');
    changeRequestLov := nextval('LOV_ID_SEQ');
    ppapLovChecklist := nextval('LOV_ID_SEQ');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (failureTypeLov, 'PR Defect Types', 'Default Problem Report Defect Types', 'Default problem report defect types','{}','');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (severityLov, 'PR Severities', 'Default Problem Report Severities', 'Default problem report severities','{Critical,High,Medium,Low}','Critical');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (dispositionsLov, 'PR Dispositions', 'Default Problem Report Dispositions', 'Default problem report dispositions','{}','');

    INSERT INTO LOV (LOV_ID, TYPE, NAME, DESCRIPTION, VALUES, DEFAULT_VALUE)
    VALUES (changeRequestLov, 'Default Change Request Urgency', 'Default Change Request Urgency', 'Default Change Request Urgency','{Critical,High,Medium,Low}','High');

    INSERT INTO lov(lov_id, type, name, description, values, default_value)
    VALUES (ppapLovChecklist,'Default PPAP Checklist','Default PPAP Checklist','Default PPAP Checklist','{}','');

    productInspectionPLanTypeId  := nextval('OBJECT_ID_SEQ');
    materialInspectionPLanTypeId  := nextval('OBJECT_ID_SEQ');
    problemReportWorkflowId  := nextval('OBJECT_ID_SEQ');
    ncrWorkflowId  := nextval('OBJECT_ID_SEQ');
    qcrWorkflowId  := nextval('OBJECT_ID_SEQ');
    mbomWorkflowId := nextval('OBJECT_ID_SEQ');
    bopWorkflowId := nextval('OBJECT_ID_SEQ');
    ppapTypeId  := nextval('OBJECT_ID_SEQ');
    materialTypeId  := nextval('OBJECT_ID_SEQ');
    toolTypeId  := nextval('OBJECT_ID_SEQ');
    plantTypeId  := nextval('OBJECT_ID_SEQ');
    assemblyLineTypeId  := nextval('OBJECT_ID_SEQ');
    machineTypeId  := nextval('OBJECT_ID_SEQ');
    workCenterTypeId  := nextval('OBJECT_ID_SEQ');
    productionOrderTypeId  := nextval('OBJECT_ID_SEQ');
    jigsFixtureTypeId  := nextval('OBJECT_ID_SEQ');
    manpowerTypeId := nextval('OBJECT_ID_SEQ');
    machineOperatorTypeId := nextval('OBJECT_ID_SEQ');
    qualityInspectorTypeId := nextval('OBJECT_ID_SEQ');
    productionManagerTypeId := nextval('OBJECT_ID_SEQ');
    operationTypeId := nextval('OBJECT_ID_SEQ');
    assemblyOperationTypeId := nextval('OBJECT_ID_SEQ');
    machiningOperationTypeId := nextval('OBJECT_ID_SEQ');
    qualityOperationTypeId := nextval('OBJECT_ID_SEQ');
    productionEngineerTypeId := nextval('OBJECT_ID_SEQ');
    equipmentTypeId := nextval('OBJECT_ID_SEQ');
    instrumentTypeId := nextval('OBJECT_ID_SEQ');
    makeToOrderId  := nextval('OBJECT_ID_SEQ');
    makeToStockId  := nextval('OBJECT_ID_SEQ');
    engineerToOrderId  := nextval('OBJECT_ID_SEQ');
    assembleToOrderId  := nextval('OBJECT_ID_SEQ');
    configureToOrderId  := nextval('OBJECT_ID_SEQ');
    mbomTypeId  := nextval('OBJECT_ID_SEQ');
    bopTypeId  := nextval('OBJECT_ID_SEQ');
    sparePartTypeId  := nextval('OBJECT_ID_SEQ');
    supAuditTypeId := nextval('OBJECT_ID_SEQ');

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (productInspectionPLanTypeId, default, 1, default, 1, 'QUALITY_TYPE');
    INSERT INTO PQM_QUALITY_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, NUMBER_SOURCE,QUALITY_TYPE)
    VALUES (productInspectionPLanTypeId, 'Product Inspection Plan', 'Product Inspection Plan', NULL, planSource,'PRODUCTINSPECTIONPLANTYPE');
    INSERT INTO PQM_INSPECTION_PLAN_TYPE (ID,INSPECTION_NUMBER_SOURCE, REVISION_SEQUENCE,LIFECYCLE)
    VALUES (productInspectionPLanTypeId, inspectionSource,revSequence,inspectionPlanLifeCycle);
    INSERT INTO PQM_PRODUCT_INSPECTION_PLAN_TYPE (ID,PRODUCT_TYPE)
    VALUES (productInspectionPLanTypeId,prodType);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (materialInspectionPLanTypeId, default, 1, default, 1, 'QUALITY_TYPE');
    INSERT INTO PQM_QUALITY_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, NUMBER_SOURCE,QUALITY_TYPE)
    VALUES (materialInspectionPLanTypeId, 'Material Inspection Plan', 'Material Inspection Plan', NULL, planSource,'MATERIALINSPECTIONPLANTYPE');
    INSERT INTO PQM_INSPECTION_PLAN_TYPE (ID,INSPECTION_NUMBER_SOURCE, REVISION_SEQUENCE,LIFECYCLE)
    VALUES (materialInspectionPLanTypeId, inspectionSource,revSequence,inspectionPlanLifeCycle);
    INSERT INTO PQM_MATERIAL_INSPECTION_PLAN_TYPE (ID,PART_TYPE)
    VALUES (materialInspectionPLanTypeId,NULL);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (problemReportWorkflowId, default, 1, default, 1, 'QUALITY_TYPE');
    INSERT INTO PQM_QUALITY_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, NUMBER_SOURCE,QUALITY_TYPE)
    VALUES (problemReportWorkflowId, 'PR', 'PR', NULL, prSource,'PRTYPE');
    INSERT INTO PQM_PROBLEM_REPORT_TYPE (ID,FAILURE_TYPES,SEVERITIES,DISPOSITIONS)
    VALUES (problemReportWorkflowId,failureTypeLov,severityLov,dispositionsLov);


    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (ncrWorkflowId, default, 1, default, 1, 'QUALITY_TYPE');
    INSERT INTO PQM_QUALITY_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, NUMBER_SOURCE,QUALITY_TYPE)
    VALUES (ncrWorkflowId, 'NCR', 'NCR', NULL, ncrSource,'NCRTYPE');
    INSERT INTO PQM_NCR_TYPE (ID, FAILURE_TYPES,SEVERITIES,DISPOSITIONS)
    VALUES (ncrWorkflowId,failureTypeLov,severityLov,dispositionsLov);


    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (qcrWorkflowId, default, 1, default, 1, 'QUALITY_TYPE');
    INSERT INTO PQM_QUALITY_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, NUMBER_SOURCE,QUALITY_TYPE)
    VALUES (qcrWorkflowId, 'QCR', 'QCR', NULL, qcrSource,'QCRTYPE');
    INSERT INTO PQM_QCR_TYPE (ID)
    VALUES (qcrWorkflowId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (ppapTypeId, default, 1, default, 1, 'QUALITY_TYPE');
    INSERT INTO PQM_QUALITY_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, NUMBER_SOURCE,QUALITY_TYPE)
    VALUES (ppapTypeId, 'PPAP', 'PPAP', NULL, ppapSource,'PPAPTYPE');
    INSERT INTO PQM_PPAP_TYPE (ID,LIFECYCLE)
    VALUES (ppapTypeId,ppapLifecycle);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (supAuditTypeId, default, 1, default, 1, 'QUALITY_TYPE');
    INSERT INTO PQM_QUALITY_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, NUMBER_SOURCE,QUALITY_TYPE)
    VALUES (supAuditTypeId, 'Supplier Audit', 'Supplier Audit', NULL, supplierAuditSource,'SUPPLIERAUDITTYPE');
    INSERT INTO PQM_SUPPLIER_AUDIT_TYPE (ID,LIFECYCLE)
    VALUES (supAuditTypeId,supplierAuditTypeLifecycle);


    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'ECO Workflow','Default ECO Workflow',NULL,'CHANGES',ecoType, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'ECR Workflow','Default ECR Workflow',NULL,'CHANGES',ecrType, wfSource, wfLifecycle, revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'DCO Workflow','Default DCO Workflow',NULL,'CHANGES',dcoType, wfSource, wfLifecycle, revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'DCR Workflow','Default DCR Workflow',NULL,'CHANGES',dcrType, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Item MCO Workflow','Default Item MCO Workflow',NULL,'CHANGES',itemMcoType, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Mfr. Part MCO Workflow','Default Mfr. Part MCO Workflow',NULL,'CHANGES',oemPartMcoType, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Deviation Workflow','Default Deviation Workflow',NULL,'CHANGES',deviationType, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Waiver Workflow','Default Waiver Workflow',NULL,'CHANGES',waiverType, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Product Inspection Plan Workflow','Default Product Inspection Plan Workflow',NULL,'QUALITY',productInspectionPLanTypeId, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Material Inspection Plan Workflow','Default Material Inspection Plan Workflow',NULL,'QUALITY',materialInspectionPLanTypeId, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Item Inspection Workflow','Default Item Inspection Workflow',NULL,'ITEM INSPECTIONS',NULL, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Material Inspection Workflow','Default Material Inspection Workflow',NULL,'MATERIAL INSPECTIONS',NULL, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Problem Report Workflow','Default Problem Report Workflow',NULL,'QUALITY',problemReportWorkflowId, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'NCR Workflow','Default NCR Workflow',NULL,'QUALITY',ncrWorkflowId, wfSource, wfLifecycle,revSequence);


    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Supplier Audit Workflow','Default Supplier Audit Workflow',NULL,'QUALITY',supAuditTypeId, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'QCR Workflow','Default QCR Workflow',NULL,'QUALITY',qcrWorkflowId, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'BOP Workflow','Default BOP Workflow',NULL,'MANUFACTURING',bopTypeId, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'PO Workflow','Default PO Workflow',NULL,'MANUFACTURING',productionOrderTypeId, wfSource, wfLifecycle,revSequence);





    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Project Workflow','Default Project Workflow',NULL,'PROJECTS',NULL, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Project Activity Workflow','Default Project Activity Workflow',NULL,'PROJECT ACTIVITIES',NULL, wfSource, wfLifecycle, revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Project Task Workflow','Default Project Task Workflow',NULL,'PROJECT TASKS',NULL, wfSource, wfLifecycle, revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Program Workflow','Default Program Workflow',NULL,'PROGRAM',NULL, wfSource, wfLifecycle,revSequence);


    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (plantTypeId, default, 1, default, 1, 'PLANTTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (plantTypeId, 'Plant', 'plant', NULL, plantSource);
    INSERT INTO MES_PLANT_TYPE (ID)
    VALUES (plantTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (assemblyLineTypeId, default, 1, default, 1, 'ASSEMBLYLINETYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (assemblyLineTypeId, 'Assembly Line', 'assembly Line', NULL, assemblySource);
    INSERT INTO MES_ASSEMBLYLINE_TYPE (ID)
    VALUES (assemblyLineTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (workCenterTypeId, default, 1, default, 1, 'WORKCENTERTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (workCenterTypeId, 'Work Center', 'Work Center', NULL, workCenterSource);
    INSERT INTO MES_WORKCENTER_TYPE (ID)
    VALUES (workCenterTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (machineTypeId, default, 1, default, 1, 'MACHINETYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (machineTypeId, 'Machine', 'machine', NULL, machineSource);
    INSERT INTO MES_MACHINE_TYPE (ID)
    VALUES (machineTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (equipmentTypeId, default, 1, default, 1, 'EQUIPMENTTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (equipmentTypeId, 'Equipment', 'Equipment', NULL, equipmentSource);
    INSERT INTO MES_EQUIPMENT_TYPE (ID)
    VALUES (equipmentTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (instrumentTypeId, default, 1, default, 1, 'INSTRUMENTTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (instrumentTypeId, 'Instrument', 'Instrument', NULL, instrumentSource);
    INSERT INTO MES_INSTRUMENT_TYPE (ID)
    VALUES (instrumentTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (toolTypeId, default, 1, default, 1, 'TOOLTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (toolTypeId, 'Tool', 'Tool', NULL, toolSource);
    INSERT INTO MES_TOOL_TYPE (ID)
    VALUES (toolTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (jigsFixtureTypeId, default, 1, default, 1, 'JIGFIXTURETYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (jigsFixtureTypeId, 'Jigs & Fixtures ', 'Jigs and Fixtures ', NULL, jigsFixtureSource);
    INSERT INTO MES_JIGS_FIXTURES_TYPE (ID)
    VALUES (jigsFixtureTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (materialTypeId, default, 1, default, 1, 'MATERIALTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (materialTypeId, 'Material', 'Material', NULL, materialSource);
    INSERT INTO MES_MATERIAL_TYPE (ID)
    VALUES (materialTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (manpowerTypeId, default, 1, default, 1, 'MANPOWERTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (manpowerTypeId, 'Manpower', 'ManPower', NULL, manpowerSource);
    INSERT INTO MES_MANPOWER_TYPE (ID)
    VALUES (manpowerTypeId);

     INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (machineOperatorTypeId, default, 1, default, 1, 'MANPOWERTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (machineOperatorTypeId, 'Machine Operator', 'Machine Operator', manpowerTypeId, manpowerSource);
    INSERT INTO MES_MANPOWER_TYPE (ID)
    VALUES (machineOperatorTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (qualityInspectorTypeId, default, 1, default, 1, 'MANPOWERTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (qualityInspectorTypeId, 'Quality Inspector', 'Quality Inspector', manpowerTypeId, manpowerSource);
    INSERT INTO MES_MANPOWER_TYPE (ID)
    VALUES (qualityInspectorTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (productionManagerTypeId, default, 1, default, 1, 'MANPOWERTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (productionManagerTypeId, 'Production Manager', 'Production Manager', manpowerTypeId, manpowerSource);
    INSERT INTO MES_MANPOWER_TYPE (ID)
    VALUES (productionManagerTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (productionEngineerTypeId, default, 1, default, 1, 'MANPOWERTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (productionEngineerTypeId, 'Production Engineer', 'Production Engineer', manpowerTypeId, manpowerSource);
    INSERT INTO MES_MANPOWER_TYPE (ID)
    VALUES (productionEngineerTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (operationTypeId, default, 1, default, 1, 'OPERATIONTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (operationTypeId, 'Operations', 'Operations', NULL , operationSource);
    INSERT INTO MES_OPERATION_TYPE (ID)
    VALUES (operationTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (assemblyOperationTypeId, default, 1, default, 1, 'OPERATIONTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (assemblyOperationTypeId, 'Assembly Operation', 'Assembly Operation', operationTypeId, operationSource);
    INSERT INTO MES_OPERATION_TYPE (ID)
    VALUES (assemblyOperationTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (machiningOperationTypeId, default, 1, default, 1, 'OPERATIONTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (machiningOperationTypeId, 'Machining Operation ', 'Machining Operation ', operationTypeId, operationSource);
    INSERT INTO MES_OPERATION_TYPE (ID)
    VALUES (machiningOperationTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (qualityOperationTypeId, default, 1, default, 1, 'OPERATIONTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (qualityOperationTypeId, 'Quality Operation ', 'Quality Operation ', operationTypeId, operationSource);
    INSERT INTO MES_OPERATION_TYPE (ID)
    VALUES (qualityOperationTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (productionOrderTypeId, default, 1, default, 1, 'PRODUCTIONORDERTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (productionOrderTypeId, 'Production Order', 'Production Order', NULL, productionOrderSource);
    INSERT INTO MES_PRODUCTION_ORDER_TYPE (ID,LIFECYCLE)
    VALUES (productionOrderTypeId,productionOrderLifecycle);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (makeToOrderId, default, 1, default, 1, 'PRODUCTIONORDERTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (makeToOrderId, 'Make-to-Order (MTO)', 'Make to order', productionOrderTypeId, productionOrderSource);
    INSERT INTO MES_PRODUCTION_ORDER_TYPE (ID,LIFECYCLE)
    VALUES (makeToOrderId,productionOrderLifecycle);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (makeToStockId, default, 1, default, 1, 'PRODUCTIONORDERTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (makeToStockId, 'Make-to-Stock (MTS)', 'Make to Stock', productionOrderTypeId, productionOrderSource);
    INSERT INTO MES_PRODUCTION_ORDER_TYPE (ID,LIFECYCLE)
    VALUES (makeToStockId,productionOrderLifecycle);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (engineerToOrderId, default, 1, default, 1, 'PRODUCTIONORDERTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (engineerToOrderId, 'Engineer-to-Order (ETO)', 'Engineer to order', productionOrderTypeId, productionOrderSource);
    INSERT INTO MES_PRODUCTION_ORDER_TYPE (ID,LIFECYCLE)
    VALUES (engineerToOrderId,productionOrderLifecycle);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (assembleToOrderId, default, 1, default, 1, 'PRODUCTIONORDERTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (assembleToOrderId, 'Assemble-to-Order (ATO)', 'Assemble to order', productionOrderTypeId, productionOrderSource);
    INSERT INTO MES_PRODUCTION_ORDER_TYPE (ID,LIFECYCLE)
    VALUES (assembleToOrderId,productionOrderLifecycle);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (configureToOrderId, default, 1, default, 1, 'PRODUCTIONORDERTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (configureToOrderId, 'Configure-to-Order (CTO)', 'Configure to order', productionOrderTypeId, productionOrderSource);
    INSERT INTO MES_PRODUCTION_ORDER_TYPE (ID,LIFECYCLE)
    VALUES (configureToOrderId,productionOrderLifecycle);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (mbomTypeId, default, 1, default, 1, 'MBOMTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (mbomTypeId, 'MBOM', 'MBOM', NULL, mbomSource);
    INSERT INTO MES_MBOM_TYPE (ID,LIFECYCLE,REVISION_SEQUENCE)
    VALUES (mbomTypeId,mbomLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (bopTypeId, default, 1, default, 1, 'BOPTYPE');
    INSERT INTO MES_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (bopTypeId, 'BOP', 'BOP', NULL, bopSource);
    INSERT INTO MES_BOP_TYPE (ID,LIFECYCLE,REVISION_SEQUENCE)
    VALUES (bopTypeId,bopLifecycle,revSequence);

    assetTypeId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (assetTypeId, default, 1, default, 1, 'ASSETTYPE');
    INSERT INTO MRO_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (assetTypeId, 'Asset', 'Asset', NULL, assetSource);
    INSERT INTO MRO_ASSET_TYPE (ID)
    VALUES (assetTypeId);

    meterTypeId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (meterTypeId, default, 1, default, 1, 'METERTYPE');
    INSERT INTO MRO_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (meterTypeId, 'Meter', 'Meter', NULL, meterSource);
    INSERT INTO MRO_METER_TYPE (ID)
    VALUES (meterTypeId);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (sparePartTypeId, default, 1, default, 1, 'SPAREPARTTYPE');
    INSERT INTO MRO_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (sparePartTypeId, 'Spare Parts', 'Spare Parts', NULL, sparePartSource);
    INSERT INTO MRO_SPAREPART_TYPE (ID)
    VALUES (sparePartTypeId);

    workRequestTypeId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (workRequestTypeId, default, 1, default, 1, 'WORKREQUESTTYPE');
    INSERT INTO MRO_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (workRequestTypeId, 'Work Request', 'Work Request', NULL, workRequestSource);
    INSERT INTO MRO_WORKREQUEST_TYPE (ID)
    VALUES (workRequestTypeId);


    maintenanceTypeId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (maintenanceTypeId, default, 1, default, 1, 'WORKORDERTYPE');
    INSERT INTO MRO_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (maintenanceTypeId, 'Maintenance', 'Maintenance', NULL, workOrderSource);
    INSERT INTO MRO_WORKORDER_TYPE (ID,TYPE)
    VALUES (maintenanceTypeId,'MAINTENANCE');

    repairTypeId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (repairTypeId, default, 1, default, 1, 'WORKORDERTYPE');
    INSERT INTO MRO_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (repairTypeId, 'Repair', 'Repair', NULL, workOrderSource);
    INSERT INTO MRO_WORKORDER_TYPE (ID,TYPE)
    VALUES (repairTypeId,'REPAIR');

     substanceTypeId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (substanceTypeId, default, 1, default, 1, 'PGCSUBSTANCETYPE');
    INSERT INTO PGC_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (substanceTypeId, 'Substance', 'Substance', NULL , substanceSource);
    INSERT INTO PGC_SUBSTANCE_TYPE (ID)
    VALUES (substanceTypeId);


     substanceGroupTypeId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (substanceGroupTypeId, default, 1, default, 1, 'PGCSUBSTANCEGROUPTYPE');
    INSERT INTO PGC_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE,AUTONUMBER_SOURCE)
    VALUES (substanceGroupTypeId, 'Substance Group', 'Substance Group', NULL ,substanceGroupSource);
    INSERT INTO PGC_SUBSTANCEGROUP_TYPE (ID)
    VALUES (substanceGroupTypeId);

     pgcSpecTypeId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (pgcSpecTypeId, default, 1, default, 1, 'PGCSPECIFICATIONTYPE');
    INSERT INTO PGC_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (pgcSpecTypeId, 'Specification', 'Specification', NULL , specSource);
    INSERT INTO PGC_SPECIFICATION_TYPE (ID)
    VALUES (pgcSpecTypeId);

     declarationTypeId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (declarationTypeId, default, 1, default, 1, 'PGCDECLARATIONTYPE');
    INSERT INTO PGC_OBJECT_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, AUTONUMBER_SOURCE)
    VALUES (declarationTypeId, 'Declaration', 'Declaration', NULL , declarationSource);
    INSERT INTO PGC_DECLARATION_TYPE (ID)
    VALUES (declarationTypeId);


    oemSupplierId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (oemSupplierId, default, 1, default, 1, 'SUPPLIERTYPE');
    INSERT INTO PLM_SUPPLIER_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, LIFECYCLE, AUTONUMBER_SOURCE)
    VALUES (oemSupplierId, 'OEM', 'OEM', NULL , supplierLifecycle, suppOEMSource);


    contractManSupplierId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (contractManSupplierId, default, 1, default, 1, 'SUPPLIERTYPE');
    INSERT INTO PLM_SUPPLIER_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, LIFECYCLE, AUTONUMBER_SOURCE)
    VALUES (contractManSupplierId, 'Contract Manufacturer', 'Contract Manufacturer', NULL , supplierLifecycle, suppContactManSource);


    distributorSupplierId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (distributorSupplierId, default, 1, default, 1, 'SUPPLIERTYPE');
    INSERT INTO PLM_SUPPLIER_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, LIFECYCLE, AUTONUMBER_SOURCE)
    VALUES (distributorSupplierId, 'Distributor', 'Distributor', NULL , supplierLifecycle, suppDistributorSource);


    traderSupplierId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (traderSupplierId, default, 1, default, 1, 'SUPPLIERTYPE');
    INSERT INTO PLM_SUPPLIER_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, LIFECYCLE, AUTONUMBER_SOURCE)
    VALUES (traderSupplierId, 'Trader', 'Trader', NULL , supplierLifecycle, suppTraderSource);

    wholesalerSupplierId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (wholesalerSupplierId, default, 1, default, 1, 'SUPPLIERTYPE');
    INSERT INTO PLM_SUPPLIER_TYPE (ID, NAME, DESCRIPTION, PARENT_TYPE, LIFECYCLE, AUTONUMBER_SOURCE)
    VALUES (wholesalerSupplierId, 'Wholesaler', 'Wholesaler', NULL , supplierLifecycle, suppWholesalerSource);

    /* Application Details Data */
    INSERT INTO APPLICATION_DETAILS (ID, OPTION_KEY, OPTION_NAME, CREATED_DATE, MODIFIED_DATE, VALUE)
        VALUES (NEXTVAL('APPLICATION_SEQUENCE'), 1, 'NAME',CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'CASSINI_PLM');

    INSERT INTO APPLICATION_DETAILS (ID, OPTION_KEY, OPTION_NAME, CREATED_DATE, MODIFIED_DATE, VALUE)
        VALUES (NEXTVAL('APPLICATION_SEQUENCE'), 6, 'SHOW_LANGUAGE',CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'true');



    programId  := nextval('OBJECT_ID_SEQ');
    projectId  := nextval('OBJECT_ID_SEQ');
    phaseId  := nextval('OBJECT_ID_SEQ');
    activityId  := nextval('OBJECT_ID_SEQ');
    taskId  := nextval('OBJECT_ID_SEQ');
    milestoneId  := nextval('OBJECT_ID_SEQ');

    projectReqId  := nextval('OBJECT_ID_SEQ');
    productReqId  := nextval('OBJECT_ID_SEQ');
    businessReqId  := nextval('OBJECT_ID_SEQ');
    functionalReqId  := nextval('OBJECT_ID_SEQ');
    marketReqId  := nextval('OBJECT_ID_SEQ');
    systemReqId  := nextval('OBJECT_ID_SEQ');
    userReqId  := nextval('OBJECT_ID_SEQ');
    technicalReqId  := nextval('OBJECT_ID_SEQ');
    qualityReqId  := nextval('OBJECT_ID_SEQ');
    softwareReqId  := nextval('OBJECT_ID_SEQ');
    customerReqId  := nextval('OBJECT_ID_SEQ');

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (programId, default, 1, default, 1, 'PMOBJECTTYPE');
    INSERT INTO PLM_PMOBJECTTYPE (ID, NAME, DESCRIPTION, PARENT, NUMBER_SOURCE,TYPE)
    VALUES (programId, 'Program', 'Program', NULL , programSource,'PROGRAM');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (projectId, default, 1, default, 1, 'PMOBJECTTYPE');
    INSERT INTO PLM_PMOBJECTTYPE (ID, NAME, DESCRIPTION, PARENT, NUMBER_SOURCE,TYPE)
    VALUES (projectId, 'Project', 'Project', NULL , projectSource,'PROJECT');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (phaseId, default, 1, default, 1, 'PMOBJECTTYPE');
    INSERT INTO PLM_PMOBJECTTYPE (ID, NAME, DESCRIPTION, PARENT, NUMBER_SOURCE,TYPE)
    VALUES (phaseId, 'Phase', 'Phase', NULL , phaseSource,'PROJECTPHASEELEMENT');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (activityId, default, 1, default, 1, 'PMOBJECTTYPE');
    INSERT INTO PLM_PMOBJECTTYPE (ID, NAME, DESCRIPTION, PARENT, NUMBER_SOURCE,TYPE)
    VALUES (activityId, 'Activity', 'Activity', NULL , activitySource,'PROJECTACTIVITY');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (taskId, default, 1, default, 1, 'PMOBJECTTYPE');
    INSERT INTO PLM_PMOBJECTTYPE (ID, NAME, DESCRIPTION, PARENT, NUMBER_SOURCE,TYPE)
    VALUES (taskId, 'Task', 'Task', NULL , taskSource,'PROJECTTASK');
     INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (milestoneId, default, 1, default, 1, 'PMOBJECTTYPE');
    INSERT INTO PLM_PMOBJECTTYPE (ID, NAME, DESCRIPTION, PARENT, NUMBER_SOURCE,TYPE)
    VALUES (milestoneId, 'Milestone', 'Milestone', NULL , milestoneSource,'PROJECTMILESTONE');


    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (projectReqId, default, 1, default, 1, 'REQUIREMENTDOCUMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (projectReqId, 'Project Requirements Document', 'Project Requirements Document', reqDocSource);
    INSERT INTO PLM_REQUIREMENTDOCUMENTTYPE (ID, PARENT, LIFECYCLE, REVISION_SEQUENCE)
    VALUES (projectReqId, NULL, reqDocLifecycle,  revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (productReqId, default, 1, default, 1, 'REQUIREMENTDOCUMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (productReqId, 'Product Requirements Document', 'Product Requirements Document', reqDocSource);
    INSERT INTO PLM_REQUIREMENTDOCUMENTTYPE (ID, PARENT, LIFECYCLE, REVISION_SEQUENCE)
    VALUES (productReqId, NULL, reqDocLifecycle,  revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (businessReqId, default, 1, default, 1, 'REQUIREMENTDOCUMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (businessReqId, 'Business Requirements Document', 'Business  Requirements Document', reqDocSource);
    INSERT INTO PLM_REQUIREMENTDOCUMENTTYPE (ID, PARENT, LIFECYCLE, REVISION_SEQUENCE)
    VALUES (businessReqId, NULL, reqDocLifecycle,  revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (functionalReqId, default, 1, default, 1, 'REQUIREMENTDOCUMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (functionalReqId, 'Functional Requirements Document', 'Functional Requirements Document', reqDocSource);
    INSERT INTO PLM_REQUIREMENTDOCUMENTTYPE (ID, PARENT, LIFECYCLE, REVISION_SEQUENCE)
    VALUES (functionalReqId, NULL, reqDocLifecycle,  revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (marketReqId, default, 1, default, 1, 'REQUIREMENTDOCUMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (marketReqId, 'Market Requirements Document', 'Market Requirements Document', reqDocSource);
    INSERT INTO PLM_REQUIREMENTDOCUMENTTYPE (ID, PARENT, LIFECYCLE, REVISION_SEQUENCE)
    VALUES (marketReqId, NULL, reqDocLifecycle,  revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (systemReqId, default, 1, default, 1, 'REQUIREMENTDOCUMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (systemReqId, 'System Requirements Document', 'System Requirements Document', reqDocSource);
    INSERT INTO PLM_REQUIREMENTDOCUMENTTYPE (ID, PARENT, LIFECYCLE, REVISION_SEQUENCE)
    VALUES (systemReqId, NULL, reqDocLifecycle,  revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (userReqId, default, 1, default, 1, 'REQUIREMENTDOCUMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (userReqId, 'User Requirements Document', 'User Requirements Document', reqDocSource);
    INSERT INTO PLM_REQUIREMENTDOCUMENTTYPE (ID, PARENT, LIFECYCLE, REVISION_SEQUENCE)
    VALUES (userReqId, NULL, reqDocLifecycle,  revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (technicalReqId, default, 1, default, 1, 'REQUIREMENTDOCUMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (technicalReqId, 'Technical Requirements Document', 'Technical  Requirements Document', reqDocSource);
    INSERT INTO PLM_REQUIREMENTDOCUMENTTYPE (ID, PARENT, LIFECYCLE, REVISION_SEQUENCE)
    VALUES (technicalReqId, NULL, reqDocLifecycle,  revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (qualityReqId, default, 1, default, 1, 'REQUIREMENTDOCUMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (qualityReqId, 'Quality Requirements Document', 'Quality Requirements Document', reqDocSource);
    INSERT INTO PLM_REQUIREMENTDOCUMENTTYPE (ID, PARENT, LIFECYCLE, REVISION_SEQUENCE)
    VALUES (qualityReqId, NULL, reqDocLifecycle,  revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (softwareReqId, default, 1, default, 1, 'REQUIREMENTDOCUMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (softwareReqId, 'Software Requirements Document', 'Software Requirements Document', reqDocSource);
    INSERT INTO PLM_REQUIREMENTDOCUMENTTYPE (ID, PARENT, LIFECYCLE, REVISION_SEQUENCE)
    VALUES (softwareReqId, NULL, reqDocLifecycle,  revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (customerReqId, default, 1, default, 1, 'REQUIREMENTDOCUMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (customerReqId, 'Customer Requirements Document', 'Customer Requirements Document', reqDocSource);
    INSERT INTO PLM_REQUIREMENTDOCUMENTTYPE (ID, PARENT, LIFECYCLE, REVISION_SEQUENCE)
    VALUES (customerReqId, NULL, reqDocLifecycle,  revSequence);

    requirementsSectionId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (requirementsSectionId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (requirementsSectionId, 'Requirements Section', 'Requirements Section', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (requirementsSectionId, NULL, reqLifecycle,  requirementPriorityList);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (currval('OBJECT_ID_SEQ'), 'Use Case', 'Use Case', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (currval('OBJECT_ID_SEQ'), NULL, reqLifecycle,  requirementPriorityList);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (currval('OBJECT_ID_SEQ'), 'User Story', 'User Story', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (currval('OBJECT_ID_SEQ'), NULL, reqLifecycle,  requirementPriorityList);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (currval('OBJECT_ID_SEQ'), 'Epic', 'Epic', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (currval('OBJECT_ID_SEQ'), NULL, reqLifecycle,  requirementPriorityList);

    designRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (designRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (designRequirementId, 'Design Requirement', 'Design Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (designRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    businessRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (businessRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (businessRequirementId, 'Business Requirement', 'Business Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (businessRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    solutionRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (solutionRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (solutionRequirementId, 'Solution Requirement', 'Solution Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (solutionRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    productRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (productRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (productRequirementId, 'Product Requirement', 'Product Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (productRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    stakeholderRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (stakeholderRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (stakeholderRequirementId, 'Stakeholder Requirement', 'Stakeholder Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (stakeholderRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    functionalRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (functionalRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (functionalRequirementId, 'Functional Requirement', 'Functional Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (functionalRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    nonFunctionalRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nonFunctionalRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (nonFunctionalRequirementId, 'Non-functional Requirement', 'Non-functional Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (nonFunctionalRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    systemRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (systemRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (systemRequirementId, 'System Requirement', 'System Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (systemRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    userRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (userRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (userRequirementId, 'User Requirement', 'User Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (userRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    userInterfaceRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (userInterfaceRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (userInterfaceRequirementId, 'User Interface Requirement', 'User Interface Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (userInterfaceRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    usabilityRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (usabilityRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (usabilityRequirementId, 'Usability Requirement', 'Usability Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (usabilityRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    qualityRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (qualityRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (qualityRequirementId, 'Quality Requirement', 'Quality Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (qualityRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    safetyRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (safetyRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (safetyRequirementId, 'Safety Requirement', 'Safety Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (safetyRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    efficiencyRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (efficiencyRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (efficiencyRequirementId, 'Efficiency Requirement', 'Efficiency Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (efficiencyRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    processRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (processRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (processRequirementId, 'Process Requirement', 'Process Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (processRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    implementationRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (implementationRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (implementationRequirementId, 'Implementation Requirement', 'Implementation Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (implementationRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    standardsRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (standardsRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (standardsRequirementId, 'Standards Requirement', 'Standards  Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (standardsRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    reliabilityRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (reliabilityRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (reliabilityRequirementId, 'Reliability Requirement', 'Reliability Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (reliabilityRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    portabilityRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (portabilityRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (portabilityRequirementId, 'Portability Requirementt', 'Portability Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (portabilityRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    performanceRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (performanceRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (performanceRequirementId, 'Performance Requirement', 'Performance Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (performanceRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    securityRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (securityRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (securityRequirementId, 'Security Requirement', 'Security Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (securityRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    operationalRequirementId  := nextval('OBJECT_ID_SEQ');
     INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (operationalRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (operationalRequirementId, 'Operational Requirement', 'Operational Requirement', reqDocSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (operationalRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    complianceRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (complianceRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (complianceRequirementId, 'Compliance Requirement', 'Compliance Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (complianceRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    regulatoryRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (regulatoryRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (regulatoryRequirementId, 'Regulatory Requirement', 'Regulatory Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (regulatoryRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    interoperabilityRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (interoperabilityRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (interoperabilityRequirementId, 'Interoperability Requirement', 'Interoperability Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (interoperabilityRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    maintenanceRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (maintenanceRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (maintenanceRequirementId, 'Maintenance Requirement', 'Maintenance Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (maintenanceRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    externalRequirementId  := nextval('OBJECT_ID_SEQ');
    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (externalRequirementId, default, 1, default, 1, 'REQUIREMENTTYPE');
    INSERT INTO PLM_REQUIREMENTOBJECTTYPE (ID, NAME, DESCRIPTION, NUMBER_SOURCE)
    VALUES (externalRequirementId, 'External Requirement', 'External Requirement', reqSource);
    INSERT INTO PLM_REQUIREMENTTYPE (ID, PARENT, LIFECYCLE, PRIORITY_LIST)
    VALUES (externalRequirementId, NULL, reqLifecycle,  requirementPriorityList);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Maintenance Workflow','Default Maintenance Workflow',NULL,'MAINTENANCE&REPAIR',maintenanceTypeId, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Repair Workflow','Default Repair Workflow',NULL,'MAINTENANCE&REPAIR',repairTypeId, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'NPR Workflow','Default NPR Workflow',NULL,'NPR',NULL, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'WORKFLOWTYPE');
    INSERT INTO plm_workflowtype(type_id, name, description, parent_type, assignable, assigned_type, number_source, lifecycle,REVISION_SEQUENCE)
    VALUES (currval('OBJECT_ID_SEQ'),'Custom Object Workflow','Custom Object Workflow',NULL,'CUSTOM OBJECTS',NULL, wfSource, wfLifecycle,revSequence);

    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'PROFILE');
    INSERT INTO profile(profile_id, name, default_profile, profile_data)
    VALUES (currval('OBJECT_ID_SEQ'),'Product Engineering (PE)', TRUE, '{dashboard.items,dashboard.changes,dashboard.project,dashboard.workflow,dashboard.menu,designData.vault,designData.assemblies,designData.parts,designData.drawings,designData.menu,item.items,item.products,item.assemblies,item.parts,item.documents,item.newPartRequests,item.menu,change.ecrs,change.ecos,change.dcrs,change.dcos,change.menu,project-management.requirement-doc,project-management.requirement-doc-temp,project-management.requirements,project-management.requirements-temp,project-management.projects,project-management.projects-temp,project-management.programs,project-management.programs-temp,project-management.menu,compliance.substances,compliance.specifications,compliance.declarations,compliance.menu,workflows.templates,workflows.instances,workflows.menu,import-export.import,import-export.import-bom,import-export.export,import-export.Menu,classification.itemType,classification.customType,classification.changeType,classification.project-managementType,classification.compliance,classification.workflow,classification.menu,settings.number-sources,settings.lifecycles,settings.lovs,settings.custom-attrs,settings.relationships,settings.preferences,settings.email-temp,settings.qties-measurement,settings.git-hub,settings.profiles,settings.plugins,settings.menu,sharing.menu,folders.menu}');


    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'PROFILE');
    INSERT INTO profile(profile_id, name, default_profile, profile_data)
    VALUES (currval('OBJECT_ID_SEQ'),'Product Manufacturing (PM)', TRUE, '{dashboard.items,dashboard.changes,dashboard.workflow,dashboard.sourcing,dashboard.menu,item.items,item.products,item.assemblies,item.parts,item.documents,item.newPartRequests,item.menu,change.mcos,change.variances-deviations,change.variances-waivers,change.menu,manufacturing.plants,manufacturing.assemblyLines,manufacturing.work-centers,manufacturing.machines,manufacturing.equipments,manufacturing.instruments,manufacturing.tools,manufacturing.jigs-fixtures,manufacturing.materials,mmanufacturing.manpower,manufacturing.shifts,manufacturing.operations,manufacturing.mboms,manufacturing.production-orders,manufacturing.menu,	maintenance-repairs.assets,maintenance-repairs.meters,maintenance-repairs.spare-parts,maintenance-repairs.maintenance-plans,maintenance-repairs.work-requests,maintenance-repairs.work-orders,maintenance-repairs.menu,sourcing.manufacturers,sourcing.manufacturers-parts,sourcing.suppliers,sourcing.menu,workflows.templates,workflows.instances,workflows.menu,import-export.import,import-export.import-bom,import-export.export,import-export.Menu,classification.itemType,classification.customType,classification.manufacturingType,classification.maintenance-repair,classification.sourcing,classification.workflow,classification.menu,settings.number-sources,settings.lifecycles,settings.lovs,settings.custom-attrs,settings.relationships,settings.preferences,settings.email-temp,settings.qties-measurement,settings.git-hub,settings.profiles,settings.plugins,settings.menu,sharing.menu,folders.menu}');


    INSERT INTO CASSINI_OBJECT (OBJECT_ID, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY, OBJECT_TYPE)
    VALUES (nextval('OBJECT_ID_SEQ'), default, 1, default, 1, 'PROFILE');
    INSERT INTO profile(profile_id, name, default_profile, profile_data)
    VALUES (currval('OBJECT_ID_SEQ'),'Product Quality (PQ)', TRUE,'{dashboard.items,dashboard.quality,dashboard.workflow,dashboard.sourcing,dashboard.menu,item.items,item.products,item.assemblies,item.parts,item.documents,item.newPartRequests,item.menu,quality.inspectionPlan,quality.inspections,quality.prs,quality.ncrs,quality.qcrs,quality.menu,workflows.templates,workflows.instances,workflows.menu,customers.menu,import-export.import,import-export.import-bom,import-export.export,import-export.Menu,classification.itemType,classification.customType,classification.workflow,classification.menu,settings.number-sources,settings.lifecycles,settings.lovs,settings.custom-attrs,settings.relationships,settings.preferences,settings.email-temp,settings.qties-measurement,settings.git-hub,settings.profiles,settings.plugins,settings.menu,sharing.menu,folders.menu,sourcing.manufacturers,sourcing.manufacturers-parts,sourcing.suppliers,sourcing.menu}');

    INSERT INTO PREFERENCE(ID,context,preference_key,json_value)
    VALUES(nextval('PREFERENCE_ID_SEQ'),'DEFAULT','DEFAULT_CHANGE_REQUEST_URGENCY','{"type":"LOV","typeId":'|| changeRequestLov ||'}');

    INSERT INTO PREFERENCE(ID,context,preference_key,json_value)
    VALUES(nextval('PREFERENCE_ID_SEQ'),'DEFAULT','DEFAULT_MAINTENANCE_PLAN_NUMBER_SOURCE','{"type":"AUTONUMBER","typeId":'|| maintenancePlanSource ||'}');

    INSERT INTO PREFERENCE(ID,context,preference_key,json_value)
    VALUES(nextval('PREFERENCE_ID_SEQ'),'DEFAULT','DEFAULT_NPR_NUMBER_SOURCE','{"type":"AUTONUMBER","typeId":'|| nprSource ||'}');

    INSERT INTO PREFERENCE(ID,context,preference_key,json_value)
    VALUES(nextval('PREFERENCE_ID_SEQ'),'DEFAULT','DEFAULT_SHIFT_NUMBER_SOURCE','{"type":"AUTONUMBER","typeId":'|| shiftSource ||'}');

    INSERT INTO PREFERENCE(ID,context,preference_key,json_value)
    VALUES(nextval('PREFERENCE_ID_SEQ'),'DEFAULT','DEFAULT_REVISION_SEQUENCE','{"type":"LOV","typeId":'|| revSequence ||'}');

    INSERT INTO PREFERENCE(ID,context,preference_key,json_value)
    VALUES(nextval('PREFERENCE_ID_SEQ'),'DEFAULT','DEFAULT_DOCUMENT_REVISION_SEQUENCE','{"type":"LOV","typeId":'|| docRevSequence ||'}');

    INSERT INTO PREFERENCE(ID,context,preference_key,json_value)
    VALUES(nextval('PREFERENCE_ID_SEQ'),'DEFAULT','DEFAULT_DOCUMENT_MANAGEMENT_LIFECYCLE','{"type":"LIFECYCLE","typeId":'|| docManagementLifecycle ||'}');

    INSERT INTO PREFERENCE (ID, context, preference_key, json_value)
    VALUES (nextval('PREFERENCE_ID_SEQ'), 'DEFAULT', 'DEFAULT_PPAP_CHECKLIST_LIFECYCLE','{"type":"LIFECYCLE","typeId":' || ppapChecklistLifecycle || '}');

    INSERT INTO PREFERENCE (ID, context, preference_key, json_value)
    VALUES(nextval('PREFERENCE_ID_SEQ'), 'DEFAULT', 'DEFAULT_PPAP_CHECKLIST', '{"type":"LOV","typeId":' || ppapLovChecklist || '}');

    INSERT INTO PREFERENCE (id,context,preference_key,datetime_value)
    VALUES(nextval('PREFERENCE_ID_SEQ'),'SYSTEM','SCHEMA_LAST_UPDATED',current_timestamp);

    INSERT INTO PREFERENCE (id,context,preference_key,boolean_value)
    VALUES(nextval('PREFERENCE_ID_SEQ'),'SYSTEM','SUPPLIER_AUDIT_EMAIL_REMINDER',FALSE);

    INSERT INTO PREFERENCE (ID, context, preference_key,boolean_value ,json_value)
    VALUES(nextval('PREFERENCE_ID_SEQ'), 'DEFAULT', 'DEFAULT_WORKFLOW_STATUS',TRUE ,'{"type":"WORKFLOWSTATUS","typeId":"null"}');
END $$;