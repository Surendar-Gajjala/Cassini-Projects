/**
 * Created by swapna on 03/10/18.
 */
define(['app/desktop/modules/reports/reports.module'
    ],
    function (module) {
        module.controller('ProjectMediaMainController', ProjectMediaMainController);

        function ProjectMediaMainController($scope, $rootScope, $timeout, $state, $cookies) {

            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-area-chart";
            $rootScope.viewInfo.title = "Reports";

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);