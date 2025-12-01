define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('ItemToItemCompareController', ItemToItemCompareController);

        function ItemToItemCompareController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                             ItemService) {
            var vm = this;

            vm.itemId = $rootScope.seletedItemId;


            vm.fromItems = [];
            vm.dupvalue = [];
            $rootScope.selectedFromItem = null;
            $scope.selectedFromRevisionItem = null;
            $scope.selectedToRevisionItem = null;

            vm.pageable = {
                page: 0,
                size: 10,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.filters = {
                searchQuery: "",
                latest: false,
                itemId: $rootScope.item.id,
                bomCompare: false
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

            vm.fromItems = angular.copy(pagedResults);
            vm.loadAllCommonTypeItems = loadAllCommonTypeItems;
            function loadAllCommonTypeItems() {
                if (vm.checked) {
                    vm.filters.latest = true;
                } else {
                    vm.filters.latest = false;
                }
                if (vm.filters.searchQuery != null && vm.filters.searchQuery != "" && vm.filters.searchQuery != undefined) {
                    ItemService.getItemsToCompare(vm.pageable, vm.filters).then(
                        function (data) {
                            vm.fromItems = data;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                              $rootScope.showErrorMessage(error.message);
                              $rootScope.hideBusyIndicator();
                         }
                    )
                } else {
                    vm.fromItems = angular.copy(pagedResults);
                    $rootScope.hideBusyIndicator();
                }
                /*ItemService.getAllItemsToCompare(vm.itemId).then(
                 function (data) {
                 angular.forEach(data, function (item) {
                 if (vm.itemId == item.id) {
                 $rootScope.selectedFromItem = item;
                 }
                 if ($rootScope.item.itemType.name == item.itemType.name) {
                 vm.fromItems.push(item);
                 }

                 });

                 }
                 )*/
            }

            $rootScope.whichItemCompare = null;
            vm.selectedItem = selectedItem;
            vm.selectedToItemForCompare = null;
            function selectedItem(item, event) {
                $rootScope.whichItemCompare = item.rev;
                vm.selectedToItemForCompare = item.rev;
            }

            var parsed = angular.element("<div></div>");
            var slectItemValidation = parsed.html($translate.instant("COMPARE_ITEM_VALIDATION")).html();
            $scope.search = parsed.html($translate.instant("ALL_VIEW_SEARCH")).html();
            vm.compareIndividualItems = compareIndividualItems;
            /* vm.compareIndividualItemRevisions = compareIndividualItemRevisions;*/
            $rootScope.itemList = [];
            $rootScope.individualItemsFlag = false;
            function compareIndividualItems() {
                vm.itemList = [];
                ItemService.getComparedItems(vm.itemId, vm.selectedToItemForCompare.id).then(
                    function (data) {
                        $rootScope.itemToItemFromItem = data.fromItem;
                        $rootScope.itemToItemToItem = data.toItem;
                        $rootScope.itemToItemElements = data.listOfItemsCompared;
                        $rootScope.hideBusyIndicator();
                        $rootScope.hideSidePanel();
                        $rootScope.closeitemCompare = true;
                        $('#myModalForItemCompare').modal('show');
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )

            }

            function compare() {
                $rootScope.showBusyIndicator();
                if (vm.selectedToItemForCompare != null) {
                    compareIndividualItems();

                }
                else {

                    $rootScope.hideBusyIndicator();
                    $rootScope.showWarningMessage(slectItemValidation);

                }

            }

            vm.itemSearch = itemSearch;
            function itemSearch() {
                vm.pageable.page = 0;
                if (vm.checked == true) {
                    if (vm.filters.searchQuery != null && vm.filters.searchQuery != "" && vm.filters.searchQuery != undefined) {
                        vm.filters.latest = true;
                        $rootScope.showBusyIndicator($("#rightSidePanel"));
                        loadAllCommonTypeItems();
                    }
                } else {
                    if (vm.filters.searchQuery != null && vm.filters.searchQuery != "" && vm.filters.searchQuery != undefined) {
                        vm.checked = false;
                        vm.filters.latest = false;
                        $rootScope.showBusyIndicator($("#rightSidePanel"));
                        loadAllCommonTypeItems();
                    }
                }
            }

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            function nextPage() {
                if (vm.fromItems.last != true) {
                    vm.pageable.page++;
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    loadAllCommonTypeItems();
                }
            }

            function previousPage() {
                if (vm.fromItems.first != true) {
                    vm.pageable.page--;
                    vm.flag = false;
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    loadAllCommonTypeItems();
                }
            }

            (function () {
                $rootScope.$on('app.item.to.item.compare', compare);

            })();
        }
    }
)
;