define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/pm/project/projectService'
    ],
    function (module) {
        module.controller('TaskImportController', TaskImportController);

        function TaskImportController($scope, $rootScope, $sce, $stateParams, ProjectService) {

            var vm = this;

            var projectId = $stateParams.projectId;

            vm.projectClone = {
                id: null
            };

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            function saveAsProject() {
                vm.projectClone.id = vm.projectClone.project.id;
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                ProjectService.copyTasks(projectId, vm.projectClone).then(
                    function (data) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showSuccessMessage("Task copied successfully");
                        $rootScope.hideSidePanel();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                        error.message = null;
                    }
                )
            }

            function loadProjects() {
                ProjectService.getAllProjects().then(
                    function (data) {
                        vm.projects = [];
                        angular.forEach(data, function (project) {
                            if (project.id != projectId) {
                                vm.projects.push(project);
                            }
                        })
                    })
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadProjects();
                    $scope.$on('app.project.copy.task', saveAsProject);
                }
            })();
        }
    }
);
