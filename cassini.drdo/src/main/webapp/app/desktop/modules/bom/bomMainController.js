define(['app/desktop/modules/bom/bom.module'
    ],
    function (module) {
        module.controller('BomMainController', BomMainController);

        function BomMainController($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            if ($application.homeLoaded == false) {
                return;
            }

            (function () {

            })();
        }
    }
);