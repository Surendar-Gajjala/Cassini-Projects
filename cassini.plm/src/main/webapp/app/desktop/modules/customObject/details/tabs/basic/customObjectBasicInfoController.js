define(
    [
        'app/desktop/modules/customObject/customObject.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/shared/services/core/supplierService',
        'app/shared/services/core/workflowService'
    ],
    function (module) {
        module.controller('CustomObjectBasicInfoController', CustomObjectBasicInfoController);

        function CustomObjectBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, $translate, CommonService, WorkflowService, CustomObjectService, SupplierService) {
            var vm = this;

            vm.loading = true;
            vm.customId = $stateParams.customId;
            vm.customObject = null;

            var parsed = angular.element("<div></div>");

            var customObjectUpdateMsg = parsed.html($translate.instant("CUSTOM_OBJECT_UPDATE_MSG")).html();
            vm.clickToUpdatePerson = parsed.html($translate.instant("CLICK_TO_UPDATE_PERSON")).html();
            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            var descriptionValidation = parsed.html($translate.instant("DESCRIPTION_NOT_EMPTY")).html();
            vm.suppliers = [];
            function loadSuppliers() {
                vm.suppliers = [];
                SupplierService.getApprovedSuppliers().then(
                    function (data) {
                        vm.suppliers = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function getSupplierById() {
                SupplierService.getSupplier(vm.customObject.supplier).then(
                    function (data) {
                        vm.customObject.supplierObject = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.updateObject = updateObject;
            function updateObject() {
                if (validate()) {
                    if (vm.customObject.supplier != null) {
                        vm.customObject.supplier = vm.customObject.supplierObject.id;
                    }
                    CustomObjectService.updateCustomObject(vm.customId, vm.customObject).then(
                        function (data) {
                            vm.customObject = data;
                            if (vm.customObject.supplier != null) getSupplierById(vm.customObject.supplier)
                            CommonService.getMultiplePersonReferences([vm.customObject], ['createdBy', 'modifiedBy']);
                            $rootScope.showSuccessMessage(customObjectUpdateMsg)
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    loadCustomObject();
                }

            }

            function validate() {
                var valid = true;

                if (vm.customObject.name == null || vm.customObject.name == undefined ||
                    vm.customObject.name == "") {
                    $rootScope.showWarningMessage(nameValidation);
                    valid = false;
                }
                else if (vm.customObject.description == null || vm.customObject.description == undefined ||
                    vm.customObject.description == "") {
                    $rootScope.showWarningMessage(descriptionValidation);
                    valid = false;
                }
                return valid;
            }

            $rootScope.loadCustomObject = loadCustomObject;
            function loadCustomObject() {
                WorkflowService.getCustomObjectWorkflowStatus(vm.customId).then(
                    function (data) {
                        vm.customObject = data;
                        $rootScope.customObject = data;
                        if (vm.customObject.type.name == 'Supplier Performance Rating' || vm.customObject.type.name == 'CPI Form' || vm.customObject.type.name == '4MChange-Supplier') loadSuppliers();
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 500);
                        vm.loading = false;
                        if (vm.customObject.supplier != null) getSupplierById(vm.customObject.supplier);
                        CommonService.getMultiplePersonReferences([vm.customObject], ['createdBy', 'modifiedBy']);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $scope.$on('app.customObj.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadCustomObject();
                    }
                });
            })();
        }
    }
);