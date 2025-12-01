define(['app/desktop/modules/shift/shift.module',
        'app/shared/services/app/application'
    ],
    function(module) {
        module.controller('ShiftMainController', ShiftMainController);

        function ShiftMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {


            var vm = this;


            (function() {
                if($application.homeLoaded == false) {

                }
            })();
        }
    }
);
