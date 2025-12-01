define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('EquipmentTimeLineController', EquipmentTimeLineController);

        function EquipmentTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate) {
            var vm = this;
            vm.equipmentId = $stateParams.equipmentId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.equipment.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);