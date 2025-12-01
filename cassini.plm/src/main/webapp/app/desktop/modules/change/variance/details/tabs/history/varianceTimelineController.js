define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('VarianceTimeLineController', VarianceTimeLineController);

        function VarianceTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate) {
            var vm = this;

            vm.varianceId = $stateParams.varianceId;

            var parsed = angular.element("<div></div>");


            (function () {
                    $scope.$on('app.variance.tabactivated', function (event, args) {
                        if (args.tabId == 'details.timeLine') {
                            $scope.$broadcast('app.object.timeline', {});
                        }
                    });
            })();
        }
    }
);