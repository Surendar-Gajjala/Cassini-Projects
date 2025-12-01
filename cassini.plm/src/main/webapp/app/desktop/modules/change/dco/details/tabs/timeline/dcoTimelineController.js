define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('DCOTimeLineController', DCOTimeLineController);

        function DCOTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate) {
            var vm = this;

            vm.dcoId = $stateParams.dcoId;

            var parsed = angular.element("<div></div>");


            (function () {
                //if ($application.homeLoaded == true) {
                    $scope.$on('app.dco.tabActivated', function (event, args) {
                        if (args.tabId == 'details.timeLine') {
                            $scope.$broadcast('app.object.timeline', {});
                        }
                    });
                //}
            })();
        }
    }
);