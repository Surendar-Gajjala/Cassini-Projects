/**
 * Created by SRAVAN on 9/19/2018.
 */
define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/store/requisitionService'

    ],
    function (module) {
        module.controller('PurchaseOrderRequestsController', PurchaseOrderRequestsController);

        function PurchaseOrderRequestsController($scope, $rootScope, $timeout, $stateParams, $cookies, RequisitionService) {

            var vm = this;
            vm.loading = false;
            vm.select = select;
            vm.checkAll = checkAll;
            vm.selectedAll = false;
            vm.selectedRequests = [];
            vm.projectRequisitionChanged = projectRequisitionChanged;
            vm.addToPurchaseItems = addToPurchaseItems;
            var requisitionMap = new Hashtable;

            function addToPurchaseItems(requisition, requisitionItem) {
                requisitionItem.id = null;
                requisitionItem.isNew = true;
                requisitionItem.editMode = true;
                requisitionItem.showEditButton = false;
                $scope.callback(requisition, requisitionItem, requisitionMap);
            }

            function projectRequisitionChanged(requisition) {
                vm.requisitionObject = null;
                vm.requisitionObject = requisition;
            }

            function checkAll() {
                if (vm.selectedAll) {
                    vm.selectedAll = true;
                } else {
                    vm.selectedAll = false;
                }
                angular.forEach(vm.projectRequisitions, function (request) {
                    request.selected = vm.selectedAll;
                    vm.selectedRequests.push(request);
                })
            }

            function select(item) {
                var flag = true;
                if (item.selected == false) {
                    vm.selectedAll = false;
                    var index = vm.selectedRequests.indexOf(item);
                    vm.selectedRequests.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedRequests, function (selectedItem) {
                        if (item.id == selectedItem.id) {
                            flag = false;
                            var index = vm.selectedRequests.indexOf(item);
                            vm.selectedRequests.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedRequests.push(item);
                    }
                }
            }

            function loadRequisitions() {
                RequisitionService.getAllRequisitions($rootScope.storeId).then(
                    function (data) {
                        vm.projectRequisitions = data;
                        angular.forEach(vm.projectRequisitions, function (projectRequisition) {
                            var reqItems = $scope.data.itemsMap.get(projectRequisition.requisitionNumber);
                            if (reqItems != null) {
                                angular.forEach(reqItems, function (reqItem) {
                                    var reqItemIndex = projectRequisition.customRequisitionItems.findIndex(item = > item.materialItem.id == reqItem.materialItem.id
                                    )
                                    ;
                                    if (reqItemIndex != -1) {
                                        projectRequisition.customRequisitionItems.splice(reqItemIndex, 1);
                                    }
                                });
                            }
                            requisitionMap.put(projectRequisition.requisitionNumber, projectRequisition);
                        });

                    }
                )
            }

            function addRequisitions() {
                if (vm.selectedRequests.length > 0) {
                    $scope.callback(vm.selectedRequests);
                } else if (vm.selectedRequests.length == 0) {
                    $rootScope.showErrorMessage("Please add atleast one Requisition");
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadRequisitions();
                    $rootScope.$on('app.purchaseOrder.requisition', addRequisitions);
                }
            })();
        }
    }
);