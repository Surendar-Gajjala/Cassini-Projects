define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/core/storeService',
        'app/shared/services/pm/project/bomService',
        'app/shared/services/store/topStoreService',
        'app/shared/services/store/topStockReceivedService',
        'app/shared/services/core/itemService',
        'app/shared/services/store/scrapItemService'

    ],
    function (module) {
        module.controller('ScrapRequestDialogController', ScrapRequestDialogController);

        function ScrapRequestDialogController($scope, $rootScope, $timeout, ItemService, $stateParams, $cookies, TaskService,
                                              TopStoreService, BomService, ScrapItemService, ProjectService) {
            var vm = this;

            vm.select = select;
            vm.checkAll = checkAll;
            vm.create = create;
            vm.resetPage = resetPage;
            vm.previousPage = previousPage;
            vm.nextPage = nextPage;
            var scrap = $scope.data.scrapObj;

            vm.hasError = false;
            vm.loading = true;
            vm.selectedAll = false;
            vm.searchQuery = null;
            vm.selectedItems = [];
            vm.projectId = null;
            vm.wbsData = [];
            vm.taskList = [];
            vm.store = null;

            vm.itemList = [];

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
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
                    vm.selectedItems.push(item);
                })
            }

            function select(item) {
                var flag = true;
                if (item.selected == false) {
                    vm.selectedAll = false;
                    var index = vm.selectedItems.indexOf(item);
                    vm.selectedItems.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedItems, function (selectedItem) {
                        if (item.id == selectedItem.id) {
                            flag = false;
                            var index = vm.selectedItems.indexOf(item);
                            vm.selectedItems.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedItems.push(item);
                    }
                }
            }

            function create() {
                angular.forEach(vm.itemList, function (item) {
                    if (item.quantity > 0) {
                        vm.selectedItems.push(item);
                    }
                });
                if (vm.selectedItems.length > 0) {
                    $scope.callback(vm.selectedItems);
                } else if (vm.selectedItems.length == 0) {
                    $rootScope.showErrorMessage("Please add atleast one item");
                }
            }

            function nextPage() {
                vm.pageable.page++;
                loadMasterData();
            }

            function previousPage() {
                vm.pageable.page--;
                loadMasterData();
            }

            function resetPage() {
                vm.filters = {
                    field: ""
                };
                vm.searchQuery = null;
                vm.pageable.page = 0;
                vm.selectedAll = false;
                loadMasterData();
            }

            function loadScrapItems() {
                ScrapItemService.getScrapItems($stateParams.scrapDetailsId).then(
                    function (data) {
                        vm.items = data;
                        loadMasterData();
                    }
                );

            }

            function loadMasterData() {
                ItemService.getProjectMaterials(scrap.project).then(
                    function (data) {
                        vm.itemList = data;
                        if (vm.itemList.length > 0) {
                            angular.forEach(vm.items, function (itemObj) {
                                var index = vm.itemList.findIndex(item = > item.id == itemObj.item
                                )
                                if (index != -1) {
                                    vm.itemList.splice(index, 1);
                                }
                            });
                        }
                    }
                );
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadScrapItems();
                    $rootScope.$on('app.scrap.reqItem', create);
                }
            })();
        }
    }
);