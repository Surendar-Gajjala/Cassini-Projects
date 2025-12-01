define([
        'app/desktop/modules/pqm/pqm.module',
        'app/shared/services/core/supplierService',
        'app/desktop/modules/mfr/supplier/directive/supplierDirective'
    ],
    function (module) {
        module.controller('SupplierSelectionController', SupplierSelectionController);

        function SupplierSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate, SupplierService) {

            var vm = this;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.error = "";

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

            vm.suppliers = angular.copy(pagedResults);

            vm.onSelectType = onSelectType;
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.onOk = onOk;

            var parsed = angular.element("<div></div>");
            var selectOnePr = parsed.html($translate.instant("SELECT_ONE_SUPPLIER")).html();

            vm.selectedSuppliers = [];
            vm.filters = {
                searchQuery: null,
                name: null,
                type: '',
                audit: '',
                city: null
            };

            $scope.check = false;

            function selectAll(check) {
                vm.selectedSuppliers = [];
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.suppliers.content, function (item) {
                        item.selected = false;
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.suppliers.content, function (item) {
                        item.selected = true;
                        vm.selectedSuppliers.push(item);
                    })
                }
            }

            function nextPage() {
                if (vm.suppliers.last != true) {
                    vm.pageable.page++;
                    $scope.check = false;
                    vm.selectedSuppliers = [];
                    loadSuppliers();
                }
            }

            function previousPage() {
                if (vm.suppliers.first != true) {
                    vm.pageable.page--;
                    $scope.check = false;
                    vm.selectedSuppliers = [];
                    loadSuppliers();
                }
            }

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.filters.type = itemType.id;
                    vm.filters.typeName = itemType.name;
                    vm.pageable.page = 0;
                    loadSuppliers();
                    vm.clear = true;
                }
            }


            function clearFilter() {
                vm.filters.searchQuery = null;
                vm.filters.type = '';
                vm.filters.typeName = null;
                vm.filters.city = null;
                vm.selectedType = null;
                vm.pageable.page = 0;
                $scope.check = false;
                vm.selectedSuppliers = [];
                vm.selectAllCheck = false;
                loadSuppliers();
                vm.clear = false;
            }

            function onOk() {
                if (vm.selectedSuppliers.length != 0) {
                    $rootScope.hideSidePanel();
                    $scope.callback(vm.selectedSuppliers);
                }

                if (vm.selectedSuppliers.length == 0) {
                    $rootScope.showWarningMessage(selectOnePr);
                }

            }

            vm.selectAllCheck = false;

            function selectCheck(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedSuppliers, function (selectedItem) {
                    if (selectedItem.id == item.id) {
                        flag = false;
                        var index = vm.selectedSuppliers.indexOf(item);
                        vm.selectedSuppliers.splice(index, 1);
                    }
                });
                if (flag) {
                    vm.selectedSuppliers.push(item);
                }

                if (vm.selectedSuppliers.length != vm.suppliers.content.length) {
                    vm.selectAllCheck = false;
                } else {
                    vm.selectAllCheck = true;
                }
            }

            vm.searchFilterItem = searchFilterItem;
            function searchFilterItem() {
                vm.pageable.page = 0;
                loadSuppliers();
                vm.clear = true;
                vm.selectAllCheck = false;
                if (vm.filters.searchQuery == "" || vm.filters.searchQuery == null) {
                    vm.clear = false;
                    vm.selectAllCheck = false;
                }
            }

            function loadSuppliers() {
                vm.filters.audit = $stateParams.supplierAuditId;
                SupplierService.getAllSuppliers(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.suppliers = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadSuppliers();
                $rootScope.$on("add.select.suppliers", onOk);
            })();
        }
    }
)
;