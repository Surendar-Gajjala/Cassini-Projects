define(['app/desktop/modules/im/im.module'
    ],
    function (module) {
        module.controller('StoresController', StoresController);

        function StoresController($scope, $rootScope, $timeout, $state, $cookies) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-shopping-cart";
            $rootScope.viewInfo.title = "Stores";

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);