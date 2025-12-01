define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module'],

    function(module){

        module.controller('AdminSettingsController',AdminSettingsController);

        function AdminSettingsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal,
                                    LoginService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-key";
            $rootScope.viewInfo.title = "Login";



            (function() {

            })();
        }
    }
)