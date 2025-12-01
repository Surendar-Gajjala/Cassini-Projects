define(
    [
        'app/desktop/modules/request/request.module'
    ],
    function (module) {
        module.controller('RequestMainController', RequestMainController);

        function RequestMainController ($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            if ($application.homeLoaded == false) {
                return;
            }

            (function() {

            })();
        }
    }
);