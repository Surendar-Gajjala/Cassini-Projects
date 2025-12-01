define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/programTemplateService'
    ],
    function (module) {
        module.controller('ProgramTemplateProjectsController', ProgramTemplateProjectsController);

        function ProgramTemplateProjectsController($scope, $rootScope, $state, $timeout, $sce, $window, $stateParams, $translate,
                                                   DialogService, ProgramTemplateService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.loading = true;
            vm.templateId = $stateParams.templateId;
            var newProject = parsed.html($translate.instant("NEW_PROJECT")).html();
            var groupSaveMsg = parsed.html($translate.instant("PROGRAM_GROUP_SAVED_MSG")).html();
            var nameValidation = parsed.html($translate.instant("PLEASE_ENTER_NAME")).html();

            var emptyProgramProject = {
                id: null,
                template: vm.templateId,
                name: null,
                description: null,
                parent: null,
                projectTemplate: null,
                type: 'GROUP'
            };

            function loadProgramProjects() {
                vm.loading = true;
                ProgramTemplateService.getProgramTemplateProjects(vm.templateId).then(
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

            vm.openProjectDetails = openProjectDetails;
            function openProjectDetails(programProject) {
                $window.localStorage.setItem("template_open_from", vm.templateId);
                $state.go('app.templates.details', {templateId: programProject.projectTemplate});
            }

            var create = parsed.html($translate.instant("CREATE")).html();
            var newTemplate = parsed.html($translate.instant("NEW_TEMPLATE")).html();
            var templateCreatedMessage = parsed.html($translate.instant("TEMPLATE_CREATE_MESSAGE")).html();
            vm.createNewTemplate = createNewTemplate;
            function createNewTemplate(programProject) {
                var options = {
                    title: newTemplate,
                    template: 'app/desktop/modules/template/new/newTemplateView.jsp',
                    controller: 'NewTemplateController as newTemplateVm',
                    resolve: 'app/desktop/modules/template/new/newTemplateController',
                    width: 600,
                    showMask: true,
                    data: {
                        selectedProgramTemplateId: vm.templateId,
                        selectedGroupId: programProject.id
                    },
                    buttons: [
                        {text: create, broadcast: 'app.template.new'}
                    ],
                    callback: function (data) {
                        loadProgramProjects();
                        $rootScope.loadProgramTemplateCounts();
                        $rootScope.showSuccessMessage(templateCreatedMessage);
                        $rootScope.hideSidePanel();
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
                    dialogMsg = deleteDialogueMsg1.format(programProject.groupName, programProject.name, "project");
                    dialogTitle = deleteDialogueTitle;
                }
                var options = {
                    title: dialogTitle,
                    message: dialogMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProgramTemplateService.deleteProgramTemplateProject($stateParams.templateId, programProject.id).then(
                            function (data) {
                                if (programProject.type == "GROUP") {
                                    $rootScope.showSuccessMessage(programGroupDeletedMessage);
                                } else {
                                    $rootScope.showSuccessMessage(deleteMessage);
                                }
                                loadProgramProjects();
                                $rootScope.loadProgramTemplateCounts();
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
                        promise = ProgramTemplateService.createProgramTemplateProject($stateParams.templateId, programProject);
                    } else {
                        promise = ProgramTemplateService.updateProgramTemplateProject($stateParams.templateId, programProject);
                    }
                    if (promise != null) {
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

            (function () {
                $scope.$on('app.programTemplate.tabActivated', function (event, data) {
                    if (data.tabId == 'details.project') {
                        $window.localStorage.setItem("template_open_from", null);
                        loadProgramProjects();
                    }
                });
            })();
        }
    }
)
;