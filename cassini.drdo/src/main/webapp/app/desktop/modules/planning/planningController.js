/**
 * Created by Nageshreddy on 03-12-2018.
 */
define(
    [
        'app/desktop/modules/planning/planning.module',
        'jspdf',
        'jspdf-autotable',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/bomService',
        'app/shared/services/core/allocationService',
        'app/shared/services/core/inventoryService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/drdoApplication',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module, jsPDF) {
        module.controller('PlanningController', PlanningController);

        function PlanningController($scope, $rootScope, $timeout, $window, $state, $filter, $stateParams, $uibModal, compare, $translate,
                                    AllocationService, InventoryService, BomService, CommonService, DialogService) {

            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa flaticon-contract11 nav-icon-font";
            $rootScope.viewInfo.title = "Planning";

            var vm = this;

            vm.selectedBom = null;
            vm.loading = true;
            vm.showPage = false;
            vm.bomItems = [];
            vm.selectedMissiles = [];
            vm.selectedMissileIds = [];

            vm.selectItemsView = false;
            vm.object = false;
            vm.currentBomNode = null;
            vm.workCenter = null;
            vm.searchValue = null;
            vm.selectedSection = null;
            vm.itemId = $stateParams.itemId;
            vm.searchValue = null;
            var classificationMainTree = null;
            $scope.toggledSection = toggledSection;
            $scope.toggledMissile = toggledMissile;
            var missileStorageKey = null;
            vm.selectedBomItems = [];
            vm.selectedBomItemsMap = new Hashtable();
            vm.showBatchNumbers = showBatchNumbers;
            vm.toggleNode = toggleNode;
            vm.cancel = cancel;
            vm.dropDown = false;
            var nodeId = 0;
            var rootNode = null;
            vm.productSelected = false;
            vm.exportReports = exportReports;
            vm.workCenters = [];
            vm.selectItems = selectItems;

            var sectionStorageKey = null;
            vm.sections = [];
            var rows = [];
            vm.bomIds = [];

            var nodes = [];
            $rootScope.bomTreeDetails = false;
            vm.autoPlan = autoPlan;
            vm.resetPlan = resetPlan;
            vm.planningItemResetPage = planningItemResetPage;
            vm.manualAllocate = manualAllocate;
            vm.manualReAllocation = manualReAllocation;
            vm.preventClick = preventClick;
            vm.showPlanningInfoPanel = showPlanningInfoPanel;
            vm.reAllocate = reAllocate;
            var missilesMap = new Hashtable();
            var missilesMap2 = new Hashtable();
            vm.selectItemForReport = selectItemForReport;

            $rootScope.checkReqSystemWithMissile = false;
            $rootScope.checkWithSystemWithMissile = false;
            $rootScope.checkStorage = false;
            $rootScope.selectedType = false;
            vm.showInventoryInfo = false;
            vm.printShortage = printShortage;
            //vm.selectedPrintBomItems = [];
            vm.stockInstancesPopOver = {
                templateUrl: 'app/desktop/modules/planning/stockInstancesPopover.jsp'
            };

            vm.allocatePopOver = {
                templateUrl: 'app/desktop/modules/planning/allocatePopOver.jsp'
            };

            function printShortage() {
                $rootScope.showBusyIndicator($('.view-container'));
                var missileNames = "";
                angular.forEach(vm.selectedMissiles, function (missile, $index) {
                    if ($index == vm.selectedMissiles.length - 1) {
                        missileNames += missile.instanceName;
                    } else {
                        missileNames += missile.instanceName + ", ";
                    }
                });
                $("#printSelectedType").show();
                var pdf;
                if (vm.selectedMissiles.length > 7) {
                    pdf = new jsPDF('l', 'pt', 'a3');
                } else {
                    pdf = new jsPDF('l', 'pt', 'a4');
                }

                var pageWidth = pdf.internal.pageSize.width;

                pdf.setFont('arial narrow');
                pdf.setFontSize(13);
                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                var imgData = $rootScope.drdoImage;
                pdf.addImage(imgData, 'JPG', pageWidth / 2.1, 25, 125, 125);

                var today = moment(new Date());
                var todayStr = today.format('DD/MM/YYYY');

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('bold');
                pdf.setFontSize(14);
                pdf.text(pageWidth / 2.05, 165, "Shortage Report");
                pdf.setFontSize(13);
                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.text(50, 170, "BOM :" + vm.selectedBom.item.itemMaster.itemName);
                pdf.text(50, 185, "Selected Section : " + vm.selectedBomItems[0].item.typeRef.name);
                pdf.text(pageWidth - 200, 185, "Date : " + todayStr);
                if (vm.selectedReportItems.length == 0) {
                    report1(pdf);
                } else {
                    report2(pdf);
                }
            }

            function report2(pdf) {
                var planningPrintDetails = pdf.autoTableHtmlToJson(document.getElementById("printSelectedItem"));

                pdf.autoTable(planningPrintDetails.columns, planningPrintDetails.data, {
                    startY: 195,
                    margin: {horizontal: 50},
                    styles: {
                        cellWidth: [200, 100],
                        overflow: 'linebreak',
                        overflowColumns: false,
                        verticalAlign: 'center'
                    },
                    headerStyles: {
                        lineWidth: 0.2,
                        lineColor: [7, 7, 7],
                        fillColor: [255, 255, 255],
                        textColor: [7, 7, 7]
                    },
                    columnStyles: {
                        text: {
                            cellWidth: [200, 100],
                            overflow: 'linebreak',
                            overflowColumns: false,
                            verticalAlign: 'center',
                            columnWidth: 'auto',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        }
                    },
                    drawCell: function (cell, data) {
                        /*var rowData = vm.selectedBomItems[data.row.index + 1];
                         if (rowData != undefined && rowData.expanded && rowData.item.bomItemType != 'PART') {
                         pdf.setFontStyle('bold');
                         pdf.setFontSize(11);
                         } else {

                         }*/

                        pdf.setFontStyle('normal');
                        pdf.setFontSize(12);
                        /*if (cell.raw != undefined) {
                         if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level1") {
                         cell.text[0] = "        " + cell.text[0];
                         } else if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level2") {
                         cell.text[0] = "            " + cell.text[0];
                         } else if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level3") {
                         cell.text[0] = "                " + cell.text[0];
                         } else if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level4") {
                         cell.text[0] = "                    " + cell.text[0];
                         }
                         }*/
                        cell.text[0] = cell.text[0];
                    }
                });

                window.open(pdf.output('bloburl'), '_blank');

                $rootScope.hideBusyIndicator();
                $("#printSelectedType").hide();
                $rootScope.showSuccessMessage("Report generated successfully");
            }

            function report1(pdf) {
                var planningPrintDetails = pdf.autoTableHtmlToJson(document.getElementById("printSelectedType"));

                pdf.autoTable(planningPrintDetails.columns, planningPrintDetails.data, {
                    startY: 195,
                    margin: {horizontal: 50},
                    styles: {
                        cellWidth: [200, 100],
                        overflow: 'linebreak',
                        overflowColumns: false,
                        verticalAlign: 'center'
                    },
                    headerStyles: {
                        lineWidth: 0.2,
                        lineColor: [7, 7, 7],
                        fillColor: [255, 255, 255],
                        textColor: [7, 7, 7]
                    },
                    columnStyles: {
                        text: {
                            cellWidth: [200, 100],
                            overflow: 'linebreak',
                            overflowColumns: false,
                            verticalAlign: 'center',
                            columnWidth: 'auto',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        }
                    },
                    drawCell: function (cell, data) {
                        /*var rowData = vm.selectedBomItems[data.row.index + 1];
                         if (rowData != undefined && rowData.expanded && rowData.item.bomItemType != 'PART') {
                         pdf.setFontStyle('bold');
                         pdf.setFontSize(11);
                         } else {

                         }*/

                        pdf.setFontStyle('normal');
                        pdf.setFontSize(12);
                        if (cell.raw != undefined) {
                            if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level1") {
                                cell.text[0] = "        " + cell.text[0];
                            } else if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level2") {
                                cell.text[0] = "            " + cell.text[0];
                            } else if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level3") {
                                cell.text[0] = "                " + cell.text[0];
                            } else if (cell.raw.firstElementChild != null && cell.raw.firstElementChild.className == "level4") {
                                cell.text[0] = "                    " + cell.text[0];
                            }
                        }
                    }
                });

                /*if (type == "SECTIONS") {
                 pdf.save(vm.selectedBom.item.itemMaster.itemName + ".pdf");
                 var modal = document.getElementById("sectionsMenu");
                 modal.style.display = "none";

                 angular.forEach(vm.bomSections, function (section) {
                 section.selected = false;
                 });
                 vm.selectAllCheck = false;
                 vm.selectedSectionsToPrint = [];
                 } else {
                 pdf.save(vm.selectedBom.item.itemMaster.itemName + "_" + vm.selectedBom.item.itemMaster.itemNumber + ".pdf");
                 }*/
                window.open(pdf.output('bloburl'), '_blank');

                $rootScope.hideBusyIndicator();
                $("#printSelectedType").hide();
                $rootScope.showSuccessMessage("Report generated successfully");
            }

            function preventClick(event) {
                event.stopPropagation();
                event.preventDefault();
            }

            function showPlanningInfoPanel() {
                vm.showInventoryInfo = !vm.showInventoryInfo;
                if (vm.showInventoryInfo) {
                    $('#planningInfoPanel').show('slide', {direction: 'left'}, 600);
                }
                else {
                    $('#planningInfoPanel').hide('slide', {direction: 'left'}, 600);
                    vm.showInventoryInfo = false;
                }
            }

            function initClassificationTree() {
                classificationMainTree = $('#classificationMainTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Planning',
                            iconCls: 'bom-root',
                            attributes: {
                                item: null
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelectType
                });

                rootNode = classificationMainTree.tree('find', 0);

                $(document).click(function () {
                    $("#contextMenu").hide();
                });
            }

            function loadClassificationTree() {
                BomService.getBomTree().then(
                    function (data) {
                        nodes = [];
                        angular.forEach(data, function (item) {
                            var node = makeNode(item);
                            nodes.push(node);
                        });

                        classificationMainTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );

                if (vm.currentBomNode == null) {
                    showPlanningInfoPanel();
                }
            }

            function loadDrdoImage() {
                BomService.getDrdoImage().then(
                    function (data) {
                        $rootScope.drdoImage = data;
                    }
                )
            }

            function makeNode(item) {
                return {
                    id: ++nodeId,
                    text: item.item.itemMaster.itemName,
                    iconCls: 'bom-node',
                    attributes: {
                        item: item
                    }
                };
            }

            vm.searchTree = searchTree;
            function searchTree() {
                $('#classificationMainTree').tree('doFilter', vm.searchValue);
            }

            function toggleNode(bomItem, tItem) {
                if (vm.bomSearch == false) {
                    if (bomItem.expanded == null || bomItem.expanded == undefined) {
                        bomItem.expanded = false;
                    }
                    bomItem.expanded = !bomItem.expanded;
                    var index = vm.selectedBomItems.indexOf(bomItem);
                    if (bomItem.item.objectType == 'BOMITEM') {
                        if (bomItem.expanded == false) {
                            removeChildren(bomItem);
                        }
                        else {
                            $rootScope.showBusyIndicator($('.view-content'));
                            AllocationService.getBomChildrenAllocation(vm.selectedBom.id, bomItem.item.id, vm.missileIds, vm.selectedMissileIds, vm.workCenter, vm.searchBomText).then(
                                function (data) {

                                    if ((vm.workCenter == null || vm.workCenter == "" || vm.workCenter == undefined) && (vm.searchBomText == null || vm.searchBomText == "" || vm.searchBomText == undefined)) {
                                        angular.forEach(data, function (item) {
                                            if (bomItem.bomChildren == undefined) {
                                                bomItem.bomChildren = [];
                                            }
                                            item.expanded = false;
                                            item.level = bomItem.level + 1;
                                            item.bomChildren = [];
                                            bomItem.bomChildren.push(item);
                                        });

                                        angular.forEach(bomItem.bomChildren, function (item) {
                                            index = index + 1;
                                            vm.selectedBomItems.splice(index, 0, item);
                                            /* if ((item.expanded && item.bomChildren.length > 0) || item.item.bomItemType == 'PART') {
                                             vm.selectedPrintBomItems.push(item);
                                             }*/
                                            vm.selectedBomItemsMap.put(item.item.id, item);
                                        });
                                    } else {
                                        vm.selectedBomItems = data;
                                    }

                                    /*------------------  To Expand All --------------------*/

                                    /*if (bomItem.item.bomItemType == "SECTION") {
                                     angular.forEach(data, function (item) {
                                     if (bomItem.bomChildren == undefined) {
                                     bomItem.bomChildren = [];
                                     }
                                     item.expanded = true;
                                     item.level = bomItem.level + 1;
                                     item.bomChildren = [];
                                     bomItem.bomChildren.push(item);
                                     });
                                     var lastIndex = 0;
                                     angular.forEach(bomItem.bomChildren, function (item) {
                                     item.bomChildren = [];
                                     index = index + 1;
                                     vm.selectedBomItems.splice(index, 0, item);
                                     vm.selectedBomItemsMap.put(item.item.id, item);
                                     index = populateChildren(item, index)
                                     });
                                     } else {
                                     angular.forEach(data, function (item) {
                                     if (bomItem.bomChildren == undefined) {
                                     bomItem.bomChildren = [];
                                     }
                                     item.expanded = false;
                                     item.level = bomItem.level + 1;
                                     item.bomChildren = [];
                                     bomItem.bomChildren.push(item);
                                     });

                                     angular.forEach(bomItem.bomChildren, function (item) {
                                     index = index + 1;
                                     vm.selectedBomItems.splice(index, 0, item);
                                     vm.selectedBomItemsMap.put(item.item.id, item);
                                     });
                                     }*/
                                    $rootScope.hideBusyIndicator();
                                    //autoPlan();
                                    //calculateShortage();
                                    if (vm.workCenter == null && vm.searchBomText == null && tItem == 'section') {
                                        AllocationService.loadBomChildrenAllocation(vm.selectedBom.id, bomItem.item.id, vm.missileIds, vm.selectedMissileIds).then(
                                            function (data) {

                                            });
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    } else if (bomItem.objectType == 'BOMINSTANCEITEM') {
                        if (bomItem.expanded == false) {
                            removeChildren(bomItem);
                        }
                        else {
                            AllocationService.getBomInstanceChildrenAllocation(vm.selectedBom.id, bomItem.id).then(
                                function (data) {
                                    angular.forEach(data, function (item) {
                                        item.expanded = false;
                                        item.level = bomItem.level + 1;
                                        item.bomChildren = [];
                                        bomItem.bomChildren.push(item);
                                    });

                                    angular.forEach(bomItem.bomChildren, function (item) {
                                        index = index + 1;
                                        vm.selectedBomItems.splice(index, 0, item);
                                    });
                                    //autoPlan();
                                    calculateShortage();
                                }
                            )
                        }
                    }
                }
            }

            function populateChildren(bomItem, lastIndex) {
                angular.forEach(bomItem.children, function (item) {
                    lastIndex++;
                    item.bomChildren = [];
                    bomItem.bomChildren.push(item);
                    //var index = vm.selectedBomItems.indexOf(bomItem);
                    vm.selectedBomItems.splice(lastIndex + 1, 0, item);
                    vm.selectedBomItemsMap.put(item.item.id, item);
                    lastIndex = populateChildren(item, lastIndex)
                });

                return lastIndex;
            }

            function removeChildren(bomItem) {
                if (bomItem != null && bomItem.bomChildren != null && bomItem.bomChildren != undefined) {
                    angular.forEach(bomItem.bomChildren, function (item) {
                        removeChildren(item);
                    });

                    var index = vm.selectedBomItems.indexOf(bomItem);
                    vm.selectedBomItems.splice(index + 1, bomItem.bomChildren.length);
                    bomItem.bomChildren = [];
                    bomItem.expanded = false;
                }
            }

            function manualAllocate(selectedBomItem, missile) {
                var itemAllocations2 = [];
                if (selectedBomItem.item.bomItemType == 'PART') {
                    var tempStock = 0;
                    var fTempStock = 0.0;
                    if (selectedBomItem.listMap != {}) {
                        tempStock = calculateStock(selectedBomItem);
                        fTempStock = calculateFractionalStock(selectedBomItem);
                        if (tempStock > 0 || fTempStock > 0.0) {
                            var allocationMap = selectedBomItem.listMap;
                            var itemAllocation = allocationMap[missile.id];
                            if (itemAllocation != undefined) {

                                if (selectedBomItem.item.item.itemMaster.itemType.hasLots) {
                                    var reqFractionalQty = angular.copy(selectedBomItem.item.fractionalQuantity + itemAllocation.failedQty);
                                    if (itemAllocation.allocateQty < reqFractionalQty) {
                                        if (reqFractionalQty < fTempStock) {
                                            if (itemAllocation.allocateQty == 0) {
                                                itemAllocation.allocateQty = reqFractionalQty;
                                                itemAllocation.currentAllocateQty = reqFractionalQty;
                                                selectedBomItem.allocated = selectedBomItem.allocated + reqFractionalQty;
                                            } else {
                                                itemAllocation.currentAllocateQty = reqFractionalQty - itemAllocation.allocateQty;
                                                itemAllocation.allocateQty = reqFractionalQty;
                                                selectedBomItem.allocated = selectedBomItem.allocated + reqFractionalQty;
                                            }
                                        } else if (fTempStock > 0) {
                                            if (itemAllocation.allocateQty == 0) {
                                                itemAllocation.allocateQty = fTempStock;
                                                itemAllocation.currentAllocateQty = fTempStock;
                                                selectedBomItem.allocated = selectedBomItem.allocated + fTempStock;
                                            } else if ((reqFractionalQty - itemAllocation.allocateQty) < fTempStock) {
                                                itemAllocation.currentAllocateQty = reqFractionalQty - itemAllocation.allocateQty;
                                                itemAllocation.allocateQty = reqFractionalQty;
                                                selectedBomItem.allocated = selectedBomItem.allocated + reqFractionalQty;

                                            } else {
                                                itemAllocation.allocateQty = itemAllocation.allocateQty + fTempStock;
                                                itemAllocation.currentAllocateQty = fTempStock;
                                                selectedBomItem.allocated = selectedBomItem.allocated + fTempStock;
                                            }
                                        }
                                    }
                                } else {
                                    var reqQty = angular.copy(selectedBomItem.item.quantity + itemAllocation.failedQty);
                                    if (itemAllocation.allocateQty < reqQty) {
                                        if (reqQty < tempStock) {
                                            if (itemAllocation.allocateQty == 0) {
                                                itemAllocation.allocateQty = reqQty;
                                                itemAllocation.currentAllocateQty = reqQty;
                                                selectedBomItem.allocated = selectedBomItem.allocated + reqQty;

                                            } else {
                                                itemAllocation.currentAllocateQty = reqQty - itemAllocation.allocateQty;
                                                itemAllocation.allocateQty = reqQty;
                                                selectedBomItem.allocated = selectedBomItem.allocated + reqQty;
                                            }
                                        } else if (tempStock > 0) {
                                            if (itemAllocation.allocateQty == 0) {
                                                itemAllocation.allocateQty = tempStock;
                                                itemAllocation.currentAllocateQty = tempStock;
                                                selectedBomItem.allocated = selectedBomItem.allocated + tempStock;
                                            } else if ((reqQty - itemAllocation.allocateQty) < tempStock) {
                                                itemAllocation.currentAllocateQty = reqQty - itemAllocation.allocateQty;
                                                itemAllocation.allocateQty = reqQty;
                                            } else {
                                                itemAllocation.allocateQty = itemAllocation.allocateQty + tempStock;
                                                itemAllocation.currentAllocateQty = tempStock;
                                                selectedBomItem.allocated = selectedBomItem.allocated + tempStock;
                                            }
                                        }
                                    }
                                }

                                if (itemAllocation.allocateQty > 0) {
                                    var itemAllocation1 = {
                                        id: itemAllocation.id,
                                        bom: vm.selectedBom.id,
                                        bomInstance: itemAllocation.bomInstance,
                                        bomInstanceItem: itemAllocation.bomInstanceItem,
                                        allocateQty: itemAllocation.allocateQty,
                                        failedQty: itemAllocation.failedQty,
                                        issuedQty: itemAllocation.issuedQty,
                                        currentAllocateQty: itemAllocation.currentAllocateQty
                                    };
                                    itemAllocations2.push(itemAllocation1);
                                }
                            }
                            AllocationService.planInventory(itemAllocations2).then(
                                function (data) {
                                    //getAllocatedQuantities(selectedBomItem.item.parent);
                                    calculateShortage();
                                    $rootScope.showSuccessMessage("Allocated available quantity successfully");
                                }
                            );
                        } else {
                            $rootScope.showErrorMessage("Quantity is not available");
                        }
                    }
                }
            }

            function getAllocatedQuantities(id) {
                var bomItem = vm.selectedBomItemsMap.get(id);
                removeChildren(bomItem);
                toggleNode(bomItem, 'item');
                calculateShortage();
            }

            function resetPlan() {
                $rootScope.showBusyIndicator($('.view-content'));
                if (vm.selectedMissiles.length > 0) {

                    angular.forEach(vm.selectedBomItems, function (selectedBomItem) {
                        if (selectedBomItem.item.bomItemType == 'PART') {
                            if (selectedBomItem.listMap != {}) {
                                var allocationMap = selectedBomItem.listMap;
                                angular.forEach(vm.selectedMissiles, function (missile) {
                                    var itemAllocation = allocationMap[missile.id];
                                    if (itemAllocation != undefined) {
                                        var temp = 0;
                                        if (selectedBomItem.item.item.itemMaster.itemType.hasLots) {
                                            if ((itemAllocation.allocateQty - itemAllocation.issuedQty) > 0) {
                                                temp = itemAllocation.allocateQty - (itemAllocation.issuedQty - itemAllocation.issueProcessQty);
                                                itemAllocation.allocateQty = itemAllocation.allocateQty - (itemAllocation.allocateQty - itemAllocation.issuedQty);
                                            }
                                        } else {
                                            if ((itemAllocation.allocateQty - itemAllocation.issuedQty) > 0) {
                                                temp = itemAllocation.allocateQty - (itemAllocation.issuedQty - itemAllocation.issueProcessQty);
                                                itemAllocation.allocateQty = itemAllocation.allocateQty - (itemAllocation.allocateQty - itemAllocation.issuedQty);

                                            }
                                        }
                                        if (selectedBomItem.allocated > 0) {
                                            if (selectedBomItem.allocated >= temp) {
                                                var temp1 = selectedBomItem.allocated;
                                                selectedBomItem.allocated = selectedBomItem.allocated - temp;
                                                temp = temp - temp1;
                                            } else if (selectedBomItem.allocated < temp) {
                                                temp = temp - selectedBomItem.allocated;
                                                selectedBomItem.allocated = 0;
                                            }
                                        }
                                        if (selectedBomItem.commonAllocated > 0 && temp > 0) {
                                            if (selectedBomItem.commonAllocated >= temp) {
                                                selectedBomItem.commonAllocated = selectedBomItem.commonAllocated - temp;
                                            } else if (selectedBomItem.commonAllocated < temp) {
                                                temp = temp - selectedBomItem.commonAllocated;
                                                selectedBomItem.commonAllocated = 0;
                                            }
                                        }
                                    }
                                })
                            }
                        }
                    });

                    AllocationService.reSetPlanForSelectedMissiles(vm.missileIds).then(
                        function (data) {
                            /*selectedBomItemPartIds = Array.from(new Set(selectedBomItemPartIds));
                             angular.forEach(selectedBomItemPartIds, function (id) {
                             getAllocatedQuantities(id);
                             });*/
                            //$scope.onDropDownSectionSelection(vm.selectedSection);
                            //calculateShortage();
                            $rootScope.showSuccessMessage("Reset done Successfully");
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            function reAllocate() {
                if (validate()) {
                    var itemAllocations2 = [];
                    selectedItem.listMap[selectMissile.id].allocateQty = selectedItem.listMap[selectMissile.id].allocateQty - vm.quantity;
                    selectedItem.listMap[selectMissile.id].resetQty = vm.quantity;
                    itemAllocations2.push(selectedItem.listMap[selectMissile.id]);
                    AllocationService.reSetPlanInventory(itemAllocations2).then(
                        function (data) {
                            //getAllocatedQuantities(selectedItem.item.parent);
                            if (selectedItem.allocated > 0) {
                                if (selectedItem.allocated >= vm.quantity) {
                                    selectedItem.allocated = selectedItem.allocated - vm.quantity;
                                } else if (selectedItem.allocated < vm.quantity) {
                                    vm.quantity = vm.quantity - selectedItem.allocated;
                                    selectedItem.allocated = 0;
                                    if (selectedItem.commonAllocated > 0 && vm.quantity > 0) {
                                        if (selectedItem.commonAllocated >= vm.quantity) {
                                            selectedItem.commonAllocated = selectedItem.commonAllocated - vm.quantity;
                                        } else if (selectedItem.commonAllocated < vm.quantity) {
                                            vm.quantity = vm.quantity - selectedItem.commonAllocated;
                                            selectedItem.commonAllocated = 0;
                                        }
                                    }
                                }
                            } else if (selectedItem.commonAllocated > 0 && vm.quantity > 0) {
                                if (selectedItem.commonAllocated >= vm.quantity) {
                                    selectedItem.commonAllocated = selectedItem.commonAllocated - vm.quantity;
                                } else if (selectedItem.commonAllocated < vm.quantity) {
                                    vm.quantity = vm.quantity - selectedItem.commonAllocated;
                                    selectedItem.commonAllocated = 0;
                                }
                            }

                            //$scope.onDropDownSectionSelection(vm.selectedSection);
                            //calculateShortage();
                            $rootScope.showSuccessMessage("Quantity re-allocated successfully");
                        }
                    );

                    var modal = document.getElementById("re-allocation");
                    modal.style.display = "none";
                }
            }

            function calculateStock(selectedBomItem) {
                var temp = angular.copy(selectedBomItem.stock);
                var allocationMap = selectedBomItem.listMap;
                temp = temp - ((selectedBomItem.allocated - selectedBomItem.issuedQty) + (selectedBomItem.commonAllocated - selectedBomItem.commonIssuedQty));
                /*angular.forEach(vm.missiles, function (missile) {
                    var itemAllocation = allocationMap[missile.id];
                    if (itemAllocation != undefined) {
                        temp = temp + itemAllocation.issuedQty;
                        //temp = temp - (itemAllocation.allocateQty - itemAllocation.issuedQty);
                    }
                });*/
                return temp;
            }

            function calculateFractionalStock(selectedBomItem) {
                var temp = angular.copy(selectedBomItem.fractionalStock);
                var allocationMap = selectedBomItem.listMap;
                temp = temp - ((selectedBomItem.allocated - selectedBomItem.issuedQty) + (selectedBomItem.commonAllocated - selectedBomItem.commonIssuedQty));
                /*angular.forEach(vm.missiles, function (missile) {
                    var itemAllocation = allocationMap[missile.id];
                    if (itemAllocation != undefined) {
                        temp = temp + itemAllocation.issuedQty;
                        //temp = temp - (itemAllocation.allocateQty - itemAllocation.issuedQty);
                    }
                });*/
                return temp;
            }

            function autoPlan() {
                $rootScope.showBusyIndicator($('.view-content'));
                if (vm.selectedMissiles.length > 0) {

                    angular.forEach(vm.selectedMissiles, function (missile) {
                        angular.forEach(vm.selectedBomItems, function (selectedBomItem) {
                            if (selectedBomItem.item.bomItemType == 'PART') {
                                if (selectedBomItem.listMap != {}) {
                                    var tempStock = calculateStock(selectedBomItem);
                                    var fTempStock = calculateFractionalStock(selectedBomItem);
                                    var allocationMap = selectedBomItem.listMap;
                                    var itemAllocation = allocationMap[missile.id];
                                    if (itemAllocation != undefined) {
                                        if (selectedBomItem.item.item.itemMaster.itemType.hasLots) {
                                            var reqFractionalQty = angular.copy(selectedBomItem.item.fractionalQuantity + itemAllocation.failedQty);
                                            if (itemAllocation.allocateQty < reqFractionalQty) {
                                                if (reqFractionalQty < fTempStock) {
                                                    if (itemAllocation.allocateQty == 0) {
                                                        itemAllocation.allocateQty = reqFractionalQty;
                                                        itemAllocation.currentAllocateQty = reqFractionalQty;
                                                        selectedBomItem.allocated = selectedBomItem.allocated + reqFractionalQty;
                                                        fTempStock = fTempStock - itemAllocation.allocateQty;
                                                    } else {
                                                        fTempStock = fTempStock - itemAllocation.allocateQty;
                                                        itemAllocation.currentAllocateQty = reqFractionalQty - itemAllocation.allocateQty;
                                                        itemAllocation.allocateQty = reqFractionalQty;
                                                        selectedBomItem.allocated = selectedBomItem.allocated + reqFractionalQty;
                                                    }
                                                } else if (fTempStock > 0) {
                                                    if (itemAllocation.allocateQty == 0) {
                                                        itemAllocation.allocateQty = fTempStock;
                                                        itemAllocation.currentAllocateQty = fTempStock;
                                                        selectedBomItem.allocated = selectedBomItem.allocated + fTempStock;
                                                        fTempStock = fTempStock - itemAllocation.allocateQty;
                                                    } else if ((reqFractionalQty - itemAllocation.allocateQty) < fTempStock) {
                                                        itemAllocation.currentAllocateQty = reqFractionalQty - itemAllocation.allocateQty;
                                                        itemAllocation.allocateQty = reqFractionalQty;
                                                        selectedBomItem.allocated = selectedBomItem.allocated + reqFractionalQty;
                                                        fTempStock = fTempStock - (reqFractionalQty - itemAllocation.allocateQty);
                                                    } else {
                                                        itemAllocation.allocateQty = itemAllocation.allocateQty + fTempStock;
                                                        itemAllocation.currentAllocateQty = fTempStock;
                                                        selectedBomItem.allocated = selectedBomItem.allocated + fTempStock;
                                                        fTempStock = 0;
                                                    }
                                                }
                                            }
                                        } else {
                                            var reqQty = angular.copy(selectedBomItem.item.quantity + itemAllocation.failedQty);
                                            if (itemAllocation.allocateQty < reqQty) {
                                                if (reqQty < tempStock) {
                                                    if (itemAllocation.allocateQty == 0) {
                                                        itemAllocation.allocateQty = reqQty;
                                                        itemAllocation.currentAllocateQty = reqQty;
                                                        selectedBomItem.allocated = selectedBomItem.allocated + reqQty;
                                                        tempStock = tempStock - itemAllocation.allocateQty;
                                                    } else {
                                                        tempStock = tempStock - itemAllocation.allocateQty;
                                                        itemAllocation.currentAllocateQty = reqQty - itemAllocation.allocateQty;
                                                        itemAllocation.allocateQty = reqQty;
                                                        selectedBomItem.allocated = selectedBomItem.allocated + reqQty;
                                                    }
                                                } else if (tempStock > 0) {
                                                    if (itemAllocation.allocateQty == 0) {
                                                        itemAllocation.allocateQty = tempStock;
                                                        itemAllocation.currentAllocateQty = tempStock;
                                                        selectedBomItem.allocated = selectedBomItem.allocated + tempStock;
                                                        tempStock = tempStock - itemAllocation.allocateQty;
                                                    } else if ((reqQty - itemAllocation.allocateQty) < tempStock) {
                                                        itemAllocation.currentAllocateQty = reqQty - itemAllocation.allocateQty;
                                                        itemAllocation.allocateQty = reqQty;
                                                        tempStock = tempStock - (reqQty - itemAllocation.allocateQty);
                                                    } else {
                                                        itemAllocation.allocateQty = itemAllocation.allocateQty + tempStock;
                                                        itemAllocation.currentAllocateQty = fTempStock;
                                                        selectedBomItem.allocated = selectedBomItem.allocated + tempStock;
                                                        tempStock = 0;
                                                    }
                                                }
                                            }
                                        }

                                        /*if (itemAllocation.allocateQty > 0) {
                                         var itemAllocation1 = {
                                         id: itemAllocation.id,
                                         bom: vm.selectedBom.id,
                                         bomInstance: itemAllocation.bomInstance,
                                         bomInstanceItem: itemAllocation.bomInstanceItem,
                                         allocateQty: itemAllocation.allocateQty,
                                         failedQty: itemAllocation.failedQty,
                                         issuedQty: itemAllocation.issuedQty,
                                         currentAllocateQty: itemAllocation.currentAllocateQty
                                         };
                                         itemAllocations1.push(itemAllocation1);
                                         selectedBomItemPartIds.push(selectedBomItem.item.parent);
                                         }*/
                                    }
                                }
                            }
                        });
                    });

                    AllocationService.planSelectedMissiles(vm.selectedMissileIds).then(
                        function (data) {
                            calculateShortage();
                            $rootScope.showSuccessMessage("Items Allocated Successfully");
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else {
                    $rootScope.showWarningMessage("Please load missiles to Plan");
                    $rootScope.hideBusyIndicator();
                }

            }

            vm.partNumberPopOver = {
                templateUrl: 'app/desktop/modules/inventory/popovers/partNumberPopoverTemplate.jsp'
            };

            vm.returnPopOver = {
                templateUrl: 'app/desktop/modules/inventory/popovers/returnPopoverTemplate.jsp'
            };

            function planningItemResetPage() {
                angular.forEach(vm.bomItems, function (item) {
                    vm.showFreeTextSearchResult = false;
                    item.selected = false;
                });
                vm.showFreeTextSearchResult = false;
            }

            function showBatchNumbers(item, missile) {
                if (item.item.itemType.storeAsLot != true) {
                    var options = {
                        title: 'Part Re-Allocation',
                        showMask: true,
                        template: 'app/desktop/modules/procurement/all/reAllocationDialog.jsp',
                        controller: 'ReAllocationDialogController as reAllocationDialogVm',
                        resolve: 'app/desktop/modules/procurement/all/reAllocationDialogController',
                        width: 700,
                        data: {
                            itemOb: item,
                            missileItem: missile,
                            bomNode1: vm.currentBomNode.attributes.item
                        },
                        buttons: [
                            {text: 'ReAllocate', broadcast: 'items.reAllocation'}
                        ],
                        callback: function (data) {
                            batchNumbers = " ";
                            vm.allocatedBatchNumbers = [];
                            angular.forEach(data, function (reAllocateItem) {
                                if (vm.allocatedBatchNumbers.length == 0) {
                                    batchNumbers = batchNumbers + " " + reAllocateItem.batchNumber;
                                } else if (vm.allocatedBatchNumbers.length > 0) {
                                    batchNumbers = batchNumbers + ", " + reAllocateItem.batchNumber;
                                }
                                vm.allocatedBatchNumbers.push(reAllocateItem);
                            });
                            ItemService.createItemsReAllocation(data).then(
                                function (data) {
                                    if (vm.bomParts.length > 0) {
                                        loadQuantitiesOfItemsByBomItemIdsAndMissileIds(vm.bomParts);
                                    }
                                    if (vm.bomLots.length > 0) {
                                        loadQuantitiesOfLotItemsByBomLotIdsAndMissileIds(vm.bomLots);
                                    }
                                    var ids = [];
                                    ids.push(item.id);
                                    InventoryService.getInventoryByMultipleRefItemsAndMultipleItems(vm.currentBomNode.attributes.item.id, ids).then(
                                        function (data) {
                                            angular.forEach(data, function (bom) {
                                                var item = vm.bomItemsAndIdsMap.get(bom.id);
                                                if (item != null) {
                                                    item.systemInventory.stockOnHand = bom.planningInventory.stockOnHand;
                                                    item.systemInventory.totalAllocatedQty = bom.planningInventory.totalAllocatedQty;
                                                }
                                            });
                                        });


                                    $rootScope.showSuccessMessage(batchNumbers + " : Re-allocated successfully");
                                    //loadCommonParts();
                                });
                            $rootScope.hideSidePanel('right');
                        }
                    };
                    $rootScope.showSidePanel(options);

                } else {
                    $rootScope.showWarningMessage("We cannot Re-allocate Lots");
                }
            }

            vm.flag = false;
            vm.selectedWithSysteAndMissile = null;

            function onSelectType(node) {
                vm.currentBomNode = node;
                sectionStorageKey = node.attributes.item.id + 'sectionProc';
                missileStorageKey = node.attributes.item.id + 'missileProc';
                loadMissiles(node);
                vm.dropDown = true;
                vm.productSelected = true;
                $rootScope.closeNotification();
                vm.selectedBomItems = [];
                vm.selectedBomItemsMap = new Hashtable();
                if (node.attributes.item) {
                    vm.selectedBom = node.attributes.item;
                    if (vm.selectedBom != null && vm.selectedBom.objectType == "BOM") {
                        $scope.bom = true;
                        $scope.instance = false;
                        vm.showInventoryPage = true;
                        $rootScope.viewInfo.title = vm.selectedBom.item.itemMaster.itemName + " - Planning";
                        AllocationService.getBomAllocation(vm.selectedBom.id).then(
                            function (data) {
                                vm.bomItems = data;
                                if (vm.bomItems.length == 0) {
                                    vm.selectedBomItems = [];
                                    vm.selectedSection = null;
                                    vm.selectedMissiles = [];
                                }
                                angular.forEach(vm.bomItems, function (item) {
                                    item.bomChildren = [];
                                    item.expanded = false;
                                    item.level = 0;
                                });
                                vm.loading = false;
                                showPlanningInfoPanel();
                            }
                        )
                    }
                    /*else if (vm.selectedBom != null && vm.selectedBom.objectType == "BOMINSTANCE") {
                     vm.showInventoryPage = true;
                     $scope.bom = false;
                     $scope.instance = true;
                     $rootScope.viewInfo.title = "Planning";
                     AllocationService.getBomInstanceInventory(vm.selectedBom.id).then(
                     function (data) {
                     vm.bomItems = data;
                     angular.forEach(vm.bomItems, function (item) {
                     item.bomChildren = [];
                     item.expanded = false;
                     item.level = 0;
                     });
                     vm.loading = false;
                     }
                     )
                     }*/

                } else {
                    vm.selectedBom = null;
                }
            }

            function calculateShortage() {
                var fShortage = null;
                angular.forEach(vm.selectedBomItems, function (selectedBomItem) {
                    fShortage = 0.0;
                    selectedBomItem.shortage = 0.0;
                    if (selectedBomItem.listMap != {}) {
                        angular.forEach(vm.selectedMissiles, function (missile) {
                            var itemAllocation = selectedBomItem.listMap[missile.id];
                            if (itemAllocation != undefined) {
                                if (selectedBomItem.item.item.itemMaster.itemType.hasLots) {
                                    selectedBomItem.shortage = selectedBomItem.shortage + (selectedBomItem.item.fractionalQuantity + itemAllocation.failedQty) - (itemAllocation.allocateQty) /*+ itemAllocation.issuedQty*/;
                                } else {
                                    selectedBomItem.shortage = selectedBomItem.shortage + (selectedBomItem.item.quantity + itemAllocation.failedQty) - (itemAllocation.allocateQty) /*+ itemAllocation.issuedQty*/;
                                }
                            }
                        });
                    }
                })
            }

            $rootScope.planningTreeDetails = false;

            function loadMissiles(node) {
                node.attributes.item.children = $filter('orderBy')(node.attributes.item.children, 'item.instanceName');
                var items = node.attributes.item.children;
                vm.missiles = [];
                angular.forEach(items, function (item) {
                    vm.missiles.push(item.item);
                });
                vm.allMissiles = [];
                vm.missileIds = [];
                missileStorageKey = node.attributes.item.id + 'missileProc';
                /*if ($application.sessionDataStorage != undefined && validateJSON(missileStorageKey)) {
                 vm.selectedMissiles = $application.sessionDataStorage.get(missileStorageKey);
                 vm.selectedMissiles = $filter('orderBy')(vm.selectedMissiles, 'instanceName');
                 }
                 if (vm.selectedMissiles == null) {
                 vm.selectedMissiles = [];
                 }*/
                angular.forEach(vm.missiles, function (missile) {
                    angular.forEach(vm.selectedMissiles, function (selectedMissile) {
                        if (missile.id == selectedMissile.id) {
                            missile.selected = true;
                            vm.selectedMissileIds.push(missile.id);
                        }
                    });
                    if (missile.selected != true) {
                        missile.selected = false;
                    }
                    vm.allMissiles.push(missile);
                    vm.missileIds.push(missile.id);
                });
            }

            function toggledSection(open) {
                if (!open) {
                    var section = vm.selectedSection;
                    angular.forEach(vm.bomItems, function (item) {
                        item.expanded = false;
                        item.bomChildren = [];
                        if (section.id == item.id) {
                            item.select = true;
                            vm.selectedBomItems.push(item);
                            vm.selectedBomItemsMap.put(item.item.id, item);
                        }
                    });
                    /*angular.forEach(vm.selectedSections, function (section) {
                     angular.forEach(vm.bomItems, function (item) {
                     item.expanded = false;
                     item.bomChildren = [];
                     if (section.id == item.id) {
                     item.select = true;
                     vm.selectedBomItems.push(item);
                     vm.selectedBomItemsMap.put(item.item.id, item);
                     }
                     })
                     })*/
                }
            }

            $scope.printFilter = function (item) {
                if ((item.expanded == true) || item.item.bomItemType == 'PART') {
                    /*for (var i = 0, len = item.bomChildren; i < len; i++) {
                     var item2 = item.bomChildren[i];
                     if (item2.item.bomItemType == 'PART') {
                     return item;
                     break;
                     }
                     }*/
                    return item;
                }
            };

            function toggledMissile(open) {
                if (!open) {
                    missileStorageKey = vm.selectedBom.id + 'missileProc';
                    sectionStorageKey = vm.selectedBom.id + 'sectionProc';
                    /*if ($application.sessionDataStorage != undefined && validateJSON(missileStorageKey)) {
                     vm.selectedMissiles = $application.sessionDataStorage.get(missileStorageKey);
                     }
                     if ($application.sessionDataStorage != undefined && validateJSON(sectionStorageKey)) {
                     vm.selectedSection = $application.sessionDataStorage.get(sectionStorageKey);
                     }*/
                    if (vm.selectedMissiles == null) {
                        vm.selectedMissiles = [];
                    }

                    /*if (vm.selectedSection == null) {
                     vm.selectedSection = null;
                     }*/
                    vm.sections = [];
                    if (vm.selectedMissiles.length > 0) {
                        angular.forEach(vm.bomItems, function (item) {
                            if (item.bomItemType == 'SECTION') {
                                if (item.id == vm.selectedSection.id) {
                                    item.select = true;
                                }
                            }
                            vm.sections.push(item);
                        });
                    }
                }
            }

            //$rootScope.allSelectSectionsPlanning = allSelectSectionsPlanning;

            /*function allSelectSectionsPlanning(check) {
             if (check) {
             angular.forEach(vm.sections, function (sysSection) {
             sysSection.select = true;
             vm.selectedSections.push(sysSection);
             vm.selectedBomItems.push(sysSection);
             });
             } else if (!check) {
             angular.forEach(vm.sections, function (sysSection) {
             sysSection.select = false;
             });
             vm.selectedSections = [];
             vm.selectedBomItems = [];
             }
             }*/

            $scope.onDropDownSectionSelection = function (section) {
                vm.workCenter = null;
                vm.searchBomText = null;
                vm.bomSearch = false;
                vm.selectedReportItems = [];
                removeChildren(vm.selectedSection);
                vm.selectedBomItems = [];
                vm.selectedBomItemsMap = new Hashtable();
                vm.selectedSection = null;
                section.expanded = false;
                vm.selectedSection = section;
                angular.forEach(vm.bomItems, function (item) {
                    if (section.item.id == item.item.id) {
                        item.select = true;
                        vm.selectedBomItems.push(item);
                        vm.selectedBomItemsMap.put(item.item.id, item);
                        vm.selectedBomItems.sort(compare.id);
                    }
                });
                if ($application.sessionDataStorage != undefined) {
                    $application.sessionDataStorage.put(sectionStorageKey, vm.selectedSection);
                }
                toggleNode(section, 'section');
                /*if (section.select == false) {
                 angular.forEach(vm.sections, function (selectedSection) {
                 if (selectedSection.item.id == section.item.id) {
                 index = vm.selectedSections.indexOf(selectedSection);
                 if (index != -1) {
                 vm.selectedSections.splice(index, 1);
                 removeChildren(section);
                 vm.selectedBomItems.remove(section);
                 }
                 }
                 });
                 if ($application.sessionDataStorage != undefined) {
                 $application.sessionDataStorage.put(sectionStorageKey, vm.selectedSections);
                 }
                 } else {
                 vm.selectedSections.push(section);
                 angular.forEach(vm.bomItems, function (item) {
                 //item.expanded = false;
                 if (section.item.id == item.item.id) {
                 item.select = true;
                 vm.selectedBomItems.push(item);
                 vm.selectedBomItemsMap.put(item.item.id, item);
                 vm.selectedBomItems.sort(compare.id);
                 }
                 });
                 if ($application.sessionDataStorage != undefined) {
                 $application.sessionDataStorage.put(sectionStorageKey, vm.selectedSections);
                 }
                 }*/
            };

            function missileUnselected(missile) {
                var index = null;
                angular.forEach(vm.selectedMissiles, function (selectedMissil) {
                    if (selectedMissil.id == missile.id) {
                        index = vm.selectedMissiles.indexOf(selectedMissil);
                    }
                });
                if (index != -1) {
                    vm.selectedMissiles.splice(index, 1);
                    vm.selectedMissileIds.remove(missile.id);
                    var colName = missile.instanceName + "(A/I/F)";
                    var colName2 = missile.instanceName + "(S)";
                    delete intColumns[colName];//Remove from Map
                    delete intColumns2[colName2];
                    var index = columns.indexOf(colName);
                    var index2 = columns2.indexOf(colName2);
                    if (index > -1) {
                        columns.splice(index, 1);
                    }
                    if (index2 > -1) {
                        columns2.splice(index2, 1);
                    }
                }
                //vm.selectedSections.sort(compare.id);
                if ($application.sessionDataStorage != undefined) {
                    $application.sessionDataStorage.put(missileStorageKey, vm.selectedMissiles);
                }
            }

            vm.selectedReportItems = [];

            function selectItemForReport(item) {
                if (item.reportSelect) {
                    vm.selectedReportItems.push(item);

                } else {
                    var index = vm.selectedReportItems.indexOf(item);
                    if (index != -1) {
                        vm.selectedReportItems.splice(index, 1);
                    }
                }
            }

            function missileSelected(missile) {
                vm.selectedMissiles.push(missile);
                vm.selectedMissileIds.push(missile.id);
                var colName = missile.instanceName + "(A/I/F)";
                var colName2 = missile.instanceName + "(S)";
                missilesMap.put(colName, missile);
                missilesMap2.put(colName2, missile);
                columns.push(colName);
                columns2.push(colName2);
                intColumns[colName] = {
                    "columnName": colName,
                    "columnValue": null,
                    "columnType": "string"
                };
                intColumns2[colName2] = {
                    "columnName": colName2,
                    "columnValue": null,
                    "columnType": "string"
                };
                //vm.selectedSections.sort(compare.id);
                if ($application.sessionDataStorage != undefined) {
                    $application.sessionDataStorage.put(missileStorageKey, vm.selectedMissiles);
                }

                if (vm.selectedMissiles.length > 0) {
                    var temp = "";
                    angular.forEach(vm.selectedMissiles, function (missile) {
                        var index = vm.selectedMissiles.indexOf(missile);
                        vm.selectedMissiles = $filter('orderBy')(vm.selectedMissiles, 'instanceName');
                        if (index == vm.selectedMissiles.length - 1) {
                            temp = "Shortage Report For System Missile :" + $rootScope.selectedTypeWithSytem + "[" + temp + missile.itemNumber + "]";
                            vm.selectedWithSysteAndMissile = temp;
                        } else {
                            temp = temp + missile.itemNumber + ",";
                            var listOfMissiles = "Shortage Report For System Missiles :" + $rootScope.selectedTypeWithSytem + "[" + temp + "]";
                            vm.selectedWithSysteAndMissile = listOfMissiles;
                        }
                    })
                }
            }

            $scope.onDropDownMissileSelection = function (missile) {
                vm.workCenter = null;
                vm.searchBomText = null;
                vm.bomSearch = false;
                if (missile.selected == false) {
                    missileUnselected(missile);
                }
                else {
                    missileSelected(missile);
                }
            };

            $scope.selectAllMissiles = function () {
                if (vm.selectedAll) {
                    angular.forEach(vm.allMissiles, function (missile) {
                        index = vm.selectedMissiles.indexOf(missile);
                        if (index == -1) {
                            missile.selected = true;
                            missileSelected(missile);
                        }
                    })
                } else {
                    angular.forEach(vm.allMissiles, function (missile) {
                        index = vm.selectedMissiles.indexOf(missile);
                        if (index != -1) {
                            missile.selected = false;
                            missileUnselected(missile);
                        }
                    })
                }

            };

            vm.bomSearch = false;
            vm.onSelectWorkCenter = onSelectWorkCenter;
            function onSelectWorkCenter() {
                if (vm.selectedSection.expanded == true) {
                    removeChildren(vm.selectedSection);
                }
                if (vm.workCenter == null) {
                    vm.selectedBomItems = [];
                    vm.selectedBomItems.push(vm.selectedSection);
                    vm.bomSearch = false;
                }
                vm.bomSearch = false;
                toggleNode(vm.selectedSection, 'item');
                if (vm.searchBomText != null && vm.searchBomText != "" && vm.searchBomText != undefined) {
                    vm.bomSearch = true;
                }
            }

            vm.searchBom = searchBom;
            function searchBom(freeText) {
                if (freeText.length >= 2) {
                    vm.searchBomText = freeText;
                    if (vm.selectedSection.expanded == true) {
                        removeChildren(vm.selectedSection);
                    }
                    vm.bomSearch = false;
                    toggleNode(vm.selectedSection, 'item');
                    vm.bomSearch = true;
                }

            }

            vm.searchBomText = null;
            vm.resetBomSearch = resetBomSearch;
            function resetBomSearch() {
                vm.searchBomText = null;
                if (vm.selectedSection.expanded == true) {
                    removeChildren(vm.selectedSection);
                }
                vm.selectedBomItems = [];
                vm.selectedBomItems.push(vm.selectedSection);
                vm.bomSearch = false;
                toggleNode(vm.selectedSection, 'item');
                if (vm.workCenter != null && vm.workCenter != "" && vm.workCenter != undefined) {
                    vm.bomSearch = true;
                }

            }

            function validateJSON(key) {
                try {
                    JSON.parse($window.localStorage.getItem(key));
                } catch (e) {
                    return false;
                }
                return true;
            }

            var selectedItem = null;
            var selectMissile = null;
            var availableQty = null;

            function manualReAllocation(selectedBomItem, missile) {
                vm.quantity = 0;
                vm.errorMessage = "";
                selectedItem = selectedBomItem;
                selectMissile = missile;
                availableQty = selectedBomItem.listMap[missile.id].allocateQty - (selectedBomItem.listMap[missile.id].issuedQty + selectedBomItem.listMap[missile.id].issueProcessQty);
                var modal = document.getElementById("re-allocation");
                var header = document.getElementsByClassName("header-row");
                angular.forEach(header, function (head) {
                    head.style.position = "initial";
                })
                modal.style.display = "block";
            }

            function validate() {
                var valid = true;
                if (vm.quantity == "" || vm.quantity == null || vm.quantity == undefined) {
                    valid = false;
                    vm.errorMessage = "Please enter Quantity";
                } else if (vm.quantity < 1) {
                    valid = false;
                    vm.errorMessage = "Please enter positive number to re-allocate";
                } else if (vm.quantity > availableQty) {
                    valid = false;
                    vm.errorMessage = "Please enter available Quantity";
                }

                return valid;
            }

            function cancel() {
                var modal = document.getElementById("re-allocation");
                var headers = document.getElementsByClassName("header-row");
                angular.forEach(headers, function (header) {
                    header.style.position = "sticky";
                });
                modal.style.display = "none";
            }

            var columns = ["Nomenclature", "Units", "Bom Qty", "Stock", "Shortage"];
            var columns2 = ["Nomenclature", "Units", "Bom Qty", "Stock", "Shortage"];
            var intColumns = {
                "Nomenclature": {
                    "columnName": "Nomenclature",
                    "columnValue": null,
                    "columnType": "string"
                },
                "Units": {
                    "columnName": "Units",
                    "columnValue": null,
                    "columnType": "string"
                },
                "Bom Qty": {
                    "columnName": "Bom Qty",
                    "columnValue": null,
                    "columnType": "string"
                },
                "Stock": {
                    "columnName": "Stock",
                    "columnValue": null,
                    "columnType": "integer"
                },
                "Shortage": {
                    "columnName": "Shortage",
                    "columnValue": null,
                    "columnType": "integer"
                }
            };
            var intColumns2 = angular.copy(intColumns);

            function excelReport1(item, empty, exportRows) {
                $rootScope.showExportMessage(" Export in process");
                for (var i = 1; i < vm.selectedBomItems.length; i++) {
                    var exportRwDetails = [];
                    var emptyColumns = angular.copy(intColumns);
                    angular.forEach(columns, function (col) {
                        empty = emptyColumns[col];
                        if (empty != undefined) {
                            item = vm.selectedBomItems[i];
                            if (empty.columnName == 'Nomenclature') {
                                if (item.item.bomItemType == 'PART') {
                                    empty.columnValue = item.item.item.itemMaster.itemName;
                                } else if (item.item.bomItemType != 'PART') {
                                    empty.columnValue = item.item.typeRef.name;
                                }
                            } else if (empty.columnName == 'Units' && item.item.bomItemType == 'PART') {
                                empty.columnValue = item.item.item.itemMaster.itemType.units
                            } else if (empty.columnName == "Bom Qty" && item.item.bomItemType == 'PART') {
                                if (!item.item.item.itemMaster.itemType.hasLots) {
                                    empty.columnValue = item.item.quantity;
                                } else if (item.item.item.itemMaster.itemType.hasLots) {
                                    empty.columnValue = item.item.fractionalQuantity;
                                }
                            } else if (empty.columnName == "Stock" && item.item.bomItemType == 'PART') {
                                if (!item.item.item.itemMaster.itemType.hasLots) {
                                    empty.columnValue = item.stock;
                                } else if (item.item.item.itemMaster.itemType.hasLots) {
                                    empty.columnValue = item.fractionalStock;
                                }
                            } else if (empty.columnName == "Shortage" && item.item.bomItemType == 'PART') {
                                empty.columnValue = item.shortage;
                            } else if (empty.columnName.includes('(A/I/F)') && item.item.bomItemType == 'PART') {
                                var miss = missilesMap.get(empty.columnName);
                                if (item.listMap["" + miss.id] != undefined) {
                                    empty.columnValue = (item.listMap["" + miss.id].allocateQty - item.listMap["" + miss.id].issuedQty) + "/" + item.listMap["" + miss.id].issuedQty + "/" + item.listMap["" + miss.id].failedQty;
                                }

                            }
                            exportRwDetails.push(empty);
                        }
                    });

                    var exporter = {
                        exportRowDetails: exportRwDetails
                    };
                    exportRows.push(exporter);
                }
            }

            function excelReport2(item, empty, exportRows) {
                $rootScope.showExportMessage(" Export in process");
                for (var i = 0; i < vm.selectedReportItems.length; i++) {
                    var exportRwDetails = [];
                    var emptyColumns = angular.copy(intColumns2);
                    angular.forEach(columns2, function (col) {
                        empty = emptyColumns[col];
                        if (empty != undefined) {
                            item = vm.selectedReportItems[i];
                            if (empty.columnName == 'Nomenclature') {
                                if (item.item.bomItemType == 'PART') {
                                    empty.columnValue = item.item.item.itemMaster.itemName;
                                } else if (item.item.bomItemType != 'PART') {
                                    empty.columnValue = item.item.typeRef.name;
                                }
                            } else if (empty.columnName == 'Units' && item.item.bomItemType == 'PART') {
                                empty.columnValue = item.item.item.itemMaster.itemType.units
                            } else if (empty.columnName == "Bom Qty" && item.item.bomItemType == 'PART') {
                                if (!item.item.item.itemMaster.itemType.hasLots) {
                                    empty.columnValue = item.item.quantity;
                                } else if (item.item.item.itemMaster.itemType.hasLots) {
                                    empty.columnValue = item.item.fractionalQuantity;
                                }
                            } else if (empty.columnName == "Stock" && item.item.bomItemType == 'PART') {
                                if (!item.item.item.itemMaster.itemType.hasLots) {
                                    empty.columnValue = item.stock;
                                } else if (item.item.item.itemMaster.itemType.hasLots) {
                                    empty.columnValue = item.fractionalStock;
                                }
                            } else if (empty.columnName == "Shortage" && item.item.bomItemType == 'PART') {
                                empty.columnValue = item.shortage;
                            } else if (empty.columnName.includes('(S)') && item.item.bomItemType == 'PART') {
                                var miss = missilesMap2.get(empty.columnName);
                                if (!item.item.item.itemMaster.itemType.hasLots) {
                                    if (item.listMap["" + miss.id] != undefined) {
                                        empty.columnValue = item.item.quantity - item.listMap["" + miss.id].allocateQty;
                                    }
                                } else if (item.item.item.itemMaster.itemType.hasLots) {
                                    if (item.listMap["" + miss.id] != undefined) {
                                        empty.columnValue = item.item.fractionalQuantity - item.listMap["" + miss.id].allocateQty;
                                    }
                                }
                            }
                            exportRwDetails.push(empty);
                        }
                    });

                    var exporter = {
                        exportRowDetails: exportRwDetails
                    };
                    exportRows.push(exporter);
                }
            }


            function exportReports() {
                var item = null;
                var empty = null;
                var exportRows = [];
                if (vm.selectedReportItems.length > 0) {
                    excelReport2(item, empty, exportRows);
                    var exportObject = {
                        "exportRows": exportRows,
                        "fileName": vm.selectedBom.item.itemMaster.itemName + "-Planning",
                        "headers": angular.copy(columns2)
                    };

                } else {
                    excelReport1(item, empty, exportRows);
                    var exportObject = {
                        "exportRows": exportRows,
                        "fileName": vm.selectedBom.item.itemMaster.itemName + "-Planning",
                        "headers": angular.copy(columns)
                    };

                }


                CommonService.exportReport('excel', exportObject).then(
                    function (data) {
                        var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        window.open(url);
                        $timeout(function () {
                            window.close();
                        }, 2000);
                        $rootScope.showSuccessMessage("Report exported successfully");
                    }
                );
            }

            function loadWorkCenters() {
                CommonService.getLovByName("Work Centers").then(
                    function (data) {
                        if (data.values != null || data.values != undefined)
                            vm.workCenters = data.values;
                    });
            }


            function selectItems() {
                vm.selectItemsView = true;
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    initClassificationTree();
                    loadClassificationTree();
                    loadDrdoImage();
                    loadWorkCenters();
                });
            })();
        }
    }
);