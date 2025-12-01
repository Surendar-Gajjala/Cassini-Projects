define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('JigsAndFixturesTimeLineController', JigsAndFixturesTimeLineController);

        function JigsAndFixturesTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                   $translate) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.jigsFixId = $stateParams.jigsFixId;


            (function () {

                $scope.$on('app.jigsAndFixtures.tabActivated', function (event, args) {
                    if (args.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);