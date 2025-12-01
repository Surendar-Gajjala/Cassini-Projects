define(
    [
        'app/phone/modules/task/task.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application'
    ],
    function(module) {
        module.controller('TaskMainController', TaskMainController);

        function TaskMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {

            $rootScope.viewName = "Tasks";
            $rootScope.backgroundColor = "#4E342E";

            var vm = this;

            (function() {
                if($application.homeLoaded == false) {

                }
            })();
        }
    }
);
