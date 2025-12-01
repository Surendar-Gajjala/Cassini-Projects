define(
    [
        'app/desktop/modules/supplier/supplier.module',
        'app/shared/services/core/customerSupplierService'
    ],
    function (module) {
        module.controller('SuppliersController', SuppliersController);

        function SuppliersController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                     $state, $stateParams, $cookies, $uibModal, CustomerSupplierService, CommonService, DialogService) {

            $rootScope.viewInfo.icon = "fa fa-th";
            $rootScope.viewInfo.title = $translate.instant('ITEMS_ALL_TITLE');
            $rootScope.viewInfo.showDetails = false;

            var parsed = angular.element("<div></div>");
            $scope.createSupplierTitle = parsed.html($translate.instant("CREATE_NEW_SUPPLIER")).html();
            var newSupplierTitle = parsed.html($translate.instant("NEW_SUPPLIER")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();
            var editSupplierTitle = parsed.html($translate.instant("UPDATE_SUPPLIER")).html();
            var updateButton = parsed.html($translate.instant("UPDATE")).html();
            var deleteDialogTitle = parsed.html($translate.instant("DELETE_SUPPLIER")).html();
            var deleteSupplierDialogMessage = parsed.html($translate.instant("DELETE_SUPPLIER_DIALOG_MESSAGE")).html();
            var supplierDeletedMessage = parsed.html($translate.instant("SUPPLIER_DELETED_MESSAGE")).html();


            var vm = this;

            vm.newSupplier = newSupplier;
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
                searchQuery: null,
                name: null,
                type: '',
                audit: ''
            };

            vm.suppliers = angular.copy(pagedResults);

            function newSupplier() {
                var options = {
                    title: newSupplierTitle,
                    template: 'app/desktop/modules/supplier/new/newSupplierView.jsp',
                    controller: 'NewSupplierController as newSupplierVm',
                    resolve: 'app/desktop/modules/supplier/new/newSupplierController',
                    width: 600,
                    showMask: true,
                    data: {
                        mode: "NEW",
                        supplierDetails: null
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.suppliers.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadAllSuppliers();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadAllSuppliers() {
                vm.loading = true;
                CustomerSupplierService.getAllSuppliers(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.suppliers = data;
                        CommonService.getPersonReferences(vm.suppliers.content, 'contactPerson');
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.editSupplier = editSupplier;
            function editSupplier(customer) {
                var options = {
                    title: editSupplierTitle,
                    template: 'app/desktop/modules/supplier/new/newSupplierView.jsp',
                    controller: 'NewSupplierController as newSupplierVm',
                    resolve: 'app/desktop/modules/supplier/new/newSupplierController',
                    width: 600,
                    showMask: true,
                    data: {
                        mode: "EDIT",
                        supplierDetails: customer
                    },
                    buttons: [
                        {text: updateButton, broadcast: 'app.suppliers.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadAllSuppliers();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.deleteSupplier = deleteSupplier;
            function deleteSupplier(customer) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteSupplierDialogMessage + " [" + customer.name + "] ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        CustomerSupplierService.deleteSupplier(customer.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(supplierDeletedMessage);
                                loadAllSuppliers();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }


            function nextPage() {
                if (vm.inspectionPlans.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadAllSuppliers();
                }
            }

            function previousPage() {
                if (vm.inspectionPlans.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadAllSuppliers();
                }
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadAllSuppliers();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.suppliers = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadAllSuppliers();
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadAllSuppliers();
                });
            })();
        }
    }
);