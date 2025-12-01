define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mesObjectTypeService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MaintenanceAndRepairBasicInfoController', MaintenanceAndRepairBasicInfoController);

        function MaintenanceAndRepairBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                        $translate, MESObjectTypeService) {
            var vm = this;
            vm.loading = true;



            (function () {
                $scope.$on('app.maintenanceAndRepair.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        //loadBasicPlant();
                    }
                });

            })();

        }
    }
);