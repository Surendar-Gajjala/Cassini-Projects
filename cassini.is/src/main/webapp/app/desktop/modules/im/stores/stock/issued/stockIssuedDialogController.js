define(['app/desktop/modules/im/im.module',
        'app/shared/services/core/storeService',
        'app/shared/services/pm/project/bomService',
        'app/shared/services/tm/taskService',
        'app/shared/services/core/inventoryService',
        'app/shared/services/pm/project/projectSiteService'],
    function (module) {
        module.controller('StockIssuedDialogController', StockIssuedDialogController);

        function StockIssuedDialogController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $sce, InventoryService, TaskService, ProjectSiteService, StoreService, BomService) {
            var vm = this;

            vm.select = select;
            vm.checkAll = checkAll;
            vm.create = create;
            vm.selectTask = selectTask;

            vm.hasError = false;
            vm.loading = true;
            vm.selectedAll = false;
            vm.selectedItems = [];
            vm.taskList = [];
            vm.taskResources = [];
            vm.storeId = $scope.data.store;
            vm.projectId = $scope.data.project;
            var siteStore = null;
            vm.loadSites = loadSites;
            vm.inventoryList = [];

            var inventory = [];

            function loadTasks() {
                TaskService.getTasksBySite(vm.projectId, siteStore.site).then(
                    function (data) {
                        vm.taskList = data;
                        angular.forEach(vm.taskList, function (task) {
                            if (task.status == 'FINISHED') {
                                var index = vm.taskList.indexOf(task);
                                vm.taskList.splice(index, 1);
                            }
                        })
                    })
            }

            function selectTask(task) {
                TaskService.getProjectResource(vm.projectId, task.id).then(
                    function (data) {
                        if (data.length == 0 || data == null) {
                            $rootScope.showErrorMessage("Please add data in Task Details");
                        }
                        else {
                            var tasks = [];

                            angular.forEach(data, function (resource) {
                                if (resource.resourceType != "MANPOWER") {
                                    tasks.push(resource);
                                }
                            });
                            angular.forEach(vm.inventoryList, function (inventory) {
                                angular.forEach(tasks, function (task) {
                                    if (inventory.boqItem == task.referenceId) {
                                        vm.taskResources.push(task);
                                    }
                                });

                            });

                            BomService.getBoqItemReferences($stateParams.projectId, vm.taskResources, 'referenceId');
                            findResourceInventory(vm.taskResources);
                        }

                    }
                )

            }

            function findResourceInventory(resources) {
                angular.forEach(resources, function (resource) {
                    resource.inventory = 0;
                    InventoryService.getItemInventory($stateParams.projectId, resource.referenceId).then(
                        function (data) {
                            inventory = data;
                            angular.forEach(inventory, function (inv) {
                                resource.inventory = inv.storeOnHand;

                            });
                            var quantity = calculateShortage(inventory);
                            if (resource.quantity > quantity) {
                                resource.shortage = resource.quantity - quantity;
                            } else {
                                resource.shortage = 0;
                            }
                        })
                })

            }

            function calculateShortage(inventory) {
                var quantity = 0;
                angular.forEach(inventory, function (inv) {
                    quantity += inv.storeOnHand;
                });
                return quantity;

            }

            function checkAll() {
                if (vm.selectedAll) {
                    vm.selectedItems = [];
                    vm.selectedAll = true;
                    angular.forEach(vm.taskResources, function (item) {
                        item.selected = vm.selectedAll;
                        vm.selectedItems.push(item);
                    });

                } else {
                    vm.selectedAll = false;
                    angular.forEach(vm.taskResources, function (item) {
                        item.selected = vm.selectedAll;
                        vm.selectedItems = [];
                    });
                }
            }

            function select(item) {
                var flag = true;
                if (item.selected == false) {
                    vm.selectedAll = false;
                    var index = vm.selectedItems.indexOf(item);
                    vm.selectedItems.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedItems, function (selectedItem) {
                        if (selectedItem.id == item.id) {
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
                if (vm.selectedItems.length > 0) {
                    $rootScope.hideSidePanel('right');
                    $scope.callback(vm.selectedItems);
                    vm.selectedItems = [];
                }
                else {
                    $rootScope.showErrorMessage("Please add  at least one Item(s)");
                }
            }

            function loadSites() {
                StoreService.getsitesByStore(vm.projectId, vm.storeId).then(
                    function (data) {
                        siteStore = data;
                        if (siteStore != null) {
                            loadTasks();
                        }
                    }
                )
            }

            function loadInventory() {
                vm.loading = true;
                StoreService.getInventory($stateParams.projectId, $stateParams.storeId).then(
                    function (data) {
                        vm.inventoryList = data;

                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadSites();
                    loadInventory();
                    $rootScope.$on('app.item.issued', create);
                }
            })();
        }
    }
)
;