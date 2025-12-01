/**
 * Created by swapna on 29/01/19.
 */
define(['app/desktop/modules/proc/proc.module',
        'app/shared/services/core/subContractService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/pm/project/projectService'
    ],
    function (module) {
        module.controller('WorkOrderSelectionController', WorkOrderSelectionController);

        function WorkOrderSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $uibModal,
                                              SubContractService, ProjectService) {

            var vm = this;

            vm.loading = true;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.selectedObj = null;
            var criteria = {
                searchQuery: null
            }
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            var pageable = {
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
                size: pageable.size,
                number: 0,
                first: true,
                numberOfElements: 0
            };

            vm.workOrders = angular.copy(pagedResults);

            function loadWorkOrders() {
                vm.clear = false;
                vm.loading = true;
                SubContractService.getPageableWorkOrders(pageable).then(
                    function (data) {
                        vm.workOrders = data;
                        SubContractService.getContractorReferences(vm.workOrders.content, 'contractor');
                        ProjectService.getProjectReferences(vm.workOrders.content, 'project');
                        angular.forEach(data.content, function (workOrder) {
                            workOrder.isChecked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.workOrders = [];
                    criteria.searchQuery = freeText;
                    SubContractService.workOrdersFreeTextSearch(pageable, criteria).then(
                        function (data) {
                            vm.workOrders = data;
                            SubContractService.getContractorReferences(vm.workOrders.content, 'contractor');
                            ProjectService.getProjectReferences(vm.workOrders.content, 'project');
                            vm.clear = true;
                            angular.forEach(data.content, function (workOrder) {
                                workOrder.isChecked = false;
                            });
                        }
                    )
                } else {
                    resetPage();
                    loadWorkOrders();
                }
            }

            function clearFilter() {
                loadWorkOrders();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function selectRadioChange(workOrder, $event) {
                radioChange(workOrder, $event);
                selectRadio();
            }

            function radioChange(workOrder, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === workOrder) {
                    workOrder.isChecked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = workOrder;
                }
            }

            function selectRadio() {
                $scope.callback(vm.selectedObj);
                $rootScope.hideSidePanel('left');
            }

            function nextPage() {
                if (vm.workOrders.last != true) {
                    pageable.page++;
                    loadWorkOrders();
                }
            }

            function previousPage() {
                if (vm.workOrders.first != true) {
                    pageable.page--;
                    loadWorkOrders();
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.workOrder.selected', selectRadio);
                    loadWorkOrders();
                }
            })();
        }
    }
)
;