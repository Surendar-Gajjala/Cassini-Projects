/**
 * Created by Namratha on 21-12-2018.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStoreService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('NewStockIssueItemsController', NewStockIssueItemsController);

        function NewStockIssueItemsController($scope, $timeout, $rootScope, $window, $stateParams,
                                              TopStoreService, ItemService) {
            var vm = this;

            vm.stockIssueItems = [];
            vm.stockIssue = $scope.data.newStockIssue;
            var addedItemsMap = $scope.data.addedItemsMap;
            vm.stockIssue.customIssueItems = [];
            vm.addToIssueItems = addToIssueItems;
            vm.itemList = [];
            vm.loading = false;
            vm.item = null;
            var searchQuery = null;
            $scope.freeTextQuery = null;
            vm.disableNext = false;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.pageable = {
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
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.itemList = pagedResults;

            function addToIssueItems(item) {
                if (!item.isAdded) {
                    item.isNew = true;
                    item.editMode = true;
                    var index = vm.itemList.content.indexOf(item);
                    vm.itemList.content.splice(index, 1);
                    $scope.callback(item);
                }
            }

            function loadItems() {
                vm.loading = true;
                vm.itemList = [];
                ItemService.getMaterialItemsDTO($rootScope.storeId, vm.pageable, $scope.freeTextQuery).then(
                    function (data) {
                        vm.itemList = data;
                        vm.loading = false;
                        if ((vm.itemList.size * (vm.pageable.page + 1)) >= vm.itemList.totalElements) {
                            vm.disableNext = true;
                        }
                        angular.forEach(vm.itemList.content, function (item) {
                            var itemObj = addedItemsMap.get(item.id);
                            if (itemObj != null) {
                                item.isAdded = true;
                            }
                            else {
                                item.isAdded = false;
                            }
                        });
                    },
                    function (error) {
                        vm.loading = false;
                        $rootScope.showErrorMessage(error.message);
                    }
                )

            }

            function back() {
                $window.history.back();
            }

            function freeTextSearch(freeText) {
                $scope.freeTextQuery = freeText;
                vm.pageable.page = 0;
                loadItems();
            }

            function resetPage() {
                searchQuery = null;
                vm.pageable.page = 0;
                $scope.freeTextQuery = null;
                loadItems();
            }

            function nextPage() {
                if (!vm.disableNext) {
                    vm.pageable.page++;
                    loadItems();
                }
            }

            function previousPage() {
                if (vm.pageable.page > 0) {
                    vm.pageable.page--;
                    vm.disableNext = false;
                    loadItems();
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadItems();
                    vm.selectedItems = [];
                }
            })();
        }
    }
)
;

