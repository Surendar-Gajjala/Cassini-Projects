define(['app/desktop/modules/task/task.module',
        'app/shared/services/taskService',
        'app/shared/services/projectService',
        'app/shared/services/personService',
        'app/shared/services/shiftService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'

    ],
    function (module) {
        module.controller('NewTaskDialogueController', NewTaskDialogueController);

        function NewTaskDialogueController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                           $application, $uibModalInstance, TaskService, ProjectService,
                                           CommonService, project, PersonService, ShiftService) {
            var vm = this;
            vm.cancel = cancel;
            vm.create = create;
            vm.getassignedpersonsByRole = getassignedpersonsByRole;
            vm.getSuperVisors = getSuperVisors;
            vm.getOfficers = getOfficers;
            vm.locationValid = locationValid;


            vm.project = project;
            vm.projects = [];
            vm.persons = [];
            vm.newlocations = [];
            vm.shifts = [];
            /*vm.locations = ["Hut-1 ", "Hut-2", "Hut-3 ", "Hut-4", "Hut-5 ", "Hut-6", "Hut-7", "Hut-8", "Hut-9" , "Hut-10","Goomty-1","Goomty-2", "Goomty-3", "Goomty-4", "Goomty-5", "Goomty-6", "Goomty-7", "Goomty-8", "Goomty-9", "Goomty-10", "Goomty-11", "Goomty-12", "Goomty-13", "Goomty-14","Relay Room","Panel Room","G Cabin","F Cabin","VNC","NWBH","D Cabin","WI Cabin","Bulb Cabin"];
             vm.locations.unshift(
             "New Location..."
             );*/
            vm.locations = null;
            vm.newLocation = null;
            vm.staffs = [];
            vm.supervisors = [];
            vm.officers = [];
            vm.administrator = [];

            vm.errorMessage = ({
                project: null,
                name: null,
                assignedDate: null,
                description: null,
                location: null,
                newLocation: null,
                assignedTo: null,
                verifiedBy: null,
                approvedBy: null
            })

            vm.statuses = [
                "ASSIGNED",
                "PENDING",
                "FINISHED",
                "VERIFIED",
                "APPROVED",
                "REJECTED"
            ];

            vm.newTask = {
                project: null,
                name: null,
                description: null,
                shift: null,
                status: 'ASSIGNED',
                location: null,
                assignedTo: null,
                verifiedBy: null,
                approvedBy: null,
                assignedDate: null
            };

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            function getProjectById() {
                ProjectService.getProjectById(vm.project.id).then(
                    function (data) {
                        vm.project = data;
                    }
                )
            }

            function validateTask() {

                vm.errorMessage = ({
                    project: null,
                    name: null,
                    assignedDate: null,
                    description: null,
                    location: null,
                    newLocation: null,
                    assignedTo: null,
                    verifiedBy: null,
                    approvedBy: null
                })

                vm.valid = false;

                if (vm.newTask.project == null || vm.newTask.project == undefined || vm.newTask.project == "") {
                    vm.errorMessage.project = "Select Required project"
                } else if (vm.newTask.name == null || vm.newTask.name == undefined || vm.newTask.name == "") {
                    vm.errorMessage.name = "Enter Task Name"
                } else if (vm.newTask.assignedDate == null || vm.newTask.assignedDate == undefined || vm.newTask.assignedDate == "") {
                    vm.errorMessage.assignedDate = "Select Assigned Date"
                } else if (vm.newTask.description == null || vm.newTask.description == undefined || vm.newTask.description == "") {
                    vm.errorMessage.description = "Description is Required"
                } else if (vm.newTask.location == null || vm.newTask.location == undefined || vm.newTask.location == "") {
                    vm.errorMessage.location = "Location is Required"
                } else if (vm.newTask.assignedTo == null || vm.newTask.assignedTo == undefined || vm.newTask.assignedTo == "") {
                    vm.errorMessage.assignedTo = "Select Task Assign person"
                } else if (vm.newTask.verifiedBy == null || vm.newTask.verifiedBy == undefined || vm.newTask.verifiedBy == "") {
                    vm.errorMessage.verifiedBy = "Select Task verify person"
                } else if (vm.newTask.approvedBy == null || vm.newTask.approvedBy == undefined || vm.newTask.approvedBy == "") {
                    vm.errorMessage.approvedBy = "Select Task approve person"
                } else if (vm.newTask.location != "New Location...") {
                    vm.newLocation = vm.newTask.location;
                    vm.valid = true;
                } else {
                    vm.valid = true;
                }
                return vm.valid;
            }

            function getShifts() {
                ShiftService.getAllShifts().then(
                    function (data) {
                        vm.shifts = data;
                    });
            }

            function create() {

                if (validateTask() && locationValid(vm.newLocation)) {
                    if (vm.newTask.location == "New Location...") {
                        vm.newTask.location = vm.newLocation;
                    }

                    if (vm.newTask.project != null || vm.newTask.project != undefined) {
                        vm.newTask.project = vm.newTask.project.id;
                    }

                    if (vm.newTask.approvedBy != null || vm.newTask.approvedBy != undefined) {
                        vm.newTask.approvedBy = vm.newTask.approvedBy.id;
                    }

                    if (vm.newTask.assignedTo != null || vm.newTask.assignedTo != undefined) {
                        vm.newTask.assignedTo = vm.newTask.assignedTo.id;
                    }

                    if (vm.newTask.verifiedBy != null || vm.newTask.verifiedBy != undefined) {
                        vm.newTask.verifiedBy = vm.newTask.verifiedBy.id;
                    }

                    $uibModalInstance.close(vm.newTask);
                }
            }

            function loadProjects() {
                ProjectService.getProjects().then(
                    function (data) {
                        vm.projects = data;
                        if (vm.projects.content.length == 1) {
                            vm.project = vm.projects.content[0];
                            vm.newTask.project = vm.project;
                        }
                    });

            }

            function getassignedpersonsByRole() {
                PersonService.getPersonsByRole("Staff").then(
                    function (data) {
                        angular.forEach(data, function (obj) {
                            loadStaffPersons(obj);
                        });
                    });
                getAdminstratorByRole();
            }

            function loadStaffPersons(obj) {
                CommonService.getPerson(obj.person).then(
                    function (data) {
                        var staff = data;
                        vm.staffs.push(staff);
                    });
            }

            function getAdminstratorByRole() {
                PersonService.getPersonsByRole("Administrator").then(
                    function (data) {
                        angular.forEach(data, function (obj) {
                            loadAdministrator(obj);
                        });
                    });
            }

            function loadAdministrator(obj) {
                CommonService.getPerson(obj.person).then(
                    function (data) {
                        var admin = data;
                        vm.staffs.push(admin);
                        vm.supervisors.push(admin);
                        vm.officers.push(admin);
                    });
            }

            function getSuperVisors() {
                PersonService.getPersonsByRole("Supervisor").then(
                    function (data) {
                        angular.forEach(data, function (obj) {
                            loadSupervisors(obj);
                        });
                    });
            }

            function loadSupervisors(obj) {
                CommonService.getPerson(obj.person).then(
                    function (data) {
                        var supervisor = data;
                        vm.supervisors.push(supervisor);
                    });
            }


            function getOfficers() {
                PersonService.getPersonsByRole("Officer").then(
                    function (data) {
                        angular.forEach(data, function (obj) {
                            loadOfficers(obj);
                        });
                    });
            }

            function loadOfficers(obj) {
                CommonService.getPerson(obj.person).then(
                    function (data) {
                        var officer = data;
                        vm.officers.push(officer);
                    });
            }

            /*    function loadLocations() {
             TaskService.getLocations().then(
             function (data) {
             vm.newlocations = data;
             angular.forEach(vm.newlocations , function(newlocation){
             if(vm.locations != newlocation ){
             var location = newlocation;
             vm.locations.push(location);
             }
             });
             });
             }
             */

            function loadLocations() {
                CommonService.getLovByName('Default Work Location').then(
                    function (data) {
                        vm.locations = data.values;
                    }
                )
            }

            function locationValid(location) {
                vm.valid = false;
                if (location == null || location == "" || location == undefined || location == "New Location...") {
                    vm.errorMessage.newLocation = "New Location is Required"
                } else {
                    vm.valid = true;
                }

                return vm.valid;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadProjects();
                    getassignedpersonsByRole();
                    getSuperVisors();
                    getOfficers();
                    loadLocations();
                    getShifts();
                    if (vm.project != null) {
                        getProjectById();
                        vm.newTask.project = vm.project;
                    }
                }
            })();
        }
    }
)
;