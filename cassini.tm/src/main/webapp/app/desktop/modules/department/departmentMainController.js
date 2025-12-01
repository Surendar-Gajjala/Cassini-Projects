/**
 * Created by Anusha on 04-08-2016.
 */
define(['app/desktop/modules/department/department.module',
        'app/shared/services/app/application'

    ],
    function(module) {
        module.controller('DepartmentMainController', DepartmentMainController);

        function DepartmentMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {


            var vm = this;



            (function() {
                if($application.homeLoaded == false) {

                }
            })();
        }
    }
);

