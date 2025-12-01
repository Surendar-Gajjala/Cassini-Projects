define(
    [
        'app/phone/modules/accomm/accomm.module'
    ],
    function(module) {
        module.controller('NewAccommodationController', NewAccommodationController);

        function NewAccommodationController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {
            var vm = this;



            (function() {
                if($application.homeLoaded == true) {

                }
            })();
        }
    }
);
