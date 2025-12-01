/**
 * Created by swapna on 19/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/requisitionService',
        'app/shared/services/store/topStoreService'

    ],
    function (module) {
        module.controller('RequisitionItemsController', RequisitionItemsController);

        function RequisitionItemsController($scope, $rootScope, $window, $state, $stateParams, RequisitionService, TopStoreService) {
            var vm = this;

            vm.requisition = null;
            vm.back = back;
            var reqItemsMap = new Hashtable();
            $rootScope.storeRequisitionItemsList = [];
            vm.removeFromItemList = removeFromItemList;
            vm.showItemDetails = showItemDetails;
            vm.isNew = false;

            $rootScope.hasNewItems = false;

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

            $rootScope.storeRequisitionItemsList = angular.copy(pagedResults);

            function loadRequisitionItems() {
                RequisitionService.getPagedRequisitionItems($rootScope.storeId, $stateParams.requisitionId, pageable).then(
                    function (data) {
                        $rootScope.storeRequisitionItemsList = data;
                    }
                )
            }

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'REQ'});
            }

            function addItems() {
                var options = {
                    title: 'Requisition Items',
                    showMask: true,
                    template: 'app/desktop/modules/stores/details/tabs/requisitions/details/tabs/items/requisitionItemsDialogView.jsp',
                    controller: 'RequisitionItemsController as reqItemsVm',
                    resolve: 'app/desktop/modules/stores/details/tabs/requisitions/details/tabs/items/requisitionItemsDialogController.js',
                    width: 700,
                    data: {
                        project: vm.requisition.project,
                        requisitionItems: $rootScope.storeRequisitionItemsList.content
                    },
                    callback: function (item) {
                        var newItem = {
                            materialItem: item,
                            notes: null,
                            quantity: null,
                            isNew: true
                        };
                        var item1 = reqItemsMap.get(item.id);
                        if (item1 == null) {
                            vm.isNew = true;
                            reqItemsMap.put(item.id, item);
                            $rootScope.storeRequisitionItemsList.content.splice(0, 0, newItem);
                            $rootScope.hasNewItems = true;
                        }
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function showItemDetails(item) {
                item.materialItem.requestedOn = new Date();
                item.materialItem.quantity = item.quantity;
                item.materialItem.type = "RequisitionItem";
                item.materialItem.timeStamp = item.materialItem.createdDate;
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

            function loadRequisition() {
                RequisitionService.getRequisition($rootScope.storeId, $stateParams.requisitionId).then(
                    function (data) {
                        vm.requisition = data;
                    }
                )
            }

            function update() {
                var valid = true;
                var stock = [];
                angular.forEach($rootScope.storeRequisitionItemsList.content, function (item) {
                    if (valid && item.quantity != undefined && item.quantity != null) {
                        if (valid && item.quantity > 0) {
                            if (valid && item.isNew == true) {
                                stock.push({
                                    materialItem: item.materialItem,
                                    notes: item.notes,
                                    quantity: item.quantity,
                                    requisition: $stateParams.requisitionId
                                })
                            }
                        }
                        else {
                            valid = false;
                            $rootScope.showErrorMessage("Qty should be +ve number");
                        }
                    }
                    else {
                        valid = false;
                        $rootScope.showErrorMessage("Please enter Qty");
                    }
                });
                if (stock.length > 0 && valid == true) {
                    vm.requisition.customRequisitionItems = stock;
                }
                if (valid) {
                    updateRequisition();
                }
            }

            function updateRequisition() {
                RequisitionService.updateRequisition($rootScope.storeId, vm.requisition).then(
                    function (data) {
                        vm.isNew = false;
                        vm.requisition.status = data.status;
                        vm.editMode = false;
                        loadRequisitionItems();
                        $rootScope.showSuccessMessage(vm.requisition.requisitionNumber + ": updated successfully");
                    },
                    function (error) {
                        $rootScope.showErrorMessage("Item already exist in this request");
                    }
                )
            }

            function removeFromItemList(item) {
                var index = $rootScope.storeRequisitionItemsList.content.indexOf(item);
                $rootScope.storeRequisitionItemsList.content.splice(index, 1);
                reqItemsMap.remove(item.materialItem.id);
                if (reqItemsMap.keys.length == 0) {
                    $rootScope.hasItems = false;
                }
            }

            function nextPage() {
                if ($rootScope.storeRequisitionItemsList.last != true) {
                    pageable.page++;
                    loadRequisitionItems();
                }
            }

            function previousPage() {
                if ($rootScope.storeRequisitionItemsList.first != true) {
                    pageable.page--;
                    loadRequisitionItems();
                }
            }

            (function () {
                loadRequisition();
                $rootScope.storeRequisitionItemsList = [];
                $scope.$on('app.request.addItems', addItems);
                $scope.$on('app.request.updateItems', update);
                $scope.$on('app.request.items.nextPageDetails', nextPage);
                $scope.$on('app.request.items.previousPageDetails', previousPage);
                if ($application.homeLoaded == true) {
                    $scope.$on('app.stock.requisitionItems', function (event, data) {
                        loadRequisitionItems();
                    });
                }
            })();
        }
    }
)
;