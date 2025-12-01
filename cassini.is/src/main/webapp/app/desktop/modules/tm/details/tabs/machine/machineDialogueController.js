define(['app/desktop/modules/tm/tm.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/pm/project/bomService'

    ],
    function (module) {
        module.controller('MachineDialogueController', MachineDialogueController);
        function MachineDialogueController($scope, $rootScope, $timeout, $state, $stateParams, ProjectService,
                                           CommonService, ItemTypeService, TaskService, BomService) {

            $rootScope.viewInfo.icon = "fa flaticon-deadlines";
            $rootScope.viewInfo.title = "Task Details";

            var vm = this;

            vm.selectedAll = false;
            vm.selectedMachines = [];
            vm.machines = [];
            vm.freeTextSearch = freeTextSearch;

            vm.checkAll = checkAll;
            vm.create = create;
            vm.select = select;
            vm.clearFilters = clearFilters;
            //vm.applyFilters = applyFilters;
            vm.previousPage = previousPage;
            vm.nextPage = nextPage;
            vm.resetPage = resetPage;
            vm.showSearchMode = false;
            $scope.freeTextQuery = null;

            vm.pageable = {
                page: 0,
                size: 10,
                sort: {
                    field: 'modifiedBy',
                    order: 'desc'
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                first: true,
                numberOfElements: 0
            };
            vm.machines = angular.copy(pagedResults);
            vm.projectId = $stateParams.projectId;

            function nextPage() {
                /* vm.pageable.page++;
                 loadMachines();*/
                if (vm.machines.last != true) {
                    vm.pageable.page++;
                    vm.selectedAll = false;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery);
                    }
                    else {
                        loadMachines();
                    }
                }
            }

            function previousPage() {
                if (vm.machines.first != true) {
                    vm.pageable.page--;
                    vm.selectedAll = false;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery);
                    }
                    else {
                        loadMachines();
                    }
                }
            }

            function resetPage() {
                vm.pageable.page = 0;
                vm.showSearchMode = false;
            }

            vm.emptyFilters = {
                itemName: null,
                itemNumber: null,
                itemType: "MACHINETYPE"
            };

            function clearFilters() {
                vm.emptyFilters.itemNumber = null;
                vm.emptyFilters.itemName = null;
                vm.emptyFilters.itemType = "MACHINETYPE";
                vm.selectedMachines = [];
                vm.selectedAll = false;
                vm.clear = false;
                loadMachines();
            }

            function freeTextSearch(freeText) {
                if (!vm.showSearchMode) {
                    vm.pageable.page = 0;
                }
                vm.showSearchMode = true;
                $scope.freeTextQuery = freeText;
                vm.emptyFilters.searchQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.loading = true;
                    vm.selectedAll = false;
                    BomService.getBoqItemsByFilters($stateParams.projectId, $stateParams.taskId, vm.pageable, vm.emptyFilters).then(
                        function (data) {
                            vm.machines = data;
                            vm.loading = false;
                            var count = 0;
                            vm.machines.numberOfElements = (vm.pageable.page + 1) * vm.pageable.size;
                            if (vm.machines.numberOfElements > vm.machines.totalElements) {
                                vm.machines.numberOfElements = vm.machines.totalElements;
                            }
                            angular.forEach(vm.machines.content, function (machine) {
                                angular.forEach(vm.selectedMachines, function (selectedMachine) {
                                    if (machine.itemNumber == selectedMachine.itemNumber && machine.sheet == selectedMachine.sheet) {
                                        machine.selected = true;
                                        count++;
                                    }
                                })
                            });
                            if (vm.machines.content.length > 0 && count == vm.machines.content.length) {
                                vm.selectedAll = true;
                            }
                            vm.clear = true;
                        }, function (error) {
                            vm.loading = false;
                        }
                    );
                } else {
                    resetPage();
                    loadMachines();
                }
            }

            function select(machine) {
                var flag = true;
                if (machine.selected == false) {
                    vm.selectedAll = false;
                    var index = vm.selectedMachines.indexOf(machine);
                    vm.selectedMachines.splice(index, 1);
                } else {
                    angular.forEach(vm.selectedMachines, function (selectMachine) {
                        if (selectMachine.id == machine.id) {
                            flag = false;
                            var index = vm.selectedMachines.indexOf(machine);
                            vm.selectedMachines.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.selectedMachines.push(machine);
                    }
                }
                if (vm.selectedMachines.length == vm.machines.content.length) {
                    vm.selectedAll = true;
                }
            }

            function create() {
                if (vm.selectedMachines.length != 0) {
                    vm.machineResources = [];
                    angular.forEach(vm.selectedMachines, function (machine) {
                        var resource = {
                            referenceId: machine.id,
                            project: $stateParams.projectId,
                            task: $stateParams.taskId,
                            quantity: 0,
                            units: machine.units,
                            resourceType: machine.itemType,
                            itemNumber: machine.itemNumber
                        };
                        vm.machineResources.push(resource);
                    });
                    TaskService.createProjectResource($stateParams.projectId, vm.machineResources).then(
                        function (data) {
                            $rootScope.hideSidePanel('left');
                            $scope.callback(data);
                            vm.creating = false;
                            vm.selectedMachines = [];
                            $rootScope.showSuccessMessage("Machine(s) added successfully");
                        });
                } else {
                    $rootScope.showErrorMessage("Please add  atleast one machine");
                }
            }

            function loadMachines() {
                if (vm.pageable == undefined) {
                    vm.pageable = {
                        page: 0,
                        size: 20
                    };
                }
                vm.loading = true;
                BomService.getItemsForTask($stateParams.projectId, $stateParams.taskId, 'MACHINETYPE', vm.pageable).then(
                    function (data) {
                        vm.machines = data;
                        var count = 0;
                        vm.loading = false;
                        vm.machines.numberOfElements = (vm.pageable.page + 1) * vm.pageable.size;
                        if (vm.machines.numberOfElements > vm.machines.totalElements) {
                            vm.machines.numberOfElements = vm.machines.totalElements;
                        }
                        angular.forEach(vm.machines.content, function (machine) {
                            angular.forEach(vm.selectedMachines, function (selectedMachine) {
                                if (machine.itemNumber == selectedMachine.itemNumber && machine.sheet == selectedMachine.sheet) {
                                    machine.selected = true;
                                    count++;
                                }
                            })
                        });
                        if (count == vm.machines.content.length) {
                            vm.selectedAll = true;
                        }
                    }
                );
            }

            function checkAll() {
                if (vm.selectedAll) {
                    angular.forEach(vm.machines.content, function (machine) {
                        if (!machine.selected) {
                            machine.selected = vm.selectedAll;
                            vm.selectedMachines.push(machine);
                        }
                    });
                } else {
                    angular.forEach(vm.machines.content, function (machine) {
                        var index = vm.selectedMachines.indexOf(machine);
                        vm.selectedMachines.splice(index, 1);
                        machine.selected = vm.selectedAll;
                    });
                }
            }

            (function () {
                if ($application.homeLoaded == true) {

                    loadMachines();
                    $rootScope.$on('app.machine.new', create);
                }
            })();
        }
    }
);