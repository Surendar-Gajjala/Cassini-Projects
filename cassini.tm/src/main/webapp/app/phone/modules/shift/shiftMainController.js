define(
    [
        'app/phone/modules/shift/shift.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application'
    ],
    function(module) {
        module.controller('ShiftMainController', ShiftMainController);

        function ShiftMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {
            var vm = this;

            (function() {
                if($application.homeLoaded == false) {

                }
            })();
        }
    }
);
