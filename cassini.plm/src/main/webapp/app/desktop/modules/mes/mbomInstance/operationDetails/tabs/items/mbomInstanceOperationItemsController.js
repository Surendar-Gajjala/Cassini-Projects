define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mbomInstanceService',
        'app/shared/services/core/bopService'
    ],
    function (module) {
        module.controller('MBOMInstanceOperationItemsController', MBOMInstanceOperationItemsController);
        
        function MBOMInstanceOperationItemsController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, $uibModal,
                                        httpFactory, MBOMInstanceService, DialogService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            var removeItemTitle = parsed.html($translate.instant("REMOVE_PART_DIALOG")).html();
            var removeItemDialogMsg = parsed.html($translate.instant("REMOVE_PART_DIALOG_MESSAGE")).html();
            var itemRemovedMsg = parsed.html($translate.instant("BOP_PLAN_PART_REMOVED_MSG")).html();
            var itemCreatedMsg = parsed.html($translate.instant("BOP_PLAN_PART_CREATE_MSG")).html();
            var resourceCreatedMsg = parsed.html($translate.instant("BOP_PLAN_PART_CREATE_MSG")).html();
            var itemUpdatedMsg = parsed.html($translate.instant("BOP_PLAN_PART_UPDATE_MSG")).html();
            vm.operationId = $stateParams.operationId;
            vm.mbomInstanceId = $stateParams.mbomInstanceId;
           
           

            var emptyItem = {
                id: null,
                bopOperation: vm.bopPlanId,
                mbomItem: null,
                quantity: null,
                type: 'CONSUMED',
                notes: null
            };

            vm.addedMbomItems = [];
            vm.addItems = addItems;
            function addItems(type) {
                var options = {
                    title: "Select Parts",
                    template: 'app/desktop/modules/mes/bop/planDetails/tabs/items/mbomItemsSelectionView.jsp',
                    controller: 'MBOMItemsSelectionController as mbomItemsSelectionVm',
                    resolve: 'app/desktop/modules/mes/bop/planDetails/tabs/items/mbomItemsSelectionController',
                    width: 750,
                    data: {
                        operationPartType: type
                    },
                    showMask: true,
                    buttons: [
                        {text: 'Add', broadcast: 'app.select.bop.plan.items'}
                    ],
                    callback: function (result) {
                        vm.addedMbomItems = [];
                        angular.forEach(result, function (mbomItem) {
                            var bopPlanItem = angular.copy(emptyItem);
                            bopPlanItem.mbomItem = mbomItem.id;
                            bopPlanItem.quantity = mbomItem.quantity - mbomItem.consumedQty;
                            bopPlanItem.itemNumber = mbomItem.itemNumber;
                            bopPlanItem.itemTypeName = mbomItem.itemTypeName;
                            bopPlanItem.itemName = mbomItem.itemName;
                            bopPlanItem.description = mbomItem.description;
                            bopPlanItem.revision = mbomItem.revision;
                            bopPlanItem.type = type;
                            bopPlanItem.editMode = true;
                            bopPlanItem.isNew = true;
                            vm.addedMbomItems.push(bopPlanItem);
                            if (type == "CONSUMED") {
                                vm.consumedParts.push(bopPlanItem);
                            } else {
                                vm.producedParts.push(bopPlanItem);
                            }
                        });
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadBopPlanItems() {
                vm.loading = true;
                MBOMInstanceService.getMbomInstanceOperationItems(vm.operationId).then(
                    function (data) {
                        vm.consumedParts = data.consumedParts;
                        vm.producedParts = data.producedParts;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.saveAll = saveAll;
            function saveAll() {
                $rootScope.showBusyIndicator($("#rightSidePanel"));
                BOPService.createMultipleBopPlanItems(vm.bopPlanId, vm.addedMbomItems).then(
                    function (data) {
                        vm.selectAllCheck = false;
                        vm.addedMbomItems = [];
                        loadBopPlanItems();
                        $rootScope.loadBOPPlanTabCounts();
                        $rootScope.showSuccessMessage(resourceCreatedMsg);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.removeAll = removeAll;
            function removeAll() {
                angular.forEach(vm.addedMbomItems, function (mbomItem) {
                    vm.consumedParts.splice(vm.consumedParts.indexOf(mbomItem), 1);
                })
                vm.addedMbomItems = [];
            }

            vm.editItem = editItem;
            function editItem(item) {
                item.oldNotes = item.notes;
                item.oldQuantity = item.quantity;
                item.editMode = true;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(item) {
                if (item.id == null || item.id == "" || item.id == undefined) {
                    vm.addedMbomItems.splice(vm.addedMbomItems.indexOf(item), 1);
                    if (item.type == "CONSUMED") {
                        vm.consumedParts.splice(vm.consumedParts.indexOf(item), 1);
                    } else {
                        vm.producedParts.splice(vm.producedParts.indexOf(item), 1);
                    }
                } else {
                    item.notes = item.oldNotes;
                    item.quantity = item.oldQuantity;
                    item.editMode = false;
                }
            }

            vm.onOk = onOk;
            function onOk(item) {
                if (item.id == null || item.id == "" || item.id == undefined) {
                    saveItem(item);
                } else {
                    updateItem(item);
                }
            }

            vm.updateItem = updateItem;
            function updateItem(item) {
                $rootScope.showBusyIndicator($('.view-container'));
                BOPService.updateBopPlanItem(vm.bopPlanId, item).then(
                    function (data) {
                        item.id = data.id;
                        item.editMode = false;
                        item.isNew = false;
                        $rootScope.showSuccessMessage(itemUpdatedMsg);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.saveItem = saveItem;
            function saveItem(item) {
                $rootScope.showBusyIndicator($('.view-container'));
                BOPService.createBopPlanItem(vm.bopPlanId, item).then(
                    function (data) {
                        item.id = data.id;
                        item.editMode = false;
                        item.isNew = false;
                        vm.addedMbomItems.splice(vm.addedMbomItems.indexOf(item), 1);
                        $rootScope.loadBOPPlanTabCounts();
                        $rootScope.showSuccessMessage(itemCreatedMsg);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.removeItem = removeItem;
            function removeItem(item) {
                var options = {
                    title: removeItemTitle,
                    message: removeItemDialogMsg.format(item.itemNumber),
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        BOPService.deleteBopPlanItem(vm.bopPlanId, item.id).then(
                            function (data) {
                                loadBopPlanItems();
                                $rootScope.loadBOPPlanTabCounts();
                                $rootScope.showSuccessMessage(itemRemovedMsg);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            vm.expandedConsumedParts = true;
            vm.expandedProducedParts = true;
            vm.toggleConsumedParts = toggleConsumedParts;
            vm.toggleProducedParts = toggleProducedParts;
            function toggleConsumedParts() {
                vm.expandedConsumedParts = !vm.expandedConsumedParts;
            }

            function toggleProducedParts() {
                vm.expandedProducedParts = !vm.expandedProducedParts;
            }

            (function () {
                $scope.$on('app.mbomInstance.operation.tabActivated', function (event, args) {
                    if (args.tabId == 'details.items') {
                        loadBopPlanItems();
                    }
                });
            })();
        }
    }
)
;



