define(['app/desktop/modules/im/im.module',
        'app/shared/services/core/storeService',
        'app/shared/services/pm/project/bomService'],
    function (module) {
        module.controller('StockReceivedDialogController', StockReceivedDialogController);

        function StockReceivedDialogController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $sce, StoreService, BomService) {
            var vm = this;

            vm.select = select;
            vm.checkAll = checkAll;
            vm.create = create;
            vm.applyCriteria = applyCriteria;
            vm.resetCriteria = resetCriteria;
            vm.previousPage = previousPage;
            vm.nextPage = nextPage;

            vm.hasError = false;
            vm.loading = true;
            vm.selectedAll = false;
            vm.searchQuery = null;
            vm.selectedItems = [];

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "itemNumber",
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

            function checkAll() {
                if (vm.selectedAll) {
                    vm.selectedItems = [];
                    vm.selectedAll = true;
                } else {
                    vm.selectedAll = false;
                }
                angular.forEach(vm.itemList, function (item) {
                    item.selected = vm.selectedAll;
                    vm.selectedItems.push(item);
                })
            }

            function select(item) {
                var flag = true;
                if (item.selected == false) {
                    vm.selectedAll = false;
                    var index = vm.selectedItems.indexOf(item);
                    vm.selectedItems.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedItems, function (selectedItem) {
                        if (selectedItem.id == item.id) {
                            flag = false;
                            var index = vm.selectedItems.indexOf(item);
                            vm.selectedItems.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedItems.push(item);
                    }
                }
            }

            function create() {
                if (vm.selectedItems.length > 0) {
                    $rootScope.hideSidePanel('right');
                    $scope.callback(vm.selectedItems);
                    vm.selectedItems = [];
                }
                else {
                    $rootScope.showErrorMessage("Please add  at least one Item(s)");
                }
            }

            function applyCriteria() {
                vm.pageable.page = 0;
                BomService.searchBomItems($stateParams.projectId, vm.searchQuery).then(
                    function (data) {
                        vm.loading = false;
                        vm.itemList = data;
                        loadItemQuantities();
                    }
                );
            }

            function resetCriteria() {
                vm.filters = {
                    field: ""
                };
                vm.pageable.page = 0;
                vm.selectedAll = false;
                vm.searchQuery = null;
                loadItems();
            }

            function nextPage() {
                vm.pageable.page++;
                loadItems();
            }

            function previousPage() {
                vm.pageable.page--;
                loadItems();
            }

            function loadItems() {
                BomService.getPagedBoqItems($stateParams.projectId, vm.pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.itemList = data;
                        loadItemQuantities();
                        angular.forEach(vm.itemList, function (item) {
                            item.selected = false;
                        });
                    }
                );
            }

            function loadItemQuantities() {
                var itemIds = [];
                angular.forEach(vm.itemList, function (item) {
                    itemIds.push(item.id);
                });
                StoreService.getStockReceivedQuantities($stateParams.projectId, itemIds).then(
                    function (data) {
                        angular.forEach(vm.itemList, function (item) {
                                item.totalReceivedQty = data[item.id];
                                if (item.totalReceivedQty == null) {
                                    item.totalReceivedQty = 0;
                                    item.balanceQty = item.quantity - item.totalReceivedQty;
                                } else {
                                    item.balanceQty = item.quantity - item.totalReceivedQty;
                                }
                            }
                        )
                    });

            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadItems();
                    $rootScope.$on('app.item.received', create);
                }
            })();
        }
    }
);