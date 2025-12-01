define(
    [
        'app/phone/modules/project/project.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application'

    ],
    function(module) {
        module.controller('ProjectMainController', ProjectMainController);

        function ProjectMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application) {



            var vm = this;



            (function() {
                if($application.homeLoaded == true) {

                }
            })();
        }
    }
);
