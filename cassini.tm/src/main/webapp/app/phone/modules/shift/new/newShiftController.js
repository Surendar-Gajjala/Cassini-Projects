define(
    [
        'app/phone/modules/shift/shift.module'
    ],
    function(module) {
        module.controller('NewShiftController', NewShiftController);

        function NewShiftController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {
            var vm = this;


            (function() {
                if($application.homeLoaded == true) {

                }
            })();
        }
    }
);
