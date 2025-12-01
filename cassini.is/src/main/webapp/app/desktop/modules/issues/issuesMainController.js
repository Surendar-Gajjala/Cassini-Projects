define(['app/desktop/modules/issues/issues.module'],

    function (module) {
        module.controller('IssuesMainController', IssuesMainController);

        function IssuesMainController($scope, $rootScope, $timeout, $state, $cookies) {
            var vm = this;

            $rootScope.viewInfo.icon = "flaticon-marketing8";
            $rootScope.viewInfo.title = "Issues";

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);