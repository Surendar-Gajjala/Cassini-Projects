define(
    [
        'app/desktop/modules/customer/customer.module',
        'app/shared/services/core/customerSupplierService'
    ],
    function (module) {
        module.controller('CustomerBasicInfoController', CustomerBasicInfoController);

        function CustomerBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, CustomerSupplierService, CommonService, $translate) {
            var vm = this;
            vm.loading = true;
            vm.customerId = $stateParams.customerId;
            vm.customer = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateCustomer = updateCustomer;

            function loadCustomerBasicDetails() {
                vm.loading = true;
                if (vm.customerId != null && vm.customerId != undefined) {
                    CustomerSupplierService.getCustomer(vm.customerId).then(
                        function (data) {
                            vm.customer = data;
                            $rootScope.customer = vm.customer;
                            $scope.name = vm.customer.name;
                            CommonService.getMultiplePersonReferences([vm.customer], ['createdBy', 'modifiedBy']);
                            if (vm.customer.description != null && vm.customer.description != undefined) {
                                vm.customer.descriptionHtml = $sce.trustAsHtml(vm.customer.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            vm.loading = false;
                            vm.editStatus = false;
                            $rootScope.viewInfo.title = parsed.html($translate.instant("CUSTOMER_DETAILS")).html();
                            $rootScope.viewInfo.description = vm.customer.name;
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var customerUpdatedSuccess = parsed.html($translate.instant("CUSTOMER_UPDATED_SUCCESS")).html();


            function validateCustomer() {
                var valid = true;
                if (vm.customer.name == null || vm.customer.name == ""
                    || vm.customer.name == undefined) {
                    valid = false;
                    vm.customer.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }
                return valid;
            }

            function updateCustomer() {
                if (validateCustomer()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    CustomerSupplierService.updateCustomer(vm.customer).then(
                        function (data) {
                            loadCustomerBasicDetails();
                            $rootScope.showSuccessMessage(customerUpdatedSuccess);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                $scope.$on('app.customer.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadCustomerBasicDetails();
                    }
                });

            })();

        }
    }
);