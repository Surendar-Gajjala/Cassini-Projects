define(['app/desktop/modules/pm/pm.module',
        'app/desktop/modules/pm/projectAdmin/team/new/personDialogueController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/pm/project/projectService'

    ],
    function (module) {
        module.controller('PersonController', PersonController);

        function PersonController($scope, $rootScope, $timeout, DialogService, ProjectService,
                                  CommonService, $state, $stateParams) {

            $rootScope.viewInfo.icon = "fa fa-users";
            $rootScope.viewInfo.title = "Team";

            var vm = this;

            vm.loading = true;
            $rootScope.showFlag = false;
            vm.showValues = true;
            vm.personRole = null;
            vm.person = null;

            vm.projectPersons = [];
            vm.persons = [];
            vm.projectRoles = [];

            vm.deleteProjectPerson = deleteProjectPerson;
            vm.removeRole = removeRole;
            vm.selectRole = selectRole;
            vm.personDetails = personDetails;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.editPerson = editPerson;
            vm.resetPage = resetPage;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "rowId",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.projectPersons = angular.copy(pagedResults);

            function personDetails(projectPerson) {
                $state.go("app.pm.project.projectAdmin.team.personDetails", {personId: projectPerson.person});
                $rootScope.showFlag = true;
            }

            function resetPage() {
                vm.pageable.page = 0;
            }

            function nextPage() {
                if ($rootScope.projectPersons.last != true) {
                    vm.pageable.page++;
                    loadProjectPagedPersons();
                }
            }

            function previousPage() {
                if ($rootScope.projectPersons.first != true) {
                    vm.pageable.page--;
                    loadProjectPagedPersons();
                }
            }

            function addPerson() {
                var options = {
                    title: 'Select Person(s)',
                    showMask: true,
                    template: 'app/desktop/modules/pm/projectAdmin/team/new/personDialogueView.jsp',
                    controller: 'PersonDialogueController as personDialogueVm',
                    resolve: 'app/desktop/modules/pm/projectAdmin/team/new/personDialogueController',
                    width: 700,
                    data: {
                        projecPersons: vm.projectPersons
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.person.new'}
                    ],
                    callback: function () {
                        loadProjectPagedPersons();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadProjectPagedPersons() {
                ProjectService.getProjectPagedPersons($stateParams.projectId, vm.pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.projectPersons = data;
                        $rootScope.projectPersons = vm.projectPersons;
                        angular.forEach(vm.projectPersons.content, function (person) {
                            person.editMode = false;
                        });
                        $rootScope.resizeTeamView();

                        loadPersonsRoles();
                        CommonService.getPersonReferences(vm.projectPersons.content, 'person');
                    }
                )
            }

            function loadPersonsRoles() {
                var personIds = [];
                angular.forEach(vm.projectPersons.content, function (projectPerson) {
                    personIds.push(projectPerson.person);
                });
                ProjectService.getPersonsRoles($stateParams.projectId, personIds).then(
                    function (data) {
                        angular.forEach(vm.projectPersons.content, function (projectPerson) {
                            projectPerson.roles = data[projectPerson.person];
                            projectPerson.roleList = getRemainingRoles(projectPerson);
                            ProjectService.getRoleReferences($stateParams.projectId, projectPerson.roles, 'role');
                        });
                    });
            }

            function getRemainingRoles(person) {
                var roles = angular.copy(vm.projectRoles);

                angular.forEach(person.roles, function (role) {
                    var index = findPersonRoleById(roles, role.role);
                    if (index != -1) {
                        roles.splice(index, 1);
                    }
                });

                return roles;
            }

            function removeRole(projectPerson, item) {
                if (item.rowId != undefined) {
                    ProjectService.deletePersonRole($stateParams.projectId, item.person, item.rowId).then(
                        function (data) {
                            $rootScope.showErrorMessage("Role removed successfully");
                            //projectPerson.roleList(getRemainingRoles(projectPerson));
                            loadProjectRoles();
                        }
                    )
                } else {
                    projectPerson.roleList.push(item);
                }

            }

            function selectRole(projectPerson, item) {
                item.roleObject = item;
                var index = projectPerson.roleList.indexOf(item);
                projectPerson.roleList.splice(index, 1);
                /* projectPerson.roleList = getRemainingRoles(projectPerson);*/
            }

            function deleteProjectPerson(projectPerson) {
                var options = {
                    title: 'Delete Person',
                    message: 'Are you sure you want to delete this (' + projectPerson.personObject.fullName + ') Person?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProjectService.deleteProjectPerson($stateParams.projectId, projectPerson.person).then(
                            function (data) {
                                $rootScope.showSuccessMessage(projectPerson.personObject.fullName + " :Project person deleted successfully");
                                loadProjectPagedPersons();
                            }
                        )
                    }
                });
            }

            function loadProjectRoles() {
                ProjectService.getProjectRoles($stateParams.projectId).then(
                    function (data) {
                        vm.projectRoles = data;
                        loadProjectPagedPersons();
                    }
                )
            }

            function findPersonRoleById(roles, id) {
                for (var i = 0; i < roles.length; i++) {
                    if (roles[i].id == id) {
                        return i;
                    }
                }
                return -1;
            }

            function applyChanges(projectPerson) {
                var personRoles = [];
                vm.showValues = true;
                projectPerson.editMode = false;
                projectPerson.showValues = true;

                angular.forEach(projectPerson.roles, function (role) {
                    if (role.id != null && role.id != undefined) {
                        var obj = {
                            project: $stateParams.projectId,
                            person: projectPerson.person,
                            role: role.id
                        };
                        personRoles.push(obj);
                    }
                });
                if (personRoles.length > 0) {
                    ProjectService.createPersonRole($stateParams.projectId, projectPerson.person, personRoles).then(
                        function (data) {
                            vm.personRoles = data;
                            loadProjectPagedPersons();
                            $rootScope.showSuccessMessage("Role added to person successfully")
                        }
                    )
                }
            }

            function cancelChanges(projectPerson) {
                vm.showValues = true;
                projectPerson.showValues = true;
                projectPerson.editMode = false;
                $timeout(function () {
                    projectPerson.showValues = false;
                }, 500);
                loadProjectRoles();
            }

            function editPerson(projectPerson) {
                projectPerson.showValues = false;
                projectPerson.editMode = true;
                $timeout(function () {
                    projectPerson.editMode = true;
                }, 500);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadProjectRoles();
                    $scope.$on('app.team.person', addPerson);
                    $scope.$on('app.team.nextPageDetails', nextPage);
                    $scope.$on('app.team.previousPageDetails', previousPage);
                }
            })();

        }
    }
)
;