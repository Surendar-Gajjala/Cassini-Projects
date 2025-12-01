define(
    [
        'app/desktop/modules/media/media.module'
    ],
    function (module) {
        module.controller('MainController', MainController);

        function NewController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;


            (function () {

            })();
        }
    }
);