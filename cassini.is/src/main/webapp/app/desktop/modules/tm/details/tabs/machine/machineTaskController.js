define(['app/desktop/modules/tm/tm.module',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/pm/project/bomService',
        'app/desktop/modules/tm/details/tabs/machine/machineDialogueController',
        'app/shared/services/store/topStockMovementService'
    ],
    function (module) {
        module.controller('MachineTaskController', MachineTaskController);

        function MachineTaskController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                       TaskService, $window, BomService, ProjectService, DialogService, TopStockMovementService) {
            var vm = this;

            vm.updateMachines = updateMachines;
            vm.deleteMachineResource = deleteMachineResource;
            vm.taskId = $stateParams.taskId;
            vm.projectId = $stateParams.projectId;
            vm.resourceMachines = [];
            vm.loading = false;

            function loadMachineResources() {
                vm.loading = true;
                TaskService.getProjectResourcesByType(vm.projectId, vm.taskId, 'MACHINETYPE').then(
                    function (data) {
                        vm.resourceMachines = data;
                        BomService.getBoqItemReferences(vm.projectId, vm.resourceMachines, 'referenceId');
                        $timeout(function () {
                            loadResourcesQuantity();
                            vm.loading = false;
                        }, 100);
                    }
                );
            }

            function loadResourcesQuantity() {
                var referenceIds = [];
                angular.forEach(vm.resourceMachines, function (machine) {
                    referenceIds.push(machine.referenceId);
                });
                TaskService.getItemAvailableQuantities(vm.projectId, referenceIds).then(
                    function (data) {
                        angular.forEach(vm.resourceMachines, function (machine) {
                            machine.assignedQty = data[machine.referenceId];
                            machine.balanceQty = machine.referenceIdObject.quantity - machine.assignedQty;
                        })
                    })
            }

            function validateQuantity(data, machine) {
                var valid = false;
                if (machine.balanceQty >= data) {
                    valid = true;
                }
                return valid;
            }

            function updateMachines(data, machine) {
                if (data > 0) {
                    if (validateQuantity(data, machine)) {
                        var resource = {
                            id: machine.id,
                            referenceId: machine.referenceId,
                            project: $stateParams.projectId,
                            task: vm.taskId,
                            quantity: data,
                            units: machine.units,
                            resourceType: "MACHINETYPE",
                            itemNumber: machine.itemNumber
                        };

                        TaskService.updateResource(vm.projectId, resource).then(
                            function (data) {
                                loadMachineResources();
                                $rootScope.showSuccessMessage("Machine qty updated successfully");
                            });
                    } else {
                        return "Machine qty cannot be greater then balance qty";
                    }
                } else {
                    return "Please enter +ve number";
                }
            }

            function deleteMachineResource(machine) {
                TopStockMovementService.getStockMovementByBomItemAndItemNum(machine.referenceIdObject.itemNumber, machine.referenceId).then(
                    function (stockMovements) {
                        var x = 0;
                        angular.forEach(stockMovements, function (stockMovement) {
                            if (stockMovement.movementType == "ISSUED") {
                                x++;
                            }
                        });
                        if (x > 0) {
                            $rootScope.showErrorMessage("This machine is already issued! We cannot remove this machine")
                        }
                        else {
                            var options = {
                                title: 'Remove Machine',
                                message: 'Are you sure you want to remove this Machine?',
                                okButtonClass: 'btn-danger'
                            };
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    ProjectService.deleteResource(vm.projectId, machine.id).then(
                                        function (data) {
                                            var index = vm.resourceMachines.indexOf(machine);
                                            vm.resourceMachines.splice(index, 1);
                                            $rootScope.showSuccessMessage("Machine removed successfully");
                                            loadMachineResources();
                                            $rootScope.loadDetailsCount();
                                        }
                                    )
                                }
                            });
                        }
                    }
                )
            }

            function machineDialogue() {
                var options = {
                    title: 'Select Machine(s)',
                    side: 'left',
                    showMask: true,
                    template: 'app/desktop/modules/tm/details/tabs/machine/machineDialogueView.jsp',
                    controller: 'MachineDialogueController as machineDialogueVm',
                    resolve: 'app/desktop/modules/tm/details/tabs/machine/machineDialogueController',
                    width: 700,
                    data: {
                        resourceMachines: vm.resourceMachines
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.machine.new'}
                    ],
                    callback: function () {
                        loadMachineResources();
                        $rootScope.loadDetailsCount();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.task.addMachine', function () {
                        machineDialogue();
                    });
                    $scope.$on('app.task.tabactivated', function (event, data) {
                        if (data.tabId == 'details.machine') {
                            loadMachineResources();
                        }
                    });
                }
            })();
        }
    }
)
;
