define(
    [
        'app/desktop/modules/dashboard/dashboard.module'
    ],
    function (module) {
        module.controller('DashboardController', DashboardController);

        function DashboardController($scope, $rootScope, $timeout, $compile, $sce, $interval, $state, $cookies, $window, $translate, $application) {

            $rootScope.viewInfo.showDetails = false;

            var vm = this;

            (function () {

            })();

        }
    }
);