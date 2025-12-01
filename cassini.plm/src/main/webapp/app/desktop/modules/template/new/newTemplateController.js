define(
    [
        'app/desktop/modules/template/template.module',
        'app/shared/services/core/projectTemplateService',
        'app/shared/services/core/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('NewTemplateController', NewTemplateController);

        function NewTemplateController($scope, $rootScope, $translate, ProjectTemplateService, ProjectService, LoginService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var nameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var pleaseSelectManager = parsed.html($translate.instant("P_S_MANAGER")).html();
            var templateNameValidation = parsed.html($translate.instant("TEMPLATE_NAME_VALIDATION")).html();
            vm.selectManager = parsed.html($translate.instant("SELECT_P_MANAGER")).html();
            $scope.selectWorkflow = parsed.html($translate.instant("SELECT_WORKFLOW")).html();

            vm.template = {
                id: null,
                name: null,
                description: null,
                manager: null,
                team: false,
                assignedTo: false,
                programTemplate: null,
                programTemplateProject: null,
                copyFolders: false,
                workflowDefinition: null,
                allFolders: true,
                allWorkflows: true
            };

            function createTemplate() {
                if (validate()) {
                    if( vm.template.workflowDefinition != null) {
                        vm.template.workflow = vm.template.workflowDefinition.id;
                    }
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    ProjectTemplateService.createProjectTemplate(vm.template).then(
                        function (data) {
                            vm.template = {
                                id: null,
                                name: null,
                                description: null,
                                manager: null,
                                programTemplate: null,
                                programTemplateProject: null,
                                workflowDefinition: null,
                                copyFolders: false,
                                allFolders: true
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

            function validate() {
                var valid = true;
                if (vm.template.name == null || vm.template.name == "" || vm.template.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                } else if (vm.template.programTemplate != null && (vm.template.manager == null || vm.template.manager == "" || vm.template.manager == undefined)) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseSelectManager);
                }

                return valid;
            }

            function saveProjectTemplate() {
                if (validate()) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    ProjectTemplateService.getProjectTemplateByName(vm.template.name).then(
                        function (template) {
                            if (template == "") {
                                ProjectService.saveAsProjectTemplate(vm.template, vm.project.id).then(
                                    function (data) {
                                        vm.template = {
                                            id: null,
                                            name: null,
                                            description: null,
                                            copyFolders: false,
                                            allFolders: true
                                        };
                                        $scope.callback();
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            } else {
                                $rootScope.showWarningMessage(templateNameValidation);
                                $rootScope.hideBusyIndicator();
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            function closeRightPanel() {
                vm.template = {
                    id: null,
                    name: null,
                    description: null,
                    copyFolders: false
                };
            }

            vm.selectTeam = selectTeam;
            function selectTeam(template) {
                if (template.team == false) {
                    template.assignedTo = false;
                }
            }

            function loadPersons() {
                LoginService.getInternalActiveLogins().then(
                    function (data) {
                        vm.persons = [];
                        angular.forEach(data, function (login) {
                            vm.persons.push(login.person);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.selectFolder = selectFolder;
            function selectFolder(value) {
                if (value == "All") {
                    vm.template.allFolders = true;
                    vm.template.projectFolders = false;
                    vm.template.activityFolders = false;
                    vm.template.taskFolders = false;
                } else if (value == "Project") {
                    vm.template.allFolders = false;
                    vm.template.projectFolders = true;
                    vm.template.activityFolders = false;
                    vm.template.taskFolders = false;
                } else if (value == "Activity") {
                    vm.template.allFolders = false;
                    vm.template.projectFolders = false;
                    vm.template.activityFolders = true;
                    vm.template.taskFolders = false;
                } else {
                    vm.template.allFolders = false;
                    vm.template.projectFolders = false;
                    vm.template.activityFolders = false;
                    vm.template.taskFolders = true;
                }
            }

            vm.selectWorkflow = selectWorkflow;
            function selectWorkflow(value) {
                if (value == "All") {
                    vm.template.allWorkflows = true;
                    vm.template.projectWorkflows = false;
                    vm.template.activityWorkflows = false;
                    vm.template.taskWorkflows = false;
                } else if (value == "Project") {
                    vm.template.allWorkflows = false;
                    vm.template.projectWorkflows = true;
                    vm.template.activityWorkflows = false;
                    vm.template.taskWorkflows = false;
                } else if (value == "Activity") {
                    vm.template.allWorkflows = false;
                    vm.template.projectWorkflows = false;
                    vm.template.activityWorkflows = true;
                    vm.template.taskWorkflows = false;
                } else {
                    vm.template.allWorkflows = false;
                    vm.template.projectWorkflows = false;
                    vm.template.activityWorkflows = false;
                    vm.template.taskWorkflows = true;
                }
            }


            function loadWorkflows() {
                ProjectService.getWorkflows('PROJECT TEMPLATE').then(
                    function (data) {
                        vm.wfs = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                if ($scope.data.projectPlan != undefined && $scope.data.projectPlan != null) {
                    vm.project = $scope.data.projectPlan;
                }
                if ($scope.data.selectedProgramTemplateId != undefined && $scope.data.selectedProgramTemplateId != null) {
                    vm.template.programTemplate = $scope.data.selectedProgramTemplateId;
                    vm.template.programTemplateProject = $scope.data.selectedGroupId;
                }
                loadPersons();
                loadWorkflows();
                $scope.$on('app.template.new', createTemplate);
                $rootScope.$on('app.rightside.panel.closing', closeRightPanel);
                $scope.$on('app.project.template.new', saveProjectTemplate);
                //}
            })();
        }
    }
);