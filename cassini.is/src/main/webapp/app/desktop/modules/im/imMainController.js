define(['app/desktop/modules/im/im.module'
    ],
    function (module) {
        module.controller('ImMainController', ImMainController);

        function ImMainController($scope, $rootScope, $timeout, $state, $cookies) {
            var vm = this;

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);