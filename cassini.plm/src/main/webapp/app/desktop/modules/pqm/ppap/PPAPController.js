define(
    [
        'app/desktop/modules/pqm/pqm.module'
    ],
    function (module) {
        module.controller('PPAPController', PPAPController);

        function PPAPController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                ProjectService) {
            $rootScope.viewInfo.showDetails = false;

            var vm = this;
            vm.project = null;
            $scope.project = null;

            (function () {

            })();
        }
    }
);