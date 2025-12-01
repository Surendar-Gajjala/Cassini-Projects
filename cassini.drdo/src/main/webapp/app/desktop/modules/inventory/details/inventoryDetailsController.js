define(
    [
        'app/desktop/modules/inventory/inventory.module',
        'jspdf',
        'jspdf-autotable',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/bomService',
        'app/shared/services/core/inventoryService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module, jsPDF) {
        module.controller('InventoryDetailsController', InventoryDetailsController);

        function InventoryDetailsController($scope, $rootScope, $timeout, $window, $filter, $state, $stateParams, $cookies, $translate,
                                            ItemTypeService, InventoryService, BomService, CommonService, DialogService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-sitemap";
            $rootScope.viewInfo.title = "Inventory";

            var vm = this;

            vm.selectedBom = null;
            vm.loading = true;
            vm.showInventoryPage = false;
            vm.bomItems = [];


            vm.searchValue = null;

            var nodeId = 0;
            var classificationMainTree = null;
            var rootNode = null;
            vm.showInventoryInfo = false;

            vm.preventClick = preventClick;
            vm.showRequestReport = showRequestReport;
            vm.showIssueReport = showIssueReport;
            vm.showInwardReport = showInwardReport;
            vm.showInventoryInfoPanel = showInventoryInfoPanel;
            vm.stockInstancesPopOver = {
                templateUrl: 'app/desktop/modules/inventory/popovers/stockInstancesPopover.jsp'
            };
            vm.onHoldInstancesPopOver = {
                templateUrl: 'app/desktop/modules/inventory/popovers/onHoldInstancesPopover.jsp'
            };
            function preventClick(event) {
                event.stopPropagation();
                event.preventDefault();
            }

            function showInventoryInfoPanel() {
                vm.showInventoryInfo = !vm.showInventoryInfo;
                if (vm.showInventoryInfo) {
                    $('#inventoryInfoPanel').show('slide', {direction: 'left'}, 600);
                }
                else {
                    $('#inventoryInfoPanel').hide('slide', {direction: 'left'}, 600);
                    vm.showInventoryInfo = false;
                }
            }

            function initClassificationTree() {
                classificationMainTree = $('#classificationMainTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Inventory',
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

            function makeInstanceNode(item) {
                return {
                    id: ++nodeId,
                    text: item.item.instanceName,
                    iconCls: 'inst-node',
                    attributes: {
                        item: item
                    }
                };
            }

            function onSelectType(node) {

                $rootScope.closeNotification();
                var data = classificationMainTree.tree('getData', node.target);

                if (node.attributes.item) {
                    vm.selectedNode = node;
                    loadSections(vm.selectedNode);
                    showInventoryInfoPanel();
                } else {
                    vm.selectedBom = null;
                }
            }

            function loadSections(node) {
                vm.selectedBom = node.attributes.item;
                if (vm.selectedBom != null && vm.selectedBom.objectType == "BOM") {
                    vm.selectedBomParent = node.attributes.item;
                    $scope.bom = true;
                    $scope.instance = false;
                    vm.showInventoryPage = true;
                    $rootScope.viewInfo.title = vm.selectedBom.item.itemMaster.itemName + " - Inventory";
                    InventoryService.getBomInventory(vm.selectedBom.id).then(
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
                } else if (vm.selectedBom != null && vm.selectedBom.objectType == "BOMINSTANCE") {
                    vm.showInventoryPage = true;
                    $scope.bom = false;
                    $scope.instance = true;
                    if (vm.selectedBomParent != null && vm.selectedBomParent != undefined) {
                        $rootScope.viewInfo.title = vm.selectedBomParent.item.itemMaster.itemName + " - " + vm.selectedBom.item.instanceName + " - Inventory";
                    } else {
                        $rootScope.viewInfo.title = vm.selectedBom.item.instanceName + " - Inventory";
                    }

                    InventoryService.getBomInstanceInventory(vm.selectedBom.id).then(
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
                }
            }

            var nodes = [];
            $rootScope.bomTreeDetails = false;
            function loadClassificationTree() {
                BomService.getBomTree().then(
                    function (data) {
                        nodes = [];
                        angular.forEach(data, function (item) {
                            var node = makeNode(item);
                            if (item.children != null && item.children != undefined && item.children.length > 0) {
                                node.state = "closed";
                                item.children = $filter('orderBy')(item.children, 'item.instanceName');
                                visitChildren(node, item.children);
                            }
                            nodes.push(node);
                        });

                        classificationMainTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )

                if (vm.selectedBom == null) {
                    showInventoryInfoPanel();
                }
            }

            function visitChildren(parent, items) {
                var nodes = [];
                angular.forEach(items, function (item) {
                    var node = makeInstanceNode(item);
                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
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

            vm.collapseAll = collapseAll;
            vm.expandAll = expandAll;

            function collapseAll() {
                var node = $('#classificationMainTree').tree('getSelected');
                if (node) {
                    $('#classificationMainTree').tree('collapseAll', node.target);
                }
                else {
                    $('#classificationMainTree').tree('collapseAll');
                }
            }

            function expandAll() {
                var node = $('#classificationMainTree').tree('getSelected');
                if (node) {
                    $('#classificationMainTree').tree('expandAll', node.target);
                }
                else {
                    $('#classificationMainTree').tree('expandAll');
                }
            }

            function showRequestReport() {
                if (vm.selectedBom.objectType == "BOM") {
                    $rootScope.showWarningMessage("Please select Instance for Request Report.");
                } else {
                    $state.go('app.inventory.requestReport', {referenceId: vm.selectedBom.id});
                }
            }

            function showIssueReport() {
                if (vm.selectedBom.objectType == "BOM") {
                    $rootScope.showWarningMessage("Please select Instance for Issue Report.");
                } else {
                    $state.go('app.inventory.issueReport', {referenceId: vm.selectedBom.id});
                }
            }

            function showInwardReport() {
                if (vm.selectedBom.objectType == "BOMINSTANCE") {
                    $rootScope.showWarningMessage("Please select BOM for Inward Report.");
                } else {
                    $state.go('app.inventory.inwardReport', {referenceId: vm.selectedBom.id});
                }
            }

            vm.toggleNode = toggleNode;
            function toggleNode(bomItem) {
                if (vm.searchMode == false) {
                    if (bomItem.expanded == null || bomItem.expanded == undefined) {
                        bomItem.expanded = false;
                    }
                    bomItem.expanded = !bomItem.expanded;
                    var index = vm.bomItems.indexOf(bomItem);
                    if (bomItem.item.objectType == 'BOMITEM') {
                        if (bomItem.expanded == false) {
                            removeChildren(bomItem);
                        }
                        else {
                            InventoryService.getBomChildrenInventory(vm.selectedBom.id, bomItem.item.id).then(
                                function (data) {

                                    angular.forEach(data, function (item) {
                                        if (bomItem.bomChildren == undefined) {
                                            bomItem.bomChildren = [];
                                        }
                                        item.expanded = false;
                                        item.level = bomItem.level + 1;
                                        bomItem.bomChildren.push(item);
                                    });

                                    angular.forEach(bomItem.bomChildren, function (item) {
                                        index = index + 1;
                                        vm.bomItems.splice(index, 0, item);
                                    });

                                    /*-------  To expand entire section ---------------*/

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

                                     angular.forEach(bomItem.bomChildren, function (item) {
                                     item.bomChildren = [];
                                     index = index + 1;
                                     vm.bomItems.splice(index, 0, item);
                                     index = populateChildren(item, index)
                                     });
                                     } else {

                                     angular.forEach(data, function (item) {
                                     if (bomItem.bomChildren == undefined) {
                                     bomItem.bomChildren = [];
                                     }
                                     item.expanded = false;
                                     item.level = bomItem.level + 1;
                                     bomItem.bomChildren.push(item);
                                     });

                                     angular.forEach(bomItem.bomChildren, function (item) {
                                     index = index + 1;
                                     vm.bomItems.splice(index, 0, item);
                                     });
                                     }*/
                                }
                            )
                        }
                    } else if (bomItem.item.objectType == 'BOMINSTANCEITEM') {
                        if (bomItem.expanded == false) {
                            removeChildren(bomItem);
                        }
                        else {
                            InventoryService.getBomInstanceChildrenInventory(vm.selectedBom.id, bomItem.item.id).then(
                                function (data) {

                                    angular.forEach(data, function (item) {
                                        item.expanded = false;
                                        item.level = bomItem.level + 1;
                                        item.bomChildren = [];
                                        item.totalLotIssued = 0;
                                        angular.forEach(item.issuedLotInstances, function (instance) {
                                            item.totalLotIssued = item.totalLotIssued + instance.lotQty;
                                        })

                                        bomItem.bomChildren.push(item);
                                    });

                                    angular.forEach(bomItem.bomChildren, function (item) {
                                        index = index + 1;
                                        vm.bomItems.splice(index, 0, item);
                                    });

                                    /*if (bomItem.item.bomItemType == "SECTION") {
                                     angular.forEach(data, function (item) {
                                     if (bomItem.bomChildren == undefined) {
                                     bomItem.bomChildren = [];
                                     }
                                     item.expanded = true;
                                     item.level = bomItem.level + 1;
                                     item.bomChildren = [];
                                     item.totalLotIssued = 0;
                                     angular.forEach(item.issuedLotInstances, function (instance) {
                                     item.totalLotIssued = item.totalLotIssued + instance.lotQty;
                                     })

                                     bomItem.bomChildren.push(item);
                                     });

                                     angular.forEach(bomItem.bomChildren, function (item) {
                                     item.bomChildren = [];
                                     index = index + 1;
                                     vm.bomItems.splice(index, 0, item);
                                     index = populateChildren(item, index)
                                     });
                                     } else {

                                     angular.forEach(data, function (item) {
                                     item.expanded = false;
                                     item.level = bomItem.level + 1;
                                     item.bomChildren = [];
                                     item.totalLotIssued = 0;
                                     angular.forEach(item.issuedLotInstances, function (instance) {
                                     item.totalLotIssued = item.totalLotIssued + instance.lotQty;
                                     })

                                     bomItem.bomChildren.push(item);
                                     });

                                     angular.forEach(bomItem.bomChildren, function (item) {
                                     index = index + 1;
                                     vm.bomItems.splice(index, 0, item);
                                     });
                                     }*/
                                }
                            )
                        }
                    }
                }
            }

            function removeChildren(bomItem) {
                if (bomItem != null && bomItem.bomChildren != null && bomItem.bomChildren != undefined) {
                    angular.forEach(bomItem.bomChildren, function (item) {
                        removeChildren(item);
                    });

                    var index = vm.bomItems.indexOf(bomItem);
                    vm.bomItems.splice(index + 1, bomItem.bomChildren.length);
                    bomItem.bomChildren = [];
                    bomItem.expanded = false;
                }
            }

            function getIndexTopInsertNewChild(bomItem) {
                var index = 0;

                if (bomItem.bomChildren != undefined && bomItem.bomChildren != null) {
                    index = bomItem.bomChildren.length;
                    angular.forEach(bomItem.bomChildren, function (child) {
                        var childCount = getIndexTopInsertNewChild(child);
                        index = index + childCount;
                    })
                }

                return index;
            }

            function populateChildren(bomItem, lastIndex) {
                angular.forEach(bomItem.children, function (item) {
                    lastIndex++;
                    item.bomChildren = [];
                    //var index = vm.bomItems.indexOf(bomItem);
                    item.totalLotIssued = 0;
                    angular.forEach(item.issuedLotInstances, function (instance) {
                        item.totalLotIssued = item.totalLotIssued + instance.lotQty;
                    })

                    vm.bomItems.splice(lastIndex, 0, item);
                    bomItem.bomChildren.push(item);
                    lastIndex = populateChildren(item, lastIndex)
                });

                return lastIndex;
            }

            vm.partNumberPopOver = {
                templateUrl: 'app/desktop/modules/inventory/popovers/partNumberPopoverTemplate.jsp'
            };

            vm.returnPopOver = {
                templateUrl: 'app/desktop/modules/inventory/popovers/returnPopoverTemplate.jsp'
            };

            vm.issuedInstancesPopOver = {
                templateUrl: 'app/desktop/modules/inventory/popovers/issuedInstancesPopover.jsp'
            };

            vm.printBomItems = [];
            vm.getInventoryReportByBom = getInventoryReportByBom;
            function getInventoryReportByBom() {
                $rootScope.showBusyIndicator();
                InventoryService.getInventoryReportByBom(vm.selectedBom.id).then(
                    function (data) {
                        vm.printBomItems = data;
                        $timeout(function () {
                            printInventoryReport("BOM");
                        }, 1000);
                    }
                )
            }

            vm.getInventoryReportBySection = getInventoryReportBySection;
            function getInventoryReportBySection(section) {
                vm.selectedSection = section;
                $rootScope.showBusyIndicator();
                InventoryService.getInventoryReportBySection(vm.selectedBom.id, section.id).then(
                    function (data) {
                        vm.printBomItems = data;
                        $timeout(function () {
                            printInventoryReport("SECTION");
                        }, 1000);
                    }
                )
            }

            vm.getInventoryReportByInstance = getInventoryReportByInstance;
            function getInventoryReportByInstance() {
                $rootScope.showBusyIndicator();
                InventoryService.getInventoryReportByInstance(vm.selectedBom.id).then(
                    function (data) {
                        vm.printBomItems = data;
                        $timeout(function () {
                            printInventoryReport("BOMINSTANCE");
                        }, 1000);
                    }
                )
            }

            vm.getInventoryReportByInstanceSection = getInventoryReportByInstanceSection;
            function getInventoryReportByInstanceSection(section) {
                vm.selectedSection = section;
                $rootScope.showBusyIndicator();
                InventoryService.getInventoryReportByInstanceSection(vm.selectedBom.id, section.id).then(
                    function (data) {
                        vm.printBomItems = data;
                        $timeout(function () {
                            printInventoryReport("SECTION");
                        }, 1000);
                    }
                )
            }


            function printInventoryReport(type) {

                $rootScope.showBusyIndicator();
                var pdf = new jsPDF('l', 'pt', 'a4');

                pdf.setFont('arial narrow');
                pdf.setFontSize(13);
                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                var imgData = $rootScope.drdoImage;
                pdf.addImage(imgData, 'JPG', 365, 25, 125, 125);

                var today = moment(new Date());
                var todayStr = today.format('DD/MM/YYYY HH:mm');

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('bold');
                if (type == "BOM") {
                    pdf.text(330, 160, vm.selectedBom.item.itemMaster.itemName + " Inventory Report");
                } else if (type == "BOMINSTANCE") {
                    if (vm.selectedBomParent != null && vm.selectedBomParent != undefined) {
                        pdf.text(330, 160, vm.selectedBomParent.item.itemMaster.itemName + "_" + vm.selectedBom.item.instanceName + " Inventory Report");
                    } else {
                        pdf.text(330, 160, vm.selectedBom.item.instanceName + " Inventory Report");
                    }
                } else {
                    if ($scope.bom) {
                        pdf.text(330, 160, vm.selectedBom.item.itemMaster.itemName + "_" + vm.selectedSection.typeRef.name + " Inventory Report");
                    } else {
                        if (vm.selectedBomParent != null) {
                            pdf.text(330, 160, vm.selectedBomParent.item.itemMaster.itemName + "_" + vm.selectedBom.item.instanceName + "_" + vm.selectedSection.typeRef.name + " Inventory Report");
                        } else {
                            pdf.text(330, 160, vm.selectedBom.item.instanceName + "_" + vm.selectedSection.typeRef.name + " Inventory Report");
                        }
                    }
                }


                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.text(665, 170, "Date : " + todayStr);
                var personDetail = pdf.autoTableHtmlToJson(document.getElementById("printInventoryReport"));

                pdf.autoTable(personDetail.columns, personDetail.data, {
                    startY: 190,
                    margin: {horizontal: 30},
                    headerStyles: {
                        lineWidth: 0.2,
                        lineColor: [7, 7, 7],
                        fillColor: [255, 255, 255],
                        textColor: [7, 7, 7]
                    },
                    styles: {overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {
                        0: {
                            columnWidth: 300, overflow: 'linebreak', overflowColumns: false,
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        1: {
                            columnWidth: 150, overflow: 'linebreak', overflowColumns: false,
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
                            columnWidth: 70, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        4: {
                            columnWidth: 60, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        5: {
                            columnWidth: 70, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        },
                        6: {
                            columnWidth: 70, overflow: 'linebreak', overflowColumns: false,
                            valign: 'middle', halign: 'center',
                            fillColor: [255, 255, 255],
                            textColor: [7, 7, 7]
                        }
                    },
                    drawCell: function (cell, data) {
                        var rowData = vm.printBomItems[data.row.index];
                        if (rowData.item.bomItemType != "PART") {
                            pdf.setFontStyle('bold');
                            pdf.setFontSize(11);
                        } else {
                            pdf.setFontStyle('normal');
                            pdf.setFontSize(10);
                        }
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
                });

                /*if (type == "BOM") {
                 pdf.save(vm.selectedBom.item.itemMaster.itemName + ".pdf");
                 } else if (type == "BOMINSTANCE") {
                 if (vm.selectedBomParent != null && vm.selectedBomParent != undefined) {
                 pdf.save(vm.selectedBomParent.item.itemMaster.itemName + "" + vm.selectedBom.item.instanceName + ".pdf");
                 } else {
                 pdf.save(vm.selectedBom.item.instanceName + ".pdf");
                 }
                 } else {
                 if ($scope.bom) {
                 pdf.save(vm.selectedBom.item.itemMaster.itemName + " " + vm.selectedSection.typeRef.name + ".pdf");
                 } else {
                 pdf.save(vm.selectedBom.item.instanceName + " " + vm.selectedSection.typeRef.name + ".pdf");
                 }
                 }*/

                window.open(pdf.output('bloburl'), '_blank');
                $rootScope.hideBusyIndicator();

                $rootScope.showSuccessMessage("Report generated successfully");
            }

            vm.searchBomText = null;
            vm.searchBom = searchBom;
            vm.resetBomSearch = resetBomSearch;
            vm.searchMode = false;
            function searchBom(freeText) {
                if (freeText != null && freeText != "" && freeText != undefined && freeText.length >= 2) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    $rootScope.searchBomText = freeText;
                    if (vm.selectedBom.objectType == "BOM") {
                        InventoryService.searchBomInventory(vm.selectedBom.id, freeText).then(
                            function (data) {
                                vm.searchMode = true;
                                vm.bomItems = data;
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        InventoryService.searchBomInstanceInventory(vm.selectedBom.id, freeText).then(
                            function (data) {
                                vm.searchMode = true;
                                vm.bomItems = data;
                                angular.forEach(vm.bomItems, function (bomItem) {
                                    bomItem.totalLotIssued = 0;
                                    angular.forEach(bomItem.issuedLotInstances, function (instance) {
                                        bomItem.totalLotIssued = bomItem.totalLotIssued + instance.lotQty;
                                    })
                                })
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                } else {
                    if (freeText.length == 0) {
                        resetBomSearch();
                    }
                }
            }

            function resetBomSearch() {
                $rootScope.searchBomText = null;
                vm.searchMode = false;
                loadSections(vm.selectedNode);
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    initClassificationTree();
                    loadClassificationTree();
                });
            })();
        }
    }
);