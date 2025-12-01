define(['app/desktop/modules/stores/store.module'
    ],
    function (module) {
        module.controller('StoreMainController', StoreMainController);

        function StoreMainController($scope, $rootScope, $timeout, $state, $cookies) {
            $rootScope.setViewType('APP');
            $rootScope.viewInfo.title = "Stores";
            var vm = this;

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);