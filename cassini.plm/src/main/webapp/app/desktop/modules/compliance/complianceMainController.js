define(
    [
        'app/desktop/modules/compliance/compliance.module'
    ],
    function (module) {
        module.controller('ComplianceMainController', ComplianceMainController);

        function ComplianceMainController($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            //$rootScope.viewInfo.icon = "flaticon-contract11";
            $rootScope.viewInfo.title = "";

            var vm = this;

            (function () {

            })();
        }
    }
);