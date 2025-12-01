define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mesObjectTypeService'

    ],
    function (module) {
        module.controller('WorkCenterResourcesController', WorkCenterResourcesController);

        function WorkCenterResourcesController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $cookies, $window,
                                               MESObjectTypeService) {
            var vm = this;
            vm.loading = true;


            (function () {
                $scope.$on('app.workcenter.tabActivated', function (event, data) {
                    if (data.tabId == 'details.whereUsed') {

                    }
                });
            })();
        }
    }
);