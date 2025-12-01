define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/programService'
    ],
    function (module) {
        module.controller('ProgramProjectsDrillDownController', ProgramProjectsDrillDownController);

        function ProgramProjectsDrillDownController($scope, $rootScope, $state, $timeout, $sce, $window, $stateParams, $translate,
                                                    DialogService, ProgramService, ProjectService, CommonService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.loading = true;
            vm.programId = $stateParams.programId;
            var newProject = parsed.html($translate.instant("NEW_PROJECT")).html();
            var groupSaveMsg = parsed.html($translate.instant("PROGRAM_GROUP_SAVED_MSG")).html();
            var groupUpdateMsg = parsed.html($translate.instant("PROGRAM_GROUP_UPDATED_MSG")).html();
            var nameValidation = parsed.html($translate.instant("PLEASE_ENTER_NAME")).html();
            $rootScope.external = $rootScope.loginPersonDetails;
            var emptyProgramProject = {
                id: null,
                program: vm.programId,
                name: null,
                description: null,
                parent: null,
                project: null,
                type: 'GROUP',
                objectType: "GROUP",
                validUser: true
            };
            var emptyProjectType = {
                id: null,
                name: null,
                description: null,
                parent: null,
                project: null,
                sequenceNumber: null,
                plannedFinishDate: null,
                plannedStartDate: null,
                actualFinishDate: null,
                actualStartDate: null,
                assignedTo: null,
                status: null,
                percentComplete: 0.0,
                workflow: null,
                activity: null,
                required: false,
                wbs: null,
                type: null,
                duration: null,
                validUser: true,
                overDue: false,
                objectType: "GROUP"
            };

            $rootScope.loadProgramProjects = loadProgramProjects;
            function loadProgramProjects() {
                vm.loading = true;
                vm.searchText = "";
                vm.programProjects = [];
                $rootScope.showBusyIndicator();
                ProgramService.getDrillDownProgramProjects(vm.programId).then(
                    function (data) {
                        vm.programProjects = [];
                        angular.forEach(data, function (programProject) {
                            programProject.editMode = false;
                            programProject.isNew = false;
                            programProject.programChildren = [];
                            programProject.level = 0;
                            programProject.expanded = true;
                            programProject.parentObject = null;
                            vm.programProjects.push(programProject);
                            var index = vm.programProjects.indexOf(programProject);

                            angular.forEach(programProject.children, function (child) {
                                index++;
                                child.editMode = false;
                                child.isNew = false;
                                child.level = programProject.level + 1;
                                child.expanded = false;
                                child.groupName = programProject.name;
                                child.programChildren = [];
                                child.parentObject = programProject;
                                programProject.programChildren.push(child);
                                vm.programProjects.splice(index, 0, child);
                            })
                        })
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            $rootScope.loadProgramProjectsBySearchQuery = loadProgramProjectsBySearchQuery;
            function loadProgramProjectsBySearchQuery(freeText) {
                vm.loading = true;
                vm.programProjects = [];
                vm.searchText = freeText;
                ProgramService.getProgramProjects(vm.programId).then(
                    function (data) {
                        vm.programProjects = [];
                        angular.forEach(data, function (programProject) {
                            programProject.searchExist = false;
                            programProject.childCount = 0;
                            if (programProject.name.toLowerCase().includes(freeText.toLowerCase())) {
                                programProject.searchExist = true;
                            }
                            if (programProject.description != null && programProject.description != "" && programProject.description.toLowerCase().includes(freeText.toLowerCase())) {
                                programProject.searchExist = true;
                            }

                            angular.forEach(programProject.children, function (child) {
                                child.searchExist = false;
                                if (child.name.toLowerCase().includes(freeText.toLowerCase())) {
                                    child.searchExist = true;
                                }
                                if (child.description != null && child.description != "" && child.description.toLowerCase().includes(freeText.toLowerCase())) {
                                    child.searchExist = true;
                                }
                            });

                            angular.forEach(programProject.children, function (child) {
                                if (child.searchExist) {
                                    programProject.childCount++;
                                    programProject.searchExist = true;
                                }
                            })
                        });
                        vm.programProjects = [];
                        angular.forEach(data, function (programProject) {
                            if (programProject.searchExist) {
                                programProject.editMode = false;
                                programProject.isNew = false;
                                programProject.programChildren = [];
                                programProject.level = 0;
                                programProject.expanded = true;
                                vm.programProjects.push(programProject);
                                var index = vm.programProjects.indexOf(programProject);

                                angular.forEach(programProject.children, function (child) {
                                    if (child.searchExist || programProject.childCount == 0) {
                                        index++;
                                        child.editMode = false;
                                        child.isNew = false;
                                        child.level = programProject.level + 1;
                                        child.expanded = false;
                                        child.groupName = programProject.name;
                                        programProject.programChildren.push(child);
                                        vm.programProjects.splice(index, 0, child);
                                    }
                                })
                            }
                        });
                        vm.loading = false;
                    }
                );
            }

            vm.openProjectDetails = openProjectDetails;
            function openProjectDetails(programProject) {
                $window.localStorage.setItem("shared-permission", "");
                if (programProject.objectType == "PROJECT") {
                    $window.localStorage.setItem("project_open_from", vm.programId);
                    if (programProject.plannedStartDate != null && programProject.plannedStartDate != "" && programProject.plannedFinishDate != null && programProject.plannedFinishDate != "") {
                        $state.go('app.pm.project.details', {projectId: programProject.project, tab: 'details.plan'});
                    } else {
                        $state.go('app.pm.project.details', {projectId: programProject.project, tab: 'details.basic'});
                    }
                } else if (programProject.objectType == "PROJECTACTIVITY") {
                    $window.localStorage.setItem("activity_open_from", vm.programId);
                    $state.go('app.pm.project.activity.details', {activityId: programProject.id, tab: 'details.basic'});
                } else {
                    $rootScope.projectId = programProject.project;
                    $window.localStorage.setItem("task_open_from", vm.programId);
                    $state.go('app.pm.project.activity.task.details', {
                        activityId: programProject.activity,
                        taskId: programProject.id,
                        tab: 'details.basic'
                    });
                }
            }

            var create = parsed.html($translate.instant("CREATE")).html();
            vm.addProjects = addProjects;
            function addProjects(programProject) {
                var options = {
                    title: newProject,
                    template: 'app/desktop/modules/pm/project/new/newProjectDialog.jsp',
                    controller: 'NewProjectController as newProjectVm',
                    resolve: 'app/desktop/modules/pm/project/new/newProjectDialogController',
                    width: 550,
                    showMask: true,
                    data: {
                        projectCreationFrom: "PROGRAM",
                        selectedProgram: $rootScope.programInfo,
                        selectedProgramGroup: programProject
                    },
                    buttons: [
                        {text: create, broadcast: 'app.project.new'}
                    ],
                    callback: function () {
                        loadProgramProjects();
                        loadProgramManagers();
                        $rootScope.loadProgramCounts();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var deleteMessage = parsed.html($translate.instant("PROJECT_DELETED_MESSAGE")).html();
            var programGroupDeletedMessage = parsed.html($translate.instant("PROGRAM_GROUP_DELETED_MESSAGE")).html();
            var deleteDialogueMsg = parsed.html($translate.instant("PROGRAM_PROJECT_DIALOG_MSG")).html();
            var deleteDialogueMsg1 = parsed.html($translate.instant("PROGRAM_PROJECT_DELETE_DIALOG_MSG")).html();
            var deleteDialogueTitle = parsed.html($translate.instant("DELETE_PROJECT_TITLE")).html();
            var deleteGroupTitle = parsed.html($translate.instant("DELETE_GROUP")).html();
            var projectsAddedMsg = parsed.html($translate.instant("PROJECTS_ADDED_MSG")).html();
            vm.addProjectsTitle = parsed.html($translate.instant("ADD_PROJECTS")).html();
            var addTitle = parsed.html($translate.instant("ADD")).html();

            vm.deleteProgramProject = deleteProgramProject;
            function deleteProgramProject(programProject) {
                var dialogTitle = deleteGroupTitle;
                var dialogMsg = deleteDialogueMsg.format(programProject.name, "group");
                if (programProject.objectType == "PROJECT") {
                    dialogMsg = deleteDialogueMsg1.format(programProject.name, "from", programProject.groupName);
                    dialogTitle = deleteDialogueTitle;
                }
                var options = {
                    title: dialogTitle,
                    message: dialogMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProgramService.deleteProgramProject($stateParams.programId, programProject.id).then(
                            function (data) {
                                if (programProject.objectType == "GROUP") {
                                    $rootScope.showSuccessMessage(programGroupDeletedMessage);
                                } else {
                                    $rootScope.showSuccessMessage(deleteMessage);
                                }
                                if (programProject.parentObject != null && programProject.parentObject != undefined) {
                                    programProject.parentObject.childCount = programProject.parentObject.childCount - 1;
                                    angular.forEach(programProject.parentObject.programChildren, function (project) {
                                        if (project.id == programProject.id) {
                                            programProject.parentObject.programChildren.splice(programProject.parentObject.programChildren.indexOf(project), 1);
                                        }
                                    })
                                    angular.forEach(programProject.parentObject.children, function (project) {
                                        if (project.id == programProject.id) {
                                            programProject.parentObject.children.splice(programProject.parentObject.children.indexOf(project), 1);
                                        }
                                    })
                                }
                                removeChildren(programProject);
                                vm.programProjects.splice(vm.programProjects.indexOf(programProject), 1);
                                loadProgramManagers();
                                $rootScope.loadProgramCounts();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            vm.saveProgramProject = saveProgramProject;
            function saveProgramProject(programProject) {
                if (validate(programProject)) {
                    $rootScope.showBusyIndicator();
                    if (programProject.isNew) {
                        ProgramService.createProgramProjectObject($stateParams.programId, programProject).then(
                            function (data) {
                                programProject.id = data.id;
                                programProject.editMode = false;
                                programProject.isNew = false;
                                programProject.programChildren = [];
                                $rootScope.showSuccessMessage(groupSaveMsg);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        var children = programProject.children;
                        var programChildren = programProject.programChildren;
                        programProject.children = [];
                        programProject.programChildren = [];
                        ProgramService.updateProgramProjectObject($stateParams.programId, programProject).then(
                            function (data) {
                                programProject.id = data.id;
                                programProject.validUser = data.validUser;
                                programProject.assignedToName = data.assignedToName;
                                programProject.editMode = false;
                                programProject.isNew = false;
                                programProject.children = children;
                                programProject.programChildren = programChildren;
                                $rootScope.showSuccessMessage(groupUpdateMsg);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                programProject.children = children;
                                programProject.programChildren = programChildren;
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function validate(programProject) {
                var valid = true;
                if (programProject.name == null || programProject.name == "" || programProject.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                }
                return valid;
            }

            vm.addGroup = addGroup;
            function addGroup() {
                var programProject = angular.copy(emptyProgramProject);
                programProject.editMode = true;
                programProject.isNew = true;
                vm.programProjects.push(programProject);
            }

            vm.removeProgramProject = removeProgramProject;
            function removeProgramProject(programProject) {
                vm.programProjects.splice(vm.programProjects.indexOf(programProject), 1);
            }

            vm.editProgramProject = editProgramProject;
            function editProgramProject(programProject) {
                programProject.editMode = true;
                programProject.oldName = programProject.name;
                programProject.oldDescription = programProject.description;
                programProject.oldPlannedStartDate = programProject.plannedStartDate;
                programProject.oldPlannedFinishDate = programProject.plannedFinishDate;
                programProject.oldAssignedTo = programProject.assignedTo;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(programProject) {
                programProject.editMode = false;
                programProject.name = programProject.oldName;
                programProject.description = programProject.oldDescription;
                programProject.plannedStartDate = programProject.oldPlannedStartDate;
                programProject.plannedFinishDate = programProject.oldPlannedFinishDate;
                programProject.assignedTo = programProject.oldAssignedTo;
            }

            vm.programProjectFilter = {
                name: null,
                type: null,
                programManager: '',
                description: null,
                searchQuery: null
            };

            $scope.clearProgramManager = clearProgramManager;
            function clearProgramManager() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedPerson = null;
                vm.searchManager = null;
                loadProgramProjects();
            }

            $scope.programManagers = [];
            function loadProgramManagers() {
                ProgramService.getProgramProjectAssignedTos(vm.programId).then(
                    function (data) {
                        $scope.programManagers = data;
                    }
                )
            }

            $scope.selectedPerson = null;
            $scope.onSelectProgramManager = onSelectProgramManager;
            function onSelectProgramManager(person) {
                $scope.selectedPerson = person;
                vm.searchManager = person.fullName;
                vm.loading = true;
                vm.programProjects = [];
                $rootScope.showBusyIndicator();
                ProgramService.getProgramProjectPlanByAssignedTo(vm.programId, person.id).then(
                    function (data) {
                        vm.programProjects = [];
                        angular.forEach(data, function (programProject) {
                            programProject.editMode = false;
                            programProject.isNew = false;
                            programProject.programChildren = [];
                            programProject.level = 0;
                            programProject.expanded = true;
                            programProject.childCount = 0;
                            vm.programProjects.push(programProject);
                            var index = vm.programProjects.indexOf(programProject);

                            angular.forEach(programProject.children, function (child) {
                                index++;
                                child.editMode = false;
                                child.isNew = false;
                                child.level = programProject.level + 1;
                                child.groupName = programProject.name;
                                child.programChildren = [];
                                child.expanded = true;
                                programProject.childCount++;
                                programProject.programChildren.push(child);
                                vm.programProjects.splice(index, 0, child);
                                index = vm.programProjects.indexOf(child);
                                index = populateChildren(child, index);
                            })
                        });
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            vm.toggleNode = toggleNode;
            function toggleNode(programProject) {
                if (programProject.expanded == null || programProject.expanded == undefined) {
                    programProject.expanded = false;
                }
                programProject.expanded = !programProject.expanded;
                var index = vm.programProjects.indexOf(programProject);
                if (programProject.expanded == false) {
                    removeChildren(programProject)
                } else {
                    if (programProject.projectType != "PROJECT") {
                        angular.forEach(programProject.children, function (child) {
                            index++;
                            child.editMode = false;
                            child.isNew = false;
                            child.level = programProject.level + 1;
                            child.expanded = false;
                            child.groupName = programProject.name;
                            programProject.programChildren.push(child);
                            vm.programProjects.splice(index, 0, child);
                        })
                    } else {
                        $rootScope.showBusyIndicator();
                        ProgramService.getProgramProjectPlan(vm.programId, programProject.project).then(
                            function (data) {
                                angular.forEach(data, function (item) {
                                    item.editMode = false;
                                    item.isNew = false;
                                    item.programChildren = [];
                                    item.level = 2;
                                    item.expanded = true;
                                    item.childCount = 0;
                                    programProject.programChildren.push(item);
                                    vm.programProjects.splice(index + 1, 0, item);
                                    index = vm.programProjects.indexOf(item);
                                    index = populateChildren(item, index);
                                });
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function populateChildren(programProject, lastIndex) {
                angular.forEach(programProject.children, function (item) {
                    lastIndex++;
                    item.expanded = true;
                    item.editMode = false;
                    item.level = programProject.level + 1;
                    item.programChildren = [];
                    item.childCount = 0;
                    if (item.objectType == "PROJECTTASK" || (item.objectType == "PROJECTACTIVITY" && item.children.length == 0)) {
                        var today = moment(new Date());
                        var todayStr = today.format($rootScope.applicationDateSelectFormat);
                        var todayDate = moment(todayStr, $rootScope.applicationDateSelectFormat);
                        var finishDate = moment(item.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                        if (todayDate.isAfter(finishDate)) item.overDue = true;
                    }
                    programProject.childCount++;
                    vm.programProjects.splice(lastIndex, 0, item);
                    programProject.programChildren.push(item);
                    lastIndex = populateChildren(item, lastIndex)
                });

                return lastIndex;
            }


            function removeChildren(programProject) {
                if (programProject != null && programProject.programChildren != null && programProject.programChildren != undefined) {
                    angular.forEach(programProject.programChildren, function (item) {
                        removeChildren(item);
                    });

                    var index = vm.programProjects.indexOf(programProject);
                    vm.programProjects.splice(index + 1, programProject.programChildren.length);
                    programProject.programChildren = [];
                    programProject.expanded = false;

                }
            }

            $scope.onSelectAssignedTo = onSelectAssignedTo;
            function onSelectAssignedTo(person, programProject) {
                programProject.assignedToName = person.personObject.fullName;
            }

            $scope.types = ["Activity", "Milestone"];
            $scope.onSelectType = onSelectType;
            function onSelectType(type, programProject) {
                if (type == "Activity") {
                    programProject.typeName = "Activity";
                    programProject.objectType = "PROJECTACTIVITY";
                } else {
                    programProject.typeName = "Milestone";
                    programProject.objectType = "PROJECTMILESTONE";
                }
            }

            function loadProjectResources() {
                $rootScope.showBusyIndicator();
                ProgramService.getProgramResources($stateParams.programId).then(
                    function (data) {
                        $scope.selectPersons = data;
                        angular.forEach($scope.selectPersons, function (person) {
                            person.editMode = false;
                        });
                        CommonService.getPersonReferences($scope.selectPersons, 'person');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.openFilesView = openFilesView;
            function openFilesView(object) {
                if (object.objectType == "PROJECTACTIVITY") {
                    $window.localStorage.setItem("activity_open_from", vm.programId);
                    $state.go('app.pm.project.activity.details', {activityId: object.id, tab: 'details.files'});
                } else {
                    $window.localStorage.setItem("task_open_from", vm.programId);
                    $state.go('app.pm.project.activity.task.details', {
                        activityId: object.activity,
                        taskId: object.id,
                        tab: 'details.files'
                    });
                }
            }

            (function () {
                $scope.$on('app.program.tabactivated', function (event, data) {
                    if (data.tabId == 'details.project') {
                        $rootScope.freeTextQuerys = null;
                        $window.localStorage.setItem("project_open_from", null);
                        $window.localStorage.setItem("activity_open_from", null);
                        $window.localStorage.setItem("task_open_from", null);
                        loadProgramProjects();
                        loadProgramManagers();
                        loadProjectResources();
                    }
                });
            })();
        }
    }
)
;