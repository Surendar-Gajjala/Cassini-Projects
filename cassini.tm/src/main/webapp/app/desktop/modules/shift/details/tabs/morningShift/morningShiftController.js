define(
    [
        'app/desktop/modules/shift/shift.module',
        'app/shared/services/shiftService'
    ],
    function (module) {
        module.controller('MorningShiftController', MorningShiftController);

        function MorningShiftController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {
            var vm = this;



            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);