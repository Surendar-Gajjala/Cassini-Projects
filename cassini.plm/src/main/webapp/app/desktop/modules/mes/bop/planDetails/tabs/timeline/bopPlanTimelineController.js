define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('BOPPlanTimeLineController', BOPPlanTimeLineController);

        function BOPPlanTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                           $translate) {
            var vm = this;
            vm.bopPlanId = $stateParams.bopPlanId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.bop.plan.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timeline') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);