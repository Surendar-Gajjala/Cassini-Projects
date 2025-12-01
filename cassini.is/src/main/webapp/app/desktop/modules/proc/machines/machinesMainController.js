define(['app/desktop/modules/proc/proc.module'
    ],
    function (module) {
        module.controller('MachinesMainController', MachinesMainController);

        function MachinesMainController($scope, $rootScope, $timeout, $state, $cookies) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-truck";
            $rootScope.viewInfo.title = "Machine";
            $rootScope.setViewType('PROCUREMENT');

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$broadcast('app.activate.procurement', {project: {name: 'Procurement'}})
                }
            })();
        }
    }
);