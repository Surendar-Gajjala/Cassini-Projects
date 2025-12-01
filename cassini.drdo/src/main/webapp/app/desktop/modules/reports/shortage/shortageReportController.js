define(['app/desktop/modules/reports/reports.module',
        'jspdf',
        'jspdf-autotable',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/bomService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/drdoApplication',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module, jsPDF) {
        module.controller('ShortageReportController', ShortageReportController);

        function ShortageReportController($scope, $rootScope, $timeout, $window, $state, $filter, $stateParams, $uibModal, compare, $translate, BomService, CommonService) {

            $rootScope.viewInfo.icon = "fa fa-file-image-o";
            $rootScope.viewInfo.title = "Material Status Report";

            var vm = this;

            vm.loading = true;
            vm.sidePanel = false;
            vm.selectBomTitle = "Select BOM";
            vm.bomInstanceTitle = "Select Missile";
            vm.selectedBom = null;
            vm.bomInstance = null;
            vm.selectedSection = null;
            vm.selectedSubsystem = null;
            vm.selectedUnit = null;
            vm.showSearchBox = false;
            vm.showSearchButton = false;

            vm.showBomInfoPanel = showBomInfoPanel;
            vm.onSelectBom = onSelectBom;
            vm.onSelectInstance = onSelectInstance;
            vm.selectSection = selectSection;
            vm.selectSubsystem = selectSubsystem;
            vm.selectUnit = selectUnit;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.loadReport = loadReport;
            vm.selectWorkCenter = selectWorkCenter;

            function loadBoms() {
                BomService.getAllBoms().then(
                    function (data) {
                        vm.boms = data;
                        if (vm.boms.length == 0) {
                            vm.selectBomTitle = "No BOM";
                        } else {
                            if (vm.boms.length == 1) {
                                vm.selectedBom = vm.boms[0];
                                onSelectBom(vm.selectedBom);
                            }
                        }
                        loadWorkCenters();
                    }
                )
            }

            function loadWorkCenters() {
                CommonService.getLovByName("Work Centers").then(
                    function (data) {
                        if (data.values != null || data.values != undefined)
                            vm.workCenters = data.values;
                    });
            }

            function showBomInfoPanel(show) {
                if (show) {
                    $('#reportsInfoPanel').show('slide', {direction: 'left'}, 600);
                    vm.sidePanel = true;
                }
                else {
                    $('#reportsInfoPanel').hide('slide', {direction: 'left'}, 600);
                    vm.sidePanel = false;
                }
            }

            function onSelectBom(bom) {
                vm.reportItems = [];
                vm.bomInstance = null;
                vm.selectedSection = null;
                vm.selectedSubsystem = null;
                vm.selectedUnit = null;
                vm.showSearchBox = false;
                vm.showSearchButton = false;
                vm.searchFilter.bom = bom.id;
                vm.selectedPath = bom.item.itemMaster.itemName;
                $rootScope.showBusyIndicator($('.view-container'));
                BomService.getBomInstances(bom.id).then(
                    function (data) {
                        vm.bomInstances = data;
                        vm.bomInstances = $filter('orderBy')(vm.bomInstances, 'item.instanceName');

                        angular.forEach(vm.bomInstances, function (instance) {
                            instance.selected = false;
                        })

                        if (vm.bomInstances.length == 0) {
                            vm.bomInstanceTitle = "No Instances";
                        } else {
                            vm.bomInstanceTitle = "Select Missile";
                        }
                        $rootScope.hideBusyIndicator();
                        $scope.$evalAsync();
                    }
                )
            }

            function onSelectInstance(bomInstance) {
                vm.reportItems = [];
                vm.selectedSection = null;
                vm.selectedSubsystem = null;
                vm.selectedUnit = null;
                vm.showSearchBox = false;
                vm.showSearchButton = false;
                vm.searchFilter.section = '';
                vm.searchFilter.subsystem = '';
                vm.searchFilter.unit = '';
                $rootScope.showBusyIndicator($('.view-container'));

                var admin = $rootScope.hasPermission('permission.admin.all');
                var versity = $rootScope.hasPermission('permission.bom.all');
                if (!versity) {
                    versity = $rootScope.hasPermission('permission.versity.group');
                }
                BomService.getSectionsByInstance(vm.bomInstance.id, admin, versity).then(
                    function (data) {
                        vm.instanceSections = data;
                        vm.showSearchBox = false;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function selectSection(section) {
                vm.selectedSubsystem = null;
                vm.selectedUnit = null;
                vm.searchFilter.subsystem = '';
                vm.searchFilter.unit = '';
                //vm.searchFilter.workCenter = null;
                vm.reportItems = [];
                vm.showSearchBox = true;
                vm.showSearchButton = true;
                $rootScope.showBusyIndicator($('.view-container'));
                vm.searchFilter.section = section.id;
                vm.selectedPath = vm.selectedBom.item.itemMaster.itemName + " / " + section.typeRef.name;
                BomService.getChildrenByBomItem(section.id).then(
                    function (data) {
                        vm.subsystems = data;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function selectSubsystem(subsystem) {
                vm.selectedUnit = null;
                vm.searchFilter.unit = '';
                //vm.searchFilter.workCenter = null;
                vm.reportItems = [];
                $rootScope.showBusyIndicator($('.view-container'));
                vm.searchFilter.subsystem = subsystem.id;
                vm.selectedPath = vm.selectedBom.item.itemMaster.itemName + " / " + vm.selectedSection.typeRef.name + " / " + subsystem.typeRef.name;
                BomService.getChildrenByBomItem(subsystem.id).then(
                    function (data) {
                        vm.units = data;
                        vm.showSearchBox = true;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function selectUnit(unit) {
                //$rootScope.showBusyIndicator($('.view-container'));
                vm.searchFilter.unit = unit.id;
                vm.selectedPath = vm.selectedBom.item.itemMaster.itemName + " / " + vm.selectedSection.typeRef.name + " / " + vm.selectedSubsystem.typeRef.name + " / " + unit.typeRef.name;
                /*BomService.searchItemReport(vm.searchFilter, vm.selectedMissileIds).then(
                 function (data) {
                 vm.reportItems = data.itemDetailsReportDtos;
                 $rootScope.hideBusyIndicator();
                 }
                 )*/
            }

            function selectWorkCenter(workCenter) {
                if (workCenter == null) {
                    vm.searchFilter.workCenter = null;
                } else {
                    vm.searchFilter.workCenter = workCenter;
                }

            }

            vm.searchFilter = {
                searchQuery: null,
                bom: '',
                section: '',
                subsystem: '',
                unit: '',
                workCenter: null
            };

            vm.fields = [
                {
                    field: 'R',
                    name: "Requested",
                    value: "requested",
                    selected: true
                },
                {
                    field: 'A',
                    name: "Allocated",
                    value: "allocated",
                    selected: true
                },
                {
                    field: 'I',
                    name: "Issued",
                    value: "issued",
                    selected: true
                },
                {
                    field: 'IP',
                    name: "Issue Process",
                    value: "issueProcess",
                    selected: true
                },
                {
                    field: 'F',
                    name: "Failure",
                    value: "failure",
                    selected: true
                },
                {
                    field: 'S',
                    name: "Shortage",
                    value: "shortage",
                    selected: true
                }
            ]

            vm.missileFieldMap = new Hashtable;
            vm.printFieldHeading = null;

            $rootScope.getSelectedField = getSelectedField;
            function getSelectedField(field) {
                var hasField = true;
                var field = vm.missileFieldMap.get(field);

                if (field == null || field == undefined) {
                    hasField = false;
                }

                return hasField;
            }

            vm.selectField = selectField;
            function selectField(field) {
                if (field.selected) {
                    vm.missileFieldMap.put(field.field, field);
                } else {
                    vm.missileFieldMap.remove(field.field);
                }
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.searchFilter.searchQuery = freeText;
                } else {
                    vm.searchFilter.searchQuery = null;
                }
            }

            function loadReport() {
                $rootScope.showBusyIndicator($('.view-container'));
                BomService.searchItemReport(vm.searchFilter, vm.selectedMissileIds).then(
                    function (data) {
                        vm.reportItems = data.itemDetailsReportDtos;
                        vm.selectedItemsToPrint = [];
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function resetPage() {
                vm.searchFilter.searchQuery = null;
            }

            vm.selectedAll = false;
            vm.selectAllMissiles = selectAllMissiles;
            vm.selectMissile = selectMissile;

            vm.selectedMissiles = [];
            vm.selectedMissileIds = [];
            vm.sections = [];

            function selectAllMissiles() {
                if (vm.selectedAll) {
                    vm.selectedMissileIds = [];
                    vm.selectedMissiles = [];
                    angular.forEach(vm.bomInstances, function (instance) {
                        instance.selected = true;
                        vm.selectedMissileIds.push(instance.id);
                        vm.selectedMissiles.push(instance);
                    })

                    if (vm.sections.length == 0) {
                        BomService.getSectionsByBom(vm.selectedBom.id).then(
                            function (data) {
                                vm.sections = data;
                            }
                        )
                    }

                    vm.showSearchButton = true;
                    vm.showSearchBox = true;
                } else {
                    angular.forEach(vm.bomInstances, function (instance) {
                        instance.selected = false;
                    })
                    vm.selectedMissileIds = [];
                    vm.selectedMissiles = [];
                    vm.showSearchButton = false;
                    vm.showSearchBox = false;
                }

            }

            function selectMissile(missile) {
                if (missile.selected) {
                    vm.selectedMissileIds.push(missile.id);
                    vm.selectedMissiles.push(missile);
                    if (vm.selectedMissiles.length == vm.bomInstances.length) {
                        vm.selectedAll = true;
                    }

                    vm.showSearchButton = true;
                    vm.showSearchBox = true;
                } else {
                    vm.selectedMissiles.splice(vm.selectedMissiles.indexOf(missile), 1);
                    vm.selectedMissileIds.splice(vm.selectedMissileIds.indexOf(missile.id), 1);
                    vm.selectedAll = false;

                    if (vm.selectedMissiles.length == 0) {
                        vm.showSearchButton = false;
                        vm.showSearchBox = false;
                        vm.selectedSection = null;
                        vm.selectedSubsystem = null;
                        vm.selectedUnit = null;
                        vm.searchFilter.section = '';
                        vm.searchFilter.subsystem = '';
                        vm.searchFilter.unit = '';
                    }
                }

                if (vm.sections.length == 0) {
                    BomService.getSectionsByBom(vm.selectedBom.id).then(
                        function (data) {
                            vm.sections = data;
                        }
                    )
                }
            }

            vm.selectItem = selectItem;
            vm.selectedItemsToPrint = [];
            function selectItem(item) {
                if (item.selected) {
                    vm.selectedItemsToPrint.push(item);
                } else {
                    vm.selectedItemsToPrint.splice(vm.selectedItemsToPrint.indexOf(item), 1);
                }
            }


            vm.printReport = printReport;
            function printReport() {
                vm.printFieldHeading = "";
                var fieldSelected = false;
                angular.forEach(vm.fields, function (field) {
                    if (field.selected) {
                        fieldSelected = true;
                        if (vm.printFieldHeading == "") {
                            vm.printFieldHeading = field.field;
                        } else {
                            vm.printFieldHeading = vm.printFieldHeading + " / " + field.field;
                        }
                    }
                })
                if (vm.selectedItemsToPrint.length > 0) {
                    angular.forEach(vm.selectedItemsToPrint, function (reportItem) {
                        angular.forEach(vm.selectedMissileIds, function (missileId) {
                            reportItem.listMap[missileId].list = "";
                            angular.forEach(vm.fields, function (field) {
                                if (field.selected) {
                                    if (reportItem.listMap[missileId].list == "") {
                                        reportItem.listMap[missileId].list = reportItem.listMap[missileId][field.value] + "";
                                    } else {
                                        reportItem.listMap[missileId].list = reportItem.listMap[missileId].list + " / " + reportItem.listMap[missileId][field.value];
                                    }

                                }
                            })
                        })
                    })
                } else {
                    angular.forEach(vm.reportItems, function (reportItem) {
                        angular.forEach(vm.selectedMissileIds, function (missileId) {
                            reportItem.listMap[missileId].list = "";
                            angular.forEach(vm.fields, function (field) {
                                if (field.selected) {
                                    if (reportItem.listMap[missileId].list == "") {
                                        reportItem.listMap[missileId].list = reportItem.listMap[missileId][field.value] + "";
                                    } else {
                                        reportItem.listMap[missileId].list = reportItem.listMap[missileId].list + " / " + reportItem.listMap[missileId][field.value];
                                    }

                                }
                            })
                        })
                    })
                }

                if (fieldSelected) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    $timeout(function () {
                        var pdf;
                        if (vm.selectedMissileIds.length < 5) {
                            pdf = new jsPDF('l', 'pt', 'a4');
                        } else {
                            pdf = new jsPDF('l', 'pt', 'a3');
                        }
                        var pageWidth = pdf.internal.pageSize.width;
                        pdf.setFont('arial narrow');

                        var pageContent = function (data) {
                            //Header

                            pdf.setFontStyle('bold');
                            pdf.setFontSize(20);
                            pdf.text(pageWidth / 2.5, 50, "Material Status Report");
                            pdf.setFontSize(13);
                            pdf.setTextColor(7, 7, 7);
                            pdf.setFontStyle('normal');
                            var imgData = $rootScope.drdoImage;
                            pdf.addImage(imgData, 'JPG', pageWidth - 160, 45, 100, 100);


                            var today = moment(new Date());
                            var todayStr = today.format('DD/MM/YYYY HH:mm');
                            pdf.text(pageWidth - 150, 25, 'Date : ' + todayStr);

                            pdf.setFontSize(12);
                            pdf.setFontStyle('normal');
                            pdf.text(25, 100, 'Items : ' + vm.selectedPath);
                            pdf.setFontSize(10);
                            pdf.text(25, 140, "R : Requested, A : Allocated, I : Issued, IP : Issue Process, F : Failure, S : Shortage");
                            pdf.setFontSize(13);
                            /*pdf.text(25, 70, 'System : ' + vm.selectedBom.item.itemMaster.itemName);
                             pdf.text(25, 90, 'Missile : ' + vm.bomInstance.item.instanceName);
                             pdf.text(25, 110, 'Section : ' + vm.selectedSection.typeRef.name);
                             if (vm.selectedSubsystem != null) {
                             pdf.text(25, 130, 'Subsystem : ' + vm.selectedSubsystem.typeRef.name);
                             }

                             if (vm.selectedUnit != null) {
                             pdf.text(25, 150, 'Unit : ' + vm.selectedUnit.typeRef.name);
                             }*/

                            // FOOTER
                            pdf.setFontSize(16);

                            var str = "Page " + data.pageCount;

                            pdf.text(str, pdf.internal.pageSize.width - 75, pdf.internal.pageSize.height - 10);
                        };

                        var personDetail;
                        if (vm.selectedItemsToPrint.length > 0) {
                            personDetail = pdf.autoTableHtmlToJson(document.getElementById("selectedItemsReport"));
                        } else {
                            personDetail = pdf.autoTableHtmlToJson(document.getElementById("itemDetailsReport"));
                        }

                        pdf.setTextColor(7, 7, 7);
                        pdf.setFontStyle('normal');
                        pdf.setFontSize(13);
                        pdf.autoTable(personDetail.columns, personDetail.data, {
                            addPageContent: pageContent,
                            startY: 165,
                            margin: {horizontal: 25, bottom: 25, top: 145},
                            headerStyles: {
                                lineWidth: 0.2,
                                lineColor: [7, 7, 7],
                                fillColor: [255, 255, 255],
                                textColor: [7, 7, 7]
                            },
                            styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                            //columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                            columnStyles: {
                                0: {
                                    columnWidth: 150, overflow: 'linebreak', overflowColumns: false,
                                    fillColor: [255, 255, 255],
                                    textColor: [7, 7, 7]
                                },
                                1: {
                                    columnWidth: 70, overflow: 'linebreak', overflowColumns: false,
                                    fillColor: [255, 255, 255],
                                    textColor: [7, 7, 7]
                                },
                                2: {
                                    columnWidth: 60, overflow: 'linebreak', overflowColumns: false,
                                    valign: 'middle', halign: 'center',
                                    fillColor: [255, 255, 255],
                                    textColor: [7, 7, 7]
                                },
                                3: {
                                    columnWidth: 60, overflow: 'linebreak', overflowColumns: false,
                                    valign: 'middle', halign: 'center',
                                    fillColor: [255, 255, 255],
                                    textColor: [7, 7, 7]
                                },
                                4: {
                                    columnWidth: 60, overflow: 'linebreak', overflowColumns: false,
                                    valign: 'middle', halign: 'center',
                                    fillColor: [255, 255, 255],
                                    textColor: [7, 7, 7]
                                }/*,
                                 4: {
                                 columnWidth: 95, overflow: 'linebreak', overflowColumns: false,
                                 valign: 'middle', halign: 'center',
                                 fillColor: [255, 255, 255],
                                 textColor: [7, 7, 7]
                                 },
                                 5: {
                                 columnWidth: 95, overflow: 'linebreak', overflowColumns: false,
                                 valign: 'middle', halign: 'center',
                                 fillColor: [255, 255, 255],
                                 textColor: [7, 7, 7]
                                 },
                                 6: {
                                 columnWidth: 95, overflow: 'linebreak', overflowColumns: false,
                                 valign: 'middle', halign: 'center',
                                 fillColor: [255, 255, 255],
                                 textColor: [7, 7, 7]
                                 },
                                 7: {
                                 columnWidth: 95, overflow: 'linebreak', overflowColumns: false,
                                 valign: 'middle', halign: 'center',
                                 fillColor: [255, 255, 255],
                                 textColor: [7, 7, 7]
                                 },
                                 8: {
                                 columnWidth: 95, overflow: 'linebreak', overflowColumns: false,
                                 valign: 'middle', halign: 'center',
                                 fillColor: [255, 255, 255],
                                 textColor: [7, 7, 7]
                                 }*/
                            }
                        });

                        //pdf.save(vm.issue.number + ".pdf");

                        window.open(pdf.output('bloburl'), '_blank');

                        $rootScope.hideBusyIndicator();
                        $rootScope.showSuccessMessage("Report generated successfully");
                    }, 2000)
                } else {
                    $rootScope.showWarningMessage("Please select at least one field to print");
                    $rootScope.hideBusyIndicator();
                }

            }

            (function () {
                loadBoms();
            })();
        }
    }
);
