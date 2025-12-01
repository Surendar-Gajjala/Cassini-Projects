define(
    [
        'app/desktop/modules/gatePass/gatePass.module'
    ],
    function (module) {
        module.controller('GatePassMainController', GatePassMainController);

        function GatePassMainController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;


            (function () {

            })();
        }
    }
)
;

