define(
    [
        'app/desktop/modules/pdm/pdm.module'
    ],
    function (module) {
        module.controller('AssembliesController', AssembliesController);

        function AssembliesController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {
            $rootScope.viewInfo.title = "";

            var vm = this;

            (function () {

            })();
        }
    }
);