/**
 * Created by SRAVAN on 7/30/2018.
 */
define(['app/desktop/modules/run/run.module'

    ],

    function (module) {

        module.controller('RunMainController', RunMainController);

        function RunMainController($scope, $rootScope, $timeout, $interval, $state, $cookies, $application) {
            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;
            $rootScope.viewInfo.icon = "";
            $rootScope.viewInfo.title = "Test Run";

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();

        }

    }
);