define(['app/app.modules'
    ],
    function (module) {
        module.controller('StoreMainController', StoreMainController);

        function StoreMainController($scope, $rootScope, $timeout, $state, $cookies) {

            //$rootScope.viewIcon = "fa fa-files-o";
            $rootScope.viewTitle = "Stores";


            (function () {
                /*if ($application.homeLoaded == true) {

                }*/
            })();
        }
    }
);