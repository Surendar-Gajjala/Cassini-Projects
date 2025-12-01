define(
    [
        'app/desktop/modules/customer/customer.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('CustomerTimeLineController', CustomerTimeLineController);

        function CustomerTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                            $translate) {
            var vm = this;
            vm.customerId = $stateParams.customerId;

            var parsed = angular.element("<div></div>");

            (function () {
                $scope.$on('app.customer.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);