define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('MaintenancePlanTimeLineController', MaintenancePlanTimeLineController);

        function MaintenancePlanTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                   $translate) {
            var vm = this;
            vm.maintenancePlanId = $stateParams.maintenancePlanId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.maintenancePlan.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);