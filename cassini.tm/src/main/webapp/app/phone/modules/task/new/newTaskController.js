define(
    [
        'app/phone/modules/task/task.module',
        'app/shared/services/taskService',
        'app/shared/services/projectService',
        'app/shared/services/personService',
        'app/shared/services/shiftService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function(module) {
        module.controller('NewTaskController', NewTaskController);

        function NewTaskController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application,
                                   $mdDialog, TaskService, ProjectService, CommonService, PersonService, ShiftService) {

            $rootScope.viewName = "New Task";

            var vm = this;

            vm.projects = [];
            vm.locations = [];
            vm.shifts = [];

            vm.staff = [];
            vm.supervisors = [];
            vm.officers = [];

            vm.newTask = {
                project: null,
                projectObject: null,
                name: null,
                description: null,
                status: 'ASSIGNED',
                shift: null,
                shiftObject: null,
                location: null,
                assignedTo: null,
                assignedToObject: null,
                verifiedBy: null,
                verifiedByObject: null,
                approvedBy: null,
                approvedByObject: null,
                assignedDate: null,
                assignedDateObject: null
            };


            vm.create = create;
            vm.cancel = cancel;


            function loadProjects() {
                ProjectService.getProjects().then(
                    function (data) {
                        vm.projects = data.content;
                    });
            }

            function loadLocations() {
                /*
                TaskService.getLocations().then(
                    function (data) {
                        vm.locations = data;
                    });
                */

                CommonService.getLovByName('Default Work Location').then(
                    function (data) {
                        vm.locations = data.values;
                    }
                )
            }

            function loadShifts() {
                ShiftService.getAllShifts().then(
                    function(data){
                        vm.shifts = data;
                    });
            }

            function cancel() {
                window.history.back();
            }

            function loadPersonsByRole() {
                PersonService.getPersonsByRole("Staff").then (
                    function(data) {
                        var personIds = [];
                        angular.forEach(data, function(info) {
                            personIds.push(info.person);
                        });

                        CommonService.getPersons(personIds).then(
                            function(data) {
                                vm.staff = data;
                                $rootScope.sortPersons(vm.staff);
                            }
                        )
                    }
                );
                PersonService.getPersonsByRole("Supervisor").then (
                    function(data) {
                        var personIds = [];
                        angular.forEach(data, function(info) {
                            personIds.push(info.person);
                        });

                        CommonService.getPersons(personIds).then(
                            function(data) {
                                vm.supervisors = data;
                                $rootScope.sortPersons(vm.supervisors);
                            }
                        );
                    }
                );
                PersonService.getPersonsByRole("Officer").then (
                    function(data) {
                        var personIds = [];
                        angular.forEach(data, function(info) {
                            personIds.push(info.person);
                        });

                        CommonService.getPersons(personIds).then(
                            function(data) {
                                vm.officers = data;
                                $rootScope.sortPersons(vm.officers);
                            }
                        );
                    }
                );
            }



            function create() {
                if(validate()) {
                    vm.newTask.project = vm.newTask.projectObject.id;
                    vm.newTask.assignedTo = vm.newTask.assignedToObject.id;
                    vm.newTask.verifiedBy = vm.newTask.verifiedByObject.id;
                    vm.newTask.approvedBy = vm.newTask.approvedByObject.id;
                    vm.newTask.shift = vm.newTask.shiftObject.shiftId;
                    vm.newTask.assignedDate = moment(vm.newTask.assignedDateObject).format("DD/MM/YYYY");

                    TaskService.createProjectTask(vm.newTask.project, vm.newTask).then(
                        function (data) {
                            window.history.back();
                        }
                    );
                }
            }

            function validate() {
                var valid = true;

                if(vm.newTask.projectObject == null) {
                    showMessage("Project has to be selected");
                    valid = false;
                }
                else if(vm.newTask.name == null) {
                    showMessage("Task name is required");
                    valid = false;
                }
                else if(vm.newTask.description == null) {
                    showMessage("Project description is required");
                    valid = false;
                }
                else if(vm.newTask.shiftObject == null) {
                    showMessage("Work shift has to be selected");
                    valid = false;
                }
                else if(vm.newTask.assignedDateObject == null) {
                    showMessage("Assigned date has to be set");
                    valid = false;
                }
                else if(vm.newTask.assignedToObject == null) {
                    showMessage("Assigned to has to be selected");
                    valid = false;
                }
                else if(vm.newTask.verifiedByObject == null) {
                    showMessage("Verified by has to be selected");
                    valid = false;
                }
                else if(vm.newTask.approvedByObject == null) {
                    showMessage("Approved by has to be selected");
                    valid = false;
                }

                return valid;
            }

            function showMessage(message) {
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#newTaskView')))
                        .clickOutsideToClose(true)
                        .title('New Task')
                        .textContent(message)
                        .ariaLabel('Alert Dialog Demo')
                        .ok('Got it!')
                        .targetEvent(event)
                );
            }


            (function() {
                loadProjects();
                loadLocations();
                loadShifts();
                loadPersonsByRole();
            })();
        }
    }
);
