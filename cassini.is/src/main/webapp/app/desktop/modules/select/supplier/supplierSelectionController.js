/**
 * Created by swapna on 26/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/supplier/supplierService'
    ],
    function (module) {
        module.controller('SupplierSelectionController', SupplierSelectionController);

        function SupplierSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore,
                                             SupplierService) {

            var vm = this;

            vm.loading = true;
            vm.selectedObj = null;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                first: true,
                numberOfElements: 0
            };

            vm.suppliers = angular.copy(pagedResults);

            function loadSuppliers() {
                vm.clear = false;
                vm.loading = true;
                SupplierService.getSuppliers(pageable).then(
                    function (data) {
                        vm.suppliers = data;
                        angular.forEach(vm.suppliers.content, function (supplier) {
                            supplier.checked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.suppliers = [];
                    SupplierService.freeTextSearch(pageable, freeText).then(
                        function (data) {
                            vm.suppliers = data;
                            vm.clear = true;
                            angular.forEach(vm.suppliers.content, function (supplier) {
                                supplier.checked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadSuppliers();
                }
            }

            function clearFilter() {
                loadSuppliers();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(supplier, $event) {
                radioChange(supplier, $event);
                selectRadio();
            }

            function radioChange(supplier, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === supplier) {
                    supplier.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = supplier;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage("Please select supplier");
                }
            }

            function nextPage() {
                if (vm.suppliers.last != true) {
                    pageable.page++;
                    loadSuppliers();
                }
            }

            function previousPage() {
                if (vm.suppliers.first != true) {
                    pageable.page--;
                    loadSuppliers();
                }
            }

            (function () {
                $rootScope.$on('app.supplier.selected', selectRadio);
                loadSuppliers();
            })();
        }
    }
)
;

