define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('ShiftTimeLineController', ShiftTimeLineController);

        function ShiftTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                         $translate) {
            var vm = this;
            vm.shiftId = $stateParams.shiftId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.shift.tabActivated', function (event, args) {
                    if (args.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);