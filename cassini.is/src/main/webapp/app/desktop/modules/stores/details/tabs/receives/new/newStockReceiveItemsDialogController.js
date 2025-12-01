define(['app/desktop/modules/stores/store.module',
        'app/shared/services/core/itemService',
        'app/shared/services/store/customPurchaseOrderService'
    ],
    function (module) {
        module.controller('NewStockReceiveItemsController', NewStockReceiveItemsController);

        function NewStockReceiveItemsController($scope, $q, $timeout, $rootScope, $window, $state, $stateParams,
                                                ItemService, CustomPurchaseOrderService) {
            var vm = this;

            vm.stockReceiveItems = [];
            vm.stockReceive = $scope.data.newStockReceive;
            var addedItemsMap = $scope.data.addedItemsMap;
            var searchQuery = null;
            vm.purchaseOrderItems = [];
            vm.viewToolbar = true;
            vm.addToReceiveItems = addToReceiveItems;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            var materialCriteria = {
                freeTextSearch: true,
                searchQuery: null
            };
            var pageable = {
                page: 0,
                size: 10,
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
            vm.itemList = pagedResults;
            vm.loading = false;
            vm.item = null;

            function addToReceiveItems(stockreceiveItem) {
                if (!stockreceiveItem.isAdded) {
                    stockreceiveItem.isNew = true;
                    stockreceiveItem.editMode = true;
                    var index = vm.itemList.content.indexOf(stockreceiveItem);
                    vm.itemList.content.splice(index, 1);
                    if (vm.itemList.content.length == 0) {
                        if (vm.itemList.last != true) {
                            pageable.page++;
                            selection(vm.selectedItem);
                        }
                        else if (vm.itemList.first != true) {
                            pageable.page--;
                            selection(vm.selectedItem);
                        }
                    }
                    $scope.callback(stockreceiveItem, vm.selectedItem);
                }
            }

            function loadMasterDataItems() {
                vm.loading = true;
                ItemService.getMaterials(pageable).then(
                    function (data) {
                        vm.itemList = data;
                        vm.loading = false;
                        angular.forEach(vm.itemList.content, function (item) {
                            var itemObj = addedItemsMap.get(item.itemNumber);
                            if (itemObj != null) {
                                item.isAdded = true;
                            }
                            else {
                                item.isAdded = false;
                            }
                        });
                    }, function (error) {

                    }
                );
            }

            function freeTextSearch(freeText) {
                /* if (searchQuery == null) {
                 pageable.page = 0;
                 }*/
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    searchQuery = freeText;
                    vm.loading = true;
                    searchMasterDataItems(searchQuery);
                }
                else {
                    resetPage();
                }
            }

            function searchMasterDataItems(freeText) {
                materialCriteria.searchQuery = freeText;
                ItemService.materialFreeTextSearch(pageable, materialCriteria).then(
                    function (data) {
                        vm.itemList = data;
                        vm.clear = true;
                        angular.forEach(vm.itemList.content, function (item) {
                            var itemObj = addedItemsMap.get(item.itemNumber);
                            if (itemObj != null) {
                                item.isAdded = true;
                            }
                            else {
                                item.isAdded = false;
                            }
                        });
                        vm.loading = false;
                    }
                )
            }

            function resetPage() {
                pageable.page = 0;
                loadMasterDataItems();
                searchQuery = null;
            }

            function nextPage() {
                if (vm.itemList.last != true && !vm.loading) {
                    pageable.page++;
                    loadMasterDataItems();
                    /*freeTextSearch(searchQuery);*/
                }
            }

            function previousPage() {
                if (vm.itemList.first != true && !vm.loading) {
                    pageable.page--;
                    loadMasterDataItems();
                    /*freeTextSearch(searchQuery);*/
                }
            }

            function back() {
                $window.history.back();
            }

            (function () {
                loadMasterDataItems();
            })();
        }
    }
)
;
