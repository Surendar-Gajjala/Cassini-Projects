/**
 * Created by swapna on 02/10/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/store/topInventoryService'
    ],
    function (module) {
        module.controller('AllocateItemsDialogController', AllocateItemsDialogController);

        function AllocateItemsDialogController($scope, $rootScope, $stateParams, $state, ProjectService, TopInventoryService) {
            var vm = this;
            vm.projects = [];
            vm.items = [];
            var valid = true;
            vm.project = null;
            vm.getUnallocatedProjectInventoryBoq = getUnallocatedProjectInventoryBoq;
            vm.loading = false;

            function loadProjects() {
                ProjectService.getProjects().then(
                    function (projects) {
                        vm.projects = projects;
                    }
                )
            }

            function getUnallocatedProjectInventoryBoq() {
                vm.loading = true;
                TopInventoryService.getUnallocatedProjectInventoryBoq($rootScope.storeId, vm.project.id).then(
                    function (data) {
                        vm.items = data;
                        vm.loading = false;
                    },
                    function (error) {
                        vm.loading = false;
                        $rootScope.showErrorMessage("Unable to view Items");
                    }
                )
            }

            function allcoate() {
                var stock = [];
                valid = true;
                angular.forEach(vm.items, function (item) {
                    item.person = window.$application.login.person.id;
                    if (valid && item.itemIssueQuantity != undefined && item.itemIssueQuantity != "") {
                        if (validateQuantity(item) && valid) {
                            stock.push(item);
                        }
                    }
                });
                if (stock.length > 0 && valid == true) {
                    $rootScope.showBusyIndicator();
                    TopInventoryService.allocateItemsToProject($rootScope.storeId, vm.project.id, stock).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                            $rootScope.showSuccessMessage("Items allocated successfully");
                        },
                        function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage("Items allocation failed");
                        }
                    )
                }
                else {
                    $rootScope.showErrorMessage("Please enter +ve number qty for the items");
                }
            }

            function validateQuantity(item) {
                var valid = true;
                if (item.storeInventory < item.itemIssueQuantity) {
                    valid = false;
                    $rootScope.showErrorMessage("Allocating qty cannot be greater than unallocated qty");
                    $rootScope.hideBusyIndicator();
                }
                else if (item.itemIssueQuantity < 0) {
                    valid = false;
                    $rootScope.showErrorMessage("Please enter +ve number");
                    $rootScope.hideBusyIndicator();
                }

                return valid;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadProjects();
                    $scope.$on('app.storeItems.allocateItems', allcoate);
                }
            })();
        }
    }
);