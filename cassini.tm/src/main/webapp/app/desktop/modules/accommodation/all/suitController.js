define(['app/desktop/modules/accommodation/accommodation.module',
        'app/shared/services/accommodationService'
    ],
    function(module) {
        module.controller('SuitController', SuitController);

        function SuitController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,AccommodationService) {
            var vm = this;


            (function() {
                if($application.homeLoaded == true) {

                }
            })();
        }
    }
);