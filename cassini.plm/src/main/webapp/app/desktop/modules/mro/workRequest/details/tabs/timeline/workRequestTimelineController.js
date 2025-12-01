define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('WorkRequestTimeLineController', WorkRequestTimeLineController);

        function WorkRequestTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate) {
            var vm = this;
            vm.workRequestId = $stateParams.workRequestId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.workRequest.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);