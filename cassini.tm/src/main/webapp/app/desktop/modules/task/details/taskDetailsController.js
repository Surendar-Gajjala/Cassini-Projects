define(['app/desktop/modules/task/task.module',
        'app/shared/services/taskService',
        'app/shared/services/projectService',
        'app/shared/services/shiftService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/personService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/noteService'
    ],
    function (module) {
        module.controller('TaskDetailsController', TaskDetailsController);

        function TaskDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                       $application, TaskService, ProjectService, CommonService,
                                       LoginService, PersonService, NoteService, ShiftService) {

            $rootScope.viewInfo.icon = "fa fa-tasks";
            $rootScope.viewInfo.title = "Task Details";

            var vm = this;
            vm.back = back;
            vm.loading = true;
            vm.task = null;
            vm.taskHistory = [];
            vm.updateTask = updateTask;
            vm.loadPendingReason = loadPendingReason;
            vm.updateStatus = updateStatus;
            vm.createReason = createReason;
            vm.addNote = addNote;
            vm.noteDetails = null;
            vm.buttonName = null;
            vm.pendingButton = null;
            vm.taskId = $stateParams.taskId;
            vm.persons = [];
            vm.historyLoading = true;
            vm.notesLoading = true;
            vm.newReason = null;
            vm.staffs = [];
            vm.supervisors = [];
            vm.officers = [];
            vm.statuses = [
                'ASSIGNED',
                'PENDING',
                'FINISHED',
                'VERIFIED',
                'APPROVED',
                'REJECTED'
            ];
            vm.reasons = []

            vm.projectId = $stateParams.projectId;
            vm.notes = [];

            var emptyNote = {
                objectType: "PROJECTTASK",
                objectId: vm.taskId,
                title: "Project Task",
                details: null
            };

            vm.newNote = angular.copy(emptyNote);
            var login = null;
            var statusValue = null;

            function loadPendingReason() {
                TaskService.getReasons().then(
                    function (data) {
                        vm.reasons = data;
                        var newReason = {
                            reason: "New Reason..."
                        }
                        vm.reasons.unshift(newReason);
                    }
                )
            }

            function addNote(note) {
                vm.newNote.details = note;
                NoteService.createNote(vm.newNote).then(
                    function (note) {
                        CommonService.getPersonReferences([note], 'createdBy');
                        CommonService.getPersonReferences([note], 'modifiedBy');
                        vm.notes.push(note);
                        vm.newNote = angular.copy(emptyNote);
                        vm.noteDetails = null;
                        vm.showNote = false;
                    }
                )
            }

            function createReason() {
                TaskService.createPendingReason(vm.newReason).then(
                    function (data) {
                        vm.task.pendingReason = data;
                    }
                );
            }

            function loadTaskHistory() {
                TaskService.getTaskHistory(vm.projectId, vm.taskId).then(
                    function (data) {
                        vm.taskHistory = data;
                        vm.historyLoading = false;
                        angular.forEach(vm.taskHistory, function (person) {
                            CommonService.getPerson(person.updatedBy).then(
                                function (data) {
                                    person.updatedBy = data.firstName;
                                }
                            );
                        })
                    }
                )
            }

            function loadTaskDetails() {
                TaskService.getProjectTask(vm.projectId, vm.taskId).then(
                    function (data) {
                        vm.task = data;
                        vm.loading = false;
                        $rootScope.viewInfo.title = "Task Details (" + vm.task.name + ")";
                        if (vm.task.pendingReason != null) {
                            TaskService.getPendingReasonById(vm.task.pendingReason).then(
                                function (data) {
                                    vm.task.pendingReason = data;
                                }
                            )
                        }
                        if (vm.task.note == null) {
                            vm.task.note = "Click Here To Enter Note";
                        }

                        var assignedDate = moment(vm.task.assignedDate, 'DD/MM/YYYY');
                        var today = moment(new Date());
                        var todayStr = today.format('DD/MM/YYYY');
                        today = moment(todayStr, 'DD/MM/YYYY');
                        var flag = assignedDate.isSameOrBefore(today);
                        if (flag) {
                            assignButtonName();
                        }

                        ShiftService.getShiftById(vm.task.shift).then(
                            function (data) {
                                vm.task.shiftObject = data;
                            }
                        );

                        loadTaskHistory();
                        loadNotes();
                        setValues();
                    }
                )
            }

            function loadNotes() {
                NoteService.getNotes('PROJECTTASK', vm.taskId).then(
                    function (notes) {
                        vm.notes = notes;
                        CommonService.getPersonReferences(vm.notes, 'createdBy');
                        CommonService.getPersonReferences(vm.notes, 'modifiedBy');
                        vm.notesLoading = false;
                    }
                )
            }

            function setValues() {
                CommonService.getPersonReferences([vm.task], 'approvedBy');
                CommonService.getPersonReferences([vm.task], 'verifiedBy');
                CommonService.getPersonReferences([vm.task], 'assignedTo');
                ProjectService.getProjectById(vm.task.project).then(function (data) {
                    vm.task.projectObject = data;
                    vm.loading = false;
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

            function updateStatus(action) {
                //var status = "ASSIGNED";
                if (action == 'Finish') {
                    vm.task.note = null;
                    vm.task.status = "FINISHED";
                }
                else if (action == 'Verify') {
                    vm.task.status = "VERIFIED";
                }
                else if (action == 'Approve') {
                    vm.task.status = "APPROVED";
                }

                else if (action == 'Pending') {
                    vm.showNote = true;
                    if (vm.task.status == "ASSIGNED") {
                        vm.task.status = "FINISHEDPENDING";
                    }
                    else if (vm.task.status == "FINISHED") {
                        vm.task.status = "VERIFIEDPENDING";
                    }
                    else if (vm.task.status == "VERIFIED") {
                        vm.task.status = "APPROVEDPENDING";
                    }
                }
                if (vm.task.status != 'PENDING') {
                    updateTask();
                } else {
                    assignButtonName();
                }
            }

            function updateTask() {
                vm.task.shift = vm.task.shiftObject.shiftId;
                vm.task.assignedTo = vm.task.assignedToObject.id;
                vm.task.approvedBy = vm.task.approvedByObject.id;
                vm.task.verifiedBy = vm.task.verifiedByObject.id;
                if (vm.task.pendingReason != null || vm.task.pendingReason != undefined) {
                    vm.task.pendingReason = vm.task.pendingReason.reasonId;
                }
                if (vm.task.note == "Click Here To Enter Note") {
                    vm.task.note = null;
                }
                TaskService.updateProjectTask(vm.task.project, vm.task).then(
                    function (data) {
                        vm.task = data;
                        setValues();
                        $rootScope.showSuccessMessage("Task Updated Successfully!");
                        assignButtonName();
                        loadTaskDetails();
                    }
                )

            }

            function back() {
                $window.history.back();
            }

            function assignButtonName() {
                statusValue = vm.task.status;
                login = $application.login.person;
                PersonService.getPersonOtherInfoByPersonId(login.id).then(
                    function (data) {
                        if (data != null && data != "") {
                            login.otherInfo = data;
                        }
                    }
                );
                if (statusValue == "ASSIGNED" && (vm.task.assignedTo == login.id || login.id == 1 || login.otherInfo.role == "Administrator")) {
                    vm.buttonName = "Finish";
                    vm.pendingButton = "Pending";
                } else if (statusValue == "FINISHED" &&
                    (vm.task.approvedBy == login.id || vm.task.verifiedBy == login.id || login.id == 1 || login.otherInfo.role == "Administrator")) {
                    vm.buttonName = "Verify";
                    vm.pendingButton = "Verify Pending";
                } else if (statusValue == "VERIFIED" && (vm.task.approvedBy == login.id || login.id == 1 || login.otherInfo.role == "Administrator")) {
                    vm.buttonName = "Approve";
                    vm.pendingButton = "Approve Pending";
                } else if (statusValue == "FINISHEDPENDING" && (vm.task.assignedTo == login.id || login.id == 1 || login.otherInfo.role == "Administrator")) {
                    vm.buttonName = "Finish";
                    vm.pendingButton = null;
                } else if (statusValue == "VERIFIEDPENDING" &&
                    (vm.task.verifiedBy == login.id || vm.task.approvedBy == login.id || login.id == 1 || login.otherInfo.role == "Administrator")) {
                    vm.buttonName = "Verify";
                    vm.pendingButton = null;
                } else if (statusValue == "APPROVEDPENDING" &&
                    (vm.task.approvedBy == login.id || login.id == 1 || login.otherInfo.role == "Administrator")) {
                    vm.buttonName = "Approve";
                    vm.pendingButton = null;
                } else {
                    vm.buttonName = null;
                    vm.pendingButton = null;
                }
                /*if (statusValue == "ASSIGNED" && (vm.task.assignedTo == login.id || login.id == 1 || login.otherInfo.role == "Administrator")) {
                 vm.pendingButton = "Pending";
                 } else if ((statusValue == "FINISHEDPENDING" || statusValue == "FINISHED") &&
                 (vm.task.approvedBy == login.id || vm.task.verifiedBy == login.id || login.id == 1 || login.otherInfo.role == "Administrator")) {
                 vm.pendingButton = "Verify Pending";
                 } else if ((statusValue == "VERIFIEDPENDING" || statusValue == "VERIFIED") &&
                 (vm.task.approvedBy == login.id || login.id == 1 || login.otherInfo.role == "Administrator")) {
                 vm.pendingButton = "Approve Pending";
                 } else {
                 vm.pendingButton = null;
                 }*/
                //loadTaskHistory();
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadPendingReason();
                    loadTaskDetails();
                    getassignedpersonsByRole();
                    getSuperVisors();
                    getOfficers();
                }
            })();
        }
    }
);