define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/productionOrderService',
        'app/shared/services/core/mbomInstanceService'
    ],
    function (module) {
        module.controller('ProductionOrderItemsController', ProductionOrderItemsController);

        function ProductionOrderItemsController($scope, $rootScope, $state, $timeout, $sce, $window, $stateParams, $translate,
                                                DialogService, ProductionOrderService, MBOMInstanceService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.loading = true;
            vm.productionOrderId = $stateParams.productionOrderId;
            var emptyItem = {
                id: null,
                productionOrder: vm.productionOrderId,
                mbomRevision: null,
                bopRevision: null,
                quantityProduced: null
            };

            $rootScope.loadProductionOrderItems = loadProductionOrderItems;
            function loadProductionOrderItems() {
                vm.loading = true;
                vm.searchText = "";
                vm.productionOrderItems = [];
                ProductionOrderService.getProductionOrderItems(vm.productionOrderId).then(
                    function (data) {
                        vm.productionOrderItems = [];
                        angular.forEach(data, function (item) {
                            item.editMode = false;
                            item.isNew = false;
                            item.itemChildren = [];
                            item.level = 0;
                            item.expanded = true;
                            vm.productionOrderItems.push(item);
                            var index = vm.productionOrderItems.indexOf(item);

                            angular.forEach(item.children, function (child) {
                                index++;
                                child.editMode = false;
                                child.isNew = false;
                                child.level = item.level + 1;
                                child.expanded = false;
                                child.groupName = item.name;
                                item.itemChildren.push(child);
                                vm.productionOrderItems.splice(index, 0, child);
                            })
                        })
                        vm.loading = false;
                    }
                );
            }

            var create = parsed.html($translate.instant("CREATE")).html();
            vm.addItems = addItems;
            function addItems() {
                var options = {
                    title: "Add Items",
                    template: 'app/desktop/modules/mes/productionOrder/details/tabs/items/newProductionOrderItemView.jsp',
                    controller: 'NewProductionOrderItemController as newProductionOrderItemVm',
                    resolve: 'app/desktop/modules/mes/productionOrder/details/tabs/items/newProductionOrderItemController',
                    width: 550,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.productionOrder.items.add'}
                    ],
                    callback: function () {
                        loadProductionOrderItems();
                        $rootScope.loadProductionOrderTabCounts();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var deleteMessage = parsed.html($translate.instant("ITEM_DELETED_MESSAGE")).html();
            var deleteDialogueMsg = parsed.html($translate.instant("DELETE_ITEM_DIALOG_MESSAGE")).html();
            var deleteGroupTitle = parsed.html($translate.instant("DELETE_ITEM")).html();

            vm.deleteItem = deleteItem;
            function deleteItem(item) {
                var options = {
                    title: deleteGroupTitle,
                    message: deleteDialogueMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        var promise = null;
                        if (item.objectType == 'MBOMINSTANCE') {
                            promise = ProductionOrderService.deleteProductionOrderItemInstance($stateParams.productionOrderId, item.id);
                        } else {
                            promise = ProductionOrderService.deleteProductionOrderItem($stateParams.productionOrderId, item.id);
                        }
                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    $rootScope.showSuccessMessage(deleteMessage);
                                    loadProductionOrderItems();
                                    $rootScope.loadProductionOrderTabCounts();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                });
            }

            vm.saveItem = saveItem;
            function saveItem(item) {
                if (validate(item)) {
                    $rootScope.showBusyIndicator();
                    if (item.objectType == "MBOMINSTANCE") {
                        ProductionOrderService.updateProductionOrderItemInstance($stateParams.productionOrderId, item).then(
                            function (data) {
                                item.id = data.id;
                                item.editMode = false;
                                item.children = [];
                                $rootScope.showSuccessMessage("Item updated successfully");
                                $rootScope.hideBusyIndicator();
                                $rootScope.loadProductionOrderTabCounts();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        var children = item.children;
                        var itemChildren = item.itemChildren;
                        item.children = [];
                        item.itemChildren = [];
                        ProductionOrderService.updateProductionOrderItem($stateParams.productionOrderId, item).then(
                            function (data) {
                                item.id = data.id;
                                item.editMode = false;
                                item.isNew = false;
                                item.children = data.children;
                                item.itemChildren = itemChildren;
                                var index = vm.productionOrderItems.indexOf(item);
                                if (item.expanded == true) {
                                    if (item != null && item.itemChildren != null && item.itemChildren != undefined) {
                                        vm.productionOrderItems.splice(index + 1, item.itemChildren.length);
                                        item.itemChildren = [];
                                        item.expanded = false;
                                    }
                                }
                                $timeout(function () {
                                    item.expanded = true;
                                    angular.forEach(item.children, function (child) {
                                        index++;
                                        child.editMode = false;
                                        child.isNew = false;
                                        child.level = item.level + 1;
                                        child.expanded = false;
                                        item.itemChildren.push(child);
                                        vm.productionOrderItems.splice(index, 0, child);
                                    });
                                    $rootScope.showSuccessMessage("Item updated successfully");
                                    $rootScope.hideBusyIndicator();
                                }, 500);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function validate(item) {
                var valid = true;
                if (item.name == null || item.name == "" || item.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                }
                return valid;
            }

            vm.removeItem = removeItem;
            function removeItem(item) {
                vm.productionOrderItems.splice(index + 1, item.itemChildren.length);
                item.itemChildren = [];
                item.expanded = false;
                vm.productionOrderItems.splice(vm.productionOrderItems.indexOf(item), 1);
            }

            vm.editItem = editItem;
            function editItem(item) {
                item.editMode = true;
                item.oldName = item.name;
                item.oldSerialNumber = item.serialNumber;
                item.oldBatchNumber = item.batchNumber;
                item.oldDescription = item.description;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(item) {
                item.editMode = false;
                item.name = item.oldName;
                item.serialNumber = item.oldSerialNumber;
                item.batchNumber = item.oldBatchNumber;
                item.description = item.oldDescription;
            }

            vm.toggleNode = toggleNode;
            function toggleNode(item) {
                if (item.expanded == null || item.expanded == undefined) {
                    item.expanded = false;
                }
                item.expanded = !item.expanded;
                var index = vm.productionOrderItems.indexOf(item);
                if (item.expanded == false) {
                    if (item != null && item.itemChildren != null && item.itemChildren != undefined) {
                        vm.productionOrderItems.splice(index + 1, item.itemChildren.length);
                        item.itemChildren = [];
                        item.expanded = false;
                    }
                } else {
                    angular.forEach(item.children, function (child) {
                        index++;
                        child.editMode = false;
                        child.isNew = false;
                        child.level = item.level + 1;
                        child.expanded = false;
                        child.groupName = item.name;
                        item.itemChildren.push(child);
                        vm.productionOrderItems.splice(index, 0, child);
                    })
                }
            }

            vm.showDetails = showDetails;
            function showDetails(orderItem) {
                $window.localStorage.setItem("mbomInstance_open_from", vm.productionOrderId);
                if (orderItem.objectType == "PRODUCTIONORDERITEM") {
                    $state.go('app.mes.mbom.details', {mbomId: orderItem.mbomRevision, tab: 'details.basic'});
                } else {
                    $state.go('app.mes.mbomInstance.details', {mbomInstanceId: orderItem.id, tab: 'details.basic'});
                }
            }

            (function () {
                $scope.$on('app.productionOrder.tabActivated', function (event, data) {
                    if (data.tabId == 'details.items') {
                        loadProductionOrderItems();
                        $window.localStorage.setItem("mbomInstance_open_from", null);
                    }
                });
            })();
        }
    }
)
;