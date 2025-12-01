define(
    [
        'app/phone/modules/shift/shift.module'
    ],
    function (module) {
        module.controller('AllShiftsController', AllShiftsController);

        function AllShiftsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application) {

            $rootScope.viewName = "Tasks";
            $rootScope.backgroundColor = "#4E342E";


            var vm = this;

            (function () {
                if ($application.homeLoaded == true) {
                }
            })();
        }
    }
);