define(
    [
        'app/desktop/modules/customer/customer.module',
        'app/shared/services/core/customerSupplierService'
    ],
    function (module) {
        module.controller('CustomersController', CustomersController);

        function CustomersController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                     $state, $stateParams, $cookies, $uibModal, CustomerSupplierService, CommonService, DialogService) {

            $rootScope.viewInfo.icon = "fa fa-th";
            $rootScope.viewInfo.title = $translate.instant('ITEMS_ALL_TITLE');
            $rootScope.viewInfo.showDetails = false;

            var parsed = angular.element("<div></div>");
            $scope.createCustomerTitle = parsed.html($translate.instant("CREATE_NEW_CUSTOMER")).html();
            var newCustomerTitle = parsed.html($translate.instant("NEW_CUSTOMER")).html();
            var editCustomerTitle = parsed.html($translate.instant("UPDATE_CUSTOMER")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();
            var updateButton = parsed.html($translate.instant("UPDATE")).html();
            var deleteDialogTitle = parsed.html($translate.instant("DELETE_CUSTOMER")).html();
            var deleteCustomerDialogMessage = parsed.html($translate.instant("DELETE_CUSTOMER_DIALOG_MESSAGE")).html();
            var customerDeletedMessage = parsed.html($translate.instant("CUSTOMER_DELETED_MESSAGE")).html();
            $scope.cannotDeleteUsedCustomer = parsed.html($translate.instant("CANNOT_DELETE_USED_CUSTOMER")).html();

            var vm = this;

            vm.newCustomer = newCustomer;
            vm.showCustomer = showCustomer;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.searchText = null;
            vm.filterSearch = null;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.filters = {
                searchQuery: null
            };

            vm.customers = angular.copy(pagedResults);

            function newCustomer() {
                var options = {
                    title: newCustomerTitle,
                    template: 'app/desktop/modules/customer/new/newCustomerView.jsp',
                    controller: 'NewCustomerController as newCustomerVm',
                    resolve: 'app/desktop/modules/customer/new/newCustomerController',
                    width: 600,
                    showMask: true,
                    data: {
                        mode: "NEW",
                        customerDetails: null
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.customers.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadAllCustomers();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.editCustomer = editCustomer;
            function editCustomer(customer) {
                var options = {
                    title: editCustomerTitle,
                    template: 'app/desktop/modules/customer/new/newCustomerView.jsp',
                    controller: 'NewCustomerController as newCustomerVm',
                    resolve: 'app/desktop/modules/customer/new/newCustomerController',
                    width: 600,
                    showMask: true,
                    data: {
                        mode: "EDIT",
                        customerDetails: customer
                    },
                    buttons: [
                        {text: updateButton, broadcast: 'app.customers.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadAllCustomers();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadAllCustomers() {
                vm.loading = true;
                CustomerSupplierService.getAllCustomers(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.customers = data;
                        CommonService.getPersonReferences(vm.customers.content, 'contactPerson');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.deleteCustomer = deleteCustomer;
            function deleteCustomer(customer) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteCustomerDialogMessage + " [" + customer.name + "] ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        CustomerSupplierService.deleteCustomer(customer.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(customerDeletedMessage);
                                loadAllCustomers();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }


            function nextPage() {
                if (vm.customers.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadAllCustomers();
                }
            }

            function previousPage() {
                if (vm.customers.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadAllCustomers();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadAllCustomers();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadAllCustomers();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.customers = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadAllCustomers();
            }

            function showCustomer(customer){
                $state.go('app.customers.details', {customerId: customer.id, tab: 'details.basic'});
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadAllCustomers();
                });
            })();
        }
    }
);