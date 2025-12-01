// An example configuration file.
var HtmlScreenshotReporter = require('C:/Users/Lenovo/AppData/Roaming/npm/node_modules/protractor-jasmine2-screenshot-reporter');
var reporter = new HtmlScreenshotReporter({
  dest: './screenshotss',
  filename: 'my-reportt.html',
  userCss: 'my-report-styles.css'
});

exports.config = {
  directConnect: true,

  // Capabilities to be passed to the webdriver instance.
  capabilities: {
    'browserName': 'chrome'
  },

  // Framework to use. Jasmine is recommended.
  framework: 'jasmine',

  // Spec patterns are relative to the current working directory when
  // protractor is called.
  specs: [
    '../tests/login/loginTests.js',

    // //-------------------Dashboard Module-----------------
    // '../tests/dashboard/items/itemsTests.js',
    // '../tests/dashboard/changes/changesTests.js',
    // '../tests/dashboard/quality/qualityTests.js',
    // '../tests/dashboard/projects/projectsTests.js',
    // '../tests/dashboard/workflows/workflowTests.js',
    // '../tests/dashboard/sourcing/sourcingTests.js',

    //  //-----------------DesignData Module-----------------
    // '../tests/designData/vaults/all/allVaultsTestCases.js',
    // '../tests/designData/vaults/new/newVaultTestCases.js',
    // '../tests/designData/assemblies/assembliesTestCases.js',
    // '../tests/designData/parts/partsTests.js',
    // '../tests/designData/drawings/drawingTests.js',

    //  //-----------------Change module --------------------

    //  //-----------------ECO module --------------------
    //   '../tests/change/eco/all/allEcosTestCases.js',
    //   '../tests/change/eco/new/newEcoTestCases.js',
    //   '../tests/change/eco/details/basic/basicTabTestCases.js',
    //   '../tests/change/eco/details/changeRequest/changeRequestTabTestCases.js',
    //   '../tests/change/eco/details/AffectedItems/affectedItemsTabTestCases.js',
    //   '../tests/change/eco/details/workflow/workflowTabTestCases.js',
    //   '../tests/change/eco/details/files/filesTabTestCases.js',
    //   '../tests/change/eco/details/timeline/timelineTabTestCases.js',

    //  //-----------------ECR module --------------------
    // '../tests/change/ecr/all/allEcrsTestCases.js',
    // '../tests/change/ecr/new/newEcrTestCases.js',
    // '../tests/change/ecr/details/basic/basicTabTestCases.js',
    // '../tests/change/ecr/details/problemReports/problemReportsTabTestCases.js',
    // '../tests/change/ecr/details/AffectedItems/affectedItemsTabTestCases.js',
    // '../tests/change/ecr/details/relatedItems/relatedItemsTabTestCases.js',
    // '../tests/change/ecr/details/workflow/workflowTabTestCases.js',
    // '../tests/change/ecr/details/files/filesTabTestCases.js',
    // '../tests/change/ecr/details/timeline/timelineTabTestCases.js',

    //  //-----------------DCR module --------------------
    // '../tests/change/dcr/all/allDcrsTestCases.js',
    // '../tests/change/dcr/new/newDcrTestCases.js',
    // '../tests/change/dcr/details/basic/basicTabTestCases.js',
    // '../tests/change/dcr/details/AffectedItems/affectedItemsTabTestCases.js',
    // '../tests/change/dcr/details/relatedItems/relatedItemsTabTestCases.js',
    // '../tests/change/dcr/details/workflow/workflowTabTestCases.js',
    // '../tests/change/dcr/details/files/filesTabTestCases.js',
    // '../tests/change/dcr/details/timeline/timelineTabTestCases.js',


    //  //-----------------DCO module --------------------
    // '../tests/change/dco/all/allDcosTestCases.js',
    // '../tests/change/dco/new/newDcoTestCases.js',
    // '../tests/change/dco/details/basic/basicTabTestCases.js',
    // '../tests/change/dco/details/changeRequests/changeRequestsTabTestCases.js',
    // '../tests/change/dco/details/affectedItems/affectedItemsTabTestCases.js',
    // '../tests/change/dco/details/RelatedItems/relatedItemsTabTestCases.js',
    // '../tests/change/dco/details/workflow/workflowTabTestCases.js',
    // '../tests/change/dco/details/files/filesTabTestCases.js',
    // '../tests/change/dco/details/timeline/timelineTabTestCases.js',

    //  //-----------------MCO module --------------------
    //  '../tests/change/mco/all/allMcosTestCases.js',
    //  '../tests/change/mco/new/newMcoTestCases.js',
    //  '../tests/change/mco/details/basic/basicTabTestCases.js',
    //  '../tests/change/mco/details/affectedItems/affectedItemsTabTestCases.js',
    //  '../tests/change/mco/details/RelatedItems/relatedItemsTabTestCases.js',
    //  '../tests/change/mco/details/workflow/workflowTabTestCases.js',
    //  '../tests/change/mco/details/files/filesTabTestCases.js',
    //  '../tests/change/mco/details/timeline/timelineTabTestCases.js',


    //  //-----------------Deviation module --------------------
    //  '../tests/change/variance/deviation/all/allDeviationsTestCases.js',
    //  '../tests/change/variance/deviation/new/newDeviationTestCases.js',
    // '../tests/change/variance/deviation/details/basic/basicTabTestCases.js',
    // '../tests/change/variance/deviation/details/AffectedItems/affectedItemsTabTestCases.js',
    // '../tests/change/variance/deviation/details/relatedItems/relatedItemsTabTestCases.js',
    // '../tests/change/variance/deviation/details/workflow/workflowTabTestCases.js',
    // '../tests/change/variance/deviation/details/files/filesTabTestCases.js',
    // '../tests/change/variance/deviation/details/timeline/timelineTabTestCases.js',

    //  //-----------------Waiver module --------------------
    // '../tests/change/variance/waiver/all/allWaiversTestCases.js',
    // '../tests/change/variance/waiver/new/newWaiverTestCases.js',
    // '../tests/change/variance/waiver/details/basic/basicTabTestCases.js',
    // '../tests/change/variance/waiver/details/AffectedItems/affectedItemsTabTestCases.js',
    // '../tests/change/variance/waiver/details/relatedItems/relatedItemsTabTestCases.js',
    // '../tests/change/variance/waiver/details/workflow/workflowTabTestCases.js',
    // '../tests/change/variance/waiver/details/files/filesTabTestCases.js',
    // '../tests/change/variance/waiver/details/timeline/timelineTabTestCases.js',

    //  //-----------------Customer module --------------------
    // '../tests/customer/all/allCustomersTestCases.js',
    // '../tests/customer/new/newCustomerTestCases.js',
    // '../tests/customer/details/basic/basicTabTestCases.js',
    // '../tests/customer/details/problemReports/problemReportsTabTestCases.js',
    // '../tests/customer/details/files/filesTabTestCases.js',
    // '../tests/customer/details/timeline/timelineTabTestCases.js',

    //  // --------------------------------------------Quality Module ------------------------------
    //  // ---------------------------Inspection plan ---------------------
    // '../tests/quality/inspectionPlan/all/allInspectionPlansTestCases.js',
    //  '../tests/quality/inspectionPlan/new/newInspectionPlanTestCases.js',
    //  '../tests/quality/inspectionPlan/details/basic/basicTabTestCases.js',
    //  '../tests/quality/inspectionPlan/details/checklist/checklistTabTestCases.js',
    //  '../tests/quality/inspectionPlan/details/workflow/workflowTabTestCases.js',
    //  '../tests/quality/inspectionPlan/details/files/filesTabTestCases.js',
    //  '../tests/quality/inspectionPlan/details/timeline/timelineTabTestCases.js',

    //  // ---------------------------Inspections ---------------------
    //  // ---------------------------Items Inspections ---------------------
    '../tests/quality/inspections/items/all/allInspectionsTestCases.js',
    '../tests/quality/inspections/items/new/newInspectionTestCases.js',
    '../tests/quality/inspections/items/details/basic/basicTabTestCases.js',
    '../tests/quality/inspections/items/details/checklist/checklistTabTestCases.js',
    '../tests/quality/inspections/items/details/relatedItems/relatedItemsTabTestCases.js',
    '../tests/quality/inspections/items/details/workflow/workflowTabTestCases.js',
    '../tests/quality/inspections/items/details/files/filesTabTestCases.js',
    '../tests/quality/inspections/items/details/timeline/timelineTabTestCases.js',

    //  // ---------------------------Material Inspections ---------------------
    '../tests/quality/inspections/materials/all/allInspectionsTestCases.js',
    '../tests/quality/inspections/materials/new/newInspectionTestCases.js',
    '../tests/quality/inspections/materials/details/basic/basicTabTestCases.js',
    '../tests/quality/inspections/materials/details/checklist/checklistTabTestCases.js',
    '../tests/quality/inspections/materials/details/relatedItems/relatedItemsTabTestCases.js',
    '../tests/quality/inspections/materials/details/workflow/workflowTabTestCases.js',
    '../tests/quality/inspections/materials/details/files/filesTabTestCases.js',
    '../tests/quality/inspections/materials/details/timeline/timelineTabTestCases.js',


    // '../tests/quality/problemReport/all/allPRsTestCases.js',
    // '../tests/quality/problemReport/new/newPRTestCases.js',
    // '../tests/quality/ncr/all/allNcrsTestCases.js',
    // '../tests/quality/ncr/new/newNcrTestCases.js',
    //  '../tests/quality/qcr/all/allQcrsTestCases.js',
    //  '../tests/quality/qcr/new/newQcrTestCases.js',

    //  // ---------------------------Project Management Module ---------------------
    // '../tests/projectManagement/requirement/all/allReqDocumentsTestCases.js',
    // '../tests/projectManagement/requirement/new/newReqDocumentTestCases.js',
    // '../tests/projectManagement/projectTemplates/all/allProjectTemplatesTestCases.js',
    // '../tests/projectManagement/projectTemplates/new/newProjectTemplateTestCases.js',
    // '../tests/projectManagement/project/all/allProjectsTestCases.js',
    // '../tests/projectManagement/project/new/newProjectTestCases.js',

    //  // ---------------------------Sourcing Module ---------------------
    // '../tests/sourcing/manufacturer/all/allManufacturersTestCases.js',
    // '../tests/sourcing/manufacturer/new/newManufacturerTestCases.js',
    // '../tests/sourcing/manufacturerParts/all/allMfrPartsTestCases.js',
    //  '../tests/sourcing/manufacturerParts/new/newMfrPartTestCases.js',
    //  '../tests/sourcing/supplier/all/allSuppliersTestCases.js',
    //  '../tests/sourcing/supplier/new/newSupplierTestCases.js',

    //  // ---------------------------Compliance Module ---------------------
    //  '../tests/compliance/substances/all/allSubstancesTestCases.js',
    //  '../tests/compliance/substances/new/newSubstanceTestCases.js',
    //  '../tests/compliance/specifications/all/allSpecsTestCases.js',
    //  '../tests/compliance/specifications/new/newSpecTestCases.js',
    //  '../tests/compliance/declarations/all/allDeclarationsTestCases.js',
    //  '../tests/compliance/declarations/new/newDeclarationTestCases.js',

    //  //-----------------Plants module --------------------
    // '../tests/manufacturing/plants/all/allPlantsTestCases.js',
    // '../tests/manufacturing/plants/new/newPlantTestCases.js',
    // '../tests/manufacturing/plants/details/basic/basicTabTestCases.js',
    // '../tests/manufacturing/plants/details/files/filesTabTestCases.js',
    // '../tests/manufacturing/plants/details/timeline/timelineTabTestCases.js',

    //  //-----------------AssemblyLines module --------------------
    // '../tests/manufacturing/assemblyLines/all/allAssemblyLinesTestCases.js',
    // '../tests/manufacturing/assemblyLines/new/newAssemblyLineTestCases.js',
    // '../tests/manufacturing/assemblyLines/details/basic/basicTabTestCases.js',
    // '../tests/manufacturing/assemblyLines/details/workCenters/workCenterTabTestCases.js',
    // '../tests/manufacturing/assemblyLines/details/files/filesTabTestCases.js',
    // '../tests/manufacturing/assemblyLines/details/timeline/timelineTabTestCases.js',

    //  //-----------------WorkCenters module ----------------------
    // '../tests/manufacturing/workCenters/all/allWorkCentersTestCases.js',
    // '../tests/manufacturing/workCenters/new/newWorkCenterTestCases.js',
    // '../tests/manufacturing/workCenters/details/basic/basicTabTestCases.js',
    // '../tests/manufacturing/workCenters/details/resources/resourcesTabTestCases.js',
    // '../tests/manufacturing/workCenters/details/files/filesTabTestCases.js',
    // '../tests/manufacturing/workCenters/details/timeline/timelineTabTestCases.js',

    //  //-----------------Machines module ----------------------
    // '../tests/manufacturing/machines/all/allMachinesTestCases.js',
    // '../tests/manufacturing/machines/new/newMachineTestCases.js',
    // '../tests/manufacturing/machines/details/basic/basicTabTestCases.js',
    // '../tests/manufacturing/machines/details/relatedItems/relatedItemsTabTestCases.js',
    // '../tests/manufacturing/machines/details/files/filesTabTestCases.js',
    // '../tests/manufacturing/machines/details/timeline/timelineTabTestCases.js',

    //  //-----------------Equipments module ----------------------
    // '../tests/manufacturing/equipments/all/allEquipmentsTestCases.js',
    // '../tests/manufacturing/equipments/new/newEquipmentTestCases.js',
    // '../tests/manufacturing/equipments/details/basic/basicTabTestCases.js',
    // '../tests/manufacturing/equipments/details/files/filesTabTestCases.js',
    // '../tests/manufacturing/equipments/details/timeline/timelineTabTestCases.js',

    //  //-----------------Instruments module ----------------------
    // '../tests/manufacturing/instruments/all/alllInstrumentsTestCases.js',
    // '../tests/manufacturing/instruments/new/newInstrumentTestCases.js',
    // '../tests/manufacturing/instruments/details/basic/basicTabTestCases.js',
    // '../tests/manufacturing/instruments/details/files/filesTabTestCases.js',
    // '../tests/manufacturing/instruments/details/timeline/timelineTabTestCases.js',

    //  //-----------------Tools module ----------------------
    // '../tests/manufacturing/tools/all/allToolsTestCases.js',
    // '../tests/manufacturing/tools/new/newToolTestCases.js',
    // '../tests/manufacturing/tools/details/basic/basicTabTestCases.js',
    // '../tests/manufacturing/tools/details/files/filesTabTestCases.js',
    // '../tests/manufacturing/tools/details/timeline/timelineTabTestCases.js',

    // //  //-----------------Jigs module ----------------------
    // '../tests/manufacturing/jigsAndFixtures/jigs/all/allJigsTestCases.js',
    // '../tests/manufacturing/jigsAndFixtures/jigs/new/newJigTestCases.js',
    // '../tests/manufacturing/jigsAndFixtures/jigs/details/basic/basicTabTestCases.js',
    // '../tests/manufacturing/jigsAndFixtures/jigs/details/files/filesTabTestCases.js',
    // '../tests/manufacturing/jigsAndFixtures/jigs/details/timeline/timelineTabTestCases.js',

    //  //-----------------Fixture module ----------------------
    // '../tests/manufacturing/jigsAndFixtures/fixtures/all/allFixturesTestCases.js',
    // '../tests/manufacturing/jigsAndFixtures/fixtures/new/newFixtureTestCases.js',
    // '../tests/manufacturing/jigsAndFixtures/fixtures/details/basic/basicTabTestCases.js',
    // '../tests/manufacturing/jigsAndFixtures/fixtures/details/files/filesTabTestCases.js',
    // '../tests/manufacturing/jigsAndFixtures/fixtures/details/timeline/timelineTabTestCases.js',


    //  //-----------------Matrials module ----------------------
    // '../tests/manufacturing/materials/all/allMaterialsTestCases.js',
    // '../tests/manufacturing/materials/new/newMaterialTestCases.js',
    // '../tests/manufacturing/materials/details/basic/basicTabTestCases.js',
    // '../tests/manufacturing/materials/details/files/filesTabTestCases.js',
    // '../tests/manufacturing/materials/details/timeline/timelineTabTestCases.js',

    //  //-----------------Manpower module ----------------------
    // '../tests/manufacturing/manpower/all/allManpowerTestCases.js',
    // '../tests/manufacturing/manpower/new/newManpowerTestCases.js',
    // '../tests/manufacturing/manpower/details/basic/basicTabTestCases.js',
    // '../tests/manufacturing/manpower/details/files/filesTabTestCases.js',
    // '../tests/manufacturing/manpower/details/timeline/timelineTabTestCases.js',

    //  //-----------------Shifts module ----------------------
    // '../tests/manufacturing/shifts/all/allShiftsTestCases.js',
    // '../tests/manufacturing/shifts/new/newShiftTestCases.js',
    // '../tests/manufacturing/shifts/details/basic/basicTabTestCases.js',
    // '../tests/manufacturing/shifts/details/files/filesTabTestCases.js',
    // '../tests/manufacturing/shifts/details/timeline/timelineTabTestCases.js',

    //  //-----------------Operations module ----------------------
    // '../tests/manufacturing/operations/all/allOperationsTestCases.js',
    // '../tests/manufacturing/operations/new/newOperationTestCases.js',
    // '../tests/manufacturing/operations/details/basic/basicTabTestCases.js',
    // '../tests/manufacturing/operations/details/files/filesTabTestCases.js',
    // '../tests/manufacturing/operations/details/timeline/timelineTabTestCases.js',

    //  //-----------------Maintenance And Repair module ----------------------
    // '../tests/maintenanceAndRepairs/asserts/assertTests.js',
    // '../tests/maintenanceAndRepairs/meters/meterTests.js',
    // '../tests/maintenanceAndRepairs/spareParts/sparePartTests.js',
    // '../tests/maintenanceAndRepairs/maintenancePlans/maintenancePlanTests.js',
    // '../tests/maintenanceAndRepairs/workRequests/workRequestTests.js',
    // '../tests/maintenanceAndRepairs/workOrders/workOrderTests.js',

    //  //-----------------Admin module ----------------------
    // '../tests/admin/user/userTests.js',
    // '../tests/admin/role/roleTests.js',
    // '../tests/admin/permission/permissionTests.js',

    //  //-----------------Settings module ----------------------
    // '../tests/settings/numberSources/numberSourcesTests.js',
    // '../tests/settings/lifeCycles/lifeCycleTests.js',
    // '../tests/settings/listOfValues/listOfValuesTests.js',
    // '../tests/settings/customAttributes/customeAttributeTests.js',
    // '../tests/settings/relationships/relationshipTests.js',

  ],

  // Options to be passed to Jasmine.
  jasmineNodeOpts: {
    defaultTimeoutInterval: 75000
  },


  // Setup the report before any tests start
  beforeLaunch: function () {
    return new Promise(function (resolve) {
      reporter.beforeLaunch(resolve);
    });
  },

  // Assign the test reporter to each running instance
  onPrepare: function () {
    jasmine.getEnv().addReporter(reporter);
  },

  // Close the report after all tests finish
  afterLaunch: function (exitCode) {
    return new Promise(function (resolve) {
      reporter.afterLaunch(resolve.bind(this, exitCode));
    });
  },


  // Assign the test reporter to each running instance
  // onPrepare: function () {

  //   var jasmineReporters = require('C:/Users/Lenovo/AppData/Roaming/npm/node_modules/jasmine-reporters');
  //   jasmine.getEnv().addReporter(new jasmineReporters.JUnitXmlReporter({
  //     consolidateAll: true,
  //     savePath: './',
  //     filePrefix: 'xmlresults'
  //   }));

  //   var fs = require('C:/Users/Lenovo/AppData/Roaming/npm/node_modules/fs-extra');

  //   fs.emptyDir('screenshots/', function (err) {
  //     console.log(err);
  //   });

  //   jasmine.getEnv().addReporter({
  //     specDone: function (result) {
  //       if (result.status == 'failed') {
  //         browser.getCapabilities().then(function (caps) {
  //           var browserName = caps.get('browserName');

  //           browser.takeScreenshot().then(function (png) {
  //             var stream = fs.createWriteStream('screenshots/' + browserName + '-' + result.fullName + '.png');
  //             stream.write(Buffer.from(png, 'base64'));
  //             stream.end();
  //           });
  //         });
  //       }
  //     }
  //   });

  //   jasmine.getEnv().addReporter(reporter);
  // },

  //HTMLReport called once tests are finished
  onComplete: function () {
    var browserName, browserVersion;
    var capsPromise = browser.getCapabilities();

    capsPromise.then(function (caps) {
      browserName = caps.get('browserName');
      browserVersion = caps.get('version');
      platform = caps.get('platform');

      var HTMLReport = require('C:/Users/Lenovo/AppData/Roaming/npm/node_modules/protractor-html-reporter-2');

      testConfig = {
        reportTitle: 'Protractor Test Execution Report',
        outputPath: './',
        outputFilename: 'ProtractorTestReport',
        screenshotPath: './screenshotss',
        testBrowser: browserName,
        browserVersion: browserVersion,
        modifiedSuiteName: false,
        screenshotsOnlyOnFailure: true,
        testPlatform: platform
      };
      new HTMLReport().from('xmlresults.xml', testConfig);
    });
  }
};
