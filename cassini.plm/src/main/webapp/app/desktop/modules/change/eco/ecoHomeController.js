define(
    [
        'app/desktop/modules/change/change.module'
    ],
    function (module) {
        module.controller('ECOHomeController', ECOHomeController);

        function ECOHomeController($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            $rootScope.viewInfo.icon = "fa flaticon-contract11";
            $rootScope.viewInfo.title = "ECOs";

            (function () {

            })();
        }
    }
);