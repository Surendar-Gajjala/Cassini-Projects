define(['app/desktop/modules/pm/pm.module',
        'app/desktop/modules/pm/projectAdmin/role/roleDialogueController',
        'app/shared/services/pm/project/projectService'
    ],
    function (module) {
        module.controller('RoleController', RoleController);
        function RoleController($scope, $rootScope, $timeout, $state, $stateParams, $uibModal, ProjectService, DialogService) {
            $rootScope.viewInfo.icon = "fa fa-users";
            $rootScope.viewInfo.title = "Team";

            var vm = this;

            vm.loading = true;
            vm.showValues = true;
            vm.projectRoles = [];
            vm.deleteProjectRole = deleteProjectRole;
            vm.editProjectRole = editProjectRole;
            vm.cancelChanges = cancelChanges;
            vm.applyChanges = applyChanges;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.resetPage = resetPage;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate"
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
            vm.projectRoles = angular.copy(pagedResults);

            function resetPage() {
                vm.pageable.page = 0;
            }

            function nextPage() {
                if (vm.projectRoles.last != true) {
                    vm.pageable.page++;
                    loadProjectRoles();
                }
            }

            function previousPage() {
                if (vm.projectRoles.first != true) {
                    vm.pageable.page--;
                    loadProjectRoles();
                }
            }

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            function newRole() {
                var options = {
                    title: 'New Role',
                    showMask: true,
                    template: 'app/desktop/modules/pm/projectAdmin/role/roleDialogueView.jsp',
                    controller: 'RoleDialogueController as roleDialogueVm',
                    resolve: 'app/desktop/modules/pm/projectAdmin/role/roleDialogueController',
                    width: 500,
                    data: {},
                    buttons: [
                        {text: 'Create', broadcast: 'app.role.new'}
                    ],
                    callback: function () {
                        loadProjectRoles();
                    }

                };

                $rootScope.showSidePanel(options);
            }

            function loadProjectRoles() {
                ProjectService.getProjectPagedRoles($stateParams.projectId, vm.pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.projectRoles = data;
                        $rootScope.projectRoles = vm.projectRoles;
                        angular.forEach(vm.projectRoles.content, function (projectRole) {
                            projectRole.editMode = false;
                            projectRole.showValues = true;
                            projectRole.newDescription = projectRole.description;
                            projectRole.newRole = projectRole.role;
                        });
                        $rootScope.resizeTeamView();
                    }
                );
            }

            function applyChanges(projectRole) {
                projectRole.editMode = false;
                projectRole.description = projectRole.newDescription;
                projectRole.role = projectRole.newRole;
                ProjectService.updateProjectRole(projectRole.id, projectRole).then(
                    function (data) {
                        projectRole.id = data.id;
                        projectRole.editMode = false;
                        $timeout(function () {
                            projectRole.showValues = true;
                        }, 100);
                        loadProjectRoles();
                        $rootScope.showSuccessMessage(projectRole.role + " : Role updated successfully");
                    }
                );

            }

            function cancelChanges(projectRole) {
                var promise = null;
                if (projectRole.id == null || projectRole.id == undefined) {
                    vm.siteList.splice(vm.projectRoles.content.indexOf(projectRole), 1);
                }
                else {
                    projectRole.newDescription = projectRole.description;
                    projectRole.newRole = projectRole.role;
                    promise = ProjectService.updateProjectRole(projectRole.id, projectRole);
                    projectRole.editMode = false;
                    loadProjectRoles();
                    $timeout(function () {
                        projectRole.showValues = true;
                    }, 500);
                }

            }

            function editProjectRole(projectRole) {
                angular.forEach(projectRole.projectRoles, function (ProjectRole) {
                    angular.forEach(vm.projectRoles, function (projectRole) {
                        if (ProjectRole.projectRole == projectRole.id) {
                            var index = vm.projectRoles.indexOf(projectRole);
                            vm.projectRoles.splice(index, 1);
                        }
                    })
                });
                projectRole.showValues = false;
                projectRole.editMode = true;
                $timeout(function () {
                }, 500);
            }

            function deleteProjectRole(projectRole) {
                ProjectService.getPersonsByRoleId($stateParams.projectId, projectRole.id).then(
                    function (data) {
                        if (data.length == 0) {
                            var options = {
                                title: 'Delete Role',
                                message: 'Are you sure you want to delete this (' + projectRole.role + ') Role?',
                                okButtonClass: 'btn-danger'
                            };
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    ProjectService.deleteProjectRole($stateParams.projectId, projectRole.id).then(
                                        function (data) {
                                            var index = vm.projectRoles.content.indexOf(projectRole);
                                            vm.projectRoles.content.splice(index, 1);
                                            $rootScope.showErrorMessage(projectRole.role + " :Role deleted successfully");
                                            loadProjectRoles();

                                        });
                                }
                            });
                        } else {
                            $rootScope.showErrorMessage("Role assigned to the Team! we cannot delete");
                        }
                    });
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadProjectRoles();
                    $scope.$on('app.team.role', newRole);

                }
            })();
        }
    }
);