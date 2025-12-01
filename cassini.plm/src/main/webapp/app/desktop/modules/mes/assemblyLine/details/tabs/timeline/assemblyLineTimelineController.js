define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('AssemblyLineTimeLineController', AssemblyLineTimeLineController);

        function AssemblyLineTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                $translate) {
            var vm = this;
            vm.assemblyLineId = $stateParams.assemblyLineId;

            (function () {
                $scope.$on('app.assemblyLine.tabActivated', function (event, args) {
                    if (args.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);