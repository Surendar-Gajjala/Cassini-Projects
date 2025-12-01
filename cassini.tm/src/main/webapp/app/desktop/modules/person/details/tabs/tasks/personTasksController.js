/**
 * Created by Anusha on 06-09-2016.
 */
define(['app/desktop/modules/person/person.module',
        'app/shared/services/projectService',
        'app/shared/services/taskService',
        'app/shared/services/shiftService',
        'app/shared/services/personService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'

    ],
    function(module) {
        module.controller('PersonTasksController', PersonTasksController);

        function PersonTasksController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, CommonService,
                                       PersonService, ProjectService, ShiftService, TaskService, DialogService) {
            var vm = this;

            vm.perId = $stateParams.personId;
            vm.personTasks = null;
            vm.loading = true;
            vm.tasks = [];
            vm.deletePersonTask = deletePersonTask;



            function loadPersonTasks() {
                PersonService.getTasksByPersons(vm.perId).then(
                    function (data) {
                        vm.personTasks = data;
                        vm.ids = [];
                        angular.forEach(vm.persons, function (person) {
                            vm.ids.push(person.person);
                        });
                        CommonService.getPersons(vm.ids).then(function (data) {
                            vm.persons = data;
                                angular.forEach(vm.personTasks, function (task) {
                                    getshiftById(task, task.shift);
                                });
                        });
                        setValues(vm.personTasks);
                    }
                )
                vm.loading = false;
            }


            function setValues(tasks) {
                CommonService.getPersonReferences(tasks, 'approvedBy');
                CommonService.getPersonReferences(tasks, 'verifiedBy');
                CommonService.getPersonReferences(tasks, 'assignedTo');
                angular.forEach(tasks, function (task) {
                    ProjectService.getProjectById(task.project).then(function (data) {
                        task.project = data;
                    })
                })
            }

            function getshiftById(task, shiftId) {
                ShiftService.getShiftById(shiftId).then(
                    function (data) {
                        task.shift = data;
                    });
            }

            function deletePersonTask(task) {
                var options = {
                    title: 'Delete PersonTask',
                    message: 'Are you sure you want to delete this PersonTask?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TaskService.deleteProjectTask(task.project.id, task.id).then(
                            function (data) {
                                var index = vm.personTasks.indexOf(task);
                                vm.personTasks.splice(index, 1);
                                $rootScope.showErrorMessage(" Deleted Successfully!");
                                loadPersonTasks();
                            }
                        )
                    }
                });
            }

            (function() {
                if($application.homeLoaded == true) {
                    loadPersonTasks();
                }
            })();
        }
    }
);

