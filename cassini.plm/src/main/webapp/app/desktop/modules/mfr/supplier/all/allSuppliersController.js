define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/shared/services/core/supplierService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllSuppliersController', AllSuppliersController);

        function AllSuppliersController($scope, $rootScope, $translate, $timeout, $state, $window, $application,
                                        $stateParams, $cookies, $sce, SupplierService, ObjectTypeAttributeService, CommonService, DialogService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newSupplier = newSupplier;
            vm.suppliers = [];
            vm.objectIds = [];
            vm.selectedAttributes = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.deleteSupplier = deleteSupplier;


            vm.searchText = null;
            vm.filterSearch = null;
            var currencyMap = new Hashtable();

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
                audit: '',
                city: null
            };
            $scope.freeTextQuery = null;

            vm.suppliers = angular.copy(pagedResults);       
            var parsed = angular.element("<div></div>"); 
            var create = parsed.html($translate.instant("CREATE")).html();
            var newSupplierTitle = parsed.html($translate.instant("NEW_SUPPLIER")).html(); 
            var deleteDialogTitle = parsed.html($translate.instant("DELETE_SUPPLIER")).html();      
            var deleteSupplierDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var supplierDeletedSuccessMessage = parsed.html($translate.instant("SUPPLIER_DELETED_MESSAGE")).html();
            $scope.cannotDeleteApprovedSupplier = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_SUPPLIER")).html();
            function newSupplier() {
                var options = {
                    title: newSupplierTitle,
                    template: 'app/desktop/modules/mfr/supplier/new/newSupplierView.jsp',
                    controller: 'NewSupplierController as newSupplierVm',
                    resolve: 'app/desktop/modules/mfr/supplier/new/newSupplierController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.supplier.new'}
                    ],
                    callback: function (supplier) {
                        $timeout(function () {
                            loadSuppliers();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.suppliers.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadSuppliers();
                }
            }

            function previousPage() {
                if (vm.suppliers.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadSuppliers();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadSuppliers();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadSuppliers();
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
                loadSuppliers();
            }

            vm.suppliers = [];
            function loadSuppliers() {
                vm.suppliers = [];
                vm.loading = false;
                SupplierService.getAllSuppliers(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.suppliers = data;
                        CommonService.getPersonReferences(vm.suppliers.content, 'modifiedBy');
                        loadAttributeValues();
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var suppliersAttributeTitle = parsed.html($translate.instant("ATTRIBUTES")).html();
            var selectedAttributesAdded = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();

            vm.showTypeAttributes = showTypeAttributes;
            function showTypeAttributes() {
                var options = {
                    title: suppliersAttributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        type: "SUPPLIERTYPE",
                        objectType: "MFRSUPPLIER"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("supplierAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadSuppliers();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("supplierAttributes", JSON.stringify(vm.selectedAttributes));

            }

            vm.objectIds = [];
            vm.attributeIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.suppliers.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.suppliers.content);

            }

            function deleteSupplier(supplier) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteSupplierDialogMessage + " [" + supplier.name + "] ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        SupplierService.deleteSupplier(supplier.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(supplierDeletedSuccessMessage);
                                loadSuppliers();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            vm.showSupplier = showSupplier;
            function showSupplier(supplier) {
                $state.go('app.mfr.supplier.details', {supplierId: supplier.id, tab: 'details.basic'});
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("supplierAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            (function () {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("supplierAttributes"));
                } else {
                    setAttributes = null;
                }
                if (setAttributes != null && setAttributes != undefined) {
                    angular.forEach(setAttributes, function (setAtt) {
                        if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                            vm.objectIds.push(setAtt.id);
                        }
                    });
                    ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                        function (data) {
                            if (data.length == 0) {
                                setAttributes = null;
                                $window.localStorage.setItem("supplierAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadSuppliers();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    loadSuppliers();
                }
            })();

        }
    }
);