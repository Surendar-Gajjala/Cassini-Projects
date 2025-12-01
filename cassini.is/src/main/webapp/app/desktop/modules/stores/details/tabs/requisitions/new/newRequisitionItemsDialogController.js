/**
 * Created by swapna on 13/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/core/itemService'

    ],
    function (module) {
        module.controller('NewRequisitionItemsController', NewRequisitionItemsController);

        function NewRequisitionItemsController($scope, $rootScope, $window, $state, $stateParams, $q, $timeout,
                                               ItemService) {
            var vm = this;

            vm.projectPersons = [];
            vm.itemList = [];
            var searchQuery = null;
            vm.newRequsition = $scope.data.newRequisition;
            vm.addToRequisitionItems = addToRequisitionItems;
            var addedItemsMap = $scope.data.addedItemsMap;
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

            function addToRequisitionItems(requisitionItem) {
                if (!requisitionItem.isAdded) {
                    requisitionItem.isNew = true;
                    requisitionItem.editMode = true;
                    var index = vm.itemList.content.indexOf(requisitionItem);
                    vm.itemList.content.splice(index, 1);
                    if (vm.itemList.content.length == 0) {
                        if (vm.itemList.last != true) {
                            pageable.page++;
                            loadMasterDataItems();
                        }
                        else if (vm.itemList.first != true) {
                            pageable.page--;
                            loadMasterDataItems();
                        }
                    }
                    $scope.callback(requisitionItem);
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

            function back() {
                $window.history.back();
            }

            function freeTextSearch(freeText) {
                if (searchQuery == null) {
                    pageable.page = 0;
                }
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
                searchQuery = null;
                loadMasterDataItems();
            }

            function nextPage() {
                if (vm.itemList.last != true && !vm.loading) {
                    pageable.page++;
                    if (searchQuery == null) {
                        loadMasterDataItems();
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
                        loadMasterDataItems();
                    }
                    else {
                        freeTextSearch(searchQuery);
                    }
                }
            }

            (function () {
                loadMasterDataItems();
            })();
        }
    }
)
;