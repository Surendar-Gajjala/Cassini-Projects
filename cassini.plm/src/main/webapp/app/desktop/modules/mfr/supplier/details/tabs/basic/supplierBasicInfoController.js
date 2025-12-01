define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/shared/services/core/supplierService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('SupplierBasicInfoController', SupplierBasicInfoController);

        function SupplierBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, CommonService,
                                             $translate, SupplierService, LoginService) {
            var vm = this;
            vm.loading = true;
            vm.supplierId = $stateParams.supplierId;
            vm.supplier = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateSupplier = updateSupplier;
            $rootScope.loadSupplierBasicDetails = loadSupplierBasicDetails;
            function loadSupplierBasicDetails() {
                vm.loading = true;
                if (vm.supplierId != null && vm.supplierId != undefined) {
                    SupplierService.getSupplier(vm.supplierId).then(
                        function (data) {
                            vm.supplier = data;
                            $rootScope.supplier = data;
                            $scope.name = vm.supplier.name;
                            $scope.email = vm.supplier.email;
                            CommonService.getPersonReferences([vm.supplier], 'createdBy');
                            CommonService.getPersonReferences([vm.supplier], 'modifiedBy');

                            if (vm.supplier.description != null && vm.supplier.description != undefined) {
                                vm.supplier.descriptionHtml = $sce.trustAsHtml(vm.supplier.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);

                            vm.loading = false;
                            $rootScope.viewInfo.title = $translate.instant("SUPPLIER_DETAILS");
                            $rootScope.viewInfo.description = "Name: " + data.name;
                            $scope.$evalAsync();
                        }, function (error) {
                              $rootScope.showErrorMessage(error.message);
                              $rootScope.hideBusyIndicator();
                         }
                    )
                }
                vm.loading = false;
            }

            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var enterValidEmail = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var updatedSuccessMsg = parsed.html($translate.instant("SUPPLIER_UPDATED_SUCCESS_MESSAGE")).html();
            var supplierDetails = parsed.html($translate.instant("SUPPLIER_DETAILS")).html();
            var emailValidation = parsed.html($translate.instant("EMAIL_VALIDATION")).html();
            function validateEmail(email) {
                var valid = true;
                var atpos = email.indexOf("@");
                var dotpos = email.lastIndexOf(".");
                if (email != null && email != undefined && email != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        valid = false;
                    }
                }
                return valid
            }

            function validateSupplier() {
                var valid = true;
                if (vm.supplier.name == null || vm.supplier.name == ""
                    || vm.supplier.name == undefined) {
                    valid = false;
                    vm.supplier.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                } else if (vm.supplier.email == null || vm.supplier.email == "" || vm.supplier.email == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(emailValidation);
                } else if (vm.supplier.email != null && !validateEmail(vm.supplier.email)) {
                    valid = false;
                    vm.editEmail = true;
                    vm.supplier.email = vm.supplier.email;
                    $rootScope.showWarningMessage(enterValidEmail);
                }

                return valid;
            }

            function updateSupplier() {
                if (validateSupplier()) {
                    SupplierService.updateSupplier(vm.supplier).then(
                        function (data) {
                            vm.supplier = data;
                            $scope.name = data.name;
                            $scope.email = vm.supplier.email;
                            vm.editEmail = false;
                            if (vm.supplier.description != null && vm.supplier.description != undefined) {
                                vm.supplier.descriptionHtml = $sce.trustAsHtml(vm.supplier.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            CommonService.getPersonReferences([vm.supplier], 'createdBy');
                            CommonService.getPersonReferences([vm.supplier], 'modifiedBy');
                            // loadSupplierBasicDetails();
                            $rootScope.viewInfo.description = vm.supplier.name;
                            $rootScope.showSuccessMessage(updatedSuccessMsg);

                        }, function (error) {
                            loadSupplierBasicDetails();
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.ediEmail = false;
            vm.supplierChangeEmail = supplierChangeEmail;
            function supplierChangeEmail() {
                vm.editEmail = true;
            }

            vm.cancelSupplier = cancelSupplier;
            function cancelSupplier() {
                vm.editEmail = false;
                vm.supplier.email = $scope.email;
            }


            (function () {
                $scope.$on('app.supplier.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadSupplierBasicDetails();
                    }
                });

            })();

        }
    }
);