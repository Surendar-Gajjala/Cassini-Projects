define(
    [
        'app/desktop/modules/po/po.module'
    ],
    function (module) {
        module.controller('ActivityMainController', ActivityMainController);

        function ActivityMainController($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            $rootScope.viewInfo.icon = "fa fa-cubes";
            $rootScope.viewInfo.title = "Activities";

            (function () {

            })();
        }
    }
);