/**
 * Created by swapna on 05/12/18.
 */

define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStockIssuedService',
        'app/shared/services/core/itemService'

    ],
    function (module) {
        module.controller('NewStockReturnItemsController', NewStockReturnItemsController);

        function NewStockReturnItemsController($scope, $rootScope, $window, $state, $stateParams,
                                               TopStockIssuedService, ItemService) {
            var vm = this;

            vm.projectPersons = [];
            vm.itemList = [];
            var project = $scope.data.projctObj;
            var projectId = 0;
            vm.newStockReturn = $scope.data.newStockReturn;
            vm.newStockReturn.customRequisitionItems = [];
            var addedItemsMap = $scope.data.addedItemsMap;
            var searchQuery = null;
            $scope.freeTextQuery = null;
            vm.addToStockReturnItems = addToStockReturnItems;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
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

            function addToStockReturnItems(stockReturnItem) {
                if (!stockReturnItem.isAdded) {
                    stockReturnItem.isNew = true;
                    stockReturnItem.editMode = true;
                    var index = vm.itemList.content.indexOf(stockReturnItem);
                    vm.itemList.content.splice(index, 1);
                    $scope.callback(stockReturnItem);
                }
            }

            function loadProjectItems() {
                vm.loading = true;
                if (project == null || project == 0) {
                    projectId = 0;
                }
                else {
                    projectId = project.id;
                }
                TopStockIssuedService.getProjectItems($rootScope.storeId, projectId, pageable).then(
                    function (data) {
                        vm.itemList = data;
                        angular.forEach(vm.itemList.content, function (item) {
                            var itemObj = addedItemsMap.get(item.id);
                            if (itemObj != null) {
                                item.isAdded = true;
                            }
                            else {
                                item.isAdded = false;
                            }
                        });
                        vm.loading = false;
                    })
            }

            function freeTextSearch(freeText) {
                $scope.freeTextQuery = freeText;
                if (searchQuery == null) {
                    pageable.page = 0;
                }
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    searchQuery = freeText;
                    vm.loading = true;
                    if (project == null || project == 0) {
                        searchMasterDataItems(freeText);
                    }
                    else {
                        searchProjectMaterials(freeText);
                    }
                }
                else {
                    resetPage();
                }
            }

            function searchProjectMaterials(freeText) {
                ItemService.searchStoreMaterials(projectId, $rootScope.storeId, pageable, freeText).then(
                    function (data) {
                        vm.itemList = data;
                        vm.clear = true;
                        angular.forEach(vm.itemList.content, function (item) {
                            item.itemType = item.materialType.name;
                            var itemObj = addedItemsMap.get(item.id);
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

            function searchMasterDataItems(freeText) {
                ItemService.materialFreeTextSearch(pageable, freeText).then(
                    function (data) {
                        vm.itemList = data;
                        vm.clear = true;
                        angular.forEach(vm.itemList.content, function (item) {
                            item.itemType = item.itemType.name;
                            var itemObj = addedItemsMap.get(item.id);
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
                $scope.freeTextQuery = null;
                searchQuery = null;
                loadProjectItems();
            }

            function nextPage() {
                if (vm.itemList.last != true && !vm.loading) {
                    pageable.page++;
                    if (searchQuery == null) {
                        loadProjectItems();
                    }
                    else {
                        freeTextSearch(searchQuery);
                    }
                }
            }

            function previousPage() {
                if (vm.itemList.first != true && !vm.loading) {
                    pageable.page--;
                    if (searchQuery == null) {
                        loadProjectItems();
                    }
                    else {
                        freeTextSearch(searchQuery);
                    }
                }
            }

            function back() {
                $window.history.back();
            }

            (function () {
                loadProjectItems();
            })();
        }
    }
)
;