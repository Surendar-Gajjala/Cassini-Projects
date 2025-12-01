define(['app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/store/topStockMovementService'
    ],
    function (module) {
        module.controller('MaterialStockMovementController', MaterialStockMovementController);

        function MaterialStockMovementController($scope, $rootScope, $timeout, $state, $cookies, $stateParams, ItemService, CommonService,
                                                 ProjectService, TopStockMovementService) {
            var vm = this;
            vm.materialId = $stateParams.materialId;
            vm.material = null;
            vm.itemInventoryHistory = [];

            function loadMaterial() {
                vm.loading = true;
                $rootScope.showBusyIndicator();
                ItemService.getMaterialItem(vm.materialId).then(
                    function (data) {
                        vm.material = data;
                        vm.loading = false;
                        CommonService.getPersonReferences([vm.material], 'createdBy');
                        CommonService.getPersonReferences([vm.material], 'modifiedBy');
                        loadStockMovement();
                    }
                )
            }

            function loadStockMovement() {
                TopStockMovementService.getStockMovementByItem(vm.material.id).then(
                    function (data) {
                        vm.itemStockMovement = data;
                        ProjectService.getProjectReferences(vm.itemStockMovement, 'project');
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $scope.$on('app.material.tabactivated', function (event, data) {
                    loadMaterial();
                });
            })();
        }
    }
);