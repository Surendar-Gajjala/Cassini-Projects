define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('SupplierTimeLineController', SupplierTimeLineController);

        function SupplierTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                         $translate) {
            var vm = this;

            vm.supplierId = $stateParams.supplierId;

            var parsed = angular.element("<div></div>");


            (function () {
                $scope.$on('app.supplier.tabActivated', function (event, args) {
                    if (args.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });
            })();
        }
    }
);