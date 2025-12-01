define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStoreService',
        'app/shared/services/pm/project/bomService',
        'app/shared/services/tm/taskService',
        'app/shared/services/core/inventoryService',
        'app/shared/services/store/topInventoryService',
        'app/shared/services/core/itemService',
        'app/shared/services/store/topStockReceivedService',
        'app/shared/services/pm/project/projectSiteService'],
    function (module) {
        module.controller('StoreStockIssuedDialogController', StoreStockIssuedDialogController);

        function StoreStockIssuedDialogController($scope, $rootScope, $timeout, $state, $stateParams, TopStockIssuedService, $sce, TopStoreService, InventoryService,
                                                  TaskService, TopStockReceivedService, BomService, TopInventoryService, ItemService) {
            var vm = this;

            vm.select = select;
            vm.checkAll = checkAll;
            vm.create = create;

            vm.hasError = false;
            vm.loading = true;
            vm.selectedAll = false;
            vm.selectedItems = [];
            vm.itemList = [];

            vm.project = null;
            vm.loadTaskResources = loadTaskResources;
            vm.inventoryList = [];
            vm.stockIssue = null;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.searchQuery = null;
            vm.selectedAll = false;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "itemNumber",
                    order: "DESC"
                }
            };

            function checkAll() {
                if (vm.selectedAll) {
                    vm.selectedAll = true;
                } else {
                    vm.selectedAll = false;
                }
                angular.forEach(vm.itemList, function (item) {
                    item.selected = vm.selectedAll;
                    item.issue = $stateParams.issueId;
                    item.project = vm.stockIssue.project;
                    vm.selectedItems.push(item);
                })
            }

            function freeTextSearch() {
                vm.pageable.page = 0;
                BomService.searchBomItems(vm.stockIssue.project, vm.searchQuery).then(
                    function (data) {
                        vm.itemList = data;
                        angular.forEach(vm.itemList, function (boqItem) {
                            findResourceInventory(boqItem, boqItem.id);
                        })

                    }
                );
            }

            function resetPage() {
                vm.filters = {
                    field: ""
                };
                vm.searchQuery = null;
                vm.pageable.page = 0;
                vm.selectedAll = false;
                loadTaskResources(vm.stockIssue.task);
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
                        item.issue = $stateParams.issueId;
                        item.project = vm.stockIssue.project;
                        vm.selectedItems.push(item);
                    }
                }
                if (vm.selectedItems.length == vm.itemList.length) {
                    vm.selectedAll = true;
                }
            }

            vm.changedResourceQuantity = changedResourceQuantity;

            function changedResourceQuantity(resource) {
                if (resource.Qty == 0) {
                    var index = vm.selectedItems.indexOf(resource);
                    if (index != -1) {
                        vm.selectedItems.splice(resource);
                    }
                }
            }

            function create() {
                vm.selectedItems = [];
                angular.forEach(vm.itemList, function (item) {
                    if (item.Qty > 0) {
                        item.project = vm.stockIssue.project;
                        item.issue = vm.stockIssue.id;
                        vm.selectedItems.push(item);
                    }
                });
                if (vm.selectedItems.length > 0) {
                    $scope.callback(vm.selectedItems);
                } else if (vm.selectedItems.length == 0) {
                    $rootScope.showErrorMessage("Please add atleast one item");
                }
            }

            function loadTaskResources() {
                vm.itemList = [];
                TopStockIssuedService.getProjectResourcesInventory($rootScope.storeId, vm.stockIssue.project, vm.stockIssue.task).then(
                    function (data) {
                        vm.itemList = data;
                        vm.loading = false;
                    },
                    function (error) {
                        vm.loading = false;
                        $rootScope.showErrorMessage(error.message);
                    }
                )

            }

            function findResourceInventory(resource, itemId) {
                resource.inventory = 0;
                TopInventoryService.getInventoryByProjectAndItemAndStore(vm.stockIssue.project, itemId, $rootScope.storeId).then(
                    function (data) {
                        if (data != "") {
                            resource.inventory = data.storeOnHand;
                            var quantity = data.storeOnHand;
                            if (resource.resourceQuantity > quantity) {
                                resource.shortage = resource.resourceQuantity - quantity;
                            } else {
                                resource.shortage = 0;
                            }
                        }
                    });
                vm.loading = false;

            }

            function loadInventory() {
                TopStoreService.getInventoryByStore($rootScope.storeId).then(
                    function (data) {
                        vm.inventoryList = data;
                    }
                )
            }

            function loadStockIssue() {
                vm.loading = true;
                TopStockIssuedService.getTopStockIssue($stateParams.storeId, $stateParams.issueId).then(
                    function (data) {
                        vm.stockIssue = data;
                        TaskService.getProjectTask(vm.stockIssue.project, vm.stockIssue.task).then(
                            function (data) {
                                vm.stockIssue.taskName = data.name;
                            }
                        );
                        $timeout(function () {
                            loadTaskResources();
                        }, 2000);

                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadStockIssue();
                    loadInventory();
                    vm.selectedItems = [];
                    $rootScope.$on('app.stores.issued', create);
                }
            })();
        }
    }
)
;