define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/desktop/modules/directives/files/objectFilesDirectiveController'
    ],
    function (module) {
        module.controller('SupplierAuditFilesController', SupplierAuditFilesController);

        function SupplierAuditFilesController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application) {
            var vm = this;
            vm.supplierAuditId = $stateParams.supplierAuditId;

            (function () {
                $scope.$on('app.supplierAudit.tabActivated', function (event, data) {
                    if (data.tabId == 'details.files') {
                        $timeout(function () {
                            $scope.$broadcast('app.objectFile.tabActivated', {});
                        }, 500);
                    }
                });
            })();
        }
    }
);

