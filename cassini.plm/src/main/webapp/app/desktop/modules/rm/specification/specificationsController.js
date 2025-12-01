define(
    [
        'app/desktop/modules/rm/rm.module'
    ],
    function (module) {
        module.controller('SpecificationsController', SpecificationsController);

        function SpecificationsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate) {

            $rootScope.viewInfo.icon = "fa flaticon-debit-card";
            $rootScope.viewInfo.title = $translate.instant('SPECIFICATIONS');
            /*
             * startup file
             * */
            var vm = this;
            vm.project = null;
            $scope.project = null;

            (function () {

            })();
        }
    }
);