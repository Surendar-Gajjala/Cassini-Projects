define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('BomItemCompareController', BomItemCompareController);

        function BomItemCompareController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                          ItemService) {
            var vm = this;

            vm.itemId = $rootScope.seletedItemId;

            vm.hasFromBomItems = [];
            vm.hasToBomItems = [];
            vm.fromRevisionBomItems = [];
            vm.toRevisionBomItems = [];

            $rootScope.selectedFromItem = null;
            $scope.selectedFromRevisionItem = null;
            $scope.selectedToRevisionItem = null;
            $scope.itemsAvailable = false;
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
                bomCompare: true
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

            vm.hasToBomItems = angular.copy(pagedResults);

            vm.loadHasBomItems = loadHasBomItems;
            function loadHasBomItems() {
                if (vm.checked) {
                    vm.filters.latest = true;
                } else {
                    vm.filters.latest = false;
                }
                if (vm.filters.searchQuery != null && vm.filters.searchQuery != "" && vm.filters.searchQuery != undefined) {
                    ItemService.getItemsToCompare(vm.pageable, vm.filters).then(
                        function (data) {
                            vm.hasToBomItems = data;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                } else {
                    vm.hasToBomItems = angular.copy(pagedResults);
                    $rootScope.hideBusyIndicator();
                }
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
            $rootScope.itemFilterList = [];
            $rootScope.individualItemsFlag = false;
            $rootScope.fromItemName = null;
            $rootScope.toItemName = null;
            $rootScope.fromItemNumber = null;
            $rootScope.toItemNumber = null;
            $rootScope.fromItemRev = null;
            $rootScope.toItemRev = null;
            function compareIndividualItems() {
                $rootScope.itemList = [];
                $rootScope.itemFilterList = [];
                $rootScope.fromItemName = null;
                $rootScope.toItemName = null;
                $rootScope.fromItemNumber = null;
                $rootScope.toItemNumber = null;
                $rootScope.fromItemRev = null;
                $rootScope.toItemRev = null;
                ItemService.getComparedIndividualItems(vm.itemId, vm.selectedToItemForCompare.id, $rootScope.bomLatestState).then(
                    function (data) {
                        $rootScope.fromItemName = data.fromItemName;
                        $rootScope.toItemName = data.toItemName;
                        $rootScope.fromItemNumber = data.fromItemNumber;
                        $rootScope.toItemNumber = data.toItemNumber;
                        $rootScope.fromItemRev = data.fromItemRev;
                        $rootScope.toItemRev = data.toItemRev;
                        $rootScope.individualRevFlags = false;
                        $rootScope.individualItemsFlag = true;
                        $rootScope.hideBusyIndicator();
                        $rootScope.itemList = data.itemList;
                        $rootScope.itemFilterList = data.itemList;
                        $rootScope.hideSidePanel();
                        $rootScope.closebomComp = true;
                        $('#myModal1').modal('show');
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
                        loadHasBomItems();
                    }
                } else {
                    if (vm.filters.searchQuery != null && vm.filters.searchQuery != "" && vm.filters.searchQuery != undefined) {
                        vm.checked = false;
                        vm.filters.latest = false;
                        $rootScope.showBusyIndicator($("#rightSidePanel"));
                        loadHasBomItems();
                    }
                }
            }

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            function nextPage() {
                if (vm.hasToBomItems.last != true) {
                    vm.pageable.page++;
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    loadHasBomItems();
                }
            }

            function previousPage() {
                if (vm.hasToBomItems.first != true) {
                    vm.pageable.page--;
                    vm.flag = false;
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    loadHasBomItems();
                }
            }

            (function () {
                $rootScope.$on('app.item.bom.compare', compare);

            })();
        }
    }
)
;