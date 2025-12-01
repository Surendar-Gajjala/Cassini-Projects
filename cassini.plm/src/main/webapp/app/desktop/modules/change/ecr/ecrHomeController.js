define(
    [
        'app/desktop/modules/change/change.module'
    ],
    function (module) {
        module.controller('ECRHomeController', ECRHomeController);

        function ECRHomeController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            var vm = this;
            vm.project = null;
            $scope.project = null;

            (function () {

            })();
        }
    }
);