define(
    [
        'app/phone/modules/project/project.module',
        'app/shared/services/projectService'
    ],
    function (module) {
        module.controller('AllProjectsController', AllProjectsController);

        function AllProjectsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $uibModal, ProjectService) {
            var vm = this;

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);
