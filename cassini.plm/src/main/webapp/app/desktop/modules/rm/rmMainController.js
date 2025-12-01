define(
    [
        'app/desktop/modules/rm/rm.module'
    ],
    function (module) {
        module.controller('RMMainController', RMMainController);

        function RMMainController($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            $rootScope.viewInfo.icon = "fa flaticon-debit-card";
            $rootScope.viewInfo.title = "Requirements";

            (function () {

            })();
        }
    }
);