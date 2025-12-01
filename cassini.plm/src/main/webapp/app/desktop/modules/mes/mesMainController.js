define(
    [
        'app/desktop/modules/mes/mes.module'
    ],
    function (module) {
        module.controller('MesMainController', MesMainController);

        function MesMainController($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            //$rootScope.viewInfo.icon = "flaticon-contract11";
            $rootScope.viewInfo.title = "";

            var vm = this;

            (function () {

            })();
        }
    }
);