define(
    [
        'app/desktop/modules/npr/npr.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('NprTimeLineController', NprTimeLineController);

        function NprTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate) {
            var vm = this;
            vm.nprId = $stateParams.nprId;

            var parsed = angular.element("<div></div>");

            (function () {
                $scope.$on('app.npr.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);