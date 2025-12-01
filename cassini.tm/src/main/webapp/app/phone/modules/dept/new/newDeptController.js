define(
    [
        'app/phone/modules/dept/dept.module'
    ],
    function(module) {
        module.controller('NewDeptController', NewDeptController);

        function NewDeptController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {
            var vm = this;



            (function() {
                if($application.homeLoaded == true) {

                }
            })();
        }
    }
);
