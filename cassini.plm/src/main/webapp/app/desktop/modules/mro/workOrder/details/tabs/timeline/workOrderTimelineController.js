define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('WorkOrderTimeLineController', WorkOrderTimeLineController);

        function WorkOrderTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                             $translate) {
            var vm = this;
            vm.workOrderId = $stateParams.workOrderId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.workOrder.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);