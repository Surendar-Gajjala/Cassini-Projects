define(
    [
        'app/phone/modules/dept/dept.module'
    ],
    function(module) {
        module.controller('DeptDetailsController', DeptDetailsController);

        function ShiftDetailsCoDeptDetailsControllerntroller($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {
            var vm = this;


            (function() {
                if($application.homeLoaded == true) {

                }
            })();
        }
    }
);