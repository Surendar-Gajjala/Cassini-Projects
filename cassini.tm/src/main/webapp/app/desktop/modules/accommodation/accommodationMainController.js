
define(['app/desktop/modules/accommodation/accommodation.module'

    ],
    function(module) {
        module.controller('AccommodationMainController', AccommodationMainController);

        function AccommodationMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {
            $rootScope.viewInfo.icon = "fa fa-tasks";
            $rootScope.viewInfo.title = "Accommodation";


            var vm = this;


            (function() {
                if($application.homeLoaded == false) {

                }
            })();
        }
    }
);
