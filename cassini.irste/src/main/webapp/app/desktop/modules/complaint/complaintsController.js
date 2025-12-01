/**
 * Created by Nageshreddy on 08-11-2018.
 */
define(
    [
        'app/desktop/modules/home/home.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('ComplaintsController', ComplaintsController);

        function ComplaintsController($scope, $rootScope, $timeout, $window, $state, $cookies, $uibModal) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-wpforms";
            $rootScope.viewInfo.title = "Complaints";


            (function () {
            })();

        }
    }
);