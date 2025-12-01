define(
    [
        'app/desktop/modules/reqTemplate/reqDocTemplate.module'
    ],
    function (module) {
        module.controller('ReqDocTemplateMainController', ReqDocTemplateMainController);

        function ReqDocTemplateMainController($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            $rootScope.viewInfo.icon = "fa flaticon-debit-card";
            $rootScope.viewInfo.title = "Requirement Document Templates";
            $rootScope.viewInfo.showDetails = false;


            (function () {

            })();
        }
    }
);