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
        module.controller('UpdateEmailController', UpdateEmailController);

        function UpdateEmailController($scope, $rootScope, $state, $stateParams, $timeout, $cookies, $translate,
                                       $application, LoginService, CommonService) {
            var vm = this;

            vm.hasError = false;
            vm.hasSuccess = false;
            vm.successMessage = "";
            vm.errorMessage = "";
            vm.emailChanged = false;

            vm.email = null;
            vm.passcodeSent = false;
            vm.verifyPasscode = null;
            vm.resetEmail = resetEmail;
            vm.cancel = cancel;
            var parsed = angular.element("<div></div>");
            $rootScope.emailValidationMessage = parsed.html($translate.instant("EMAIL_VALIDATION")).html();
            $scope.submitTitle = parsed.html($translate.instant("SUBMIT")).html();
            $scope.verifyTitle = parsed.html($translate.instant("VERIFY")).html();
            var otpErrorMessage = parsed.html($translate.instant("PASSCODE")).html();
            vm.submitText = $scope.submitTitle;
            $rootScope.emailValidMessage = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var passcodeSentMsg = parsed.html($translate.instant("PASSCODE_SEND_SUCCESSFULLY")).html();
            var emailUpdatedAndPasscodeSent = parsed.html($translate.instant("EMAIL_UPDATED_PASSCODE_SEND_SUCCESSFULLY")).html();
            $scope.enterPasscodePlaceHolder = parsed.html($translate.instant("ENTER_PASSCODE")).html();

            function cancel() {
                LoginService.logout().then(
                    function (success) {
                        $state.go('login', {}, {reload: true});
                    },
                    function (error) {
                        $state.go('login', {}, {reload: true});
                    }
                );
            }

            function resetEmail() {
                if ($rootScope.emailFormType == 'update' && vm.submitText === $scope.submitTitle) {
                    if (vm.email != null && validateEmail() == true) {
                        vm.hasError = false;
                        vm.passcodeSent = false;
                        $rootScope.personObject.email = vm.email;
                        $rootScope.personObject.sendPasscode = true;
                        $rootScope.showBusy($('.login-card'));
                        CommonService.updatePerson($rootScope.personObject).then(
                            function (data) {
                                vm.passcodeSent = true;
                                vm.hasSuccess = true;
                                vm.submitText = $scope.verifyTitle;
                                vm.emailChanged = false;
                                vm.successMessage = emailUpdatedAndPasscodeSent;
                                $timeout(function () {
                                    vm.hasSuccess = false;
                                    vm.successMessage = "";
                                }, 5000)
                                $rootScope.hideBusy();
                            },
                            function (error) {
                                vm.hasError = true;
                                vm.errorMessage = error.message;
                                $rootScope.hideBusy();
                            }
                        )

                    }
                    else if (vm.email == null || vm.email == "") {
                        vm.hasError = true;
                        vm.errorMessage = $rootScope.emailValidationMessage;
                    }
                } else {
                    verifyEmail();
                }
            }


            function validateEmail() {
                var valid = true;
                var atpos = vm.email.indexOf("@");
                var dotpos = vm.email.lastIndexOf(".");
                if (vm.email != null && vm.email != undefined && vm.email != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        valid = false;
                        vm.hasError = true;
                        vm.errorMessage = $rootScope.emailValidMessage;
                    }
                    else {
                        valid = true;
                    }
                }
                return valid
            }

            vm.verifyEmail = verifyEmail;
            function verifyEmail() {
                if (vm.verifyPasscode != null && vm.verifyPasscode != "" && vm.verifyPasscode != undefined) {
                    $rootScope.showBusy($('.login-card'));
                    LoginService.verifyPersonEmail($rootScope.personObject.id, vm.verifyPasscode).then(
                        function (data) {
                            if ($application.session != null && $application.session != undefined && $application.session.hasTwoFactorAuthentication) {
                                $application.session.login.person.emailVerified = true;
                                $rootScope.showTwoFactorAuthentication("sendPasscode");
                            } else {
                                $state.go('app.home', {}, {reload: true});
                                $rootScope.hideBusy();
                            }
                        }, function (error) {
                            vm.hasError = true;
                            vm.errorMessage = error.message;
                            $rootScope.hideBusy();
                        }
                    )
                } else {
                    vm.hasError = true;
                    vm.errorMessage = otpErrorMessage;
                }
            }

            $rootScope.resendVerificationCode = resendVerificationCode;
            function resendVerificationCode() {
                vm.verifyPasscode = null;
                vm.hasError = false;
                vm.hasSuccess = false;
                $rootScope.showBusy($('.login-card'));
                LoginService.resendPersonEmailPasscode($rootScope.personObject.id).then(
                    function (data) {
                        vm.hasSuccess = true;
                        vm.passcodeSent = true;
                        var email = $rootScope.personObject.email;
                        var atIndex = email.lastIndexOf("@");
                        var lastTwoCharacters = email.substring(atIndex - 2, atIndex);
                        var removeFirstAndLastTwoChar = email.substring(2, atIndex - 2);
                        var replaceChars = Array(removeFirstAndLastTwoChar.length + 1).join('x');
                        var firstTwoCharacters = email.substring(0, 2);
                        var afterAtIndex = email.substring(atIndex);
                        var modifiedEmail = firstTwoCharacters + replaceChars + lastTwoCharacters + afterAtIndex;
                        vm.successMessage = passcodeSentMsg.format(modifiedEmail);
                        vm.submitText = $scope.verifyTitle;
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


            (function () {
            })();

        }
    }
);