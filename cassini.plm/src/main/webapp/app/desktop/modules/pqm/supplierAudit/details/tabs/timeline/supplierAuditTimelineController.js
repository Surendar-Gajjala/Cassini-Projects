define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('SupplierAuditTimelineController', SupplierAuditTimelineController);

        function SupplierAuditTimelineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                                 $translate) {
            var vm = this;

            vm.supplierAuditId = $stateParams.supplierAuditId;

            var parsed = angular.element("<div></div>");


            (function () {
                $scope.$on('app.supplierAudit.tabActivated', function (event, args) {
                    if (args.tabId == 'details.timeline') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });
            })();
        }
    }
);