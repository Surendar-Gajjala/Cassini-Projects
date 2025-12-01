define(['app/desktop/modules/sharing/sharing.module'

    ],
    function (module) {
        module.controller('SharingMainController', SharingMainController);

        function SharingMainController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $cookies) {
            $rootScope.viewInfo.icon = "las la-share";
            $rootScope.viewInfo.title = $translate.instant("SHARING");

            var vm = this;

            (function () {

            })();
        }
    }
);