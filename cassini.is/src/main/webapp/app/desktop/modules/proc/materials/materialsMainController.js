define(['app/desktop/modules/proc/proc.module'
    ],
    function (module) {
        module.controller('MaterialsMainController', MaterialsMainController);

        function MaterialsMainController($scope, $rootScope, $timeout, $state, $cookies) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-th";
            $rootScope.viewInfo.title = "Materials";
            $rootScope.setViewType('PROCUREMENT');

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$broadcast('app.activate.procurement', {project: {name: 'Procurement'}})
                }
            })();
        }
    }
);