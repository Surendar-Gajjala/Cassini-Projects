define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/store/topInventoryService',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('ItemStoreInventoryController', ItemStoreInventoryController);

        function ItemStoreInventoryController($scope, $rootScope, $timeout, $interval, $state, $stateParams, TopInventoryService,
                                              ProjectService) {

            $rootScope.viewInfo.icon = "glyphicon glyphicon-equalizer";
            $rootScope.viewInfo.title = "Inventory";

            var vm = this;

            vm.loading = true;
            vm.mode = $stateParams.mode;
            vm.colspan = 0;
            vm.inventories = [];
            var searchQuery = null;

            vm.previousPage = previousPage;
            vm.nextPage = nextPage;
            vm.back = back;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;

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
            vm.inventories = angular.copy(pagedResults);

            function back() {
                $rootScope.mode = "Inventory"
                window.history.back();
            }

            function freeTextSearch(searchTerm) {
                vm.pageable.page = 0;
                searchQuery = searchTerm;
                loadInventories();
            }

            function resetPage() {
                searchQuery = null;
                vm.pageable.page = 0;
                loadInventories();
            }

            function loadInventories() {
                vm.loading = true;
                vm.inventories = [];
                TopInventoryService.searchCustomInventories(vm.pageable, searchQuery).then(
                    function (data) {
                        vm.loading = false;
                        vm.inventories = data;
                        angular.forEach(vm.inventories, function (inventory) {
                            inventory.storeDetails = [];
                            angular.forEach(inventory.storeDetailsList, function (inv) {
                                if (inv.projectStoreInvDetailsList.length == 0) {
                                    var project = {
                                        projectName: "Total",
                                        quantity: 0
                                    };
                                    inventory.storeDetails.push(project);
                                    angular.forEach(inventory.projectInventoryDetailsList, function (i) {
                                        var project = {
                                            projectName: i.projectName,
                                            quantity: 0
                                        };
                                        inventory.storeDetails.push(project);
                                    })
                                }
                                else {
                                    var project = {
                                        projectName: "Total",
                                        quantity: inv.totalQuantity
                                    };
                                    inventory.storeDetails.push(project);
                                    angular.forEach(inv.projectStoreInvDetailsList, function (i) {
                                        inventory.storeDetails.push(i);
                                    })
                                }
                            })
                        })
                    }
                );
            }

            function previousPage() {
                if (vm.pageable.page > 0) {
                    vm.pageable.page--;
                    loadInventories();
                }
            }

            function nextPage() {
                if (vm.inventories[0].last != true) {
                    vm.pageable.page++;
                    loadInventories();
                }
            }

            function loadProjects() {
                ProjectService.getProjects().then(
                    function (projects) {
                        vm.colspan = projects.length + 1;
                    })
            }

            (function () {
                loadProjects();
                loadInventories();
            })();
        }
    }
);