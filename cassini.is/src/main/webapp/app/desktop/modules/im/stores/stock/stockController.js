define(['app/desktop/modules/im/im.module'
    ],
    function (module) {
        module.controller('StockController', StockController);

        function StockController($scope, $rootScope, $timeout, $state, $cookies) {
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