/**
 * Created by swapna on 19/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'

    ],
    function (module) {
        module.controller('PurchaseOrderRequisitionItemsController', PurchaseOrderRequisitionItemsController);

        function PurchaseOrderRequisitionItemsController($scope, CustomPurchaseOrderService, $rootScope, DialogService, $window, $state, $stateParams) {
            var vm = this;

            var reqItemsMap = new Hashtable();

            vm.editPurchaseItem = editPurchaseItem;
            vm.applyChanges = applyChanges;
            vm.removeFromPurchaseItems = removeFromPurchaseItems;
            vm.openPurchaseItemDetails = openPurchaseItemDetails;
            vm.deletePurchaseItem = deletePurchaseItem;
            vm.cancelChanges = cancelChanges;
            vm.purchaseOrderRequesitionItems = [];

            vm.purchaseItem = {
                id: null,
                customPurchaseOrder: null,
                requisition: null,
                materialItem: null,
                purchaseItemQuantity: null,
                purchaseItemNotesObject: null,
                reqItemQuantity: null,
                quantity: null,
                notes: null
            }

            var pageable = {
                page: 0,
                size: 20
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.groupedItemsPurchaseOrder = [];

            function editPurchaseItem(item) {
                item.editMode = true;
                item.showEditButton = false;
            }

            function openPurchaseItemDetails(item) {
                item.materialItem.quantity = item.quantity;
                var options = {
                    title: 'Item Details',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/itemBasicInfoView.jsp',
                    controller: 'ItemBasicInfoController as itemBasicInfoVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/itemBasicInfoController',
                    width: 600,
                    data: {
                        item: item.materialItem
                    },
                    buttons: [
                        /*{text: 'Update', broadcast: 'app.stores.receivedItem.info'}*/
                    ],
                    callback: function () {
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function applyChanges(item) {
                item.notes = item.purchaseItemNotesObject;
                item.quantity = item.purchaseItemQuantity;
                CustomPurchaseOrderService.updateCustomPurchaseOrderItem(vm.purchaseOrder, item).then(
                    function (data) {
                        item.showEditButton = true;
                        item.editMode = false;
                        vm.purchaseItem.notes = item.purchaseItemNotesObject;
                        vm.purchaseItem.quantity = item.purchaseItemQuantity;
                    });
            }

            function deletePurchaseItem(item) {
                var options = {
                    title: 'Delete Purchase Order Item',
                    message: 'Are you sure you want to delete this Item?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        removeFromPurchaseItems(item);
                        CustomPurchaseOrderService.deletePurchaseItem(vm.purchaseOrder.id, item.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Purchase Order item deleted successfully");
                            }, function (error) {

                            }
                        )
                    }
                });
            }

            function cancelChanges(item) {
                item.showEditButton = true;
                item.editMode = false;
                item.purchaseItemNotesObject = item.notes;
                item.purchaseItemQuantity = item.quantity;
            }

            function removeFromPurchaseItems(purchaseItem) {
                var purchaseItemIndex = vm.groupedItemsPurchaseOrder.findIndex(item = > item.materialItem.id == purchaseItem.materialItem.id
            )
                ;
                var items = reqItemsMap.get(purchaseItem.requisition.requisitionNumber);
                if (items.length == 1) {
                    if (purchaseItemIndex != -1) {
                        vm.groupedItemsPurchaseOrder.splice(purchaseItemIndex, 1);
                    }
                    var requestNumberIndex = vm.groupedItemsPurchaseOrder.findIndex(item = > item.materialItem.itemNumber == purchaseItem.requisition.requisitionNumber
                )
                    ;
                    if (requestNumberIndex != -1) {
                        vm.groupedItemsPurchaseOrder.splice(requestNumberIndex, 1);
                        reqItemsMap.remove(purchaseItem.requisition.requisitionNumber);
                        $rootScope.showSuccessMessage("Item removed successfully");
                    }
                } else {
                    if (purchaseItemIndex != -1) {
                        vm.groupedItemsPurchaseOrder.splice(purchaseItemIndex, 1);
                        items = reqItemsMap.get(purchaseItem.requisition.requisitionNumber);
                        var removableItemIndex = items.findIndex(item = > item.materialItem.id == purchaseItem.materialItem.id
                    )
                        ;
                        if (removableItemIndex != -1) {
                            items.splice(removableItemIndex, 1);
                            reqItemsMap.put(purchaseItem.requisition.requisitionNumber, items);
                        }
                    }
                }

                if (vm.groupedItemsPurchaseOrder.length == 0) {
                    vm.showCreatePurchaseButton = false;
                }
            }

            function back() {
                $window.history.back();
            }

            function loadGroupByRequisitionItems(requistion, reqItem) {
                vm.groupedItemsPurchaseOrder = [];
                var items = reqItemsMap.get(requistion.requisitionNumber);
                if (items == null) {
                    items = [];
                    reqItemsMap.put(requistion.requisitionNumber, items);
                }
                items.push(reqItem);
                var keys = reqItemsMap.keys();
                angular.forEach(keys, function (key) {
                    vm.groupedItemsPurchaseOrder.push({rowType: "req", materialItem: {itemNumber: key}});
                    items = reqItemsMap.get(key);
                    angular.forEach(items, function (item) {
                        var purchaseItem = angular.copy(vm.purchaseItem);
                        purchaseItem.rowType = "item";
                        purchaseItem.id = item.id;
                        purchaseItem.requisition = requistion;
                        purchaseItem.materialItem = item.materialItem;
                        purchaseItem.reqItemQuantity = item.quantity;
                        purchaseItem.purchaseItemQuantity = item.quantity;
                        purchaseItem.notes = item.notes;
                        purchaseItem.quantity = item.quantity;
                        purchaseItem.isNew = item.isNew;
                        purchaseItem.editMode = item.editMode;
                        purchaseItem.showEditButton = item.showEditButton;
                        purchaseItem.purchaseItemNotesObject = purchaseItem.isNew == true ? null : item.notes;
                        vm.groupedItemsPurchaseOrder.push(purchaseItem);
                    });
                });
                if (vm.groupedItemsPurchaseOrder.length > 0) {
                    $rootScope.groupedItemsPurchaseOrder = vm.groupedItemsPurchaseOrder;
                    vm.showCreateIndentButton = true;
                }
                if (requistion.customRequisitionItems != null) {
                    var requestNumberIndex = requistion.customRequisitionItems.findIndex(item = > item.materialItem.id == reqItem.materialItem.id
                )
                    ;
                    if (requestNumberIndex != -1) {
                        requistion.customRequisitionItems.splice(requestNumberIndex, 1);
                    }
                }
            }

            function loadPurchaseAndPurchaseItems() {
                reqItemsMap = new Hashtable();
                CustomPurchaseOrderService.getPurchaseOrder($stateParams.purchaseOrderId).then(
                    function (data) {
                        /*  load PurchaseOrder & title*/
                        vm.purchaseOrder = data;

                        /* loading PurchaseOrdersItems */
                        angular.forEach(data.customPurchaseOrdersItems, function (item) {
                            item.isNew = false;
                            item.editMode = false;
                            item.showEditButton = true;
                            loadGroupByRequisitionItems(item.requisition, item);
                        });
                    }, function (error) {

                    }
                );
            }

            function addItems() {
                var options = {
                    title: 'Items',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/purchaseOrders/new/purchaseOrderRequestsView.jsp',
                    controller: 'PurchaseOrderRequestsController as purchaseOrderRequestsVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/purchaseOrders/new/purchaseOrderRequestsController',
                    width: 700,
                    data: {
                        groupedItemsPurchaseOrder: vm.groupedItemsPurchaseOrder,
                        itemsMap: reqItemsMap
                    },
                    buttons: [],
                    callback: function (requistion, reqItem) {
                        loadGroupByRequisitionItems(requistion, reqItem);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function update() {
                if (validatePurchaseOrder()) {
                    CustomPurchaseOrderService.updateCustomPurchaseOrder(vm.purchaseOrder).then(
                        function (data) {
                            vm.showCreatePurchaseButton = false;
                            vm.editMode = false;
                            vm.purchaseOrder.status = data.status;
                            $rootScope.showSuccessMessage("PurchaseOrder (" + data.poNumber + ") updated successfully");
                            angular.forEach(data.customPurchaseOrdersItems, function (poItem) {
                                var itemIndex = vm.groupedItemsPurchaseOrder.findIndex(item = > item.materialItem.id == poItem.materialItem.id
                                )
                                ;
                                vm.groupedItemsPurchaseOrder[itemIndex].isNew = false;
                                vm.groupedItemsPurchaseOrder[itemIndex].editMode = false;
                                vm.groupedItemsPurchaseOrder[itemIndex].showEditButton = true;
                            });
                        }, function (error) {
                        }
                    )
                }
            }

            function nextPage() {
                pageable.page++;
                loadPurchaseAndPurchaseItems();
            }

            function previousPage() {
                pageable.page--;
                loadPurchaseAndPurchaseItems();
            }

            function validatePurchaseOrder() {
                var valid = false;
                if (vm.purchaseOrder.supplier == null || vm.purchaseOrder.supplier == undefined || vm.purchaseOrder.supplier == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Supplier");
                } else if (vm.purchaseOrder.poDate == null || vm.purchaseOrder.poDate == undefined || vm.purchaseOrder.poDate == "") {
                    valid = false;
                    $rootScope.showWarningMessage("Please select PO Date");
                } else {
                    if (vm.groupedItemsPurchaseOrder.length > 0) {
                        angular.forEach(vm.groupedItemsPurchaseOrder, function (grpItem) {
                            if (grpItem.rowType == 'item') {
                                grpItem.quantity = grpItem.purchaseItemQuantity;
                                grpItem.notes = grpItem.purchaseItemNotesObject;
                                vm.purchaseOrder.customPurchaseOrdersItems.push(grpItem);
                            }
                        });
                        valid = true;
                    } else {
                        $rootScope.showWarningMessage("Please add atleast one item to Purchase Order");
                        valid = false;
                    }
                }
                return valid;
            }

            (function () {
                $scope.$on('app.po.addItems', addItems);
                $scope.$on('app.po.update', update);
                $scope.$on('app.po.items.nextPageDetails', nextPage);
                $scope.$on('app.po.items.previousPageDetails', previousPage);
                $scope.$on('app.po.itemsTabActivated', function (event, data) {
                    if (data.tabId == 'details.items') {
                        loadPurchaseAndPurchaseItems();
                    }
                });
            })();
        }
    }
);
