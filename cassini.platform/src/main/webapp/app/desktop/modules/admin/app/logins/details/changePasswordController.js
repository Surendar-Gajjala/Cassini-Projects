define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'

    ],
    function ($app) {
        $app.controller('ChangePasswordController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$uibModalInstance', 'DialogService', 'LoginService', '$translate',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, $uibModalInstance, DialogService, LoginService, $translate) {

                    var vm = this;
                    var parsed = angular.element("<div></div>");
                    vm.submitChangePassword = submitChangePassword;
                    vm.cancelChangePassword = cancelChangePassword;
                    vm.cross = cross;

                    vm.passwordTitle = parsed.html($translate.instant("SHOW_PASSWORD")).html();
                    vm.confirmPasswordTitle = parsed.html($translate.instant("SHOW_PASSWORD")).html();

                    vm.password = {
                        oldPassword: "",
                        newPassword: "",
                        confirmPassword: ""
                    };
                    vm.error = {
                        hasError: false,
                        errorMessage: ""
                    };

                    function cross() {
                        vm.error.errorMessage = "";
                        vm.error.hasError = false;
                    }

                    var oldPasswordValidation = parsed.html($translate.instant("OLD_PASSWORD_VALIDATION")).html();
                    var newPasswordValidation = parsed.html($translate.instant("NEW_PASSWORD_VALIDATION")).html();
                    var cannotSamePasswordValidation = parsed.html($translate.instant("CANNOT_SAME_PASSWORD_VALIDATION")).html();
                    var notSamePasswordValidation = parsed.html($translate.instant("NOT_SAME_PASSWORD_VALIDATION")).html();
                    var passwordLength = parsed.html($translate.instant("NEW_PASSWORD_LENGTH")).html();
                    var passwordLowerCase = parsed.html($translate.instant("NEW_PASSWORD_LOWERCASE")).html();
                    var passwordUpperCase = parsed.html($translate.instant("NEW_PASSWORD_UPPERCASE")).html();
                    var passwordSpecialChar = parsed.html($translate.instant("NEW_PASSWORD_SPECIAL")).html();
                    var passwordNumber = parsed.html($translate.instant("NEW_PASSWORD_NUMBER")).html();
                    var confirmPasswordValidation = parsed.html($translate.instant("CONFIRM_PASSWORD_VALID")).html();
                    vm.passwordInformation = parsed.html($translate.instant("PASSWORD_INFORMATION")).html();
                    var passwordFormat = parsed.html($translate.instant("PASSWORD_VALIDATION_FORMAT")).html();

                    vm.invalidPassword = parsed.html($translate.instant("INVALID_PASSWORD")).html();
                    vm.invalidUsername = parsed.html($translate.instant("INVALID_USERNAME")).html();
                    var passwordStrength = /^[\s\S]{8,32}$/,
                        upper = /[A-Z]/,
                        lower = /[a-z]/,
                        number = /[0-9]/,
                        special = /[ !"#$%&'()*+,\-./:;<=>?@[\\\]^_`{|}~]/;

                    $rootScope.strengthPassword = strengthPassword;

                    function strengthPassword() {
                        var userPassword = document.getElementById("newPassword").value;
                        var meter = document.getElementById('password-strength-meter');

                        var score = 0;
                        if (upper.test(userPassword)) {
                            score++;
                        }
                        if (lower.test(userPassword)) {
                            score++;
                        }
                        if (number.test(userPassword)) {
                            score++;
                        }
                        if (special.test(userPassword)) {
                            score++;
                        }

                        meter.value = score;
                    }

                    function validatePassword() {
                        var valid = true;
                        if (vm.password.oldPassword.length <= 0) {
                            vm.error.errorMessage = oldPasswordValidation;
                            vm.error.hasError = true;
                            valid = false;
                        } else if (vm.password.newPassword.length <= 0) {
                            vm.error.errorMessage = newPasswordValidation;
                            vm.error.hasError = true;
                            valid = false;
                        } else if (vm.password.newPassword != null) {
                            /*if (vm.password.newPassword.length < 8) {
                                vm.error.errorMessage = passwordLength;
                                vm.error.hasError = true;
                                valid = false;
                            } else if (!upper.test(vm.password.newPassword)) {
                                vm.error.errorMessage = passwordUpperCase;
                                vm.error.hasError = true;
                                valid = false;
                            } else if (!lower.test(vm.password.newPassword)) {
                                vm.error.errorMessage = passwordLowerCase;
                                vm.error.hasError = true;
                                valid = false;
                            } else if (!number.test(vm.password.newPassword)) {
                                vm.error.errorMessage = passwordNumber;
                                vm.error.hasError = true;
                                valid = false;
                            } else if (!special.test(vm.password.newPassword)) {
                                vm.error.errorMessage = passwordSpecialChar;
                                vm.error.hasError = true;
                                valid = false;
                            } else if (vm.password.confirmPassword == null || vm.password.confirmPassword == "") {
                                vm.error.errorMessage = confirmPasswordValidation;
                                vm.error.hasError = true;
                                valid = false;*/
                            if(!$rootScope.validPassword){
                                vm.error.errorMessage = passwordFormat;
                                vm.error.hasError = true;
                                valid = false;
                            } else if (vm.password.oldPassword == vm.password.newPassword) {
                                vm.error.errorMessage = cannotSamePasswordValidation;
                                vm.error.hasError = true;
                                valid = false;
                            } else if (vm.password.newPassword != vm.password.confirmPassword) {
                                vm.error.errorMessage = notSamePasswordValidation;
                                vm.error.hasError = true;
                                vm.passwordSafeCharacters = false;
                                valid = false;
                            }
                            /*else if (safeCharsPasswordCheck() == false) {
                             vm.passwordSafeCharacters = true;
                             valid = false;
                             }*/
                        }

                        $timeout(function () {
                            cross();
                        }, 5000);

                        return valid;
                    }


                    //Check Password has Special Characters
                    vm.passwordSafeCharacters = false;
                    vm.safeCharsPasswordCheck = safeCharsPasswordCheck;
                    function safeCharsPasswordCheck() {
                        var userPassword = document.getElementById("newPassword").value;
                        var regex = /^[a-zA-Z0-9!*:,'_~( )@\*\)\(_\-\s\u00E4\u00F6\u00FC\u00C4\u00D6\u00DC\u00df]+$/g;
                        // var match = getValue().match(regex);
                        if (userPassword.match(regex) == null || userPassword.includes(" ")) {
                            return false;
                        }
                        else {
                            vm.passwordSafeCharacters = false;
                            return true;
                        }

                    }

                    var changePasswordMessage = parsed.html($translate.instant("CHANGE_PASSWORD_MESSAGE")).html();

                    vm.showPassword = showPassword;
                    function showPassword() {
                        var eyeIcon = document.getElementById("showPassword");
                        var newPassword = document.getElementById("newPassword");
                        if (eyeIcon.attributes.class.nodeValue == "fa fa-fw fa-eye") {
                            eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye-slash";
                            eyeIcon.title = parsed.html($translate.instant("SHOW_PASSWORD")).html();
                            /*vm.passwordTitle = "Show Password";*/
                        } else {
                            eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye";
                            eyeIcon.title = parsed.html($translate.instant("HIDE_PASSWORD")).html();
                           /* vm.passwordTitle = "Hide Password";*/
                        }

                        if (newPassword.attributes.type.nodeValue == "password") {
                            newPassword.attributes.type.nodeValue = "text";
                        } else {
                            newPassword.attributes.type.nodeValue = "password";
                        }
                    }

                    vm.showConfirmPassword = showConfirmPassword;
                    function showConfirmPassword() {
                        var eyeIcon = document.getElementById("showConfirmPassword");
                        var newPassword = document.getElementById("confirmPassword");
                        if (eyeIcon.attributes.class.nodeValue == "fa fa-fw fa-eye") {
                            eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye-slash";
                            eyeIcon.title = parsed.html($translate.instant("SHOW_PASSWORD")).html();
                           /* vm.confirmPasswordTitle = "Show Password";*/
                        } else {
                            eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye";
                            eyeIcon.title = parsed.html($translate.instant("HIDE_PASSWORD")).html();
                           /* vm.confirmPasswordTitle = "Hide Password";*/
                        }

                        if (newPassword.attributes.type.nodeValue == "password") {
                            newPassword.attributes.type.nodeValue = "text";
                        } else {
                            newPassword.attributes.type.nodeValue = "password";
                        }
                    }


                    function performLogout() {
                        $uibModalInstance.close();
                        $rootScope.showSuccessMessage(changePasswordMessage);
                        $timeout(function () {
                            LoginService.logout().then(
                                function (success) {
                                    $rootScope.$broadcast("app.notifications.logout");
                                    $state.go('login', {}, {reload: true});
                                },
                                function (error) {
                                    console.error(error);
                                }
                            );
                        }, 5000);

                        /*DialogService.confirmChangePasswordLogout(function (yes) {
                         if (yes == true) {

                         LoginService.logout().then(
                         function (success) {
                         $rootScope.$broadcast("app.notifications.logout");
                         $state.go('login', {}, {reload: true});
                         },

                         function (error) {
                         console.error(error);
                         }
                         );
                         }
                         })*/
                    }

                    function cancelChangePassword() {
                        $uibModalInstance.dismiss('cancel');
                    }


                    vm.loginDTO = {
                        oldPassword: null,
                        newPassword: null
                    };

                    function submitChangePassword() {
                        vm.error.hasError = false;

                        if (validatePassword()) {
                            vm.loginDTO.oldPassword = vm.password.oldPassword;
                            vm.loginDTO.newPassword = vm.password.newPassword;
                            LoginService.changePassword(vm.loginDTO).then(
                                function (data) {
                                    performLogout();
                                },
                                function (error) {
                                    vm.error.errorMessage = error.message;
                                    vm.error.hasError = true;
                                }
                            )
                        }
                    };
                }
            ]
        );
    }
);