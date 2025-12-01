define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('MBOMInstanceOperationTimeLineController', MBOMInstanceOperationTimeLineController);

        function MBOMInstanceOperationTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                           $translate) {
            var vm = this;
            vm.operationId = $stateParams.operationId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.mbomInstance.operation.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timeline') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);