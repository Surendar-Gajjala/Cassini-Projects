define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('MeterTimeLineController', MeterTimeLineController);

        function MeterTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate) {
            var vm = this;
            vm.meterId = $stateParams.meterId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.meter.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);