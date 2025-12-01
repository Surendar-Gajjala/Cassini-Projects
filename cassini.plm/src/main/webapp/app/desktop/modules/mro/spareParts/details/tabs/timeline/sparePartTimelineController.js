define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('SparePartTimeLineController', SparePartTimeLineController);

        function SparePartTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate) {
            var vm = this;
            vm.sparePartId = $stateParams.sparePartId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.sparePart.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);