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
        module.controller('TwoFactorAuthenticationController', TwoFactorAuthenticationController);

        function TwoFactorAuthenticationController($scope, $rootScope, $state, $stateParams, $timeout, $cookies, $translate,
                                                   $application, LoginService, CommonService) {
            var vm = this;

            vm.hasError = false;
            vm.hasSuccess = false;
            vm.disableResend = false;
            vm.errorMessage = "";
            vm.successMessage = "";

            vm.passcode = null;
            vm.submitText = 'Submit';

            vm.checkTwoFactorAuthentication = checkTwoFactorAuthentication;
            $rootScope.resetTwoFactorAuthentication = resetTwoFactorAuthentication;
            vm.cancel = cancel;
            var parsed = angular.element("<div></div>");
            var otpErrorMessage = parsed.html($translate.instant("PASSCODE")).html();
            $scope.enterPasscodePlaceHolder = parsed.html($translate.instant("ENTER_PASSCODE")).html();
            var passcodeSentMsg = parsed.html($translate.instant("PASSCODE_SEND_SUCCESSFULLY")).html();


            function cancel() {
                LoginService.logout().then(
                    function (success) {
                        $state.go('login', {}, {reload: true});
                    },
                    function (error) {
                    }
                );
            }

            function resetTwoFactorAuthentication() {
                vm.passcode = null;
                vm.hasError = false;
                vm.hasSuccess = false;
                vm.disableResend = true;
                $rootScope.showBusy($('.login-card'));
                LoginService.resetTwoFactorAuthenticationPassword($application.session.login.id).then(
                    function (data) {
                        vm.hasSuccess = true;
                        var email = $application.session.login.person.email;
                        var atIndex = email.lastIndexOf("@");
                        var lastTwoCharacters = email.substring(atIndex - 2, atIndex);
                        var removeFirstAndLastTwoChar = email.substring(2, atIndex - 2);
                        var replaceChars = Array(removeFirstAndLastTwoChar.length + 1).join('x');
                        var firstTwoCharacters = email.substring(0, 2);
                        var afterAtIndex = email.substring(atIndex);
                        var modifiedEmail = firstTwoCharacters + replaceChars + lastTwoCharacters + afterAtIndex;
                        vm.successMessage = passcodeSentMsg.format(modifiedEmail);
                        vm.disableResend = false;
                        $timeout(function () {
                            vm.hasSuccess = false;
                        }, 5000);
                        $rootScope.hideBusy();
                    }, function (error) {
                        vm.hasError = true;
                        vm.hasSuccess = false;
                        vm.errorMessage = error.message;
                        $rootScope.hideBusy();
                    }
                )
            }

            function checkTwoFactorAuthentication() {
                if (vm.passcode != null && vm.passcode != "" && vm.passcode != undefined) {
                    $rootScope.showBusy($('.login-card'));
                    LoginService.verifyTwoFactorAuthenticationPassword($application.session.login.id, vm.passcode).then(
                        function () {
                            $state.go('app.home', {}, {reload: true});
                            $rootScope.hideBusy();
                        },
                        function (error) {
                            vm.hasError = true;
                            vm.errorMessage = error.message;
                            $rootScope.hideBusy();
                        }
                    )

                }
                else {
                    vm.hasError = true;
                    vm.errorMessage = otpErrorMessage;
                }
            }

            (function () {

            })();

        }
    }
);