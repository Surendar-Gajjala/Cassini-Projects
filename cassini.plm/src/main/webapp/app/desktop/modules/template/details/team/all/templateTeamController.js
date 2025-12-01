define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectTemplateService',
        'app/shared/services/core/activityService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'

    ],
    function (module) {
        module.controller('TemplateTeamController', TemplateTeamController);

        function TemplateTeamController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, ProjectTemplateService, DialogService, CommonService) {

            var vm = this;

            vm.loading = true;
            $rootScope.showFlag = false;
            vm.showValues = true;
            vm.personRole = null;
            vm.person = null;

            vm.projectTemplateMembers = [];

            vm.persons = [];
            vm.projectRoles = [];

            vm.addPerson = addPerson;
            vm.deleteProjectPerson = deleteProjectPerson;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;

            var parsed = angular.element("<div></div>");
            var selectPerson = parsed.html($translate.instant("SELECT_PERSON")).html();
            var deleteMessage = parsed.html($translate.instant("DELETE_PERSON_SUCCESS")).html();
            var deleteDialogueMsg = parsed.html($translate.instant("DELETE_PERSON_VALIDATE")).html();
            var deleteDialogueTitle = parsed.html($translate.instant("DELETE_PERSON")).html();
            vm.addMember = parsed.html($translate.instant("ADD_MEMBER")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.projectTemplateMembers = angular.copy(pagedResults);

            function resetPage() {
                vm.projectTemplateMembers = angular.copy(pagedResults);
                vm.pageable.page = 0;
            }

            function nextPage() {
                if (vm.projectTemplateMembers.last != true) {
                    vm.pageable.page++;
                    loadProjectTemplatePersons();
                }
            }

            function previousPage() {
                if (vm.projectTemplateMembers.first != true) {
                    vm.pageable.page--;
                    loadProjectTemplatePersons();
                }
            }

            function loadProjectTemplatePersons() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ProjectTemplateService.getProjectTemplateMembers($stateParams.templateId, pageable).then(
                    function (data) {
                        vm.projectTemplateMembers = data;
                        angular.forEach(vm.projectTemplateMembers.content, function (person) {
                            person.editMode = false;
                            person.isNew = false;
                        });
                        CommonService.getPersonReferences(vm.projectTemplateMembers.content, 'person');
                        $rootScope.loadProjectTemplateCounts();
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var projectPersonDelete = parsed.html($translate.instant("ITEMDELETE")).html();

            function deleteProjectPerson(projectPerson) {
                var options = {
                    title: deleteDialogueTitle,
                    message: deleteDialogueMsg + " [ " + projectPerson.personObject.fullName + " ] " + projectPersonDelete + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProjectTemplateService.deleteProjectTemplateMember($stateParams.templateId, projectPerson.person).then(
                            function (data) {
                                $rootScope.showSuccessMessage(deleteMessage);
                                loadProjectTemplatePersons();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            function addPerson() {
                var options = {
                    title: selectPerson,
                    showMask: true,
                    template: 'app/desktop/modules/template/details/team/new/templatePersonDialogueView.jsp',
                    controller: 'TemplatePersonDialogueController as templatePersonVm',
                    resolve: 'app/desktop/modules/template/details/team/new/templatePersonDialogueController',
                    width: 600,
                    data: {
                        projecPersons: vm.projectTemplateMembers
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.template.person.new'}
                    ],
                    callback: function (result) {
                        //loadProjectTemplatePersons();
                        vm.persons = result;
                        vm.itemFlag = true;
                        var selectPerson = {
                            template: $stateParams.templateId,
                            person: null,
                            personObject: null,
                            role: null
                        };
                        angular.forEach(vm.persons, function (person) {
                            var specPerson = angular.copy(selectPerson);
                            specPerson.editMode = true;
                            specPerson.person = person.id;
                            specPerson.personObject = person;
                            specPerson.isNew = true;
                            vm.selectedPersons.unshift(specPerson);
                            vm.projectTemplateMembers.content.unshift(specPerson);
                        });
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var projectPersonAddedSuccess = parsed.html($translate.instant("PROJECT_PERSON_ADDED_SUCESS")).html();
            var projectPersonUpdatedSuccess = parsed.html($translate.instant("PROJECT_PERSON_UPDATED_SUCESS")).html();

            vm.saveTeam = saveTeam;
            function saveTeam(person) {
                $rootScope.showBusyIndicator($('.view-container'));
                if (person.id == null) {
                    ProjectTemplateService.createProjectTemplateMember($stateParams.templateId, person).then(
                        function (data) {
                            person.id = data.id;
                            person.editMode = false;
                            person.isNew = false;
                            vm.selectedPersons.splice(vm.selectedPersons.indexOf(person), 1);
                            $rootScope.loadProjectTemplateCounts();
                            $rootScope.showSuccessMessage(projectPersonAddedSuccess);
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    ProjectTemplateService.updateProjectTemplateMember($stateParams.templateId, person).then(
                        function (data) {
                            person.id = data.id;
                            person.editMode = false;
                            person.isNew = false;
                            $rootScope.showSuccessMessage(projectPersonUpdatedSuccess);
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.onCancel = onCancel;
            function onCancel(person) {

                vm.selectedPersons.splice(vm.selectedPersons.indexOf(person), 1);
                vm.projectTemplateMembers.content.splice(vm.projectTemplateMembers.content.indexOf(person), 1);
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(person) {
                person.editMode = false;
                person.role = person.oldRole;
            }


            vm.editTeam = editTeam;
            function editTeam(projectPerson) {
                vm.itemFlag = false;
                projectPerson.editMode = true;
                projectPerson.oldRole = projectPerson.role;

            }

            vm.saveAll = saveAll;
            function saveAll() {
                $rootScope.showBusyIndicator($('.view-container'));
                ProjectTemplateService.createProjectTemplateMembers($stateParams.templateId, vm.selectedPersons).then(
                    function (data) {
                        loadProjectTemplatePersons();
                        vm.selectedPersons = [];
                        $rootScope.loadProjectTemplateCounts();
                        $rootScope.showSuccessMessage(projectPersonAddedSuccess);
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

            }

            vm.removeAll = removeAll;
            function removeAll() {
                angular.forEach(vm.selectedPersons, function (person) {
                    vm.projectTemplateMembers.content.splice(vm.projectTemplateMembers.content.indexOf(person), 1);
                });
                vm.selectedPersons = [];
            }


            (function () {
                $scope.$on('app.template.tabActivated', function (event, data) {
                    if (data.tabId == 'details.team') {
                        vm.selectedPersons = [];
                        loadProjectTemplatePersons();
                    }
                });
            })();

        }
    }
);

