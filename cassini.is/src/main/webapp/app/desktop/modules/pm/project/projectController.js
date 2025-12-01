define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/pm/project/projectService'
    ],
    function (module) {
        module.controller('ProjectController', ProjectController);

        function ProjectController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                   ProjectService) {
            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = "Home";

            var vm = this;
            vm.project = null;
            $scope.project = null;
            $rootScope.loadProject = loadProject;

            function loadProject() {
                ProjectService.getProject($stateParams.projectId).then(
                    function (data) {
                        vm.project = data;
                        $rootScope.totalTasks = vm.project.totalTasks;
                        $rootScope.pendingTasks = vm.project.pendingTasks;
                        $rootScope.finishedTasks = vm.project.finishedTasks;
                        $scope.project = data;
                        $rootScope.$broadcast('app.activate.project', {project: data})
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadProject();
                }
            })();
        }
    }
);