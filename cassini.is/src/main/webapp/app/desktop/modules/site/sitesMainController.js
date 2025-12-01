define(['app/desktop/modules/site/sites.module'
    ],
    function (module) {
        module.controller('SitesMainController', SitesMainController);

        function SitesMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-map-marker";
            $rootScope.viewInfo.title = "Sites";

            (function () {
                if ($application.homeLoaded == true) {
                }
            })();
        }
    }
);