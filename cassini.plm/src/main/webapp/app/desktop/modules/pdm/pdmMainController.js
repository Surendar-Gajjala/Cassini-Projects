define(
    [
        'app/desktop/modules/pqm/pqm.module'
    ],
    function (module) {
        module.controller('PDMMainController', PDMMainController);

        function PDMMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {
            $rootScope.viewInfo.title = "";

            var vm = this;

            (function () {

            })();
        }
    }
);