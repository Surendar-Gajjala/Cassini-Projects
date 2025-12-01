define(['app/desktop/modules/items/item.module'
    ],
    function (module) {
        module.controller('ItemMainController', ItemMainController);

        function ItemMainController ($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            if ($application.homeLoaded == false) {
                return;
            }

            (function() {

            })();
        }
    }
);