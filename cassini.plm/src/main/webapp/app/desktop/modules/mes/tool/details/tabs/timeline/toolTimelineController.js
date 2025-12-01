define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('ToolTimeLineController', ToolTimeLineController);

        function ToolTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                        $translate) {
            var vm = this;
            vm.toolId = $stateParams.toolId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.tool.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);