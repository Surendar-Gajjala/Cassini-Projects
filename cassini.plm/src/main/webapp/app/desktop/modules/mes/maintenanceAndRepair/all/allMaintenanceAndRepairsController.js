define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective'
    ],
    function (module) {
        module.controller('AllMaintenanceAndRepairController', AllMaintenanceAndRepairController);

        function AllMaintenanceAndRepairController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newMaintenanceAndRepair = newMaintenanceAndRepair;
            vm.machines = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;

            vm.searchText = null;
            vm.filterSearch = null;


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

            vm.filters = {
                searchQuery: null
            };
            $scope.freeTextQuery = null;

            // vm.machines = angular.copy(pagedResults);

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newMaintenanceAndRepair = parsed.html($translate.instant("NEW_MAINTENANCE_AND_REPAIRS")).html();

            function newMaintenanceAndRepair() {
                var options = {
                    title: newMaintenanceAndRepair,
                    template: 'app/desktop/modules/mes/maintenanceAndRepair/new/newMaintenanceAndRepairsView.jsp',
                    controller: 'NewMaintenanceAndRepairController as newMaintenanceAndRepairVm',
                    resolve: 'app/desktop/modules/mes/maintenanceAndRepair/new/newMaintenanceAndRepairsController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.maintenanceAndRepair.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadMaintenanceAndRepairs();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }


            function nextPage() {
                if (vm.machines.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadMaintenanceAndRepairs();
                }
            }

            function previousPage() {
                if (vm.machines.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadMaintenanceAndRepairs();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadMaintenanceAndRepairs();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadMaintenanceAndRepairs();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.machines = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadMaintenanceAndRepairs();
            }

            function loadMaintenanceAndRepairs(){

            }

            vm.showMaintenanceAndRepair = showMaintenanceAndRepair;
            function showMaintenanceAndRepair(maintenanceAndRepair){
                $state.go('app.mes.masterData.maintenanceAndRepair.details', {maintenanceAndRepairId: maintenanceAndRepair.id, tab: 'details.basic'});
            }

            (function () {
                loadMaintenanceAndRepairs();
            })();

        }
    }
);