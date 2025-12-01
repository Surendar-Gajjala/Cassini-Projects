define(
    [
        'app/phone/modules/dept/dept.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application'
    ],
    function(module) {
        module.controller('DeptMainController', DeptMainController);

        function DeptMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {

            $rootScope.viewName = "Departments";
            $rootScope.backgroundColor = "#F57F17";

            var vm = this;

            (function() {
                if($application.homeLoaded == false) {

                }
            })();
        }
    }
);
