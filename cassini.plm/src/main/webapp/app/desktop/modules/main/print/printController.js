define(
    [
        'app/desktop/modules/main/main.module'
    ],

    function (module) {
        module.controller('PrintController', PrintController);

        function PrintController($scope, $rootScope, $timeout, $interval, $state, $location, $application, $translate, $window) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var route = parsed.html($translate.instant("DETAILS_TAB_ROUTE")).html();
            var attributesTitle = parsed.html($translate.instant("DETAILS_TAB_ATTRIBUTES")).html();
            var bom = parsed.html($translate.instant("ITEM_DETAILS_TAB_BOM")).html();
            var changes = parsed.html($translate.instant("ITEM_DETAILS_TAB_CHANGES")).html();
            var quality = parsed.html($translate.instant("QUALITY")).html();
            var inspections = parsed.html($translate.instant("INSPECTIONS")).html();
            var mfrParts = parsed.html($translate.instant("ITEM_DETAILS_TAB_MANUFACTURER_PARTS")).html();
            var projects = parsed.html($translate.instant("PROJECTS")).html();
            var requirements = parsed.html($translate.instant("REQUIREMENTS")).html();
            var changeRequests = parsed.html($translate.instant("CHANGE_REQUESTS")).html();
            var AffectedItems = parsed.html($translate.instant("ECO_AFFECTED_ITEMS")).html();
            var AffectedMboms = parsed.html($translate.instant("MCO_AFFECTED_MBOM")).html();
            var problemItems = parsed.html($translate.instant("PROBLEM_ITEMS")).html();
            var checklist = parsed.html($translate.instant("CHECKLIST")).html();
            var problemSources = parsed.html($translate.instant("PROBLEM_SOURCES")).html();
            var RelatedItems = parsed.html($translate.instant("RELATED_ITEMS")).html();
            var capa = parsed.html($translate.instant("CAPA")).html();
            var checkList = parsed.html($translate.instant("CHECKLIST")).html();
            var auditPlan = parsed.html($translate.instant("PLAN")).html();
            var teamTitle = parsed.html($translate.instant("TEAM")).html();
            var referenceItems = parsed.html($translate.instant("REFERENCE_ITEMS")).html();
            var deliverable = parsed.html($translate.instant("DETAILS_TAB_DELIVERABLES")).html();
            var plan = parsed.html($translate.instant("PLAN")).html();
            var resources = parsed.html($translate.instant("RESOURCES")).html();
            var maintenance = parsed.html($translate.instant("MAINTENANCE")).html();
            var spareparts = parsed.html($translate.instant("SPARE_PARTS")).html();
            var operations = parsed.html($translate.instant("OPERATIONS")).html();
            var substances = parsed.html($translate.instant("SUBSTANCES")).html();
            var parts = parsed.html($translate.instant("PARTS")).html();
            var specifications = parsed.html($translate.instant("SPECIFICATIONS")).html();
            var whereUsed = parsed.html($translate.instant("DETAILS_TAB_WHERE_USED")).html();
            var contacts = parsed.html($translate.instant("CONTACTS")).html();
            var workCentersTitle = parsed.html($translate.instant("WORK_CENTERS_TITLE")).html();
            var complianceReport = parsed.html($translate.instant("COMPLIANCE_REPORT")).html();
            var file = parsed.html($translate.instant("FILES")).html();
            var problemReportsTitle = parsed.html($translate.instant("PROBLEMREPORTS")).html();

            function selectedPrintObject(objectType) {
                if (objectType == "ITEM") {
                    if ($rootScope.itemPrintClass == 'PRODUCT') {
                        vm.printOptions = [
                            {label: basic, name: 'Basic', selected: true},
                            {label: attributesTitle, name: 'Attributes', selected: true},
                            {label: bom, name: 'BOM', selected: true},
                            {label: changes, name: 'Changes', selected: false},
                            {label: quality, name: 'Quality', selected: false},
                            {label: projects, name: 'Projects', selected: false},
                            {label: mfrParts, name: 'Manufacturer Parts', selected: false},
                            {label: requirements, name: 'Requirements', selected: false},
                            {label: inspections, name: 'Inspections', selected: false},
                            {label: specifications, name: 'Specifications', selected: false},
                            {label: complianceReport, name: 'Compliance Report', selected: false},
                            {label: file, name: 'Files', selected: false}
                        ];
                    }
                    else {
                        vm.printOptions = [
                            {label: basic, name: 'Basic', selected: true},
                            {label: attributesTitle, name: 'Attributes', selected: true},
                            {label: bom, name: 'BOM', selected: true},
                            {label: changes, name: 'Changes', selected: false},
                            {label: quality, name: 'Quality', selected: false},
                            {label: projects, name: 'Projects', selected: false},
                            {label: mfrParts, name: 'Manufacturer Parts', selected: false},
                            {label: requirements, name: 'Requirements', selected: false},
                            {label: specifications, name: 'Specifications', selected: false},
                            {label: complianceReport, name: 'Compliance Report', selected: false},
                            {label: file, name: 'Files', selected: false}
                        ];
                    }

                }
                else if (objectType == "ECO") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: attributesTitle, name: 'Attributes', selected: true},
                        {label: changeRequests, name: 'Change Requests', selected: false},
                        {label: AffectedItems, name: 'Affected Items', selected: false},
                        {label: file, name: 'Files', selected: false}
                    ];
                }

                else if (objectType == "ECR") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: attributesTitle, name: 'Attributes', selected: true},
                        {label: problemReportsTitle, name: 'Problem Reports', selected: false},
                        {label: AffectedItems, name: 'Affected Items', selected: false},
                        {label: RelatedItems, name: 'Related Items', selected: false},
                        {label: file, name: 'Files', selected: false}
                    ];
                }
                else if (objectType == "DCR" || objectType == "MCO" || objectType == "DEVIATION" || objectType == "WAIVER") {
                    if ($rootScope.allMCOType == 'ITEMMCO'){
                        vm.printOptions = [
                            {label: basic, name: 'Basic', selected: true},
                            {label: attributesTitle, name: 'Attributes', selected: true},
                            {label: AffectedMboms, name: 'Affected Mbom', selected: false},
                            {label: file, name: 'Files', selected: false}
                        ];
                    }
                    else{

                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: attributesTitle, name: 'Attributes', selected: true},
                        {label: AffectedItems, name: 'Affected Items', selected: false},
                        {label: RelatedItems, name: 'Related Items', selected: false},
                        {label: file, name: 'Files', selected: false}
                    ];
                }
                }
                else if (objectType == "DCO") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: attributesTitle, name: 'Attributes', selected: true},
                        {label: changeRequests, name: 'Change Requests', selected: false},
                        {label: AffectedItems, name: 'Affected Items', selected: false},
                        {label: RelatedItems, name: 'Related Items', selected: false},
                    ];
                }
                else if (objectType == "MATERIALINSPECTIONPLAN" || objectType == "PRODUCTINSPECTIONPLAN") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: attributesTitle, name: 'Attributes', selected: true},
                        {label: checkList, name: 'Check List', selected: false}
                    ];
                }
                else if (objectType == "ITEMINSPECTION" || objectType == "MATERIALINSPECTION") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: attributesTitle, name: 'Attributes', selected: true},
                        {label: RelatedItems, name: 'Related Items', selected: false},
                        {label: checkList, name: 'Check List', selected: false}
                    ];
                }
                else if (objectType == "PR" || objectType == "NCR") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: attributesTitle, name: 'Attributes', selected: true},
                        {label: problemItems, name: 'Problem Items', selected: false},
                        {label: RelatedItems, name: 'Related Items', selected: false}
                    ];
                }
                else if (objectType == "QCR") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: attributesTitle, name: 'Attributes', selected: true},
                        {label: problemItems, name: 'Problem Items', selected: false},
                        {label: RelatedItems, name: 'Related Items', selected: false},
                        {label: problemSources, name: 'Problem Sources', selected: false},
                        {label: capa, name: 'CAPA', selected: false},
                    ];
                }
                else if (objectType == "PPAP") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: attributesTitle, name: 'Attributes', selected: true},
                        {label: checkList, name: 'Checklist', selected: false}
                    ];
                }

                else if (objectType == "SUPPLIERAUDIT") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: attributesTitle, name: 'Attributes', selected: true},
                        {label: auditPlan, name: 'Plan', selected: false},
                        {label: file, name: 'Files', selected: false}
                    ];
                }

                else if (objectType == "PROJECT") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: teamTitle, name: 'Team', selected: false},
                        {label: plan, name: 'Plan', selected: false},
                        {label: deliverable, name: 'Deliverables', selected: false},
                        {label: referenceItems, name: 'Reference Items', selected: false},
                        {label: requirements, name: 'Requirements', selected: false}
                    ];
                }
                else if (objectType == "PLANT") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true}
                    ];
                }
                else if (objectType == "ASSEMBLYLINE") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: workCentersTitle, name: 'Work Centers', selected: false}
                    ];
                }
                else if (objectType == "WORKCENTER") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: resources, name: 'Resources', selected: false}
                    ];
                }
                else if (objectType == "MACHINE") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: RelatedItems, name: 'Related Items', selected: false}
                    ];
                }

                else if (objectType == "EQUIPMENT" || objectType == "TOOL" || objectType == "JIGFIXTURE" || objectType == "MATERIAL" || objectType == "SHIFT" || objectType == "OPERATION") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true}
                    ];
                }

                else if (objectType == "MANPOWER" || objectType == "INSTRUMENT") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: maintenance, name: 'Maintenance', selected: false},
                        {label: RelatedItems, name: 'Related Items', selected: false}
                    ];
                }
                else if (objectType == "MROASSET") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: spareparts, name: 'Spare Parts', selected: false},
                        {label: maintenance, name: 'Maintenance', selected: false}

                    ];
                }
                else if (objectType == "MROMETER") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true}
                    ];
                }
                else if (objectType == "MROSPAREPART") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true}
                    ];
                }
                else if (objectType == "MROMAINTENANCEPLAN") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: operations, name: 'Operations', selected: false}
                    ];
                }

                else if (objectType == "MROWORKREQUEST") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true}
                    ];
                }
                else if (objectType == "MROWORKORDER") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: operations, name: 'Operations', selected: false},
                        {label: resources, name: 'Resources', selected: false},
                        {label: spareparts, name: 'Spare Parts', selected: false}
                    ];
                }
                else if (objectType == "PGCSUBSTANCE") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true}
                    ];
                }
                else if (objectType == "PGCSPECIFICATION") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: substances, name: 'Substances', selected: false}
                    ];
                }
                else if (objectType == "PGCDECLARATION") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: parts, name: 'Parts', selected: false},
                        {label: specifications, name: 'Specifications', selected: false}
                    ];
                }
                else if (objectType == "MANUFACTURER") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: parts, name: 'Parts', selected: false}
                    ];
                }

                else if (objectType == "MANUFACTURERPART") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: whereUsed, name: 'Where Used', selected: false},
                        {label: changes, name: 'Changes', selected: false},
                        {label: quality, name: 'Quality', selected: false}
                    ];
                }
                else if (objectType == "MFRSUPPLIER") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: contacts, name: 'Contacts', selected: false},
                        {label: parts, name: 'Parts', selected: false}
                    ];
                }
                else if (objectType == "PROGRAM") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: resources, name: 'Resources', selected: false},
                        {label: projects, name: 'Projects', selected: false},
                        {label: file, name: 'Files', selected: false}
                    ];
                }
                else if (objectType == "BOP") {
                    vm.printOptions = [
                        {label: basic, name: 'Basic', selected: true},
                        {label: route, name: 'Route', selected: true},
                    ];
                }

            }

            vm.printOptions = [];
            $rootScope.showPrintOptions = showPrintOptions;
            function showPrintOptions(objectId, objectType) {
                vm.printOptions = [];
                selectedPrintObject(objectType);
                $rootScope.showBusyIndicator();
                var options = {
                    title: "Print",
                    template: 'app/desktop/modules/main/printOptionsPanel.jsp',
                    controller: 'PrintOptionsPanelController as printOptnsVm',
                    resolve: 'app/desktop/modules/main/printOptionsPanelController',
                    width: 300,
                    showMask: true,
                    data: {
                        objectId: objectId,
                        objectType: objectType,
                        printOptions: vm.printOptions
                    },
                    buttons: [
                        {text: "Submit", broadcast: 'app.print.options'}
                    ],
                    callback: function (url) {
                        window.open(url, '_blank');

                    }
                };

                $rootScope.showSidePanel(options);

            }

            (function () {

            })();
        }
    }
);