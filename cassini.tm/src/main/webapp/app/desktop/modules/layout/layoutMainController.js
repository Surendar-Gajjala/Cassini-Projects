/**
 * Created by Anusha on 12-08-2016.
 */
define(['app/desktop/modules/layout/layout.module'

    ],
    function(module) {
        module.controller('LayoutMainController', LayoutMainController);

        function LayoutMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,$application) {


            var vm = this;

            $rootScope.viewInfo.icon = "fa calendar";
            $rootScope.viewInfo.title = "Layout";


            (function() {
                if($application.homeLoaded == false) {

                }
            })();
        }
    }
);