define(
    [
        'app/desktop/modules/issue/issue.module'
    ],
    function (module) {
        module.controller('IssueMainController', IssueMainController);

        function IssueMainController ($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            if ($application.homeLoaded == false) {
                return;
            }

            (function() {

            })();
        }
    }
);