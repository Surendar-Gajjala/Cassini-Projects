define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('SubstanceTimeLineController', SubstanceTimeLineController);

        function SubstanceTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                             $translate) {
            var vm = this;
            vm.substanceId = $stateParams.substanceId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.substance.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);