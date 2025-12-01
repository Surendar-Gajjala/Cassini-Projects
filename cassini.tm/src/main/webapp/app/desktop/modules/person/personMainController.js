define(['app/desktop/modules/person/person.module',
        'app/shared/services/app/application'

    ],
    function (module) {
        module.controller('PersonMainController', PersonMainController);

        function PersonMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application) {

            var vm = this;
            $rootScope.viewInfo.icon = "fa flaticon-businessman276";
            $rootScope.viewInfo.title = "Person";


            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);