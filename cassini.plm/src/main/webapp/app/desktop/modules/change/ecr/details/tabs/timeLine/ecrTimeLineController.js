define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('ECRTimeLineController', ECRTimeLineController);

        function ECRTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate) {
            var vm = this;

            vm.ecrId = $stateParams.ecrId;

            var parsed = angular.element("<div></div>");


            (function () {
                //if ($application.homeLoaded == true) {
                    $scope.$on('app.ecr.tabActivated', function (event, args) {
                        if (args.tabId == 'details.timeLine') {
                            $scope.$broadcast('app.object.timeline', {});
                        }
                    });
                //}
            })();
        }
    }
);