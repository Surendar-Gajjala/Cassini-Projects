define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('DCRTimeLineController', DCRTimeLineController);

        function DCRTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application) {
            var vm = this;

            vm.dcrId = $stateParams.dcrId;

            var parsed = angular.element("<div></div>");

            (function () {
                //if ($application.homeLoaded == true) {
                    $scope.$on('app.dcr.tabActivated', function (event, args) {
                        if (args.tabId == 'details.timeLine') {
                            $scope.$broadcast('app.object.timeline', {});
                        }
                    });
                //}
            })();
        }
    }
);