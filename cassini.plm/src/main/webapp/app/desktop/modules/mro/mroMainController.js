define(
    [
        'app/desktop/modules/mro/mro.module'
    ],
    function (module) {
        module.controller('MROMainController', MROMainController);

        function MROMainController($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            //$rootScope.viewInfo.icon = "flaticon-contract11";
            $rootScope.viewInfo.title = "";

            var vm = this;

            (function () {

            })();
        }
    }
);