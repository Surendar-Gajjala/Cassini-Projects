define(
    [
        'app/desktop/modules/procurement/procurement.module'
    ],
    function (module) {
        module.controller('ProcurementMainController', ProcurementMainController);

        function ProcurementMainController($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            if ($application.homeLoaded == false) {
                return;
            }

            (function () {

            })();
        }
    }
);