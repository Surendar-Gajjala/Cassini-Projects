define(['app/desktop/modules/stores/store.module'
    ],
    function (module) {
        module.controller('StoreStockController', StoreStockController);

        function StoreStockController($scope, $rootScope, $timeout, $state, $cookies) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-shopping-cart";
            $rootScope.viewInfo.title = "Stock details";

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);