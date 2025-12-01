define(
    [
        'app/desktop/modules/dispatch/dispatch.module'
    ],
    function (module) {
        module.controller('DispatchMainController', DispatchMainController);

        function DispatchMainController($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            if ($application.homeLoaded == false) {
                return;
            }

            (function () {

            })();
        }
    }
);