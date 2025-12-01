/**
 * Created by Anusha on 11-07-2016.
 */
define(['app/desktop/modules/project/project.module',
        'app/shared/services/app/application'

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
