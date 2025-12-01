define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('MaterialTimeLineController', MaterialTimeLineController);

        function MaterialTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                            $translate) {
            var vm = this;
            vm.materialId = $stateParams.materialId;

            (function () {
                $scope.$on('app.material.tabActivated', function (event, args) {
                    if (args.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);