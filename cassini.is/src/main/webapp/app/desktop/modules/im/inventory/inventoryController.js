define(
    [
        'app/desktop/modules/im/im.module',
        'app/shared/services/core/inventoryService',
        'app/shared/services/pm/project/bomService'
    ],
    function (module) {
        module.controller('InventoryController', InventoryController);

        function InventoryController($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, InventoryService, BomService) {

            $rootScope.viewInfo.icon = "glyphicon glyphicon-equalizer";
            $rootScope.viewInfo.title = "Inventory";

            var vm = this;

            vm.loading = true;
            vm.mode = $stateParams.mode;

            vm.previousPage = previousPage;
            vm.nextPage = nextPage;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.inventories = [];

            function loadInventories() {
                vm.loading = true;
                InventoryService.getAllCustomInventories($stateParams.projectId).then(
                    function (data) {
                        vm.loading = false;
                        vm.inventories = data;
                        angular.forEach(vm.inventories, function (inv) {
                            BomService.getBoqItemByProjectAndItemNumber($stateParams.projectId, inv.itemNumber).then(
                                function (boqs) {
                                    inv.boqQuantity = 0;
                                    inv.boqQuantity = boq.quantity;
                                }
                            )
                        })
                    });
            }

            function nextPage() {
                vm.pageable.page++;
                loadInventories();
            }

            function previousPage() {
                vm.pageable.page--;
                loadInventories();
            }

            (function () {
                loadInventories();
            })();
        }
    }
);