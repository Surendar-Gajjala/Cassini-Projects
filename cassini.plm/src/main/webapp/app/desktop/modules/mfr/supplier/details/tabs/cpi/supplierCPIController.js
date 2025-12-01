define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/shared/services/core/supplierService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService'
    ],
    function (module) {
        module.controller('SupplierCPIController', SupplierCPIController);

        function SupplierCPIController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, DialogService,
                                       $translate, SupplierService, CustomObjectService, CommonService, CustomObjectTypeService) {
            var vm = this;

            vm.loading = true;
            vm.supplierId = $stateParams.supplierId;
            vm.customObjects = [];
            vm.showObjectDetails = showObjectDetails;
            var type = "";

            function loadCPIs() {
                vm.loading = true;
                CustomObjectTypeService.getCustomObjectByName("CPI Form").then(
                    function (data) {
                        if (data.customObjectType != null && data.customObjectType != "") {
                            vm.typeId = data.customObjectType.id;
                            CustomObjectService.getCustomObjectsByTypeAndSupplier(vm.typeId, vm.supplierId).then(
                                function (data) {
                                    vm.customObjects = data;
                                    CommonService.getMultiplePersonReferences(vm.customObjects, ['createdBy', 'modifiedBy']);
                                    vm.loading = false;
                                }
                            );
                        } else {
                            vm.loading = false;
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function showObjectDetails(object) {
                $window.localStorage.setItem("shared-permission", $rootScope.sharedPermission);
                $state.go('app.customobjects.details', {
                    customId: object.id,
                    permission: $rootScope.sharedPermission,
                    tab: 'details.basic'
                });
            }

            (function () {
                $scope.$on('app.supplier.tabActivated', function (event, data) {
                    if (data.tabId == 'details.cpi') {
                        loadCPIs();
                    }
                });
            })();
        }
    }
);