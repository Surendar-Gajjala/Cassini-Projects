define(
    [
        'app/desktop/modules/template/template.module',
        'app/shared/services/core/projectTemplateService',
        'app/shared/services/core/projectService'
    ],
    function (module) {
        module.controller('AddTemplateController', AddTemplateController);

        function AddTemplateController($scope, $rootScope, $sce, $translate, ProjectTemplateService, ProjectService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            $scope.selectTemplate = parsed.html($translate.instant("SELECT_TEMPLATE")).html();

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            function addProjectTemplate() {
                ProjectService.addProjectWithTemplate(vm.project, vm.selectedTemplate.id).then(
                    function (data) {
                        $scope.$off('app.project.template.add', addProjectTemplate);
                        $rootScope.loadProject();
                        $scope.callback();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            (function () {
                //if ($application.homeLoaded == true) {
                vm.project = $scope.data.projectPlan;
                $scope.$on('app.project.template.add', addProjectTemplate);
                //}
            })();
        }
    }
);