var urlsPage = function () {
    var EC = protractor.ExpectedConditions;
    this.loginUrl = 'http://localhost:8085/#/login';
    this.baseUrl = 'http://localhost:8085/#/app/';
    var baseUrl = 'http://localhost:8085/#/app/';
    this.homeUrl = baseUrl + 'home';

    // ----------------Dashboard Urls --------------------
    this.allDashboardItems = baseUrl + 'dashboards/items';
    this.allDashboardChanges = baseUrl + 'dashboards/changes';
    this.allDashboardQuality = baseUrl + 'dashboards/quality';
    this.allDashboardProjects = baseUrl + 'dashboards/projects';
    this.allDashboardWorkflows = baseUrl + 'dashboards/workflows';
    this.allDashboardSourcings = baseUrl + 'dashboards/oems';

    // ----------------Design Data Urls --------------------
    this.allVaults = baseUrl + 'pdm/vaults';
    this.allAssemblies = baseUrl + 'pdm/files/ASSEMBLY';
    this.allParts = baseUrl + 'pdm/files/PART';
    this.allDrawings = baseUrl + 'pdm/drawings';

    // ----------------Items Module Urls --------------------
    this.allItems = baseUrl + 'items/all';
    this.allProductsItems= baseUrl + 'items/products';
    this.allAssembliesItems = baseUrl + 'items/assemblies';
    this.allPartsItems = baseUrl + 'items/parts';
    this.allDocumentsItems = baseUrl + 'items/documents';
    this.allNewPartRequestsItems = baseUrl + 'nprs/all';

    //  --------------- Change Urls -----------------
    this.allEcos = baseUrl + 'changes/eco/all';
    this.allEcrs = baseUrl + 'changes/ecr/all';
    this.allDcrs = baseUrl + 'changes/dcr/all';
    this.allDcos = baseUrl + 'changes/dco/all';
    this.allMcos = baseUrl + 'changes/mco/all';
    this.allDeviation = baseUrl + 'changes/variance/all/deviation';
    this.allWaiver = baseUrl + 'changes/variance/all/waiver';

    //  --------------- Quality Urls -----------------
    this.allInspectionPlan = baseUrl + 'pqm/inspectionPlan/all';
    this.allInspections = baseUrl + 'pqm/inspections/all';
    this.allProblemReports = baseUrl + 'pqm/problemReport/all';
    this.allNcrs = baseUrl + 'pqm/ncr/all';
    this.allQcrs = baseUrl + 'pqm/qcr/all';

    //  --------------- Project Management Urls -----------------
    this.allReqDocuments = baseUrl + 'req/document/all';
    this.allProjectTemplates = baseUrl + 'templates/all';
    this.allProjects = baseUrl + 'pm/project/all';

    //  --------------- Sourcing Module Urls -----------------
    this.allManufacturers = baseUrl + 'mfr/all';
    this.allManufacturerParts = baseUrl + 'mfr/mfrparts/all';
    this.allSuppliers = baseUrl + 'mfr/supplier/all';

    //  --------------- Compliance Module Urls -----------------
    this.allSubstances = baseUrl + 'compliance/substance/all'
    this.allSpecifications = baseUrl + 'compliance/specification/all'
    this.allDeclarations = baseUrl + 'compliance/declaration/all'

    //  --------------- Customer Module Urls -----------------
    this.allCustomers = baseUrl + 'customers/all'

    //  --------------- Manufacturing Module Urls -----------------
    this.allPlants = baseUrl + 'mes/plant/all'
    this.allAssembliLines = baseUrl + 'mes/assemblyline/all'
    this.allWorkCenters = baseUrl + 'mes/workcenter/all'
    this.allMachines = baseUrl + 'mes/machine/all'
    this.allEquipments = baseUrl + 'mes/equipment/all'
    this.allInstruments = baseUrl + 'mes/instrument/all'
    this.allTools = baseUrl + 'mes/tool/all'
    this.allJigsAndFixtures = baseUrl + 'mes/jigsAndFixtures/all'
    this.allMaterials = baseUrl + 'mes/material/all'
    this.allManpower = baseUrl + 'mes/manpower/all'
    this.allShifts = baseUrl + 'mes/shift/all'
    this.allOperations = baseUrl + 'mes/operation/all'

    //  --------------- Manintenance and Repairs Module Urls -----------------
    this.allAssets = baseUrl + 'mro/asset/all'
    this.allMeters = baseUrl + 'mro/meter/all'
    this.allSpareParts = baseUrl + 'mro/sparePart/all'
    this.allMaintenancePlans = baseUrl + 'mro/maintenanceplans/all'
    this.allWorkRequests = baseUrl + 'mro/workRequest/all'
    this.allWorkOrders = baseUrl + 'mro/workorders/all'

     //  ---------------Admin Module Urls -----------------
     this.adminUsersUrl = baseUrl + 'newadmin/users'
     this.adminRolesUrl = baseUrl + 'newadmin/roles'
     this.adminPermissionsUrl = baseUrl + 'newadmin/permissions'

     //-------------settings--------------------------------
     this.numberSourceUrl= baseUrl + 'settings/autonumbers';
     this.lifecyclesUrl= baseUrl + 'settings/lifecycles';
     this.lovsUrl= baseUrl + 'settings/lovs';
     this.customAttributesUrl= baseUrl + 'settings/properties';
     this.relationshipsUrl= baseUrl + 'settings/relationships';
     this.preferencesUrl= baseUrl + 'settings/preferences';
     this.emailTemplatesUrl= baseUrl + 'settings/email_templates';
     this.quantityOfMeasurementUrl= baseUrl + 'settings/measurements';
     this.githubUrl= baseUrl + 'settings/github';
     this.applicationProfilesUrl= baseUrl + 'settings/profiles';
     this.pluginsUrl= baseUrl + 'settings/plugins';


    this.byCss = function (value) { return element(by.css(value)); };
    this.byId = function (value) { return element(by.id(value)); };
    this.byModel = function (value) { return element(by.model(value)); };

    this.url_id;

};
module.exports = new urlsPage();