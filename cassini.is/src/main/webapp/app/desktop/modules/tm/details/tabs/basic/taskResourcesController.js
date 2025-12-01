define(['app/desktop/modules/tm/tm.module',
        'app/shared/services/tm/taskService',
        'app/shared/services/core/itemService',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('TaskResourcesController', TaskResourcesController);

        function TaskResourcesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, CommonService, ItemService, ProjectService, TaskService, DialogService) {
            var vm = this;

            vm.taskId = $stateParams.taskId;
            vm.projectId = $stateParams.projectId;
            /*vm.taskHistory = $scope.data.taskHistory;*/
            vm.totalUnitsCompleted = $scope.data.totalUnitsCompleted;
            vm.task = $scope.data.task;
            vm.mode = $scope.data.mode;

            vm.persons = [];
            vm.machineItems = [];
            vm.materialItems = [];
            vm.projectRoles = [];

            var today = moment(new Date());
            var todayStr = today.format('DD/MM/YYYY');
            vm.newUnitOfWorkCompleted = {
                task: $stateParams.taskId,
                timeStamp: new Date(),
                completedBy: $rootScope.login.person,
                unitsCompleted: null
            };
            vm.newUnitOfWorkCompleted.timeStamp = todayStr;

            function loadProjectResources() {
                TaskService.getTaskResources(vm.taskId).then(
                    function (data) {
                        vm.resources = data;
                        vm.persons = data.persons;
                        vm.machineItems = data.machineItems;
                        vm.materialItems = data.materialItems;
                        vm.projectRoles = data.projectRoles;
                    }
                )
            }

            vm.taskHistoryResources = [];
            function create() {
                if (validateResources()) {
                    var today = new Date();
                    vm.newUnitOfWorkCompleted.timeStamp = moment(today);
                    vm.newUnitOfWorkCompleted.timeStamp = vm.newUnitOfWorkCompleted.timeStamp.format('DD/MM/YYYY, HH:mm:ss');
                    if (vm.newUnitOfWorkCompleted.unitsCompleted <= vm.task.totalUnits) {
                        if ((vm.totalUnitsCompleted + vm.newUnitOfWorkCompleted.unitsCompleted) <= vm.task.totalUnits) {
                            if (vm.newUnitOfWorkCompleted.unitsCompleted == "" || vm.newUnitOfWorkCompleted.unitsCompleted == null) {
                                vm.newUnitOfWorkCompleted.unitsCompleted = 0;
                            }
                            vm.newUnitOfWorkCompleted.completedBy = $rootScope.login.person.id;
                            TaskService.updateTaskCompletion($stateParams.projectId, $stateParams.taskId, vm.newUnitOfWorkCompleted).then(
                                function (data) {
                                    vm.taskHistory = data;
                                    if (vm.taskHistory != null) {
                                        TaskService.createTaskResources(vm.taskId, vm.taskHistory, vm.taskHistoryResources).then(
                                            function (data) {
                                                $rootScope.hideSidePanel();
                                                $rootScope.showSuccessMessage("Task resources added successfully");
                                                $scope.callback(data);
                                            }
                                        )
                                    }

                                }
                            );
                            /*$rootScope.showSuccessMessage("Task Details updated successfully");*/

                        }
                        else {
                            $rootScope.showErrorMessage("Total Units cannot be greater than Total Units completed");
                        }
                    }

                    else {
                        $rootScope.showErrorMessage("Unit of Work cannot be greater than Total Units");

                    }
                }
            }

            function validateResources() {
                vm.valid = true;
                if (vm.persons.length > 0) {
                    angular.forEach(vm.persons, function (person) {
                        var personResources = {
                            task: $stateParams.taskId,
                            taskHistory: null,
                            quantity: 1,
                            resourceType: 'MANPOWER',
                            resourceId: person.id
                        };
                        vm.taskHistoryResources.push(personResources);

                    })

                }
                if (vm.machineItems.length > 0) {
                    angular.forEach(vm.machineItems, function (item) {
                        if (item.machineQty != null) {
                            var machineResources = {
                                task: $stateParams.taskId,
                                taskHistory: null,
                                quantity: item.machineQty,
                                resourceType: 'MACHINE',
                                resourceId: item.id
                            };
                            vm.taskHistoryResources.push(machineResources);
                        }
                    })
                }
                if (vm.materialItems.length > 0) {
                    angular.forEach(vm.materialItems, function (item) {
                        if (item.materialQty != null) {
                            var materialResources = {
                                task: $stateParams.taskId,
                                taskHistory: null,
                                quantity: item.materialQty,
                                resourceType: 'MATERIAL',
                                resourceId: item.id
                            };
                            vm.taskHistoryResources.push(materialResources);
                        }
                    })
                }
                if (vm.projectRoles.length > 0) {
                    angular.forEach(vm.projectRoles, function (item) {
                        if (item.roleQty != null) {
                            var roleResources = {
                                task: $stateParams.taskId,
                                taskHistory: null,
                                quantity: item.roleQty,
                                resourceType: 'ROLE',
                                resourceId: item.id
                            };
                            vm.taskHistoryResources.push(roleResources);
                        }
                    })
                }
                return vm.valid;
            }

            vm.taskCompletionResources = [];
            function loadTaskCompletionResources() {
                /*  TaskService.getTaskCompletionResources(vm.taskId, vm.taskHistory).then(
                 function (data) {
                 if (data.length > 0) {
                 vm.mode = 'EDIT';
                 vm.taskCompletionResources = data;
                 CommonService.getPersonReferences(vm.taskCompletionResources, 'resourceId');
                 ItemService.getItemReferences(vm.taskCompletionResources, 'resourceId');
                 ItemService.getMachineItemReferences(vm.taskCompletionResources, 'resourceId');
                 ProjectService.getRoleReferences($rootScope.task.project, vm.taskCompletionResources, 'resourceId');
                 } else {
                 loadProjectResources()
                 vm.mode = 'Null';
                 }

                 }
                 )*/
            }

            (function () {
                if ($application.homeLoaded == true) {
                    /* if (vm.mode == "EDIT") {
                     loadTaskCompletionResources();
                     }*/
                    loadTaskCompletionResources();
                    loadProjectResources()
                    /*loadProjectResources();*/
                    $rootScope.$on('app.task.resources', create);
                }
            })();
        }
    }
)
;
