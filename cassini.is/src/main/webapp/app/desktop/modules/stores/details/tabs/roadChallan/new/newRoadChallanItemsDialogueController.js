define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('NewRoadChallanItemsController', NewRoadChallanItemsController);

        function NewRoadChallanItemsController($scope, $q, $timeout, $rootScope, $window, $state, $stateParams,
                                               TopStoreService) {
            var vm = this;

            vm.roadChallanItems = [];
            vm.roadChallan = $scope.data.newRoadChallan;
            vm.roadChallan.customRoadChalanItems = [];
            var addedItemsMap = $scope.data.addedItemsMap;
            vm.loading = false;
            vm.addToRoadChallanItems = addToRoadChallanItems;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            var pageable = {
                page: 0,
                size: 10,
                sort: {
                    field: "item",
                    order: "ASC"
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
            vm.items = pagedResults;

            function addToRoadChallanItems(roadchallanItem) {
                if (!roadchallanItem.isAdded) {
                    roadchallanItem.stockMovementDTO.itemDTO.itemType = roadchallanItem.stockMovementDTO.itemDTO.materialType;
                    roadchallanItem.materialItem = roadchallanItem.stockMovementDTO.itemDTO;
                    roadchallanItem.isNew = true;
                    roadchallanItem.editMode = true;
                    var index = vm.roadChallanItems.indexOf(roadchallanItem);
                    vm.roadChallanItems.splice(index, 1);
                    if (vm.roadChallanItems.length == 0) {
                        if (vm.items.last != true) {
                            pageable.page++;
                            loadInventoryItems();
                        }
                        else if (vm.items.first != true) {
                            pageable.page--;
                            loadInventoryItems();
                        }
                    }
                    $scope.callback(roadchallanItem);
                }
            }

            function loadInventoryItems() {
                vm.clear = false;
                vm.loading = true;
                TopStoreService.getStoreInventory(pageable, $rootScope.storeId).then(
                    function (data) {
                        vm.roadChallanItems = data.content;
                        vm.items = data;
                        vm.loading = false;
                        angular.forEach(vm.roadChallanItems, function (item) {
                            var itemObj = addedItemsMap.get(item.stockMovementDTO.itemDTO.itemNumber);
                            if (itemObj != null) {
                                item.isAdded = true;
                            }
                            else {
                                item.isAdded = false;
                            }
                        });
                    }
                )
            }

            function freeTextSearch(freeText) {
                pageable.page = 0;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.roadChallanItems = [];
                    vm.loading = true;
                    TopStoreService.storeInventoryFreeTextSearchWithoutProject($stateParams.storeId, pageable, freeText).then(
                        function (data) {
                            vm.roadChallanItems = data;
                            vm.clear = true;
                            angular.forEach(vm.roadChallanItems, function (item) {
                                var itemObj = addedItemsMap.get(item.stockMovementDTO.itemDTO.itemNumber);
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
                } else {
                    resetPage();
                    loadInventoryItems();
                }
            }

            function resetPage() {
                pageable.page = 0;
            }

            function nextPage() {
                if (vm.items.last != true && !vm.loading) {
                    pageable.page++;
                    loadInventoryItems();
                }
            }

            function previousPage() {
                if (vm.items.first != true && !vm.loading) {
                    pageable.page--;
                    loadInventoryItems();
                }
            }

            (function () {
                loadInventoryItems();
            })();

        }
    }
)
;
