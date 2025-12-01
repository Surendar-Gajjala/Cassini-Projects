define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('ChangePasswordController', ChangePasswordController);

        function ChangePasswordController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                          $state, $stateParams, $cookies, LoginService, DialogService) {

            var vm = this;
            var parsed = angular.element("<div></div>");

            var userId = $stateParams.userId;
            var userPasswordMsg = parsed.html($translate.instant("CHANGE_PASSWORD_MESSAGE")).html();

            var userPasswordDialogTitle = parsed.html($translate.instant("RESET_PASSWORD")).html();
            var userPasswordDialogMessage = parsed.html($translate.instant("RESET_PASSWORD_DIALOG_MESSAGE")).html();


            vm.isLoginSameAsUser = false;
            vm.loading = true;
            vm.resetPassword = resetPassword;

            function resetPassword() {
                var options = {
                    title: userPasswordDialogTitle,
                    message: userPasswordDialogMessage,
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator();
                        LoginService.resetUserPassword(userId).then(
                            function (data) {
                                $rootScope.showSuccessMessage(passwordResetMsg);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            })
                    }
                });
            }


            var newPasswordValidation = parsed.html($translate.instant("NEW_PASSWORD_VALIDATION")).html();
            var notSamePasswordValidation = parsed.html($translate.instant("NOT_SAME_PASSWORD_VALIDATION")).html();
            var confirmPasswordValidation = parsed.html($translate.instant("CONFIRM_PASSWORD_VALID")).html();
            vm.passwordInformation = parsed.html($translate.instant("PASSWORD_INFORMATION")).html();

            vm.invalidPassword = parsed.html($translate.instant("INVALID_PASSWORD")).html();
            vm.invalidUsername = parsed.html($translate.instant("INVALID_USERNAME")).html();
            var passwordResetMsg = parsed.html($translate.instant("PASSWORD_RESET_MSG")).html();

            vm.userPassword = {
                confirmPassword: null,
                newPassword: null,
                email: null
            };

            vm.loginDto = {
                login: null,
                newPassword: null
            };
            vm.saveLoginPassword = saveLoginPassword;
            function saveLoginPassword() {
                $rootScope.passwordStrengthValid();
                if ($rootScope.validPassword && validatePassword()) {
                    $rootScope.showBusyIndicator();
                    vm.loginDto.newPassword = vm.userPassword.newPassword;
                    vm.loginDto.login = vm.login;
                    LoginService.passwordReset(vm.loginDto).then(
                        function () {
                            $rootScope.showSuccessMessage(userPasswordMsg);
                            $rootScope.hideBusyIndicator();
                            performLogout();
                        }, function (error) {
                            $rootScope.hideSidePanel();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }


            function validatePassword() {
                var valid = true;
                if (vm.userPassword.newPassword == null || vm.userPassword.newPassword == "") {
                    $rootScope.showWarningMessage(newPasswordValidation);
                    valid = false;
                } else if (vm.userPassword.confirmPassword == null || vm.userPassword.confirmPassword == "") {
                    $rootScope.showWarningMessage(confirmPasswordValidation);
                    valid = false;
                } else if (vm.userPassword.newPassword != vm.userPassword.confirmPassword) {
                    $rootScope.showWarningMessage(notSamePasswordValidation);
                    valid = false;

                }

                return valid;
            }


            vm.showPassword = showPassword;
            function showPassword() {
                var eyeIcon = document.getElementById("showPassword");
                var newPassword = document.getElementById("newPassword");
                if (eyeIcon.attributes.class.nodeValue == "fa fa-fw fa-eye-slash show-password") {
                    eyeIcon.title = parsed.html($translate.instant("HIDE_PASSWORD")).html();
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye show-password";
                } else {
                    eyeIcon.title = parsed.html($translate.instant("SHOW_PASSWORD")).html();
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye-slash show-password";
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
                if (eyeIcon.attributes.class.nodeValue == "fa fa-fw fa-eye-slash show-password") {
                    eyeIcon.title = parsed.html($translate.instant("HIDE_PASSWORD")).html();
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye show-password";
                } else {
                    eyeIcon.title = parsed.html($translate.instant("SHOW_PASSWORD")).html();
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye-slash show-password";
                }

                if (newPassword.attributes.type.nodeValue == "password") {
                    newPassword.attributes.type.nodeValue = "text";
                } else {
                    newPassword.attributes.type.nodeValue = "password";
                }
            }

            function performLogout() {
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
                }, 2000);
            }

            function loadLogin() {
                LoginService.getLoginByPerson(userId).then(
                    function (data) {
                        vm.login = data;

                        if($application.session.login.id === vm.login.id) {
                            vm.isLoginSameAsUser = true;
                        }
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadLogin();
            })();
        }
    }
);