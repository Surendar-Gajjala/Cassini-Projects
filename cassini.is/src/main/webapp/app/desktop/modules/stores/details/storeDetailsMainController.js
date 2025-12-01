define(['app/desktop/modules/stores/store.module'
    ],
    function (module) {
        module.controller('StoreDetailsMainController', StoreDetailsMainController);

        function StoreDetailsMainController($scope, $rootScope, $timeout, $state, $cookies) {

            $rootScope.viewInfo.title = "Store Received Item Details";
            var vm = this;

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);