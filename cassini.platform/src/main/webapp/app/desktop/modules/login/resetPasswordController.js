define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/js/utils/uiUtils'
    ],
    function (module) {
        module.controller('ResetPasswordController', ResetPasswordController);

        function ResetPasswordController($scope, $rootScope, $state, $stateParams, $timeout, $cookies, $translate,
                                         $application, LoginService) {
            var vm = this;

            vm.hasError = false;
            vm.errorMessage = "";
            var parsed = angular.element("<div></div>");
            vm.loginName = null;
            vm.processing = false;
            vm.showPasscode = false;
            vm.submitText = parsed.html($translate.instant("SUBMIT")).html();
            var usernameErrorMessage = $translate.instant("USER_NAME_VALIDATION");
            var otpErrorMessage = parsed.html($translate.instant("PASSCODE")).html();
            var validOtpErrorMessage = parsed.html($translate.instant("VALID_OTP")).html();
            var usernameFormateErrorMessage = parsed.html($translate.instant("USERNAME_FORMATE")).html();
            //vm.submitText = 'Submit';
            vm.resend = false;
            vm.otp = null;

            vm.resetPassword = resetPassword;
            vm.verifyOtp = verifyOtp;
            vm.cancel = cancel;

            function cancel() {
                $rootScope.view = 'login';
            }


            vm.loginDTO = {
                loginName: null,
                otp: null
            };
            function resetPassword() {
                if (vm.loginName == null || vm.loginName == '' || vm.loginName == " ") {
                    vm.processing = false;
                    vm.hasError = true;
                    vm.errorMessage = usernameErrorMessage;
                }
                else {
                    vm.processing = true;
                    vm.hasError = false;
                    vm.errroMessage = null;
                    vm.loginDTO.loginName = vm.loginName;
                    LoginService.resetPassword(vm.loginDTO).then(
                        function () {
                            vm.processing = false;
                            vm.showPasscode = true;
                            vm.submitText = parsed.html($translate.instant("RESEND_OTP")).html();
                            //vm.submitText = 'Resend OTP';
                            vm.resend = true;
                        },
                        function (error) {
                            vm.processing = false;
                            vm.hasError = true;
                            vm.errorMessage = error.message;
                        }
                    )
                }
            }

            function verifyOtp() {
                if (vm.otp == null || vm.otp == '' || vm.otp == " ") {
                    vm.processing = false;
                    vm.hasError = true;
                    vm.errorMessage = otpErrorMessage;
                }
                else if (vm.otp != null) {
                    //vm.otp = parseInt(vm.otp);
                    if (isNaN(vm.otp)) {
                        vm.processing = false;
                        vm.hasError = true;
                        vm.errorMessage = validOtpErrorMessage;
                    }
                    else {
                        vm.processing = true;
                        vm.hasError = false;
                        vm.errroMessage = null;
                        vm.loginDTO.loginName = vm.loginName;
                        vm.loginDTO.passcode = vm.otp;
                        LoginService.verifyOtp(vm.loginDTO).then(
                            function (data) {
                                $scope.$parent.$parent.verifiedOtp = data;
                                $scope.$parent.$parent.loginName = vm.loginName;
                                $scope.$parent.showNewPasswordView();
                            },
                            function (error) {
                                vm.processing = false;
                                vm.hasError = true;
                                vm.errorMessage = error.message;
                            }
                        )
                    }
                }
            }

            (function () {
            })();

        }
    }
);