define(['app/desktop/modules/mfr/mfr.module'

    ],
    function (module) {
        module.controller('MfrMainController', MfrMainController);

        function MfrMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {
            $rootScope.viewInfo.icon = "fa flaticon-office42";
            $rootScope.viewInfo.title = "Manufacturers";

            var vm = this;

            (function () {

            })();
        }
    }
);