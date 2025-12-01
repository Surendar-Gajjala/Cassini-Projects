define(
    [
        'app/desktop/modules/pm/pm.module'
    ],
    function (module) {
        module.controller('ProgramController', ProgramController);

        function ProgramController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                   ProjectService) {
            // $rootScope.viewInfo.icon = "";
            $rootScope.viewInfo.title = "Programs";

            var vm = this;
            vm.project = null;
            $scope.project = null;

            (function () {

            })();
        }
    }
);