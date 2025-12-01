define(
    [
        'app/phone/modules/accomm/accomm.module'
    ],
    function(module) {
        module.controller('AccommodationDetailsController', AccommodationDetailsController);

        function AccommodationDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {
            var vm = this;


            (function() {
                if($application.homeLoaded == true) {

                }
            })();
        }
    }
);