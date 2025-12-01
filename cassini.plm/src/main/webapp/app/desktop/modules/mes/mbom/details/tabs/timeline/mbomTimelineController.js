define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('MBOMTimeLineController', MBOMTimeLineController);

        function MBOMTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                        $translate) {
            var vm = this;
            vm.mbomId = $stateParams.mbomId;

            var parsed = angular.element("<div></div>");

            (function () {
                $scope.$on('app.mbom.tabActivated', function (event, args) {
                    if (args.tabId == 'details.timelineHistory') {
                        $('.tab-content .tab-pane').css("overflow", "auto");
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });
            })();
        }
    }
);