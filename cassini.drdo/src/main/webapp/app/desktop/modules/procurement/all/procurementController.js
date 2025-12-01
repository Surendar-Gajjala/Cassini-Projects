define(
    [
        'app/desktop/modules/procurement/procurement.module',
        'app/shared/services/core/procurementService',
        'app/shared/services/core/inwardService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('ProcurementController', ProcurementController);

        function ProcurementController($scope, $rootScope, $translate, $window, $timeout, $application,
                                       ProcurementService, InwardService, ItemService, DialogService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa flaticon-businessman276";
            $rootScope.viewInfo.title = "Suppliers";

            var vm = this;

            vm.loading = true;
            vm.buttonName = "New Supplier";

            var pageable = {
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
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            var filters = {
                name: null,
                description: null,
                code: null,
                mobileNumber: null,
                email: null,
                contactPerson: null,
                searchText: null
            };

            vm.suppliers = angular.copy(pagedResults);
            vm.manufacturers = angular.copy(pagedResults);

            vm.newProcurement = newProcurement;

            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;
            vm.suppliersView = true;
            vm.manufacturersView = false;
            vm.deleteManufacturer = deleteManufacturer;
            vm.deleteSupplier = deleteSupplier;
            vm.changeView = changeView;

            function deleteManufacturer(mfr) {
                if (mfr.id != undefined) {
                    var options = {
                        title: "Delete Manufacturer",
                        message: "",
                        okButtonClass: 'btn-danger'
                    };

                    if (mfr.id != undefined) {
                        ItemService.checkItemInstanceWithMfr(mfr.id).then(
                            function (data) {
                                if (data) {
                                    options.message = "Manufacturer already used, you can not delete this";
                                    DialogService.confirm(options, function (yes) {
                                        if (yes == true) {

                                        }
                                    });
                                } else {
                                    options.message = "Are you sure do you want to delete this Manufacturer";
                                    DialogService.confirm(options, function (yes) {
                                        if (yes == true) {
                                            ProcurementService.deleteManufacturer(mfr.id).then(
                                                function (data) {
                                                    loadManufacturers();
                                                }
                                            )
                                        }
                                    });
                                }
                            });
                    }
                }
            }

            function deleteSupplier(supp) {
                var options = {
                    title: "Delete Supplier",
                    message: "",
                    okButtonClass: 'btn-danger'
                };

                if (supp.id != undefined) {
                    InwardService.checkInwardsWithSupplier(supp.id).then(
                        function (data) {
                            if (data) {
                                options.message = "Supplier already used, you can not delete this";
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {

                                    }
                                });
                            } else {
                                options.message = "Are you sure do you want to delete this supplier";
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        ProcurementService.deleteSupplier(supp.id).then(
                                            function (data) {
                                                loadSuppliers();
                                            }
                                        )
                                    }
                                });
                            }
                        });
                }
            }

            function changeView(mode) {
                if (mode == "supplier") {
                    vm.manufacturersView = false;
                    vm.buttonName = "New Supplier";
                    filters.searchText = null;
                    vm.searchText = null;
                    $rootScope.viewInfo.title = "Suppliers";
                    $timeout(function () {
                        vm.suppliersView = true;
                    }, 200);
                    pageable = {
                        page: 0,
                        size: 20,
                        sort: {
                            field: "modifiedDate",
                            order: "DESC"
                        }
                    };
                    loadSuppliers();
                } else if (mode == "manufacturer") {
                    vm.suppliersView = false;
                    vm.buttonName = "New Manufacturer";
                    filters.searchText = null;
                    vm.searchText = null;
                    $rootScope.viewInfo.title = "Manufacturers";
                    $timeout(function () {
                        vm.manufacturersView = true;
                    }, 200);
                    pageable = {
                        page: 0,
                        size: 20,
                        sort: {
                            field: "modifiedDate",
                            order: "DESC"
                        }
                    };
                    loadManufacturers();
                }
            }

            function resetPage() {
                pageable.page = 0;
                $scope.freeTextQuery = null;
                if (vm.manufacturersView) {
                    loadManufacturers();
                } else if (vm.suppliersView) {
                    loadSuppliers();
                }
            }

            function nextPage() {
                if (vm.suppliersView) {
                    if (vm.suppliers.last != true) {
                        pageable.page++;
                        if ($scope.freeTextQuery == null || $scope.freeTextQuery == '') {
                            loadSuppliers();
                        } else {
                            loadFreeTextSuppliers();
                        }

                    }

                } else {
                    if (vm.manufacturers.last != true) {
                        pageable.page++;
                        if ($scope.freeTextQuery == null || $scope.freeTextQuery == '') {
                            loadManufacturers();
                        } else {
                            loadFreeTextManufacturers();
                        }

                    }
                }
            }

            function previousPage() {
                if (vm.suppliersView) {
                    if (vm.suppliers.first != true) {
                        pageable.page--;
                        loadSuppliers();
                    }
                } else {
                    if (vm.manufacturers.first != true) {
                        pageable.page--;
                        loadManufacturers();
                    }
                }
            }

            function clearFilter() {
                loadSuppliers();
                vm.clear = false;
                $rootScope.showSearch = false;
            }


            function newProcurement() {
                if (vm.buttonName == "New Supplier") {
                    var options = {
                        title: "New Supplier",
                        template: 'app/desktop/modules/procurement/new/newSupplierView.jsp',
                        controller: 'NewSupplierController as newSupplierVm',
                        resolve: 'app/desktop/modules/procurement/new/newSupplierController',
                        width: 600,
                        showMask: true,
                        buttons: [
                            {text: "Create", broadcast: 'app.procurement.supplier.new'}
                        ],
                        callback: function () {
                            $timeout(function () {
                                loadSuppliers();
                            }, 500);
                        }
                    };
                } else {
                    var options = {
                        title: "New Manufacturer",
                        template: 'app/desktop/modules/procurement/new/newManufacturerView.jsp',
                        controller: 'NewManufacturerController as newManufacturerVm',
                        resolve: 'app/desktop/modules/procurement/new/newManufacturerController',
                        width: 600,
                        showMask: true,
                        buttons: [
                            {text: "Create", broadcast: 'app.procurement.manufacturer.new'}
                        ],
                        callback: function () {
                            $timeout(function () {
                                loadManufacturers();
                            }, 500);
                        }
                    };
                }
                $rootScope.showSidePanel(options);

            }

            vm.editSupplier = editSupplier;
            function editSupplier(supplier) {
                var options = {
                    title: "Edit Supplier",
                    template: 'app/desktop/modules/procurement/new/editSupplierView.jsp',
                    controller: 'EditSupplierController as editSupplierVm',
                    resolve: 'app/desktop/modules/procurement/new/editSupplierController',
                    width: 600,
                    showMask: true,
                    data: {
                        supplierDetails: supplier
                    },
                    buttons: [
                        {text: "Update", broadcast: 'app.procurement.supplier.edit'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadSuppliers();
                        }, 500);
                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.editManufacturer = editManufacturer;
            function editManufacturer(manufacturer) {
                var options = {
                    title: "Edit Manufacturer",
                    template: 'app/desktop/modules/procurement/new/editManufacturerView.jsp',
                    controller: 'EditManufacturerController as editManufacturerVm',
                    resolve: 'app/desktop/modules/procurement/new/editManufacturerController',
                    width: 600,
                    showMask: true,
                    data: {
                        manufacturerDetails: manufacturer
                    },
                    buttons: [
                        {text: "Update", broadcast: 'app.procurement.manufacturer.edit'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadManufacturers();
                        }, 500);
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != "" && freeText != undefined) {
                    pageable.page = 0;
                    filters.searchText = freeText;
                    $scope.freeTextQuery = freeText;
                    if (vm.manufacturersView) {
                        loadFreeTextSuppliers();
                    } else if (vm.suppliersView) {
                        loadFreeTextManufacturers();
                    }
                } else {
                    resetPage();
                }
            }

            function loadFreeTextSuppliers() {
                ProcurementService.getFilterManufactures(pageable, filters).then(
                    function (data) {
                        vm.manufacturers = data;
                        vm.loading = false;
                    }
                )
            }

            function loadFreeTextManufacturers() {
                ProcurementService.getFilterSuppliers(pageable, filters).then(
                    function (data) {
                        vm.suppliers = data;
                        vm.loading = false;
                    }
                )
            }

            function loadSuppliers() {
                ProcurementService.getAllSuppliers(pageable).then(
                    function (data) {
                        vm.suppliers = data;
                        vm.loading = false;
                    }
                )
            }


            function loadManufacturers() {
                ProcurementService.getAllManufactures(pageable).then(
                    function (data) {
                        vm.manufacturers = data;
                        vm.loading = false;
                    }
                )
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadSuppliers();
                    loadManufacturers();
                });
            })();
        }
    }
);