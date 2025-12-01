define(
    [
        'app/desktop/modules/req/req.module'
    ],
    function (module) {
        module.controller('ReqMainController', ReqMainController);

        function ReqMainController($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            $rootScope.viewInfo.icon = "fa flaticon-debit-card";
            $rootScope.viewInfo.title = "Requirement Documents";
            $rootScope.viewInfo.showDetails = false;



            (function () {

            })();
        }
    }
);