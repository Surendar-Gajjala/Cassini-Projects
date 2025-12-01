define(
    [
        'app/phone/modules/accomm/accomm.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application'
    ],
    function(module) {
        module.controller('AccommodationMainController', AccommodationMainController);

        function AccommodationMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application) {

            $rootScope.viewName = "Accommodation";
            $rootScope.backgroundColor = "#607D8B";

            var vm = this;

            (function() {
                if($application.homeLoaded == false) {

                }
            })();
        }
    }
);
