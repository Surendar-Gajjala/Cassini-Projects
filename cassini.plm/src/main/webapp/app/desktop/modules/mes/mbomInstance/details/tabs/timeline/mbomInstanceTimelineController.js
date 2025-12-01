define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('MBOMInstanceTimeLineController', MBOMInstanceTimeLineController);

        function MBOMInstanceTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                $translate) {
            var vm = this;
            vm.mbomInstanceId = $stateParams.mbomInstanceId;

            var parsed = angular.element("<div></div>");

            (function () {
                $scope.$on('app.mbomInstance.tabActivated', function (event, args) {
                    if (args.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });
            })();
        }
    }
);