define(
    [
        'app/desktop/modules/customer/customer.module',
        'app/shared/services/core/customerSupplierService'
    ],
    function (module) {
        module.controller('CustomerProblemReportController', CustomerProblemReportController);

        function CustomerProblemReportController($scope, $rootScope, $timeout, $state, $stateParams, CustomerSupplierService, $q, $translate, $application) {
            var vm = this;
            vm.loading = true;
            vm.customerId = $stateParams.customerId;

            function loadCustomerProblemReports() {
                CustomerSupplierService.getAllCustomerProblemReports(vm.customerId).then(
                    function (data) {
                        vm.problemReports = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        vm.loading = false;
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showProblemReport = showProblemReport;
            function showProblemReport(problemReport) {
                $state.go("app.pqm.pr.details", {problemReportId: problemReport.id, tab: 'details.basic'});
            };

            (function () {
                $scope.$on('app.customer.tabActivated', function (event, data) {
                    if (data.tabId == 'details.problemReport') {
                        loadCustomerProblemReports();
                    }
                });
            })();
        }
    }
);

