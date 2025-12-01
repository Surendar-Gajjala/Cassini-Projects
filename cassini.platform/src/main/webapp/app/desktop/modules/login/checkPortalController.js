define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/js/utils/uiUtils'
    ],
    function (module) {
        module.controller('CheckPortalController', CheckPortalController);

        function CheckPortalController($scope, $rootScope, $state, $stateParams, $timeout, $cookies, $translate,
                                       $application, LoginService, CommonService) {
            var vm = this;

            vm.portalAccount = {
                email: null,
                password: null,
                authKey: null
            };

            vm.errorMessage = "Missing or invalid portal information. Enter correct portal information or contact your administrator.";

            vm.setupPortalAccount = setupPortalAccount;
            function setupPortalAccount() {
                vm.errorMessage = null;
                LoginService.setupPortalAccount(vm.portalAccount).then (
                    function (success) {
                        $rootScope.view = "login";
                    },
                    function (error) {
                        vm.errorMessage = error.message;
                    }
                )
            }

            vm.cancel = cancel;
            function cancel() {
                $rootScope.view = "login";
            }

            (function () {
            })();

        }
    }
);