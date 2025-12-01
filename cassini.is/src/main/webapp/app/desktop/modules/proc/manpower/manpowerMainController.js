define(['app/desktop/modules/proc/proc.module'
    ],
    function (module) {
        module.controller('ManpowerMainController', ManpowerMainController);

        function ManpowerMainController($scope, $rootScope, $timeout, $state, $cookies) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-users";
            $rootScope.viewInfo.title = "Manpower";
            $rootScope.setViewType('PROCUREMENT');

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$broadcast('app.activate.procurement', {project: {name: 'Procurement'}})
                }
            })();
        }
    }
);