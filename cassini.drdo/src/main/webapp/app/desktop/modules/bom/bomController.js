define(
    [
        'app/desktop/modules/bom/bom.module',
        'jspdf',
        'jspdf-autotable',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/drdoApplication',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/modules/bom/details/bomDetailsController',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/bomService',
        'app/desktop/modules/bom/new/createArticlesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module, jsPDF) {
        module.controller('BomController', BomController);

        function BomController($scope, $rootScope, $timeout, $bom, $stateParams, $filter, $uibModal, $cookies, $window, $translate,
                               CommonService, ItemTypeService, ItemService, DialogService, BomService) {

            $rootScope.viewInfo.icon = "fa fa-sitemap";
            $rootScope.viewInfo.title = "  BOM";

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.loading = true;
            var lastSelectedNode = null;
            vm.showAddItem = false;
            vm.addItemNumber = addItemNumber;
            vm.deleteItemNumber = deleteItemNumber;

            var nodeId = 0;
            var classificationMainTree = null;
            var rootNode = null;

            vm.newItem = {
                id: null,
                item: {
                    id: null,
                    itemNumber: null,
                    itemName: null,
                    units: 'Each',
                    description: null,
                    drawingNumber: null,
                    itemType: {
                        id: null,
                        name: null
                    },
                    revision: null,
                    status: null,
                    hasBom: false,
                    newDescription: null
                },
                parent: null,
                quantity: 1,
                description: null,
                material: null,
                notes: null,
                level: 0,
                expanded: false,
                section: null,
                systemId: null,
                subSystemId: null,
                unitId: null
            };

            vm.preventClick = preventClick;
            vm.showBomInfoPanel = showBomInfoPanel;

            function preventClick(event) {
                event.stopPropagation();
                event.preventDefault();
            }

            vm.showBomInfo = false;

            function showBomInfoPanel() {
                vm.showBomInfo = !vm.showBomInfo;
                if (vm.showBomInfo) {
                    $('#bomInfoPanel').show('slide', {direction: 'left'}, 600);
                }
                else {
                    $('#bomInfoPanel').hide('slide', {direction: 'left'}, 600);
                }
            }

            function initBOMTree() {
                classificationMainTree = $('#classificationMainTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'BOM',
                            iconCls: 'bom-root',
                            attributes: {
                                item: null
                            },
                            children: []
                        }
                    ],
                    onContextMenu: onContextMenu,
                    onAfterEdit: onAfterEdit,
                    onSelect: onSelectType
                });

                rootNode = classificationMainTree.tree('find', 0);

                $(document).click(function () {
                    $("#contextMenu").hide();
                });
            }

            function onAfterEdit(node) {
                if (vm.selectedNodeType != null) {
                    BomService.createBomInstance(vm.selectedNodeType.attributes.item.item.id, node.text).then(
                        function (bom) {
                            node.attributes.item = angular.copy(bom);
                            node.text = bom.item.instanceName;
                            classificationMainTree.tree('update', {
                                target: node.target,
                                attributes: node.attributes
                            });
                        }, function (error) {
                            classificationMainTree.tree('remove', node.target);
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function deleteItemNumber() {
                if (vm.selectedNodeType.attributes.item.item.instanceName != undefined) {
                    BomService.deleteBomInstance(vm.selectedNodeType.attributes.item.item.id).then(
                        function () {
                            classificationMainTree.tree('remove', vm.selectedAddItemNode.target);
                            $rootScope.showSuccessMessage("Instance deleted successfully");
                            $rootScope.viewInfo.title = "  BOM";
                        }
                    )
                }
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

            vm.selectedBom = null;
            vm.missileParent = null;
            vm.selectedBomData = null;
            vm.selectedBomNodeData = null;
            $rootScope.selectedTypeSystemWithMissile = null;
            $rootScope.selectedTypeWithSytem = null;
            $rootScope.checkWithSystemWithMissile = false;
            $rootScope.checkWithSystem = false;
            var itemObject = null;

            function onSelectType(node) {
                vm.selectedNodeType = node;
                $rootScope.selectedMissile = null;
                $rootScope.closeNotification();
                var data = classificationMainTree.tree('getData', node.target);
                itemObject = data.attributes.item;
                if (node.attributes.item) {
                    vm.selectedBom = node.attributes.item;
                    if (node.attributes.item.objectType == "BOM") {
                        $rootScope.viewInfo.title = "  BOM - " + node.attributes.item.item.itemMaster.itemName;
                        vm.showAddItem = true;
                        $scope.$broadcast('app.bom.selected', {
                            parentObject: null,
                            itemObject: itemObject
                        });
                        //showBomInfoPanel();
                    } else {
                        var parent = classificationMainTree.tree('getParent', node.target);
                        $rootScope.viewInfo.title = "  BOM - " + parent.attributes.item.item.itemMaster.itemName + " - " + node.attributes.item.item.instanceName;
                        vm.showAddItem = false;
                        showBomInfoPanel();
                        $rootScope.selectedMissile = vm.selectedBom;
                        $scope.$broadcast('app.bom.selected', {
                            parentObject: parent.attributes.item,
                            itemObject: itemObject
                        });
                    }


                } else {
                    $rootScope.viewInfo.title = "  BOM";
                    vm.showAddItem = false;
                    $scope.$broadcast('app.bom.selected', {parentObject: null, itemObject: null});
                }
            }

            function onContextMenu(e, node) {
                vm.selectedAddItemNode = node;
                e.preventDefault();
                var parent = classificationMainTree.tree('getParent', node.target);
                var $contextMenu = $("#contextMenu");
                $contextMenu.show();
                $('#addType').show();
                $('#deleteType').show();
                $('#importBOM').show();
                if (parent === null) {
                    $('#addType').hide();
                    $('#deleteType').hide();
                    $('#importBOM').hide();
                    $contextMenu.hide();
                } else if (parent.id === 0) {
                    $('#addType').text('Create Instances');
                    $('#importBOM').text('Import BOM');
                    $('#deleteType').hide();
                } else {
                    $('#deleteType').show();
                    $('#deleteType').text('Delete Instance');
                    $('#addType').hide();
                }

                $contextMenu.css({
                    left: e.pageX,
                    top: e.pageY
                });


                $contextMenu.on("click", "a", function () {
                    $contextMenu.hide();
                });

                if (node.attributes.item.instanceName != undefined) {
                    $('#addType').hide();
                    $contextMenu.hide();
                }
            }

            var nodes = [];
            $rootScope.bomTreeDetails = false;
            function loadBOMTree() {
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
                        $('#importBOM').click(function () {
                            $('#fileUpload').trigger('click');
                        });
                        /*$('#fileUpload').on('change', function (e) { //dynamic property binding
                         uploadFile();
                         });*/
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
                showBomInfoPanel();
                updateUniqueCodes();
            }

            $scope.uploadFile = function () {
                var file = document.getElementById("fileUpload").valueOf();
                if (file.files[0] != null && file.files[0] != undefined && file != "") {
                    $rootScope.showExportMessage("File import is in process");
                    BomService.importBomParts(itemObject.id, file.files[0]).then(
                        function (data) {
                            $rootScope.showSuccessMessage("BOM imported successfully");
                            onSelectType(vm.selectedNodeType);
                            $("#fileUpload").val("");
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $("#fileUpload").val("");
                        })
                }
            };

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

            function updateUniqueCodes() {
                BomService.updateUniqueCodes().then(
                    function (data) {

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

            function addItemNumber() {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/bom/new/createArticlesView.jsp',
                    controller: 'CreateArticlesController as newArticleVm',
                    size: 'sm',
                    resolve: {
                        itemObject: function () {
                            return vm.selectedNodeType;
                        }
                    }
                });

                modalInstance.result.then(
                    function (result) {
                        var selectedNode = classificationMainTree.tree('getSelected');
                        if (result != undefined && result.length > 0) {
                            classificationMainTree.tree('expand', selectedNode.target);
                            $rootScope.showBusyIndicator($(".view-content"));
                            BomService.createBomInstance(vm.selectedNodeType.attributes.item.item.id, result).then(
                                function (data) {
                                    angular.forEach(data, function (missile) {
                                        vm.selectedAddItemNode = selectedNode;
                                        if (selectedNode != null) {
                                            classificationMainTree.tree('append', {
                                                parent: selectedNode.target,
                                                data: [
                                                    {
                                                        id: ++nodeId,
                                                        iconCls: 'inst-node',
                                                        text: missile.item.instanceName,
                                                        attributes: {
                                                            item: missile
                                                        }
                                                    }
                                                ]
                                            });
                                        }
                                    });
                                    $rootScope.showSuccessMessage("Instances created successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                );
            }

            vm.searchTree = searchTree;
            vm.searchValue = null;
            function searchTree() {
                if (vm.searchValue != null) {
                    $('#classificationMainTree').tree('expandAll');
                }
                $('#classificationMainTree').tree('doFilter', vm.searchValue);
            }


            vm.showRequestReport = showRequestReport;

            function showRequestReport() {
                var modal = document.getElementById("report-view");
                modal.style.display = "block";
            }

            vm.closeRequestReport = closeRequestReport;
            function closeRequestReport() {
                var modal = document.getElementById("report-view");
                modal.style.display = "none";
            }

            vm.printRequestReport = printRequestReport;

            function printRequestReport() {

                $rootScope.showBusyIndicator();
                var pdf = new jsPDF('l', 'pt', 'a4');

                pdf.setFont('arial narrow');
                pdf.setFontSize(13);
                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                var imgData = $rootScope.drdoImage;
                pdf.addImage(imgData, 'JPG', 350, 25, 125, 125);

                var today = moment(new Date());
                var todayStr = today.format('DD/MM/YYYY');

                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('bold');
                pdf.text(350, 160, vm.selectedBom.item.instanceName + " Request Report");
                pdf.setTextColor(7, 7, 7);
                pdf.setFontStyle('normal');
                pdf.text(50, 170, "BOM :" + $rootScope.selectedSystemDetails.itemMaster.itemName);
                pdf.text(675, 170, "Date : " + todayStr);
                var personDetail = pdf.autoTableHtmlToJson(document.getElementById("printRequestReport"));

                pdf.autoTable(personDetail.columns, personDetail.data, {
                    startY: 190,
                    margin: {horizontal: 50},
                    styles: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false},
                    columnStyles: {text: {columnWidth: [200, 100], overflow: 'linebreak', overflowColumns: false}},
                    drawCell: function (cell, data) {
                        var rowData = vm.requestReportItems[data.row.index];
                        if (rowData.item.bomItemType != "PART") {
                            pdf.setFontStyle('bold');
                            pdf.setFontSize(11);
                        } else {
                            pdf.setFontStyle('normal');
                            pdf.setFontSize(12);
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

                //pdf.save($rootScope.selectedSystemDetails.itemMaster.itemName + "_" + vm.selectedBom.item.instanceName + ".pdf");
                window.open(pdf.output('bloburl'), '_blank');
                closeRequestReport();
                $rootScope.hideBusyIndicator();

                $rootScope.showSuccessMessage("Report generated successfully");

            }

            vm.synchronizeSelectedBom = synchronizeSelectedBom;
            function synchronizeSelectedBom() {
                if (vm.selectedBom != null) {
                    var options = {
                        title: "Synchronize BOM",
                        message: "Please confirm to Synchronize BOM.",
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                $rootScope.showBusyIndicator($('.view-container'));
                                BomService.synchronizeBom(vm.selectedBom.id).then(
                                    function (data) {
                                        $rootScope.showSuccessMessage("BOM updated successfully");
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }
                    )
                }
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    initBOMTree();
                    loadBOMTree();
                });
            })();
        }
    }
)
;