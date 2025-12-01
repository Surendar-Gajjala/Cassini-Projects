define(
    [
        'app/desktop/modules/pqm/pqm.module'
    ],
    function (module) {
        module.controller('QCRController', QCRController);

        function QCRController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                               ProjectService) {
            // $rootScope.viewInfo.icon = "";

            var vm = this;
            vm.project = null;
            $scope.project = null;

            (function () {

            })();
        }
    }
);