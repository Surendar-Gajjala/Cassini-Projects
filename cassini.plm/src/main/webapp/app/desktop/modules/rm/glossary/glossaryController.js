define(
    [
        'app/desktop/modules/rm/rm.module'
    ],
    function (module) {
        module.controller('GlossaryController', GlossaryController);

        function GlossaryController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            $rootScope.viewInfo.title = "Terminology";

            var vm = this;
            vm.project = null;
            $scope.project = null;

            (function () {

            })();
        }
    }
);