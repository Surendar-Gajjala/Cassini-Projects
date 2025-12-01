/**
 * Created by swapna on 1/11/18.
 */


define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'

    ],
    function (module) {
        module.controller('TeamController', TeamController);

        function TeamController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, ProjectService, DialogService, CommonService) {

            var vm = this;

            vm.loading = true;
            $rootScope.showFlag = false;
            vm.showValues = true;
            vm.personRole = null;
            vm.person = null;

            vm.projectMembers = [];
            vm.persons = [];
            vm.projectRoles = [];


            vm.addPerson = addPerson;
            vm.deleteProjectPerson = deleteProjectPerson;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;

            var parsed = angular.element("<div></div>");
            vm.selectedPersons = [];
            $scope.selectPersons = [];
            var selectPerson = parsed.html($translate.instant("SELECT_PERSON")).html();
            var deleteMessage = parsed.html($translate.instant("DELETE_PERSON_SUCCESS")).html();
            var deleteDialogueMsg = parsed.html($translate.instant("DELETE_PERSON_VALIDATE")).html();
            var deleteDialogueValidateMsg = parsed.html($translate.instant("DELETE_PERSON_TASK_VALIDATE")).html();
            var deleteDialogueTitle = parsed.html($translate.instant("DELETE_PERSON")).html();
            vm.addMember = parsed.html($translate.instant("ADD_MEMBER")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();
            $scope.cannotDeletePerson = parsed.html($translate.instant("CANNOT_DELETE_PERSON")).html();
            var personUpdatedMsg = parsed.html($translate.instant("PERSON_UPDATED_MSG")).html();

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
            vm.projectMembers = angular.copy(pagedResults);

            function resetPage() {
                vm.projectMembers = angular.copy(pagedResults);
                vm.pageable.page = 0;
            }

            function nextPage() {
                if (vm.projectMembers.last != true) {
                    vm.pageable.page++;
                    loadProjectPagedPersons();
                }
            }

            function previousPage() {
                if (vm.projectMembers.first != true) {
                    vm.pageable.page--;
                    loadProjectPagedPersons();
                }
            }

            function loadProjectPagedPersons() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ProjectService.getProjectMembers($stateParams.projectId, pageable).then(
                    function (data) {
                        $scope.selectPersons = data;
                        angular.forEach($scope.selectPersons.content, function (person) {
                            person.editMode = false;
                        });
                        CommonService.getPersonReferences($scope.selectPersons.content, 'person');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var projectPersonDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var projectPersonAddedSuccess = parsed.html($translate.instant("PROJECT_PERSON_ADDED_SUCESS")).html();
            var projectPersonUpdatedSuccess = parsed.html($translate.instant("PROJECT_PERSON_UPDATED_SUCESS")).html();

            function deleteProjectPerson(projectPerson) {
                ProjectService.getProjectTasksAssignedToCount($stateParams.projectId, projectPerson.person).then(
                    function (data) {
                        var options = null;
                        if (data == null || data == "" || data == 0) {
                            options = {
                                title: deleteDialogueTitle,
                                message: deleteDialogueMsg + " [ " + projectPerson.personObject.fullName + " ] " + projectPersonDelete + "?",
                                okButtonClass: 'btn-danger'
                            };
                        } else {
                            options = {
                                title: deleteDialogueTitle,
                                message: deleteDialogueValidateMsg.format(projectPerson.personObject.fullName, data),
                                okButtonClass: 'btn-danger'
                            };
                        }
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                $rootScope.showBusyIndicator($('.view-container'));
                                ProjectService.deleteProjectMember($stateParams.projectId, projectPerson.person).then(
                                    function (data) {
                                        $rootScope.showSuccessMessage(deleteMessage);
                                        loadProjectPagedPersons();
                                        $rootScope.loadProjectCounts();
                                        $rootScope.loadProject();
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function addPerson() {
                var options = {
                    title: selectPerson,
                    showMask: true,
                    template: 'app/desktop/modules/pm/project/details/tabs/team/new/personDialogueView.jsp',
                    controller: 'PersonDialogueController as personDialogueVm',
                    resolve: 'app/desktop/modules/pm/project/details/tabs/team/new/personDialogueController',
                    width: 600,
                    data: {
                        projecPersons: $scope.selectPersons
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.person.new'}
                    ],
                    callback: function (result) {
                        vm.persons = result;
                        vm.itemFlag = true;
                        var selectPerson = {
                            project: $stateParams.projectId,
                            person: null,
                            personObject: null,
                            role: null,
                        };
                        angular.forEach(vm.persons, function (person) {
                            var specPerson = angular.copy(selectPerson);
                            specPerson.editMode = true;
                            specPerson.person = person.id;
                            specPerson.personObject = person;
                            vm.selectedPersons.unshift(specPerson);
                            $scope.selectPersons.content.unshift(specPerson);
                        });

                        // loadProjectPagedPersons();
                    }
                };

                $rootScope.showSidePanel(options);
            }


            vm.saveTeam = saveTeam;
            function saveTeam(person) {
                $rootScope.showBusyIndicator($('.view-container'));
                if (person.id == null) {
                    ProjectService.createProjectPerson($stateParams.projectId, person).then(
                        function (data) {
                            person.id = data.id;
                            person.editMode = false;
                            vm.selectedPersons.splice(vm.selectedPersons.indexOf(person), 1);
                            $rootScope.loadProjectCounts();
                            $rootScope.showSuccessMessage(projectPersonAddedSuccess);
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    ProjectService.updateProjectPerson($stateParams.projectId, person).then(
                        function (data) {
                            person.id = data.id;
                            person.editMode = false;
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
                $scope.selectPersons.content.splice($scope.selectPersons.content.indexOf(person), 1);
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(person) {
                person.editMode = false;
            }


            vm.editTeam = editTeam;
            function editTeam(projectPerson) {
                vm.itemFlag = false;
                projectPerson.editMode = true;

            }

            vm.saveAll = saveAll;
            function saveAll() {
                $rootScope.showBusyIndicator($('.view-container'));
                ProjectService.createProjectMultipleMembers($stateParams.projectId, vm.selectedPersons).then(
                    function (data) {
                        loadProjectPagedPersons();
                        vm.selectedPersons = [];
                        $rootScope.loadProjectCounts();
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
                    $scope.selectPersons.content.splice($scope.selectPersons.content.indexOf(person), 1);
                });
                vm.selectedPersons = [];
            }

            (function () {
                $scope.$on('app.project.tabactivated', function (event, data) {
                    if (data.tabId == 'details.members') {
                        loadProjectPagedPersons();
                    }
                });
            })();

        }
    }
);