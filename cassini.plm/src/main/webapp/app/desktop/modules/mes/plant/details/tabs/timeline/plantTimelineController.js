define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('PlantTimeLineController', PlantTimeLineController);

        function PlantTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                         $translate) {
            var vm = this;

            vm.plantId = $stateParams.plantId;

            var parsed = angular.element("<div></div>");


            (function () {
                $scope.$on('app.plant.tabActivated', function (event, args) {
                    if (args.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });
            })();
        }
    }
);