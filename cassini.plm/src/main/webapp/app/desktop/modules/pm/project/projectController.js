define(
    [
        'app/desktop/modules/pm/pm.module',
    ],
    function (module) {
        module.controller('ProjectController', ProjectController);

        function ProjectController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                   ProjectService) {
            // $rootScope.viewInfo.icon = "";
            $rootScope.viewInfo.title = "Projects";

            var vm = this;
            vm.project = null;
            $scope.project = null;

            (function () {

            })();
        }
    }
);