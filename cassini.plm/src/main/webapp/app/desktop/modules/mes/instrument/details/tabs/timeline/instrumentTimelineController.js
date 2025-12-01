define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('InstrumentTimeLineController', InstrumentTimeLineController);

        function InstrumentTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                              $translate) {
            var vm = this;
            vm.instrumentId = $stateParams.instrumentId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.instrument.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);