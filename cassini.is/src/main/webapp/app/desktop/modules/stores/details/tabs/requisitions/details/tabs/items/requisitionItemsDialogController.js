/**
 * Created by swapna on 27/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/requisitionService',
        'app/shared/services/core/itemService'

    ],
    function (module) {
        module.controller('RequisitionItemsController', RequisitionItemsController);

        function RequisitionItemsController($scope, $rootScope, $window, $state, $stateParams, RequisitionService, ItemService) {
            var vm = this;

            vm.itemList = [];
            var requisitionItems = $scope.data.requisitionItems;
            var project = $scope.data.project;
            vm.addToRequisitionItems = addToRequisitionItems;

            function addToRequisitionItems(item) {
                var index = vm.itemList.indexOf(item);
                vm.itemList.splice(index, 1);
                $scope.callback(item);
            }

            function loadProjectItems() {
                vm.loading = true;
                RequisitionService.findNonRequisitionItems($rootScope.storeId, $stateParams.requisitionId, project.id).then(
                    function (data) {
                        vm.itemList = data;
                        vm.loading = false;
                        //loadUnAssignedProjectItems();
                    })
            }

            function loadUnAssignedProjectItems() {
                RequisitionService.getRequisitionItems($rootScope.storeId, $stateParams.requisitionId).then(
                    function (data) {
                        requisitionItems = data;
                        angular.forEach(requisitionItems, function (reqItem) {
                            angular.forEach(vm.itemList, function (projectItem) {
                                if (reqItem.materialItem.id == projectItem.id) {
                                    var index = vm.itemList.indexOf(projectItem);
                                    vm.itemList.splice(index, 1);
                                }
                            })
                        });
                        vm.loading = false;
                    }
                )
            }

            function back() {
                $window.history.back();
            }

            (function () {
                loadProjectItems();
            })();
        }
    }
)
;