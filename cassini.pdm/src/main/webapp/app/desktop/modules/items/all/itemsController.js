define(
    [
        'app/desktop/modules/items/item.module',
        'split-pane',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/itemService'

    ],
    function (module) {
        module.controller('ItemsController', ItemsController);

        function ItemsController($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, $uibModal,
                                 CommonService, DialogService, ItemService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-th";
            $rootScope.viewInfo.title = "Items";
            $rootScope.viewInfo.showDetails = false;


            var vm = this;
            vm.loading = true;
            vm.clear = false;
            vm.pageable = {
                page: 0,
                size: 20,
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

            $scope.pagenumbers = [20, 50, 100];
            /*vm.folderObject = {
             folder: 0,
             objectType: null,
             objectId: 0
             };*/

            vm.showItem = showItem;
            vm.items = angular.copy(pagedResults);
            vm.showNewItem = showNewItem;
            vm.editItem = editItem;
            vm.deleteItem = deleteItem;
            vm.clearFilter = clearFilter;
            vm.freeTextSearch = freeTextSearch;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;
            vm.flag = false;
            vm.onSelectType = onSelectType;
            vm.itemSearch = itemSearch;
            vm.advancedSearch = advancedSearch;
            vm.selectAll = selectAll;

            vm.allItemsSelected = false;
            var searchMode = null;
            var simpleFilters = null;
            var freeTextQuery = null;
            var advancedFilters = null;
            var classificationTree = null;

            function clearFilter() {

                vm.clear = false;
            }


            function selectAll() {

                vm.allItemsSelected = !vm.allItemsSelected;
                angular.forEach(vm.items.content, function (item) {

                    item.selected = vm.allItemsSelected;

                });

            }

            function showNewItem() {
                var options = {
                    title: 'New Item',
                    template: 'app/desktop/modules/items/new/newItemView.jsp',
                    controller: 'NewItemController as newItemVm',
                    resolve: 'app/desktop/modules/items/new/newItemController',
                    width: 600,
                    showMask: true,
                    data: {},
                    buttons: [
                        {text: 'Create', broadcast: 'app.items.new'}
                    ],
                    callback: function () {
                        loadItems();
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function freeTextSearch(freeText) {
                searchMode = "freetext";
                freeTextQuery = freeText;
                vm.pageable.page = 0;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    ItemService.freeTextSearch(vm.pageable, freeText).then(
                        function (data) {
                            vm.items = data;
                            vm.clear = true;
                            CommonService.getPersonReferences(vm.items.content, 'createdBy');
                            CommonService.getPersonReferences(vm.items.content, 'modifiedBy');
                        }
                    )
                }
                else {
                    vm.resetPage();
                    loadItems();
                }
            }

            function deleteItem(item) {
                var options = {
                    title: 'Delete Item',
                    message: 'Are you sure you want to delete this Item?',
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ItemService.deleteItem(item.id).then(
                            function (data) {
                                var index = vm.items.content.indexOf(item);
                                vm.items.content.splice(index, 1);
                                $rootScope.showErrorMessage("Item deleted successfully!");
                            }
                        )
                    }
                });
            }

            function showItem(item) {
                $state.go('app.items.details', {itemId: item.id});
            }

            function editItem(item) {
                $state.go('app.items.edit', {itemId: item.id});
            }


            function resetPage() {
                vm.pageable.page = 0;
            }

            function nextPage() {
                if (vm.items.last != true) {
                    vm.pageable.page++;

                }
            }

            function previousPage() {
                if (vm.items.first != true) {
                    vm.pageable.page--;

                }
            }

            function onSelectType(node) {
                var data = classificationTree.tree('getData', node.target);
                vm.selectedItemType = data.attributes.itemType;
                loadItems();
            }

            function loadItems() {
                searchMode = null;
                vm.clear = false;
                vm.loading = true;
                vm.items = angular.copy(pagedResults);
                var promise = null;
                if (vm.selectedItemType == null) {
                    promise = ItemService.getItems(vm.pageable);
                }
                else {
                    promise = ItemService.getItemsByType(vm.selectedItemType.id, vm.pageable);
                }

                promise.then(
                    function (data) {
                        vm.loading = false;
                        vm.items = data;
                        angular.forEach(vm.items.content, function (item) {

                            item.selected = false;

                        });
                        CommonService.getPersonReferences(vm.items.content, 'createdBy');
                        CommonService.getPersonReferences(vm.items.content, 'modifiedBy');
                    }
                )
            }

            function itemSearch() {
                var options = {
                    title: 'Items simple search',
                    template: 'app/desktop/modules/items/search/itemSearchDialogueView.jsp',
                    controller: 'ItemSearchDialogueController as itemSearchDialogueVm',
                    resolve: 'app/desktop/modules/items/search/itemSearchDialogueController',
                    width: 600,
                    buttons: [
                        {text: 'Search', broadcast: 'app.items.search'}
                    ],
                    callback: function (filter) {
                        vm.resetPage();

                        if ((filter.itemType != null && filter.itemType != undefined && filter.itemType != "") ||
                            (filter.itemNumber != null && filter.itemNumber != undefined && filter.itemNumber != "") ||
                            (filter.revision != null && filter.revision != undefined && filter.revision != "") ||
                            (filter.description != null && filter.description != undefined && filter.description != "")) {


                            searchItem(filter);
                            $rootScope.closeNotification();
                            $rootScope.hideSidePanel();
                        } else {

                            $rootScope.showWarningMessage('Enter Search Details');

                        }


                    }
                };

                $rootScope.showSidePanel(options);

            }

            function searchItem(filters) {
                searchMode = "simple";
                simpleFilters = filters;
                ItemService.searchItem(vm.pageable, filters).then(
                    function (data) {
                        vm.items = data;
                        vm.clear = true;
                        CommonService.getPersonReferences(vm.items.content, 'createdBy');
                        CommonService.getPersonReferences(vm.items.content, 'modifiedBy');
                    }
                )
            }

            function advancedSearch() {
                var options = {
                    title: 'Items advanced search',
                    template: 'app/desktop/modules/items/search/advancedSearchView.jsp',
                    resolve: 'app/desktop/modules/items/search/advancedSearchController',
                    controller: 'AdvancedSearchController as advancedSearchVm',
                    width: 700,
                    buttons: [
                        {text: 'Search', broadcast: 'app.items.advsearch'}
                    ],
                    callback: function (filter) {
                        vm.resetPage();
                        advancedItemSearch(filter);
                        $rootScope.hideSidePanel();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function advancedItemSearch(filters) {
                searchMode = "advanced";
                advancedFilters = filters;

                ItemService.advancedSearchItem(vm.pageable, filters).then(
                    function (data) {
                        vm.items = data;
                        vm.clear = true;
                        CommonService.getPersonReferences(vm.items.content, 'createdBy');
                        CommonService.getPersonReferences(vm.items.content, 'modifiedBy');
                    }
                )
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadItems();
                });
            })();
        }
    }
)
;