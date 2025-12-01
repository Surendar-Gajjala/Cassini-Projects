define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('ProductionOrderTimeLineController', ProductionOrderTimeLineController);

        function ProductionOrderTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                         $translate) {
            var vm = this;

            vm.productionOrderId = $stateParams.productionOrderId;

            var parsed = angular.element("<div></div>");


            (function () {
                //if ($application.homeLoaded == true) {
                $scope.$on('app.productionOrder.tabActivated', function (event, args) {
                    if (args.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });
                //}
            })();
        }
    }
);