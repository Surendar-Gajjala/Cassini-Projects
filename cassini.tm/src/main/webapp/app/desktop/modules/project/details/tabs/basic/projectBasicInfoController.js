define(
    [
        'app/desktop/modules/project/project.module',

    ],
    function (module) {
        module.controller('ProjectBasicInfoController', ProjectBasicInfoController);

        function ProjectBasicInfoController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                             ProjectService) {
            var vm = this;

            vm.loading = true;
            vm.projectId = $stateParams.projectId;
            vm.project = null;
            vm.updateProject = updateProject;

            function loadProject() {
                vm.loading = true;
                ProjectService.getProjectById(vm.projectId).then(
                    function (data) {
                        vm.project = data;
                        vm.loading = false;
                        $rootScope.viewInfo.title = "Project Details (" + vm.project.name + ")";
                    }
                )
            }

            function updateProject() {
                ProjectService.updateProject(vm.project).then(
                    function (data) {
                        $rootScope.showSuccessMessage("Project Updated Successfully!");
                    }
                )
            }



            (function () {
                if ($application.homeLoaded == true) {
                    loadProject();
                    $scope.$on('app.project.update',  function() {
                       updateProject();
                    })
                }
            })();
        }
    }
);