define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/programTemplateService',
        'app/shared/services/core/programService',
        'app/shared/services/core/projectService'
    ],
    function (module) {
        module.controller('NewProgramTemplateController', NewProgramTemplateController);

        function NewProgramTemplateController($scope, $rootScope, $translate, ProgramTemplateService, ProjectService, ProgramService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var nameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var templateNameValidation = parsed.html($translate.instant("TEMPLATE_NAME_VALIDATION")).html();
            $scope.selectWorkflow = parsed.html($translate.instant("SELECT_WORKFLOW")).html();

            vm.template = {
                id: null,
                name: null,
                description: null,
                resources: false,
                workflowDefinition: null,
                projects: false,
                team: false,
                assignedTo: false,
                program: null,
                allFolders: false,
                allWorkflows: false
            };

            function validate() {
                var valid = true;
                if (vm.template.name == null || vm.template.name == "" || vm.template.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                } else if (vm.template.projects && vm.selectedObjectIds.length == 0) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select atleast one project");
                }

                return valid;
            }

            function saveProjectTemplate() {
                if (validate()) {
                    if (vm.template.workflowDefinition != null) {
                        vm.template.workflow = vm.template.workflowDefinition.id;
                    }
                    vm.template.selectedProjectIds = vm.selectedObjectIds;
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    ProgramTemplateService.createProgramTemplate(vm.template).then(
                        function (data) {
                            vm.template = {
                                id: null,
                                name: null,
                                description: null,
                                resources: false,
                                workflowDefinition: null,
                                projects: false,
                                team: false,
                                assignedTo: false,
                                program: null,
                                allFolders: true,
                                allWorkflows: true
                            };
                            $scope.callback();
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.selectTeam = selectTeam;
            function selectTeam(template) {
                if (template.team == false) {
                    template.assignedTo = false;
                }
            }

            vm.selectProjects = selectProjects;
            function selectProjects(template) {
                if (template.projects == false) {
                    template.assignedTo = false;
                    template.team = false;
                    if (template.copyFolders) {
                        document.getElementById("program").checked = true;
                        vm.template.programFolders = true;
                        vm.template.allFolders = false;
                    }
                    if (template.copyWorkflows) {
                        document.getElementById("programWorkflow").checked = true;
                        vm.template.programWorkflows = true;
                        vm.template.allWorkflows = false;
                    }
                }
            }

            vm.selectCopyFolders = selectCopyFolders;
            function selectCopyFolders() {
                if (vm.template.copyFolders) {
                    if (vm.template.projects) {
                        vm.template.allFolders = true;
                        vm.template.programFolders = false;
                    } else {
                        document.getElementById("program").checked = true;
                        vm.template.programFolders = true;
                        vm.template.allFolders = false;
                    }
                }
            }

            vm.selectCopyWorkflows = selectCopyWorkflows;
            function selectCopyWorkflows() {
                if (vm.template.copyWorkflows) {
                    if (vm.template.projects) {
                        vm.template.allWorkflows = true;
                        vm.template.programWorkflows = false;
                    } else {
                        document.getElementById("programWorkflow").checked = true;
                        vm.template.programWorkflows = true;
                        vm.template.allWorkflows = false;
                    }
                }
            }

            vm.selectFolder = selectFolder;
            function selectFolder(value) {
                if (value == "All") {
                    vm.template.allFolders = true;
                    vm.template.programFolders = false;
                    vm.template.projectFolders = false;
                    vm.template.activityFolders = true;
                    vm.template.taskFolders = false;
                } else if (value == "Program") {
                    vm.template.allFolders = false;
                    vm.template.programFolders = true;
                    vm.template.projectFolders = false;
                    vm.template.activityFolders = true;
                    vm.template.taskFolders = false;
                } else if (value == "Project") {
                    vm.template.allFolders = false;
                    vm.template.programFolders = false;
                    vm.template.projectFolders = true;
                    vm.template.activityFolders = true;
                    vm.template.taskFolders = false;
                } else if (value == "Activity") {
                    vm.template.allFolders = false;
                    vm.template.programFolders = false;
                    vm.template.projectFolders = false;
                    vm.template.activityFolders = true;
                    vm.template.taskFolders = false;
                } else {
                    vm.template.allFolders = false;
                    vm.template.programFolders = false;
                    vm.template.projectFolders = false;
                    vm.template.activityFolders = true;
                    vm.template.taskFolders = true;
                }
            }

            vm.selectWorkflow = selectWorkflow;
            function selectWorkflow(value) {
                if (value == "All") {
                    vm.template.allWorkflows = true;
                    vm.template.programWorkflows = false;
                    vm.template.projectWorkflows = false;
                    vm.template.activityWorkflows = false;
                    vm.template.taskWorkflows = false;
                } else if (value == "Program") {
                    vm.template.allWorkflows = false;
                    vm.template.programWorkflows = true;
                    vm.template.projectWorkflows = false;
                    vm.template.activityWorkflows = false;
                    vm.template.taskWorkflows = false;
                } else if (value == "Project") {
                    vm.template.allWorkflows = false;
                    vm.template.programWorkflows = false;
                    vm.template.projectWorkflows = true;
                    vm.template.activityWorkflows = false;
                    vm.template.taskWorkflows = false;
                } else if (value == "Activity") {
                    vm.template.allWorkflows = false;
                    vm.template.programWorkflows = false;
                    vm.template.projectWorkflows = false;
                    vm.template.activityWorkflows = true;
                    vm.template.taskWorkflows = false;
                } else {
                    vm.template.allWorkflows = false;
                    vm.template.programWorkflows = false;
                    vm.template.projectWorkflows = false;
                    vm.template.activityWorkflows = false;
                    vm.template.taskWorkflows = true;
                }
            }

            function loadWorkflows() {
                ProjectService.getWorkflows('PROGRAM TEMPLATE').then(
                    function (data) {
                        vm.wfs = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadProgramProjects() {
                vm.programProjects = [];
                ProgramService.getProgramProjects(vm.template.program).then(
                    function (data) {
                        vm.programProjects = [];
                        vm.groupMap = new Hashtable();
                        angular.forEach(data, function (programProject) {
                            programProject.programChildren = [];
                            programProject.level = 0;
                            programProject.expanded = true;
                            programProject.selectedProjectCount = 0;
                            vm.programProjects.push(programProject);
                            vm.groupMap.put(programProject.id, programProject);
                            var index = vm.programProjects.indexOf(programProject);

                            angular.forEach(programProject.children, function (child) {
                                index++;
                                child.level = programProject.level + 1;
                                child.expanded = false;
                                programProject.programChildren.push(child);
                                vm.programProjects.splice(index, 0, child);
                            })
                        })
                        vm.loading = false;
                    }
                );
            }

            vm.selectedObjectIds = [];
            vm.selectCheck = selectCheck;
            function selectCheck(object) {
                if (object.selected) {
                    if (object.type == "GROUP") {
                        angular.forEach(vm.programProjects, function (project) {
                            if (project.parent == object.id) {
                                project.selected = true;
                                project.selectedProjectCount = project.children.length;
                                vm.groupMap.put(project.id, project);
                                if (vm.selectedObjectIds.indexOf(project.id) == -1) {
                                    vm.selectedObjectIds.push(project.id);
                                }
                            }
                        })
                    } else {
                        angular.forEach(vm.programProjects, function (project) {
                            if (project.type == "GROUP") {
                                var group = vm.groupMap.get(project.id);
                                group.selectedProjectCount = 0;
                                vm.groupMap.put(group.id, group);
                            }
                            if (project.parent == object.parent) {
                                if (project.selected) {
                                    var group = vm.groupMap.get(object.parent);
                                    group.selectedProjectCount++;
                                    vm.groupMap.put(group.id, group);
                                }
                            }
                            var group = vm.groupMap.get(object.parent);
                            if (group != null && group.children.length > 0 && group.children.length == group.selectedProjectCount) {
                                group.selected = true;
                                if (vm.selectedObjectIds.indexOf(group.id) == -1) {
                                    vm.selectedObjectIds.push(group.id);
                                }
                            }
                        })
                    }
                    vm.selectedObjectIds.push(object.id);
                } else {
                    if (object.type == "GROUP") {
                        angular.forEach(vm.programProjects, function (project) {
                            if (project.parent == object.id) {
                                project.selected = false;
                                object.selectedProjectCount = 0;
                                vm.groupMap.put(object.id, object);
                                if (vm.selectedObjectIds.indexOf(project.id) != -1) {
                                    vm.selectedObjectIds.splice(vm.selectedObjectIds.indexOf(project.id), 1);
                                }
                            }
                        })
                    } else {
                        angular.forEach(vm.programProjects, function (project) {
                            if (project.id == object.parent) {
                                project.selectedProjectCount--;
                                project.selected = false;
                                vm.groupMap.put(project.id, project);
                                if (vm.selectedObjectIds.indexOf(project.id) != -1) {
                                    vm.selectedObjectIds.splice(vm.selectedObjectIds.indexOf(project.id), 1);
                                }
                            }
                        })
                    }
                    vm.selectedObjectIds.splice(vm.selectedObjectIds.indexOf(object.id), 1);
                }

                if (vm.selectedObjectIds.length == vm.programProjects.length) {
                    vm.selectAllCheck = true;
                } else {
                    vm.selectAllCheck = false;
                }
            }

            vm.selectAllCheck = false;
            vm.selectAll = selectAll;
            function selectAll() {
                if (vm.selectAllCheck) {
                    vm.selectedObjectIds = [];
                    angular.forEach(vm.programProjects, function (project) {
                        project.selected = true;
                        if (project.type == "GROUP") {
                            project.selectedProjectCount = project.children.length;
                        }
                        vm.selectedObjectIds.push(project.id);
                    })
                } else {
                    angular.forEach(vm.programProjects, function (project) {
                        project.selected = false;
                        if (project.type == "GROUP") {
                            project.selectedProjectCount = 0;
                        }
                    })
                    vm.selectedObjectIds = [];
                }
            }


            (function () {
                vm.template.program = $scope.data.selectedProgramTemplateId;
                if (vm.template.program != null && vm.template.program != undefined) {
                    loadProgramProjects();
                }
                $scope.$on('app.program.template.new', saveProjectTemplate);
                loadWorkflows();
            })();
        }
    }
)
;