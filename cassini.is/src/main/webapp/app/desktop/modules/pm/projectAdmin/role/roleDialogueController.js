define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/pm/project/projectService'
    ],
    function (module) {
        module.controller('RoleDialogueController', RoleDialogueController);

        function RoleDialogueController($scope, $rootScope, $timeout, $state, $stateParams, ProjectService) {
            $rootScope.viewInfo.icon = "fa fa-users";
            $rootScope.viewInfo.title = "Team";

            var vm = this;

            vm.error = "";
            vm.hasError = false;
            vm.valid = true;

            vm.create = create;
            var roleMap = new Hashtable();
            var projectRoles = [];

            vm.projectRole = {
                role: null,
                description: "",
                project: $stateParams.projectId
            };

            function validateRole() {
                vm.valid = true;

                if (vm.projectRole.role == null || vm.projectRole.role == undefined || vm.projectRole.role == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Role Name cannot be empty");
                }
                else if (roleMap.get(vm.projectRole.role) != null) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("{0} Name already exists".format(vm.projectRole.role));
                }
                /*else if (vm.projectRole.description == null || vm.projectRole.description == undefined || vm.projectRole.description == "") {
                 vm.valid = false;
                 $rootScope.showErrorMessage("Role description cannot be empty");
                 } */
                return vm.valid;
            }

            function create() {
                if (validateRole() == true) {
                    ProjectService.createProjectRole($stateParams.projectId, vm.projectRole).then(
                        function (data) {
                            $rootScope.hideSidePanel('left');
                            $scope.callback(data);
                            vm.creating = false;
                            $rootScope.showSuccessMessage("Role created successfully");
                            vm.projectRole = {
                                role: null,
                                description: "",
                                project: $stateParams.projectId
                            };

                        }, function (error) {
                            $rootScope.showWarningMessage(error.message);
                        }
                    )
                }
            }

            function loadProjectRoles() {
                ProjectService.getProjectRoles($stateParams.projectId).then(
                    function (data) {
                        vm.loading = false;
                        projectRoles = data;
                        angular.forEach(projectRoles, function (projectRole) {
                            roleMap.put(projectRole.role, projectRole);
                        })

                    });
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$broadcast('app.activate.procurement', {project: {name: 'Procurement'}})
                    loadProjectRoles();
                    $rootScope.$on('app.role.new', create);
                }
            })();
        }
    }
);