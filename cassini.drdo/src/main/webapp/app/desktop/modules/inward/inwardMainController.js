define(
    [
        'app/desktop/modules/inward/inward.module'
    ],
    function (module) {
        module.controller('InwardMainController', InwardMainController);

        function InwardMainController ($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            if ($application.homeLoaded == false) {
                return;
            }

            (function() {

            })();
        }
    }
);