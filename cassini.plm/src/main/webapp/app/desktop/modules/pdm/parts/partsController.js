define(
    [
        'app/desktop/modules/pdm/pdm.module'
    ],
    function (module) {
        module.controller('PartsController', PartsController);

        function PartsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {
            $rootScope.viewInfo.title = "";

            var vm = this;

            (function () {

            })();
        }
    }
);