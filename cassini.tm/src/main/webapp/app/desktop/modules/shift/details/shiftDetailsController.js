define(['app/desktop/modules/shift/shift.module'
    ],
    function(module) {
        module.controller('ShiftDetailsController', ShiftDetailsController);

        function ShiftDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {
            var vm = this;



            (function() {
                if($application.homeLoaded == true) {

                }
            })();
        }
    }
);