define(['app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('MachineSelectionController', MachineSelectionController);

        function MachineSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore,
                                            $uibModal, ItemService) {

            var vm = this;

            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.selectRadio = selectRadio;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate"
                }
            };
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            var machineCriteria = {
                freeTextSearch: true
            };

            vm.selectedObj = null;
            vm.machines = angular.copy(pagedResults);

            function nextPage() {
                if (vm.machines.last != true) {
                    pageable.page++;
                    loadMachines();
                }
            }

            function previousPage() {
                if (vm.machines.first != true) {
                    pageable.page--;
                    loadMachines();
                }
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    machineCriteria.searchQuery = freeText;
                    ItemService.machineFreeTextSearch(pageable, machineCriteria).then(
                        function (data) {
                            vm.machines = data;
                            vm.clear = true;
                        }
                    )
                } else {
                    resetPage();
                    loadMachines();
                }
            }

            function clearFilter() {
                loadMachines();
                vm.clear = false;
            }

            function resetPage() {
                pageable.page = 0;
            }

            function loadMachines() {
                vm.clear = false;
                vm.loading = true;
                ItemService.getMachines(pageable).then(
                    function (data) {
                        vm.machines = data;
                        angular.forEach(data, function (machine) {
                            machine.isChecked = false;
                        });
                        vm.loading = false;
                    }
                );
            }

            function selectRadioChange(machine, $event) {
                radioChange(machine, $event);
                selectRadio();
            }

            function radioChange(machine, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === machine) {
                    machine.isChecked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = machine;
                }
            }

            function selectRadio() {
                $scope.callback(vm.selectedObj);
                $rootScope.hideSidePanel('left');
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.machine.selected', selectRadio);
                    loadMachines();
                }
            })();
        }
    }
)
;
