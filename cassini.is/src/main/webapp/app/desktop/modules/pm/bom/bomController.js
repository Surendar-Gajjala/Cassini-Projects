/**
 * Created by swapna on 24/04/18.
 */
define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/pm/project/bomService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/shared/services/core/itemService',
        'app/shared/services/store/topStoreService',
        'app/shared/services/store/topInventoryService',
        'app/desktop/modules/pm/bom/selectionItemDialogController',
        'app/desktop/modules/pm/bom/costToProjectDialogController',
        'app/shared/services/tm/taskService',
        'app/shared/services/store/topStockMovementService',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('BomController', BomController);

        function BomController($scope, $rootScope, $timeout, $state, DialogService, $uibModal, $window, $stateParams, $cookies,
                               BomService, ItemService, TopStockMovementService, TopInventoryService, TaskService, CommonService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-align-justify";
            $rootScope.viewInfo.title = "BOQ";

            vm.loading = true;
            vm.saveBomGroups = saveBomGroups;
            vm.addBomGroup = addBomGroup;
            vm.deleteBomGroup = deleteBomGroup;
            vm.deleteBomItem = deleteBomItem;
            vm.showBomGroup = showBomGroup;
            vm.editGroupName = editGroupName;
            vm.updateBomGroup = updateBomGroup;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.costToProjectDialogue = costToProjectDialogue;
            vm.editBomItem = editBomItem;
            vm.cancelChanges = cancelChanges;
            vm.applyChanges = applyChanges;
            vm.flag = false;
            vm.select = select;
            var bomGroup = null;
            vm.rupee = "₹";
            vm.newQuantity = 0;
            vm.newNotes = null;
            $rootScope.exportinputParams = exportinputParams;
            vm.back = back;
            vm.clear = false;
            vm.bomGroups = [];
            vm.queryString = null;
            $rootScope.heading = "Item";
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "boqName",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            var id = 0;
            var emptyBomGroup = {
                id: null,
                name: "New Group",
                active: true,
                editName: false,
                bom: null,
                bomItems: []
            };
            vm.currentBomGroup = null;

            function select() {
                var options = {
                    title: 'Select ' + $rootScope.heading + '(s)',
                    side: 'left',
                    showMask: true,
                    template: 'app/desktop/modules/pm/bom/selectionItemDialog.jsp',
                    controller: 'SelectionItemDialogController as selectionItemVm',
                    resolve: 'app/desktop/modules/pm/bom/selectionItemDialogController',
                    width: 800,
                    data: {
                        machineItems: vm.currentBomGroup.machineItems,
                        materialItems: vm.currentBomGroup.materialItems
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.bom.new'}
                    ],
                    callback: function (data) {
                        vm.selectedItems = data;
                        if (vm.selectedItems.length > 0) {
                            vm.flag = true;
                        }
                        angular.forEach(vm.selectedItems, function (bomItem) {
                            if (bomItem.itemType.objectType == "MATERIALTYPE") {
                                vm.currentBomGroup.materialItems.push(bomItem);
                            }
                            else if (bomItem.itemType.objectType == "MACHINETYPE") {
                                vm.currentBomGroup.machineItems.push(bomItem);
                            }
                        });
                        angular.forEach(vm.selectedItems, function (item) {
                            var newItem = {
                                boq: item.boq,
                                project: $stateParams.projectId,
                                itemNumber: item.itemNumber,
                                itemName: item.itemName,
                                description: item.description,
                                units: item.units,
                                notes: item.notes,
                                actualCost: 0,
                                unitPrice: 0.0,
                                unitCost: 0,
                                quantity: 0,
                                issuedQty: 0,
                                balanceQty: 0,
                                inventory: 0,
                                itemCost: 0,
                                itemPrice: 0,
                                itemType: item.itemType.objectType,
                                resourceTypeName: item.itemType.name,
                                edit: true,
                                newQuantity: 0,
                                newNotes: null,
                                newUnitPrice: 0,
                                receivedQty: 0,
                                itemId: item.id,
                                newMode: true,
                                isSavedItem: true
                            };
                            vm.currentBomGroup.bomItems.splice(0, 0, newItem)
                        });

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function back() {
                $window.history.back();
            }

            function addBomGroup() {
                if ($rootScope.selectedProject.locked == false) {
                    emptyBomGroup.name = "New Group";
                    var index = vm.bomGroups.length + 1;
                    emptyBomGroup.name += index;
                    var bom = {
                        name: emptyBomGroup.name,
                        project: $stateParams.projectId
                    };
                    BomService.createProjectBom($stateParams.projectId, bom).then(
                        function (data) {
                            vm.bomGroups.push(data);
                            data.bomItems = [];
                            editGroupName(data);
                            showBomGroup(data);

                        }
                    );
                }
            }

            function updateBomGroup(group) {
                BomService.updateProjectBom($stateParams.projectId, group).then(
                    function (data) {
                        group = data;
                    }
                )
            }

            function deleteBomGroup(bomGroup) {

                if ($rootScope.selectedProject.locked == false) {
                    var options = {
                        title: 'Delete BOQ',
                        message: 'Are you sure you want to delete this BOQ?',
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            BomService.deleteProjectBom($stateParams.projectId, bomGroup.id).then(
                                function (data) {
                                    var index = vm.bomGroups.indexOf(bomGroup);
                                    vm.bomGroups.splice(index, 1);
                                    if (bomGroup.active == true) {
                                        if (index > 0) {
                                            index = index - 1;
                                        }
                                        vm.bomGroups[index].active = true;
                                        showBomGroup(vm.bomGroups[index]);
                                        $rootScope.showSuccessMessage("Selected BOQ group deleted successfully");
                                    }
                                }
                            );
                        }
                    });
                }

            }

            function showBomGroup(currentGroup) {
                currentGroup.active = true;
                loadBomItems(currentGroup);
                vm.currentBomGroup = currentGroup;
                bomGroup = angular.copy(vm.currentBomGroup);
                angular.forEach(vm.bomGroups, function (group) {
                    if (currentGroup.id != group.id) {
                        group.active = false;
                    } else {
                        group.active = true;
                    }
                });
            }

            function editGroupName(group) {
                if ($rootScope.selectedProject.locked == false) {
                    group.editName = true;
                    $timeout(function () {
                        $("#editName" + group.id).focus().select();
                    }, 100)
                }
            }

            function loadBomGroups() {
                vm.loading = true;
                vm.bomGroups = [];
                var count = 0;
                BomService.getProjectBoms($stateParams.projectId).then(
                    function (data) {
                        if (data.length == 0) {
                            vm.loading = false;
                        }
                        angular.forEach(data, function (bom) {
                            bom.editName = false;
                            vm.bomGroups.push(bom);
                        });

                        angular.forEach(vm.bomGroups, function (group) {
                            count++;
                            if (count == vm.bomGroups.length) {
                                vm.loading = false;
                            }
                        });
                        if (count > 0) {
                            showBomGroup(vm.bomGroups[0]);
                        }
                    }
                );
            }

            function loadBomItems(group) {
                var items = [];
                BomService.getBomItems($stateParams.projectId, group.id).then(
                    function (data) {
                        group.bomItems = data;
                        group.materialItems = [];
                        group.machineItems = [];
                        angular.forEach(group.bomItems, function (item) {
                            item.profitloss = 0;
                            if (item.itemNumber != null) {
                                item.newNotes = item.notes;
                                item.editMode = false;
                                item.showValues = true;
                                item.edit = false;
                                items.push(item);
                            }
                            if (item.itemType == "MATERIALTYPE") {
                                group.materialItems.push(item);
                            }
                            else {
                                group.machineItems.push(item);
                            }
                        });
                        calculateItemTotals(items);
                    }
                )
            }

            function calculateItemTotals(items) {
                angular.forEach(items, function (item) {
                    item.newQuantity = item.quantity;
                    item.newUnitPrice = item.unitPrice;
                    item.editMode = false;
                    item.showValues = true;
                    item.edit = false;

                    //item.itemCost = (item.unitCost * item.quantity);
                    //item.itemPrice = (item.unitPrice * item.quantity);
                    //item.itemTotal = item.unitPrice * item.quantity;
                    //item.itemTotal = item.itemTotal.toFixed(2);
                    //item.profitloss = 0;
                    //if (item.itemTotal == 0.00 && item.actualCost == 0) {
                    //    item.profitloss = 0;
                    //}
                    //else {
                    //    if (item.actualCost > item.itemTotal) {
                    //        var loss = 0 - (item.actualCost - item.itemTotal);
                    //        var percentageloss = (loss / item.actualCost) * 100;
                    //        item.profitloss = percentageloss;
                    //    }
                    //    else {
                    //        var profit = item.itemTotal - item.actualCost;
                    //        var percentageProfit = (profit / item.actualCost) * 100;
                    //        item.profitloss = percentageProfit;
                    //    }
                    //}
                    //
                    //item.profitloss = item.profitloss.toFixed(2);
                });
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.queryString = freeText;
                    BomService.searchBomItems($stateParams.projectId, vm.queryString).then(
                        function (data) {
                            vm.currentBomGroup.bomItems = [];
                            angular.forEach(data, function (item) {
                                if (item.boqName == vm.currentBomGroup.name) {
                                    item.showValues = true;
                                    vm.currentBomGroup.bomItems.push(item);
                                    item.profitloss = 0;
                                    if (item.itemNumber != null) {
                                        item.itemCost = (item.unitCost * item.quantity);
                                        item.itemPrice = (item.unitPrice * item.quantity);
                                    }
                                }
                            });
                            calculateItemTotals(vm.currentBomGroup.bomItems);
                        }
                    );
                    vm.clear = true;

                } else {
                    resetPage();
                }

            }

            function isExistsItem(item, rows) {
                var exists = false;
                angular.forEach(rows, function (rowItem) {
                    if (rowItem.itemNumber == item.itemNumber) {
                        exists = true;
                    }
                });
                return exists;
            }

            function resetPage() {
                vm.queryString = null;
                showBomGroup(bomGroup);
            }

            function saveBomGroups() {
                //angular.forEach(vm.bomGroups, function (group) {
                //    saveBomGroup(group);
                //});
                saveBomGroup(vm.currentBomGroup);
            }

            vm.dbHide = true;
            function saveBomGroup(group) {
                vm.dbHide = false;

                $timeout(function () {
                    vm.dbHide = true;
                }, 1000)
                var dataToSave = [];
                var valid = true;

                for (var i = 0; i < group.bomItems.length; i++) {
                    var item = group.bomItems[i];
                    item.notes = item.newNotes;
                    if (item.itemNumber != null) {
                        item.showValues = true;
                        item.boq = group.id;
                        item.boqName = group.name;
                        item.project = $stateParams.projectId;
                        item.quantity = item.newQuantity;
                        item.unitPrice = item.newUnitPrice;

                        if (item.quantity < 0) {
                            valid = false;
                            $rootScope.showErrorMessage("Please enter +ve number for BOQ Qty");

                        }

                        else {

                            if (/*item.itemNumber == null ||*/
                            item.itemType == null || item.units == null || item.quantity == null) {
                                valid = false;
                            }
                            else if (item.quantity < 0) {
                                valid = false;
                                $rootScope.showErrorMessage("Please enter +ve number for BOQ Qty");
                            }
                            else if (item.newUnitPrice < 0) {
                                valid = false;
                                $rootScope.showErrorMessage("Please enter +ve number for Unit Price field");
                            }

                            if (valid == true) {
                                dataToSave.push(item);
                                if (dataToSave.length > 0) {
                                    vm.flag = true;
                                }
                            }
                            else {
                                break;
                            }
                        }
                    }
                }
                if (valid && dataToSave.length > 0) {
                    BomService.saveBomItems($stateParams.projectId, group.id, dataToSave).then(
                        function (data) {
                            calculateItemTotals(data);
                            group.bomItems = data;
                            $rootScope.showSuccessMessage("BOQ saved successfully");
                            loadBomItems(group);
                            vm.flag = false;
                        }
                    )
                }
            }

            function costToProjectDialogue(row) {
                if (row.itemNumber != null && row.itemNumber != undefined && row.itemNumber != "") {
                    if (row.id != null && row.id != undefined && row.id != "") {
                        BomService.getBomItemByItemNumberAndboqName($stateParams.projectId, row.itemNumber, vm.currentBomGroup.name).then(
                            function (data) {
                                if (data != null && data != "" && data != undefined) {
                                    var modalInstance = $uibModal.open({
                                        animation: true,
                                        templateUrl: 'app/desktop/modules/pm/bom/costToProjectDialog.jsp',
                                        controller: 'CostToProjectDialogController as costToProjectDialogVm',
                                        size: 'md',
                                        resolve: {
                                            boqItem: function () {
                                                return row;
                                            }
                                        }
                                    });
                                    modalInstance.result.then(
                                        function (result) {
                                            if (result != 0) {
                                                row.unitCost = result;
                                                row.itemCost = (row.unitCost * row.quantity);
                                                row.actualCost = result * row.quantity;
                                                row.itemTotal = 0;
                                                row.itemTotal = row.unitPrice * row.quantity;
                                                var percentage = 0;
                                                if (row.itemTotal == 0.00 && row.actualCost == 0) {
                                                    row.profitloss = 0;
                                                }
                                                else {
                                                    if (row.actualCost > row.itemTotal) {
                                                        var loss = 0 - (row.actualCost - row.itemTotal);
                                                        percentage = (loss / row.actualCost) * 100;
                                                        row.profitloss = percentage;
                                                    }
                                                    else {
                                                        var profit = row.itemTotal - row.actualCost;
                                                        percentage = (profit / row.actualCost) * 100;
                                                        row.profitloss = percentage;
                                                    }
                                                }
                                                row.profitloss = row.profitloss.toFixed(2);
                                                BomService.updateBomItem($stateParams.projectId, row.boq, row).then(
                                                    function (data) {

                                                    });
                                            } else {
                                                row.actualCost = 0;
                                                BomService.updateBomItem($stateParams.projectId, row.boq, row).then(
                                                    function (data) {

                                                    });
                                            }

                                        }
                                    );
                                }
                                else {
                                    $rootScope.showErrorMessage("Please save BOQ items before you proceed");
                                }

                            }
                        )
                    }
                    else {
                        $rootScope.showErrorMessage("Please save Item first then proceed");
                    }
                }
                else {
                    $rootScope.showErrorMessage("Please select valid row");
                }
            }

            function editBomItem(item) {
                item.showValues = false;
                item.editMode = true;
                $timeout(function () {
                }, 5000);
            }

            function applyChanges(item) {
                if (item.newQuantity <= 0) {
                    $rootScope.showErrorMessage("Please enter +ve number for BOQ qty");
                }
                else if (item.newUnitPrice < 0) {
                    $rootScope.showErrorMessage("Please enter +ve number for Unit Price field");
                }
                else {
                    item.quantity = item.newQuantity;
                    item.notes = item.newNotes;
                    item.unitPrice = item.newUnitPrice;
                    item.boqName = vm.currentBomGroup.name;
                    if (item.editMode == true) {
                        item.itemId = item.id;
                    }
                    var promise = BomService.updateBomItem($stateParams.projectId, vm.currentBomGroup.id, item);
                    promise.then(
                        function (data) {
                            item.id = data.id;
                            item.editMode = false;
                            item.edit = false;
                            item.showValues = true;
                            item.newMode = false;
                            item.itemCost = (item.unitCost * item.quantity);
                            item.itemPrice = (item.unitPrice * item.quantity);
                            item.itemTotal = item.unitPrice * item.quantity;
                            item.actualCost = item.unitCost * item.quantity;
                            item.itemTotal = item.itemTotal.toFixed(2);
                            item.profitloss = 0;
                            if (item.itemTotal == 0.00 && item.actualCost == 0) {
                                item.profitloss = 0;
                            }
                            else {
                                if (item.actualCost > item.itemTotal) {
                                    var loss = 0 - (item.actualCost - item.itemTotal);
                                    var percentageloss = (loss / item.actualCost) * 100;
                                    item.profitloss = percentageloss;
                                }
                                else {
                                    var profit = item.itemTotal - item.actualCost;
                                    var percentageProfit = (profit / item.actualCost) * 100;
                                    item.profitloss = percentageProfit;
                                }
                            }

                            item.profitloss = item.profitloss.toFixed(2);
                            item.balanceQty = item.quantity - item.issuedQty;
                            $rootScope.showSuccessMessage("BOQ item updated successfully");
                        },
                        function (error) {
                            $rootScope.showWarningMessage("Please click on save button");
                        }
                    )
                }

            }

            /*   function cancelChanges(bomItem) {
             bomItem.newQuantity = bomItem.quantity;
             bomItem.newNotes = bomItem.notes;
             bomItem.newUnitPrice = bomItem.unitPrice;
             bomItem.editMode = false;
             bomItem.showValues = true;
             bomItem.newMode = true;
             if (bomItem.newMode == true) {
             var index = vm.currentBomGroup.bomItems.indexOf(bomItem);
             vm.currentBomGroup.bomItems.splice(index, 1);
             angular.forEach(vm.selectedItems, function (bomItem) {
             if (bomItem.itemType.objectType == "MATERIALTYPE") {
             var index = vm.currentBomGroup.materialItems.indexOf(bomItem);
             vm.currentBomGroup.materialItems.splice(index, 1);
             }
             else if (bomItem.itemType.objectType == "MACHINETYPE") {
             var index = vm.currentBomGroup.machineItems.indexOf(bomItem);
             vm.currentBomGroup.machineItems.splice(index, 1);
             }
             });
             }
             }*/

            function cancelChanges(bomItem) {
                vm.showValues = true;
                bomItem.newQuantity = bomItem.quantity;
                bomItem.newNotes = bomItem.notes;
                bomItem.newUnitPrice = bomItem.unitPrice;
                bomItem.editMode = false;
                bomItem.showValues = true;
                bomItem.newMode = true;
                $timeout(function () {
                    bomItem.showValues = false;
                }, 500);
                loadBomGroups();
            }

            function deleteBomItem(bomItem) {

                TopInventoryService.getInventoryByItemNumberAndProject($stateParams.projectId, bomItem.itemNumber).then(
                    function (data) {
                        if (data.length > 0) {
                            $rootScope.showErrorMessage("Item is used in Inventory");
                        } else {
                            var options = {
                                title: 'Remove Item',
                                message: 'Are you sure you want to remove ' + bomItem.itemName + '(' + bomItem.itemNumber + ')?',
                                okButtonClass: 'btn-danger'
                            };
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    if (bomItem.itemId == undefined) {
                                        bomItem.itemId = bomItem.id;
                                    }
                                    BomService.deleteBomItem($stateParams.projectId, vm.currentBomGroup.id, bomItem).then(
                                        function (data) {
                                            var index = vm.currentBomGroup.bomItems.indexOf(bomItem);
                                            if (bomItem.itemType == 'MATERIALTYPE') {
                                                var index1 = vm.currentBomGroup.materialItems.indexOf(bomItem);
                                                vm.currentBomGroup.materialItems.splice(index1, 1);
                                            }
                                            else {
                                                var index1 = vm.currentBomGroup.machineItems.indexOf(bomItem);
                                                vm.currentBomGroup.machineItems.splice(index1, 1);
                                            }
                                            vm.currentBomGroup.bomItems.splice(index, 1);
                                            $rootScope.showSuccessMessage("BOQ Item removed successfully");
                                        }
                                    )
                                }
                            });
                        }
                    }
                );
            }

            function adjustContentHeight() {
                $timeout(function () {
                    var height = $("#contentpanel").height();
                    $('#view-content').height(height - 90);
                });
            }

            document.getElementById("file").onchange = function () {
                var file = document.getElementById("file");

                if (vm.importFile = file.files) {
                    $rootScope.showBusyIndicator();
                    BomService.importBoq(vm.importFile, $stateParams.projectId).then(
                        function (data) {
                            document.getElementById("file").value = "";
                            $rootScope.showSuccessMessage("Imported successfully");
                            $rootScope.hideBusyIndicator();
                            loadBomGroups();

                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                            document.getElementById("file").value = "";
                        }
                    )
                }
            };

            var inputParamHeaders = ["ItemNumber", "Item Name", "Item Description", "Item Type", "Units", "Quantity", "Notes"];

            function exportinputParams() {
                var exportRows = [];

                var exportObject = {
                    "exportRows": exportRows,
                    "fileName": $rootScope.selectedProject.name + "-" + "Boq",
                    "headers": angular.copy(inputParamHeaders)
                };

                BomService.exportBoq($rootScope.selectedProject.id, "EXCEL", exportObject).then(
                    function (data) {
                        var url = "{0}//{1}//api/projects/".format(window.location.protocol, window.location.host);
                        url += $rootScope.selectedProject.id + "/boq/export/file/" + data + "/download";
                        window.open(url, '_self');

                    }
                )
            }

            $scope.importPdf = importPdf;
            function importPdf() {
                $rootScope.showBusyIndicator($('.view-container'));
                var fileElem = document.getElementById("filePdf");
                var file = fileElem.files[0];
                if (vm.importPdfFile = file) {
                    var re = /(\.pdf|\.PDF)$/i;
                    if (!re.exec(file.name)) {
                        $rootScope.showErrorMessage("Upload pdf files only");
                        $rootScope.hideBusyIndicator();
                    } else {
                        BomService.importPdfFile(file, $stateParams.projectId).then(
                            function (data) {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showSuccessMessage("File imported successfully");
                                loadBomPdf();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            $scope.importExcel = importExcel;
            function importExcel() {
                $rootScope.showBusyIndicator($('.view-container'));
                var fileElem = document.getElementById("fileExcel");
                var file = fileElem.files[0];
                if (vm.importFile = file) {
                    $rootScope.showBusyIndicator();
                    BomService.importBoq(vm.importFile, $stateParams.projectId).then(
                        function (data) {
                            document.getElementById("file").value = "";
                            $rootScope.showSuccessMessage("File imported successfully");
                            $rootScope.hideBusyIndicator();
                            loadBomGroups();

                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                            document.getElementById("file").value = "";
                        }
                    )
                }
            }

            function loadBomPdf() {
                BomService.getPdfFile($stateParams.projectId).then(
                    function (data) {
                        vm.pdfFile = data;
                    }
                )
            }

            vm.downloadPdf = downloadPdf;

            function downloadPdf(file) {
                var url = "{0}//{1}/api/projects/{2}/boq/{3}/download".
                    format(window.location.protocol, window.location.host, file.project, file.id);
                window.open(url, '_self');
            }

            vm.filePreview = filePreview;
            function filePreview(file) {
                var fileId = file.id;
                var url = "{0}//{1}/api/projects/{2}/boq/files/{3}/preview".
                    format(window.location.protocol, window.location.host, $stateParams.projectId, fileId);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = file.name;
                });
                $timeout(function () {
                    window.close();
                }, 2000);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadBomGroups();
                    loadBomPdf();
                    adjustContentHeight();
                    $(window).resize(function () {
                        adjustContentHeight()
                    });
                }
            })();
        }
    }
);