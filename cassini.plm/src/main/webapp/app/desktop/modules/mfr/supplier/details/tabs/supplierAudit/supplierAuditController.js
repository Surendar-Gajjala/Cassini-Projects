define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/shared/services/core/supplierService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('SupplierAuditController', SupplierAuditController);

        function SupplierAuditController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, DialogService, $translate, SupplierService) {
            var vm = this;

            vm.loading = true;
            vm.supplierId = $stateParams.supplierId;
            vm.supplierAudits = [];
            vm.showSupplierAuditDetails = showSupplierAuditDetails;

            function loadSupplierAudits() {
                vm.loading = true;
                SupplierService.getSupplierAuditsPlans(vm.supplierId).then(
                    function (data) {
                        vm.supplierAudits = data;
                        vm.loading = false;
                    });
            }

            function showSupplierAuditDetails(supplierAudit) {
                $window.localStorage.setItem("shared-permission", $rootScope.sharedPermission);
                $state.go('app.pqm.supplierAudit.details', {
                    supplierAuditId: supplierAudit.supplierAudit,
                    tab: 'details.basic'
                });
            }

            vm.showSupplierPlanDetails = showSupplierPlanDetails;
            function showSupplierPlanDetails(plan) {
                var options = {
                    title: plan.name + " Details",
                    template: 'app/desktop/modules/pqm/supplierAudit/details/tabs/plan/supplierPlanDetailsView.jsp',
                    controller: 'SupplierPlanDetailsController as supplierPlanDetailsVm',
                    resolve: 'app/desktop/modules/pqm/supplierAudit/details/tabs/plan/supplierPlanDetailsController',
                    width: 700,
                    showMask: true,
                    data: {
                        supplierAuditPlan: plan
                    }
                };

                $rootScope.showSidePanel(options);
            }

            (function () {
                $scope.$on('app.supplier.tabActivated', function (event, data) {
                    if (data.tabId == 'details.supplierAudit') {
                        loadSupplierAudits();
                    }
                });
            })();
        }
    }
);