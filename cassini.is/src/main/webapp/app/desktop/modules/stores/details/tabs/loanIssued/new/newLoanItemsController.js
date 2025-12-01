/**
 * Created by swapna on 13/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/topInventoryService',
        'app/shared/services/store/topStoreService'

    ],
    function (module) {
        module.controller('NewLoanItemsController', NewLoanItemsController);

        function NewLoanItemsController($scope, $rootScope, $window, $state, $stateParams, $timeout,
                                        TopInventoryService, TopStoreService) {
            var vm = this;
            vm.itemList = [];
            vm.newLoan = $scope.data.newLoan;
            var addedItemsMap = $scope.data.addedItemsMap;
            vm.newLoan.loanItems = [];
            vm.addToLoanItems = addToLoanItems;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            var pageable = {
                page: 0,
                size: 10
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

            function addToLoanItems(loanItem) {
                if (!loanItem.isAdded) {
                    loanItem.isNew = true;
                    loanItem.editMode = true;
                    var index = vm.itemList.content.indexOf(loanItem);
                    vm.itemList.content.splice(index, 1);
                    loanItem.isAdded = true;
                    if (vm.itemList.content.length == 0) {
                        if (vm.itemList.last != true && !vm.loading) {
                            pageable.page++;
                            loadStoreInventoryforLoan();
                        }
                        else if (vm.itemList.first != true && !vm.loading) {
                            pageable.page--;
                            loadStoreInventoryforLoan();
                        }
                    }
                    $scope.callback(loanItem);
                }
            }

            function loadStoreInventoryforLoan() {
                vm.loading = true;
                searchMode = null;
                TopInventoryService.getStoreInventoryforLoan($rootScope.storeId, vm.newLoan.fromProjectObject.id, pageable).then(
                    function (data) {
                        vm.itemList = data;
                        angular.forEach(vm.itemList.content, function (item) {
                            var itemObj = addedItemsMap.get(item.itemDTO.itemNumber);
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
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.loading = true;
                    searchMode = "freeText";
                    pageable.page = 0;
                    TopStoreService.storeInventoryFreeTextSearchWithProject($stateParams.storeId, vm.newLoan.fromProjectObject.id, pageable, freeText).then(
                        function (data) {
                            vm.itemList = data;
                            angular.forEach(vm.itemList.content, function (item) {
                                var itemObj = addedItemsMap.get(item.itemDTO.itemNumber);
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
                    loadStoreInventoryforLoan();
                }
            }

            function resetPage() {
                pageable.page = 0;
            }

            function nextPage() {
                if (vm.itemList.last != true && !vm.loading) {
                    pageable.page++;
                    loadSearchItem();
                }
            }

            function previousPage() {
                if (vm.itemList.first != true && !vm.loading) {
                    pageable.page--;
                    loadSearchItem();
                }
            }

            var searchMode = null;

            function loadSearchItem() {
                if (searchMode == 'freeText') {
                    freeTextSearch();
                } else if (searchMode == null) {
                    loadStoreInventoryforLoan();
                }
            }

            (function () {
                loadStoreInventoryforLoan();
            })();
        }
    }
)
;