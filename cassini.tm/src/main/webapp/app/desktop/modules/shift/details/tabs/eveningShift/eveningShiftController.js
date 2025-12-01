define(
    [
        'app/desktop/modules/shift/shift.module',
        'app/shared/services/shiftService'
    ],
    function (module) {
        module.controller('EveningShiftController', EveningShiftController);

        function EveningShiftController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {
            var vm = this;



            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);