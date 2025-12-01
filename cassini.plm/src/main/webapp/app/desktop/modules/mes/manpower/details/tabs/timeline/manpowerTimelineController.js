define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('ManpowerTimeLineController', ManpowerTimeLineController);

        function ManpowerTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                            $translate) {
            var vm = this;
            vm.manpowerId = $stateParams.manpowerId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.manpower.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);