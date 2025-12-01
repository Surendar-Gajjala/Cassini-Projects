define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/programService'
    ],
    function (module) {
        module.controller('ProgramProjectsController', ProgramProjectsController);

        function ProgramProjectsController($scope, $rootScope, $state, $timeout, $sce, $window, $stateParams, $translate,
                                           DialogService, ProgramService, ProjectService) {
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
                type: 'GROUP'
            };

            $rootScope.loadProgramProjects = loadProgramProjects;
            function loadProgramProjects() {
                vm.loading = true;
                vm.searchText = "";
                vm.programProjects = [];
                ProgramService.getProgramProjects(vm.programId).then(
                    function (data) {
                        vm.programProjects = [];
                        angular.forEach(data, function (programProject) {
                            programProject.editMode = false;
                            programProject.isNew = false;
                            programProject.programChildren = [];
                            programProject.level = 0;
                            programProject.expanded = true;
                            vm.programProjects.push(programProject);
                            var index = vm.programProjects.indexOf(programProject);

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
                        })
                        vm.loading = false;
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

            $rootScope.loadProgramProjectsByProgramManager = loadProgramProjectsByProgramManager;
            function loadProgramProjectsByProgramManager(programManager) {
                vm.loading = true;
                vm.programProjects = [];
                ProgramService.getProgramProjects(vm.programId).then(
                    function (data) {
                        vm.programProjects = [];
                        angular.forEach(data, function (programProject) {
                            programProject.searchExist = false;

                            angular.forEach(programProject.children, function (child) {
                                child.searchExist = false;
                                if (child.projectManager == programManager) {
                                    child.searchExist = true;
                                }
                            });

                            angular.forEach(programProject.children, function (child) {
                                if (child.searchExist) {
                                    programProject.searchExist = true;
                                }
                            })
                        });
                        $timeout(function () {
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
                                        if (child.searchExist) {
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
                            })
                            vm.loading = false;
                        }, 1000);
                    }
                );
            }

            vm.openProjectDetails = openProjectDetails;
            function openProjectDetails(programProject) {
                $window.localStorage.setItem("project_open_from", vm.programId);
                if (programProject.plannedStartDate != null && programProject.plannedStartDate != "" && programProject.plannedFinishDate != null && programProject.plannedFinishDate != "") {
                    $state.go('app.pm.project.details', {projectId: programProject.project, tab: 'details.plan'});
                } else {
                    $state.go('app.pm.project.details', {projectId: programProject.project, tab: 'details.basic'});
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
                if (programProject.type == "PROJECT") {
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
                                if (programProject.type == "GROUP") {
                                    $rootScope.showSuccessMessage(programGroupDeletedMessage);
                                } else {
                                    $rootScope.showSuccessMessage(deleteMessage);
                                }
                                loadProgramProjects();
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
                    var promise = null;
                    if (programProject.isNew) {
                        promise = ProgramService.createProgramProject($stateParams.programId, programProject);
                        promise.then(
                            function (data) {
                                programProject.id = data.id;
                                programProject.editMode = false;
                                programProject.isNew = false;
                                programProject.children = [];
                                $rootScope.showSuccessMessage(groupSaveMsg);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        promise = ProgramService.updateProgramProject($stateParams.programId, programProject);
                        promise.then(
                            function (data) {
                                programProject.id = data.id;
                                programProject.editMode = false;
                                programProject.isNew = false;
                                programProject.children = [];
                                $rootScope.showSuccessMessage(groupUpdateMsg);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
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
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(programProject) {
                programProject.editMode = false;
                programProject.name = programProject.oldName;
                programProject.description = programProject.oldDescription;
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
                vm.programProjectFilter.programManager = '';
                loadProgramProjects();
                $rootScope.hideBusyIndicator();
            }

            $scope.programManagers = [];
            function loadProgramManagers() {
                ProgramService.getProgramProjectManagers(vm.programId).then(
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
                vm.programProjectFilter.programManager = person.id;
                loadProgramProjectsByProgramManager(vm.programProjectFilter.programManager);
            }

            vm.toggleNode = toggleNode;
            function toggleNode(programProject) {
                if (programProject.expanded == null || programProject.expanded == undefined) {
                    programProject.expanded = false;
                }
                programProject.expanded = !programProject.expanded;
                var index = vm.programProjects.indexOf(programProject);
                if (programProject.expanded == false) {
                    if (programProject != null && programProject.programChildren != null && programProject.programChildren != undefined) {
                        vm.programProjects.splice(index + 1, programProject.programChildren.length);
                        programProject.programChildren = [];
                        programProject.expanded = false;
                    }
                } else {
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
                }
            }

            (function () {
                $scope.$on('app.program.tabactivated', function (event, data) {
                    if (data.tabId == 'details.project') {
                        $rootScope.freeTextQuerys = null;
                        $window.localStorage.setItem("project_open_from", null);
                        loadProgramProjects();
                        loadProgramManagers();
                    }
                });
            })();
        }
    }
)
;